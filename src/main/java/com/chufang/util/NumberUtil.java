package com.chufang.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumberUtil {

	private static DecimalFormat df = new DecimalFormat("0.##");
	
	private static DecimalFormat df2dot = new DecimalFormat("0.00");
	
	public static double convert2(double d) {
		  BigDecimal bg = new BigDecimal(d);
		  return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public static String round2(float data) {
		return df.format(data);
	}
	
	public static float convertStrToFloat(String money) {
		return Float.parseFloat(df.format(Float.parseFloat(money)+0.0001f));
	}
	
	public static double convertStrToDouble(String money) {
		return Double.parseDouble(df.format(Double.parseDouble(money)+0.0001d));
	}
	
	public static float convertStrToFloatDot2(String money) {
		return Float.parseFloat(df2dot.format(Float.parseFloat(money)+0.0001f));
	}
	
	public static String formatYuan(float money) {
		return df.format(money + 0.00001f);
	}
	
	public static String formatYuan(double money) {
		if (money == 0) {
			return "0";
		}
		return df.format(money + 0.00001f);
	}
	
	public static String formatYuan2dot(double money) {
		if (money == 0) {
			return "0";
		}
		return df2dot.format(money + 0.00001f);
	}
	
	public static int formatFen(String money) {
		return new Double((Double.parseDouble(money) + 0.005d) * 100).intValue();
	}
	
	public static int formatFen(double money) {
		return new Double((money + 0.005f)* 100).intValue();
	}
	
	public static int formatFen(float money) {
		return new Double((money + 0.005f) * 100).intValue();
	}
}
