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

@WebServlet("/AddServlet")
public class AddServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static SessionFactory sessionFactory = HibernateConfiguration.getSessionFactory();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        long mobileNumber = Long.parseLong(request.getParameter("mobile"));
        String gender = request.getParameter("gender");
        String email = request.getParameter("email");
        String location = request.getParameter("location");

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
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

                response.sendRedirect("DataServlet?type=customers");
            } catch (Exception e) {
                System.out.println(e);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
