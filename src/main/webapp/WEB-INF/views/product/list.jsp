<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script>
$(function() {
	// 장바구니에 추가
	$(".cart").on("click", function() {
		var params = {
			pno: $(this).attr("data-pno"),
			_csrf: "${_csrf.token}"
		}
		$.ajax({ 
			url: "/zboard3/carts",
			method: "post",
			data: params,
		}).done(()=>{
			var choice = confirm("상품을 장바구니에 담았습니다. 장바구니로 이동하시겠습니까?");
			if(choice==true)
				location.href = "/zboard3/cart/read"
		}).fail((xhr)=>{
			alert(xhr.responseText);
		})
	});
});
</script>
</head>
<body>
	<a href="/zboard3/cart/read">장바구니로</a><br>
	<c:forEach items="${list}" var="product" varStatus="status">
		<div style="width: 176px; margin-right: 25px; display: inline-block;">
			<img src="${product.image}" width="175px">
			<div>
				<span style="width: 175px;">${product.price }원</span>
				<span style="font-size: 0.75em;">${product.name }</span>
			</div>
			<div>
				<button class="cart" data-pno="${product.pno}">장바구니 담기</button>
			</div>
		</div>
		<c:if test="${status.count ==5}">
			<hr>
		</c:if>
	</c:forEach>
</body>
</html>