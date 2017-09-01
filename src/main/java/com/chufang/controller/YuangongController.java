package com.chufang.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chufang.annotation.UserInfo;
import com.chufang.consts.ChufangConsts;
import com.chufang.entity.ext.UserDetail;
import com.chufang.parameter.YuangongParam;
import com.chufang.service.YuangongService;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/yuangong")
public class YuangongController extends BaseController {
	
	@Resource
	private YuangongService yuangongService;
	
	
	
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	public @ResponseBody JSONObject query(String shoujihm, int zhuangtai, int pageNo, 
			@UserInfo UserDetail userDetail) throws Exception {
		return wrapResult(yuangongService.query(userDetail.getYiguanId(), shoujihm, zhuangtai, pageNo));
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody JSONObject save(HttpServletRequest request, YuangongParam yuangong, @UserInfo UserDetail userDetail) throws Exception {
		JSONObject rst =  wrapResult(yuangongService.save(userDetail.getYiguanId(), yuangong));
		if (userDetail.getId() == yuangong.getYuangongid()) { //修改自己信息。
			Object userInfo = request.getSession().getAttribute(ChufangConsts.USER_INFO_KEY);
			if (userInfo != null) {
				JSONObject tmpUserInfo = JSONObject.fromObject(userInfo.toString());
				tmpUserInfo.put(ChufangConsts.USER_ROLE, yuangong.getQuanxian());
				tmpUserInfo.put(ChufangConsts.ADMIN, yuangong.getQuanxian().equals("管理员") ? 1 : 0);
				tmpUserInfo.put(ChufangConsts.LONGON_USER_NAME,  yuangong.getXingming());
				request.getSession().setAttribute(ChufangConsts.USER_INFO_KEY, tmpUserInfo.toString());
			}
		}
		return rst;
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.POST)
	public @ResponseBody JSONObject getById(@PathVariable("id") int yuangongId, @UserInfo UserDetail userDetail) throws Exception {
		return wrapResult(yuangongService.getById(userDetail.getYiguanId(), yuangongId));
	}
	
	@RequestMapping(value = "/update/zhuangtai/{id}", method = RequestMethod.POST)
	public @ResponseBody JSONObject updateZhuangtai(@PathVariable("id") int yuangongId, 
			int zhuangtai,
			@UserInfo UserDetail userDetail) throws Exception {
		return wrapResult(yuangongService.updateZhuangTai(userDetail.getYiguanId(), yuangongId, zhuangtai));
	}
	
	@RequestMapping(value = "/getself", method = RequestMethod.POST)
	public @ResponseBody JSONObject getself(@UserInfo UserDetail userDetail) throws Exception {
		return wrapResult(yuangongService.getSelfXinxi(userDetail.getId()));
	}
	
	@RequestMapping(value = "/kaifangys/get", method = RequestMethod.POST)
	public @ResponseBody JSONObject kaifangYSGet(@UserInfo UserDetail userDetail, int isall) throws Exception {
		return wrapResult(yuangongService.getKaifangys(userDetail.getYiguanId(), isall));
	}
}
