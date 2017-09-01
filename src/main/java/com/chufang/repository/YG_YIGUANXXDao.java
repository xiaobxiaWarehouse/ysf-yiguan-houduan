package com.chufang.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.chufang.entity.YG_YIGUANXX;
import com.chufang.enumtype.ConditionType;
import com.chufang.parameter.Condition;

@Repository
public class YG_YIGUANXXDao extends BaseRepository<YG_YIGUANXX> {
	public void saveYgBaseInfo(YG_YIGUANXX yiguanxx) {
		Condition condition = new Condition();
		condition.setCondition("id", yiguanxx.getId(), ConditionType.EQ);
		Map<String, Object> params = new HashMap<>();
		params.put("JIANCHENG", yiguanxx.getJiancheng());
		params.put("LIANXIREN", yiguanxx.getLianxiren());
		params.put("LIANXIDH", yiguanxx.getLianxidh());
		params.put("SHENGFEN", yiguanxx.getShengfen());
		params.put("CHENGSHI", yiguanxx.getChengshi());
		params.put("QUXIAN", yiguanxx.getQuxian());
		params.put("JIEDAODZ", yiguanxx.getJiedaodz());
		params.put("TANGJIMRFWSID",  yiguanxx.getTangjimrfwsid());
		params.put("GAOJIMRFWSID", yiguanxx.getGaojimrfwsid());
		params.put("ZHONGYAOPFKLJMRFWSID", yiguanxx.getZhongyaopfkljmrfwsid());
		super.updateByCondition(condition, params, YG_YIGUANXX.class);
	}
	
	public YG_YIGUANXX getYGXXById(long yiguanId) {
		Condition condition = new Condition();
		condition.setCondition("id", yiguanId, ConditionType.EQ);
		return getSingleData(super.queryObjByCondition(condition, YG_YIGUANXX.class));
	}
	
	public int updateYGXX(YG_YIGUANXX yiguanxx) {
		Condition condition = new Condition();
		condition.setCondition("id", yiguanxx.getId(), ConditionType.EQ);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("MINGCHENG", yiguanxx.getMingcheng());
		params.put("Jiancheng", yiguanxx.getJiancheng());
		params.put("Leixing", yiguanxx.getLeixing());
		params.put("Lianxiren", yiguanxx.getLianxiren());
		params.put("lianxidh", yiguanxx.getLianxidh());
		params.put("Shengfen", yiguanxx.getShengfen());
		params.put("Chengshi", yiguanxx.getChengshi());
		params.put("Quxian", yiguanxx.getQuxian());
		params.put("Jiedaodz", yiguanxx.getJiedaodz());
		params.put("Tangjimrfwsid", yiguanxx.getTangjimrfwsid() == 0 ? null : yiguanxx.getTangjimrfwsid());
		params.put("Gaojimrfwsid", yiguanxx.getGaojimrfwsid() == 0 ? null : yiguanxx.getGaojimrfwsid());
		params.put("Wanjimrfwsid", yiguanxx.getWanjimrfwsid() == 0 ? null : yiguanxx.getWanjimrfwsid());
		params.put("Sanjimrfwsid", yiguanxx.getSanjimrfwsid() == 0 ? null : yiguanxx.getSanjimrfwsid());
		params.put("Zhongyaopfkljmrfwsid", yiguanxx.getZhongyaopfkljmrfwsid() == 0 ? null : yiguanxx.getZhongyaopfkljmrfwsid());
		
		return super.updateByCondition(condition, params, YG_YIGUANXX.class);
	}
}
