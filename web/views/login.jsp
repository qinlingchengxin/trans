<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="baseUrl" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>登录</title>
    <link rel="stylesheet" type="text/css" href="${baseUrl}/css/basic.css">
    <link rel="stylesheet" type="text/css" href="${baseUrl}/css/login.css">
    <script type="text/javascript">
        function login() {
            document.getElementById("frm").submit();
        }
    </script>
</head>
<body>
<div class="top">
    <div>数据传输中心</div>
</div>
<div class="content">
    <div class="login">
        <form id="frm" action="${baseUrl}/web/admin/login.do" method="post">
            <div class="loginBox fr">
                <div class="title">用户登录</div>
                <div class="user clearfloat userAndPassword">
                    <span class="fl"><img src="${baseUrl}/img/user.png"></span><input class="fl" type="text" name="username">
                </div>
                <div class="password clearfloat userAndPassword">
                    <span class="fl"><img src="${baseUrl}/img/password.png"></span><input class="fl" type="password" name="password">
                </div>
                <div class="entry">
                    <span onclick="login();">登录</span>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>