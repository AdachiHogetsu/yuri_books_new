<%--
  Created by IntelliJ IDEA.
  User: DJCY
  Date: 2023/11/10
  Time: 17:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>商品列表</title>
    <meta charset="utf-8"/>
    <link rel="stylesheet" href="css/bootstrap.css"/>
    <script src="/js/logout.js"></script>
</head>
<body>
<div class="container-fluid">

    <jsp:include page="/sellman/header.jsp"></jsp:include>

    <div class="text-right"><a class="btn btn-warning" href="/sellman/goods_add.jsp?type_id=${type_id}">添加商品</a></div>

    <br>

    <ul role="tablist" class="nav nav-tabs">
        <li <c:if test="${type==0 }">class="active"</c:if> role="presentation"><a href="/sellman/goods_list">全部商品</a></li>
        <li <c:if test="${type==1 }">class="active"</c:if> role="presentation"><a href="/sellman/goods_list?type=1">条幅推荐</a></li>
        <li <c:if test="${type==2 }">class="active"</c:if> role="presentation"><a href="/sellman/goods_list?type=2">热销推荐</a></li>
        <li <c:if test="${type==3 }">class="active"</c:if> role="presentation"><a href="/sellman/goods_list?type=3">新品推荐</a></li>
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
            <th width="10%">图片</th>
            <th width="10%">名称</th>
            <th width="15%">介绍</th>
            <th width="10%">价格</th>
            <th width="5%">库存</th>
            <th width="10%">类目</th>
            <th width="7.5%">销售量</th>
            <th width="7.5%">销售额</th>
            <th width="15%">操作</th>
        </tr>

        <c:forEach items="${p.list }" var="g" varStatus="loop">
            <tr>
                <td><p>${g.id }</p></td>
                <td><p><img src="${g.cover}" width="100px" height="100px"></p></td>
                <td><p>${g.name}</p></td>
                <td><p>${g.intro}</p></td>
                <td><p>${g.price}</p></td>
                <td><p>${g.stock}</p></td>
                <td><p>${g.type.name}</p></td>
                <td><p>${amountValues[loop.index]}</p></td>
                <td><p>${amountValues[loop.index]*g.price}</p></td>
                <td>
                    <p>
                        <c:choose>
                            <c:when test="${g.isScroll }">
                                <a class="btn btn-info" href="/sellman/goods_recommend?id=${g.id }&method=remove&typeTarget=1&pageNumber=${p.pageNumber}&type=${type}&typeId=${typeIds[loop.index]}">移出条幅</a>
                            </c:when>
                            <c:otherwise>
                                <a class="btn btn-primary" href="/sellman/goods_recommend?id=${g.id }&method=add&typeTarget=1&pageNumber=${p.pageNumber}&type=${type}&typeId=${typeIds[loop.index]}">加入条幅</a>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${g.isHot }">
                                <a class="btn btn-info" href="/sellman/goods_recommend?id=${g.id }&method=remove&typeTarget=2&pageNumber=${p.pageNumber}&type=${type}&typeId=${typeIds[loop.index]}">移出热销</a>
                            </c:when>
                            <c:otherwise>
                                <a class="btn btn-primary" href="/sellman/goods_recommend?id=${g.id }&method=add&typeTarget=2&pageNumber=${p.pageNumber}&type=${type}&typeId=${typeIds[loop.index]}">加入热销</a>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${g.isNew }">
                                <a class="btn btn-info" href="/sellman/goods_recommend?id=${g.id }&method=remove&typeTarget=3&pageNumber=${p.pageNumber}&type=${type}&typeId=${typeIds[loop.index]}">移出新品</a>
                            </c:when>
                            <c:otherwise>
                                <a class="btn btn-primary" href="/sellman/goods_recommend?id=${g.id }&method=add&typeTarget=3&pageNumber=${p.pageNumber}&type=${type}&typeId=${typeIds[loop.index]}">加入新品</a>
                            </c:otherwise>
                        </c:choose>

                    </p>
                    <a class="btn btn-success" href="/sellman/goods_editshow?id=${g.id }& pageNumber=${p.pageNumber}&type=${type}&typeId=${typeIds[loop.index]}">修改</a>
                    <a class="btn btn-danger" href="/sellman/goods_delete?id=${g.id }&pageNumber=${p.pageNumber}&type=${type}&typeId=${typeIds[loop.index]}">删除</a>
                </td>
            </tr>
        </c:forEach>


    </table>

    <br>
    <jsp:include page="/page.jsp">
        <jsp:param value="/sellman/goods_list" name="url"/>
        <jsp:param value="&type=${type }" name="param"/>
    </jsp:include>
    <br>
</div>
</body>
</html>
