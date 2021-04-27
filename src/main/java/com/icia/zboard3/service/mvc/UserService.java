package com.icia.zboard3.service.mvc;

import java.time.*;
import java.time.format.*;
import java.time.temporal.*;

import org.apache.commons.lang3.*;
import org.modelmapper.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;

import com.icia.zboard3.dao.*;
import com.icia.zboard3.dto.*;
import com.icia.zboard3.entity.*;
import com.icia.zboard3.exception.*;
import com.icia.zboard3.util.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class UserService {
	private final UserDao userDao;
	private final PasswordEncoder passwordEncoder;
	private final MailUtil mailUtil;
	private final ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("dateFormatter")
	private DateTimeFormatter dtf;
	
	// US-09.
	public void joinCheck(String checkCode) {
		User result = userDao.findByCheckCode(checkCode).orElseThrow(()->new UserJob.JoinCheckFailException());
		userDao.joinCheck(result.getUsername());
	}

	// US-10.
	public void resetPwd(String username, String email) {
		User result = userDao.findById(username).orElseThrow(()-> new UserJob.FindPasswordException());
		if(result.getEmail().equals(email)==false)
			throw new UserJob.FindPasswordException();
		
		String password = RandomStringUtils.randomAlphanumeric(20);
		String encodedPassword = passwordEncoder.encode(password);
		userDao.update(User.builder().username(username).password(encodedPassword).build());
		mailUtil.sendResetPasswordMail("admin@icia.co.kr", email, password);
	}

	// US-11.
	public void passwordCheck(String password, String username) {
		String encodedPassword = userDao.findById(username).orElseThrow(()-> new UserJob.UserNotFoundException()).getPassword();
		if(passwordEncoder.matches(password, encodedPassword)==false)
			throw new UserJob.PasswordCheckFailException();
	}

	// US-12.
	public UserDto.Info info(String username) {
		User user = userDao.findById(username).orElseThrow(()-> new UserJob.UserNotFoundException());
		UserDto.Info dto = modelMapper.map(user, UserDto.Info.class);
		
		dto.setLevel(user.getLevels().name()).setBirthdayString(dtf.format(user.getBirthday())).setJoindayString(dtf.format(user.getJoinday()));
		dto.setDay(ChronoUnit.DAYS.between(user.getJoinday(), LocalDate.now()));
		dto.setProfile(Zboard3Constant.ProfilePath + user.getProfile()); 
		return dto;
	}

	// US-13.
	public void resign(String username) {
		userDao.findById(username).orElseThrow(()-> new UserJob.UserNotFoundException());
		userDao.delete(username);
	}
}