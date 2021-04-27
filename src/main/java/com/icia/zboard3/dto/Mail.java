package com.icia.zboard3.dto;

import lombok.*;
import lombok.experimental.*;

@Data
@AllArgsConstructor
@Builder
@Accessors(chain=true)
public class Mail {
	private String from;
	private String to;
	private String subject;
	private String text;
}
