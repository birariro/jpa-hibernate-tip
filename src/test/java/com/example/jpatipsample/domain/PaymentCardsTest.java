package com.example.jpatipsample.domain;

import com.example.jpatipsample.utils.QueryAssertions;
import com.example.jpatipsample.utils.QueryAssertionsConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryAssertionsConfig.class})
class PaymentCardsTest {
    @Autowired
    private PaymentCards paymentCards;
    @PersistenceContext
    private EntityManager entityManager;

    private PaymentCard paymentCard;

    @BeforeEach
    void init() {
        this.paymentCard = paymentCards.save(PaymentCard.of(
                "986476729861",
                "8955477843979741",
                "401",
                "0131"
        ));
        entityManager.clear();
    }

    @Test
    @DisplayName("같은 pk 로 반복 조회시 EntityManger 에 캐싱")
    void should_onlyOnceAction_by_EntityMangerCache() {

        QueryAssertions.assertThat(() -> {
            paymentCards.findById(paymentCard.getId());
            paymentCards.findById(paymentCard.getId());
            paymentCards.findById(paymentCard.getId());
        }).isCountTo(1);
    }

    @Test
    @DisplayName("pk를 사용한 조회가 아니라면 EntityManger 는 캐싱 하지 않는다")
    void should_notOnlyOnceAction_by_EntityMangerCacheOnlyWorkPK() {

        QueryAssertions.assertThat(() -> {
            PaymentCard card = paymentCards.findById(paymentCard.getId()).orElseThrow();
            paymentCards.findByOwnerId(card.getOwnerId());
            paymentCards.findByOwnerId(card.getOwnerId());
            paymentCards.findByOwnerId(card.getOwnerId());
        }).isCountTo(4);
    }

    @Test
    @DisplayName("같은 NaturalId 로 반복 조회시 Hibernate 에 의해 캐싱 한다")
    void should_onlyOnceAction_by_HibernateCacheWorkNaturalId() {

        QueryAssertions.assertThat(() -> {
            PaymentCard card = paymentCards.findById(paymentCard.getId()).orElseThrow();
            paymentCards.findByNumber(card.getNumber());
            paymentCards.findByNumber(card.getNumber());
            paymentCards.findByNumber(card.getNumber());
        }).isCountTo(1);
    }

}