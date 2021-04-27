package com.icia.zboard3;

import java.util.*;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;

import com.icia.zboard3.dao.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/spring/**/*-context.xml")
public class BoardDaoTest {
	@Autowired
	private BoardDao boardDao;
	
	@Test
	public void findAllTest() {
		Map<String,Integer> map = new HashMap<>();
		map.put("startRowNum", 1);
		map.put("endRowNum", 10);
		System.out.println(boardDao.findAll(map).size());
		boardDao.findAll(map).forEach(b->System.out.println(b));
		
	}
}
