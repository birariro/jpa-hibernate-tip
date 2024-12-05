package com.example.jpatipsample.support;

import com.example.jpatipsample.domain.PaymentCard;
import com.example.jpatipsample.domain.PaymentCards;
import com.example.jpatipsample.utils.QueryAssertions;
import com.example.jpatipsample.utils.QueryAssertionsConfig;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryAssertionsConfig.class})
public class InQueryExecutor {

    List<Long> paymentCardIds;
    private PaymentCards paymentCards;
    private EntityManager entityManager;

    @Autowired
    public InQueryExecutor(PaymentCards paymentCards, EntityManager entityManager) {
        this.paymentCards = paymentCards;
        this.entityManager = entityManager;
    }


    public void init(int initSize) {

        List<PaymentCard> paymentCardList = new ArrayList<>();

        for (int i = 0; i < initSize; i++) {
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
    @DisplayName("in_clause_padding 을 설정하지 않는다면 쿼리가 재사용 되지 않는다")
    void should_noCache_notInClausePadding() {
        init(10);
        inQueryExecute(3, 3);
        inQueryExecute(10, 10);
    }

    public void inQueryExecute(int count, int expected) {

        QueryAssertions.assertThat(() -> {

            List ids = new ArrayList();
            for (int i = 0; i < count; i++) {
                ids.add(paymentCardIds.get(0));
                paymentCards.findByIdIn(ids);
            }

        }).isKindCountTo(expected);

    }
}