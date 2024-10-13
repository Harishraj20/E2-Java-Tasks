package com.example.Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.example.BeanClass.Customer;
import com.example.utils.HibernateConfiguration;

@WebServlet("/DeleteCustomerServlet")
public class DeleteCustomerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static SessionFactory sessionFactory = HibernateConfiguration.getSessionFactory();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int val = Integer.parseInt(request.getParameter("customerId"));

        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            String hql = "DELETE FROM Customer WHERE id = :customerId";
            int deletedCount = session.createQuery(hql).setParameter("customerId", val).executeUpdate();

            tx.commit();

            if (deletedCount > 0) {
                System.out.println("Customer with ID " + val + " deleted.");
            } else {
                System.out.println("No customer found with ID " + val);
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            session.close();
        }

        response.sendRedirect("DataServlet?type=customers");
    }
}
