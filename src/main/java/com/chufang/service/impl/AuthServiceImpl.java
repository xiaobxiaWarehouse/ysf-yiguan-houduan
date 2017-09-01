package com.chufang.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chufang.annotation.Roles;
import com.chufang.consts.ChufangConsts;
import com.chufang.consts.ErrorCode;
import com.chufang.entity.PT_KEHUXX;
import com.chufang.entity.PT_YUANGONGXX;
import com.chufang.entity.YG_YIGUANXX;
import com.chufang.entity.YG_YUANGONGXX;
import com.chufang.entity.ext.AccountDto;
import com.chufang.entity.ext.UserDetail;
import com.chufang.parameter.UserInfoParam;
import com.chufang.repository.PT_KEHUXXDao;
import com.chufang.repository.PT_YUANGONGXXDao;
import com.chufang.repository.YG_YIGUANXXDao;
import com.chufang.repository.YG_YUANGONGXXDao;
import com.chufang.service.AuthService;
import com.chufang.service.HttpService;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class AuthServiceImpl extends CommonService implements AuthService {
	
	private final static Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
	private final static int SUPER_MANAGE_ROLE_ID = -1;
	
	@Resource
	private PT_KEHUXXDao kehuxxDao;
	
	@Resource
	private YG_YUANGONGXXDao ygyonghuDao;
	
	@Resource
	private PT_YUANGONGXXDao ptyuangongDao;
	
	@Resource
	private YG_YIGUANXXDao yiguanDao;
	
	@Resource
	private HttpService httpService;
	
	@Value("${max.fail.logontimes}")
	private int maxFailLogonTimes;
	
	@Value("${is.dev}")
	private boolean isDev;
	
	@Value("${identity.key}")
	private String identityKey;
	
	@Value("${apprukou}")
	private String appRuKou;
	
	@Value("${sms.host}")
	private String SMS_HOST;
	
	@Override
	public JSONObject authByPwd(String account, String pwd, String ip, int userType, String sessionVcode, String vcode) {
		JSONObject rst = generateRst();
		
		if (StringUtils.isNotEmpty(sessionVcode) && !sessionVcode.equalsIgnoreCase(vcode)) {
			rst.put(ChufangConsts.RC, ErrorCode.VCODE_NOT_MACTH);
			rst.put(ChufangConsts.NEED_VCODE, 1);
			return rst;
		}
		
		rst.put(ChufangConsts.NEED_VCODE, 0);
		PT_KEHUXX kehuxx = kehuxxDao.getKehuXXByShouJi(account);
		if (kehuxx == null) {
			rst.put(ChufangConsts.RC, ErrorCode.WRONG_ACCT_OR_PWD);
			return rst;
		} else {
			
			if (maxFailLogonTimes <= kehuxx.getMimacwcs()) { //超过限制了。但是没有vcode表示vcode过期
				if (StringUtils.isEmpty(vcode)) {
					rst.put(ChufangConsts.NEED_VCODE, 1);
					rst.put(ChufangConsts.RC, ErrorCode.VCODE_NOT_MACTH);
					return rst;
				} else if (StringUtils.isEmpty(sessionVcode)) {
					rst.put(ChufangConsts.NEED_VCODE, 1);
					rst.put(ChufangConsts.RC, ErrorCode.VCODE_NOT_MACTH);
					return rst;
				}
			}
			
			if (!kehuxx.getMima().equals(pwd)) {
				if (maxFailLogonTimes <= kehuxx.getMimacwcs()) {
					rst.put(ChufangConsts.NEED_VCODE, 1);
				}
				kehuxxDao.updateFailTimes(kehuxx.getId(), kehuxx.getMimacwcs() + 1);
				rst.put(ChufangConsts.RC, ErrorCode.WRONG_ACCT_OR_PWD);
				return rst;
			} else { //判断禁用标记
				 if (userType == 1) { //YG
					 YG_YUANGONGXX yonghuxx = ygyonghuDao.getYgYonghuByCustId(kehuxx.getId());
					 if (yonghuxx == null) {
						 rst.put(ChufangConsts.RC, ErrorCode.NON_EXIST_ACCOUNT);
						 return rst;
					 } else if (yonghuxx.getJinyongbz() == 1) {
						 rst.put(ChufangConsts.RC, ErrorCode.ACCOUNT_FREEZON);
					 } else {
						 YG_YIGUANXX yiguanXX = yiguanDao.getYGXXById(yonghuxx.getYiguanid());
						 rst.put(ChufangConsts.ADMIN, yonghuxx.getGuanliybz());
						 rst.put(ChufangConsts.USER_ROLE, yonghuxx.getQuanxian());
						 rst.put(ChufangConsts.YIGUAN_ID, yonghuxx.getYiguanid());
						 rst.put(ChufangConsts.LONGON_USER_NAME, yonghuxx.getXingming());
						 rst.put(ChufangConsts.USER_ID, kehuxx.getId());
						 rst.put(ChufangConsts.LONGON_USER_NAME, yonghuxx.getXingming());
						 rst.put(ChufangConsts.LONGON_USER_ACCOUNT, kehuxx.getShoujihm());
						 rst.put(ChufangConsts.YIGUAN_NAME, yiguanXX.getMingcheng());
					 }
				 } else {
					 PT_YUANGONGXX yonghuxx = ptyuangongDao.getYuangongxxBykehuId(kehuxx.getId());
					 if (yonghuxx == null || yonghuxx.getJinyongbz() == 1) {
						 rst.put(ChufangConsts.RC, ErrorCode.ACCOUNT_FREEZON);
						 return rst;
					 } else {
						 rst.put(ChufangConsts.ADMIN, 0);
						 rst.put(ChufangConsts.USER_ROLE, "0");
						 rst.put(ChufangConsts.YIGUAN_ID, 0);
						 rst.put(ChufangConsts.LONGON_USER_NAME, yonghuxx.getXingming());
						 rst.put(ChufangConsts.USER_ID, kehuxx.getId());
						 rst.put(ChufangConsts.LONGON_USER_NAME, yonghuxx.getXingming());
						 rst.put(ChufangConsts.LONGON_USER_ACCOUNT, kehuxx.getShoujihm());
						 rst.put(ChufangConsts.YIGUAN_NAME, "");
					 }
				 }
			}
		}
		kehuxxDao.updateOkLogon(kehuxx.getId(), ip, kehuxx.getDenglucs() + 1, null);
		return rst;
	}
	
	@Override
	public JSONObject authByVcode(String account, String ip,
			int userType, String sessionVcode, String vcode, String password) {
		JSONObject rst = generateRst();
		if (sessionVcode == null) {
			rst.put(ChufangConsts.RC, ErrorCode.VCODE_EXPIRE);
			return rst;
		}
		if (sessionVcode != null && !sessionVcode.equals(vcode)) {
			rst.put(ChufangConsts.RC, ErrorCode.VCODE_NOT_MACTH);
			return rst;
		}
		
		PT_KEHUXX kehuxx = kehuxxDao.getKehuXXByShouJi(account);
		if (kehuxx == null) {
			rst.put(ChufangConsts.RC, ErrorCode.ACCOUNT_NOT_EXIST);
			return rst;
		} else {
			 if (userType == 1) { //YG
				 YG_YUANGONGXX yonghuxx = ygyonghuDao.getYgYonghuByCustId(kehuxx.getId());
				 if (yonghuxx == null || yonghuxx.getJinyongbz() == 1) {
					 rst.put(ChufangConsts.RC, ErrorCode.ACCOUNT_FREEZON);
					 return rst;
				 } else {
					 YG_YIGUANXX yiguanXX = yiguanDao.getYGXXById(yonghuxx.getYiguanid());
					 rst.put(ChufangConsts.ADMIN, yonghuxx.getGuanliybz());
					 rst.put(ChufangConsts.USER_ROLE, yonghuxx.getQuanxian());
					 rst.put(ChufangConsts.YIGUAN_ID, yonghuxx.getYiguanid());
					 rst.put(ChufangConsts.LONGON_USER_NAME, yonghuxx.getXingming());
					 rst.put(ChufangConsts.USER_ID, kehuxx.getId());
					 rst.put(ChufangConsts.LONGON_USER_NAME, yonghuxx.getXingming());
					 rst.put(ChufangConsts.LONGON_USER_ACCOUNT, kehuxx.getShoujihm());
					 rst.put(ChufangConsts.YIGUAN_NAME, yiguanXX.getMingcheng());
				 }
			 } else {
				 PT_YUANGONGXX yonghuxx = ptyuangongDao.getYuangongxxBykehuId(kehuxx.getId());
				 if (yonghuxx == null || yonghuxx.getJinyongbz() == 1) {
					 rst.put(ChufangConsts.RC, ErrorCode.ACCOUNT_FREEZON);
					 return rst;
				 } else {
					 rst.put(ChufangConsts.ADMIN, 0);
					 rst.put(ChufangConsts.USER_ROLE, "0");
					 rst.put(ChufangConsts.YIGUAN_ID, 0);
					 rst.put(ChufangConsts.LONGON_USER_NAME, yonghuxx.getXingming());
					 rst.put(ChufangConsts.USER_ID, kehuxx.getId());
					 rst.put(ChufangConsts.LONGON_USER_NAME, yonghuxx.getXingming());
					 rst.put(ChufangConsts.LONGON_USER_ACCOUNT, kehuxx.getShoujihm());
					 rst.put(ChufangConsts.YIGUAN_NAME, "");
				 }
			 }
		}
		kehuxxDao.updateOkLogon(kehuxx.getId(), ip, kehuxx.getDenglucs() + 1, password);
		return rst;
	}
	
	@Override
	public boolean hasAccess(int roleId, Roles roles) {
		if (roleId == SUPER_MANAGE_ROLE_ID) {
			return true;
		}
		int[] roleIds = roles.value();
		for (int r : roleIds) {
			if (r == roleId) {
				return true;
			}
		}
		return false;
	}

	@Override
	public JSONObject sendVcode(String account, String vcode, boolean checkAccount) {
		JSONObject rst = generateRst();
		if (checkAccount) {
			PT_KEHUXX kehuxx = kehuxxDao.getKehuXXByShouJi(account);
			if (kehuxx == null) {
				rst.put(ChufangConsts.RC, ErrorCode.ACCOUNT_NOT_EXIST);
			}
		}
		if (!sendMsg(account, vcode)) {
			rst.put(ChufangConsts.RC, ChufangConsts.FAIL);
		}
		
		return rst;
	}
	
	public boolean sendMsg(String phone, String code) {
		try {
		String url = SMS_HOST + "?apprukou=" + appRuKou + "&identitykey=" + identityKey + 
				"&tongzhilx=1&mubiaosjhm=" + phone + "&tongzhinr=" + URLEncoder.encode("{\"code\":\"" + code + "\"}", "utf-8");
//		String url2= "https://ysfw.beekang.com/ysfapi/ysftongzhidl";
//		Map<String, Object> param = new HashMap<String, Object>();
//		param.put("apprukou", appRuKou);
//		param.put("identitykey", identityKey);
//		param.put("tongzhilx", "1");
//		param.put("mubiaosjhm", phone);
//		param.put("tongzhinr", "{\"code\",\"" + code + "\"}");
//		String rst = httpService.processRequestStrV2(url2, param);
		String rst = httpService.processGetRequestStr(url);
		if ("SUCCESS".equalsIgnoreCase(rst)) {
			return true;
		} else {
			logger.warn("fail to send msg .phone(" + phone + ").");
			return false;
		}
		} catch (Exception e) {
			logger.error("fail to send" ,e);
			return false;
		}
	}

	@Override
	public JSONObject updateUserInfo(UserInfoParam userInfo) {
		JSONObject rst = generateRst();
		
		if (StringUtils.isNotEmpty(userInfo.getShoujihm())) {
			if (StringUtils.isEmpty(userInfo.getVerifyYanzhengma()) || !userInfo.getVerifyYanzhengma().equals(userInfo.getYanzhengma())) {
				rst.put(ChufangConsts.RC, ErrorCode.VCODE_NOT_MACTH);
				return rst;
			}
		}
		if (StringUtils.isEmpty(userInfo.getMima()) && StringUtils.isNotEmpty(userInfo.getYuanmima())) {
			rst.put(ChufangConsts.RC, ErrorCode.NEW_PWD_IS_BLANK);
			return rst;
		}
		if (StringUtils.isNotEmpty(userInfo.getMima()) && StringUtils.isEmpty(userInfo.getYuanmima())) {
			rst.put(ChufangConsts.RC, ErrorCode.OLD_PWD_IS_BLANK);
			return rst;
		}
		PT_KEHUXX kehuxx = kehuxxDao.getKehuxxById(userInfo.getId());
		if (kehuxx == null) {
			rst.put(ChufangConsts.RC, ErrorCode.NO_MATCH_DATA);
			return rst;
		}
		if (StringUtils.isNotEmpty(userInfo.getYuanmima()) && !kehuxx.getMima().equals(userInfo.getYuanmima())) {
			rst.put(ChufangConsts.RC, ErrorCode.MIMA_NOT_MATCH);
			return rst;
		}
		String shoujihm = null;
		if (StringUtils.isNotEmpty(userInfo.getShoujihm())) {
			shoujihm = userInfo.getShoujihm();
		} else {
			shoujihm = "" + kehuxx.getShoujihm();
		}
		kehuxxDao.updateBaseInfoById(userInfo.getId(),shoujihm, userInfo.getXingming(), userInfo.getXingbie(), userInfo.getMima());
		//名字可能修改了。需要修改员工信息
		YG_YUANGONGXX ygYonghu = ygyonghuDao.getYgYonghuByCustId(userInfo.getId());
		ygYonghu.setXingming(userInfo.getXingming());
		ygyonghuDao.updateById(ygYonghu);
		return rst;
	}

	@Override
	public JSONObject getUserInfo(long id) {
		JSONObject rst = generateRst();
		PT_KEHUXX kehuxx = kehuxxDao.getKehuxxById(id);
		rst.put("shouji", kehuxx.getShoujihm());
		rst.put("xingming", kehuxx.getXingming());
		rst.put("xingbie", kehuxx.getXingbie());
		return rst;
	}

	@Override
	@Transactional
	public JSONObject newUserInfo(AccountDto account) {
		JSONObject rst = generateRst();
		
		PT_KEHUXX kehuxx = kehuxxDao.getKehuXXByShouJi(account.getShouji());
		if (kehuxx != null) {
			 rst.put(ChufangConsts.RC, ErrorCode.DUP_ACCOUNT);
			 return rst;
		}
		kehuxx = new PT_KEHUXX();
		try {
			kehuxx.setShoujihm(Long.parseLong(account.getShouji()));
		} catch (Exception e) {
			logger.error("fail to parse shouji=" + account.getShouji() + ")");
			rst.put(ChufangConsts.RC, ErrorCode.WRONG_SHOUJI);
			return rst;
		}
		kehuxx.setMima(DigestUtils.md5Hex(account.getShouji()));
		kehuxx.setMimacwcs(0);
		kehuxx.setXingbie(account.getXingbie());
		kehuxx.setXingming(account.getXingming());
		kehuxx.setUuid(UUID.randomUUID().toString());
		long kehuId = kehuxxDao.newKehuxx(kehuxx, null);
		//新建医馆员工
		YG_YUANGONGXX ygyonghuxx = new YG_YUANGONGXX();
		ygyonghuxx.setJinyongbz(account.getJingrongbz());
		ygyonghuxx.setKehuid(new Long(kehuId).intValue());
		ygyonghuxx.setGangwei(account.getJuese());
		ygyonghuxx.setQuanxian(account.getGangwei());
		ygyonghuxx.setXingming(account.getXingming());
		ygyonghuDao.newYgYonghu(ygyonghuxx);
		rst.put("id", kehuId);
		return rst;
	}

	@Override
	public PT_KEHUXX getKehuxxById(long id) {
		return kehuxxDao.getKehuxxById(id);
	}

	@Override
	public YG_YUANGONGXX getYgYuanGongXX(long kehuId) {
		return ygyonghuDao.getYgYonghuByCustId(kehuId);
	}
	
	@Override
	public JSONObject checkUserLogon(HttpServletRequest request) {
		JSONObject rst = new JSONObject();
		Object userInfo  = request.getSession().getAttribute(ChufangConsts.USER_INFO_KEY) ;
		if (userInfo == null) {
			rst.put("login", false);
		} else {
			rst.put("login", true);
			rst.put("userName", JSONObject.fromObject(userInfo.toString()).getString(ChufangConsts.LONGON_USER_NAME));
			rst.put("userAccount", JSONObject.fromObject(userInfo.toString()).getString(ChufangConsts.LONGON_USER_ACCOUNT));
			rst.put("yiguanName", JSONObject.fromObject(userInfo.toString()).getString(ChufangConsts.YIGUAN_NAME));

		}
		rst.put(ChufangConsts.RC, ChufangConsts.OK);
	
		return rst;
	}

	@Override
	public JSONObject menuGet(UserDetail userDetail) {
		JSONObject rst = generateRst();
		List<String> quanxianList = Arrays.asList(userDetail.getQuanxian().split(","));
		String quanxianFile = "";
		if (quanxianList.contains("1")) {
			quanxianFile = "all.txt";
		} else if (quanxianList.size() >= 2) { //两种角色。
			quanxianFile = "both.txt";
		} else if (quanxianList.get(0).equals("2")) {
			quanxianFile = "tiaoji.txt";
		} else if (quanxianList.get(0).equals("3")) {
			quanxianFile = "caiwu.txt";
		} else {
			return rst;
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("quanxian/" + quanxianFile)));
		StringBuffer content = new StringBuffer();
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				content.append(line);
			}
		} catch (Exception e) {
			logger.error("fail to read file " + quanxianFile);
		}
		rst.put("menu", JSONArray.fromObject(content.toString()));
		return rst;
	}
	
	@Override
	public JSONObject checkNeedVcode(String account) {
		JSONObject rst = generateRst();
		PT_KEHUXX kehu = kehuxxDao.getKehuXXByShouJi(account);
		if (kehu == null || kehu.getMimacwcs() >=3) {
			rst.put(ChufangConsts.NEED_VCODE, 1);
		} else {
			rst.put(ChufangConsts.NEED_VCODE, 0);
		}
		return rst;
	}

}
