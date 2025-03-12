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
        <title>Chi tiết đơn xin nghỉ</title>
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <style>
            body {
                font-family: 'Roboto', Arial, sans-serif;
                background-color: #f4f4f9;
                display: flex;
                flex-direction: column;
                min-height: 100vh;
                justify-content: center;
                align-items: center;
                margin: 0;
            }
            .container {
                width: 400px;
                max-height: 500px;
                background: linear-gradient(135deg, #ffffff, #f9f9f9);
                padding: 30px;
                border-radius: 12px;
                box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.15);
                text-align: center;
                flex: 0 0 auto;
                margin: auto;
            }
            h2 {
                font-size: 28px;
                font-weight: 700;
                color: #333;
                margin-bottom: 20px;
            }
            p {
                font-size: 16px;
                color: #666;
                margin-bottom: 15px;
            }
            .input-group {
                display: flex;
                flex-direction: column;
                align-items: center;
                width: 100%;
                margin-bottom: 20px;
            }
            label {
                font-size: 16px;
                margin-bottom: 5px;
                width: 90%;
                text-align: left;
            }
            .input-box {
                font-size: 14px;
                width: 90%;
                padding: 10px;
                margin: 5px 0;
                border: 1px solid #ccc;
                border-radius: 4px;
                background-color: #f9f9f9;
                text-align: center;
            }
            .status {
                font-weight: bold;
                font-size: 16px;
                margin-top: 10px;
            }
            .button-group {
                display: flex;
                justify-content: center;
                gap: 10px;
                margin-top: 20px;
            }
            .action-btn {
                background-color: #4CAF50;
                color: white;
                padding: 10px 20px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                font-size: 16px;
                display: flex;
                flex-direction: column;
                justify-content: center;
                align-items: center;
                text-decoration: none;
                transition: background-color 0.3s;
            }
            .action-btn i {
                font-size: 24px;
                margin-bottom: 5px;
            }
            .action-btn:hover {
                background-color: #45a049;
            }
            .reject-btn {
                background-color: #d9534f;
            }
            .reject-btn:hover {
                background-color: #b52b27;
            }
            .footer {
                background-color: #333;
                color: white;
                text-align: center;
                padding: 10px;
                font-size: 14px;
                width: 100%;
                margin-top: auto;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h2>Chi tiết đơn xin nghỉ phép</h2>
            <p><strong>Tên nhân viên:</strong> ${requestScope.request.fullName}</p>
            <p><strong>Phòng ban:</strong> ${requestScope.request.departmentName}</p>
            <p><strong>Ngày bắt đầu:</strong> <fmt:formatDate value="${requestScope.request.startDate}" pattern="dd/MM/yyyy"/></p>
            <p><strong>Ngày kết thúc:</strong> <fmt:formatDate value="${requestScope.request.endDate}" pattern="dd/MM/yyyy"/></p>
            <p><strong>Lý do:</strong> ${requestScope.request.reason}</p>
            <p><strong>Trạng thái:</strong> ${requestScope.request.status}</p>

            <div class="button-group">
                <c:if test="${sessionScope.user.roles.contains('Manager') || sessionScope.user.roles.contains('Admin')}">
                    <c:if test="${requestScope.request.status == 'Inprogress'}">
                        <form action="${pageContext.request.contextPath}/ViewRequest" method="post" style="display:inline;">
                            <input type="hidden" name="requestId" value="${requestScope.request.requestId}">
                            <input type="hidden" name="action" value="Approved">
                            <input type="hidden" name="page" value="${requestScope.currentPage}">
                            <button type="submit" class="button"><i class="fas fa-check"></i> Phê duyệt</button>
                        </form>
                        <form action="${pageContext.request.contextPath}/ViewRequest" method="post" style="display:inline;">
                            <input type="hidden" name="requestId" value="${requestScope.request.requestId}">
                            <input type="hidden" name="action" value="Rejected">
                            <input type="hidden" name="page" value="${requestScope.currentPage}">
                            <button type="submit" class="button reject"><i class="fas fa-times"></i> Từ chối</button>
                        </form>
                    </c:if>
                </c:if>
                <a href="${pageContext.request.contextPath}/ListRequests?page=${requestScope.currentPage}" class="button"><i class="fas fa-arrow-left"></i> Quay lại</a>
            </div>
        </div>
        <div class="footer">
            © 2025 Hệ thống quản lý nghỉ phép
        </div>
    </body>
</html>