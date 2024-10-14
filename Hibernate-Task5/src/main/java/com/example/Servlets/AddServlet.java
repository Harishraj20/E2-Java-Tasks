package com.example.Servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.CrudMethods.CustomersOperations;

@WebServlet("/AddServlet")
public class AddServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        long mobileNumber = Long.parseLong(request.getParameter("mobile"));
        String gender = request.getParameter("gender");
        String email = request.getParameter("email");
        String location = request.getParameter("location");

        CustomersOperations.insertCustomers(name, age, mobileNumber, email, location, gender);

        response.sendRedirect("DataServlet?type=customers");
    }

}
