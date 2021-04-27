package com.icia.zboard3.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class CKResponse {
	private Integer uploaded;
	private String fileName;
	private String url;
}
