package com.icia.zboard3.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductDto {
	@Data
	public static class Write {
		private String name;
		private Integer cno;
		private Integer price;
		private Integer stock;
		private String info;
		private String image;
	}
}
