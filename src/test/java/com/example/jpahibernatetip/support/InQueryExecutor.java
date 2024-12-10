package com.example.jpahibernatetip.support;

import com.example.jpahibernatetip.domain.PaymentCards;
import com.example.jpahibernatetip.utils.QueryAssertions;
import com.example.jpahibernatetip.utils.QueryAssertionsConfig;
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

    private PaymentCards paymentCards;

    @Autowired
    public InQueryExecutor(PaymentCards paymentCards) {
        this.paymentCards = paymentCards;
    }


    @Test
    @DisplayName("in_clause_padding 을 설정하지 않는다면 쿼리가 재사용 되지 않는다")
    void should_noCache_notInClausePadding() {
        inQueryExecute(3, 3);
        inQueryExecute(10, 10);
    }

    public void inQueryExecute(int count, int expected) {

        QueryAssertions.assertThat(() -> {

            List ids = new ArrayList();
            for (int i = 0; i < count; i++) {
                ids.add(i);
                paymentCards.findByIdIn(ids);
            }

        }).isKindCountTo(expected);
    }
}