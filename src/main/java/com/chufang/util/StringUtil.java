package com.chufang.util;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class StringUtil {

	private final static String BASE = "abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnop"
			+ "qrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789";

	private final static String BASE_NUM = "012345678901234567890123456789012345678901234567890123456789";

	public static String getRandomString(int length) { // length表示生成字符串的长度
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(BASE.length());
			sb.append(BASE.charAt(number));
		}
		return sb.toString();
	}

	public static String getRandomNumString(int length) { // length表示生成字符串的长度
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(BASE_NUM.length());
			sb.append(BASE_NUM.charAt(number));
		}
		return sb.toString();
	}
	
	public static String getShareCode(String pre) {
		return pre + UUID.randomUUID().toString().replaceAll("-", "") + StringUtil.getRandomNumString(6);
	}
	
	public static String getTakeOrderNo() {
		return DateUtil.dt14FromDate(new Date()) + getRandomNumString(8);
	}
	
	public static String processNullStr(String str) {
		return str == null ? "" : str;
	}
	
	public static void main(String[] arg) {
		System.out.println(getRandomString(32));
	}
}
