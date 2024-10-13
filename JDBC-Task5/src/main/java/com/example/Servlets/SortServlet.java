package com.example.Servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.example.BeanClass.Customer;
import com.example.utils.HibernateConfiguration; 

public class SortServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static SessionFactory sessionFactory = HibernateConfiguration.getSessionFactory(); 
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sortOrder = request.getParameter("sortorder");
        String columnName = request.getParameter("value");

        String hql = "FROM Customer ORDER BY " + columnName + " " + sortOrder;

        List<Customer> customers = new ArrayList<>();

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Query<Customer> query = session.createQuery(hql, Customer.class);
            customers = query.list();
            tx.commit();
        } catch (Exception e) {
           System.out.println(e);
        }

        request.setAttribute("customers", customers);
        request.getRequestDispatcher("Customers.jsp").forward(request, response);
    }
}
