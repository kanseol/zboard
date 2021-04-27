<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="/zboard3/user/password_check" method="post">
		<div class="form-group">
			<label for="password">비밀번호</label>
			<input id="password" type="password" name="password" class="form-control">
			<span class="helper-text" id="password_msg"></span>
		</div>
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
		<button class="btn btn-success" id="passwordCheck">비밀번호 확인</button>
	</form>
</body>
</html>


