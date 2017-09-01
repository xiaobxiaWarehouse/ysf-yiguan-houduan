package com.chufang.service;

import java.util.Map;

import net.sf.json.JSONObject;

public interface HttpService {
	JSONObject processRequest(String url, JSONObject data);
	
	String processRequestStr(String url, String data);

	JSONObject processRequestStr(String url, Map<String, Object> params);
	
	String processRequestStrV2(String url, Map<String, Object> params);
	
	JSONObject processRequestStr(String url, Map<String, Object> params, int timeOut);
	
	JSONObject processGetRequestStr(String url, Map<String, Object> params);
	
	public String urlencode(Map<String, ?> data);
	
	String processGetRequestStr(String url);
	
	String processGetRequestStr(String url, Map<String, String> header, Map<String, String> data);
	
}
