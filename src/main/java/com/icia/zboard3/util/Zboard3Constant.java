package com.icia.zboard3.util;

public interface Zboard3Constant {
	public final static int BOARD_PER_PAGE = 10;
	public static final int PAGE_PER_BLOCK = 5;
	
	public static final String ProfileFolder = "c:/upload/profile";
	public static final String AttachmentFolder = "c:/upload/attachment";
	public static final String ImageFolder = "c:/upload/image";
	
	// ck업로드할 때 일단 temp에 저장. 글 작성 버튼을 누르면 이미지 파일을 temp에서 image로 이동
	public static final String TempFolder = "c:/upload/temp";
	public static final String TempPath = "http://localhost:8081/temp/";
	
	public static final String ProfilePath = "http://localhost:8081/profile/";
	public static final String AttachmentPath = "http://localhost:8081/attachment/";
	public static final String ImagePath = "http://localhost:8081/image/";
	
	// temp 폴더의 이미지를 image 폴더로 이동하면 이미지를 보는 경로도 temp에서 image로 바꿔야한다
	// String.replaceAll(패턴, 변경할 문자열)
	public static final String CK_PATTERN = "src=\"http://localhost:8081/temp/";
	public static final String CK_REPLACE = "src=\"http://localhost:8081/image/";
}
