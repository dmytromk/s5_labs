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
        return mobileTariffDao.findById(id);
    }

    public List<MobileTariff> findAll() {
        return mobileTariffDao.findAll();
    }

    public List<MobileTariff> sortByPrice(){
        return  mobileTariffDao.sortAllByParameter("price_per_month");
    }

    public int countAll() {
        return mobileTariffDao.countAll();
    }


    public void saveMobileTariff(MobileTariff mobileTariff) {
        mobileTariffDao.save(mobileTariff);
    }

    public void updateMobileTariff(MobileTariff mobileTariff) {
        mobileTariffDao.update(mobileTariff);
    }

    public void deleteMobileTariff(MobileTariff mobileTariff) {
        mobileTariffDao.delete(mobileTariff);
    }

    public List<MobileTariff> findByFilter(MobileTariffFilter filter) {
        return mobileTariffDao.findAllByFilter(filter);
    }
}
