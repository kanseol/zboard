<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>	
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글 작성</title>
<script src="/zboard3/ckeditor/ckeditor.js"></script>
<script>
$(function() {
	var ck = CKEDITOR.replace('content', {
		// 사진 업로드 경로를 설정
		filebrowserUploadUrl : '/zboard3/boards/image'
	});

	// 아파치 파일 업로드 : attachments[0], attachments[1], attachments[2] -> List
	//					   dto 클래스 필드로 MultipartFile 추가 가능
	$("#addAttachment").on("click", function() {
		$("<input>").attr("type", "file").attr("name","attachments").appendTo($("#attach"));
	})
})
</script>
</head>
<body>
<form id="writeForm" action="/zboard3/board/write" method="post" enctype="multipart/form-data">
	<div class="form-group">
		<label for="title">제목:</label>
		<input type="text" class="form-control" id="title" placeholder="제목" name="title">
    </div>
    <div class="form-group">
		<textarea class="form-control" rows="5" id="content" name="content"></textarea>
	</div>
	<div class="form-group" id="attach">
		<button type="button" id="addAttachment">첨부 파일 추가 </button>
	</div>
	<input type="hidden" name="_csrf" value="${_csrf.token}">
	<button  class="btn btn-success" id="write">작성</button>
</form>
</body>
</html>