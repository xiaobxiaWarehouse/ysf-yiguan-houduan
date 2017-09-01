package com.chufang.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.chufang.consts.ChufangConsts;
import com.chufang.consts.ErrorCode;
import com.chufang.entity.YG_YIGUANXX;
import com.chufang.repository.YG_YIGUANXXDao;
import com.chufang.service.YiGuanService;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Service
public class YiGuanServiceImpl extends CommonService implements YiGuanService {

	@Resource
	private YG_YIGUANXXDao yiguanxxDao;
	
	@Override
	public JSONObject queryYGXXById(long yiguanid) {
		JSONObject rst = generateRst();
		YG_YIGUANXX yiguanxx = yiguanxxDao.getYGXXById(yiguanid);
		if (yiguanxx == null) {
			rst.put(ChufangConsts.RC, ErrorCode.NO_MATCH_DATA);
			return rst;
		}
//		rst.put("mingcheng", yiguanxx.getMingcheng());
//		rst.put("jiancheng", yiguanxx.getJiancheng());
//		rst.put("leixing", yiguanxx.getLeixing());
//		rst.put("lianxiren", yiguanxx.getLianxiren());
//		rst.put("lianxidh", yiguanxx.getLianxidh());
//		rst.put("shengfen", yiguanxx.getShengfen());
//		rst.put("chengshi", yiguanxx.getChengshi());
//		rst.put("quxian", yiguanxx.getQuxian());
//		rst.put("jiedaodz", yiguanxx.getJiedaodz());
//		rst.put("tangjimrfwsid", yiguanxx.getTangjimrfwsid());
//		rst.put("gaojifwsid", yiguanxx.getGaojimrfwsid());
//		rst.put("wanjifwsid", yiguanxx.getWanjimrfwsid());
//		rst.put("sanjifwsid", yiguanxx.getSanjimrfwsid());
//		rst.put("zhongyaopfwsid", yiguanxx.getZhongyaopfkljmrfwsid());
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[]{"chuangjiansj","id","chuangjianren","chufangyymbid","zhucesj"});
		rst.putAll(JSONObject.fromObject(yiguanxx, config));
		return rst;
	}

	@Override
	public JSONObject updateYGXX(YG_YIGUANXX yiguanxx) {
		JSONObject rst = generateRst();
		yiguanxxDao.updateYGXX(yiguanxx);
		return rst;
	}

}
