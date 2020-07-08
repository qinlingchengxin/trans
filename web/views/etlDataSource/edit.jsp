<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="baseUrl" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>数据源</title>
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
                url: "${baseUrl}/web/etl/dataSourceSave.do",
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
                url: "${baseUrl}/web/etl/dataSourceAdd.do",
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

        function testConn() {
            var form = $('#form');
            var baseObj = form.serializeObject();
            $.ajax({
                url: "${baseUrl}/web/etl/testConnDs.do",
                type: "POST",
                data: baseObj,
                dataType: "json",
                success: function (result) {
                    if (result.code == 1000) {
                        alert('链接成功');
                    } else {
                        alert("链接失败");
                    }
                }
            });
        }

        function chPort(val) {
            var portNode = $("#doc-ipt-dbPort-1");
            if (val == 0) {
                portNode.val(3306);
                $("#div_dbSchema").hide();
            } else if (val == 1) {
                portNode.val(1521);
                $("#div_dbSchema").hide();
            } else if (val == 2) {
                portNode.val(1433);
                $("#div_dbSchema").hide();
            } else if (val == 3) {
                portNode.val(54321);
                $("#div_dbSchema").show();
            }
        }

        $(function () {
            var dbType = "${dataSource.dbType}";
            if (dbType == 3) {
                $("#div_dbSchema").show();
            }
        });
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
            <a href="javascript:history.go(-1);">返回</a><span>></span><a href="javascript:void(0)">数据源</a>
        </div>
        <div class="second">
            <form id="form">
                <input type="hidden" id="id" name="id" value="${dataSource.id}"/>

                <div>
                    <span class="title_self">数据源名称</span>
                    <input id="doc-ipt-sourceName-1" type="text" name="sourceName" value="${dataSource.sourceName}"/>
                </div>

                <div>
                    <span class="title_self">类型</span>

                    <select id="doc-ipt-dbType-1" name="dbType" onchange="chPort(this.value);">
                        <option value="0" <c:if test="${dataSource.dbType == 0}">selected="selected"</c:if>>MySql</option>
                        <option value="1" <c:if test="${dataSource.dbType == 1}">selected="selected"</c:if>>Oracle</option>
                        <%--<option value="2" <c:if test="${dataSource.dbType == 2}">selected="selected"</c:if>>MS SQL</option>--%>
                        <option value="3" <c:if test="${dataSource.dbType == 3}">selected="selected"</c:if>>KingBase</option>
                    </select>
                </div>

                <div>
                    <span class="title_self">IP</span>
                    <input id="doc-ipt-dbIp-1" type="text" name="dbIp" value="${dataSource.dbIp}"/>
                </div>

                <div>
                    <span class="title_self">端口</span>
                    <input id="doc-ipt-dbPort-1" type="text" name="dbPort" value="${dataSource.dbPort == 0 ? 3306 : dataSource.dbPort}"/>
                </div>

                <div>
                    <span class="title_self">数据库名称</span>
                    <input id="doc-ipt-dbName-1" type="text" name="dbName" value="${dataSource.dbName}"/>
                </div>

                <div id="div_dbSchema" style="display: none;">
                    <span class="title_self">数据库模式</span>
                    <input id="doc-ipt-dbSchema-1" type="text" name="dbSchema" value="${dataSource.dbSchema}"/>
                </div>

                <div>
                    <span class="title_self">用户名</span>
                    <input id="doc-ipt-dbUsername-1" type="text" name="dbUsername" value="${dataSource.dbUsername}"/>
                </div>
                <div>
                    <span class="title_self">用户密码</span>
                    <input id="doc-ipt-dbPwd-1" type="password" name="dbPwd" value="${dataSource.dbPwd}"/>
                </div>

                <div>
                    <c:if test='${isView  == 0}'>
                        <c:if test='${dataSource.id  == null}'>
                            <span id="btn_add" class="save" onclick="add();">添加</span>
                        </c:if>
                        <c:if test='${dataSource.id != null}'>
                            <span class="save" onclick="save();">保存</span>
                        </c:if>
                    </c:if>
                    <span class="save" onclick="testConn();">点此测试</span>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>