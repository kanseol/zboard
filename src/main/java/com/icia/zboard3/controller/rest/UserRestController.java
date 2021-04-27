package com.icia.zboard3.controller.rest;

import java.security.*;

import javax.validation.*;

import org.springframework.http.*;
import org.springframework.security.access.annotation.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import com.icia.zboard3.dto.*;
import com.icia.zboard3.service.rest.*;

import lombok.*;

@RequiredArgsConstructor
@RestController
public class UserRestController {
	private final UserRestService service;
	
	// 아이디 중복 확인 - @NonNull로 체크해 null일 경우 IllegalArgumentException
	@GetMapping("/users/user/username")
	public ResponseEntity<String> idAvailableCheck(@NonNull String username) {
		service.idAvailableCheck(username);
		return ResponseEntity.ok(username);
	}
	
	// 이메일 중북 확인
	@GetMapping("/users/user/email")
	public ResponseEntity<String> emailAvailableCheck(@NonNull String email) {
		service.emailAvailableCheck(email);
		return ResponseEntity.ok(email);
	}
	
	
	// U-03. 회원 가입
	@PostMapping("/users/new")
	public ResponseEntity<Void> join(@Valid UserDto.Join dto, BindingResult bindingResult, @RequestParam(required=false) MultipartFile profile) throws BindException {
		if(bindingResult.hasErrors())
			throw new BindException(bindingResult);
		service.join(dto, profile);
		return ResponseEntity.ok(null);
	}
	
	// U-04. 아이디 찾기
	// annotation은 변형된 클래스. 기본 필드의 value.
	// consumes : 어떤 데이터에 반응할 꺼니...똑같은 url인데 consumes 형식을 xml 또는 json으로 구별할 수도 있다
	@GetMapping(value="/users/username/email", produces=MediaType.TEXT_PLAIN_VALUE)	
	public ResponseEntity<String> findId(@NonNull String email) {
		String username = service.findId(email);
		return ResponseEntity.ok(username);
	}
	
	// U-05. 이름 변경
	@Secured("ROLE_USER")
	@PatchMapping("/users/username")
	public ResponseEntity<Void> changeIrum(String irum, Principal principal) {
		service.changeIrum(irum, principal.getName());
		return ResponseEntity.ok(null);
	}
	
	// U-06. 비밀번호 변경
	@Secured("ROLE_USER")
	@PatchMapping("/users/password")
	public ResponseEntity<Void> changePassword(@Valid UserDto.ChangePassword dto, BindingResult bindingResult, Principal principal) throws BindException {
		if(bindingResult.hasErrors())
			throw new BindException(bindingResult);
		service.changePassword(dto, principal.getName());
		return ResponseEntity.ok(null);
	}
	
	// U-07. 내정보 변경
	@Secured("ROLE_USER")
	@PostMapping("/users/alter")	
	public ResponseEntity<Void> update(UserDto.Update dto, BindingResult bindingResult, 
			@RequestParam(required = false) MultipartFile profile, Principal principal) throws BindException {
		if(bindingResult.hasErrors())
			throw new BindException(bindingResult);
		service.update(dto, profile, principal.getName());
		return ResponseEntity.ok(null);
	}
	
}