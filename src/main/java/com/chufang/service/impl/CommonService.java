package com.chufang.service.impl;
import org.springframework.stereotype.Component;

import com.chufang.consts.ChufangConsts;

import net.sf.json.JSONObject;

@Component
public class CommonService {
	public JSONObject generateRst() {
		JSONObject rst = new JSONObject();
		rst.put(ChufangConsts.RC, ChufangConsts.OK);
		return rst;
	}
	
	public JSONObject generatePage(int currentPage, int totalCount, int totalPage) {
		JSONObject rst = new JSONObject();
		rst.put("currentPage", currentPage);
		rst.put("totalCount", totalCount);
		rst.put("totalPage", totalPage);
		return rst;
	}
}
