package com.icia.zboard3.service.mvc;

import java.io.*;
import java.nio.file.*;
import java.time.format.*;
import java.util.*;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import org.modelmapper.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.*;
import org.springframework.web.multipart.*;

import com.icia.zboard3.dao.*;
import com.icia.zboard3.dto.*;
import com.icia.zboard3.dto.BoardDto.*;
import com.icia.zboard3.entity.*;
import com.icia.zboard3.entity.Comment;
import com.icia.zboard3.exception.*;
import com.icia.zboard3.util.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class BoardService {
	private final BoardDao boardDao;
	private final AttachmentDao attachmentDao;
	private final CommentDao commentDao;
	private final ModelMapper modelMapper;
	private final PagingUtil<BoardDto.ListView> pagingUtil;
	
	@Autowired
	@Qualifier("timeFormatter")
	private DateTimeFormatter dtf;
	
	// BS-08
	// writer가 null이면 전체 페이징, writer가 있는 경우 그 사람이 쓴 글만 페이징
	public Page<BoardDto.ListView> list(int pageno, String writer) {
		int count = boardDao.count(writer);
		Page<BoardDto.ListView> page = pagingUtil.getPage(pageno, count);
		
		List<Map> mList = null;
		if(writer==null)
			mList = boardDao.findAll(pagingUtil.getRowNum(pageno, count));
		else
			mList = boardDao.findAllByWriter(pagingUtil.getRowNum(pageno, count), writer);
		
		List<BoardDto.ListView> list = new ArrayList<BoardDto.ListView>();
		mList.forEach(map->list.add(modelMapper.map(map, BoardDto.ListView.class)));
		return page.setList(list);
	}
	
	
	// ck에서 사진만 업로드하고 글을 작성하지 않으면 -> 빠져나가는 이벤트 -> ajax로 사진 삭제 처리 -> 만사 OK
	// 글 작성하지 않고 빠져나가는 이벤트가 없다
	
	// 이미지를 업로드할 때 글을 작성하지 않을 수도 있으므로 -> c:/upload/temp, http://localhost:8081/temp
	// 글을 작성하면 c:/upload/temp -> c:/upload/image로 이동,  http://localhost:8081/temp ->  http://localhost:8081/image
	// 임시 폴더에 남아있는 이미지들은 모두 고아 이미지 -> 서버 점검시 삭제
	
	// BS-09
	public Integer write(Write dto, List<MultipartFile> attachments, String username) {
		Board board = Board.builder().title(dto.getTitle()).writer(username).build();
		
		// Jsoup을 html 파싱 -> 이미지 이동
		Document document = Jsoup.parseBodyFragment(dto.getContent());
		Elements elements = document.getElementsByTag("img");
		if(elements.isEmpty()==false) {
			for(Element element:elements) {
				String src = element.attr("src");
				// http://localhost:8081/temp/122315448.jpg
				String fileName = src.substring(src.lastIndexOf("/")+1);
				
				// SPRING11 + .jpg
				// 프사 저장 : username + ofn.substring(ofn.lastIndexOf("."));
				
				File origin = new File(Zboard3Constant.TempFolder, fileName);
				File dest = new File(Zboard3Constant.ImageFolder, fileName);
				try {
					FileCopyUtils.copy(Files.readAllBytes(origin.toPath()), dest);
					origin.delete();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		// 자바에서 문자열 작업하는 함수는 문자열을 리턴한다
		// String str = "hello world";
		// str.substring(5)라고 str이 잘리는 것이 아니라 str에서 "world"를 추출해서 리턴한다
		String content = dto.getContent().replaceAll(Zboard3Constant.CK_PATTERN, Zboard3Constant.CK_REPLACE);
		board.setContent(content);
		board.setAttachmentCnt(attachments.size());
		boardDao.insert(board);
		
		// MultipartFile을 하드디스크에 저장한 다음 Attachment 객체를 생성해서 DB에 저장
		if(attachments.isEmpty()==false) {
			for(MultipartFile a:attachments) {
				// aaa.jpg -> 17253344-aaa.jpg
				String saveFileName = System.currentTimeMillis()+"-"+a.getOriginalFilename();
				File saveFile = new File(Zboard3Constant.AttachmentFolder, saveFileName);
				try {
					FileCopyUtils.copy(a.getBytes(), saveFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
				Boolean isImage = a.getContentType().toLowerCase().startsWith("image/");
				Attachment attachment = new Attachment(0, board.getBno(), a.getOriginalFilename(), saveFileName, 
					username, a.getSize(), isImage);
				attachmentDao.insert(attachment);
			}
		}
		return board.getBno();
	}
	
	// BS-10
	public BoardDto.Read read(Integer bno, String username) {
		Board result = boardDao.findById(bno).orElseThrow(()->new BoardJob.BoardNotFoundException());
		BoardDto.Read dto = modelMapper.map(result, BoardDto.Read.class);
		
		// 로그인했고 글쓴 사람이 아니라면 조회수 증가
		if(username!=null && result.getWriter().equals(username)==false) {
			int readCnt = result.getReadCnt()+1;
			boardDao.update(Board.builder().bno(bno).readCnt(readCnt).build());
			result.setReadCnt(readCnt);
		}

		// 댓글 가져오기 
		// 댓글이 10개이하면 댓글의 개수(board.getCommentCnt())만큼 읽어온다. nextCommentPage는 0
		// 댓글이 10개이상이면 10개 읽어온다. nextCommentPage는 2
		if(result.getCommentCnt()>0) {
			int commentCnt = result.getCommentCnt();
			int endRowNum = commentCnt>10? 10: commentCnt;
			int nextCommentPage = commentCnt>endRowNum? 2: 0;
			dto.setNextCommentPage(nextCommentPage);
			
			List<Comment> cList = commentDao.findFirstPage(endRowNum, bno);
			List<CommentDto.Read> comments = new ArrayList<>();
			for(Comment c:cList) {
				CommentDto.Read r = modelMapper.map(c, CommentDto.Read.class);
				r.setWriteTimeString(dtf.format(c.getWriteTime()));
				r.setIsWriter(c.getWriter().equals(username));
				comments.add(r);
			}
			dto.setComments(comments);
		}
		
		// 첨부파일 가져오기
		if(result.getAttachmentCnt()>0) 
			dto.setAttachments(attachmentDao.findAllByBno(bno));
		
		return dto;
	}

	// BS-11
	@Transactional
	public void delete(Integer bno, String username) {
		// 글이 있는가
		Board board = boardDao.findById(bno).orElseThrow(()-> new BoardJob.BoardNotFoundException());
		// 글쓴 사람이 맞는가
		if(board.getWriter().equals(username)==false)
			throw new BoardJob.IllegalAccessException();
		// DB에서 첨부파일 삭제
		List<Attachment> aList = attachmentDao.findAllByBno(bno);
		attachmentDao.deleteByBno(bno);
		// DB에서 댓글 삭제
		commentDao.deleteByBno(bno);
		// DB에서 글 삭제
		boardDao.delete(bno);
		// 서버에서 첨부파일 삭제
		if(aList.isEmpty()==false) {
			for(Attachment a:aList) {
				File file = new File(Zboard3Constant.AttachmentFolder, a.getSaveFileName());
				file.delete();
			}
		}
		// 서버에서 이미지 삭제
		Document doc = Jsoup.parseBodyFragment(board.getContent());
		Elements imgs = doc.getElementsByTag("img");
		if(imgs.isEmpty()==false) {
			for(Element img:imgs) {
				// http://localhost:8081/image/aaaa.jpg
				String src = img.attr("src");
				String fileName = src.substring(src.lastIndexOf("/")+1);
				File file = new File(Zboard3Constant.ImageFolder, fileName);
				file.delete();
			}
		}
		
	}

	// BS-12
	public void update(BoardDto.Update dto, String username) {
		/*
		 * 1. board.findById(dto.getBno()) 							-> BoardNotFoundException
		 * 2. board.getWriter().equals(username)==false				-> IllegalAccessException
		 * 3. board.getContent()에 있는 이미지 목록 : List oldImgList
		 * 4. dto.getContent()에 있는 이미지 목록 : List newImgList
		 * 5. oldImgList에는 있고 newImgList에 없는 이미지를 찾아서 삭제
		 * 6. newImgList의 이미지를 temp 폴더로 이동, 경로로 replaceAll -> boardDao.update()
		 */
		Board board = boardDao.findById(dto.getBno()).orElseThrow(()->new BoardJob.BoardNotFoundException());
		if(board.getWriter().equals(username)==false)
			throw new BoardJob.IllegalAccessException();
		
		// <img src="/zboard3/image/aaa.jpg" width="120px">
		Elements oldImgs = Jsoup.parseBodyFragment(board.getContent()).getElementsByTag("img");
		Elements newImgs = Jsoup.parseBodyFragment(dto.getContent()).getElementsByTag("img");
		
		List<String> oldImgList = new ArrayList<>();		// [aaa.jpg, bbb.jpg, ccc.jpg] -> aaa, bbb 삭제 대상
		List<String> newImgList = new ArrayList<>();		// [(xxx.jpg, zzz.jpg), ccc.jpg]
		
		for(Element e:oldImgs) 
			oldImgList.add(e.attr("src").substring(e.attr("src").lastIndexOf("/")+1));
		for(Element e:newImgs) 
			newImgList.add(e.attr("src").substring(e.attr("src").lastIndexOf("/")+1));

		// image 폴더의 aaa.jpg, bbb.jpg 삭제. ccc.jpg는 남아있다
		for(String fileName:oldImgList) {
			if(newImgList.contains(fileName)==false)
				new File(Zboard3Constant.ImageFolder, fileName).delete();
		}
		
		// temp에 있는 xxx.jpg, zzz.jpg를 image로 이동. ccc.jpg는 원래부터 image 폴더에 있었다
		for(String fileName:newImgList) {
			File origin = new File(Zboard3Constant.TempFolder, fileName);
			File dest = new File(Zboard3Constant.ImageFolder, fileName);
			try {
				// ccc.jpg로 temp에서 삭제하고 image로 옮기려고 한다 -> Exception 발생 -> exists()로 파일이 있는 지 확인 후 이동
				if(origin.exists()) {
					FileCopyUtils.copy(Files.readAllBytes(origin.toPath()), dest);
					origin.delete();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Board param = Board.builder().bno(dto.getBno()).title(dto.getTitle()).build();
		param.setContent(dto.getContent().replaceAll(Zboard3Constant.CK_PATTERN, Zboard3Constant.CK_REPLACE));
		boardDao.update(param);
	}
}






