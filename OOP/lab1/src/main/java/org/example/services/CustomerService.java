package org.example.services;

import org.example.dao.CustomerDao;
import org.example.entities.Customer;
import org.example.entities.HomeTariff;
import org.example.entities.MobileTariff;

import java.util.List;

public class CustomerService {
    private final CustomerDao customerDao = new CustomerDao(Customer.class);

    public CustomerService() {

    }

    public Customer findById(long id) {
        return customerDao.findById(id);
    }

    public List<Customer> findAll() {
        return customerDao.findAll();
    }

    public int countAll() {
        return customerDao.countAll();
    }

    public void saveCustomer(Customer customer) {
        customerDao.save(customer);
    }

    public void updateCustomer(Customer customer) {
        customerDao.update(customer);
    }

    public void deleteCustomer(Customer customer) {
        customerDao.delete(customer);
    }

    public void addMobileTariff(Customer customer, MobileTariff mobileTariff) {
        customerDao.addMobileTariff(customer, mobileTariff);
    }

    public void removeMobileTariff(Customer customer, MobileTariff mobileTariff) {
        customerDao.removeMobileTariff(customer, mobileTariff);
    }

    public void addHomeTariff(Customer customer, HomeTariff homeTariff) {
        customerDao.addHomeTariff(customer, homeTariff);
    }

    public void removeHomeTariff(Customer customer, HomeTariff homeTariff) {
        customerDao.removeHomeTariff(customer, homeTariff);
    }
}
