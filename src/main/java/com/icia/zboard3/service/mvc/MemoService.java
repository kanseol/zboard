package com.icia.zboard3.service.mvc;

import java.time.format.*;
import java.util.*;
import java.util.stream.*;

import org.modelmapper.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

import com.icia.zboard3.dao.*;
import com.icia.zboard3.dto.*;
import com.icia.zboard3.entity.*;
import com.icia.zboard3.exception.*;
import com.icia.zboard3.websocket.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class MemoService {
	private final MemoDao memoDao;
	private final MessageWebSocketHandler handler;
	private final ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("timeFormatter")
	private DateTimeFormatter dtf;
	
	public void write(MemoDto.Write dto, String username) {
		Memo memo = modelMapper.map(dto, Memo.class).setSender(username);
		memoDao.insert(memo);
		handler.sendMessage(memo.getSender(), memo.getReceiver(), memo.getTitle());
	}

	public List<MemoDto.ListView> send(String username) {
		List<Memo> list = memoDao.findAllBySender(username);
		List<MemoDto.ListView> memos = new ArrayList<>();
		for(Memo memo: list) {
			memos.add(modelMapper.map(memo, MemoDto.ListView.class).setWriteTimeString(dtf.format(memo.getWriteTime())));
		}
		return memos;
	}

	// 자바 8 : lambda, stream api, optional, NIO(java io는 읽기/쓰기 작업을 blocking된다) -> 스프링5 webflux -> jdbc가 blocking
	// stream() -> map() 변환담당 -> collect(변환된 원소들을 모아서 ArrayList로 변환)
	public List<MemoDto.ListView> receive(String username) {
		return memoDao.findAllByReceiver(username).stream().map(m->modelMapper.map(m, MemoDto.ListView.class).setWriteTimeString(dtf.format(m.getWriteTime()))).collect(Collectors.toList());
	}

	public MemoDto.Read read(Integer mno) {
		Memo memo = memoDao.findById(mno).orElseThrow(BoardJob.MemoNotFoundException::new);
		memoDao.setRead(mno);
		return modelMapper.map(memo, MemoDto.Read.class).setWriteTimeString(dtf.format((memo.getWriteTime())));
	}

	public void disabledBySender(List<Integer> list) {
		memoDao.disabledBySender(list);
	}
	
	public void disabledByReceiver(List<Integer> list) {
		memoDao.disabledByReceiver(list);
	}
	
	// cron 표기법 : 리눅스의 스케줄 표현법. 7자리 -> 스프링은 마지막 년도를 생략해서 6자리
	//				cron maker로 작성
	@Scheduled(cron="0 0 4 1/1 * ?")
	public void delete() {
		memoDao.delete();
	}
}
