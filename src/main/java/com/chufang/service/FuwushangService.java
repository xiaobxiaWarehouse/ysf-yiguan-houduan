package com.chufang.service;

import net.sf.json.JSONObject;

public interface FuwushangService {
	JSONObject queryFuwushangList(int pageNo, long yiguanId);
	
	JSONObject loadFuwushangById(long id, long yiguanId);
	
	JSONObject loadFuwushangById(long id);
	
	JSONObject getAllFuwushang(long yigaunId, boolean isAll);
	
	JSONObject getTradeFusushang(long yigaunId);
}
