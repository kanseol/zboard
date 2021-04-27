package com.icia.zboard3.util;

import java.util.*;

import org.springframework.stereotype.*;

import com.icia.zboard3.dto.*;

@Component
public class PagingUtil<T> {
	// startRowNum, endRowNum
	public Map<String, Integer> getRowNum(int pageno, int count) {
		int startRowNum = (pageno-1) * Zboard3Constant.BOARD_PER_PAGE + 1;
		int endRowNum = startRowNum + Zboard3Constant.BOARD_PER_PAGE - 1;
		if(endRowNum>count)
			endRowNum = count;
		Map<String, Integer> map = new HashMap<>();
		map.put("startRowNum", startRowNum);
		map.put("endRowNum", endRowNum);
		return map;
	}
	
	public Page<T> getPage(int pageno, int count) {
		// 글이 123개면 페이지의 개수는 13
		int countOfPage = count/Zboard3Constant.BOARD_PER_PAGE + 1;
		
		// 글이 120개일 경우 페이지의 개수는 13 -> 12
		if(count%Zboard3Constant.BOARD_PER_PAGE==0)
			countOfPage--;
		
		// pageno가 말도 안되는 값일 경우 마지막 페이지로
		if(pageno>countOfPage)
			pageno = countOfPage;
		
		/*		   pageno	 blockNo		prev		start		end		next
		  		 	1~5			0			  0			   1		 5		  6
					6~10		1			  5			   6		 10		  11
					11~15		2			  10		  11		 13		  0
		*/
		int blockNo = (pageno-1)/Zboard3Constant.PAGE_PER_BLOCK;
		int prev = blockNo*Zboard3Constant.PAGE_PER_BLOCK;
		int start = prev + 1;
		int end = prev + Zboard3Constant.PAGE_PER_BLOCK;
		int next = end + 1;
		if(end>=countOfPage) {
			end = countOfPage;
			next = 0;
		}
		return new Page<T>(pageno, prev, start, end, next, null);
	}
}




















