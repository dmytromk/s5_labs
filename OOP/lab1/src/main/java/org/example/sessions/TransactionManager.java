package org.example.sessions;

import org.example.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;

import java.util.function.Consumer;
import java.util.function.Function;

public class TransactionManager {
    private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public static <T> T readTransaction(Function<Session, T> transactionOperation) {
        try (Session session = sessionFactory.openSession()) {
            return transactionOperation.apply(session);
        }
    }

    public static void commitTransaction(Consumer<Session> transactionOperation) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                transactionOperation.accept(session);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                throw new RuntimeException("Transaction operation failed: " + e.getMessage(), e);
            }
        }
    }
}
