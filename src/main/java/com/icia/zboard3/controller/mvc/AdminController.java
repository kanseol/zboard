package com.icia.zboard3.controller.mvc;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

//@Secured("ROLE_ADMIN")
@Controller
public class AdminController {
	@GetMapping("/system/stat")
	public ModelAndView stat() {
		return new ModelAndView("main").addObject("viewname", "system/stat.jsp");
	}
}
