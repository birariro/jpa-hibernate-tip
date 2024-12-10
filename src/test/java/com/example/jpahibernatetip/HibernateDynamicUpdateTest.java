package com.example.jpahibernatetip;

import com.example.jpahibernatetip.domain.Amount;
import com.example.jpahibernatetip.domain.Payment;
import com.example.jpahibernatetip.domain.PaymentCard;
import com.example.jpahibernatetip.domain.PaymentCards;
import com.example.jpahibernatetip.domain.Payments;
import com.example.jpahibernatetip.domain.VAT;
import com.example.jpahibernatetip.utils.QueryAssertions;
import com.example.jpahibernatetip.utils.QueryAssertionsConfig;
import com.example.jpahibernatetip.utils.support.QueryAssertion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
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
class HibernateDynamicUpdateTest {

    @Autowired
    private PaymentCards paymentCards;
    @Autowired
    private Payments payments;

    @PersistenceContext
    private EntityManager entityManager;

    private PaymentCard paymentCard;
    private Payment payment;

    @BeforeEach
    void init() {

        this.paymentCard = paymentCards.save(PaymentCard.of(
                "986476729861",
                "8955477843979741",
                "401",
                "0131"
        ));

        this.payment = payments.save(Payment.of(this.paymentCard, Amount.of(1000), VAT.of(10), 10));
        entityManager.clear();
    }

    @Test
    @DisplayName("일반적인 업데이트는 모든 필드 업데이트, Dynamic Update 는 변경된 필드만 업데이트")
    @Transactional
    void dynamic_update_query() {

        /**
         * common update query
         */
        Payment payment = payments.findById(this.payment.getId()).orElseThrow();
        QueryAssertion queryAssertion = QueryAssertions.assertThat(() -> {
            payment.setAmount(Amount.of(2000));
            entityManager.flush();
        });
        String updateQuery = queryAssertion.getQueryStorage().getQueryStrings().get(0);
        assertThat(updateQuery).isEqualTo("update tb_payment set amount=?,installment_month=?,payment_card_id=?,vat=? where id=?");

        /**
         * dynamic update query
         */
        PaymentCard paymentCard = paymentCards.findById(this.paymentCard.getId()).orElseThrow();
        QueryAssertion dynamicQueryAssertion = QueryAssertions.assertThat(() -> {
            paymentCard.setCvc("1234");
            entityManager.flush();
        });
        String dynamicUpdateQuery = dynamicQueryAssertion.getQueryStorage().getQueryStrings().get(0);
        assertThat(dynamicUpdateQuery).isEqualTo("update tb_payment_card set cvc=? where id=?");
    }
}


