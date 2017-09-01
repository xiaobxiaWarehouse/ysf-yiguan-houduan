package com.chufang.service;

import com.chufang.parameter.YuangongParam;

import net.sf.json.JSONObject;

public interface YuangongService {
	JSONObject query(long yiguanId, String shouji, int zhuangtai, int pageNo);
	JSONObject save(long yiguanId, YuangongParam yuangong);
	JSONObject updateZhuangTai(long yiguanId, int id, int zhuangtai);
	JSONObject getById(long yiguanId, long id);
	JSONObject getSelfXinxi(long id);
	JSONObject getKaifangys(long yiguanId, int isall);
}
