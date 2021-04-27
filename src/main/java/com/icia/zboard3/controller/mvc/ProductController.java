package com.icia.zboard3.controller.mvc;

import java.util.*;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.icia.zboard3.entity.*;
import com.icia.zboard3.service.mvc.*;

import lombok.*;

@RequiredArgsConstructor
@Controller
public class ProductController {
	private final ProductService service;
	private final ObjectMapper objectMapper;
	
	// 제품 추가 화면 : 제품 추가 처리는 없음. 카테고리 읽어와서 보여주기 목적
	@GetMapping("/product/add")
	public ModelAndView addProduct() throws JsonProcessingException {
		List<Category> list = service.listCategory();
		String json = objectMapper.writeValueAsString(list);
		return new ModelAndView("main").addObject("viewname", "product/add_product.jsp").addObject("catetoryList", json);
	}
	
	// 제품 목록 출력하기
	@GetMapping("/product/list")
	public ModelAndView listProduct(Integer pageno) {
		return new ModelAndView("main").addObject("viewname", "product/list.jsp").addObject("list", service.list());
	}
}



