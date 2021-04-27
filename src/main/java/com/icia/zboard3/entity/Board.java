package com.icia.zboard3.entity;

import java.time.*;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Board {
	private Integer bno;
	private String title;
	private String content;
	private String writer;
	private LocalDateTime writeTime;
	private Integer readCnt;
	private Integer attachmentCnt;
	private Integer commentCnt;
	private Integer deleteCommentCnt;
	private Integer goodCnt;
	private Integer badCnt;
}
