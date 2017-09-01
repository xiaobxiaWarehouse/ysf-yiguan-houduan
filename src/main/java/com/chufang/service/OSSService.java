package com.chufang.service;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

public interface OSSService {
	JSONObject upload(HttpServletRequest request);
	
	String getPrivateAccessURL(String objectKey);
}
