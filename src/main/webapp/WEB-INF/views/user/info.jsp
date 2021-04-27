<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	#user td {
		height: 60px;
		line-height: 60px;
	}
	
	#user td input {
		height: 25px;
	}
	
	.first {
		background-color: #f3f3f3;
		text-align: center;
	}
	#profile_sajin {
		line-height: 25px;
	}
	.key {
		width: 35%;
		display: inline-block;
	}
</style>
<script>
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

function makePage() {
	$("#passwordArea").hide();
	$("#activateChangePwd").on("click", function() {
		$("#passwordArea").toggle();
	});

	var email = '${user.email}'.split("@");
	$("#email1").val(email[0]);
	// disabled, checked, selected, readonly
	$("#email2").val(email[1]).prop("disabled", true);

	var $options = $("#selectEmail option");
	
	// $.each : jQuery 객체 전용 함수, 범용 함수
	// $.each(집합, function(인덱스, 원소 )) -> 범용
	
	var isFind = false;
	$.each($options, function(idx, option) {
		// email[1]이 daum, gmail, naver 중 하나일 경우 
		if($(option).text()===email[1]) {
			$(option).prop("selected", true);
			// 이메일 서버를 찾았다는 정보를 저장
			isFind = true;
		}
	});

	// 이메일 서버를 못찾았다 -> 직접 입력을 선택 -> 이메일 서버 편집이 가능해야 한다
	if(isFind==false)
		$("#email2").prop("disabled", false);


}

$(document).ready(function() {
	makePage();
	$("#profile").on("change", loadProfile);
	
	$("#selectEmail").on("change", function() {
		var $choice = $("#selectEmail").val();
		if($choice=="직접 입력") 
			$("#email2").prop("disabled", false).val("").attr("placeholder", "직접 입력").focus();
		else 
			$("#email2").val($choice).prop("disabled", true);
	});
	
	// 이름 변경
	$("#changeIrum").on("click", function() {
		var params = {
			_method: "patch",
			irum: $("#irum").val(),
			_csrf: "${_csrf.token}"
		}
		$.ajax({
			url : "/zboard3/users/username",
			data : params,
			method: "post"
		}).done(()=>alert("성공")).fail(()=>alert("실패"));		
	})
	
	// 비밀번호 변경
	$("#changePwd").on("click", function() {
		var $password = $("#password").val();
		var $newPassword = $("#newPassword").val();
		var $newPassword2 = $("#newPassword2").val();

		// 비밀번호는 특수문자를 포함하는 영숫자와 특수문자 8~10자
		// 임시비밀번호는 영숫자 20자 
		var pattern1 = /(?=.*[!@#$%^&*])^[A-Za-z0-9!@#$%^&*]{8,10}$/;
		var pattern2 = /^[A-Za-z0-9]{20}$/;
		if(pattern1.test($password)==false && pattern2.test($password)==false ) {
			alert("비밀번호를 확인하세요")
			return false;
		}
		if(pattern1.test($newPassword)==false) {
			alert("새 비밀번호는 특수문자를 포함하는 영숫자와 특수문자 8~10자 입니다");
			return false;
		}
		if($newPassword!==$newPassword2) {
			alert("새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다");
			return false;
		}
		var params = {
			password : $password,
			newPassword: $newPassword,
			_csrf: "${_csrf.token}",
			_method: "patch"
		}
		$.ajax({
			url : "/zboard3/users/password",
			data : params,
			method: "post"
		}).done(()=>alert("성공")).fail((msg)=>console.log(msg.responseText));		
	});

	// 전체 변경
	$("#update").on("click", function() {
		var formData = new FormData();
		if($("#profile")[0].files[0]!=undefined)
			formData.append("profile", $("#profile")[0].files[0]);
		formData.append("irum", $("#irum").val());
		formData.append("email", $("#email1").val() + "@" + $("#email2").val());
		formData.append("_csrf", "${_csrf.token}");

		var $password = $("#password").val();
		var $newPassword = $("#newPassword").val();
		var $newPassword2 = $("#newPassword2").val();
		var pattern = /^[0-9a-zA-Z~!@#$%^&*()_+]{8,10}$/;

		if(pattern.test($password)==true && pattern.test($newPassword)==true && pattern.test($newPassword2)) {
			if($newPassword===$newPassword2) {
				formData.append("password", $password);
				formData.append("newPassword", $newPassword);
			}
		}

		$.ajax({
			url : "/zboard3/users/alter",
			data : formData,
			method: "post",
			processData: false,
			contentType: false
		}).done(()=>alert("성공")).fail((msg)=>console.log(msg.responseText));		
	})

	$("#resign").on("click", function() {
		if(confirm("정말로 탈퇴하시겠습니까?")==false)
			return;
		var $form = $("<form>").attr("action","/zboard3/user/resign").attr("method","post").appendTo($("body"));
		$("<input>").attr("type","hidden").attr("name","_csrf").val("${_csrf.token}").appendTo($form);
		$form.submit();
	});
});
</script>
</head>
<body>
	<div>
		<img id="show_profile" height="200px;" src="${user.profile }">
	</div>
	<div>
		<input type="file" name="profile" id="profile">
	</div>
	<table class="table table-hover" id="user">
		<tr>
			<td class="first">이름</td>
			<td><input type="text" id="irum" value="${user.irum}">&nbsp;	<button type="button" class="btn btn-info" id="changeIrum">이름변경</button></td>
			<td><img width="120px" id="profile">
			</td>
		</tr>
		<tr>
			<td class="first">생년월일</td><td colspan="2"><span id="birthday">${user.birthdayString }</span></td>
		</tr>
		<tr>
			<td class="first">가입일</td><td colspan="2"><span id="joinday">${user.joindayString }</span></td>
		</tr>
		<tr><td class="first">비밀번호</td>
			<td colspan="2">
				<button type="button" class="btn btn-info" id="activateChangePwd">비밀번호 수정</button>
				<div id="passwordArea">
					<span class="key">현재 비밀번호 : </span><input type="password" id="password" ><br>
					<span class="key">새 비밀번호 : </span><input type="password" id="newPassword"><br>
					<span class="key">새 비밀번호 확인 : </span><input type="password" id="newPassword2">
	  				<button type="button" class="btn btn-info" id="changePwd">변경</button>
				</div>
			</td></tr>
		<tr><td class="first">이메일</td>
			<td colspan="2">
				<input type="text" name="email1" id="email1">&nbsp;@&nbsp;<input type="text" name="email2" id="email2">&nbsp;&nbsp;
				<select id="selectEmail">
					<option selected="selected">직접 입력</option>
					<option>naver.com</option>
					<option>daum.net</option>
					<option>gmail.com</option>
				</select>
			</td></tr>
		<tr><td class="first">회원정보</td>
			<td colspan="2">
				가입기간 : <span id="days">${user.day }일</span><br>
				레벨 : <span id="level">${user.level }</span>
			</td></tr>
	</table>
	<button type="button" class="btn btn-success" id="update">변경하기</button>
	<button type="button" class="btn btn-alert" id="resign">탈퇴하기</button>
</body>
</html>