<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	/* float을 clear하는 방법 -> overflow:hidden; */
	#wrap { width: 780px; margin: 0 auto; overflow: hidden;}
	#find_id { width: 50%; float: left; margin-top: 50px; text-align: center;}
	#reset_pwd { width: 50%; float: right; margin-top: 50px; }
</style>
<script>
$(document).ready(function() {
	$("#username").on("blur", function() {
		var username = $("#username").val().toUpperCase();
		$("#username").val(username);
	});
	
	$("#findId").on("click", function() {
		$.ajax("/zboard3/users/username/email?email="+$("#email_left").val())
			.done((username)=>{ 
				$("#email_left").val("");
				Swal.fire("아이디를 찾았습니다", "고객님의 아이디:"+username, "success");
			})
			.fail(()=>Swal.fire("검색 실패", "아이디를 찾지 못했습니다", "error"));
	});
	
	$("#resetPwd").on("click", function() {
		$("#reset_form").attr("action","/zboard3/user/reset_pwd").attr("method","post").submit();
	})
})
</script>
</head>
<body>
	<div id="wrap">
		<div id="find_id">
			<div class="form-group">
				<label for="email_left">이메일</label>
				<span id="email_msg_left"></span>
				<input type="text" name="email" id="email_left">
			</div>
			<button type="button" class="btn btn-success" id="findId">아이디 찾기</button>
		</div>
		<div id="reset_pwd">
			<form id="reset_form">
				<div class="form-group">
					<label for="username">아이디</label>
					<span id="username_msg"></span>
					<input type="text" name="username" id="username">
				</div>
				<div class="form-group">
					<label for="email_right">이메일</label>
					<span id="email_msg_right"></span>
					<input type="text" name="email" id="email_right">
				</div>
				<input type="hidden" name="_csrf" value="${_csrf.token }">
				<button type="button" class="btn btn-success" id="resetPwd">비밀번호 찾기</button>
			</form>
		</div>
	</div>
</body>
</html>














