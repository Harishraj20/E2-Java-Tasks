package com.example.DisplayList;

import com.example.BeanClass.Customer;
import com.example.utils.HibernateConfiguration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class CustomerDisplay {

    private static final SessionFactory sessionFactory = HibernateConfiguration.getSessionFactory(); 
    public static List<Customer> getCustomers() {
        List<Customer> customers = null;

       
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<Customer> query = session.createQuery("FROM Customer", Customer.class);
            customers = query.getResultList(); 
            transaction.commit(); 
        } catch (Exception e) {
            System.out.println(e);
        }

        return customers; 
    }
}
