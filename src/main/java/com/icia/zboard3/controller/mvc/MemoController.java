package com.icia.zboard3.controller.mvc;

import java.security.*;
import java.util.*;

import javax.validation.*;

import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import com.icia.zboard3.dto.*;
import com.icia.zboard3.service.mvc.*;

import lombok.*;

@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
@Controller
public class MemoController {
	private final MemoService service;

	// View : 메모 작성 페이지
	@GetMapping("/memo/write")
	public ModelAndView write() {
		return new ModelAndView("main").addObject("viewname","memo/write.jsp");
	}
	
	@PostMapping("/memo/write")
	public ModelAndView write(@Valid MemoDto.Write dto, BindingResult bindingResult, Principal principal) throws BindException {
		if(bindingResult.hasErrors())
			throw new BindException(bindingResult);
		service.write(dto, principal.getName());
		return new ModelAndView("redirect:/memo/send");
	}
	
	// 보낸 메모함
	@GetMapping("/memo/send")
	public ModelAndView send(Principal principal) {
		List<MemoDto.ListView> memos = service.send(principal.getName());
		return new ModelAndView("main").addObject("viewname","memo/send.jsp").addObject("memos", memos);
	}
	
	// 받은 메모함
	@GetMapping("/memo/receive")
	public ModelAndView receive(Principal principal) {
		List<MemoDto.ListView> memos = service.receive(principal.getName());
		return new ModelAndView("main").addObject("viewname","memo/receive.jsp").addObject("memos", memos);
	}
	
	// 메모 읽기
	@GetMapping("/memo/read")
	public ModelAndView read(@NonNull Integer mno) {
		MemoDto.Read memo = service.read(mno);
		return new ModelAndView("main").addObject("viewname","memo/read.jsp").addObject("memo", memo);
	}
	
	@PostMapping("/memo/disabled_by_sender")
	public ModelAndView disabledBySender(@RequestParam List<Integer> mnos) {
		service.disabledBySender(mnos);
		return new ModelAndView("redirect:/memo/send");
	}
		
	@PostMapping("/memo/disabled_by_receiver")
	public ModelAndView disabledByReceiver(@RequestParam List<Integer> mnos) {
		service.disabledByReceiver(mnos);
		return new ModelAndView("redirect:/memo/receive");
	}
}