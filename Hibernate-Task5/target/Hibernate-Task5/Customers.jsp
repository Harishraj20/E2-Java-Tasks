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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <style>
                
        .action-button{
            display: flex;
            justify-content: space-between;
        }
        th,
        td {
        padding: 10px;
        border: none;
        }
        table{
            width: 100%;
        }
        .edit-button,.delete-form{
            width: 40%;
            padding: 4%;
            border: none;
            background-color: #1f0a50;
            color: #fffefe;
            border-radius: 10px;
            cursor: pointer;
        }
        .delete-button{
            width: 100%;
            background-color: #1f0a50;
            border: none;
            border-radius: 10px;
            color: #ffffff;
            cursor: pointer;
        }
input {
    border: 1px solid #ccc;
    padding: 5px;
    border-radius: 4px;
    transition: border 0.3s ease;
}

    </style>
    
</head>
<body>
    <h2 style="text-align: center; color:#1f0a50;">CUSTOMERS LIST</h2>

    <div class="formHeader">
        <form class="formSort" action="SortServlet" method="post">
            <select name="value" id="options">
                <option value="name" <%= "name".equals(request.getParameter("value")) ? "selected" : "" %>>Name</option>
                <option value="age" <%= "age".equals(request.getParameter("value")) ? "selected" : "" %>>Age</option>
                <option value="mobileNumber" <%= "mobileNumber".equals(request.getParameter("value")) ? "selected" : "" %>>Mobile Number</option>
                <option value="emailId" <%= "emailId".equals(request.getParameter("value")) ? "selected" : "" %>>Email ID</option>
                <option value="location" <%= "location".equals(request.getParameter("value")) ? "selected" : "" %>>Location</option>
                <option value="gender" <%= "gender".equals(request.getParameter("value")) ? "selected" : "" %>>Gender</option>
            </select>

            <button class="sortButton" type="submit" name="sortorder" value="asc">Asc</button>
            <button class="sortButton" type="submit" name="sortorder" value="desc">Desc</button>
        </form>

        <button class="add-customer" id="add-customer-button">Add Customer</button>
    </div>
    <div class="table">
    <table>
        <thead>
            <tr>
                <th style="width: 15%;">Name</th>
                <th style="width: 10%;">Age</th>
                <th style="width: 10%;">Mobile Number</th>
                <th style="width: 20%;">Email ID</th>
                <th style="width: 15%;">Location</th>
                <th style="width: 10%;">Gender</th>
                <th style="width: 20%;">Actions</th>
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
                    <td><%= item.getMobileNumber() %></td>
                    <td><%= item.getEmailId() %></td>
                    <td><%= item.getLocation() %></td>
                    <td><%= item.getGender() %></td>
                    <td>
                        <div class="action-button">
                        <form class="delete-form" action="DeleteCustomerServlet" method="get" onsubmit="return confirm('Are you sure you want to delete the Customer: <%= item.getName() %>?');">
                            <button class="delete-button" type="submit" name="customerId" value="<%= item.getId() %>">Delete <i class="fa-solid fa-trash"></i></button>
                        </form>
                        <button class="edit-button" onclick="openEditModal('<%= item.getId() %>','<%= item.getName() %>', '<%= item.getAge() %>', '<%= item.getMobileNumber() %>', '<%= item.getEmailId() %>', '<%= item.getLocation() %>', '<%= item.getGender() %>')">Edit <i class="fa-solid fa-edit"></i></button>
                    </div>
                    </td>
                </tr>
            <%
                    } 
                } else {
            %>
                <tr>
                    <td colspan="7">No customers available.</td>
                </tr>
            <%
                } 
            %>
        </tbody>
    </table>
</div>

    <div id="addUserModal" class="add-customer-modal"  style="display: none;">
        <div class="modal-content">
            <div class="heading">
                <h3 id="modal-title">ADD CUSTOMERS</h3>
                <span class="close-button"><i class="fa-regular fa-circle-xmark fa-lg"></i></span>
            </div>
            <p class="error-message"> 
            </p>
            <form id="customerForm" class="addCustomerForm" action="AddServlet" onsubmit ="return validateFields()" method="post">

                <input type="hidden" name="customerId" id="customerId">
                <div class="form-elements">
                    <label for="name">NAME:</label>
                    <input type="text" name="name" id="name" placeholder="Enter Name">
                </div>
                <div id="nameError" class="error"></div>

                <div class="form-elements">
                    <label for="age">AGE:</label>
                    <input type="text" name="age" id="age" placeholder="Enter age">
                </div>
                <div id="ageError" class="error"></div>

                <div class="form-elements">
                    <label for="mobile">MOBILE NUMBER:</label>
                    <input type="text" name="mobile" id="mobile" placeholder="Enter 10 Digit Mobile Number" >
                </div>
                <div id="mobileError" class="error"></div>

                <div class="form-elements">
                    <label for="gender">GENDER:</label>
                    <input type="text" name="gender" id="gender" placeholder="Enter Gender" >
                </div>
                <div id="genderError" class="error"></div>


                <div class="form-elements">
                    <label for="email">MAIL-ID:</label>
                    <input type="text" name="email" id="email"  placeholder="Enter Email [Example - abc@domain.com]">
                </div>
                <div id="emailError" class="error"></div>

                <div class="form-elements">
                    <label for="location">LOCATION:</label>
                    <input type="text" name="location" id="location" placeholder="Enter Location">
                </div>
                <div id="locationError" class="error"></div>

                <div class="form-button">
                    <input class="reset-button" type="reset" value="Reset">
                    <input class="submit-button" type="submit" value="Submit" id="form-submit">
                </div>
            </form>
        </div>
    </div>

    <div class="backbutton-div">
        <button id="back-button" onclick="window.location.href='index.html'">Back</button>
    </div>

    <script>
        function openEditModal(id,name, age, mobile, email, location, gender) {
                document.getElementById('customerForm').action = 'UpdateServlet';
                document.getElementById('modal-title').innerText = 'EDIT CUSTOMERS';
                document.getElementById('form-submit').value = 'Update';
                document.getElementById('customerId').value = id;
                document.getElementById('name').value = name;
                document.getElementById('age').value = age;
                document.getElementById('mobile').value = mobile;
                document.getElementById('email').value = email;
                document.getElementById('location').value = location;
                document.getElementById('gender').value = gender;
                document.getElementById('addUserModal').style.display = 'flex';
}
    </script>
    <script src="script.js"></script>
     
</body>
</html>
