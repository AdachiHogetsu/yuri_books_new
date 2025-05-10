<%--
  Created by IntelliJ IDEA.
  User: DJCY
  Date: 2023/11/08
  Time: 6:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<!DOCTYPE html>
<html>
<head>
	<title>添加销售人员</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link type="text/css" rel="stylesheet" href="../css/bootstrap.css">
	<link type="text/css" rel="stylesheet" href="../css/style.css">
	<script type="text/javascript" src="../js/jquery.min.js"></script>
	<script type="text/javascript" src="../js/bootstrap.min.js"></script>
	<script type="text/javascript" src="../js/simpleCart.min.js"></script>
	<script src="/js/logout.js"></script>
</head>
<body>


	<!--account-->
	<div class="account">
		<div class="container">
			<div class="register">
				<c:if test="${!empty msg }">
					<div class="alert alert-danger">${msg }</div>
				</c:if>
				<form action="/admin/sellman_user_rigister" method="post">
					<div class="register-top-grid">
						<h3>添加销售人员</h3>
						<div class="input">
							<span>用户名 <label style="color:red;">*</label></span>
							<input type="text" name="username" placeholder="请输入用户名" required="required">
						</div>
						<div class="input">
							<span>密码 <label style="color:red;">*</label></span>
							<input type="password" name="password" placeholder="请输入密码" required="required">
						</div>
						<div class="input">
							<label for="select_topic" class="col-sm-1 control-label">类目</label>
							<div class="col-sm-6">
								<select class="form-control" id="select_topic" name="typeid">

									<c:forEach items="${typeList }" var="t">
										<option value="${t.id }">${t.name }</option>
									</c:forEach>


								</select>
							</div>
						</div>
						<div class="clearfix"> </div>
					</div>
					<div class="register-but text-center">
					   <input type="submit" value="提交">
					   <div class="clearfix"> </div>
					</div>
				</form>
				<div class="clearfix"> </div>
			</div>
	    </div>
	</div>
	<!--//account-->

	<!--footer-->
	<jsp:include page="/footer.jsp"></jsp:include>
	<!--//footer-->
</body>
</html>