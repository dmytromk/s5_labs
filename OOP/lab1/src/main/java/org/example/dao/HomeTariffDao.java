package org.example.dao;

import org.example.entity.HomeTariff;
import org.hibernate.SessionFactory;

public class HomeTariffDao extends AbstractDao<HomeTariff> {

    public HomeTariffDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

}
