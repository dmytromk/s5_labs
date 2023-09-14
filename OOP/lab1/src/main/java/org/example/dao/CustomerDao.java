package org.example.dao;

import org.example.entity.Customer;
import org.example.entity.HomeTariff;
import org.example.entity.MobileTariff;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class CustomerDao extends AbstractDao<Customer> {

    public CustomerDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void addMobileTariffToCustomer(Customer customer, MobileTariff mobileTariff) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        customer.addMobileTariff(mobileTariff);
        session.persist(customer);
        transaction.commit();
        session.close();
    }

    public void addHomeTariffToCustomer(Customer customer, HomeTariff homeTariff) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        customer.addHomeTariff(homeTariff);
        session.persist(customer);
        transaction.commit();
        session.close();
    }

}
