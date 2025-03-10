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
        body { font-family: 'Roboto', Arial, sans-serif; 
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
            margin-top: 20px; }
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
            color: #4CAF50; 
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
                    <td>${req.status}</td>
                    <td><a href="view_request.jsp?id=${req.requestId}"><i class="fas fa-eye"></i> Xem</a></td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <div class="footer">
        © 2025 Hệ thống quản lý nghỉ phép
    </div>
</body>
</html>