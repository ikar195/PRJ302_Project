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
        <title>Chi tiết đơn xin nghỉ phép</title>
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
                overflow: hidden;
                padding: 0;
            }
            .container {
                width: 400px;
                background: linear-gradient(135deg, #ffffff, #f9f9f9);
                padding: 30px;
                border-radius: 12px;
                box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.15);
                text-align: center;
                margin: auto;
                display: flex;
                flex-direction: column;
                justify-content: center;
                align-items: center;
            }
            h2 {
                font-size: 28px;
                color: #333;
                margin-bottom: 20px;
            }
            .info-group {
                text-align: left;
                margin-bottom: 15px;
            }
            .info-group i {
                margin-right: 10px;
                color: #666;
            }
            .info-group span {
                font-weight: bold;
            }
            .info-group p {
                margin: 5px 0;
                color: #666;
            }
            .button-group {
                display: flex;
                justify-content: center;
                gap: 15px;
                margin-top: 20px;
            }
            .button {
                display: inline-block;
                padding: 10px 20px;
                color: white;
                text-decoration: none;
                border: none;
                border-radius: 25px;
                cursor: pointer;
                transition: background-color 0.3s;
                font-size: 16px;
            }
            .home {
                background-color: #4CAF50;
            }
            .home:hover {
                background-color: #45a049;
            }
            .approve {
                background-color: #4CAF50;
            }
            .approve:hover {
                background-color: #45a049;
            }
            .reject {
                background-color: #f44336;
            }
            .reject:hover {
                background-color: #e53935;
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
            .input-box {
                padding: 10px;
                margin: 5px 0;
                border: 1px solid #ccc;
                border-radius: 4px;
                font-size: 14px;
                resize: none;
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
            <h2>Chi tiết đơn xin nghỉ</h2>
            <c:if test="${not empty error}">
                <p style="color: red;">${error}</p>
            </c:if>
            <c:if test="${not empty requestScope.request}">
                <div class="info-group">
                    <p><i class="fas fa-user"></i><span>Tên nhân viên:</span> ${requestScope.request.fullName}</p>
                    <p><i class="fas fa-building"></i><span>Phòng ban:</span> ${requestScope.request.departmentName}</p>
                    <p><i class="fas fa-calendar-alt"></i><span>Ngày bắt đầu:</span> <fmt:formatDate value="${requestScope.request.startDate}" pattern="dd/MM/yyyy"/></p>
                    <p><i class="fas fa-calendar-alt"></i><span>Ngày kết thúc:</span> <fmt:formatDate value="${requestScope.request.endDate}" pattern="dd/MM/yyyy"/></p>
                    <p><i class="fas fa-file-alt"></i><span>Lý do:</span> ${requestScope.request.reason}</p>
                    <p><i class="fas fa-info-circle"></i><span>Trạng thái:</span>
                            <c:choose>
                                <c:when test="${requestScope.request.status == 'Approved'}">
                                <span class="status-approved">${requestScope.request.status}</span>
                            </c:when>
                            <c:when test="${requestScope.request.status == 'Rejected'}">
                                <span class="status-rejected">${requestScope.request.status}</span>
                            </c:when>
                            <c:otherwise>
                                <span>${requestScope.request.status}</span>
                            </c:otherwise>
                        </c:choose>
                    </p>
                    <c:if test="${requestScope.request.status != 'Inprogress' and not empty requestScope.request.comment}">
                        <p ><i class="fas fa fa-comment"></i><span>Comment:</span> ${requestScope.request.comment}</p>
                            </c:if>    
                </div>
               <c:if test="${(sessionScope.user.roles.contains('Manager') || sessionScope.user.departmentName.contains('Manager')) && sessionScope.user.userId != requestScope.request.userID}">
                    <c:if test="${requestScope.request.status == 'Inprogress'}">
                        <form action="ViewRequest" method="post">
                            <input type="hidden" name="requestId" value="${requestScope.request.requestId}">
                            <input type="hidden" name="page" value="${requestScope.currentPage}">
                            <div class="info-group">
                                <span for="comment">Comment:</span>
                                <textarea id="comment" name="comment" class="input-box" rows="4" cols="50" placeholder="Nhập nhận xét (có thể không nhập)" maxlength="200"></textarea>
                            </div>
                            <div class="button-group">
                                <button type="submit" name="action" value="Approved" class="button approve"><i class="fas fa-check"></i> Phê duyệt</button>
                                <button type="submit" name="action" value="Rejected" class="button reject"><i class="fas fa-times"></i> Từ chối</button>
                            </div>
                        </form>
                    </c:if>
                </c:if>
            </c:if>
            <c:if test="${empty requestScope.request}">
                <p style="color: red;">Không có dữ liệu đơn xin nghỉ!</p>
            </c:if>

            <div class="button-group">
                <a href="${pageContext.request.contextPath}/ListRequests?page=${requestScope.currentPage}" class="button home"><i class="fas fa-chevron-left"></i> Quay lại</a>
            </div>
        </div>
        <div class="footer">
            © 2025 Hệ thống quản lý nghỉ phép
        </div>
    </body>
</html>