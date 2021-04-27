package com.icia.zboard3.entity;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Product {
	private Integer pno;
	private String name;
	private Integer cno;
	private Integer price;
	private Integer stock;
	private String info;
	private String image;
	private LocalDate regday;
	private Integer numberOfReview;
	private Integer sumOfStar;
	private Integer countOfStar;
}
