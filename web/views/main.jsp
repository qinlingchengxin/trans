<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="baseUrl" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>数据传输中心</title>
</head>
<frameset rows="127,*,60" cols="*" frameborder="no" border="0" framespacing="0">
    <frame src="${baseUrl}/web/admin/top.do" name="topFrame" scrolling="No" noresize="noresize" id="topFrame" title="topFrame"/>
    <frameset cols="220,*" frameborder="no" border="0" framespacing="0">
        <frame src="${baseUrl}/web/admin/left.do" name="leftFrame" scrolling="No" noresize="noresize" id="leftFrame" title="leftFrame"/>
        <frame src="${baseUrl}/web/admin/welcome.do" name="rightFrame" id="rightFrame" title="rightFrame"/>
    </frameset>
    <frame src="${baseUrl}/web/admin/footer.do" name="topFrame" scrolling="No" noresize="noresize" id="footerFrame" title="footerFrame"/>
</frameset>
</html>
