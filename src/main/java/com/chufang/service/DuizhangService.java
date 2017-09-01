package com.chufang.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public interface DuizhangService {
	JSONObject query(long yiguanId, String startDate, String endDate, int pageNo);
	
	JSONObject pageYaopin(long yiguanId, String kaishirq, String jiezhirq, long fuwushangId, int chufanglx, int pageNo);
	
	JSONObject querySummary(long yiguanId, long fuwushangId, String kaishirq, String jiezhirq);
	
	void duizhangExport(HttpServletRequest request, HttpServletResponse response, long yiguanId, String startDate, String endDate,
			long fuwushangId);
	
}
