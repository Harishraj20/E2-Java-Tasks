<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.BeanClass.Customer" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customer List</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <h1 style="text-align: center; color:#1f0a50;">CUSTOMERS LIST</h1>
    <table>
        <thead>
            <tr>
                <th>Name</th>
                <th>Age</th>
                <th>Email</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<Customer> customers = (List<Customer>) request.getAttribute("customers");
                if (customers != null && !customers.isEmpty()) {
                    for (Customer item : customers) {
            %>
                <tr>
                    <td><%= item.getName() %></td>
                    <td><%= item.getAge() %></td>
                    <td><%= item.getEmail() %></td>
                </tr>
            <%
                    } 
                } else {
            %>
                <tr>
                    <td colspan="3">No customers available.</td>
                </tr>
            <%
                } 
            %>
        </tbody>
    </table>
    <form action="SortServlet" method="post">
        <select name="value" id="options">
            <option value="name" ${param.value == null || param.value == 'name' ? 'selected' : ''}>Name</option>
            <option value="emailId" ${param.value == 'emailId' ? 'selected' : ''}>EmailId</option>
            <option value="age" ${param.value == 'age' ? 'selected' : ''}>Age</option>
        </select>
        <button type="submit" name="sortorder" value="asc">Asc</button>
        <button type="submit" name="sortorder" value="desc">Desc</button>
    </form>
    
    
    
    <br>
    <br>
    <br>
    
    <button onclick="window.location.href='index.html'">Back</button>
</body>
</html>
