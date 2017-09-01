package com.chufang.test.service;

import javax.annotation.Resource;

import org.junit.Test;

import com.chufang.parameter.UserInfoParam;
import com.chufang.service.AuthService;
import com.chufang.test.BaseTest;

public class AuthTest extends BaseTest {

	@Resource
	private AuthService authService;
	
	
	
//	@Test
	public void testByPwd() {
		String account = "1";
		String pwd = "1";
		String ip = "127.0.0.1";
		int userType = 1;
		String sessionVcode = "123456";
		String vcode = "123456";
		System.out.println(authService.authByPwd(account, pwd, ip, userType, sessionVcode, vcode));
	}
	
//	@Test
	public void testUpdate() {
		UserInfoParam userInfo = new UserInfoParam();
		userInfo.setYuanmima("2");
		userInfo.setId(1L);
		userInfo.setXingbie(2);
		userInfo.setXingming("whltest");
		userInfo.setMima("2");
		userInfo.setShoujihm("1111");
		userInfo.setYanzhengma("11");
		userInfo.setVerifyYanzhengma("11");
		System.out.println(authService.updateUserInfo(userInfo));
	}
}
