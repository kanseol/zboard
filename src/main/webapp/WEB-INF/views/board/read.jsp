<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>	
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글 읽기</title>
<style>
	#content {
		min-height: 400px;
	}
</style>
<link rel="stylesheet" href="/zboard3/css/read.css">
<script src="/zboard3/ckeditor/ckeditor.js"></script>

<!-- 로그인 여부(isLogin), 글쓴이 여부(isWriter)  -->
<sec:authorize access="isAuthenticated()">
	<!-- principal.username을 변수 username에 저장(EL은 읽기 전용. 태그 라이브러리를 이용해 값을 추가할 수 있다 -->
	<sec:authentication property="principal.username" var="username" />
	<c:set var="irum" property="홍길동" />
	<script>
		var isLogin = true;
		var isWriter = ${board.writer==username};
	</script>
</sec:authorize>

<sec:authorize access="isAnonymous()">
	<script>
		var isLogin = false;
		var isWriter = false;
	</script>
</sec:authorize>

<script>
function init() {
	// 비로그인이라면 : 아무것도 못해(default 값)
	// 로그인했고 글쓴이인 경우
	// 		제목 활성화, 내용에 ck 적용
	//		btnArea에 변경/삭제 버튼 추가
	//		comment_textarea 활성화, commentBtnArea에 "댓글 추가" 버튼 추가
	// 로그인했고 글쓴이가 아닌 경우
	//		추천, 비추 badge 활성화
	// 		comment_textarea 활성화, commentBtnArea에 "댓글 추가" 버튼 추가
	if(isLogin==true && isWriter==true) {
		$("#title").prop("disabled", false);
		var ck = CKEDITOR.replace('content', {
			filebrowserUploadUrl : '/zboard3/boards/image?_csrf=${_csrf.token}'
		});
		
		var $btnArea = $("#btnArea");
		$("<button>").attr("id","update").attr("class","btn btn-info").text("변 경").appendTo($btnArea);
		$("<button>").attr("id","delete").attr("class","btn btn-info").text("삭 제").appendTo($btnArea);

		$("#comment_textarea").prop("readonly",false)
			.attr("placeholder", "욕설이나 모욕적인 댓글은 삭제될 수 있습니다");

		var $commentBtnArea = $("#commentBtnArea");
		$("<button>").attr("id","write").attr("class","btn btn-info").text("댓글 추가").appendTo($commentBtnArea);
	} else if(isLogin==true && isWriter==false) {
		$("#good_btn").prop("disabled", false);
		$("#bad_btn").prop("disabled", false);
		
		$("#comment_textarea").prop("readonly",false)
			.attr("placeholder", "욕설이나 모욕적인 댓글은 삭제될 수 있습니다");

		var $commentBtnArea = $("#commentBtnArea");
		$("<button>").attr("id","write").attr("class","btn btn-info").text("댓글 추가").appendTo($commentBtnArea);
	}
}

function printAttachments(attachments) {
	var $attachment = $("#attachment");
	$attachment.empty();
	$.each(attachments, function(idx, attachment) {
		var $li = $("<li>").css("overflow","hidden").css("width","300px").appendTo($attachment);
		var $div1 = $("<div>").css("float","left").appendTo($li);
		if(attachment.isImage==true)
			$("<i class='fa fa-file-image-o'></i>").appendTo($div1);
		else
			$("<i class='fa fa-paperclip'></i>").appendTo($div1);
		$("<span>").text(" ").appendTo($div1);
		$("<a>").attr("href","/zboard3/attachments/" + attachment.ano).text(attachment.originalFileName)
			.appendTo($div1);
		var $div2 = $("<div>").css("float", "right").appendTo($li);
		if(isWriter==true) {
			$("<span>").attr("class",'delete_attachment').data("ano",attachment.ano)
				.data("bno",attachment.bno).attr("title",attachment.originalFileName+" 삭제")
				.css("cursor","pointer").text("X").appendTo($div2);
		} 	
	});
}

function printComments(dto) {
	// #comments : 댓글 출력 영역, #next : 다음으로 버튼 출력 영역  
	var $comments = $("#comments");

	// 기존 다음으로 버튼 삭제. remove()는 자기자신을 삭제(자식이 있으면 자식까지 모두), empty()는 자신의 자식들만 삭제
	$("#nextCommentPage").remove();

	$.each(dto.comments, function(idx, comment) {
		var $outer_div = $("<div>").appendTo($comments);
		var $upper_div = $("<div>").appendTo($outer_div);
		$("<span>").css("color","blue").text(comment.writer).appendTo($upper_div);
		$("<span>").text(" [" + comment.writeTime+"]").appendTo($upper_div);
		var $lower_div = $("<div>").css("overflow", "hidden").appendTo($outer_div);
		$("<img>").attr("src",comment.profile).css("width","60px").appendTo($lower_div);
		$("<span>").text(comment.content).appendTo($lower_div);

		// 로그인 했고 댓글 작성자와 로그인한 사용자의 아이디가 같다면
		// ${username}는 스프링 시큐리티 태그 라이브러리로 21라인에서 EL에 추가한 값
		if(isLogin==true && comment.writer=='${username}') {
			$("<button>").attr("class","delete_comment").data("cno", comment.cno).data("bno", comment.bno)
				.text("삭제").css("float","right").appendTo($lower_div);
		}
		$("<hr>").appendTo($comments);
	});

	// 다음 댓글이 있을 경우 #next에 버튼 추가
	if(dto.nextCommentPage>0)
		$("<button>").text("다음 댓글").data("pageno", dto.nextCommentPage).attr("id","nextCommentPage").appendTo($("#next"));
}

$(function() {
	init();

	// 댓글 작성하기
	$("#commentBtnArea").on("click", "#write", function() {
		var params = { bno : '${board.bno}', content : $("#comment_textarea").val(), _csrf: '${_csrf.token}' };
		$.ajax({ url: "/zboard3/comments", method: "post", data: params }).done((map)=>{
			$("#comment_textarea").val("")
			// 출력된 댓글들을 삭제
			$("#comments").empty();
			// 서버에서 보내온 최신 댓글 10개를 출력
			printComments(map)
		});
	});

	// 댓글 삭제하기
	$("#comments").on("click", ".delete_comment", function() {
		var params = { bno : '${board.bno}', cno : $(this).data("cno"), _csrf: '${_csrf.token}', _method: "delete" };
		$.ajax({ url: "/zboard3/comments", method: "post", 	data: params}).done((map)=>{
			$("#comments").empty();
			printComments(map)
		});
	});

	// 첨부파일 삭제하기
	$("#attachment").on("click", ".delete_attachment", function() {
		var params = {bno: $(this).data("bno"), _csrf: "${_csrf.token}", _method: "delete"};
		$.ajax({url: "/zboard3/attachments/" + $(this).data("ano"), method: "post", data: params })
			.done(attachments=>printAttachments(attachments));
	});

	// 다음 댓글
	$("#next").on("click", "#nextCommentPage", function() {
		var params={ pageno:$(this).data("pageno"), bno : '${board.bno}'}
		$.ajax({url:"/zboard3/comments/next", data:params}).done((dto)=>{
			printComments(dto);
		})
	});

	// 추천
	$("#good_btn").on("click", function() {
		var params = { bno : '${board.bno}', isGood: 1, _csrf: '${_csrf}', _method: 'patch' };
		$.ajax({url : '/zboard3/board/goodOrBad', data: params, method: 'post'})
			.done((cnt)=>$("#goodCnt").text(cnt)).fail(()=>alert("추천에 실패했습니다"));
	});

	// 비추
	$("#bad_btn").on("click", function() {
		var params = { bno : '${board.bno}', isGood: 0, _csrf: '${_csrf}', _method: 'patch' };
		$.ajax({url : '/zboard3/board/goodOrBad', data: params, method: 'post'})
			.done((cnt)=>$("#badCnt").text(cnt)).fail(()=>alert("비추에 실패했습니다"));
	});

	//  변경
	$("#btnArea").on("click", "#update", function() {
		// 글 작성할 때와는 다르게 urlencoded(첨부파일은 포함하고 있지 않다)
		var $form = $("<form>").attr("action","/zboard3/board/update").attr("method","post");
		$("<input>").attr("type","hidden").attr("name", "bno").val('${board.bno}').appendTo($form);
		$("<input>").attr("type","hidden").attr("name", "title").val($("#title").val()).appendTo($form);
		$("<input>").attr("type","hidden").attr("name", "content").val(CKEDITOR.instances['content'].getData()).appendTo($form);
		$("<input>").attr("type","hidden").attr("name", "_csrf").val('${_csrf.token}').appendTo($form);
		$form.appendTo($("body")).submit();
	});

	// 글 삭제  
	$("#btnArea").on("click", "#delete", function() {
		var $form = $("<form>").attr("action","/zboard3/board/delete").attr("method", "post").appendTo($("body"));
		$("<input>").attr("type","hidden").attr("name","_csrf").val("${_csrf.token}").appendTo($form);
		$("<input>").attr("type","hidden").attr("name","bno").val($("#bno").text()).appendTo($form);
		$form.submit();
	});
});

</script>


</head>
<body>
<div id="wrap">
	<div>
		<div id="title_div">
			<!-- 제목, 작성자 출력 영역 -->
			<div id="upper">
				<input type="text" id="title" disabled="disabled" value="${board.title }">
				<span id="writer">${board.writer }</span>
			</div>
			<!-- 글번호, 작성일, 아이피, 추천수, 조회수, 신고수 출력 영역 -->
			<div id="lower">
				<ul id="lower_left">
					<li>글번호<span id="bno">${board.bno }</span></li>
					<li><span id="writeTimeString">${board.writeTimeString }</span></li>
				</ul>
				<ul id="lower_right">
					<li><button type="button" class="btn btn-primary" id="good_btn" disabled="disabled">추천<span class="badge" id="goodCnt">${board.goodCnt }</span></button></li>
					<li><button type="button" class="btn btn-success" id="b" disabled="disabled">조회 <span class="badge" id="readCnt">${board.readCnt }</span></button></li>    
  					<li><button type="button" class="btn btn-danger" id="bad_btn" disabled="disabled">비추<span class="badge" id="badCnt">${board.badCnt }</span></button></li>        
				</ul>
			</div>
			<!-- 첨부파일 출력 영역 -->
			${isWriter }
			<div>
				<ul id="attachment">
					<c:forEach items="${board.attachments}" var="attachment">
						<li style="overflow:hidden; width: 300px;">
							<div style="float:left">
								<c:if test="${attachment.isImage==true }">
									<i class="fa fa-file-image-o"></i>
								</c:if>
								<c:if test="${attachment.isImage==false }">
									<i class="fa fa-paperclip"></i>
								</c:if>
								 <a href='/zboard3/attachments/${attachment.ano }'>${attachment.originalFileName}</a>
							</div>
							<div style="float:right;">										
								<c:if test="${board.writer==username}">
									<span class='delete_attachment' data-ano='${attachment.ano}' data-bno='${attachment.bno}' 
										title='${attachment.originalFileName} 삭제' style='cursor:pointer;'>X</span>
								</c:if>
							</div>
						</li>
					</c:forEach>	
				</ul>
			</div>
		</div> 
		<!--  본문, 갱신 버튼, 삭제 버튼 출력 영역 -->
		<div id="content_div">
			<div class="form-group">
				<div class="form-control" id="content">${board.content }
				</div>
			</div>
			<div id="btnArea">
			</div>
		</div>
	</div>
	<div>
		<div class="form-group">
      		<label for="comment_teaxarea">댓글을 입력하세요</label>
      		<textarea class="form-control" rows="5" id="comment_textarea" readonly="readonly"
      			placeholder="댓글을 작성하려면 로그인 해주세요"></textarea>
    	</div>
    	<div id="commentBtnArea">
    	<!-- 로그인하면 댓글 달기 버튼을 추가 -->
		</div>
		<hr>
		<div id="comments">
			<c:forEach items="${board.comments }" var="comment">
				<div>
					<div><span style="color:blue;">${comment.writer }</span> ${comment.writeTimeString }</div>
					<div style="overflow:hidden;">
						<img src="${comment.profile }" style="width:60px;">
						<span>${comment.content }</span>
						<c:if test="${comment.isWriter==true }">
							<button class="delete_comment" data-cno="${comment.cno}" style="float:right;">삭제</button>
						</c:if>
					</div>
				</div>
				<hr>
			</c:forEach>
		</div>
		<div id="next">
			<c:if test="${board.nextCommentPage>0 }">
				<button id="nextCommentPage" data-pageno="${board.nextCommentPage }">다음 댓글</button>
			</c:if>
		</div>
	</div>
	<hr>
</div>
</body>
</html>