package com.icia.zboard3.dao;

import java.util.*;

import org.apache.ibatis.annotations.*;

import com.icia.zboard3.entity.*;

public interface CategoryDao {
	@Select("select nvl2(parentCno, 2, 1) clevel , cno, name, parentCno from category")
	public List<Map<String,Object>> findAll();
	
	@Insert("insert into category values(#{cno}, #{name}, #{parentCnt}")
	public Integer insert(Category category);
	
	@Delete("delete from category where cno=#{cno} and rownum=1")
	public Integer delete(Integer cno);
}
