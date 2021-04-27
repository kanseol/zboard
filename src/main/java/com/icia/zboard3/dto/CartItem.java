package com.icia.zboard3.dto;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class CartItem {
	private Integer pno;
	private String image;
	private String name;
	private Integer price;
	private Integer count;
	private Integer itemPrice;
	
	// 생성자
	public CartItem(int pno, String image, String name, int price, int count) {
		this.pno = pno;
		this.image = image;
		this.name = name;
		this.price = price;
		this.count = count;
		this.itemPrice = this.price * this.count;
	}
	
	// 개수 증가
	public void increase() {
		count++;
		itemPrice = price * count;
	}
	
	// 개수 감소
	public void decrease() {
		if(count>0) {
			count--;
			itemPrice = price * count;
		} 
	}
}
