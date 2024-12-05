package com.example.jpatipsample.domain.support;

import com.example.jpatipsample.domain.PaymentCard;

import java.util.List;
import java.util.Optional;

public interface PaymentCardsQuery {
    Optional<PaymentCard> findByNumber(String paymentCardNumber);

    void statelessDeleteAllData(List<Long> ids);
}