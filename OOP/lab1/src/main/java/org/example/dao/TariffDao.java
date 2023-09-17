package org.example.dao;

import org.example.entities.Tariff;
import org.example.filters.Filter;
import org.example.sessions.TransactionManager;
import org.hibernate.query.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TariffDao<T extends Tariff, F extends Filter> extends AbstractDao<T>{
    public TariffDao(Class<T> entityType) {
        super(entityType);
    }

    public List<T> findAllByFilter(F filter) {
        StringBuilder builder = new StringBuilder()
                .append("FROM ")
                .append(this.entityType.getSimpleName())
                .append( " WHERE 1 = 1");

        HashMap<String, Object> parameters = filter.toMap();

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            builder.append(" AND ")
                    .append(entry.getKey());

            if (entry.getKey().contains("min")){
                builder.append(" >= :");
            }

            else if (entry.getKey().contains("max")) {
                builder.append(" <= :");
            }

            else builder.append(" = :");

            builder.append(entry.getKey());
        }

        return TransactionManager.readTransaction(session -> {
            Query<T> query = session.createQuery(builder.toString(), this.entityType);

            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }

            return query.list();
        });
    }
}
