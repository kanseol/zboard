package com.icia.zboard3.entity;

import java.time.*;

import com.fasterxml.jackson.annotation.*;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
	private Integer cno;
	private Integer bno;
	private String writer;
	private String content;
	
	// json 출력 형식을 지정하는 jackson 어노테이션
	// 어노테이션을 사용하면 jackson이 메시지 컨버터 설정을 변경
	@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
	private LocalDateTime writeTime;
	private String profile;
}
