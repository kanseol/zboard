package com.icia.zboard3.exception;

import lombok.*;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class MemoJob {
	public static class MemoNotFoundException extends RuntimeException {
	}
}
