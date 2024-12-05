package com.example.jpatipsample.domain;

import com.example.jpatipsample.domain.support.PaymentCardsQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentCards extends JpaRepository<PaymentCard, Long>, PaymentCardsQuery {
    Optional<PaymentCard> findByOwnerId(String ownerId);

    List<PaymentCard> findByIdIn(List<Long> ids);

    @Modifying
    @Query("UPDATE PaymentCard c " +
            "SET c.ownerId = :uuid, c.number = :uuid, c.cvc = NULL, c.expiryDate = NULL " +
            "WHERE c.id = :id")
    void deleteAllData(@Param("uuid") String uuid, @Param("id") Long id);

    default void deleteAllData(List<Long> ids) {
        for (long id : ids) {
            String uniqueValue = UUID.randomUUID() + "_" + System.nanoTime();
            deleteAllData(uniqueValue, id);
        }
    }
}
