package com.icia.zboard3.dto;

import java.time.*;

import javax.validation.constraints.*;

import org.springframework.format.annotation.*;

import lombok.*;
import lombok.experimental.*;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class UserDto {
	@Data
	public static class Join {
		@NotNull(message="아이디는 필수입력입니다")
		@Pattern(regexp="^[A-Z0-9]{8,10}$", message="아이디는 영숫자 8~10자입니다" )
		private String username;
		
		// 비밀번호는 [특수문자를 하나이상 포함], [숫자를 하나이상 포함]하는 [영숫자와 특수문자 8~10자]입니다
		// () 하나의 조건
		// (?= ) 문자열의 앞에서 부터 조건을 체크해라(전방 탐색)
		// . : 임의의 글자, * : 0개 이상, + : 1개 이상
		// .* : 임의의 글자가 0개 이상(임의의 글자가 있을 수도 없을 수도 있다)
		@NotNull(message="비밀번호는 필수입력입니다")
		@Pattern(regexp="(?=.*[!@#$%^&*])^[A-Za-z0-9!@#$%^&*]{8,10}$", message="비밀번호는 특수문자를 포함하는 영숫자 8~10자입니다")
		private String password;
		
		@NotNull(message="이름은 필수입력입니다")
		@Pattern(regexp="^[가-힣]{2,5}$", message="이름은 한글 2~5자입니다")
		private String irum;
		
		@NotNull(message="이메일은 필수입력입니다")
		@Email(message="잘못된 이메일 형식입니다")
		private String email;
		
		@NotNull(message="생일은 필수입력입니다")
		@DateTimeFormat(pattern="yyyy-MM-dd")
		private LocalDate birthday;
	}
	
	@Data
	public static class ChangePassword {
		@NotNull(message="비밀번호는 필수입력입니다")
		@Pattern(regexp="(?=.*[!@#$%^&*])^[A-Za-z0-9!@#$%^&*]{8,10}$", message="비밀번호는 특수문자를 포함하는 영숫자 8~10자입니다")
		private String password;
		
		@NotNull(message="비밀번호는 필수입력입니다")
		@Pattern(regexp="(?=.*[!@#$%^&*])^[A-Za-z0-9!@#$%^&*]{8,10}$", message="비밀번호는 특수문자를 포함하는 영숫자 8~10자입니다")
		private String newPassword;
	}
	
	@Data
	public static class Update {
		private String email;
		private String irum;
		
		@Pattern(regexp="(?=.*[!@#$%^&*])^[A-Za-z0-9!@#$%^&*]{8,10}$", message="비밀번호는 특수문자를 포함하는 영숫자 8~10자입니다")
		private String password;
		
		@Pattern(regexp="(?=.*[!@#$%^&*])^[A-Za-z0-9!@#$%^&*]{8,10}$", message="비밀번호는 특수문자를 포함하는 영숫자 8~10자입니다")
		private String newPassword;
	}
	
	@Data
	public static class ResetPassword {
		private String username;
		private String email;
	}
	
	@Data
	@Accessors(chain=true)
	public static class Info {
		private String irum;
		private String email;
		private String joindayString;
		private Long day;				
		private String birthdayString;
		private Integer writeCnt;
		private String level;
		private String profile;
	}

}