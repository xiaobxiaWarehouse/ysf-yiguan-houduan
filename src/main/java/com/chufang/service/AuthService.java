package com.chufang.service;

import javax.servlet.http.HttpServletRequest;

import com.chufang.annotation.Roles;
import com.chufang.entity.PT_KEHUXX;
import com.chufang.entity.YG_YUANGONGXX;
import com.chufang.entity.ext.AccountDto;
import com.chufang.entity.ext.UserDetail;
import com.chufang.parameter.UserInfoParam;

import net.sf.json.JSONObject;

public interface AuthService {
	JSONObject authByPwd(String account, String pwd, String ip,
			int userType, String sessionVcode, String vcode);
	
	JSONObject authByVcode(String account, String ip,
			int userType, String sessionVcode, String vcode, String password);
	
	boolean hasAccess(int roleId, Roles roles);
	
	JSONObject sendVcode(String account, String vcode, boolean checkAccount);
	
	JSONObject updateUserInfo(UserInfoParam userInfo);
	
	JSONObject getUserInfo(long id);
	
	JSONObject newUserInfo(AccountDto account);
	
	PT_KEHUXX getKehuxxById(long id);
	
	YG_YUANGONGXX getYgYuanGongXX(long kehuId);
	
	JSONObject checkUserLogon(HttpServletRequest request);
	
	JSONObject menuGet(UserDetail userDetail);
	
	JSONObject checkNeedVcode(String account);

}
