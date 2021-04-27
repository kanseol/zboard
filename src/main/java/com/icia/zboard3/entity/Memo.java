package com.icia.zboard3.entity;

import java.time.*;

import lombok.*;
import lombok.experimental.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain=true)
public class Memo {
	private Integer mno;
	private String title;
	private String content;
	private String sender;					// 보낸 아이디 : Principal
	private String receiver;				// 받는 아이디 : 입력
	private LocalDateTime writeTime;
	private Boolean isRead;					// 받는 사람이 읽었느냐?
	private Boolean disabledBySender;			// 보낸 측에서 비활성화
	private Boolean disabledByReceiver;		// 받는 측에서 비활성화
	
	// 보낸쪽, 받는쪽 모두 비활성화했다면 @Scheduled로 일정한 때에 정말 삭제
}
