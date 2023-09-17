package org.example.dao;

import org.example.sessions.TransactionManager;
import org.hibernate.query.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractDao<T> {
    protected final Class<T> entityType;

    public AbstractDao(Class<T> entityType) {
        this.entityType = entityType;
    }

    public T findById(long id) {
        return TransactionManager.readTransaction(session -> session.get(this.entityType, id));
    }

    public List<T> findAll() {
        return TransactionManager.readTransaction(session ->
                session.createQuery("FROM " + this.entityType.getSimpleName(),
                        this.entityType).list());
    }

    public Long countAll() {
        return TransactionManager.readTransactionCount(session ->
                session.createQuery("SELECT COUNT(c) FROM " + this.entityType.getSimpleName() + " c",
                        Long.class).getSingleResult());
    }

    public List<T> sortAllByParameter(String parameter) {
        StringBuilder builder = new StringBuilder()
                .append("FROM ")
                .append(this.entityType.getSimpleName())
                .append( " ORDER BY ")
                .append(parameter);

        return TransactionManager.readTransaction(session ->
                session.createQuery(builder.toString(),
                        this.entityType).list());
    }

    public List<T> findAllByParameters(HashMap<String, Object> parameters) {
        StringBuilder builder = new StringBuilder()
                .append("FROM ")
                .append(this.entityType.getSimpleName())
                .append( " WHERE 1 = 1");

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            builder.append(" AND ")
                    .append(entry.getKey())
                    .append(" = :")
                    .append(entry.getKey());
        }

        return TransactionManager.readTransaction(session -> {
            Query<T> query = session.createQuery(builder.toString(), this.entityType);

            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }

            return query.list();
        });
    }

    public void save(T entity) {
        TransactionManager.commitTransaction(session -> session.persist(entity));
    }

    public void delete(T entity) {
        TransactionManager.commitTransaction(session -> session.remove(entity));
    }

    public void update(T entity) {
        TransactionManager.commitTransaction(session -> session.merge(entity));
    }
}
