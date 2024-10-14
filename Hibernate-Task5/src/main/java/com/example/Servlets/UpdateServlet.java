package com.example.Servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.CrudMethods.CustomersOperations;

@WebServlet("/UpdateServlet")
public class UpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override 
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int customerId = Integer.parseInt(request.getParameter("customerId"));
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        long mobile = Long.parseLong(request.getParameter("mobile"));
        String email = request.getParameter("email");
        String location = request.getParameter("location");

        String gender = request.getParameter("gender");


        CustomersOperations.updateCustomers(customerId, name, age, mobile, email, location, gender);

        response.sendRedirect("DataServlet?type=customers");
           
    }
}
