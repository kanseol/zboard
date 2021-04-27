package com.icia.zboard3.editor;

import java.beans.*;
import java.util.*;

import org.apache.commons.lang3.math.*;
import org.springframework.stereotype.*;

// 1,11,22 -> List<Integer>로 변환 -> @ModelAttribute일 때만 동작
@Component
public class IntArrayListEditor extends PropertyEditorSupport {
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		List<Integer> list = new ArrayList<>();
		String[] ar = text.split(",");
		for(String str: ar)
			list.add(NumberUtils.toInt(str, 0));
		setValue(list);
	}
}
