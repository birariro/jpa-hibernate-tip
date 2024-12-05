package com.example.jpatipsample;

import com.example.jpatipsample.domain.PaymentCards;
import com.example.jpatipsample.support.InQueryExecutor;
import com.example.jpatipsample.utils.QueryAssertionsConfig;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryAssertionsConfig.class})
@ActiveProfiles("in_clause_padding")
class HibernateInClauseParameterPaddingTest {

    private final InQueryExecutor inQueryExecutor;

    @Autowired
    public HibernateInClauseParameterPaddingTest(PaymentCards paymentCards, EntityManager entityManager) {
        inQueryExecutor = new InQueryExecutor(paymentCards, entityManager);
        inQueryExecutor.init(50);
    }

    @Test
    @DisplayName("in_clause_padding 을 설정한다면 않는다면 2의 제곱으로 재사용 한다.")
    void should_cache_inClausePadding() {
        //1, 2, 4, 8, 16, 32, 64, 127
        inQueryExecutor.inQueryExecute(3, 3);
        inQueryExecutor.inQueryExecute(10, 5);
        inQueryExecutor.inQueryExecute(50, 7);
    }
}


