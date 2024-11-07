<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored =
"false" %> <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Users Details</title>
    <link href="<c:url value='/css/table.css' />" rel="stylesheet" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
      integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg=="
      crossorigin="anonymous"
      referrerpolicy="no-referrer"
    />
  </head>
  <body>
    <div class="user-details">
      <p>User Details</p>
    </div>
    <div class="table-container">
      <table border="1">
        <thead>
          <tr>
            <th style="width: 25%">User Id</th>
            <th style="width: 25%">Name</th>
            <th style="width: 25%">Visit Count</th>
            <th style="width: 25%">Last Login</th>
          </tr>
        </thead>
        <tbody>
          <c:choose>
            <c:when test="${not empty userList}">
              <c:forEach var="user" items="${userList}">
                <tr>
                  <td>${user.id}</td>
                  <td>${user.userName}</td>
                  <td>${user.visitCount}</td>
                  <td>${user.formattedLastLogin}</td>
                </tr>
              </c:forEach>
            </c:when>
            <c:otherwise>
              <tr>
                <td colspan="4" style="text-align: center">
                  No data available
                </td>
              </tr>
            </c:otherwise>
          </c:choose>
        </tbody>
      </table>
    </div>
    <div class="button-container">
      <form class="button-container" action="/finaltask/back" method="get">
        <button class="back-button">
          <i class="fa-solid fa-arrow-left fa-xs"></i> Back
        </button>
      </form>
    </div>
  </body>
</html>
