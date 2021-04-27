package com.icia.zboard3;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;

import com.icia.zboard3.dto.*;
import com.icia.zboard3.service.mvc.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/spring/**/*-context.xml")
public class BoardServiceTest {
	@Autowired
	private BoardService service;
	
	@Test
	public void test1() {
		Page<BoardDto.ListView> page1 =  service.list(1, null);
		System.out.println(page1);
		
		Page<BoardDto.ListView> page2 =  service.list(1, "SPRING11");
		System.out.println(page2);
		
		Page<BoardDto.ListView> page3 =  service.list(1, "SPRING22");
		System.out.println(page3);
		
		Page<BoardDto.ListView> page4 =  service.list(111, "SPRING11");
		System.out.println(page4);
	}
}
