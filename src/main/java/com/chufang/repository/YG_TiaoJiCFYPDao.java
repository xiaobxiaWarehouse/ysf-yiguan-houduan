package com.chufang.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.chufang.entity.YG_TIAOJICFYP;
import com.chufang.parameter.Condition;

@Repository
public class YG_TiaoJiCFYPDao extends BaseRepository<YG_TIAOJICFYP>{
	public List<YG_TIAOJICFYP> getYaopinList(long tiaojicfId, long yiguanId) {
		Condition condition = new Condition();
		condition.setCondition("TIAOJICFID", tiaojicfId);
		condition.setCondition("YIGUANID", yiguanId);
		
		return queryObjByCondition(condition, YG_TIAOJICFYP.class);
	}
}
