package com.icia.zboard3.dao;

import java.util.*;

import org.apache.ibatis.annotations.*;

import com.icia.zboard3.entity.*;

public interface AttachmentDao {
	// AttachmentDao-01. findById
	@Select("select * from attachment where ano=#{ano} and rownum=1")
	public Optional<Attachment> findById(Integer ano);
	
	// AttachmentDao-02. delete
	@Delete("delete from attachment where ano=#{ano} and rownum=1")
	public void delete(Integer ano);
	
	// AttachmentDao-03. findAllByBno
	@Select("select * from attachment where bno=#{bno}")
	public List<Attachment> findAllByBno(Integer bno);
	
	
	// AttachmentDao-04. insert
	@Insert("insert into attachment values(attachment_seq.nextval, #{bno}, #{writer}, #{originalFileName},#{saveFileName}, #{length}, #{isImage})")
	public Integer insert(Attachment attachment);


	// AttachmentDao-05. deleteByBno
	@Delete("delete from attachment where bno=#{bno}")
	public Integer deleteByBno(Integer bno);

	


	
}





