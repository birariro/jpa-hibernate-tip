package com.example.jpatipsample.domain.support;

import com.example.jpatipsample.domain.PaymentCard;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;

import java.util.Optional;

@RequiredArgsConstructor
class PaymentCardsQueryImpl implements PaymentCardsQuery {

    private final EntityManager entityManager;

    @Override
    public Optional<PaymentCard> findByNumber(String paymentCardNumber) {
        return entityManager.unwrap(Session.class)
                .bySimpleNaturalId(PaymentCard.class)
                .loadOptional(paymentCardNumber);
    }
}
