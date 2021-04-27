<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
.success {
	color: green;
	font-size: 0.75em;
}

.fail {
	color: red;
	font-size: 0.75em;
}
</style>
<script>
// 아이디 체크, 이메일 체크, 비밀번호 체크, 이름 체크, 생일 체크
// : 입력값을 읽어와서 비어있는 지 확인 -> 패턴을 통과하는 지 확인 후 필요하면 에러메시지 출력 -> return true
//   value, pattern, msgElement, message
function check(value, pattern, msgElement, message) {
	if(value=="") {
		msgElement.text("필수 입력입니다").attr("class", "fail");
		return false;
	}
	if(pattern.test(value)==false) {
		msgElement.text(message).attr("class", "fail");
		return false;
	}
	return true;
}

function usernameCheck() {
	var $username = $("#username").val().toUpperCase();
	$("#username").val($username);
	var pattern = /^[0-9A-Z]{8,10}$/;
	return check($username, pattern, $("#username_msg"), "아이디는 대문자와 숫자 8~10자입니다")
}

function emailCheck() {
	var $email = $("#email").val();
	var pattern = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
	return check($email, pattern, $("#email_msg"), "정확한 이메일을 입력하세요")
}

function passwordCheck() {
	$("#password_msg").text("");
	var $password = $("#password").val();
	var pattern = /(?=.*[!@#$%^&*])^[A-Za-z0-9!@#$%^&*]{8,10}$/;
	return check($password, pattern, $("#password_msg"), 
			"비밀번호는 특수문자를 하나 이상 포함하는 영숫자와 특수문자 8~10자입니다")
}

function irumCheck() {
	$("#irum_msg").text("");
	var $irum = $("#irum").val();
	var pattern = /^[가-힣]{2,}$/;
	return check($irum, pattern, $("#irum_msg"), "이름은 한글 2자 이상입니다")
}
function birthdayCheck() {
	$("#birthday_msg").text("");
	var $birthday = $("#birthday").val();
	var pattern = /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/;
	return check($birthday, pattern, $("#birthday_msg"), "정확한 생일을 입력하세요")
}

function password2Check() {
	var $password2 = $("#password2").val();
	if($password2=="") {
		$("#password2_msg").text("필수입력입니다").attr("class","fail");
		return false;
	} 
	if($password2!==$("#password").val()) {
		$("#password2_msg").text("비밀번호가 일치하지 않습니다").attr("class","fail");
		return false;
	}
	return true;
}

function loadProfile() {
	var file = $("#profile")[0].files[0];
	var maxSize = 1024*1024;			
	if(file.size>maxSize) {
		Swal.fire('프로필 크기 오류', '프로필 사진은 1MB를 넘을 수 없습니다','error');
		$("#profile").val("");
		$("#show_profile").removeAttr("src");
		return false;
	}
	var reader = new FileReader();
	reader.readAsDataURL(file);
	reader.onload = function() {
		$("#show_profile").attr("src", reader.result);
	}
	return true;
}

function printMessage(msgElement, message, css) {
	msgElement.text(message).attr("class",css);
}

function join() {
	var formData = new FormData($("#join_form")[0]);
	$.ajax({
		url: "/zboard3/users/new",
		method: "post",
		data: formData,
		processData: false,
		contentType: false
	}).done(()=>Swal.fire("가입신청 완료","이메일을 확인하세요", "success"))
	.fail((msg)=>Swal.fire('가입신청 실패', msg,'error'));
}

$(document).ready(function() {
	// 이벤트와 처리함수(핸들러) 등록
	$("#profile").on("change", loadProfile);
	$("#password").on("blur", passwordCheck);
	$("#irum").on("blur", irumCheck);
	$("#password2").on("blur", password2Check);
	$("#birthday").on("blur", birthdayCheck);

	var usernameUrl = "/zboard3/users/user/username?username=";
	var emailUrl = "/zboard3/users/user/email?email=";

	// 아이디가 사용여부에 대한 메시지 출력 (회원 가입할 때 재 확인이 필요하다)
	$("#username").on("blur", function() {
		if(usernameCheck()==false)
			return false;
		$.ajax(usernameUrl+$("#username").val())
			.done(()=>printMessage($("#username_msg"), "좋은 아이디네요", "success"))
			.fail(()=>printMessage($("#username_msg"), "사용중인 아이디입니다", "fail"));	
	});
	$("#email").on("blur", function() {
		if(emailCheck()==false)
			return false;
		$.ajax(emailUrl+$("#email").val())
			.done(()=>printMessage($("#email_msg"), "사용가능한 이메일입니다", "success"))
			.fail(()=>printMessage($("#email_msg"), "사용중인 이메일입니다", "fail"));	
	});

	$("#join").on("click", function() {
		var r1 = usernameCheck();
		var r2 = emailCheck();
		var r3 = passwordCheck();
		var r4 = password2Check();
		var r5 = irumCheck();
		var r6 = birthdayCheck();
		if((r1&&r2&&r3&r4&&r5&&r6)==false)
			return false;

		$.when($.ajax(usernameUrl+$("#username").val()), $.ajax(emailUrl+$("#email").val()))
			.done(()=>join()).fail(()=>Swal.fire('확인 실패','아이디나 이메일이 사용중입니다',"error"));
	})
	
})
</script>
</head>
<body>
	<form id="join_form">
		<div id="wrap">
			<img id="show_profile" height="240px">
			<input type="hidden" name="_csrf" value="${_csrf.token }">
			<div class="form-group">
				<label for="profile">프로필 사진</label>
				<input id="profile" type="file" name="profile" class="form-control"  accept=".jpg,.jpeg,.png,.gif">
			</div>
			<div>
				<label for="username">아이디</label>
				<span id="username_msg"></span>
				<div class="form-group">
					<input type="text" class="form-control" id="username" name="username">
				</div>
			</div>
			<div>
				<label for="irum">이름</label>
				<span id="irum_msg"></span>
				<div class="form-group">
					<input type="text" class="form-control" id="irum" name="irum">
				</div>
			</div>
			<div>
				<label for="password">비밀번호</label>
				<span id="password_msg"></span>
				<div class="form-group">
					<input id="password" type="password" class="form-control" name="password">
				</div>
			</div>
			<div>
				<label for="password2">비밀번호 확인</label>
				<span id="password2_msg"></span>
				<div class="form-group">
					<input id="password2" type="password" class="form-control">
				</div>	
			</div>
			<div>
				<label for="email">이메일</label>
				<span id="email_msg"></span>
				<div class="form-group">
					<input id="email" type="text" name="email" class="form-control">
				</div>
			</div>
			<div>
				<label for="birthday">생년월일</label>
				<span id="birthday_msg"></span>
				<div class="form-group">
					<input id="birthday" type="date" name="birthday" class="form-control">
				</div>
			</div>
			<div class="form-group" style="text-align: center;">
				<button type="button" id="join" class="btn btn-info">가입</button>&nbsp;&nbsp;&nbsp;&nbsp;
				<button type="button" id="home" class="btn btn-primary">HOME</button>
			</div>
		</div>
	</form>
</body>
</html>