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
        body { font-family: 'Roboto', Arial, sans-serif; 
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
            height: 500px; 
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
            margin-bottom: 3px; 
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
            margin-bottom: 20px; 
        }
        label { 
            font-size: 16px; 
            margin-bottom: 5px; 
            width: 90%; 
            text-align: left; 
        }
        .input-box { 
            width: 90%; 
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
            margin-top: 4px; 
        }
        .submit-btn { 
            background-color: #4CAF50; 
            color: white; 
            padding: 12px 20px; 
            border: none; 
            border-radius: 4px; 
            cursor: pointer; 
            font-size: 16px; 
            width: 120px; 
            height: 40px; 
            text-align: center; 
            transition: background-color 0.3s; }
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
    </style>
</head>
<body>
    <div class="container">
        <h2>Đơn xin nghỉ phép</h2>
        <p><i class="fas fa-user"></i><b> ${sessionScope.user.fullName}</b><b> - Phòng IT</b></p>
        <form action="../CreateRequestServlet" method="post">
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
                <textarea id="reason" name="reason" class="input-box" rows="4" maxlength="200" required></textarea>
            </div>
            <div class="button-group">
                <button type="submit" class="submit-btn"><i class="fas fa-paper-plane"></i> Gửi</button>
                <a href="../home.jsp" class="submit-btn"><i class="fas fa-home"></i> Home</a>
            </div>
        </form>
    </div>
    <div class="footer">
        © 2025 Hệ thống quản lý nghỉ phép
    </div>
</body>
</html>