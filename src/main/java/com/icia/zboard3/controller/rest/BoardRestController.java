package com.icia.zboard3.controller.rest;

import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;

import javax.validation.*;

import org.springframework.http.*;
import org.springframework.security.access.annotation.*;
import org.springframework.security.access.prepost.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import com.icia.zboard3.dto.*;
import com.icia.zboard3.entity.*;
import com.icia.zboard3.service.rest.*;
import com.icia.zboard3.util.*;

import lombok.*;

@RequiredArgsConstructor
@RestController
public class BoardRestController {
	private final BoardRestService service;
	
	// B-01. ck 이미지 업로드
	@Secured("ROLE_USER")
	@PostMapping(value="/boards/image", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CKResponse> ckImageUpload(MultipartFile upload) {
		CKResponse ckResponse = service.ckImageUpload(upload);
		return ResponseEntity.ok(ckResponse);
	}
	
	// B-02. 다음 댓글 읽기 -> 같은 주소에 @Secured가 걸려있다 -> @Secured 동작한다
	@GetMapping(value="/comments/next", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommentDto.Rest> nextComment(@NonNull Integer pageno, @NonNull Integer bno) {
		return ResponseEntity.ok(service.nextComment(pageno, bno));
	}
	
	// B-03. 댓글 추가
	@Secured("ROLE_USER")
	@PostMapping(value="/comments", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommentDto.Rest> writeComment(@Valid CommentDto.Write dto, BindingResult bindingResult, Principal principal) throws BindException {
		if(bindingResult.hasErrors())
			throw new BindException(bindingResult);
		return ResponseEntity.ok(service.writeComment(dto, principal.getName()));
	}
	
	// B-04. 댓글 삭제
	@Secured("ROLE_USER")
	@DeleteMapping(value="/comments", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommentDto.Rest> deleteComment(@NonNull Integer cno, @NonNull Integer bno, Principal principal) {
		return ResponseEntity.ok(service.deleteComment(cno, bno, principal.getName()));
	}
	
	// B-06. 첨부파일 삭제
	@Secured("ROLE_USER")
	@DeleteMapping(value="/attachments/{ano}", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Attachment>> deleteAttachment(@PathVariable Integer ano, @NonNull Integer bno, Principal principal) {
		return ResponseEntity.ok(service.deleteAttachment(ano,bno,principal.getName()));
	}
	
	// B-05. 첨부파일 보기 
	@GetMapping(value="/attachments/{ano}", produces=MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> viewAttachment(@PathVariable Integer ano) {
		Attachment attachment = service.findAttachmentyId(ano);
		// 사용자에게 보여주거나 다운로드할 원본 파일명
		String originalFileName = attachment.getOriginalFileName();
		// 저장된 파일명을 이용해 파일을 연다
		File file = new File(Zboard3Constant.AttachmentFolder, attachment.getSaveFileName());
		// request, response : 헤더(편지봉투) + 바디(편지지)
		HttpHeaders headers = new HttpHeaders();
		if(attachment.getIsImage()) {
			// 이미지일 경우 보여주기 설정 - 확장자를 이용해 ContentType을 지정
			String extension = originalFileName.substring(originalFileName.lastIndexOf(".")).toUpperCase();
			MediaType type = MediaType.IMAGE_JPEG;
			if(extension.equals("PNG"))
				type = MediaType.IMAGE_PNG;
			else if(extension.equals("GIF"))
				type=MediaType.IMAGE_GIF;
			headers.setContentType(type);
			headers.add("Content-Disposition", "inline;filename=" + originalFileName);
		} else 
			// 이미지가 아닐 경우 다운로드
			headers.add("Content-Disposition", "attachment;filename=" + originalFileName);
		try {
			// 파일을 byte 배열로 읽어야 하는 경우 : FileCopyUtils(바이트 배열, 목적지 파일), 아래
			// MultipartFile의 경우 getBytes() 메소드를 제공
			// File 객체에는 그런 메소드가 없다
			return ResponseEntity.ok().headers(headers).body(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// B-07. 글 추천 또는 비추
	@Secured("ROLE_USER")
	@PatchMapping(value="/board/goodOrBad", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> goodOrBad(@NonNull Integer bno, @NonNull Boolean isGood, Principal principal) {
		// Boolean으로 받으려면 "0" 또는 "1", "false" 또는 "true".			true/false
		// 클라에서 서버로 보내는 방법 : urlencoded, multipart/form-data, application/json -> @RequestBody
		
		Integer result = service.goodOrBad(bno, isGood, principal.getName());
		return ResponseEntity.ok(result);
	}
}









