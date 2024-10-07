package com.example.Servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.JDBCConnectivity.DatabaseConnection;

public class DeleteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String val = request.getParameter("productName");
         String deleteQuery = "DELETE FROM products WHERE id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(deleteQuery)) {

            stmt.setInt(1, Integer.parseInt(val));
            stmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
        response.sendRedirect("DataServlet?type=products");
    }
}
