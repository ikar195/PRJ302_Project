<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login Page</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <style>
        <c:if test="${not empty error}">
                <p style="color: red;">${error}</p>
            </c:if>
        body {
            display: flex;
            justify-content: center;
            align-items: center; 
            height: 100vh; 
            background-color: #4CAF50; 
            margin: 0; 
            font-family: Arial, sans-serif; 
        }
        .login-container { 
            background: white; 
            padding: 30px; 
            border-radius: 16px; 
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); 
            width: 360px; 
            height: 360px; 
            text-align: center; 
            display: flex; 
            flex-direction: column; 
            align-items: center; 
            justify-content: center; 
        }
        .logo { 
            width: 120px; 
            margin-bottom: 20px; 
        }
        .input-container { 
            width: 100%; 
            display: flex; 
            align-items: center; 
            border-bottom: 1px solid #ccc; 
            padding: 30px 0; 
        }
        .input-container i { 
            margin-right: 10px; 
            color: #777; 
        }
        .input-container input { 
            width: 100%; 
            border: none; 
            outline: none; 
            font-size: 17px; 
        }
        .login-button { 
            width: 30%; 
            padding: 10px; 
            background: #4CAF50; 
            border: none; 
            color: white; 
            font-size: 16px; 
            border-radius: 24px; 
            cursor: pointer; 
            margin-top: 32px; 
        }
        .login-button:hover { 
            background: #45a049; 
        }
    </style>
</head>
<body>
    <form action="login" method="post">
        <div class="login-container">
            <img src="img/Signature.jpg" alt="Logo" class="logo">
            <div class="input-container">
                <i class="fas fa-user"></i>
                <input type="text" name="username" placeholder="Username" required>
            </div>
            <div class="input-container">
                <i class="fas fa-lock"></i>
                <input type="password" name="password" placeholder="Password" required>
            </div>
            <button class="login-button">LOGIN</button>
            <c:if test="${not empty error}">
                <p style="color: red;">${error}</p>
            </c:if>
        </div>
    </form>
</body>
</html>