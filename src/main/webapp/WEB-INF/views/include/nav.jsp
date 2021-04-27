<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script>
$(function() {
	$("#logout").on("click", function() {
		var $input = $("<input>").attr("type","hidden").attr("name","_csrf").val('${_csrf.token}');
		$("<form>").attr("action","/zboard3/user/logout").attr("method","post").append($input).appendTo("body").submit();
	});
})
</script>
</head>
<body>
<div id="nav" class="navbar navbar-inverse">
	<div class="container-fluid">
		<div class="navbar-header">
			<a class="navbar-brand" href="/zboard3">ICIA</a>
		</div>
		<ul class="nav navbar-nav" id="menu_parent">
			<sec:authorize access="isAnonymous()">
				<li><a href="/zboard3/user/find">아이디찾기</a></li>
				<li><a href="/zboard3/user/find">비밀번호 찾기</a></li>
				<li><a href="/zboard3/user/join">회원가입</a></li>
			</sec:authorize>
          	<sec:authorize access="hasRole('ROLE_USER')">
          		<li><a href='/zboard3/user/info'>내정보</a></li>
          		<li><a href='/zboard3/board/write'>글쓰기</a></li>
          		<li><a href='/zboard3/memo/receive'>받은 메모</a></li>
				<li><a href='/zboard3/memo/send'>보낸 메모</a></li>
				<li><a id='logout' href='#'>로그아웃</a></li>
			</sec:authorize>
			<sec:authorize access="hasRole('ROLE_ADMIN')">
				<li class="dropdown">
					<a class="dropdown-toggle" data-toggle="dropdown" href="#">관리자<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="/zboard3/system/stat">통계</a></li>
		      		 	<li><a href="/zboard3/mall/product">상품 등록</a></li>
					</ul>
				</li>
	        </sec:authorize>
		</ul>
		<ul class="nav navbar-nav navbar-right">
			<li><a href="/zboard3/system/stat">통계</a></li>
			<li><a href="/zboard3/product/list">shop</a></li>
			<sec:authorize access="isAnonymous()">
				<li><a href="/zboard3/user/login">로그인</a></li>
			</sec:authorize>
			<sec:authorize access="isAuthenticated()">
				<li><a href="#" id="logout">로그아웃</a></li>
			</sec:authorize>
    	</ul>
	</div>
</div>
</body>
</html>



