package com.chufang.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chufang.annotation.NoAuth;
import com.chufang.annotation.UserInfo;
import com.chufang.entity.ext.UserDetail;
import com.chufang.service.DuizhangService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/duizhang")
public class DuizhangController extends BaseController {
	
	@Resource
	private DuizhangService duizhangService;
	
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	public @ResponseBody JSONObject query(String startDate, String endDate, int pageNo, @UserInfo UserDetail userDetail) throws Exception {
		return wrapResult(duizhangService.query(userDetail.getYiguanId(), startDate, endDate, pageNo));
	}
	
	@RequestMapping(value = "/detail/{fuwushangid}", method = RequestMethod.POST)
	public @ResponseBody JSONObject detail(@PathVariable("fuwushangid") long fuwushangId,
			String kaishirq, String jiezhirq, @UserInfo UserDetail userDetail) throws Exception {
		return wrapResult(duizhangService.querySummary(userDetail.getYiguanId(), fuwushangId, kaishirq, jiezhirq));
	}

	@RequestMapping(value = "/page/yaopin/{fuwushangid}", method = RequestMethod.POST)
	public @ResponseBody JSONObject yaopinList(@PathVariable("fuwushangid") long fuwushangId,
			String kaishirq, String jiezhirq, int chufanglx, int pageNo, @UserInfo UserDetail userDetail) throws Exception {
		return wrapResult(duizhangService.pageYaopin(userDetail.getYiguanId(), kaishirq, jiezhirq, fuwushangId, chufanglx, pageNo));
	}
	
	@RequestMapping(value = "/export", method = RequestMethod.POST)
	public void export(HttpServletRequest request, HttpServletResponse response, long fuwushangid, String kaishirq, String jiezhirq, @UserInfo UserDetail userDetail) {
		
		duizhangService.duizhangExport(request, response, userDetail.getYiguanId(), kaishirq,jiezhirq, fuwushangid);
	}
}
