package com.icia.zboard3.advice;

import java.net.*;

import org.springframework.validation.*;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.mvc.support.*;

import com.icia.zboard3.exception.*;

@ControllerAdvice(basePackages= {"com.icia.zboard3.controller.mvc", "com.icia.zboard3.service.mvc"})
public class ZBoard3Advice {
	@ExceptionHandler(UserJob.JoinCheckFailException.class)
	public ModelAndView joinCheckFailExceptionHandler(RedirectAttributes ra) {
		ra.addFlashAttribute("msg", "체크코드를 찾을 수 없습니다. 회원 재가입이 필요합니다");
		return new ModelAndView("redirect:/user/join");
	}
		
	@ExceptionHandler(UserJob.UserNotFoundException.class)
	public ModelAndView userNotFoundException(RedirectAttributes ra) {
		ra.addFlashAttribute("msg", "사용자를 찾을 수 없습니다");
		return new ModelAndView("redirect:/");
	}
	
	@ExceptionHandler(UserJob.PasswordCheckFailException.class)
	public ModelAndView passwordCheckFailExceptionHandler(RedirectAttributes ra) {
		ra.addFlashAttribute("msg", "비밀번호를 확인하지 못했습니다");
		return new ModelAndView("redirect:/user/password_check");
	}
	
	// @Valid -> 첫번째 오류 메시지만 출력
	@ExceptionHandler(BindException.class)
	public ModelAndView bindExceptionHandler(BindingResult bindingResult, RedirectAttributes ra) {
		ra.addFlashAttribute("msg", bindingResult.getAllErrors().get(0).getDefaultMessage());
		return new ModelAndView("redirect:/");
	}
	
	// lombok : @NonNull -> NullPointerException -> IllegalArgumentException 예외 전환
	@ExceptionHandler(IllegalArgumentException.class)
	public ModelAndView illegalArgumentExceptionHandler (RedirectAttributes ra) {
		ra.addFlashAttribute("msg", "필수 파라미터를 입력하지 않았습니다");
		return new ModelAndView("redirect:/");
	}
	
	// 비로그인 경로로 로그인 상태에서 접근했을 때 : 인터셉터로 
	@ExceptionHandler(IllegalAccessException.class)
	public ModelAndView illegalAccessException(RedirectAttributes ra) {
		ra.addFlashAttribute("msg", "잘못된 접근입니다");
		return new ModelAndView("redirect:/");
	}
}





