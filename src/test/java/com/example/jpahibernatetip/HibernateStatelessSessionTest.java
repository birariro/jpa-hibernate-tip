package com.example.jpahibernatetip;

import com.example.jpahibernatetip.domain.PaymentCard;
import com.example.jpahibernatetip.domain.PaymentCards;
import com.example.jpahibernatetip.support.IdGenerator;
import com.example.jpahibernatetip.utils.QueryAssertions;
import com.example.jpahibernatetip.utils.QueryAssertionsConfig;
import com.example.jpahibernatetip.utils.support.QueryAssertion;
import org.junit.jupiter.api.BeforeEach;
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
class HibernateStatelessSessionTest {

    @Autowired
    private PaymentCards paymentCards;
    private List<PaymentCard> paymentCardList;
    private List<PaymentCard> paymentCardList2;

    @BeforeEach
    void init() {

        this.paymentCardList = new ArrayList<>();
        this.paymentCardList2 = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            String id = IdGenerator.generateULID();
            String id2 = IdGenerator.generateULID();
            paymentCardList.add(PaymentCard.of(
                    id,
                    id,
                    "401",
                    "0131"
            ));
            paymentCardList2.add(PaymentCard.of(
                    id2,
                    id2,
                    "101",
                    "0101"
            ));

        }
    }


    @Test
    @DisplayName("statelessSession 이 stateFulSession 보다 실행 속도가 빠르다")
    void fastStatelessQueryThenStateFul() {

        QueryAssertion statelessQueryAssertion = QueryAssertions.assertThat(() -> {
            paymentCards.statelessSaveAll(paymentCardList2);
        });
        QueryAssertion queryAssertion = QueryAssertions.assertThat(() -> {
            paymentCards.saveAll(paymentCardList);
        });
        
        statelessQueryAssertion.isFast(queryAssertion);
    }
}


