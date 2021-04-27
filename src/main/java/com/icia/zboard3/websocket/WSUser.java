package com.icia.zboard3.websocket;

import java.io.*;
import java.util.*;

import org.springframework.web.socket.*;

import lombok.*;
import lombok.extern.slf4j.*;

// 한명의 사용자 : 화면이 하나만 있을 경우(Single Page Application - vue.js, react.js) 사용자==화면(웹소켓)
// 			    웹의 화면이 여러개일 수 있다....웹 소켓 여러개
@Slf4j
@Data
public class WSUser {
	private String username;
	
	// 로그인 사용자 한명이 가진 웹 소켓(화면)
	private List<WebSocketSession> list = new Vector<>();
	
	// 로그인하면 WSUser 객체 생성
	public WSUser(String username, WebSocketSession session) {
		this.username = username;
		list.add(session);
	}
	
	// 새화면을 열거나 페이지를 이동
	public void add(WebSocketSession session) {
		list.add(session);
	}
	
	public void delete(WebSocketSession session) {
		list.remove(session);
	}
	
	public void sendMessage(String msg) {
		TextMessage message = new TextMessage(msg);
		for(WebSocketSession session:list) {
			try {
				session.sendMessage(message);
			} catch (IOException e) {
				log.warn("{}", e.getMessage());
			}
		}
	}
	
	// 웹 소켓 연결을 끊을 때 만약 소켓 숫자가 1개라면 유저까지 삭제
	public int getSessionCount() {
		return list.size();
	}
}