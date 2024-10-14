package com.example.CrudMethods;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.example.BeanClass.Customer;
import com.example.utils.HibernateConfiguration;

public class CustomersOperations {

    private static final SessionFactory sessionFactory = HibernateConfiguration.getSessionFactory();

    public static List<Customer> fetchCustomers() {

        List<Customer> customers = new ArrayList<>();

        try (Session session = sessionFactory.openSession()) {
            Query<Customer> query = session.createQuery("FROM Customer", Customer.class);
            customers = query.list();

        } catch (Exception e) {
            System.out.println(e);
        }
        return customers;

    }

    public static void insertCustomers(String name, int age, long mobileNumber, String email, String location,
            String gender) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx;
            try {
                tx = session.beginTransaction();
                Customer customer = new Customer();
                customer.setName(name);
                customer.setAge(age);
                customer.setMobileNumber(mobileNumber);
                customer.setEmailId(email);
                customer.setLocation(location);
                customer.setGender(gender);

                session.save(customer);
                tx.commit();
            } catch (Exception e) {
                System.out.println(e);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void updateCustomers(int customerId, String name, int age, long mobile, String email, String location,
            String gender) {
                Session session = sessionFactory.openSession();
                    Transaction tx;

        try {
            tx = session.beginTransaction();
            String hql = "UPDATE Customer SET name = :name, age = :age, mobileNumber = :mobile, emailId = :email, location = :location, gender = :gender WHERE id = :id";
            Query<?> query = session.createQuery(hql);
            query.setParameter("name", name);
            query.setParameter("age", age);
            query.setParameter("mobile", mobile);
            query.setParameter("email", email);
            query.setParameter("location", location);
            query.setParameter("gender", gender);
            query.setParameter("id", customerId);

            query.executeUpdate();
            tx.commit();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void deleteCustomers(int customerId) {
        Session session = sessionFactory.openSession();
        Transaction tx;

        try {
            tx = session.beginTransaction();

            String hql = "DELETE FROM Customer WHERE id = :customerId";
            session.createQuery(hql).setParameter("customerId", customerId).executeUpdate();

            tx.commit();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static List<Customer> sortCustomers(String columnName, String sortOrder) {
        String hql = "FROM Customer ORDER BY " + columnName + " " + sortOrder;

        List<Customer> customers = new ArrayList<>();

        try (Session session = sessionFactory.openSession()) {
            Query<Customer> query = session.createQuery(hql, Customer.class);
            customers = query.list();

        } catch (Exception e) {
            System.out.println(e);
        }
        return customers;
    }
}
