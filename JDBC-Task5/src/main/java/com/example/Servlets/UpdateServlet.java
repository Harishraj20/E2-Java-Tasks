package com.example.Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.example.BeanClass.Customer;
import com.example.utils.HibernateConfiguration; 

@WebServlet("/UpdateServlet")
public class UpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final SessionFactory sessionFactory = HibernateConfiguration.getSessionFactory(); 


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int customerId = Integer.parseInt(request.getParameter("customerId"));
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        long mobile = Long.parseLong(request.getParameter("mobile"));
        String email = request.getParameter("email");
        String location = request.getParameter("location");

        String gender = request.getParameter("gender");

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            String hql = "UPDATE Customer SET name = :name, age = :age, mobileNumber = :mobile, emailId = :email, location = :location, gender = :gender WHERE id = :id";
            Query<?> query = session.createQuery(hql);
            query.setParameter("name", name);
            query.setParameter("age", age);
            query.setParameter("mobile", mobile);
            query.setParameter("email", email);
            query.setParameter("location", location);
            query.setParameter("gender", gender);
            query.setParameter("id", customerId);

            int result = query.executeUpdate();
            tx.commit();

            if (result > 0) {
                response.sendRedirect("DataServlet?type=customers");
            } else {
                System.out.println("No results found");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
