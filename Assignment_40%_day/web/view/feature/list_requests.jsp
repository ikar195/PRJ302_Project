<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${empty sessionScope.user}">
    <c:redirect url="../login.jsp"/>
</c:if>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Danh sách đơn</title>
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <style>
            body {
                font-family: 'Roboto', Arial, sans-serif;
                background-color: #f4f4f4;
                text-align: center;
                margin: 0;
                display: flex;
                flex-direction: column;
                min-height: 100vh;
            }
            .container {
                width: 80%;
                margin: 30px auto;
                background: linear-gradient(135deg, #ffffff, #f9f9f9);
                padding: 30px;
                border-radius: 12px;
                box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.15);
                flex: 1;
            }
            h2 {
                font-size: 28px;
                color: #333;
                margin-bottom: 13px;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 20px;
            }
            th, td {
                border: 1px solid #ddd;
                padding: 12px 15px;
                text-align: center;
            }
            th {
                background-color: #4CAF50;
                color: white;
                font-size: 16px;
            }
            tr:nth-child(even) {
                background-color: #f9f9f9;
            }
            tr:hover {
                background-color: #e0e0e0;
                cursor: pointer;
            }
            td a {
                color: #000000;
                text-decoration: none;
                transition: color 0.3s;
            }
            td a:hover {
                color: #45a049;
            }
            .back-button {
                display: inline-block;
                padding: 10px 20px;
                background-color: #4CAF50;
                color: white;
                text-decoration: none;
                border-radius: 25px;
                margin-top: 1px;
                transition: background-color 0.3s;
            }
            .back-button:hover {
                background-color: #45a049;
            }
            .footer {
                background-color: #333;
                color: white;
                text-align: center;
                padding: 10px;
                font-size: 14px;
                margin-top: auto;
            }
            .pagination {
                margin-top: 20px;
            }
            .pagination a {
                color: #4CAF50;
                padding: 8px 16px;
                text-decoration: none;
                border: 1px solid #ddd;
                margin: 0 4px;
                border-radius: 4px;
            }
            .pagination a.active {
                background-color: #4CAF50;
                color: white;
            }
            .pagination a:hover:not(.active) {
                background-color: #ddd;
            }
            .status-approved {
                color: #4CAF50; /* Màu xanh cho Approved */
                font-weight: bold;
            }
            .status-rejected {
                color: #f44336; /* Màu đỏ cho Rejected */
                font-weight: bold;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h2>Danh sách đơn xin nghỉ phép</h2>
            <a href="/Assignment_40%25_day/view/home.jsp" class="back-button"><i class="fas fa-home"></i> Home</a>
            <table border="1">
                <tr>
                    <th>Tên nhân viên</th>
                    <th>Phòng ban</th>
                    <th>Ngày bắt đầu</th>
                    <th>Ngày kết thúc</th>
                    <th>Trạng thái</th>
                    <th>Chi tiết</th>
                </tr>
                <c:forEach var="req" items="${requests}">
                    <tr>
                        <td>${req.fullName}</td>
                        <td>${req.departmentName}</td>
                        <td><fmt:formatDate value="${req.startDate}" pattern="dd/MM/yyyy"/></td>
                        <td><fmt:formatDate value="${req.endDate}" pattern="dd/MM/yyyy"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${req.status == 'Approved'}">
                                <span class="status-approved">${req.status}</span>
                            </c:when>
                            <c:when test="${req.status == 'Rejected'}">
                                <span class="status-rejected">${req.status}</span>
                            </c:when>
                            <c:otherwise>
                                <span>${req.status}</span>
                            </c:otherwise>
                        </c:choose>
                        </td>
                        <td><a href="${pageContext.request.contextPath}/ViewRequest?id=${req.requestId}">Xem</a></td>
                    </tr>
                </c:forEach>
            </table>
            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="ListRequests?page=${currentPage - 1}">&laquo; Previous</a>
                </c:if>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <a href="ListRequests?page=${i}" class="${currentPage == i ? 'active' : ''}">${i}</a>
                </c:forEach>
                <c:if test="${currentPage < totalPages}">
                    <a href="ListRequests?page=${currentPage + 1}">Next &raquo;</a>
                </c:if>
            </div>
        </div>
        <div class="footer">
            © 2025 Hệ thống quản lý nghỉ phép
        </div>
    </body>
</html>