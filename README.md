# JPA / Hibernate Tip

## Usage

```shell
docker compose -f compose.db.yml up --build -d
```

## 테스트코드에서 spring.jpa.hibernate.ddl-auto=validate 검증

Hibernate의 DDL 스키마를 검증하기 위해 테스트 코드에서`spring.jpa.hibernate.ddl-auto=validate`
하는것으로 엔티티 클래스와 데이터베이스 스키마 간의 불일치를 확인할 수 있다.

```java

@DataJpaTest(
        properties = "spring.jpa.hibernate.ddl-auto=validate"
)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class SchemaValidationTest {

    @Test
    public void testSchemaValidity() {
    }
}
```

샘플 코드 : https://github.com/birariro/jpa-tip-example/blob/main/src/test/java/com/example/jpatipsample/SchemaValidationTest.java

## 비지니스에서 나오는 자연키는 **NaturalId 를 사용**

JPA에서는 기본적으로 PK를 기준으로 1차 캐시를 활용한다

그렇기에 PK가 아닌 다른 필드로 접근시 1차캐시의 도움을 받지 못하게되는데

Hibernate를 사용한 프로젝트에서 비지니스 자연키가 존재할때

자연키를 사용하여 1차캐시를 활용 가능하도록 @NaturalId를 제공한다.

해당 기능은 Hibernate 의 기능이기 때문에

JPA EntityManger이 아닌 Hibernate Session을 사용해야한다.

자연키 필드에 @NaturalId를 부여하고

```java

@Entity
public class PaymentCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    @Column(name = "number", unique = true)
    private String number;

    @Column(name = "cvc")
    private String cvc;

    @Column(name = "expiryDate")
    private String expiryDate;
}
```

Hibernate Session을 사용하여 조회하는 메소드를 작성한다.

```java

@RequiredArgsConstructor
class PaymentCardsQueryImpl implements PaymentCardsQuery {

    private final EntityManager entityManager;

    @Override
    public Optional<PaymentCard> findByNumber(String paymentCardNumber) {
        return entityManager.unwrap(Session.class)
                .bySimpleNaturalId(PaymentCard.class)
                .loadOptional(paymentCardNumber);
    }
}
```

샘플 코드: https://github.com/birariro/jpa-tip-example/blob/main/src/test/java/com/example/jpatipsample/HibernateNaturalIdCacheTest.java

## **DynamicInsert & DynamicUpdate**

DynamicInsert, DynamicUpdate 는 Hibernate가 지원하는 동적 쿼리 기능이다

기존에는 SQL 쿼리를 캐싱해두고 모든 필드에 대해 파라미터를 매핑시키는것으로 최적화를 하였지만

해당 어노테이션을 사용하여

엔티티를 업데이트할때 변경된 필드만 가지고 SQL 구문을 생성하거나

엔티티를 추가할때 null 이 아닌 필드만 가지고 SQL구문을 생성한다.

Hibernate 변경된 컬럼만을 가지고 SQL 구문을 생성하야하기에 엔티티 필드 수준의 추적이필요하게되며

그로인해 성능 오버헤드가 발생하게된다

따라서 이 기능은 항상 사용하는것이 아닌 필요한 경우에만 사용해야하게되는데

1. 엔티티가 많은 컬럼을 가진경우
2. 컬럼 수준의 lock 을 사용하는경우
3. 컬럼 수준의 버전을 사용하는경우
4. 다양한 컬럼에서 동시성 이슈가 발생 가능한경우

사용하는법은 엔티티에 어노테이션을 추가만 하면 된다.

```java

@DynamicUpdate
@DynamicInsert
public class PaymentCard {
}
```

```java
//적용 전
update PaymentCard
        set cvc=?,expiry_date=?,owner_id=?
        where id=?

//적용 후        
        update PaymentCard
        set cvc=?
        whereid=?

//적용 전
        insert into tb_payment(amount,create_at,installment_month,payment_card_id,vat)
        values(?,?,?,?,?)

//적용 후        
        insert into tb_payment_card(number,owner_id)
        values(?,?)

```

샘플 코드: https://github.com/birariro/jpa-tip-example/blob/main/src/test/java/com/example/jpatipsample/HibernateDynamicUpdateTest.java

### 대규모 작업시 StatelessSession 으로 처리

기존에 하이버네이트가 사용하던 StatefulSession 은

SessionFactory를 통해 생성되며 1차캐시,더티채킹, AutoFlush를 원하는 트랜젝션에 적합한 방식이다

StatelessSession은 SessionFactory를 통해 생성되며 1차캐시 및 2차캐시, 쿼리캐시를 지원하지않는다

더티채킹을 하지않으며 Lazy로딩을 지원하지않고

하이버네이트의 이벤트 모델과 인터셉터가 동작하지않는다

따라서 더티채킹, Lazy loading, 캐시 등이 필요하지 않는 Read-Only 혹은 Batch Processing에서 StatfulSession 보다 성능을 챙길수있고 오버헤드를 줄이는데 도움이 된다
```java

public void method() {

    StatelessSession statelessSession = null;
    Transaction transaction = null;

    try {

        statelessSession = entityManager.getEntityManagerFactory()
                .unwrap(SessionFactory.class)
                .openStatelessSession();

        transaction = statelessSession.beginTransaction();
        String uniqueValue = UUID.randomUUID() + "_" + System.nanoTime();
        
        MutationQuery query = statelessSession.createMutationQuery(
                "UPDATE PaymentCard c " +
                        "SET c.ownerId = :uuid, c.number = :uuid, c.cvc = NULL, c.expiryDate = NULL "
                        );
        query.setParameter("uuid", uniqueValue);
        query.executeUpdate();
        
        transaction.commit();

    } catch (Exception e) {
        if (transaction != null) {
            transaction.rollback();
        }
        e.printStackTrace();
    } finally {
        if (statelessSession != null) {
            statelessSession.close();
        }
    }
}
```
샘플 코드: https://github.com/birariro/jpa-tip-example/blob/main/src/test/java/com/example/jpatipsample/HibernateStatelessSessionTest.java

## hibernate의 parameter padding 로 in 쿼리 최적화

in 쿼리 사용시 파라미터수에 따라 쿼리를 생성하여 실행 하기에

in 쿼리의 파라미터가 3,4,5,6개일경우 총 4개의 쿼리를 생성하게된다.

Hibernate에서는 패딩을 사용하여 in clause 에 대해 execution plan 을 재사용 할수있도록 지원한다.

`in_clause_parameter_padding` 옵션을 활성화 하면

2의 거듭제곱 단위(1, 2, 4, 8, 16, 32, 64, 127)로 패딩 처리하게되어

3,4,5,6 파라미터 4개의 쿼리가 2개의 쿼리로 줄어들게 된다.

```yaml
spring.jpa.properties.hibernate.query.in_clause_parameter_padding=true
spring.jpa.properties.hibernate.query.plan_cache_max_size: 2048
```

샘플 코드: https://github.com/birariro/jpa-tip-example/blob/main/src/test/java/com/example/jpatipsample/HibernateInClauseParameterPaddingTest.java

## Slow쿼리 로그 활성화

hibernate의 옵션을 사용하여 slow 쿼리에 대해 로그를 남기도록 지정 가능하다

```yaml
spring.jpa.properties.hibernate.session.events.log.LOG_QUERIES_SLOWER_THAN_MS=1
```

위와같이 설정하면

```yaml
Slow query took 4 milliseconds [select pc1_0.id,pc1_0.create_at,pc1_0.cvc,pc1_0.expiry_date,pc1_0.number,pc1_0.owner_id from tb_payment_card pc1_0]
```

어떤 쿼리를 실행시 발생했는지 로그로 출력한다.

샘플 코드: https://github.com/birariro/jpa-tip-example/blob/main/src/test/java/com/example/jpatipsample/HibernateSlowQueryLogTest.java