package com.icia.zboard3.util;

import javax.servlet.http.*;

import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.stereotype.*;
import org.springframework.web.servlet.handler.*;

// Adapter : 복잡한 인터페이스를 기본 구현해 놓은 클래스
@Component
public class AnonymousInterceptor extends HandlerInterceptorAdapter {
	
	// 로그인 여부를 확인해서 로그인 되어있다면 예외 처리
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication.getPrincipal().toString().equals("anonymousUser")==false) {
			throw new IllegalAccessException();
		}
		return true;
	}
}
