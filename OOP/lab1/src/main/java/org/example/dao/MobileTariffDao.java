package org.example.dao;

import org.example.entity.MobileTariff;
import org.hibernate.SessionFactory;

public class MobileTariffDao extends AbstractDao<MobileTariff> {

    public MobileTariffDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
