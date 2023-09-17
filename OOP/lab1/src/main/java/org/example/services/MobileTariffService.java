package org.example.services;

import org.example.dao.MobileTariffDao;
import org.example.entities.MobileTariff;
import org.example.filters.MobileTariffFilter;

import java.util.List;

public class MobileTariffService {
    private final MobileTariffDao mobileTariffDao = new MobileTariffDao(MobileTariff.class);
    private final MobileTariffFilter mobileTariffFilter = new MobileTariffFilter();

    public MobileTariffService() {

    }

    public MobileTariff findById(long id) {
        return this.mobileTariffDao.findById(id);
    }

    public List<MobileTariff> findAll() {
        return this.mobileTariffDao.findAll();
    }

    public List<MobileTariff> sortByPrice(){
        return this.mobileTariffDao.sortAllByParameter("price_per_month");
    }

    public int countAll() {
        return this.mobileTariffDao.countAll();
    }


    public void saveMobileTariff(MobileTariff mobileTariff) {
        this.mobileTariffDao.save(mobileTariff);
    }

    public void updateMobileTariff(MobileTariff mobileTariff) {
        this.mobileTariffDao.update(mobileTariff);
    }

    public void deleteMobileTariff(MobileTariff mobileTariff) {
        this.mobileTariffDao.delete(mobileTariff);
    }

    public List<MobileTariff> findByFilter(MobileTariffFilter filter) {
        return this.mobileTariffDao.findAllByFilter(filter);
    }
}
