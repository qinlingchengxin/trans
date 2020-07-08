<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="baseUrl" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>映射字段</title>
    <link rel="stylesheet" type="text/css" href="${baseUrl}/css/basic.css">
    <link rel="stylesheet" type="text/css" href="${baseUrl}/css/form.css">
    <link rel="stylesheet" type="text/css" href="${baseUrl}/css/topAndBottom.css">
    <script type="text/javascript" src="${baseUrl}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${baseUrl}/js/menu.js"></script>
    <script type="text/javascript" src="${baseUrl}/js/jquery.extend.js"></script>

    <script type="text/javascript">

        function add() {
            var form = $('#form');
            var baseObj = form.serializeObject();
            $.ajax({
                url: "${baseUrl}/web/etl/fieldAdd.do",
                type: "POST",
                data: baseObj,
                dataType: "json",
                success: function (result) {
                    if (result.code == 1000) {
                        alert('添加成功');
                    } else {
                        alert(result.msg);
                    }
                }
            });
        }

        function checkedDesField(srcField) {
            var selNode = $("#doc-ipt-desFieldName-1");
            var options = selNode.find("option[value='" + srcField + "']");
            if (options.length > 0 && selNode.val() != srcField) {
                selNode.find("option").removeAttr("selected");
                options.attr("selected", "selected");
                selNode.val(srcField);
            }
        }
    </script>
</head>
<body>
<div class="content clearfloat">
    <div class="content_right fr">
        <div class="first">
            <a href="javascript:history.go(-1);">返回</a><span>></span><a href="javascript:void(0)">映射字段</a>
        </div>
        <div class="second">
            <form id="form">
                <input type="hidden" id="entityId" name="entityId" value="${etlField.entityId}"/>

                <div>
                    <span class="title">原字段名</span>
                    <select id="doc-ipt-srcFieldName-1" name="srcFieldName" onchange="checkedDesField(this.value);">
                        <c:forEach items="${srcFields}" var="srcField">
                            <option value="${srcField.name}" <c:if test="${fn:toUpperCase(srcField.name) == fn:toUpperCase(etlField.srcFieldName)}">selected="selected"</c:if>>${srcField.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div>
                    <span class="title">目标字段</span>
                    <select id="doc-ipt-desFieldName-1" name="desFieldName">
                        <c:forEach items="${desFields}" var="desField">
                            <option value="${desField.name}" <c:if test="${fn:toUpperCase(desField.name) == fn:toUpperCase(etlField.desFieldName)}">selected="selected"</c:if>>${desField.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div>
                    <span id="btn_add" class="save" onclick="add();">添加</span>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>