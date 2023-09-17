package org.example.dao;

import org.example.entities.HomeTariff;
import org.example.filters.HomeTariffFilter;

public class HomeTariffDao extends TariffDao<HomeTariff, HomeTariffFilter> {

    public HomeTariffDao() {
        super(HomeTariff.class);
    }
}
