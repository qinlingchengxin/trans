<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="baseUrl" value="${pageContext.request.contextPath}"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>映射表</title>
    <link rel="stylesheet" type="text/css" href="${baseUrl}/css/basic.css">
    <link rel="stylesheet" type="text/css" href="${baseUrl}/css/index.css">
    <link rel="stylesheet" type="text/css" href="${baseUrl}/css/topAndBottom.css">
    <script type="text/javascript" src="${baseUrl}/js/jquery.min.js"></script>
    <script type="text/javascript">
        function entityDel(id) {
            $.ajax({
                url: "${baseUrl}/web/etl/entityDel.do?id=" + id,
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

        function startEtlJob(id, index) {
            $.ajax({
                url: "${baseUrl}/web/etl/startEtlJob.do?entityId=" + id,
                type: "GET",
                dataType: "json",
                success: function (result) {
                    if (result.code == 1000) {
                        $("#etl_job_start_" + index).hide();
                        $("#etl_job_stop_" + index).show();
                        alert('启动成功！');
                    } else {
                        alert(result.msg);
                    }
                }
            });
        }

        function stopEtlJob(id, index) {
            $.ajax({
                url: "${baseUrl}/web/etl/stopEtlJob.do?entityId=" + id,
                type: "GET",
                dataType: "json",
                success: function (result) {
                    if (result.code == 1000) {
                        $("#etl_job_stop_" + index).hide();
                        $("#etl_job_start_" + index).show();
                        alert('已停止！');
                    } else {
                        alert(result.msg);
                    }
                }
            });
        }

        function startApiJob(id, index) {
            $.ajax({
                url: "${baseUrl}/web/etl/startApiJob.do?entityId=" + id,
                type: "GET",
                dataType: "json",
                success: function (result) {
                    if (result.code == 1000) {
                        var repeat = result.data;
                        if (repeat == 1) {
                            $("#api_job_start_" + index).hide();
                            $("#api_job_stop_" + index).show();
                        }
                        alert('启动成功！');
                    } else {
                        alert(result.msg);
                    }
                }
            });
        }

        function stopApiJob(id, index) {
            $.ajax({
                url: "${baseUrl}/web/etl/stopApiJob.do?entityId=" + id,
                type: "GET",
                dataType: "json",
                success: function (result) {
                    if (result.code == 1000) {
                        $("#api_job_stop_" + index).hide();
                        $("#api_job_start_" + index).show();
                        alert('已停止！');
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
            <div class="fl title"><a class="title" href="javascript:history.go(-1);">返回</a> > 任务 > 映射表</div>
            <div class="fr operation">
                <span><a href="${baseUrl}/web/etl/entityList.do?prjId=${prjId}" target="rightFrame"> <img src="${baseUrl}/img/flush.png">刷新</a></span>
                <span><a href="${baseUrl}/web/etl/entityEdit.do?prjId=${prjId}" target="rightFrame"> <img src="${baseUrl}/img/zengjia.png">新增</a></span>
            </div>
        </div>
        <div class="third">
            <table>
                <tr>
                    <th width="4%">序号</th>
                    <th width="12%" class="self_th_td">原表名</th>
                    <th width="12%" class="self_th_td">目标表名</th>
                    <th width="12%">原表主键</th>
                    <th width="12%">目标表主键</th>
                    <th width="10%">创建时间</th>
                    <th style="text-align: center;">操作</th>
                </tr>
                <c:forEach items="${etlEntities}" var="etlEntity" varStatus="vs">
                    <tr id="tr_${etlEntity.id}">
                        <td>${vs.index + 1}</td>
                        <td class="self_th_td">${fn:toLowerCase(etlEntity.srcTabName)}</td>
                        <td class="self_th_td">${fn:toLowerCase(etlEntity.desTabName)}</td>
                        <td>${etlEntity.srcPrimaryKey}</td>
                        <td>${etlEntity.desPrimaryKey}</td>
                        <td>
                            <c:if test="${etlEntity.createTime > 0}">
                                <jsp:useBean id="createTime" class="java.util.Date"/>
                                <jsp:setProperty name="createTime" property="time" value="${etlEntity.createTime}"/>
                                <fmt:formatDate value="${createTime}" pattern="yyyy-MM-dd HH:mm"/>
                            </c:if>
                        </td>
                        <td style="text-align: center;">
                            <span><a href="${baseUrl}/web/etl/entityEdit.do?prjId=${prjId}&id=${etlEntity.id}" target="rightFrame"><img src="${baseUrl}/img/bianji.png">编辑</a></span>
                            <span><a href="javascript:entityDel('${etlEntity.id}');"><img src="${baseUrl}/img/delete.png">删除</a></span>
                            <span><a href="${baseUrl}/web/etl/fieldList.do?entityId=${etlEntity.id}" target="rightFrame"><img src="${baseUrl}/img/chakan.png">映射字段</a></span>
                            <c:if test="${etlEntity.exec == 0}">
                                <span id="etl_job_start_${vs.index}"><a href="javascript:startEtlJob('${etlEntity.id}','${vs.index}');" target="rightFrame"><img src="${baseUrl}/img/bianji.png">执行ETL任务</a></span>
                                <span id="etl_job_stop_${vs.index}" style="display: none;"><a href="javascript:stopEtlJob('${etlEntity.id}', '${vs.index}');" target="rightFrame"><img src="${baseUrl}/img/sync.png">停止任务</a></span>
                            </c:if>

                            <c:if test="${etlEntity.exec == 1}">
                                <span id="etl_job_start_${vs.index}" style="display: none;"><a href="javascript:startEtlJob('${etlEntity.id}','${vs.index}');" target="rightFrame"><img src="${baseUrl}/img/bianji.png">执行任务</a></span>
                                <span id="etl_job_stop_${vs.index}"><a href="javascript:stopEtlJob('${etlEntity.id}','${vs.index}');" target="rightFrame"><img src="${baseUrl}/img/sync.png">停止ETL任务</a></span>
                            </c:if>

                            <c:if test="${etlEntity.apiExec == 0}">
                                <span id="api_job_start_${vs.index}"><a href="javascript:startApiJob('${etlEntity.id}','${vs.index}');" target="rightFrame"><img src="${baseUrl}/img/bianji.png">执行API任务</a></span>
                                <span id="api_job_stop_${vs.index}" style="display: none;"><a href="javascript:stopApiJob('${etlEntity.id}', '${vs.index}');" target="rightFrame"><img src="${baseUrl}/img/sync.png">停止API任务</a></span>
                            </c:if>

                            <c:if test="${etlEntity.apiExec == 1}">
                                <span id="api_job_start_${vs.index}" style="display: none;"><a href="javascript:startApiJob('${etlEntity.id}','${vs.index}');" target="rightFrame"><img src="${baseUrl}/img/bianji.png">执行API任务</a></span>
                                <span id="api_job_stop_${vs.index}"><a href="javascript:stopApiJob('${etlEntity.id}','${vs.index}');" target="rightFrame"><img src="${baseUrl}/img/sync.png">停止API任务</a></span>
                            </c:if>
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
                        <li><a href="${baseUrl}/web/etl/entityList.do?prjId=${prjId}&page=${currPage - 1}">上一页</a></li>
                    </c:if>
                    <li id="currPage">${currPage}</li>
                    <c:if test="${currPage >= totalPage}">
                        <li><a href="#">下一页</a></li>
                    </c:if>
                    <c:if test="${currPage < totalPage}">
                        <li><a href="${baseUrl}/web/etl/entityList.do?prjId=${prjId}&page=${currPage + 1}">下一页</a></li>
                    </c:if>
                </ul>
            </div>
        </div>
    </div>
</div>
</body>
</html>