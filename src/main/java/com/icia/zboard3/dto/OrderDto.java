package com.icia.zboard3.dto;

import java.util.*;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderDto {
	@Data
	public static class ReceiverOrder {
		private List<Integer> pnos;
		private List<Integer> counts;
	}
}
