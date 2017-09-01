package com.chufang.repository;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.chufang.parameter.Condition;

@Repository
public class HelloRepository<T> extends BaseRepository<T> {
	public long insert(T t) {
		return super.insertObjectAndGetAutoIncreaseId(t, null);
	}
	
	public int update(Condition condition, Map<String, Object> params, Class<T> cls) {
		return super.updateByCondition(condition, params, cls);
	}
	
	public List<T> query(Condition condition, Class<T> cls) {
		return super.queryObjByCondition(condition, cls);
	}
	
	public int delete(Condition condition, Class<T> cls) {
		return super.deleteByCondition(condition, cls);
	}
}
