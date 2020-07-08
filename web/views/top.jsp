<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="baseUrl" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>数据传输中心</title>
    <link rel="stylesheet" type="text/css" href="${baseUrl}/css/basic.css">
    <link rel="stylesheet" type="text/css" href="${baseUrl}/css/index.css">
    <link rel="stylesheet" type="text/css" href="${baseUrl}/css/topAndBottom.css">
    <script type="text/javascript" src="${baseUrl}/js/jquery.min.js"></script>
    <script type="text/javascript">

        function logout() {
            $.ajax({
                url: "${baseUrl}/web/admin/logout.do",
                type: "GET",
                dataType: "json",
                success: function (result) {
                    if (result.code == 1000) {
                        window.parent.location.reload();
                    } else {
                        alert(result.msg);
                    }
                }
            });
        }
    </script>
</head>

<body>
<div class="top clearfloat">
    <div class="logo fl"><a href="javascript:void(0)">数据传输中心</a></div>
    <div class="fr time">
        <p>您好，<span>管理员</span>欢迎进入数据传输中心!</p>

        <p>${now}&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:logout();" style="color: #7CA710;">退出</a></p>
    </div>
</div>
<div class="fullSearch clearfloat">
    <span class="fl welcome">WELCOME</span>
    <a class="fl index" href="javascript:void(0)"><img src="${baseUrl}/img/shouye.png"></a>
</div>
</body>
</html>
