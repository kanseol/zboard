package com.icia.zboard3.service.mvc;

import java.util.*;

import javax.annotation.*;

import org.modelmapper.*;
import org.springframework.stereotype.*;

import com.icia.zboard3.dao.*;
import com.icia.zboard3.entity.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class ProductService {
	private final CategoryDao categoryDao;
	private final ProductDao productDao;
	private final ModelMapper modelMapper;
	private List<Category> categoryList = new ArrayList<>();
	
	@PostConstruct
	public void init() {
		categoryDao.findAll().forEach(m->categoryList.add(modelMapper.map(m, Category.class)));
	}
	
	public List<Category> listCategory() {
		return categoryList;
	}

	public List<Product> list() {
		return productDao.findAll();
	}
}
