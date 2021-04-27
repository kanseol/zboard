package com.icia.zboard3.dao;

import java.util.*;

import com.icia.zboard3.entity.*;

public interface UserDao {
	// UDAO-01
	public Boolean existsById(String username);
	
	// UDAO-02
	public Boolean existsByEmail(String email);
	
	// UDAO-03
	public Integer insert(User user);
	
	// UDAO-04
	public Optional<User> findByEmail(String email);
	
	// UDAO-05
	public Integer update(User user);
	
	// UDAO-06
	public Optional<User> findById(String username);
	
	// UDAO-07
	public Integer deleteByCheckCodeIsNotNull();
	
	// UDAO-08
	public Optional<User> findByCheckCode(String checkCode);

	// UDAO-09
	public Integer joinCheck(String username);
	
	// UDAO-10
	public Integer delete(String username);
}







































