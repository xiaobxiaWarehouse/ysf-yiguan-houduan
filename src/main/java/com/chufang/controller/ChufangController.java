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
import com.chufang.consts.ChufangConsts;
import com.chufang.entity.ext.UserDetail;
import com.chufang.parameter.ChufangParam;
import com.chufang.parameter.TiaoJiSearchParam;
import com.chufang.service.YG_TiaoJiCFService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/tiaoji")
public class ChufangController extends BaseController {
	
	@Resource
	private YG_TiaoJiCFService tiaojicfService;
	
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	public @ResponseBody JSONObject query(TiaoJiSearchParam searchParam, @UserInfo UserDetail uDetail) throws Exception {
		return wrapResult(tiaojicfService.queryTiaoJiCf(searchParam, uDetail.getYiguanId()));
	}
	
	@RequestMapping(value = "/export", method = RequestMethod.POST)
	public void export(HttpServletRequest request, HttpServletResponse response, TiaoJiSearchParam searchParam, @UserInfo UserDetail uDetail) throws Exception {
		tiaojicfService.export(request, response, searchParam, uDetail.getYiguanId());
//		tiaojicfService.export(request, response, searchParam, 1);
	}
	
	@RequestMapping(value = "/zuijin/tongji", method = RequestMethod.POST)
	public @ResponseBody JSONObject statics7(@UserInfo UserDetail uDetail) throws Exception {
		return wrapResult(tiaojicfService.queryStatics(7, uDetail.getYiguanId()));
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody JSONObject saveTiaoJi(ChufangParam chufang, @UserInfo UserDetail uDetail) throws Exception {
		return wrapResult(tiaojicfService.saveTiaojiCf(chufang, uDetail.getId(), uDetail.getYiguanId()));
	}
	
	@RequestMapping(value = "/get/{tiaojicfid}", method = RequestMethod.POST)
	public @ResponseBody JSONObject getTiaojicf(@PathVariable("tiaojicfid") long tiaojicfId, 
			@UserInfo UserDetail uDetail) throws Exception {
		return wrapResult(tiaojicfService.getDetailById(tiaojicfId, uDetail.getYiguanId()));
	}
	
	@RequestMapping(value = "/dianzicf/{tiaojicfid}", method = RequestMethod.POST)
	public @ResponseBody JSONObject getDianzicf(@PathVariable("tiaojicfid") long tiaojicfId, 
			@UserInfo UserDetail uDetail) throws Exception {
		return wrapResult(tiaojicfService.getDianziCf(tiaojicfId, uDetail.getYiguanId()));
	}
	
	@RequestMapping(value = "/zhuangtai/update", method = RequestMethod.POST)
	public @ResponseBody JSONObject zhuangtaiUpdate(long tiaojicfid, int status, @UserInfo UserDetail uDetail) {
		return wrapResult(tiaojicfService.updateTiaojiCFStatus(tiaojicfid, status, uDetail.getId(), uDetail.getYiguanId()));
	}
	
	@RequestMapping(value = "/token/get", method = RequestMethod.POST)
	public @ResponseBody JSONObject saveTokenGet(HttpServletRequest request, @UserInfo UserDetail uDetail) {
		
		return wrapResult(tiaojicfService.getSaveToken(request));
	}
	
	@RequestMapping(value = "/pinlv/get", method = RequestMethod.POST)
	public @ResponseBody JSONObject yaongyaopinlvGet(HttpServletRequest request, @UserInfo UserDetail uDetail) {
		
		return wrapResult(tiaojicfService.getYongyaopl());
	}
	
	@RequestMapping(value = "/yongfa/get", method = RequestMethod.POST)
	public @ResponseBody JSONObject yongfaGet(HttpServletRequest request, @UserInfo UserDetail uDetail) {
		
		return wrapResult(tiaojicfService.getYongfa());
	}
}
