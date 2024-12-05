package com.example.jpatipsample.domain;

import com.example.jpatipsample.domain.support.PaymentCardsQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentCards extends JpaRepository<PaymentCard, Long>, PaymentCardsQuery {
    Optional<PaymentCard> findByOwnerId(String ownerId);
}
