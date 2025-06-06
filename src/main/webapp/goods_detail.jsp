<%--
  Created by IntelliJ IDEA.
  User: DJCY
  Date: 2023/11/12
  Time: 20:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
    <title>商品详情</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link type="text/css" rel="stylesheet" href="css/bootstrap.css">
    <link type="text/css" rel="stylesheet" href="css/style.css">
    <link type="text/css" rel="stylesheet" href="css/flexslider.css">
    <script type="text/javascript" src="js/jquery.min.js"></script>
    <script type="text/javascript" src="js/jquery.flexslider.js"></script>
    <script type="text/javascript" src="js/bootstrap.min.js"></script>
    <script type="text/javascript" src="layer/layer.js"></script>
    <script type="text/javascript" src="js/cart.js"></script>
    <script src="js/logout.js"></script>
    <script>
        $(function() {
            $('.flexslider').flexslider({
                animation: "slide",
                controlNav: "thumbnails"
            });
        });
    </script>
    <script>
        var xhr = new XMLHttpRequest();
        var url = "/visit";
        xhr.open("GET", url, true);
        xhr.send();
    </script>
    <script>
        window.addEventListener('beforeunload', function(event) {
            var xhr = new XMLHttpRequest();
            var url = "/visit?goodsTypeid=${g.type.id}&userId=${user.id}";
            xhr.open("POST", url, true);
            xhr.send();
        });
    </script>

</head>
<body>







<!--header-->
<jsp:include page="/header.jsp"></jsp:include>
<!--//header-->


<!--//single-page-->
<div class="single">
    <div class="container">
        <div class="single-grids">
            <div class="col-md-4 single-grid">
                <div class="flexslider">

                    <ul class="slides">
                        <li data-thumb="${g.cover}">
                            <div class="thumb-image"> <img src="${g.cover}" data-imagezoom="true" class="img-responsive"> </div>
                        </li>
                        <li data-thumb="${g.image1}">
                            <div class="thumb-image"> <img src="${g.image1}" data-imagezoom="true" class="img-responsive"> </div>
                        </li>
                        <li data-thumb="${g.image2}">
                            <div class="thumb-image"> <img src="${g.image2}" data-imagezoom="true" class="img-responsive"> </div>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="col-md-4 single-grid simpleCart_shelfItem">
                <h3>${g.name}</h3>
                <div class="tag">
                    <p>分类 : <a href="goods_list?typeid=${g.type.id}">${g.type.name}</a></p>
                </div>
                <p>${g.intro}</p>
                <p>库存 : ${g.stock}</p>
                <div class="galry">
                    <div class="prices">
                        <h5 class="item_price">¥ ${g.price}</h5>
                    </div>
                    <div class="clearfix"></div>
                </div>
                <div class="btn_form">
                    <a href="javascript:;" class="add-cart item_add" onclick="buy(${g.id})">加入购物车</a>
                </div>
            </div>
            <div class="col-md-4 single-grid1">
                <!-- <h2>商品分类</h2> -->
                <ul>
                    <li><a  href="/goods_list">全部系列</a></li>

                    <c:forEach items="${typeList}" var="t">
                        <li><a href="/goods_list?typeid=${t.id}">${t.name}</a></li>
                    </c:forEach>
                </ul>
            </div>
            <div class="clearfix"> </div>
        </div>
    </div>
</div>


<!--footer-->
<jsp:include page="/footer.jsp"></jsp:include>
<!--//footer-->


</body>
</html>