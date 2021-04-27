package com.icia.zboard3.entity;

import java.time.*;

import lombok.*;
import lombok.experimental.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain=true)
public class User {
	private String username;
	private String password;
	private String irum;
	private String email;
	private LocalDate joinday;
	private LocalDate birthday;
	private Integer loginFailureCnt;
	private Integer writeCnt;
	private Level levels;
	private Boolean enabled;	
	private String profile;
	private String checkCode;	
}
