package com.icia.zboard3.advice;

import java.util.*;

import org.springframework.http.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.mvc.support.*;

import com.icia.zboard3.exception.*;

@ControllerAdvice(basePackages = {"com.icia.zboard3.controller.rest", "com.icia.zboard3.service.rest"})
public class ZBoard3RestAdvice {
	private HttpHeaders getHeaders() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", "text/plain;charset=utf-8");
		return httpHeaders;
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> illegalArgumentExceptionHandler() {
		return new ResponseEntity<>("필수 데이터를 입력하지 않았습니다", getHeaders(), HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(BindException.class)
	public ResponseEntity<Map<String, Object>> bindExceptionHandler(BindingResult bindingResult) {
		// bindingResult 객체에 오류메시지가 들어있다 -> REST이므로 모든 오류 메시지를 출력 -> 화면 작성자가 알아서 처리
		Map<String, Object> messages = new HashMap<String, Object>();
		List<ObjectError> list = bindingResult.getAllErrors();
		
		// 에러가 발생한 필드명이 알아내려면 ObjectError 객체를 FieldError 객체로 형변환해야 한다
		// username : 아이디는 대문자와 숫자 8~10자입니다
		// birthday : 생일은 필수 입력입니다
		for(ObjectError error:list) {
			FieldError fe = (FieldError)error;
			messages.put(fe.getField(), fe.getDefaultMessage());
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body(messages);
	}
	
	@ExceptionHandler(UserJob.IdExistException.class)
	public ResponseEntity<Void> idExistExceptionHandler() {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
	}
	
	@ExceptionHandler(UserJob.EmailExistException.class)
	public ResponseEntity<Void> emailExistExceptionHandler() {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
	}
	
	@ExceptionHandler(UserJob.UserNotFoundException.class)
	public ResponseEntity<String> userNotFoundExceptionHandler() {
		return new ResponseEntity<>("사용자를 찾을 수 없습니다", getHeaders(), HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(UserJob.FindPasswordException.class)
	public ResponseEntity<String> findPasswordExceptionHandler() {
		return new ResponseEntity<>("비밀번호를 찾을 수 없습니다", getHeaders(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(IllegalAccessException.class)
	public ModelAndView illegalAccessException(RedirectAttributes ra) {
		ra.addFlashAttribute("msg", "잘못된 접근입니다");
		return new ModelAndView("redirect:/");
	}

}


