package com.icia.zboard3.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attachment {
	private Integer ano;
	private Integer bno;
	private String originalFileName;
	private String saveFileName;
	private String writer;
	private Long length;
	private Boolean isImage;
}
