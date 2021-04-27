package com.icia.zboard3.util;

import java.io.*;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

// 횡단 관심을 처리하는 방법
// 1. Filter : 서블릿 기술. DispatcherServlet 앞에서 실행
//			  @WebFilter 또는 web.xml에 설정
//			  FilterChain을 구성
// 2. Interceptor : 스프링 기술. Controller 앞(preHandle) 또는 뒤(postHandle) 또는 앞/뒤에서 실행
// 3. AOP : 경로 + 조건(패키지, 파라미터, 예외)

// 모든 요청에 대해 동작
@WebFilter("/*")
public class LoginMessageFilter implements Filter {
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// 서블릿이 Http 전용기술이 아니기때문에 HttpServletRequest가 아닌 ServletRequest를 사용 -> 실제로는 http전용으로 쓰임
		HttpServletRequest req = (HttpServletRequest)request;
		HttpSession session = req.getSession();
		
		// 세션에 msg 메시지가 있을 경우 request로 옮기고 삭제
		if(session.getAttribute("msg")!=null) {
			request.setAttribute("msg", session.getAttribute("msg"));
			session.removeAttribute("msg");
		}
		
		// 자기 다음 필터를 동작시켜야 한다
		chain.doFilter(request, response);
	}

}
