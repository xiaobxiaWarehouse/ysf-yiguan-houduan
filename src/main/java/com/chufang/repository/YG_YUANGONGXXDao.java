package com.chufang.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.chufang.entity.Page;
import com.chufang.entity.YG_YUANGONGXX;
import com.chufang.entity.ext.YuangongExt1;
import com.chufang.entity.ext.YuangongExt2;
import com.chufang.enumtype.ConditionType;
import com.chufang.parameter.Condition;
import com.chufang.rowmaper.CustomerMapper;

@Repository
public class YG_YUANGONGXXDao extends BaseRepository<YG_YUANGONGXX> {
	public YG_YUANGONGXX getYgYonghuByCustId(long custId, long yiguanID) {
		Condition condition = new Condition();
		condition.setCondition("KEHUID", custId, ConditionType.EQ);
		condition.setCondition("YIGUANID", yiguanID, ConditionType.EQ);
		return getSingleData(super.queryObjByCondition(condition, YG_YUANGONGXX.class));
	}
	
	public YG_YUANGONGXX getYgYonghuByCustId(long custId) {
		Condition condition = new Condition();
		condition.setCondition("KEHUID", custId, ConditionType.EQ);
		return getSingleData(super.queryObjByCondition(condition, YG_YUANGONGXX.class));
	}
	
	public YG_YUANGONGXX getYgYonghuById(int id, long yiguanId) {
		Condition condition = new Condition();
		condition.setCondition("id", id, ConditionType.EQ);
		condition.setCondition("YIGUANID", yiguanId, ConditionType.EQ);
		return getSingleData(super.queryObjByCondition(condition, YG_YUANGONGXX.class));
	}
	
	
	
	public YG_YUANGONGXX getYgYonghuById(int id) {
		Condition condition = new Condition();
		condition.setCondition("id", id, ConditionType.EQ);
		return getSingleData(super.queryObjByCondition(condition, YG_YUANGONGXX.class));
	}
	
	public long newYgYonghu(YG_YUANGONGXX ygYonghu) {
		return super.insertObjectAndGetAutoIncreaseId(ygYonghu, null);
	}
	
	public int updateById(YG_YUANGONGXX ygYonghu) {
		Condition condition = new Condition();
		condition.setCondition("id", ygYonghu.getId(), ConditionType.EQ);
		Map<String, Object> params = getUpdateParam(new String[]{"Jinyongbz","Gangwei","JIARUSJ", "Quanxian","XINGMING"}, ygYonghu);
		if (params == null) {
			return -1;
		}
		return super.updateByCondition(condition, params, YG_YUANGONGXX.class);
	}
	
	public void page(Page<YuangongExt1> page, long yiguanId, String shouji, int zhuangtai) {
		StringBuilder sql = new StringBuilder("select kehu.id as yuangongid, kehu.shoujihm, kehu.xingming, "
				+ " kehu.xingbie, yg.gangwei, yg.jinyongbz from " + Tables.TBL_PT_KEHUXX + " kehu, " + Tables.TBL_YG_YUANGONGXX + " yg"
				+ " where kehu.id = yg.kehuid and yg.YIGUANID = ?");
		List<Object> params = new ArrayList<Object>();
		params.add(yiguanId);
		if (zhuangtai > -1) {
			sql.append(" and yg.JINYONGBZ = ?");
			params.add(zhuangtai);
		}
		
		if (StringUtils.isNotEmpty(shouji)) {
			sql.append(" and (kehu.SHOUJIHM like ? or kehu.XINGMING like ?) ");
			params.add("%" + shouji + "%");
			params.add("%" + shouji + "%");
		}
		
		sql.append(" order by kehu.id");
		
		super.query(sql.toString(), params.toArray(), page, new CustomerMapper<YuangongExt1>(YuangongExt1.class));
		
	}
	
	public YuangongExt2 getYuangongExt2ById(long yiguanId, long yuangongId) {
		String sql = "select kehu.shoujihm, kehu.xingming, yg.gangwei, yg.jinyongbz, kehu.id as yuangongid, yg.quanxian"
				+ " from " + Tables.TBL_YG_YUANGONGXX + " yg," + Tables.TBL_PT_KEHUXX + " kehu where "
				+ " kehu.id = yg.kehuid and kehu.id = ? and yg.yiguanid = ?";
		return getSingleData(this.jdbcTemplate.query(sql, new Object[]{yuangongId, yiguanId}, new CustomerMapper<YuangongExt2>(YuangongExt2.class)));
	}
	
	public int updateJinyongBZ(long yiguanId, int yuangongId, int jinyongbz) {
		Condition condition = new Condition();
		condition.setCondition("id", yuangongId, ConditionType.EQ);
		condition.setCondition("yiguanid", yiguanId, ConditionType.EQ);
		return super.updateByCondition(condition, 
				getUpdateParam(new String[]{"JINYONGBZ"}, new Object[]{jinyongbz}), YG_YUANGONGXX.class);
	}
	
	public List<String> getKaifangYs(long yiguanId, int isall) {
		StringBuilder sql = new StringBuilder("select XINGMING from " + Tables.TBL_YG_YUANGONGXX + " where GANGWEI in (1,3) and YIGUANID = ?");
		List<Object> params = new ArrayList<Object>();
		params.add(yiguanId);
		if (isall == 0) {
			sql.append( " and JINYONGBZ = ?");
			params.add(isall);
		}
		return this.jdbcTemplate.queryForList(sql.toString(), params.toArray(), String.class);
	}
}
