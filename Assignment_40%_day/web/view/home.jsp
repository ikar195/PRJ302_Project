<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty sessionScope.user}">
    <c:redirect url="login.jsp"/>
</c:if>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Trang chủ</title>
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <style>
            body {
                margin: 0;
                padding: 0;
                font-family: 'Roboto', Arial, sans-serif;
                background-color: #f4f4f9;
                display: flex;
                flex-direction: column;
                height: 100vh;
            }
            .navbar {
                background-color: #4CAF50;
                padding: 15px 20px;
                color: white;
                display: flex;
                justify-content: center;
                gap: 90px;
                box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            }
            .navbar a {
                text-decoration: none;
                color: white;
                font-size: 18px;
                transition: color 0.3s;
            }
            .navbar a:hover {
                color: #ddd;
            }
            .welcome-section {
                flex: 1;
                display: flex;
                justify-content: center;
                align-items: center;
                flex-direction: column;
                text-align: center;
                background: linear-gradient(135deg, #ffffff, #f4f4f9);
                padding: 50px 20px;
            }
            .welcome-section h2 {
                opacity: 0;
                animation: fadeIn 1s ease-in forwards;
            }
            @keyframes fadeIn {
                from {
                    opacity: 0;
                    transform: translateY(-20px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }
            .welcome-section p {
                color: #666;
                font-size: 18px;
                margin-bottom: 20px;
            }
            .footer {
                background-color: #333;
                color: white;
                text-align: center;
                padding: 10px;
                font-size: 14px;
            }
        </style>
    </head>
    <body>
        <div class="navbar">
            <a href="../CreateRequest"><i class="fas fa-plus"></i> Tạo đơn xin nghỉ phép</a>
            <a href="../ListRequests"><i class="fas fa-list"></i> Xem danh sách đơn</a>
            <c:if test="${sessionScope.user.roles.contains('Manager') || sessionScope.user.roles.contains('Admin')}">
                <a href="../Agenda"><i class="fas fa-calendar"></i> Xem Agenda nhân sự</a>
            </c:if>
            <a href="../LogoutServlet"><i class="fas fa-sign-out-alt"></i> Đăng xuất</a>
        </div>
        <div class="welcome-section">
            <h2>Chào mừng ${sessionScope.user.fullName} đến với hệ thống quản lý đơn nghỉ phép</h2>
            <p>Quản lý đơn nghỉ phép dễ dàng, nhanh chóng và hiệu quả!</p>
        </div>
        <div class="footer">
            © 2025 Hệ thống quản lý nghỉ phép
        </div>
    </body>
</html>