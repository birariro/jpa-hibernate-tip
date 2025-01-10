package com.example.jpahibernatetip.domain;

import com.example.jpahibernatetip.domain.support.PaymentCardsQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentCards extends JpaRepository<PaymentCard, Long>, PaymentCardsQuery {
    Optional<PaymentCard> findByOwnerId(String ownerId);

    List<PaymentCard> findByIdIn(List<Long> ids);
}
