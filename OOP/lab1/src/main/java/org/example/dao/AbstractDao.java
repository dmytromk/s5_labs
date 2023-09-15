package org.example.dao;

import org.example.sessions.TransactionManager;

import java.util.List;

public abstract class AbstractDao<T> {
    protected final Class<T> entityType;

    public AbstractDao(Class<T> entityType) {
        this.entityType = entityType;
    }

    public T findById(long id) {
        return TransactionManager.readTransaction(session -> session.get(entityType, id));
    }

    public List<T> findAll() {
        return TransactionManager.readTransaction(session ->
                session.createQuery("SELECT * FROM " + entityType.getSimpleName(), entityType).list());
    }

    public int countAll() {
        return TransactionManager.readTransactionCount(session ->
                session.createQuery("SELECT COUNT(*) " + entityType.getSimpleName(), entityType).getFirstResult());
    }

    public List<T> filterAllByParameter(String parameter) {
        StringBuilder builder = new StringBuilder()
                .append("FROM ")
                .append(entityType.getSimpleName())
                .append( " ORDER BY ")
                .append(parameter);

        return TransactionManager.readTransaction(session ->
                session.createQuery(builder.toString(),
                        entityType).list());
    }

    public void save(T entity) {
        TransactionManager.commitTransaction(session -> session.persist(entity));
    }

    public void delete(T entity) {
        TransactionManager.commitTransaction(session -> session.remove(entity));
    }
}
