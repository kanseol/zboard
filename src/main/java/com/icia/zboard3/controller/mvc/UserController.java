package com.icia.zboard3.controller.mvc;

import java.security.*;

import javax.servlet.http.*;

import org.springframework.security.core.*;
import org.springframework.security.web.authentication.logout.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.mvc.support.*;

import com.icia.zboard3.service.mvc.*;

import lombok.*;

@RequiredArgsConstructor
@Controller
public class UserController {
	private final UserService service;
	
	// View : 회원 가입 페이지
	@GetMapping("/user/join")
	public ModelAndView join() {
		return new ModelAndView("main").addObject("viewname", "user/join.jsp");
	}
	
	// View : 아이디 또는 비밀번호 찾기 페이지
	@GetMapping("/user/find")
	public ModelAndView find() {
		return new ModelAndView("main").addObject("viewname", "user/find.jsp");
	}
	
	// View : 로그인 페이지 
	@GetMapping("/user/login")
	public ModelAndView login() {
		return new ModelAndView("main").addObject("viewname", "user/login.jsp");
	}
	
	// View : 비밀번호 확인 페이지
	@GetMapping("/user/password_check")
	public ModelAndView passwordCheck() {
		return new ModelAndView("main").addObject("viewname","user/password_check.jsp");
	}
	
	// U-08. 체크코드 확인
	@GetMapping("/user/join_check")	
	public ModelAndView joinCheck(@NonNull String checkCode) {
		service.joinCheck(checkCode);
		return new ModelAndView("redirect:/user/login");
	}
	
	// U-09. 비밀번호 리셋
	@PostMapping("/user/reset_pwd")
	public ModelAndView resetPwd(@RequestParam @NonNull String username, @NonNull String email, RedirectAttributes ra) {
		service.resetPwd(username, email);
		ra.addFlashAttribute("msg", "임시비밀번호를 이메일로 보냈습니다");
		return new ModelAndView("redirect:/user/login");
	}
	
	// U-10. 비밀번호 확인 - 비밀번호를 확인에 성공하면 세션에 그 정보를 저장 후 내정보보기로 이동
	@PostMapping("/user/password_check")
	public ModelAndView passwordCheck(String password, Principal principal, HttpSession session) {
		service.passwordCheck(password, principal.getName());
		session.setAttribute("passwordCheck", true);
		return new ModelAndView("redirect:/user/info");
	}
	
	// U-11. 내정보 보기
	@GetMapping("/user/info")	
	public ModelAndView info(Principal principal, HttpSession session, RedirectAttributes ra) {
		if(session.getAttribute("passwordCheck")==null) {
			ra.addFlashAttribute("msg", "비밀번호 확인이 필요합니다");
			return new ModelAndView("redirect:/user/password_check");
		}
		return new ModelAndView("main").addObject("viewname","user/info.jsp").addObject("user", service.info(principal.getName()));
	}
	
	// U-12. 회원 탈퇴
	@PostMapping("/user/resign")
	public ModelAndView resign(Authentication auth, SecurityContextLogoutHandler handler, HttpServletRequest req, 
			HttpServletResponse res) {
		service.resign(auth.getName());
		handler.logout(req, res, auth);
		return new ModelAndView("redirect:/");
	}
}






