package com.icia.zboard3.dao;

import java.util.*;

import org.apache.ibatis.annotations.*;

import com.icia.zboard3.entity.*;

public interface BoardDao {
	// BDAO-01.	findById
	public Optional<Board> findById(Integer bno);
	
	// BDAO-02. update
	public void update(Board board);
	
	// BDAO-03.	count
	public Integer count(String writer);
	
	// BDAO-04.	findAll
	public List<Map> findAll(Map<String, Integer> map);
	
	
	// BDAO-05.	findAllByWriter
	public List<Map> findAllByWriter(@Param("map") Map<String, Integer> map, @Param("writer") String writer);
	
	// BDAO-06. insert
	public Integer insert(Board board);


	// BDAO-07. delete
	public Integer delete(Integer bno);
}
