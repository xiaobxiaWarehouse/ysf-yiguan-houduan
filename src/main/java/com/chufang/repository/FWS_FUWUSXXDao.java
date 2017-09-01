package com.chufang.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.chufang.entity.FWS_FUWUSXX;
import com.chufang.entity.Page;
import com.chufang.entity.ext.FuwushangxxExt;
import com.chufang.enumtype.ConditionType;
import com.chufang.parameter.Condition;
import com.chufang.rowmaper.CustomerMapper;

@Repository
public class FWS_FUWUSXXDao extends BaseRepository<FWS_FUWUSXX> {
	public void queryFws(Page<FuwushangxxExt> page, long yiguanId) {
		String sql = "select a.id, a.MINGCHENG as fuwushangmc,qianyue.YEWUYXM as yewuyuan, qianyue.YEWUYDH as lianxidh,"
				+ " concat(a.SHENGFEN,a.CHENGSHI,ifnull(a.QUXIAN,''),a.JIEDAODZ) as xiangxidz, a.TIAOJIFW as tiaojifw,"
				+ " qianyue.FUWUKQBZ as fuwuzt from " + Tables.TBL_FWS_FUWUSXX + " a, "
			    + Tables.TBL_FWS_QIANYUEXX + " qianyue"
//				+ " where a.id = qianyue.FUWUSID and qianyue.yiguanid = ? and a.JINYONGBZ = 0 and qianyue.FUWUKQBZ = 1 order by a.id desc";
			    + " where a.id = qianyue.FUWUSID and qianyue.yiguanid = ? order by a.id desc";
		super.query(sql, new Object[]{yiguanId}, page, new CustomerMapper<FuwushangxxExt>(FuwushangxxExt.class));
	}
	
	public FuwushangxxExt loadFwsExtById(long id, long yiguanId) {
		String sql = "select a.id, a.MINGCHENG as fuwushangmc,qianyue.YEWUYXM as yewuyuan, qianyue.YEWUYDH as lianxidh,"
				+ " concat(a.SHENGFEN,a.CHENGSHI,ifnull(a.QUXIAN,''),a.JIEDAODZ) as xiangxidz, a.TIAOJIFW as tiaojifw,"
				+ " qianyue.FUWUKQBZ as fuwuzt,a.SHENGFEN, a.chengshi,a.quxian,a.JIEDAODZ,qianyue.QIANYUESJ as hezuorq from " + Tables.TBL_FWS_FUWUSXX + " a, "
				+ Tables.TBL_FWS_QIANYUEXX + " qianyue"
				+ " where a.id = qianyue.FUWUSID and a.id = ? and qianyue.yiguanId = ?";
		return getSingleData(jdbcTemplate.query(sql, new Object[]{id, yiguanId}, new CustomerMapper<FuwushangxxExt>(FuwushangxxExt.class)));
	}
	
	public FWS_FUWUSXX loadFwsById(long id) {
		Condition condition = new Condition();
		condition.setCondition("id", id, ConditionType.EQ);
		return getSingleData(queryObjByCondition(condition, FWS_FUWUSXX.class));
	}
	
	public List<Map<String,Object>> loadAll(long yiguanId) {
		String sql = "select fws.id, fws.mingcheng,qy.FUWUKQBZ as fuwukqbz,fws.JINYONGBZ as jinyongbz from " + Tables.TBL_FWS_FUWUSXX + " fws, "
				+ "" + Tables.TBL_FWS_QIANYUEXX + " qy where fws.id = qy.FUWUSID and qy.YIGUANID = ? and qy.FUWUKQBZ = 1 and fws.JINYONGBZ = 0";
		return this.jdbcTemplate.queryForList(sql, new Object[]{yiguanId});
		
	}
	
	public List<Map<String,Object>> loadAllV2(long yiguanId) {
		String sql = "select fws.id, fws.mingcheng, qy.FUWUKQBZ as fuwukqbz,fws.JINYONGBZ as jinyongbz from " + Tables.TBL_FWS_FUWUSXX + " fws, "
				+ "" + Tables.TBL_FWS_QIANYUEXX + " qy where fws.id = qy.FUWUSID and qy.YIGUANID = ?";
		return this.jdbcTemplate.queryForList(sql, new Object[]{yiguanId});
		
	}
	
	public List<Map<String,Object>> loadAllV3(long yiguanId) {
		String sql = "select id, mingcheng from " + Tables.TBL_FWS_FUWUSXX + " where id in ("
				+ "select FUWUSID from " + Tables.TBL_YG_TIAOJICF + " where YIGUANID = ?)";
		return this.jdbcTemplate.queryForList(sql, new Object[]{yiguanId});
		
	}
}
