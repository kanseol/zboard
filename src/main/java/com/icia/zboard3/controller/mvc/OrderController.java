package com.icia.zboard3.controller.mvc;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import com.icia.zboard3.dto.*;

@Controller
public class OrderController {
	@PostMapping("/order")
	public ModelAndView order(OrderDto.ReceiverOrder dto) {
		System.out.println(dto);
		return null;
	}
}
