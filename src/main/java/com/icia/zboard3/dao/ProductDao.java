package com.icia.zboard3.dao;

import java.util.*;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.icia.zboard3.entity.*;

public interface ProductDao {
	@Insert("insert into product values(proudct_seq.nextval, #{name}, #{cno}, #{price}, #{stock}, #{info}, #{image}, sysdate, 0, 0, 0)")
	public Integer insert(Product product);
	
	@Select("select * from product where pno=#{pno} and rownum=1")
	public Optional<Product> findById(Integer pno);
	
	@Select("select * from product")
	public List<Product> findAll();
}
