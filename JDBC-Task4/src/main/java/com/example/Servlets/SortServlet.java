package com.example.Servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.BeanClass.Customer;
import com.example.DisplayList.CustomerDisplay;

public class SortServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String type = request.getParameter("sortorder");
        String columnName = request.getParameter("value");

        String query = "SELECT * FROM customers ORDER BY " + columnName + " " + type;
        

        List<Customer> customers = CustomerDisplay.getCustomers(query);

        request.setAttribute("customers", customers);
        request.getRequestDispatcher("Customers.jsp").forward(request, response);
    }
}
