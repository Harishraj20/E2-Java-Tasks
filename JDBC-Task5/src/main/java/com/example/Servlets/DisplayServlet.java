package com.example.Servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.example.BeanClass.Customer;
import com.example.BeanClass.product;
import com.example.utils.HibernateConfiguration; 

@WebServlet("/DataServlet")
public class DisplayServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static SessionFactory sessionFactory = HibernateConfiguration.getSessionFactory(); 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String type = request.getParameter("type");

        if (type.equals("customers")) {
            List<Customer> customers = fetchCustomers();
            request.setAttribute("customers", customers);
            request.getRequestDispatcher("Customers.jsp").forward(request, response);
        } else {
            List<product> products = fetchProducts();
            request.setAttribute("products", products);
            request.getRequestDispatcher("products.jsp").forward(request, response);
        }
    }

    public static List<Customer> fetchCustomers() {
        List<Customer> customers = new ArrayList<>();

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Query<Customer> query = session.createQuery("FROM Customer", Customer.class);
            customers = query.list();
            tx.commit();
        } catch (Exception e) {
            System.out.println(e);
        }
        return customers;
    }

    public List<product> fetchProducts() {
        List<product> products = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Query<product> query = session.createQuery("FROM product", product.class);
            products = query.list();
            tx.commit();
        } catch (Exception e) {
            System.out.println(e);
        }
        return products;
    }
}
