<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty sessionScope.user}">
    <c:redirect url="../login.jsp"/>
</c:if>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Đơn xin nghỉ phép</title>
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
                height: 550px;
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
                font-weight: 700;
                color: #333;
                margin-bottom: 1px;
            }
            p {
                font-size: 16px;
                color: #666;
                margin-bottom: 20px;
            }
            .input-group {
                display: flex;
                flex-direction: column;
                align-items: center;
                width: 100%;
                margin-bottom: 15px;
            }
            label {
                font-size: 16px;
                width: 90%;
                text-align: left;
            }
            .input-box {
                width: 270px;
                padding: 10px;
                margin: 5px 0;
                border: 1px solid #ccc;
                border-radius: 4px;
                font-size: 14px;
            }
            .input-box[rows] {
                resize: none;
            }
            .button-group {
                display: flex;
                justify-content: center;
                gap: 15px;
            }
            .submit-btn {
                background-color: #4CAF50;
                color: white;
                padding: 12px 0;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                font-size: 16px;
                width: 100px;
                height: auto;
                text-align: center;
                transition: background-color 0.3s;
                text-decoration: none;
            }
            .submit-btn:hover {
                background-color: #45a049;
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
            .user-info {
                background-color: #e0e0e0; 
                border: 1px solid #b0b0b0; 
                border-radius: 20px; 
                padding: 8px 15px;
                display: inline-block; 
                font-size: 14px; 
                color: #454545;
                text-align: center; 
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h2>Đơn xin nghỉ phép</h2>
            <p class="user-info"><i class="fas fa-user"></i><b> ${sessionScope.user.fullName}</b><b> - ${sessionScope.user.departmentName}</b></p>
            <form action="/Assignment_40%25_day/CreateRequest" method="post" enctype="multipart/form-data">
                <div class="input-group">
                    <label for="from-date"><i class="fas fa-calendar-alt"></i> Từ ngày:</label>
                    <input type="date" id="from-date" name="startDate" class="input-box" required>
                </div>
                <div class="input-group">
                    <label for="to-date"><i class="fas fa-calendar-alt"></i> Tới ngày:</label>
                    <input type="date" id="to-date" name="endDate" class="input-box" required>
                </div>
                <div class="input-group">
                    <label for="reason"><i class="fas fa-file-alt"></i> Lý do:</label>
                    <textarea id="reason" name="reason" class="input-box" rows="4" placeholder="Nhập lý do nghỉ phép (tối đa 200 ký tự, ví dụ: ốm, việc gia đình)" maxlength="200" required></textarea>
                </div>
                <div class="input-group">
                    <label for="attachment"><i class="fas fa-paperclip"></i> Đính kèm ảnh .jpg (nếu có):</label>
                    <input type="file" id="attachment" name="attachment" class="input-box" accept="image/*">
                </div>
                <div class="button-group">
                    <button type="submit" class="submit-btn"><i class="fas fa-paper-plane"></i>  Gửi</button>
                    <a href="/Assignment_40%25_day/view/home.jsp" class="submit-btn"><i class="fas fa-home"></i>  Home</a>
                </div>
            </form>
            <% if (request.getAttribute("error") != null) { %>
            <p style="color: red;"><%= request.getAttribute("error") %></p>
            <% } %>
        </div>
        <div class="footer">
            © 2025 Hệ thống quản lý nghỉ phép
        </div>
    </body>
</html>