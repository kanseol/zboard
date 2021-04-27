package com.icia.zboard3.dto;

import lombok.*;
import lombok.experimental.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemoDto {
	@Data
	public static class Write {
		private String title;
		private String content;
		private String receiver;				
	}
	
	@Data
	@Accessors(chain=true)
	public static class ListView {
		private Integer mno;					
		private String title;
		private String sender;		
		private String receiver;	
		private String writeTimeString;
		private Boolean isRead;						
	}
	
	@Data
	@Accessors(chain=true)
	public static class Read {
		private Integer mno;
		private String title;
		private String content;
		private String sender;	
		private String receiver;
		private String writeTimeString;
		private Boolean isRead;	
	}
}



