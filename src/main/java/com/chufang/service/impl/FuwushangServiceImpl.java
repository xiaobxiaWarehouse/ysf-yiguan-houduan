package com.chufang.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.chufang.consts.ChufangConsts;
import com.chufang.consts.ErrorCode;
import com.chufang.entity.FWS_FUWUSXX;
import com.chufang.entity.Page;
import com.chufang.entity.ext.FuwushangxxExt;
import com.chufang.repository.FWS_FUWUSXXDao;
import com.chufang.service.FuwushangService;
import com.chufang.util.DateUtil;
import com.chufang.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Service
public class FuwushangServiceImpl extends CommonService implements FuwushangService {

	@Resource
	private FWS_FUWUSXXDao fuwushangDao;
	
	@Override
	public JSONObject queryFuwushangList(int pageNo, long yiguanId) {
		JSONObject rst = generateRst();
		JSONArray tmpArr = new JSONArray();
		Page<FuwushangxxExt> page = new Page<FuwushangxxExt>();
		page.setPageNo(pageNo);
		fuwushangDao.queryFws(page, yiguanId);
		List<FuwushangxxExt> dataList = page.getData();
		
		for (FuwushangxxExt fws : dataList) {
			tmpArr.add(wrapFuwushang(fws));
		}
//		rst.put("page", generatePage(page.getPageNo(), page.getTotalCount(), page.getTotalPage()));
		rst.put("datalist", tmpArr);
		return rst;
	}
	
	private JSONObject wrapFuwushang(FuwushangxxExt fuwushang) {
		JSONObject rst = new JSONObject();
		rst.put("id", fuwushang.getId());
		rst.put("fuwushangmc", fuwushang.getFuwushangmc());
		rst.put("yewuyuan", fuwushang.getYewuyuan());
		rst.put("lianxifs", fuwushang.getLianxidh());
		rst.put("xiangxidz", fuwushang.getXiangxidz());
		rst.put("tiaojifw", fuwushang.getTiaojifw());
		rst.put("zhuangtai", fuwushang.getFuwuzt());
		rst.put("hezuorq", fuwushang.getHezuorq() == null ? "" : DateUtil.dt10FromDate(fuwushang.getHezuorq()));
		return rst;
	}

	@Override
	public JSONObject loadFuwushangById(long id, long yiguanId) {
		JSONObject rst = generateRst();
		FuwushangxxExt fuwushang = fuwushangDao.loadFwsExtById(id, yiguanId);
		if (fuwushang != null) {
			rst.putAll(wrapFuwushang(fuwushang));
			rst.put("shengfen", StringUtil.processNullStr(fuwushang.getShengfen()));
			rst.put("chengshi", StringUtil.processNullStr(fuwushang.getChengshi()));
			rst.put("quxian", StringUtil.processNullStr(fuwushang.getQuxian()));
		} else {
			rst.put(ChufangConsts.RC, ErrorCode.NO_MATCH_DATA);
		}
		return rst;
	}
	
	@Override
	public JSONObject loadFuwushangById(long id) {
		JSONObject rst = generateRst();
		FWS_FUWUSXX fuwushang = fuwushangDao.loadFwsById(id);
		if (fuwushang != null) {
			rst.putAll(JSONObject.fromObject(fuwushang));
		} else {
			rst.put(ChufangConsts.RC, ErrorCode.NO_MATCH_DATA);
		}
		return rst;
	}

	@Override
	public JSONObject getAllFuwushang(long yiguanId, boolean isAll) {
		JSONObject rst = generateRst();
		if (isAll) {
			rst.put("data", fuwushangDao.loadAllV2(yiguanId));
		} else {
			rst.put("data", fuwushangDao.loadAll(yiguanId));
		}
		return rst;
	}
	
	@Override
	public JSONObject getTradeFusushang(long yiguanId) {
		JSONObject rst = generateRst();
		rst.put("data", fuwushangDao.loadAllV3(yiguanId));
		return rst;
	}

}
