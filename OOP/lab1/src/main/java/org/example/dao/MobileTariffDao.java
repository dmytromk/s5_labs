package org.example.dao;

import org.example.entities.MobileTariff;
import org.example.filters.MobileTariffFilter;

public class MobileTariffDao extends TariffDao<MobileTariff, MobileTariffFilter> {

    public MobileTariffDao() {
        super(MobileTariff.class);
    }
}
