package com.chufang.service;

import net.sf.json.JSONObject;

public interface OCRService {
	JSONObject processImgFile(String webImgFile);
	
	String accessToken();
}
