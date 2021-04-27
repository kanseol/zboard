package com.icia.zboard3.service.rest;

import java.io.*;

import org.apache.commons.lang3.*;
import org.modelmapper.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.*;
import org.springframework.web.multipart.*;

import com.icia.zboard3.dao.*;
import com.icia.zboard3.dto.*;
import com.icia.zboard3.entity.*;
import com.icia.zboard3.exception.*;
import com.icia.zboard3.util.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class UserRestService {
	private final UserDao userDao;
	private final AuthorityDao authorityDao;
	private final PasswordEncoder passwordEncoder;
	private final MailUtil mailUtil;
	private final ModelMapper modelMapper;
	
	// US-01.
	// 아이디나 이메일이 존재할 경우 사용자 정의 예외 -> ControllerAdvice에서 오류 메시지 처리
	@Transactional(readOnly=true)
	public void idAvailableCheck(String username) {
		if(userDao.existsById(username))
			throw new UserJob.IdExistException();
	}

	// US-02.
	@Transactional(readOnly=true)
	public void emailAvailableCheck(String email) {
		if(userDao.existsByEmail(email))
			throw new UserJob.EmailExistException();
		
	}

	// US-03.
	@Transactional
	public void join(UserDto.Join dto, MultipartFile profile) {
		User user = modelMapper.map(dto, User.class);
		// 프사는 default.jpg를 기본값으로 지정
		user.setProfile("default.jpg");
		

		if(profile!=null && profile.isEmpty()==false) {
			// 업로드한 사진의 이름을 꺼낸다 : aaa.jpg
			String originalFileName = profile.getOriginalFilename();
			// 업로드한 사진의 확장자를 잘라낸다 : .jpg
			String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
			// 프사의 이름은 아이디와 같다 : SPRING22 + .JPG -> SPRING22.jpg
			String profileFileName = user.getUsername() + extension;
			// 폴더와 프사이름을 가지고 하드 디스크에 파일 생성
			File file = new File(Zboard3Constant.ProfileFolder, profileFileName);
			try {
				FileCopyUtils.copy(profile.getBytes(), file);
				// 프사를 업로드했고 저장에 성공한 경우 프사를 변경
				user.setProfile(profileFileName);  
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
			
		
		String checkCode = RandomStringUtils.randomAlphanumeric(20);
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword).setCheckCode(checkCode).setLevels(Level.NORMAL);
		
		userDao.insert(user);
		authorityDao.insert(user.getUsername(), "ROLE_USER");
		mailUtil.sendJoinCheckMail("admin@icia.co.kr", user.getEmail(), checkCode);
	}

	// US-04.
	@Transactional(readOnly=true)
	public String findId(String email) {
		return userDao.findByEmail(email).orElseThrow(()-> new UserJob.UserNotFoundException()).getUsername();
	}

	// US-05.
	public void changeIrum(String irum, String username) {
		userDao.findById(username).orElseThrow(()-> new UserJob.UserNotFoundException());
		userDao.update(User.builder().username(username).irum(irum).build());
	}

	// US-06.
	public void changePassword(UserDto.ChangePassword dto, String username) {
		User user = userDao.findById(username).orElseThrow(()-> new UserJob.UserNotFoundException());
		if(passwordEncoder.matches(dto.getPassword(), user.getPassword())==false)
			throw new UserJob.PasswordCheckFailException();
		String newEncodedPassword = passwordEncoder.encode(dto.getNewPassword());
		userDao.update(User.builder().username(username).password(newEncodedPassword).build());
	}

	// US-07.
	public void update(UserDto.Update dto, MultipartFile profile, String username) {
		User result = userDao.findById(username).orElseThrow(()-> new UserJob.UserNotFoundException());
		
		// ModelMapper를 사용하지 않은 이유
		// UserDto.Update의 password, newPassword은 선택 입력 -> ModelMapper로 변환하면
		//				DTO dto					User param
		// irum: 		홍길동					홍길동
		// email:		hkd@naver.com			hkd@naver.com
		// password:	1234567!				[1234567! -> 비밀번호가 null이 아니므로 비밀번호가 변경]
		// newPassword: null					
		User param = User.builder().irum(dto.getIrum()).email(dto.getEmail()).username(username).build();
	
		if(profile!=null && profile.isEmpty()==false) {
			// 프사이름을 사용자 아이디 + 업로드한 프사의 확장자
			String oringinalFileName = profile.getOriginalFilename();
			String extension = oringinalFileName.substring(oringinalFileName.lastIndexOf("."));
			String newProfileFileName = username + extension.toUpperCase();
			
			/*
			// 아이디가 SPRING -> SPRING.JPG, SPRING.JPEG, SPRING.PNG, SPRING.GIF가 가능. 모두 삭제
			// 클래스이름s 라는 이름을 가진 클래스들 : 유틸리티	Objects, Collections (List, Map, Set-중복 불가능), Arrays
			// Arrays.asList : 배열을 ArrayList로 변환 -> 읽기 전용
			List<String> availableFileNames = Arrays.asList(username+".JPG", username+".JPEG", username+".PNG", username+".GIF");
			for(String afName:availableFileNames) {
				// 파일이 있으면 파일을 열고, 파일이 없으면 파일을 생성
				File file = new File(Zboard3Constant.ProfileFolder, afName);
				if(file.exists()==true)
					file.delete();
			}
			*/
			
			// 기존 프사가 default.jpg가 아닌 경우 삭제
			if(result.getProfile().equals("default.jpg")==false) {
				File oldProfileFile = new File(Zboard3Constant.ProfileFolder, result.getProfile());
				if(oldProfileFile.exists()==true)
					oldProfileFile.delete();
			}
			
			File file = new File(Zboard3Constant.ProfileFolder, newProfileFileName);
			try {
				profile.transferTo(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(dto.getPassword()!=null && dto.getNewPassword()!=null) {
			String encodedPassword = result.getPassword();
			if(passwordEncoder.matches(dto.getPassword(), encodedPassword)==true)
				param.setPassword(passwordEncoder.encode(dto.getPassword()));
		}
		userDao.update(param);
	}
	
	// US-08.
	@Scheduled(cron="0 0 4 ? * THU")
	public void deleteByCheckCodeIsNotNull() {
		userDao.deleteByCheckCodeIsNotNull();
	}
}
