<%--
  Created by IntelliJ IDEA.
  User: DJCY
  Date: 2024/4/20
  Time: 17:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>销售人员营销额</title>
    <meta charset="utf-8"/>
    <link rel="stylesheet" href="css/bootstrap.css"/>
    <script src="/js/logout.js"></script>
</head>
<body>
<div class="container-fluid">

    <jsp:include page="/admin/header.jsp"></jsp:include>

    <div class="text-right">
        <a class="btn btn-warning" href="/admin/sellman_add.jsp">添加销售人员</a>
        <a class="btn btn-warning" href="/admin/draw_kpi.jsp">商品销售情况可视化</a>
    </div>


    <br>

    <ul role="tablist" class="nav nav-tabs">
        <li role="presentation"><a href="/admin/sellman_list">全部销售人员</a></li>
        <li class="active" role="presentation"><a href="/admin/sellman_kpi_list">销售人员营销情况</a></li>
    </ul>
    <br/>
    <c:if test="${!empty msg }">
        <div class="alert alert-success">${msg }</div>
    </c:if>
    <c:if test="${!empty failMsg }">
        <div class="alert alert-danger">${failMsg }</div>
    </c:if>


    <br>

    <table class="table table-bordered table-hover">

        <tr>
            <th width="5%">ID</th>
            <th width="10%">名字</th>
            <th width="10%">负责的类目id</th>
            <th width="25%">销售额(CNY)</th>
        </tr>

        <c:forEach items="${p.list }" var="g" varStatus="loop">
            <tr>
                <td><p>${g.id }</p></td>
                <td><p>${g.username}</p></td>
                <td><p>${g.selltypeid}</p></td>
                <td><p>${kpi[loop.index]}</p></td>
            </tr>
        </c:forEach>


    </table>

    <br>
    <jsp:include page="/page.jsp">
        <jsp:param value="/admin/sellman_kpi_list" name="url"/>
    </jsp:include>
    <br>
</div>
</body>
</html>
