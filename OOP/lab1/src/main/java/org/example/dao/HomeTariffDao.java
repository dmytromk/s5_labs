package org.example.dao;

import org.example.entity.HomeTariff;
import org.example.filter.HomeTariffFilter;
import org.example.sessions.TransactionManager;
import org.hibernate.query.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeTariffDao extends AbstractDao<HomeTariff> {

    public HomeTariffDao(Class<HomeTariff> entityType) {
        super(entityType);
    }

    public List<HomeTariff> findAllByFilter(HomeTariffFilter filter) {
        StringBuilder builder = new StringBuilder()
                .append("FROM ")
                .append(entityType.getSimpleName())
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
            Query<HomeTariff> query = session.createQuery(builder.toString(), entityType);

            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }

            return query.list();
        });
    }
}
