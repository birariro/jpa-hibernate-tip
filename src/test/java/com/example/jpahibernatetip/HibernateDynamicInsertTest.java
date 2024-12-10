package com.example.jpahibernatetip;

import com.example.jpahibernatetip.domain.Payment;
import com.example.jpahibernatetip.domain.PaymentCard;
import com.example.jpahibernatetip.domain.PaymentCards;
import com.example.jpahibernatetip.domain.Payments;
import com.example.jpahibernatetip.utils.QueryAssertions;
import com.example.jpahibernatetip.utils.QueryAssertionsConfig;
import com.example.jpahibernatetip.utils.support.QueryAssertion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryAssertionsConfig.class})
class HibernateDynamicInsertTest {

    @Autowired
    private PaymentCards paymentCards;
    @Autowired
    private Payments payments;

    @PersistenceContext
    private EntityManager entityManager;


    @Test
    @DisplayName("일반적인 insert는 모든 필드를 insert, Dynamic insert 는 필요한 필드만 insert.")
    @Transactional
    void dynamic_insert_query() {

        /**
         * common insert query
         */
        QueryAssertion queryAssertion = QueryAssertions.assertThat(() -> {
            payments.save(Payment.of(0L, null, null, 0));
            entityManager.flush();
        });
        String updateQuery = queryAssertion.getQueryStorage().getQueryStrings().get(0);
        assertThat(updateQuery).isEqualTo("insert into tb_payment (amount,create_at,installment_month,payment_card_id,vat) values (?,?,?,?,?)");


        /**
         * dynamic insert query
         */
        QueryAssertion dynamicQueryAssertion = QueryAssertions.assertThat(() -> {

            paymentCards.save(PaymentCard.of(
                    "986476729861",
                    "8955477843979741",
                    null,
                    null
            ));
            entityManager.flush();
        });
        String dynamicUpdateQuery = dynamicQueryAssertion.getQueryStorage().getQueryStrings().get(0);
        assertThat(dynamicUpdateQuery).isEqualTo("insert into tb_payment_card (number,owner_id) values (?,?)");
    }
}


