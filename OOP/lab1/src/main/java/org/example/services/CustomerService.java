package org.example.services;

import org.example.dao.CustomerDao;
import org.example.entities.Customer;
import org.example.entities.HomeTariff;
import org.example.entities.MobileTariff;

import java.util.List;

public class CustomerService {
    private final CustomerDao customerDao = new CustomerDao();

    public CustomerService() {

    }

    public Customer findById(long id) {
        return this.customerDao.findById(id);
    }

    public List<Customer> findAll() {
        return this.customerDao.findAll();
    }

    public int countAll() {
        return this.customerDao.countAll();
    }

    public void saveCustomer(Customer customer) {
        this.customerDao.save(customer);
    }

    public void updateCustomer(Customer customer) {
        this.customerDao.update(customer);
    }

    public void deleteCustomer(Customer customer) {
        this.customerDao.delete(customer);
    }

    public void addMobileTariff(Customer customer, MobileTariff mobileTariff) {
        this.customerDao.addMobileTariff(customer, mobileTariff);
    }

    public void removeMobileTariff(Customer customer, MobileTariff mobileTariff) {
        this.customerDao.removeMobileTariff(customer, mobileTariff);
    }

    public void addHomeTariff(Customer customer, HomeTariff homeTariff) {
        this.customerDao.addHomeTariff(customer, homeTariff);
    }

    public void removeHomeTariff(Customer customer, HomeTariff homeTariff) {
        this.customerDao.removeHomeTariff(customer, homeTariff);
    }
}
