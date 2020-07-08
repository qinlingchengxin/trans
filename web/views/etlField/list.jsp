<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="baseUrl" value="${pageContext.request.contextPath}"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>映射字段</title>
    <link rel="stylesheet" type="text/css" href="${baseUrl}/css/basic.css">
    <link rel="stylesheet" type="text/css" href="${baseUrl}/css/index.css">
    <link rel="stylesheet" type="text/css" href="${baseUrl}/css/topAndBottom.css">
    <script type="text/javascript" src="${baseUrl}/js/jquery.min.js"></script>
    <script type="text/javascript">
        function fieldDel(id) {
            $.ajax({
                url: "${baseUrl}/web/etl/fieldDel.do?id=" + id,
                type: "GET",
                dataType: "json",
                success: function (result) {
                    if (result.code == 1000) {
                        $("#tr_" + id).remove();
                        alert('删除成功');
                    } else {
                        alert(result.msg);
                    }
                }
            });
        }
    </script>
</head>
<body>

<div class="content clearfloat">
    <div class="content_right fr">
        <div class="first clearfloat">
            <div class="fl title">
                <a class="title" href="javascript:history.go(-1);">返回</a> > 任务 > 映射表 > 映射字段
            </div>
            <div class="fr operation">
                <span><a href="${baseUrl}/web/etl/fieldList.do?entityId=${entityId}" target="rightFrame"> <img src="${baseUrl}/img/flush.png">刷新</a></span>
                <span><a href="${baseUrl}/web/etl/fieldEdit.do?entityId=${entityId}" target="rightFrame"> <img src="${baseUrl}/img/zengjia.png">新增</a></span>
            </div>
        </div>
        <div class="third">
            <table>
                <tr>
                    <th width="4%">序号</th>
                    <th width="15%">原字段名</th>
                    <th width="15%">目标字段</th>
                    <th width="12%">创建时间</th>
                    <th style="text-align: center;">操作</th>
                </tr>
                <c:forEach items="${etlFields}" var="etlField" varStatus="vs">
                    <tr id="tr_${etlField.id}">
                        <td>${vs.index + 1}</td>
                        <td>${fn:toLowerCase(etlField.srcFieldName)}</td>
                        <td>${fn:toLowerCase(etlField.desFieldName)}</td>
                        <td>
                            <c:if test="${etlField.createTime > 0}">
                                <jsp:useBean id="createTime" class="java.util.Date"/>
                                <jsp:setProperty name="createTime" property="time" value="${etlField.createTime}"/>
                                <fmt:formatDate value="${createTime}" pattern="yyyy-MM-dd HH:mm"/>
                            </c:if>
                        </td>
                        <td style="text-align: center;">
                            <span><a href="javascript:fieldDel('${etlField.id}');"><img src="${baseUrl}/img/delete.png">删除</a></span>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <div class="fenye">
                <ul class="fy_ul">
                    <li class="fy_li_first">共 ${count} 条记录</li>
                    <c:if test="${currPage == 1}">
                        <li><a href="#">上一页</a></li>
                    </c:if>
                    <c:if test="${currPage > 1}">
                        <li><a href="${baseUrl}/web/etl/fieldList.do?entityId=${entityId}&page=${currPage - 1}">上一页</a></li>
                    </c:if>
                    <li id="currPage">${currPage}</li>
                    <c:if test="${currPage >= totalPage}">
                        <li><a href="#">下一页</a></li>
                    </c:if>
                    <c:if test="${currPage < totalPage}">
                        <li><a href="${baseUrl}/web/etl/fieldList.do?entityId=${entityId}&page=${currPage + 1}">下一页</a></li>
                    </c:if>
                </ul>
            </div>
        </div>
    </div>
</div>
</body>
</html>