package com.icia.zboard3.exception;

import lombok.*;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class UserJob {
	public static class UserNotFoundException extends RuntimeException {
	}
	
	public static class IdExistException extends RuntimeException {
	}
	
	public static class EmailExistException extends RuntimeException {
	}

	// 비밀번호 확인 실패(/user/password_check -> /user/password_check)
	public static class PasswordCheckFailException extends RuntimeException {
	}
	
	// 비밀번호 검색 실패 : 아이디 또는 이메일이 틀리다 -> /user/find를 다시 띄울거니까
	public static class FindPasswordException extends RuntimeException {
	}
	
	// 체크코드 확인 실패 : 에러메시지와 함께 /user/join으로 이동
	public static class JoinCheckFailException extends RuntimeException {
	}
}
