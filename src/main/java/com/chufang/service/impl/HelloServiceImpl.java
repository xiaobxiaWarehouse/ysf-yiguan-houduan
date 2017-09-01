package com.chufang.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.chufang.entity.Test;
import com.chufang.enumtype.ConditionType;
import com.chufang.parameter.Condition;
import com.chufang.repository.HelloRepository;
import com.chufang.service.HelloService;

import net.sf.json.JSONObject;

@Service
public class HelloServiceImpl extends CommonService implements HelloService {

	@Resource
	private HelloRepository<Test> helloDao;
	
	@Override
	public JSONObject insert(Test t) {
		JSONObject rst = generateRst();
		rst.put("id", helloDao.insert(t));
		return rst;
	}

	@Override
	public JSONObject query(String name) {
		JSONObject rst = generateRst();
		Condition condition = new Condition();
		condition.setCondition("name", name, ConditionType.EQ);
		rst.put("result", helloDao.query(condition, Test.class));
		return rst;
	}

	@Override
	public JSONObject update(long id, String name) {
		JSONObject rst = generateRst();
		Condition condition = new Condition();
//		condition.setAndCondition("id", id, ConditionType.EQ);
//		condition.setAndCondition("birth", 12, ConditionType.EQ);
		condition.setCondition("id", 3, ConditionType.LT);
		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		rst.put("updateCnt", helloDao.update(condition, params, Test.class));
		return rst;
	}

}
