<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<sec:authorize access="hasRole('ROLE_USER')">
	<script src="/zboard3/script/wsocket.js"></script>
</sec:authorize>
<script>
$(document).ready(function() {
	// 1. 아이디를 클릭하면 나타나는 가입일보기, 게시물보기, 메모보내기로 구성된 컨텍스트 메뉴에 대한 처리
	// 1-1. 게시물보기, 메모보내기 기능을 실행하려면 사용자 아이디를 필요로 하므로 클릭한 아이디를 컨텍스트 메뉴로 복사
	$(".writer").on("click", function() {
		// 글쓴사람 아이디를 클릭하면 모달 메뉴를 출력
		// 아이디는 td에 data-writer로 저장. 모달 메뉴를 띄우는 순간에 메뉴쪽에 복사	
		$(".modal-body li").attr("data-writer", $(this).attr("data-writer"));
	});
	
	// 1-2. 게시물 보기를 선택했을 때
	$("#readById").on("click", function() {
		location.href = "/zboard3/board/list?pageno=1&writer=" + $(this).data("writer");
	});
	
	// 1-3. 모달 대화상자의 가입일 조회 클릭
	$("#findJoinDate").on("click", function() {
		$.ajax({url: "/zboard3/users/joinday?username=" + $(this).attr("data-writer")})
		.done((result)=>Swal.fire($(this).attr("data-writer") + "님의 가입일", result, "success"))
		.fail(()=>Swal.fire("오류 발생!", $(this).attr("data-writer") + "님의 가입일", "warning"));
	});
	
	// 1-5. 모달 대화상자의 메모 보내기
	$("body").on("click", "#writeMemo", function() {
		// 메모 작성 페이지로 이동
		location.href = "/zboard3/memo/write?receiver=" + $(this).attr("data-writer")
	});
});
</script>
</head>
<body>
<div>
	<table class="table table-hover">
		<tr><th>번호</th><th style="width: 15px;"></th><th>제목</th><th>글쓴이</th><th>시간</th><th>읽기</th></tr>
		<tbody id="list">
			<c:forEach items="${page.list }" var="b">
				<tr>
					<td>${b.bno }</td>
					<td>
						<c:if test="${b.attachmentCnt>0 }"><i class="fa fa-paperclip"></i></c:if>
					</td>
					<td>
						<a href="/zboard3/board/read?bno=${b.bno }">${b.title }</a>
						&nbsp;&nbsp;[${b.commentCnt}]
					</td>
					<td class='writer' data-toggle="modal" data-target="#myModal" data-writer="${b.writer }">${b.writer }</td>
					<td>${b.writeTimeString }</td>
					<td>${b.readCnt }</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div id="pagination" style="text-align: center;">
		<ul class="pagination">
			<c:if test="${page.prev>0 }">
				<li><a href="/zboard3/board/list?pageno=${page.prev }">이전으로</a></li>
			</c:if>
			
			<c:forEach begin="${page.start }" end="${page.end }" var="i">
				<c:if test="${page.pageno==i}">
					<li class="active"><a href="/zboard3/board/list?pageno=${i }">${i}</a></li>
				</c:if>
				<c:if test="${page.pageno != i }">
					<li><a href="/zboard3/board/list?pageno=${i }">${i}</a></li>
				</c:if>
			</c:forEach>
			<c:if test="${page.next>0 }">
				<li><a href="/zboard3/board/list?pageno=${page.next }">다음으로</a></li>
			</c:if>
		</ul>
	</div>
	<div class="modal fade" id="myModal" role="dialog" style="top:40%;">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body" >
					<ul style="list-style: none;">
						<li id="readById">게시물 보기</li>
						<li id="findJoinDate">가입일 보기</li>
						<sec:authorize access="hasRole('ROLE_USER')">
							<li id="writeMemo">메모 보내기</li>
						</sec:authorize>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>













