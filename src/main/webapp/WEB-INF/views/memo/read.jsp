<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<sec:authorize access="hasRole('ROLE_USER')">
	<script src="/zboard3/script/wsocket.js"></script>
</sec:authorize>
<script>
$(function() {
	$("#btnSend").on("click", function() {
		location.href = "/zboard3/memo/send";
	});
	$("#btnReceive").on("click", function() {
		location.href = "/zboard3/memo/receive";
	});	
})
</script>
</head>
<body>
	<table>
		<tr><td>${memo.writeTimeString}에 ${memo.sender }님이 ${memo.receiver }님에게 보낸 메모</td></tr>
		<tr><td>${memo.title }</td></tr> 
		<tr><td>${memo.content }</td></tr>
	</table>
	<button id="btnSend">보낸 메모 페이지로</button>
	<button id="btnReceive">받은 메모 페이지로</button>
</body>
</html>