package com.chufang.controller;

import org.springframework.stereotype.Component;

import com.chufang.consts.ChufangConsts;
import com.chufang.consts.ErrorCode;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Component
public class BaseController {
	protected JSONObject wrapResult(JSONObject obj) {
		try {
			JSONObject rst = new JSONObject();
			if (obj.getInt(ChufangConsts.RC) != ChufangConsts.OK) {
				obj.put(ChufangConsts.ERR_MSG,ErrorCode.errorMap.get(obj.getInt(ChufangConsts.RC)) == null ? 
						"" : ErrorCode.errorMap.get(obj.getInt(ChufangConsts.RC)));
				rst.put("success", false);
			} else {
				rst.put("success", true);
			}
			rst.put("result", obj);
			return rst;
		} catch (Exception e) {
		}
		return obj;
	}
}
