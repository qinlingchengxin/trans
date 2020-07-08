<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="baseUrl" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>映射表</title>
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
                url: "${baseUrl}/web/etl/entitySave.do",
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
                url: "${baseUrl}/web/etl/entityAdd.do",
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

        function changePk(tableName, type) {
            var prjId = $("#prjId").val();
            var tab_id_node;
            var node;
            if (type == 0) {
                node = $("#doc-ipt-srcPrimaryKey-1");
                tab_id_node = $("#src_tab_" + tableName);
                $("#srcTabId").val(tab_id_node.attr("data_tab_id"));
            } else {
                node = $("#doc-ipt-desPrimaryKey-1");
                tab_id_node = $("#des_tab_" + tableName);
                $("#desTabId").val(tab_id_node.attr("data_tab_id"));
            }

            node.empty();

            $.ajax({
                url: "${baseUrl}/web/etl/primaryKey.do",
                type: "GET",
                data: {"prjId": prjId, "tableName": tableName, "type": type},
                dataType: "json",
                success: function (result) {
                    if (result.code == 1000) {
                        var pks = result.data;
                        var optionNodes = "";
                        if (pks.length > 0) {
                            for (var i = 0; i < pks.length; i++) {
                                optionNodes += '<option value="' + pks[i].name + '">' + pks[i].name + '</option>';
                            }
                        }

                        node.html($(optionNodes));
                    } else {
                        alert(result.msg);
                    }
                }
            });
        }

        function checkNumber(node) {
            if (!/^\d+$/.test(node.value)) {
                alert("请输入正确数字！");
                node.value = 0;
                return;
            }
        }

        function checkedDesField(srcField) {
            var selNode = $("#doc-ipt-desPrimaryKey-1");
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
            <a href="javascript:history.go(-1);">返回</a><span>></span><a href="javascript:void(0)">映射表</a>
        </div>
        <div class="second">
            <form id="form">
                <input type="hidden" id="id" name="id" value="${etlEntity.id}"/>

                <input type="hidden" id="prjId" name="prjId" value="${etlEntity.prjId}"/>
                <input type="hidden" id="srcTabId" name="srcTabId" value="${etlEntity.srcTabId}"/>
                <input type="hidden" id="desTabId" name="desTabId" value="${etlEntity.desTabId}"/>

                <div>
                    <span class="title">原表名</span>
                    <select id="doc-ipt-srcTabName-1" name="srcTabName" onchange="changePk(this.value, 0);">
                        <c:forEach items="${srcTables}" var="srcTable">
                            <option id="src_tab_${srcTable.name}" data_tab_id="${srcTable.id}" value="${srcTable.name}"
                                    <c:if test="${fn:toUpperCase(srcTable.name) == fn:toUpperCase(etlEntity.srcTabName)}">selected="selected"</c:if>>${srcTable.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div>
                    <span class="title">目标表名</span>
                    <select id="doc-ipt-desTabName-1" name="desTabName" onchange="changePk(this.value, 1);">
                        <c:forEach items="${desTables}" var="desTable">
                            <option id="des_tab_${srcTable.name}" data_tab_id="${desTable.id}" value="${desTable.name}"
                                    <c:if test="${fn:toUpperCase(desTable.name) == fn:toUpperCase(etlEntity.desTabName)}">selected="selected"</c:if>>${desTable.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div>
                    <span class="title">原标识字段</span>
                    <select id="doc-ipt-srcPrimaryKey-1" name="srcPrimaryKey" onchange="checkedDesField(this.value);">
                        <c:forEach items="${srcFields}" var="srcPk">
                            <option value="${srcPk.name}" <c:if test="${fn:toUpperCase(srcPk.name) == fn:toUpperCase(etlEntity.srcPrimaryKey)}">selected="selected"</c:if>>${srcPk.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div>
                    <span class="title">目标标识字段</span>
                    <select id="doc-ipt-desPrimaryKey-1" name="desPrimaryKey">
                        <c:forEach items="${desFields}" var="desPk">
                            <option value="${desPk.name}" <c:if test="${fn:toUpperCase(desPk.name) == fn:toUpperCase(etlEntity.desPrimaryKey)}">selected="selected"</c:if>>${desPk.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div>
                    <span class="title">是否重复执行</span>
                    <select id="doc-ipt-repeat-1" name="repeat">
                        <option value="0" <c:if test="${etlEntity.repeat == 0}">selected="selected"</c:if>>否</option>
                        <option value="1" <c:if test="${etlEntity.repeat == 1}">selected="selected"</c:if>>是</option>
                    </select>

                    <span class="title">任务类型</span>
                    <select id="doc-ipt-scheduleType-1" name="scheduleType">
                        <option value="1" <c:if test="${etlEntity.scheduleType == 1}">selected="selected"</c:if>>间隔</option>
                        <option value="2" <c:if test="${etlEntity.scheduleType == 2}">selected="selected"</c:if>>天</option>
                        <option value="3" <c:if test="${etlEntity.scheduleType == 3}">selected="selected"</c:if>>周</option>
                        <option value="4" <c:if test="${etlEntity.scheduleType == 4}">selected="selected"</c:if>>月</option>
                    </select>
                </div>

                <div>

                    <span class="title">间隔秒</span>
                    <input style="width: 40px;" id="doc-ipt-intervalSecond-1" type="text" name="intervalSecond" value="${etlEntity.intervalSecond}" onchange="checkNumber(this);"/>

                    <span class="title">间隔分</span>
                    <input style="width: 40px;" id="doc-ipt-intervalMinute-1" type="text" name="intervalMinute" value="${etlEntity.intervalMinute}" onchange="checkNumber(this);"/>
                </div>

                <div>
                    <span class="title">固定时</span>
                    <select id="doc-ipt-fixedHour-1" name="fixedHour">
                        <c:forEach begin="0" end="23" var="v">
                            <option value="${v}" <c:if test="${etlEntity.fixedHour == v}">selected="selected"</c:if>>${v}</option>
                        </c:forEach>
                    </select>

                    <span class="title">固定分钟</span>
                    <select id="doc-ipt-fixedMinute-1" name="fixedMinute">
                        <c:forEach begin="0" end="59" var="v">
                            <option value="${v}" <c:if test="${etlEntity.fixedMinute == v}">selected="selected"</c:if>>${v}</option>
                        </c:forEach>
                    </select>
                </div>

                <div>
                    <span class="title">固定周</span>
                    <select id="doc-ipt-fixedWeekday-1" name="fixedWeekday">
                        <option value="0" <c:if test="${etlEntity.fixedWeekday == 0}">selected="selected"</c:if>>周日</option>
                        <option value="1" <c:if test="${etlEntity.fixedWeekday == 1}">selected="selected"</c:if>>周一</option>
                        <option value="2" <c:if test="${etlEntity.fixedWeekday == 2}">selected="selected"</c:if>>周二</option>
                        <option value="3" <c:if test="${etlEntity.fixedWeekday == 3}">selected="selected"</c:if>>周三</option>
                        <option value="4" <c:if test="${etlEntity.fixedWeekday == 4}">selected="selected"</c:if>>周四</option>
                        <option value="5" <c:if test="${etlEntity.fixedWeekday == 5}">selected="selected"</c:if>>周五</option>
                        <option value="6" <c:if test="${etlEntity.fixedWeekday == 6}">selected="selected"</c:if>>周六</option>
                    </select>

                    <span class="title">固定日</span>
                    <select id="doc-ipt-fixedDay-1" name="fixedDay">
                        <c:forEach begin="1" end="31" var="v">
                            <option value="${v}" <c:if test="${etlEntity.fixedDay == v}">selected="selected"</c:if>>${v}</option>
                        </c:forEach>
                    </select>
                </div>

                <div>
                    <span class="title" style="vertical-align: top;">描述</span>
                    <textarea id="doc-ipt-description-1" name="description">${etlEntity.description}</textarea>
                </div>

                <div>
                    <c:if test='${etlEntity.id  == null}'>
                        <span id="btn_add" class="save" onclick="add();">添加</span>
                    </c:if>
                    <c:if test='${etlEntity.id != null}'>
                        <span class="save" onclick="save();">保存</span>
                    </c:if>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>