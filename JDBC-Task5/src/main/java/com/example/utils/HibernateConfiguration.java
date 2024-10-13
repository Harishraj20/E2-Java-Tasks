package com.example.utils;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import com.example.BeanClass.Customer;

public class HibernateConfiguration {
    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml") // This assumes hibernate.cfg.xml is in src/main/resources
                    .addAnnotatedClass(Customer.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
