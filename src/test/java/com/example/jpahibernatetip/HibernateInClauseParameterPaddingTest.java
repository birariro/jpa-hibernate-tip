package com.example.jpahibernatetip;

import com.example.jpahibernatetip.domain.PaymentCards;
import com.example.jpahibernatetip.support.InQueryExecutor;
import com.example.jpahibernatetip.utils.QueryAssertionsConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest(
        properties = {
                "spring.jpa.properties.hibernate.query.in_clause_parameter_padding=true",
                "spring.jpa.properties.hibernate.query.plan_cache_max_size=2048"
        }
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryAssertionsConfig.class})
class HibernateInClauseParameterPaddingTest {

    private final InQueryExecutor inQueryExecutor;

    @Autowired
    public HibernateInClauseParameterPaddingTest(PaymentCards paymentCards) {
        inQueryExecutor = new InQueryExecutor(paymentCards);
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


