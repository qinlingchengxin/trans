<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="baseUrl" value="${pageContext.request.contextPath}"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>任务</title>
    <link rel="stylesheet" type="text/css" href="${baseUrl}/css/basic.css">
    <link rel="stylesheet" type="text/css" href="${baseUrl}/css/index.css">
    <link rel="stylesheet" type="text/css" href="${baseUrl}/css/topAndBottom.css">
    <script type="text/javascript" src="${baseUrl}/js/jquery.min.js"></script>
    <script type="text/javascript">
        function stopAllEtlJob(id) {
            $.ajax({
                url: "${baseUrl}/web/etl/stopAllEtlJob.do?prjId=" + id,
                type: "GET",
                dataType: "json",
                success: function (result) {
                    if (result.code == 1000) {
                        alert('操作成功');
                    } else {
                        alert(result.msg);
                    }
                }
            });
        }

        function stopAllApiJob(id) {
            $.ajax({
                url: "${baseUrl}/web/etl/stopAllApiJob.do?prjId=" + id,
                type: "GET",
                dataType: "json",
                success: function (result) {
                    if (result.code == 1000) {
                        alert('操作成功');
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
            <div class="fl title">任务</div>
            <div class="fr operation">
                <span><a href="${baseUrl}/web/etl/projectList.do" target="rightFrame"> <img src="${baseUrl}/img/flush.png">刷新</a></span>
                <span><a href="${baseUrl}/web/etl/projectEdit.do" target="rightFrame"> <img src="${baseUrl}/img/zengjia.png">新增</a></span>
            </div>
        </div>
        <div class="third">
            <table>
                <tr>
                    <th width="4%">序号</th>
                    <th width="14%">名称</th>
                    <th width="14%">源数据库</th>
                    <th width="14%">目标数据库</th>
                    <th width="10%">创建时间</th>
                    <th style="text-align: center;">操作</th>
                </tr>
                <c:forEach items="${etlProjects}" var="etlProject" varStatus="vs">
                    <tr id="tr_${etlProject.id}">
                        <td>${vs.index + 1}</td>
                        <td>${etlProject.prjName}</td>
                        <td>
                            <a style="color: #1166B8;" href="${baseUrl}/web/etl/dataSourceEdit.do?id=${etlProject.srcDbId}&isView=1" target="rightFrame">${etlProject.srcDbName}</a>
                        </td>
                        <td>
                            <a style="color: #1166B8;" href="${baseUrl}/web/etl/dataSourceEdit.do?id=${etlProject.desDbId}&isView=1" target="rightFrame">${etlProject.desDbName}</a>
                        </td>
                        <td>
                            <c:if test="${etlProject.createTime > 0}">
                                <jsp:useBean id="createTime" class="java.util.Date"/>
                                <jsp:setProperty name="createTime" property="time" value="${etlProject.createTime}"/>
                                <fmt:formatDate value="${createTime}" pattern="yyyy-MM-dd HH:mm"/>
                            </c:if>
                        </td>
                        <td style="text-align: center;">
                            <span><a href="${baseUrl}/web/etl/projectEdit.do?id=${etlProject.id}" target="rightFrame"><img src="${baseUrl}/img/bianji.png">编辑</a></span>
                            <span><a href="${baseUrl}/web/etl/entityList.do?prjId=${etlProject.id}" target="rightFrame"><img src="${baseUrl}/img/chakan.png">映射表</a></span>
                            <span><a href="javascript:stopAllEtlJob('${etlProject.id}');"><img src="${baseUrl}/img/sync.png">停止所有ETL任务</a></span>
                            <span><a href="javascript:stopAllApiJob('${etlProject.id}');"><img src="${baseUrl}/img/sync.png">停止所有API任务</a></span>
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
                        <li><a href="${baseUrl}/web/etl/projectList.do?page=${currPage - 1}">上一页</a></li>
                    </c:if>
                    <li id="currPage">${currPage}</li>
                    <c:if test="${currPage >= totalPage}">
                        <li><a href="#">下一页</a></li>
                    </c:if>
                    <c:if test="${currPage < totalPage}">
                        <li><a href="${baseUrl}/web/etl/projectList.do?page=${currPage + 1}">下一页</a></li>
                    </c:if>
                </ul>
            </div>
        </div>
    </div>
</div>
</body>
</html>