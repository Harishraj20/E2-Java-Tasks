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

import org.hibernate.query.Query;

import com.example.utils.HibernateConfiguration; 

@WebServlet("/DeleteProductServlet")
public class DeleteProductServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final SessionFactory sessionFactory = HibernateConfiguration.getSessionFactory(); 

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String val = request.getParameter("productName"); 
        int productId = Integer.parseInt(val);

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            String hql = "DELETE FROM product WHERE id = :productId";
    
            Query<?> query = session.createQuery(hql);
            query.setParameter("productId", productId);
            int result = query.executeUpdate(); 

            tx.commit(); 

            if (result > 0) {
                System.out.println("Product with ID " + productId + " deleted.");
            } else {
                System.out.println("No product found with ID " + productId);
            }
        } catch (Exception e) {
           System.out.println(e);
        }

        response.sendRedirect("DataServlet?type=products"); 
    }
}
