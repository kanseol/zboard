<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>     
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<style>
	#cart table {
		width: 800px;
		border-collapse: collapse;
		border: 1px solid lightgray;
	}
	#cart .first { width: 50px; }
	#cart .second { width: 150px;  }
	#cart .third { widht: 300px; font-size: 0.8em; }
	#cart .fourth {width: 150px;}
	#cart .fifth { width: 150px; }
	#cart .button_area a, .button_area span { 
		font-size: 0.8em; text-align: center;
		height: 30px; line-height: 30px;
	}
	#cart .price { padding-left: 15px; }
</style>
<script>
$(function() {
	// 장바구니 상품 개수 변경
	$(".change").on("click", function() {
		console.log( $(this).data("pno"));
		var params = {
			_method: "patch",
			_csrf: "${_csrf.token}",
			pno: $(this).data("pno")
		}
		if($(this).text()=="+")
			params.isIncrease = 1;
		else
			params.isIncrease = 0;
		$.ajax({
			url: "/zboard3/carts",
			data: params,
			method: "post"
		}).done((map)=>{
			// +,-의 부모인 .fourth td 요소를 선택
			var $parent = $(this).parent();
			// .fourth의 자식 중 .price를 선택해 상품 개수 출력
			$parent.children(".count").text(map.cartItem.count);
			// .fourth의 자식 중 .price를 선택해 상품구입 가격(상품가격*상품개수) 출력
			$parent.children(".price").text(map.cartItem.itemPrice + "원");
			// .fourth의 앞의 앞의 앞 요소인 .first의 자식 중 .checks를 선택해 상품구입 가격 저장
			$parent.prev().prev().prev().children(".checks").data("count", map.cartItem.count);
			// .fourth의 뒤 요소인 .fifth의 자식 중 .buy를 선택해 상품 개수 저장(구입버튼 누르면 상품번호와 상품개수를 /order로 전달하기 위해)
			$parent.next().children(".buy").attr("data-count", map.cartItem.count);
			$("#totalPrice").text(map.totalPrice + "원");
		}); 
	});

	$(".buy").on("click", function() {
		var $form = $("<form>").attr("action","/zboard3/order").attr("method","post");
		$("<input>").attr("type","hidden").attr("name","pnos").val($(this).data("pno")).appendTo($form);
		$("<input>").attr("type","hidden").attr("name","counts").val($(this).data("count")).appendTo($form);
		$("<input>").attr("type","hidden").attr("name","_csrf").val($("_csrf.token")).appendTo($form);
		$form.appendTo($("body")).submit();
	});

	$(".delete").on("click", function() {
		console.log($(this).data("pno"));
		var params = {
			_csrf : "${_csrf.token}",
			_method : "delete",
			pnos: $(this).data("pno")
		};
		$.ajax({
			url:"/zboard3/carts",
			method:"post",
			data: params,
			success:function(result) {
				location.reload();
			}
		})
	});

	$("#check_all").on("change", function() {
		if($(this).prop("checked")==true)
			$(".checks").prop("checked", true);
		else
			$(".checks").prop("checked", false);
	});

	// 선택 삭제 버튼을 클릭했을 때
	$("#buy_all").on("click", function() {
		var $form = $("<form>").attr("action","/zboard3/order").attr("method","post");

		// 선택 삭제 버튼은 체크한 상품들을 삭제한다 -> 체크한 상품 선택
		var $checks = $(".checks:checked");
		// 체크한 상품이 없을 경우 작업 중지
		if($checks.length==0)
			return false;
		
		$.each($checks, function(idx, check) {
			var $check = $(check);
			$("<input>").attr("type","hidden").attr("name","pnos").val($check.data("pno")).appendTo($form);
			$("<input>").attr("type","hidden").attr("name","counts").val($check.data("count")).appendTo($form);
		});
		$("<input>").attr("type","hidden").attr("name","_csrf").val($("_csrf.token")).appendTo($form);
		$form.appendTo($("body")).submit();
	});

	$("#delete_all").on("click", function() {
		var ar = "";
		$.each($(".checks"), function(idx, check) {
			var $check = $(check);
			if($check.prop("checked")==true)
				ar = ar + $check.data("pno") + ",";
		})
		// 체크된 체크박스가 없을 경우 작업 중단
		if(ar.length==0)
			return;	
		var params ={
			_method: "delete",
			pnos: ar.substring(0, ar.lastIndexOf(",")),
			_csrf:"${_csrf.token}"
		}
		$.ajax({
			url:"/zboard3/carts",
			method:"post",
			data: params,
			success:function(result) {
				location.reload();
			}
		})
		
		
	});
	
});
</script>
</head>
<body>
<div id="cart">
	<c:if test="${fn:length(cart.cartItemList) == 0}">
		<img src="/product/empty_cart.png">
	</c:if>
	<c:if test="${fn:length(cart.cartItemList) != 0}">
			<div id="cart_area">
			<table>
				<c:forEach items="${cart.cartItemList}" var="item">
					<tr>
						<td class="first">
							<input type="checkbox" class="checks" data-pno="${item.pno}" data-count="${item.count}">
						</td>
						<td class="second">
							<img src="${item.image}" style="width:135px;">
						</td>
						<td class="third">
							${item.name}
						</td>
						<td class="fourth">
							<div class="price">${item.itemPrice}원</div>
							<button class="btn btn-primary change" data-pno="${item.pno}">+</button>
							<span class="count">${item.count}</span>
							<button class="btn btn-primary change" data-pno="${item.pno}">-</button>
						</td>
						<td class="fifth">
							<button class="buy" data-pno="${item.pno}" data-count="${item.count}">구입</button>
							<button class="delete" data-pno="${item.pno}">삭제</button>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
		<div id="button_area" style="overflow:hidden;">
			<div style="float:left;">
				<input type="checkbox" id="check_all">전체 선택 
				<button id="delete_all">선택삭제</button>
			</div>
			<div style="float:right;">
				<span id="totalPrice">${cart.totalPrice } 원</span>
				<button type="button" id="buy_all">주문하기</button>
			</div>
		</div>
	</c:if>
</div>
</body>
</html>