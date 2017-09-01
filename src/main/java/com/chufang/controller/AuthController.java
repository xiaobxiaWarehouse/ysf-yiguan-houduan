package com.chufang.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chufang.annotation.NoAuth;
import com.chufang.annotation.UserInfo;
import com.chufang.consts.ChufangConsts;
import com.chufang.entity.ext.AccountDto;
import com.chufang.entity.ext.UserDetail;
import com.chufang.parameter.UserInfoParam;
import com.chufang.service.AuthService;
import com.chufang.util.CookieUtil;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/auth")
public class AuthController extends BaseController {
	
	@Resource
	private AuthService authService;
	
	private final static Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	
	@RequestMapping(value = "/logon/{pt}/byPwd", method = RequestMethod.POST)
	@NoAuth
	public @ResponseBody JSONObject logonByPwd(HttpServletRequest request, 
			HttpServletResponse response, String account, String password, String vcode,
			@PathVariable("pt") String source) throws Exception {
		JSONObject logonRst = new JSONObject();
		JSONObject rst = authService.authByPwd(account, password,
				getRemoteAddrIp(request), "yg".equals(source) ? 1 : 2, 
						getVcodeFromSession(request, ChufangConsts.VCODE_KEY), vcode);
		
		logonRst.put(ChufangConsts.RC, rst.getInt(ChufangConsts.RC));
		logonRst.put(ChufangConsts.NEED_VCODE, rst.getInt(ChufangConsts.NEED_VCODE));
		
		if (rst.getInt(ChufangConsts.RC) == ChufangConsts.OK) {
			saveSession(rst, request.getSession());
			logonRst.put("userName", rst.getString(ChufangConsts.LONGON_USER_NAME));
			logonRst.put("userAccount", rst.getString(ChufangConsts.LONGON_USER_ACCOUNT));
			logonRst.put("yiguanName", rst.getString(ChufangConsts.YIGUAN_NAME));
		}
		return  wrapResult(logonRst);
	}
	
	@RequestMapping(value = "/logon/{pt}/byVcode", method = RequestMethod.POST)
	@NoAuth
	public @ResponseBody JSONObject logonByVcode(HttpServletRequest request, 
			HttpServletResponse response, String account, String vcode,
			@PathVariable("pt") String source, String password) throws Exception {
		JSONObject logonRst = new JSONObject();
		JSONObject rst = authService.authByVcode(account,
				getRemoteAddrIp(request), "yg".equals(source) ? 1 : 2, 
						getVcodeFromSession(request, ChufangConsts.VCODE_KEY2), vcode, password);
		
		logonRst.put(ChufangConsts.RC, rst.getInt(ChufangConsts.RC));

		if (rst.getInt(ChufangConsts.RC) == ChufangConsts.OK) {
			saveSession(rst, request.getSession());
			logonRst.put("userName", rst.getString(ChufangConsts.LONGON_USER_NAME));
			logonRst.put("userAccount", rst.getString(ChufangConsts.LONGON_USER_ACCOUNT));
			logonRst.put("yiguanName", rst.getString(ChufangConsts.YIGUAN_NAME));
		}
		return wrapResult(rst);
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	@NoAuth
	public @ResponseBody JSONObject logout(HttpServletRequest request) throws Exception {
		JSONObject rst = new JSONObject();
		rst.put(ChufangConsts.RC, ChufangConsts.OK);
		request.getSession().removeAttribute(ChufangConsts.USER_INFO_KEY);
		return rst;
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody JSONObject updateMima(HttpServletRequest request, 
			UserInfoParam userInfo, @UserInfo UserDetail userDetail) throws Exception {
		userInfo.setVerifyYanzhengma(getVcodeFromSession(request, ChufangConsts.VCODE_KEY2));
		userInfo.setId(userDetail.getId());
		return wrapResult(authService.updateUserInfo(userInfo));
	}
	
	@RequestMapping(value = "/userinfo/get", method = RequestMethod.POST)
	public @ResponseBody JSONObject getUserInfo(@UserInfo UserDetail userDetail) throws Exception {
		return wrapResult(authService.getUserInfo(userDetail.getId()));
	}
	
	@RequestMapping(value = "/new/user", method = RequestMethod.POST)
	public @ResponseBody JSONObject newUser(@UserInfo UserDetail userDetail, AccountDto accountDto) throws Exception {
		return wrapResult(authService.newUserInfo(accountDto));
	}
	
	@RequestMapping(value = "/menu/get", method = RequestMethod.POST)
	public @ResponseBody JSONObject menuGet(@UserInfo UserDetail userDetail) throws Exception {
		return wrapResult(authService.menuGet(userDetail));
	}
	
//	@RequestMapping(value = "/menu/get2", method = RequestMethod.GET)
//	@NoAuth
//	public @ResponseBody JSONObject menuGet2() throws Exception {
//		UserDetail userDetail = new UserDetail(1, "1,2,3", 1, 1L, "管理员");
//		return wrapResult(authService.menuGet(userDetail));
//	}
	
	@RequestMapping(value = "/logon/check", method = RequestMethod.POST)
	@NoAuth
	public @ResponseBody JSONObject logonCheck(HttpServletRequest request) throws Exception {
		return wrapResult(authService.checkUserLogon(request));
	}
	
	@RequestMapping(value = "/yanzhenma/needcheck", method = RequestMethod.POST)
	@NoAuth
	public @ResponseBody JSONObject yanzhenmaNeedCheck(String shoujihm) throws Exception {
		return wrapResult(authService.checkNeedVcode(shoujihm));
	}
	
	private String getVcodeFromSession(HttpServletRequest request, String key) {
		Object obj = request.getSession().getAttribute(key);
		if (obj == null) {
			return null;
		} else {
			return obj.toString();
		}
//		String cookieValue = CookieUtil.getCookieValue(request, key);
//		
//		if (cookieValue == null) {
//			return null;
//		} else {
//			try {
//				cookieValue = new AES().aesDecrypt(cookieValue, null);
//			} catch (Exception e) {
//				logger.error("fail to parse cookie value=" + cookieValue);
//				return null;
//			}
//			return cookieValue;
//		}
	}
	
	private void saveSession(JSONObject rst, HttpSession session) {
		JSONObject userInfo = new JSONObject();
		userInfo.put(ChufangConsts.USER_ID, rst.getLong(ChufangConsts.USER_ID));
		userInfo.put(ChufangConsts.USER_ROLE, rst.getString(ChufangConsts.USER_ROLE));
		userInfo.put(ChufangConsts.ADMIN, rst.getInt(ChufangConsts.ADMIN));
		userInfo.put(ChufangConsts.YIGUAN_ID, rst.getLong(ChufangConsts.YIGUAN_ID));
		userInfo.put(ChufangConsts.LONGON_USER_NAME, rst.getString(ChufangConsts.LONGON_USER_NAME));
		userInfo.put(ChufangConsts.LONGON_USER_ACCOUNT, rst.getString(ChufangConsts.LONGON_USER_ACCOUNT));
		userInfo.put(ChufangConsts.YIGUAN_NAME, rst.getString(ChufangConsts.YIGUAN_NAME));
		session.setAttribute(ChufangConsts.USER_INFO_KEY, userInfo.toString());
		session.removeAttribute(ChufangConsts.VCODE_KEY);
		session.removeAttribute(ChufangConsts.VCODE_KEY2);
	}
	
	private String getRemoteAddrIp(HttpServletRequest request) {
   	 String ip = request.getHeader("x-forwarded-for");  
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }
        if (ip.indexOf(",") > 0) {
       	 ip = ip.split(",")[0];
        }
        return ip;  
   }
}
