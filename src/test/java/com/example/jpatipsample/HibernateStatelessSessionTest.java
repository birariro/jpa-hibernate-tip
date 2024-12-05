package com.example.jpatipsample;

import com.example.jpatipsample.domain.PaymentCard;
import com.example.jpatipsample.domain.PaymentCards;
import com.example.jpatipsample.support.IdGenerator;
import com.example.jpatipsample.utils.QueryAssertions;
import com.example.jpatipsample.utils.QueryAssertionsConfig;
import com.example.jpatipsample.utils.support.QueryAssertion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryAssertionsConfig.class})
class HibernateStatelessSessionTest {

    List<Long> paymentCardIds;
    @Autowired
    private PaymentCards paymentCards;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void init() {

        List<PaymentCard> paymentCardList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            String id = IdGenerator.generateULID();
            PaymentCard paymentCard = paymentCards.save(PaymentCard.of(
                    id,
                    id,
                    "401",
                    "0131"
            ));
            paymentCardList.add(paymentCard);
        }
        entityManager.clear();
        this.paymentCardIds = paymentCardList.stream().map(PaymentCard::getId).toList();
    }


    @Test
    @DisplayName("statelessSession 이 stateFulSession 보다 실행 속도가 빠르다")
    void fastStatelessQueryThenStateFul() {

        QueryAssertion queryAssertion = QueryAssertions.assertThat(() -> {
            paymentCards.deleteAllData(paymentCardIds);
        });

        QueryAssertion statelessQueryAssertion = QueryAssertions.assertThat(() -> {
            paymentCards.statelessDeleteAllData(paymentCardIds);
        });

        statelessQueryAssertion.isFast(queryAssertion);
    }
}


