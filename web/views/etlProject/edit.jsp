<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="baseUrl" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>任务</title>
    <link rel="stylesheet" type="text/css" href="${baseUrl}/css/basic.css">
    <link rel="stylesheet" type="text/css" href="${baseUrl}/css/form.css">
    <link rel="stylesheet" type="text/css" href="${baseUrl}/css/topAndBottom.css">
    <script type="text/javascript" src="${baseUrl}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${baseUrl}/js/menu.js"></script>
    <script type="text/javascript" src="${baseUrl}/js/jquery.extend.js"></script>

    <script type="text/javascript">

        function save() {
            var form = $('#form');
            var baseObj = form.serializeObject();
            $.ajax({
                url: "${baseUrl}/web/etl/projectSave.do",
                type: "POST",
                data: baseObj,
                dataType: "json",
                success: function (result) {
                    if (result.code == 1000) {
                        alert('保存成功');
                    } else {
                        alert(result.msg);
                    }
                }
            });
        }

        function add() {
            var form = $('#form');
            var baseObj = form.serializeObject();
            $.ajax({
                url: "${baseUrl}/web/etl/projectAdd.do",
                type: "POST",
                data: baseObj,
                dataType: "json",
                success: function (result) {
                    if (result.code == 1000) {
                        $("#id").val(result.data.id);
                        $("#btn_add").attr("onclick", "save();").text("保存");
                        alert('添加成功');
                    } else {
                        alert(result.msg);
                    }
                }
            });
        }
    </script>

    <style type="text/css">
        .title_self {
            display: inline-block;
            width: 180px;
            text-align: right;
            margin-right: 5px;
        }
    </style>
</head>
<body>
<div class="content clearfloat">
    <div class="content_right fr">
        <div class="first">
            <a href="javascript:history.go(-1);">返回</a><span>></span><a href="javascript:void(0)">任务</a>
        </div>
        <div class="second">
            <form id="form">
                <input type="hidden" id="id" name="id" value="${etlProject.id}"/>

                <div>
                    <span class="title_self">项目名称</span>
                    <input id="doc-ipt-prjName-1" type="text" name="prjName" value="${etlProject.prjName}"/>
                </div>

                <div>
                    <span class="title_self">原数据源</span>

                    <select id="doc-ipt-srcDbId-1" name="srcDbId">
                        <c:forEach items="${dataSources}" var="dataSource">
                            <option value="${dataSource.id}" <c:if test="${dataSource.id == etlProject.srcDbId}">selected="selected"</c:if>>${dataSource.sourceName}</option>
                        </c:forEach>
                    </select>
                </div>

                <div>
                    <span class="title_self">目标数据源</span>

                    <select id="doc-ipt-desDbId-1" name="desDbId">
                        <c:forEach items="${dataSources}" var="dataSource">
                            <option value="${dataSource.id}" <c:if test="${dataSource.id == etlProject.desDbId}">selected="selected"</c:if>>${dataSource.sourceName}</option>
                        </c:forEach>
                    </select>
                </div>

                <div>
                    <c:if test='${etlProject.id  == null}'>
                        <span id="btn_add" class="save" onclick="add();">添加</span>
                    </c:if>
                    <c:if test='${etlProject.id != null}'>
                        <span class="save" onclick="save();">保存</span>
                    </c:if>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>