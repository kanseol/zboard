package com.icia.zboard3.exception;

import lombok.*;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class ShopJob {
	public static class ProductNotFoundException extends RuntimeException {
	}

	public static class CartItemNotFoundException extends RuntimeException {
	}

	public static class OutOfStockException extends RuntimeException {
	}

	public static class CartNotExistException extends RuntimeException {
	}

}
