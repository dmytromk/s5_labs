package org.example.services;

import org.example.dao.HomeTariffDao;
import org.example.entities.HomeTariff;
import org.example.entities.MobileTariff;
import org.example.filters.HomeTariffFilter;

import java.util.List;

public class HomeTariffService {
    private final HomeTariffDao homeTariffDao = new HomeTariffDao(HomeTariff.class);
    private final HomeTariffFilter homeTariffFilter = new HomeTariffFilter();

    public HomeTariffService() {

    }

    public HomeTariff findById(long id) {
        return homeTariffDao.findById(id);
    }

    public List<HomeTariff> findAll() {
        return homeTariffDao.findAll();
    }

    public List<HomeTariff> sortByPrice(){
        return  homeTariffDao.sortAllByParameter("price_per_month");
    }

    public int countAll() {
        return homeTariffDao.countAll();
    }

    public void saveHomeTariff(HomeTariff homeTariff) {
        homeTariffDao.save(homeTariff);
    }

    public void updateHomeTariff(HomeTariff homeTariff) {
        homeTariffDao.update(homeTariff);
    }

    public void deleteHomeTariff(HomeTariff homeTariffFilter) {
        homeTariffDao.delete(homeTariffFilter);
    }

    public List<HomeTariff> findByFilter(HomeTariffFilter homeTariffFilter) {
        return homeTariffDao.findAllByFilter(homeTariffFilter);
    }
}
