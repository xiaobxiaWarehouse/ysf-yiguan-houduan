package com.chufang.repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.chufang.entity.PT_KEHUXX;
import com.chufang.enumtype.ConditionType;
import com.chufang.parameter.Condition;

@Repository
public class PT_KEHUXXDao extends BaseRepository<PT_KEHUXX> {

	public PT_KEHUXX getKehuXXByShouJi(String shouji) {
		Condition condition = new Condition();
		condition.setCondition("SHOUJIHM", shouji, ConditionType.EQ);
		return getSingleData(queryObjByCondition(condition,PT_KEHUXX.class));
	}
	
	public PT_KEHUXX getKehuxxById(long id) {
		Condition condition = new Condition();
		condition.setCondition("id", id, ConditionType.EQ);
		return getSingleData(queryObjByCondition(condition,PT_KEHUXX.class));
	}
	
	public void updateFailTimes(long id, int cuowucs) {
		Condition condition = new Condition();
		condition.setCondition("id", id, ConditionType.EQ);
		Map<String, Object> params = new HashMap<>();
		params.put("MIMACWCS", cuowucs);

		super.updateByCondition(condition, params, PT_KEHUXX.class);
	}
	
	public void updateOkLogon(long id, String ip, long denglucs, String password) {
		Condition condition = new Condition();
		condition.setCondition("id", id, ConditionType.EQ);
		Map<String, Object> params = new HashMap<>();
		params.put("MIMACWCS", 0);
		params.put("ZUIHOUDLIP", ip);
		params.put("DENGLUCS", denglucs);
		params.put("ZUIHOUDLSJ", new Date());
		if (StringUtils.isNotEmpty(password)) {
			params.put("MIMA", password);
		}
		super.updateByCondition(condition, params, PT_KEHUXX.class);
	}
	
	public long newKehuxx(PT_KEHUXX kehuxx, List<String> definedFields) {
		return super.insertObjectAndGetAutoIncreaseId(kehuxx, definedFields);
	}
	
	public int updateBaseInfoById(long id, String shouji, String xingming, int xingbie, String mima) {
		Condition condition = new Condition();
		condition.setCondition("id", id, ConditionType.EQ); 
		Map<String, Object> params = null;
		if (StringUtils.isEmpty(mima)) {
			params = getUpdateParam(new String[]{"SHOUJIHM","XINGMING","XINGBIE"}, 
					new Object[]{shouji, xingming, xingbie});
		}  else {
			params = getUpdateParam(new String[]{"SHOUJIHM","XINGMING","XINGBIE", "MIMA"}, 
						new Object[]{shouji, xingming, xingbie, mima});
		}
		
		return super.updateByCondition(condition, params, PT_KEHUXX.class);
	}
}
