package com.icia.zboard3.controller.mvc;


import java.security.*;
import java.util.*;

import org.springframework.security.access.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;
import org.springframework.web.servlet.*;

import com.icia.zboard3.dto.*;
import com.icia.zboard3.service.mvc.*;

import lombok.*;


@RequiredArgsConstructor
@Controller
public class BoardController {
	private final BoardService service;
	// View : 글쓰기 페이지
	@Secured("ROLE_USER")
	@GetMapping("/board/write")
	public ModelAndView write() {
		return new ModelAndView("main").addObject("viewname", "board/write.jsp");
	}
	
	// B-08. 페이징
	@GetMapping({"/", "/board/list"})
	public ModelAndView list(@RequestParam(defaultValue="1") Integer pageno, String writer) {
		return new ModelAndView("main").addObject("viewname", "board/list.jsp")
				.addObject("page", service.list(pageno, writer));
	}
	
	// B-09. 글쓰기
	@Secured("ROLE_USER")
	@PostMapping("/board/write")
	public ModelAndView write(BoardDto.Write dto, BindingResult bindingResult, @RequestParam List<MultipartFile> attachments, 
			Principal principal) throws BindException  {
		if(bindingResult.hasErrors())
			throw new BindException(bindingResult);
		Integer bno = service.write(dto, attachments, principal.getName());
		return new ModelAndView("redirect:/board/read?bno=" + bno);
	}

	// B-10. 글읽기
	@GetMapping("/board/read")
	public ModelAndView read(@NonNull Integer bno, Principal principal) {
		String username = principal==null? null : principal.getName(); 
		BoardDto.Read dto = service.read(bno, username);
		return new ModelAndView("main").addObject("viewname", "board/read.jsp").addObject("board", dto);
	}
	
	// B-11. 글삭제
	@Secured("ROLE_USER")
	@PostMapping("/board/delete")
	public ModelAndView delete(@NonNull Integer bno, Principal principal) {
		// 글 삭제 
		// - 글을 삭제할 것인가? 첨부파일, 댓글, 글 모두 삭제 
		// - 비활성화할 것인가? 글 읽기를 하면 "삭제된 글입니다"라고 내용을 출력. 댓글은 정상적으로 출력
		
		service.delete(bno, principal.getName());
		return new ModelAndView("redirect:/");
	}
	
	// B-12. 글변경
	@Secured("ROLE_USER")
	@PostMapping("/board/update")
	public ModelAndView update(BoardDto.Update dto, Principal principal) {
		service.update(dto, principal.getName());
		return new ModelAndView("redirect:/board/read?bno=" + dto.getBno());
	}
}