package com.icia.zboard3;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.transaction.annotation.*;

import com.icia.zboard3.dao.*;
import com.icia.zboard3.entity.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/spring/**/*-context.xml")
public class CommentDaoTest {
	@Autowired
	private CommentDao dao;
	
	@Test
	public void insertTest() {
		int bno=0;
		String username=null;
		for(int i=0; i<35; i++)
			dao.insert(new Comment(0, bno, username, "내용없어요", null, "http://localhost:8081/profile/anonymous.jpg"));
	}
}
