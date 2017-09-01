package com.chufang.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.chufang.entity.Page;
import com.chufang.entity.YG_TIAOJICF;
import com.chufang.entity.ext.StatusSummary;
import com.chufang.enumtype.ConditionType;
import com.chufang.enumtype.Status;
import com.chufang.parameter.Condition;
import com.chufang.rowmaper.CustomerMapper;
import com.chufang.util.DateUtil;

@Repository
public class YG_TiaoJiCFDao extends BaseRepository<YG_TIAOJICF> {

	public void pageByYGTiaoJiCf(String kaishirq, String jiezhirq, long fuwushangId, 
			int status, String chufangbh, String phone, int isAll, Page<YG_TIAOJICF> pager, long yiguanId) {
		StringBuilder sql = new StringBuilder("select a.*,(select MINGCHENG from "
			+ Tables.TBL_FWS_FUWUSXX + " where id = a.FUWUSID) as fuwusmc from " + Tables.TBL_YG_TIAOJICF).append(" a where YIGUANID = ? ");
		List<Object> params = new ArrayList<>();
		params.add(yiguanId);
		if (StringUtils.isNotEmpty(kaishirq)) {
			sql.append(" and CHUANGJIANSJ >= ?");
			params.add(DateUtil.getBeginDateTime(DateUtil.dt10FromStr(kaishirq)));
		} 
		if (StringUtils.isNotEmpty(jiezhirq)) {
			sql.append(" and CHUANGJIANSJ <= ?");
			params.add(DateUtil.getEndDateTime(DateUtil.dt10FromStr(jiezhirq)));
		}
		if (StringUtils.isNotEmpty(chufangbh)) {
			sql.append(" and CHUFANGBH like ?");
			params.add("%" + chufangbh + "%");
		}
		if (fuwushangId > 0) {
			sql.append(" and FUWUSID = ?");
			params.add(fuwushangId);
		}
		
		if (StringUtils.isNotEmpty(phone)) {
			sql.append(" and (SHOUHUOREN like ? or SHOUHUOLXDH like ?)");
			params.add("%" + phone + "%");
			params.add("%" + phone + "%");
		}
		
		if (status > 0) {
			sql.append(" and zhuangtai = ? ");
			params.add(status);
		} else {
			if (isAll == 0) {
				sql.append(" and zhuangtai in (?,?,?,?,?) ");
				params.add(Status.SAVING.getValue());
				params.add(Status.SUBMITED.getValue());
				params.add(Status.WAITING_RECEIVE.getValue());
				params.add(Status.ADJUST.getValue());
				params.add(Status.RETUNED.getValue());
			}
			sql.append(" order by CHUANGJIANSJ desc");
		}
		
		super.query(sql.toString(), params.toArray(), pager, new CustomerMapper<YG_TIAOJICF>(YG_TIAOJICF.class));
	}
	
	public int getStatusCnt(long yiguanId, String kaishirq, String jiezhirq, int status) {
		StringBuilder sql = new StringBuilder("select count(1) from " + Tables.TBL_YG_TIAOJICF + " where YIGUANID = ? ");
		List<Object> params = new ArrayList<>();
		params.add(yiguanId);
		if (status == Status.SAVING.getValue()) {
			wrapSJCondition(sql, "CHUANGJIANSJ", kaishirq, jiezhirq, params);
		} else if (status == Status.SUBMITED.getValue()) {
			wrapSJCondition(sql, "TIJIAOSJ", kaishirq, jiezhirq, params);
		} else if (status == Status.WAITING_RECEIVE.getValue()) {
			wrapSJCondition(sql, "SHENHESJ", kaishirq, jiezhirq, params);
		} else if (status == Status.ADJUST.getValue()) {
			wrapSJCondition(sql, "JIESHOUSJ", kaishirq, jiezhirq, params);
		} else if (status == Status.RETUNED.getValue()) {
			wrapSJCondition(sql, "TUIHUISJ", kaishirq, jiezhirq, params);
		}
		sql.append( " and zhuangtai = ?");
		params.add(status);
		
		return this.jdbcTemplate.queryForObject(sql.toString(), params.toArray(), Integer.class);
	}
	
	private void wrapSJCondition(StringBuilder sql, String filedName, String kaishirq, String jiezhirq, List<Object> params) {
		if (StringUtils.isNotEmpty(kaishirq)) {
			sql.append(" and CHUANGJIANSJ >= ?");
			params.add(DateUtil.getBeginDateTime(DateUtil.dt10FromStr(kaishirq)));
		} 
		if (StringUtils.isNotEmpty(jiezhirq)) {
			sql.append(" and CHUANGJIANSJ <= ?");
			params.add(DateUtil.getEndDateTime(DateUtil.dt10FromStr(jiezhirq)));
		}
	}
	
	public List<StatusSummary> getStatusSummary(long yiguanId, String kaishirq, String jiezhirq) {
		StringBuilder sql = new StringBuilder("select zhuangtai, count(1) as amount from "
				 + Tables.TBL_YG_TIAOJICF).append(" where YIGUANID = ? ");
		List<Object> params = new ArrayList<>();
//		params.add(Status.SAVING.getValue());
//		params.add(Status.SUBMITED.getValue());
//		params.add(Status.ADJUST.getValue());
//		params.add(Status.RETUNED.getValue());
//		params.add(Status.DONE.getValue());
		params.add(yiguanId);
		if (StringUtils.isNotEmpty(kaishirq)) {
			sql.append(" and CHUANGJIANSJ >= ?");
			params.add(DateUtil.getBeginDateTime(DateUtil.dt10FromStr(kaishirq)));
		} 
		if (StringUtils.isNotEmpty(jiezhirq)) {
			sql.append(" and CHUANGJIANSJ <= ?");
			params.add(DateUtil.getEndDateTime(DateUtil.dt10FromStr(jiezhirq)));
		}
		sql.append(" group by zhuangtai");
		return this.jdbcTemplate.query(sql.toString(), params.toArray(), new CustomerMapper<StatusSummary>(StatusSummary.class));
	}
	
	public List<Map<String, Object>> queryLatestStaticsData(Date beginDate, Date endDate, long yiguanId) {
		String sql = "select tt.chuangjiansj, tt.shuliang from (SELECT DATE_FORMAT(CHUANGJIANSJ,'%Y-%m-%d') as chuangjiansj,count(1) as shuliang "
				+ " FROM " + Tables.TBL_YG_TIAOJICF + " WHERE yiguanId = ? and CHUANGJIANSJ BETWEEN "
				+ " ? AND ? GROUP BY DATE_FORMAT(CHUANGJIANSJ,'%Y-%m-%d')) tt group by tt.chuangjiansj";
		
		return super.jdbcTemplate.queryForList(sql, new Object[]{yiguanId, DateUtil.getBeginDateTime(beginDate),
				DateUtil.getEndDateTime(endDate)});
	}
	
	public long newTiaoji(YG_TIAOJICF tiaojicf) {
		return super.insertObjectAndGetAutoIncreaseId(tiaojicf, null);
	}
	
	public int updateTiaoji(YG_TIAOJICF tiaojicf) {
		Condition condition = new Condition();
		condition.setCondition("id", tiaojicf.getId(), ConditionType.EQ);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("YISHENGXM", tiaojicf.getYishengxm());
		params.put("chuangjianren", tiaojicf.getChuangjianren());
		params.put("yishengxm", tiaojicf.getYishengxm());
		params.put("chufanglx", tiaojicf.getChufanglx());
		params.put("fuwusid", tiaojicf.getFuwusid());
		params.put("nongjianbz", tiaojicf.getNongjianbz());
		params.put("tiaojibz", tiaojicf.getTiaojibz());
		params.put("shouhuodwlx", tiaojicf.getShouhuodwlx());
		params.put("genfangbz", tiaojicf.getGenfangbz());
		params.put("jihuapsrq", tiaojicf.getJihuapsrq());
		params.put("Jihuapssjfw", tiaojicf.getJihuapssjfw());
		params.put("shouhuoren", tiaojicf.getShouhuoren());
		params.put("shouhuolxdh", tiaojicf.getShouhuolxdh());
		params.put("shouhuodz", tiaojicf.getShouhuodz());
		params.put("chufangtp", tiaojicf.getChufangtp());
		params.put("zhuangtai", tiaojicf.getZhuangtai());
		params.put("Tiaojifs", tiaojicf.getTiaojifs());
		if(tiaojicf.getZhuangtai() == Status.SUBMITED.getValue()) {
			params.put("TIJIAOSJ", tiaojicf.getTijiaosj());
			params.put("TIJIAOREN", tiaojicf.getTijiaoren());
		}
		return super.updateByCondition(condition, params, YG_TIAOJICF.class);
	}
	
	public int updateStatus(long id, int[] orinStatus, Map<String, Object> params) {
		Condition condition = new Condition();
		condition.setCondition("id", id, ConditionType.EQ);
		condition.setCondition("zhuangtai", orinStatus, ConditionType.IN);
		return super.updateByCondition(condition, params, YG_TIAOJICF.class);
	}
	
	public YG_TIAOJICF getTiaojicfById(long tiaojicfId, long yiguanId) {
		Condition condition = new Condition();
		condition.setCondition("id", tiaojicfId);
		condition.setCondition("YIGUANID", yiguanId);
		return getSingleData(super.queryObjByCondition(condition, YG_TIAOJICF.class));
	}
	
	public String getLatestAddress(String shoujihm, String xingming) {
		String sql = "select SHOUHUODZ from " + Tables.TBL_YG_TIAOJICF + " where SHOUHUOREN = ? and SHOUHUOLXDH = ? order by id desc";
		return this.jdbcTemplate.query(sql, new Object[]{shoujihm, xingming}, new ResultSetExtractor<String>() {

			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					return rs.getString("SHOUHUODZ");
				}
				return null;
			}
			
		});
	}
	
	public void updateTiaojicfParseRst(long tiaojicfId, String chufanghb, Date jiuzhenrq, String huanzhexm, int xingbie,
			int nianning, String menzhengblh, String jiuzhenks, String huanzhelxdh, String zhenduan, String huanzhelxdz, 
			String feiyonglb, int tieshu, int weishu, int peisongbm, boolean shouhuorenFlag, boolean shouhuolxdhFlag, boolean shouhuodzFlag) {
		Condition condition = new Condition();
		condition.setCondition("id", tiaojicfId);
		List<String> columns = convertArray2List(new String[]{"CHUFANGBH","JIUZHENRQ","HUANZHEXM", "XINGBIE",
				"NIANLING","MENZHENBLH","KESHIMC","HUANZHELXDH","ZHENDUAN","HUANZHELXDZ","FEIYONGLB","TIESHU","WEISHU"});
		List<Object> values = convertArray2List(new Object[]{chufanghb, jiuzhenrq, huanzhexm, xingbie,
							nianning, menzhengblh, jiuzhenks, huanzhelxdh, zhenduan, huanzhelxdz, 
							feiyonglb, tieshu, weishu});
		if (peisongbm == 2) {
			if (shouhuorenFlag) {
				columns.add("SHOUHUOREN");
				values.add(huanzhexm);
			}
			if (shouhuolxdhFlag) {
				columns.add("SHOUHUOLXDH");
				values.add(huanzhelxdh);
			}
			if (shouhuodzFlag) {
				columns.add("SHOUHUODZ");
				values.add(huanzhelxdz);
			}
		}
		
		super.updateByCondition(condition, getUpdateParam(convertListToArray(columns), values.toArray()), YG_TIAOJICF.class);
	}
	
	public List<String> getYongyaopl() {
		String sql = "select MINGCHENG from " + Tables.TBL_PT_YONGYAOPL + " order by id";
		return this.jdbcTemplate.queryForList(sql, String.class);
	}
	
	public List<String> getYongfa() {
		String sql = "select MINGCHENG from " + Tables.TBL_PT_YAOPINYF + " order by id";
		return this.jdbcTemplate.queryForList(sql, String.class);
	}
	
	private String[] convertListToArray(List<String> dataList) {
		String[] result = new String[dataList.size()];
		for (int i = 0; i < dataList.size(); i++) {
			result[i] = dataList.get(i);
		}
		return result;
	}
	
	private List<String> convertArray2List(String[] arr) {
		List<String> rst = new ArrayList<String>();
		for (String a : arr) {
			rst.add(a);
		}
		return rst;
	}
	
	private List<Object> convertArray2List(Object[] arr) {
		List<Object> rst = new ArrayList<Object>();
		for (Object a : arr) {
			rst.add(a);
		}
		return rst;
	}
}
