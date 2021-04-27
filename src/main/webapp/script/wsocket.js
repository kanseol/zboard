// window.onload = function() {}는 하나만 가능
// jQuery의 $(document).ready(function(){}); 여러개 만들어도 상관없다(다 실행된다)

$(function() {
	var wsocket;
	function webSocketConnect() {
		// 로그인했고 웹 소켓 연결이 되지 않았다면...연결
		if(wsocket==undefined) {
			wsocket = new WebSocket("ws://localhost:8081/zboard3/web/socket");
			console.log(wsocket);
			wsocket.onmessage = function(evt) {
				console.log(evt)
				var data = evt.data.split(":");
				toastr.options={"progressBar": true};
				toastr.options.onclick = function() { location.href ="/zboard3/memo/receive" }
				toastr.success(data[1], data[0]);
			}
		}
	}
	webSocketConnect();	
});

