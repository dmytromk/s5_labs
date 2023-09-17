package org.example.services;

import org.example.dao.HomeTariffDao;
import org.example.entities.HomeTariff;
import org.example.filters.HomeTariffFilter;

import java.util.List;

public class HomeTariffService {
    private final HomeTariffDao homeTariffDao = new HomeTariffDao();


    public HomeTariffService() {

    }

    public HomeTariff findById(long id) {
        return this.homeTariffDao.findById(id);
    }

    public List<HomeTariff> findAll() {
        return this.homeTariffDao.findAll();
    }

    public List<HomeTariff> sortByPrice(){
        return this.homeTariffDao.sortAllByParameter("price_per_month");
    }

    public int countAll() {
        return this.homeTariffDao.countAll();
    }

    public void saveHomeTariff(HomeTariff homeTariff) {
        this.homeTariffDao.save(homeTariff);
    }

    public void updateHomeTariff(HomeTariff homeTariff) {
        this.homeTariffDao.update(homeTariff);
    }

    public void deleteHomeTariff(HomeTariff homeTariffFilter) {
        this.homeTariffDao.delete(homeTariffFilter);
    }

    public List<HomeTariff> findByFilter(HomeTariffFilter homeTariffFilter) {
        return this.homeTariffDao.findAllByFilter(homeTariffFilter);
    }
}
