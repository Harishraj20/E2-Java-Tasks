package com.example.Servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import com.example.BeanClass.Customer;
import com.example.CrudMethods.CustomersOperations;


public class SortServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sortOrder = request.getParameter("sortorder");
        String columnName = request.getParameter("value");

        List<Customer> customers = CustomersOperations.sortCustomers(columnName, sortOrder);

        request.setAttribute("customers", customers);
        request.getRequestDispatcher("Customers.jsp").forward(request, response);
    }
}
