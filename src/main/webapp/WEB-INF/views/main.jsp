<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@9"></script>
<link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<link rel="stylesheet" href="/zboard3/css/main.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<script>
$(function() {
	var msg = "${msg}";
	console.log(msg)
	if(msg!="") {
		$("#alert").text(msg);
		$("#msg").show();
	}
});
</script>
</head>
<body>
<div id="page">
	<header>
		<jsp:include page="include/header.jsp" />
	</header>
	<nav>
		<jsp:include page="include/nav.jsp" />
	</nav>
	<div id="main">
		<aside>
			<jsp:include page="include/aside.jsp" />
		</aside>
		<section>
			<div class="alert alert-danger alert-dismissible" id="msg" style="display:none;">
    			<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
    			<strong>서버 메시지 </strong><span id="alert"></span>
  			</div>
			<jsp:include page="${viewname}" />
		</section>
	</div>
	<footer>
		<jsp:include page="include/footer.jsp" />
	</footer>
</div>
</body>
</html>