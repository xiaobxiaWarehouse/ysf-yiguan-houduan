package com.chufang.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chufang.parameter.ChufangParam;
import com.chufang.parameter.TiaoJiSearchParam;

import net.sf.json.JSONObject;

public interface YG_TiaoJiCFService {

	JSONObject queryTiaoJiCf(TiaoJiSearchParam searchParam, long yiguanId);
	
	void export(HttpServletRequest request, HttpServletResponse response,TiaoJiSearchParam searchParam, long yiguanId);
	
	JSONObject queryStatics(int day, long yiguanId);

	JSONObject saveTiaojiCf(ChufangParam chufang, long userId, long yiguanId);
	
	JSONObject updateTiaojiCFStatus(long tiaojicfId, int status, long userId, long yiguanId);
	
	JSONObject getDetailById(long tiaojicfId, long yiguanId);
	
	JSONObject getDianziCf(long tiaojicfId, long yiguanId);
	
	JSONObject queryLatestAddress(String shouji, String xingming);
	
	JSONObject getSaveToken(HttpServletRequest request);
	
	JSONObject getYongyaopl();
	
	JSONObject getYongfa();
}
