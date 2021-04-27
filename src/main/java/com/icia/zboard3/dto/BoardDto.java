package com.icia.zboard3.dto;

import java.util.*;

import javax.validation.constraints.*;

import com.icia.zboard3.entity.*;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardDto {
	// Write
	@Data
	public static class Write {
		@NotNull
		private String title;
		private String content;
	}
	
	// Read
	@Data
	public static class Read {
		private Integer bno;
		private String title;
		private String content;
		private String writer;
		private String writeTimeString;
		private Integer readCnt;
		private Integer attachmentCnt;
		private Integer goodCnt;
		private Integer badCnt;
		private Boolean isWriter;
		private List<Attachment> attachments;
		private List<CommentDto.Read> comments;
		// 다음으로 버튼에 저장할 댓글 다음 페이지 번호
		private Integer nextCommentPage;
	}
	
	// ListView
	@Data
	public static class ListView {
		private Integer bno;
		private String title;
		private String writer;
		private String writeTimeString;
		private Integer readCnt;
		private Integer attachmentCnt;
		private Integer commentCnt;
	}

	@Data
	public static class Update {
		@NotNull
		private Integer bno;
		@NotNull
		private String title;
		private String content;
	}
}




