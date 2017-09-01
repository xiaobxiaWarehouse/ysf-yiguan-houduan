package com.chufang.consts;

import java.util.HashMap;
import java.util.Map;

public class ErrorCode {
	public final static int FORCE_LOGOUT = -2003;
	public final static int INVAILD_ACCOUNT = -2004;
	public final static int NO_MATCH_DATA = -1000;
	public final static int INVALID_PARAM = -1001;
	public final static int WRONG_ACCT_OR_PWD = 100001;
	public final static int ACCOUNT_FREEZON = 100002;
	public final static int ACHIEVE_MAX_FAILS = 100003;
	public final static int VCODE_NOT_MACTH = 100004;
	public final static int VCODE_EXPIRE= 100005;
	public final static int NON_EXIST_ACCOUNT= 100006;
	public final static int MIMA_NOT_MATCH= 200001;
	public final static int DUP_ACCOUNT= 300001;
	public final static int WRONG_SHOUJI = 300002;
	public final static int SCAN_FAIL = 400001;
	public final static int STATUS_NOT_SYNC = 500001;
	public final static int YUYI_FENXI_FAIL = 600001;
	public final static int ACCOUNT_NOT_EXIST = 700001;
	public final static int ACCOUNT_EXISTS = 700002;
	public final static int NEW_PWD_IS_BLANK = 700003;
	public final static int OLD_PWD_IS_BLANK = 700004;
	public static Map<Integer,String> errorMap = new HashMap<Integer, String>();
	static {
		errorMap.put(INVAILD_ACCOUNT, "未知账号");
		errorMap.put(WRONG_ACCT_OR_PWD, "账号或密码错误");
		errorMap.put(NON_EXIST_ACCOUNT, "账号不存在");
		errorMap.put(NO_MATCH_DATA, "无匹配数据");
		errorMap.put(MIMA_NOT_MATCH, "密码不匹配");
		errorMap.put(ACCOUNT_FREEZON, "账号冻结");
		errorMap.put(ACHIEVE_MAX_FAILS, "达到最大失败登陆次数");
		errorMap.put(VCODE_NOT_MACTH, "验证码不匹配");
		errorMap.put(VCODE_EXPIRE, "验证码过期");
		errorMap.put(DUP_ACCOUNT, "账号已存在");
		errorMap.put(WRONG_SHOUJI, "错误手机号码");
		errorMap.put(SCAN_FAIL, "扫描失败");
		errorMap.put(INVALID_PARAM, "参数错误");
		errorMap.put(STATUS_NOT_SYNC, "数据不同步，请刷新后操作");
		errorMap.put(YUYI_FENXI_FAIL, "语义解析失败");
		errorMap.put(ACCOUNT_NOT_EXIST, "账号不存在");
		errorMap.put(ACCOUNT_EXISTS, "账号已存在");
		errorMap.put(NEW_PWD_IS_BLANK, "新密码为空");
		errorMap.put(OLD_PWD_IS_BLANK, "原始密码为空");
	}
}
