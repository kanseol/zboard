package com.icia.zboard3.security;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.springframework.security.core.*;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.savedrequest.*;
import org.springframework.stereotype.*;

import com.icia.zboard3.dao.*;
import com.icia.zboard3.entity.*;

import lombok.*;

@RequiredArgsConstructor
@Component("loginSuccessHandler")
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private final UserDao userDao;
	private final MemoDao memoDao;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		String username = authentication.getName();
		// 로그인 실패 횟수를 0으로 리셋
		userDao.update(User.builder().username(username).loginFailureCnt(0).build());
		
		// 로그인한 사용자에게 출력할 메시지가 있다면 이곳에 작성
		if(memoDao.existsByIsReadIsFalse(username)) {
			HttpSession session = request.getSession();
			session.setAttribute("msg", "읽지 않은 메모가 있습니다");
		}
		
		// RequestCache : 사용자가 가려던 목적지를 저장하는 인터페이스
		SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
		
		// 이동할 주소가 있으면 그곳으로, 없으면 루트로
		RedirectStrategy rs = new DefaultRedirectStrategy();
		if(savedRequest!=null)
			rs.sendRedirect(request, response, savedRequest.getRedirectUrl());
		else
			rs.sendRedirect(request, response, "/");
	}
}


















