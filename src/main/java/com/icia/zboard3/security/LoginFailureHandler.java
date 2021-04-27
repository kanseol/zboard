package com.icia.zboard3.security;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.servlet.mvc.support.*;

import com.icia.zboard3.dao.*;
import com.icia.zboard3.entity.*;

import lombok.*;


@RequiredArgsConstructor
@Component("loginFailureHandler")
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	private final UserDao dao;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		HttpSession session = request.getSession();
		String url = "/user/login";
		if(exception instanceof InternalAuthenticationServiceException) 
			session.setAttribute("msg", "아이디를 확인하세요");
		else if(exception instanceof BadCredentialsException) {
			String username = request.getParameter("username");
			int loginFailureCnt = dao.findById(username).get().getLoginFailureCnt()+1;
			if(loginFailureCnt<5) {
				dao.update(User.builder().username(username).loginFailureCnt(loginFailureCnt).build());
				session.setAttribute("msg", "로그인에 " + loginFailureCnt + "회 실패했습니다");
			} else {
				dao.update(User.builder().username(username).enabled(false).build());
				session.setAttribute("msg", "로그인에 5회이상 실패해 계정이 블록되었습니다");
			}			
		} else if(exception instanceof DisabledException) {
			session.setAttribute("msg", "블록된 계정입니다. 관리자에게 연락하세요");
			url = "/";
		}

		
		new DefaultRedirectStrategy().sendRedirect(request, response, url);
	}
}





