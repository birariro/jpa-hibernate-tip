package com.example.jpahibernatetip.domain.support;

import com.example.jpahibernatetip.domain.PaymentCard;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
class PaymentCardsQueryImpl implements PaymentCardsQuery {

    private final EntityManager entityManager;

    @Override
    public Optional<PaymentCard> findByNumber(String paymentCardNumber) {
        return entityManager.unwrap(Session.class)
                .bySimpleNaturalId(PaymentCard.class)
                .loadOptional(paymentCardNumber);
    }

    public void statelessDeleteAllData(List<Long> ids) {

        StatelessSession statelessSession = null;
        Transaction transaction = null;

        try {

            statelessSession = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class)
                    .openStatelessSession();

            transaction = statelessSession.beginTransaction();
            for (long id : ids) {
                String uniqueValue = UUID.randomUUID() + "_" + System.nanoTime();
                MutationQuery query = statelessSession.createMutationQuery(
                        "UPDATE PaymentCard c " +
                                "SET c.ownerId = :uuid, c.number = :uuid, c.cvc = NULL, c.expiryDate = NULL " +
                                "WHERE c.id = :id");
                query.setParameter("uuid", uniqueValue);
                query.setParameter("id", id);

                query.executeUpdate();
            }

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (statelessSession != null) {
                statelessSession.close();
            }
        }
    }
}
