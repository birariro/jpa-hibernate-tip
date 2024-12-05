package com.example.jpatipsample;

import com.example.jpatipsample.domain.PaymentCards;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest(
        properties = "spring.jpa.properties.hibernate.session.events.log.LOG_QUERIES_SLOWER_THAN_MS=1"
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class HibernateSlowQueryLogTest {

    @Autowired
    private PaymentCards paymentCards;

    @Test
    void should_print_slow_query() {
        paymentCards.findAll();
    }
}
