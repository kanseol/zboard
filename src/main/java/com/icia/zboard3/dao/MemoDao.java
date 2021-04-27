package com.icia.zboard3.dao;

import java.util.*;

import com.icia.zboard3.entity.*;

public interface MemoDao {
	public int insert(Memo memo);
	
	public List<Memo> findAllBySender(String username);
	
	public List<Memo> findAllByReceiver(String username);
	
	public Optional<Memo> findById(Integer mno);
	
	// 글 하나를 읽은 것으로 변경
	public void setRead(Integer mno);
	
	// 사용자 입장에서는 메모 삭제(서버 입장에서는 메모 비활성화) - 한꺼번에 여러개 비활성화 
	public void disabledBySender(List<Integer> list);
	public void disabledByReceiver(List<Integer> list);

	public void delete();
	
	// 보낸 사람, 받은 사람 모두 비활성화한 메모를 특정 시간에 삭제하기 - @Scheduled <- spring task scheduler
	public Boolean existsByIsReadIsFalse(String receiver);
}
