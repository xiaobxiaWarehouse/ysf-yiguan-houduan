package com.chufang.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chufang.annotation.UserInfo;
import com.chufang.entity.ext.UserDetail;
import com.chufang.service.FuwushangService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/fuwushang")
public class FuwushangController extends BaseController {
	
	@Resource
	private FuwushangService fuwushangService;
	
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	public @ResponseBody JSONObject query(int pageNo, @UserInfo UserDetail userDetail) throws Exception {
		return wrapResult(fuwushangService.queryFuwushangList(pageNo, userDetail.getYiguanId()));
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.POST)
	public @ResponseBody JSONObject getById(@PathVariable("id") int id, @UserInfo UserDetail userDetail) throws Exception {
		return wrapResult(fuwushangService.loadFuwushangById(id, userDetail.getYiguanId()));
	}
	
	@RequestMapping(value = "/all/get", method = RequestMethod.POST)
	public @ResponseBody JSONObject getAll(@UserInfo UserDetail userDetail) throws Exception {
		return wrapResult(fuwushangService.getAllFuwushang(userDetail.getYiguanId(), false));
	}
	
	@RequestMapping(value = "/all/get/v2", method = RequestMethod.POST)
	public @ResponseBody JSONObject getAllV2(@UserInfo UserDetail userDetail) throws Exception {
		return wrapResult(fuwushangService.getAllFuwushang(userDetail.getYiguanId(), true));
	}
	
	@RequestMapping(value = "/all/get/v3", method = RequestMethod.POST)
	public @ResponseBody JSONObject getAllV3(@UserInfo UserDetail userDetail) throws Exception {
		return wrapResult(fuwushangService.getTradeFusushang(userDetail.getYiguanId()));
	}
	
}
