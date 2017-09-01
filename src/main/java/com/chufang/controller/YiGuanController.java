package com.chufang.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chufang.annotation.UserInfo;
import com.chufang.entity.YG_YIGUANXX;
import com.chufang.entity.ext.UserDetail;
import com.chufang.service.YiGuanService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/yiguan")
public class YiGuanController extends BaseController {
	
	@Resource
	private YiGuanService yiguanService;
	
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	public @ResponseBody JSONObject query(@UserInfo UserDetail userDetail) throws Exception {
		return wrapResult(yiguanService.queryYGXXById(userDetail.getYiguanId()));
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody JSONObject save(@UserInfo UserDetail userDetail, YG_YIGUANXX yiguanxx) throws Exception {
		yiguanxx.setId(userDetail.getYiguanId());
		return wrapResult(yiguanService.updateYGXX(yiguanxx));
	}
}
