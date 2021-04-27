package com.icia.zboard3.websocket;

import java.util.*;

import org.springframework.stereotype.*;
import org.springframework.web.socket.*;

// WSUser를 관리하는 서비스
@Component
public class WSUserService {
	// 다중 작업에 안전한가?
	// ArrayList vs Vector, StringBuilder vs StringBuffer
	private List<WSUser> list = new Vector<>();
	
	public void add(WebSocketSession session) {
		// 사용자이름을 검색해 이름이 있으면 웹소켓 추가, 없으면 WSUser 추가
		String username = session.getPrincipal().getName();
		for(int i=0; i<list.size(); i++) {
			if(list.get(i).getUsername().equals(username)) {
				list.get(i).add(session);
				return;
			}
		}
		WSUser user = new WSUser(username, session);
		list.add(user);
	}
	
	public int delete(WebSocketSession session) {
		// 마지막 웹소켓이면 사용자까지 삭제
		String username = session.getPrincipal().getName();
		for(int i=0; i<list.size(); i++) {
			if(list.get(i).getUsername().equals(username)) {
				if(list.get(i).getSessionCount()==1) {
					list.remove(i);
					return 1;
				} else 
					list.get(i).delete(session);
			}
		}
		return -1;
	}
	
	public void sendMsg(String sender, String receiver, String msg) {
		for(WSUser user:list) {
			System.out.println("반복 찾기 중 : " + user);
			if(user.getUsername().equals(receiver)) 
				user.sendMessage(sender+"의 메시지:" + msg);
		}
	}
	
	public void sendMsgAll(String sender, String msg) {
		for(WSUser user:list) 
			user.sendMessage(sender+"의 메시지:" + msg);
	}
}