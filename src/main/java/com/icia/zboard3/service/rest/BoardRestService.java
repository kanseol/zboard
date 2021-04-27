package com.icia.zboard3.service.rest;

import java.io.*;
import java.util.*;


import org.modelmapper.*;
import org.springframework.stereotype.*;
import org.springframework.web.multipart.*;

import com.icia.zboard3.dao.*;
import com.icia.zboard3.dto.*;
import com.icia.zboard3.entity.*;
import com.icia.zboard3.exception.*;
import com.icia.zboard3.util.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class BoardRestService {
	private final BoardDao boardDao;
	private final UserBoardDao userBoardDao;
	private final PagingUtil pagingUtil;
	private final CommentDao commentDao;
	private final AttachmentDao attachmentDao;
	private final UserDao userDao;
	private final ModelMapper modelMapper;
	
	// BS-01.
	public CKResponse ckImageUpload(MultipartFile upload) {
		if(upload!=null && upload.isEmpty()==false) {
			if(upload.getContentType().toLowerCase().startsWith("image/")) {
				String originalFileName = upload.getOriginalFilename();
				String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
				String imageName = UUID.randomUUID().toString() + extension;
				File file = new File(Zboard3Constant.TempFolder, imageName);
				try {
					upload.transferTo(file);
				} catch (IllegalStateException | IOException e) {
					e.printStackTrace();
				}
				return new CKResponse(1, imageName, Zboard3Constant.TempPath + imageName);
			}
		}
		return null;
	}
	
	// BS-02.
	public CommentDto.Rest nextComment(Integer pageno, Integer bno) {
		Board board = boardDao.findById(bno).orElseThrow(()->new BoardJob.BoardNotFoundException());
		Map<String,Integer> rownumMap = pagingUtil.getRowNum(pageno, board.getCommentCnt());
		
		// pageno가 2, endRowNum은 20, 댓글의 개수가 35라면 pageno=3
		// pageno가 3, endRowNum은 30, 댓글의 개수가 35라면 pageno=4
		// pageno가 4, endRowNum은 40 -> pageno=0
		int nextCommentPage = board.getCommentCnt()>rownumMap.get("endRowNum") ? pageno+1 : 0;
		return new CommentDto.Rest(commentDao.findAll(rownumMap, bno), nextCommentPage);
	}

	// BS-03.
	public CommentDto.Rest writeComment(CommentDto.Write dto, String username) {
		Board board = boardDao.findById(dto.getBno()).orElseThrow(()->new BoardJob.BoardNotFoundException());
		// 예외가 발생하지 않을 거야 -> get()으로 꺼낸다 -> 만약 null이면 NoSuchElementException 발생
		String profile = userDao.findById(username).get().getProfile();
		Comment comment = modelMapper.map(dto, Comment.class);
		comment.setWriter(username);
		comment.setProfile(Zboard3Constant.ProfilePath + profile);
		commentDao.insert(comment);
		boardDao.update(Board.builder().bno(dto.getBno()).commentCnt(board.getCommentCnt()+1).build());
		
		int endRowNum = board.getCommentCnt()>10 ? 10 : board.getCommentCnt();
		int nextCommentPage = board.getCommentCnt()>10 ? 2 : 0;
		return new CommentDto.Rest(commentDao.findFirstPage(endRowNum, dto.getBno()), nextCommentPage);
	}
	
	// BS-04
	public CommentDto.Rest deleteComment(Integer cno, Integer bno, String username) {
		Board board = boardDao.findById(bno).orElseThrow(()->new BoardJob.BoardNotFoundException());
		Comment comment = commentDao.findById(cno).orElseThrow(()->new BoardJob.CommentNotFoundException());
		if(comment.getWriter().equals(username)==false)
			throw new BoardJob.IllegalAccessException();
		commentDao.delete(cno);
		boardDao.update(Board.builder().deleteCommentCnt(board.getDeleteCommentCnt()+1).commentCnt(board.getCommentCnt()-1).bno(bno).build());
		
		int endRowNum = board.getCommentCnt()>10 ? 10 : board.getCommentCnt();
		int nextCommentPage = board.getCommentCnt()>10 ? 2 : 0;
		return new CommentDto.Rest(commentDao.findFirstPage(endRowNum, bno), nextCommentPage);
	}
	
	// BS-05
	public Attachment findAttachmentyId(Integer ano) {
		return attachmentDao.findById(ano).orElseThrow(()->new BoardJob.AttachmentNotFoundException());
	}

	// BS-06
	public List<Attachment> deleteAttachment(Integer ano, Integer bno, String username) {
		Board board = boardDao.findById(bno).orElseThrow(()->new BoardJob.BoardNotFoundException());
		Attachment attachment = attachmentDao.findById(ano).orElseThrow(()->new BoardJob.AttachmentNotFoundException());
		if(attachment.getWriter().equals(username)==false)
			throw new BoardJob.IllegalAccessException();
		attachmentDao.delete(ano);
		
		File file = new File(Zboard3Constant.AttachmentFolder, attachment.getSaveFileName());
		if(file.exists())
			file.delete();
		boardDao.update(Board.builder().bno(bno).attachmentCnt(board.getAttachmentCnt()-1).build());
		return attachmentDao.findAllByBno(bno);
	}
	
	// BS-07.
	public Integer goodOrBad(Integer bno, Boolean isGood, String username) {
		// userBoardDao.existsById(username, bno) -> 있으면 추천수/비추수 리턴
		// 없으면 userBoardDao.insert(username, bno) -> 추천수 증가 -> 추천수 리턴
		Board board = boardDao.findById(bno).orElseThrow(()->new BoardJob.BoardNotFoundException());
		if(board.getWriter().equals(username)==true)
			throw new BoardJob.IllegalAccessException();
		
		// 이미 추천 또는 비추한 글이라면 현재 추천수 또는 비추수를 리턴
		if(userBoardDao.existsById(username, bno))
			return isGood==true? board.getGoodCnt() : board.getBadCnt();
			
		userBoardDao.insert(username, bno);
		if(isGood==true) {
			boardDao.update(Board.builder().goodCnt(board.getGoodCnt()+1).bno(bno).build());
			return board.getGoodCnt()+1;
		} else {
			boardDao.update(Board.builder().badCnt(board.getBadCnt()+1).bno(bno).build());
			return board.getBadCnt()+1;
		}
	}
}