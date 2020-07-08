<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="baseUrl" value="${pageContext.request.contextPath}"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>数据源</title>
    <link rel="stylesheet" type="text/css" href="${baseUrl}/css/basic.css">
    <link rel="stylesheet" type="text/css" href="${baseUrl}/css/index.css">
    <link rel="stylesheet" type="text/css" href="${baseUrl}/css/topAndBottom.css">
    <script type="text/javascript" src="${baseUrl}/js/jquery.min.js"></script>
    <script type="text/javascript">
        function syncTableField(id) {
            $.ajax({
                url: "${baseUrl}/web/etl/syncTableField.do?dsId=" + id,
                type: "GET",
                dataType: "json",
                success: function (result) {
                    if (result.code == 1000) {
                        alert('同步完成！');
                    } else {
                        alert(result.msg);
                    }
                }
            });
        }
    </script>
    <style type="text/css">
        .alive {
            color: green;
        }

        .dead {
            color: red;
        }
    </style>
</head>
<body>

<div class="content clearfloat">
    <div class="content_right fr">
        <div class="first clearfloat">
            <div class="fl title">数据源</div>
            <div class="fr operation">
                <span><a href="${baseUrl}/web/etl/dataSourceList.do" target="rightFrame"> <img src="${baseUrl}/img/flush.png">刷新</a></span>
                <span><a href="${baseUrl}/web/etl/dataSourceEdit.do" target="rightFrame"> <img src="${baseUrl}/img/zengjia.png">新增</a></span>
            </div>
        </div>
        <div class="third">
            <table>
                <tr>
                    <th width="4%">序号</th>
                    <th width="10%">源名称</th>
                    <th width="10%">类型</th>
                    <th width="14%">IP:端口</th>
                    <th width="10%">数据库名称</th>
                    <th width="10%">用户名</th>
                    <th width="4%">存活</th>
                    <th width="10%">创建时间</th>
                    <th style="text-align: center;">操作</th>
                </tr>
                <c:forEach items="${dataSources}" var="dataSource" varStatus="vs">
                    <tr>
                        <td>${vs.index + 1}</td>
                        <td>${dataSource.sourceName}</td>
                        <td>
                            <c:if test="${dataSource.dbType == 0}">MySql</c:if>
                            <c:if test="${dataSource.dbType == 1}">Oracle</c:if>
                            <c:if test="${dataSource.dbType == 2}">MS SQL</c:if>
                            <c:if test="${dataSource.dbType == 3}">KingBase</c:if>
                        </td>
                        <td>${dataSource.dbIp}:${dataSource.dbPort}</td>
                        <td>${dataSource.dbName}</td>
                        <td>${dataSource.dbUsername}</td>

                        <c:if test="${dataSource.alive == 0}">
                            <td class="dead">否</td>
                        </c:if>

                        <c:if test="${dataSource.alive == 1}">
                            <td class="alive">是</td>
                        </c:if>

                        <td>
                            <c:if test="${dataSource.createTime > 0}">
                                <jsp:useBean id="createTime" class="java.util.Date"/>
                                <jsp:setProperty name="createTime" property="time" value="${dataSource.createTime}"/>
                                <fmt:formatDate value="${createTime}" pattern="yyyy-MM-dd HH:mm"/>
                            </c:if>
                        </td>
                        <td style="text-align: center;">
                            <span><a href="${baseUrl}/web/etl/dataSourceEdit.do?id=${dataSource.id}" target="rightFrame"><img src="${baseUrl}/img/bianji.png">编辑</a></span>
                            <span><a href="${baseUrl}/web/etl/etlAllTables.do?dsId=${dataSource.id}" target="rightFrame"><img src="${baseUrl}/img/chakan.png">数据表</a></span>
                            <span><a href="javascript:syncTableField('${dataSource.id}');" target="rightFrame"><img src="${baseUrl}/img/sync.png">同步结构</a></span>
                                <%--<span><a href="${baseUrl}/api/doc.do?dsId=${dataSource.id}" target="rightFrame"><img src="${baseUrl}/img/chakan.png">文档</a></span>--%>
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
                        <li><a href="${baseUrl}/web/etl/dataSourceList.do?page=${currPage - 1}">上一页</a></li>
                    </c:if>
                    <li id="currPage">${currPage}</li>
                    <c:if test="${currPage >= totalPage}">
                        <li><a href="#">下一页</a></li>
                    </c:if>
                    <c:if test="${currPage < totalPage}">
                        <li><a href="${baseUrl}/web/etl/dataSourceList.do?page=${currPage + 1}">下一页</a></li>
                    </c:if>
                </ul>
            </div>
        </div>
    </div>
</div>
</body>
</html>