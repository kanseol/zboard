package com.icia.zboard3.dto;

import java.util.*;

import lombok.*;
import lombok.experimental.*;

@Data
@AllArgsConstructor
@Accessors(chain=true)
public class Page<T> {
	private Integer pageno;
	private Integer prev;
	private Integer start;
	private Integer end;
	private Integer next;
	private List<T> list;
}
