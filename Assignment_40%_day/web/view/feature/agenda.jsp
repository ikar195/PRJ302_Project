<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.Date" %>
<c:if test="${empty sessionScope.user || (!sessionScope.user.roles.contains('Manager') && !sessionScope.user.roles.contains('Admin'))}">
    <c:redirect url="../login.jsp"/>
</c:if>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Màn hình Agenda</title>
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <style>
            html, body {
                height: 100%;
                margin: 0;
                padding: 0;
                font-family: 'Roboto', Arial, sans-serif;
                background-color: #f4f4f9;
                overflow: hidden; /* Ngăn cuộn toàn trang */
            }

            body {
                display: flex;
                flex-direction: column;
                min-height: 100vh; /* Đảm bảo chiều cao tối thiểu là 100% viewport */
            }

            .container {
                width: 80%;
                max-width: 900px;
                background: linear-gradient(135deg, #ffffff, #f9f9f9);
                padding: 25px;
                border-radius: 12px;
                box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.15);
                text-align: center;
                margin: 20px auto;
                flex-grow: 1; /* Cho phép container mở rộng */
                overflow: hidden; /* Ngăn cuộn trong container */
                display: flex;
                flex-direction: column;
            }

            h2 {
                font-size: 28px;
                font-weight: 700;
                color: #333;
                margin-bottom: 25px;
            }

            .date-selection {
                margin-top: 20px;
                margin-bottom: 25px;
            }
            .date-selection label {
                margin-right: 15px;
                font-size: 16px;
                font-weight: bold;
            }
            .date-selection input {
                padding: 10px;
                margin: 5px 10px;
                border: 1px solid #ccc;
                border-radius: 4px;
                background-color: #f9f9f9;
                font-size: 14px;
            }
            .action-btn {
                background-color: #4CAF50;
                color: white;
                padding: 10px 15px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                margin: 5px 10px;
                font-size: 16px;
                transition: background-color 0.3s;
                text-decoration: none;
                display: inline-block;
                width: auto;
            }
            .action-btn:hover {
                background-color: #45a049;
            }
            .button-wrapper {
                display: inline-block; /* Giữ nút Home trong một khối không bị kéo dài */
                margin-bottom: 15px;
            }

            /* Bọc table để có thể cuộn ngang + dọc */
            .table-wrapper {
                width: 100%;
                max-height: calc(100vh - 220px);
                overflow: auto;     /* Cho phép cuộn cả ngang lẫn dọc */
                margin: 20px 0;
                border: 1px solid #ccc; /* Viền tùy chọn cho khung nhìn rõ hơn */
                flex-grow: 1;
                -ms-overflow-style: none; /*ẩn thanh cuộn trong edge */
            }
            .table-wrapper::-webkit-scrollbar {
                display: none; /* Ẩn thanh cuộn trên Chrome, Safari, và các trình duyệt Webkit */
            }

            /* Table */
            .agenda-table {
                border-collapse: collapse;
                width: 100%;
                table-layout: auto;
            }

            /* Ô chung */
            .agenda-table th, .agenda-table td {
                border: 2px solid #ccc;
                padding: 12px;
                text-align: center;
                font-size: 14px;
                white-space: nowrap; /* Không cho xuống dòng */
            }

            /* Sticky HEADER (hàng đầu) */
            .agenda-table thead th {
                position: sticky;
                top: 0;             /* Dính trên cùng */
                z-index: 3;         /* Cao hơn tbody */
                background-color: #4CAF50;
                color: #fff;
                border-top: 0px;
            }

            /* Sticky COLUMN (cột đầu) */
            .agenda-table th:first-child,
            .agenda-table td:first-child {
                position: sticky;
                left: 0;           /* Dính sát lề trái */
                z-index: 2;        /* Cao hơn các cột còn lại */
                background-color: #fff;
                border-left: 0px;
            }

            /* Riêng ô giao giữa thead và cột đầu (top-left corner) */
            .agenda-table thead th:first-child {
                z-index: 5;        /* Cao nhất để không bị đè */
                background-color: #4CAF50; /* Giữ màu header */
                color: #fff;
            }

            /* Màu cho các trạng thái */
            .none {
                background-color: #ffffff; /* Màu trắng */
            }
            .working {
                background-color: #00CC00;
            }
            .leave {
                background-color: #d9534f;
            }

            .footer {
                background-color: #333;
                color: white;
                text-align: center;
                padding: 10px;
                font-size: 14px;
                width: 100%;
                margin-top: auto;
                box-sizing: border-box;
                flex-shrink: 0;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h2>Màn hình Agenda</h2>
            <c:if test="${not empty error}">
                <p style="color: red; font-weight: bold;">${error}</p>
            </c:if>
            <div class="button-wrapper">
                <a href="/Assignment_40%25_day/view/home.jsp" class="action-btn"><i class="fas fa-home"></i> Home</a>
            </div>
            <form action="${pageContext.request.contextPath}/Agenda" method="post">
                <div class="date-selection">
                    <label for="start-date"><i class="fas fa-calendar-alt"></i> Ngày bắt đầu: </label>
                    <input type="date" id="start-date" name="startDate" required>
                    <label for="end-date"><i class="fas fa-calendar-alt"></i> Ngày kết thúc: </label>
                    <input type="date" id="end-date" name="endDate" required>
                    <button type="submit" class="action-btn">Xem Agenda</button>
                </div>
            </form>
            <c:if test="${not empty startDate && not empty endDate && not empty users}">
                <div class="table-wrapper">
                    <table class="agenda-table">
                        <thead>
                            <tr>
                                <th>Nhân sự</th>
                                    <c:forEach var="i" begin="0" end="${(endDate.time - startDate.time) / (1000 * 60 * 60 * 24)}">
                                        <c:set var="currentDate" value="<%= new java.util.Date(((java.util.Date)request.getAttribute(\"startDate\")).getTime() + ((Integer)pageContext.getAttribute(\"i\")).longValue() * 24 * 60 * 60 * 1000) %>" />
                                    <th><fmt:formatDate value="${currentDate}" pattern="dd/MM/yyyy"/></th>
                                    </c:forEach>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="emp" items="${users}">
                                <tr>
                                    <td>${emp.fullName}</td>
                                    <c:forEach var="i" begin="0" end="${(endDate.time - startDate.time) / (1000 * 60 * 60 * 24)}">
                                        <c:set var="currentDate" value="<%= new java.util.Date(((java.util.Date)request.getAttribute(\"startDate\")).getTime() + ((Integer)pageContext.getAttribute(\"i\")).longValue() * 24 * 60 * 60 * 1000) %>" />
                                        <c:set var="today" value="<%= new java.util.Date() %>" />
                                        <c:set var="status" value="${agenda[emp.userId][currentDate]}"/>
                                        <c:choose>
                                            <c:when test="${status == 'OnLeave'}">
                                                <td class="leave"></td> <!-- Ngày nghỉ, kể cả trong tương lai -->
                                            </c:when>
                                            <c:when test="${currentDate.after(today)}">
                                                <td class="none"></td> <!-- Ngày tương lai không có dữ liệu -->
                                            </c:when>
                                            <c:otherwise>
                                                <td class="${status == 'Working' ? 'working' : 'working'}"></td> <!-- Ngày quá khứ/hiện tại -->
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>
        </div>
        <div class="footer">
            © 2025 Hệ thống quản lý nghỉ phép
        </div>
    </body>
</html>