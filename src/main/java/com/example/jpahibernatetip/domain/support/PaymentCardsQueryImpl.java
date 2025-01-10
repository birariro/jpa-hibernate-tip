package com.example.jpahibernatetip.domain.support;

import com.example.jpahibernatetip.domain.PaymentCard;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;

import java.util.List;
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

    public void statelessSaveAll(List<PaymentCard> paymentCards) {
        StatelessSession statelessSession = entityManager.getEntityManagerFactory()
                .unwrap(SessionFactory.class)
                .openStatelessSession();

        Transaction tx = statelessSession.beginTransaction();
        try {
            for (PaymentCard card : paymentCards) {
                statelessSession.insert(card);
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            statelessSession.close();
        }
    }
}
