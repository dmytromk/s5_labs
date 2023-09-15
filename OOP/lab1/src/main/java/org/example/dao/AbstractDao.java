package org.example.dao;

import org.example.sessions.TransactionManager;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Map;

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
                session.createQuery("FROM " + entityType.getSimpleName(), entityType).list());
    }

    public void save(T entity) {
        TransactionManager.commitTransaction(session -> session.persist(entity));
    }

    public void delete(T entity) {
        TransactionManager.commitTransaction(session -> session.remove(entity));
    }
}
