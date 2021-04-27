package com.icia.zboard3.entity;

import lombok.Data;

@Data
public class Category {
	private Integer clevel;
	private Integer cno;
	private String name;
	private Integer parentCno;
}
