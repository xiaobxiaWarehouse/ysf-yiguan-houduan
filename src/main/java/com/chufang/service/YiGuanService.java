package com.chufang.service;

import com.chufang.entity.YG_YIGUANXX;

import net.sf.json.JSONObject;

public interface YiGuanService {
	JSONObject queryYGXXById(long yiguanid);
	
	JSONObject updateYGXX(YG_YIGUANXX yiguanxx);
}
