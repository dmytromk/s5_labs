package org.example.dao;

import org.example.entity.HomeTariff;
import org.example.entity.MobileTariff;
import org.example.filter.HomeTariffFilter;
import org.example.filter.MobileTariffFilter;
import org.example.sessions.TransactionManager;
import org.hibernate.query.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobileTariffDao extends AbstractDao<MobileTariff> {

    public MobileTariffDao(Class<MobileTariff> entityType) {
        super(entityType);
    }

    public List<MobileTariff> findAllByFilter(MobileTariffFilter filter) {
        StringBuilder builder = new StringBuilder()
                .append("FROM ")
                .append(entityType.getSimpleName())
                .append(" WHERE 1 = 1");

        HashMap<String, Object> parameters = filter.toMap();

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            builder.append(" AND ")
                    .append(entry.getKey());

            if (entry.getKey().contains("min")) {
                builder.append(" >= :");
            } else if (entry.getKey().contains("max")) {
                builder.append(" <= :");
            } else builder.append(" = :");

            builder.append(entry.getKey());
        }

        return TransactionManager.readTransaction(session -> {
            Query<MobileTariff> query = session.createQuery(builder.toString(), entityType);

            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }

            return query.list();
        });
    }
}
