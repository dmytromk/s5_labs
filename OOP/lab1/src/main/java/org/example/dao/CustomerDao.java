package org.example.dao;

import org.example.entities.Customer;
import org.example.entities.HomeTariff;
import org.example.entities.MobileTariff;
import org.example.sessions.TransactionManager;

public class CustomerDao extends AbstractDao<Customer> {

    public CustomerDao() {
        super(Customer.class);
    }

    public void addMobileTariff(Customer customer, MobileTariff mobileTariff) {
        customer.addMobileTariff(mobileTariff);
        TransactionManager.commitTransaction(session ->
                session.merge(customer));
    }

    public void removeMobileTariff(Customer customer, MobileTariff mobileTariff) {
        customer.removeMobileTariff(mobileTariff);
        TransactionManager.commitTransaction(session ->
                session.merge(customer));
    }

    public void addHomeTariff(Customer customer, HomeTariff homeTariff) {
        customer.addHomeTariff(homeTariff);
        TransactionManager.commitTransaction(session ->
                session.merge(customer));
    }

    public void removeHomeTariff(Customer customer, HomeTariff homeTariff) {
        customer.removeHomeTariff(homeTariff);
        TransactionManager.commitTransaction(session ->
                session.merge(customer));
    }

}
