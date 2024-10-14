package com.example.utils;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateConfiguration {
    private static final SessionFactory sessionFactory;

    static {
     
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();
    }
       

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
