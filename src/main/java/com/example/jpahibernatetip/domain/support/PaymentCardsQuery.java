package com.example.jpahibernatetip.domain.support;

import com.example.jpahibernatetip.domain.PaymentCard;

import java.util.List;
import java.util.Optional;

public interface PaymentCardsQuery {
    Optional<PaymentCard> findByNumber(String paymentCardNumber);

    void statelessSaveAll(List<PaymentCard> paymentCards);
}
