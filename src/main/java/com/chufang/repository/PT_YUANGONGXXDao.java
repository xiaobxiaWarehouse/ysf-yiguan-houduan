package com.chufang.repository;

import org.springframework.stereotype.Repository;

import com.chufang.entity.PT_YUANGONGXX;
import com.chufang.enumtype.ConditionType;
import com.chufang.parameter.Condition;

@Repository
public class PT_YUANGONGXXDao extends BaseRepository<PT_YUANGONGXX> {
	public PT_YUANGONGXX getYuangongxxBykehuId(long kehuId) {
		Condition condition = new Condition();
		condition.setCondition("KEHUID", kehuId, ConditionType.EQ);
		return getSingleData(queryObjByCondition(condition, PT_YUANGONGXX.class));
	}
}
