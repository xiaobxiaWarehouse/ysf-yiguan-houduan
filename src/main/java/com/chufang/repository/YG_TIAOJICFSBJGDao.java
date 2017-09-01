package com.chufang.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.chufang.entity.YG_TIAOJICFSBJG;
import com.chufang.enumtype.ConditionType;
import com.chufang.parameter.Condition;

@Repository
public class YG_TIAOJICFSBJGDao extends BaseRepository<YG_TIAOJICFSBJG> {

	public void newYgTiaoJicfSB(YG_TIAOJICFSBJG tiaojicfsb) {
		super.insertObject(tiaojicfsb, null);
	}
	
	public int updateYgTiaojicfSB(long tiaojicfId, int zhuangtai, String ocrtxt, String shibietxt, String shibaiyy) {
		Condition condition = new Condition();
		condition.setCondition("TIAOJICFID", tiaojicfId, ConditionType.EQ);
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("OCRWENBEN", ocrtxt);
		param.put("YUYIFXJG", shibietxt);
		param.put("ZHUANGTAI", zhuangtai);
		if (StringUtils.isNotEmpty(shibaiyy)) {
			param.put("SHIBAIYY", ocrtxt);
		}
		
		return super.updateByCondition(condition, param, YG_TIAOJICFSBJG.class);
	}
	
	public YG_TIAOJICFSBJG getTiaojisbJG(long tiaojicfId, long yiguanId) {
		Condition condition = new Condition();
		condition.setCondition("TIAOJICFID", tiaojicfId);
		condition.setCondition("YIGUANID", yiguanId);
		
		return getSingleData(queryObjByCondition(condition, YG_TIAOJICFSBJG.class));
	}
	
	public void batchInsertYP(final long tiaojicfId, final long yiguanId, final List<Map<String, Object>> yaopinList) {
		String delete = "delete from " + Tables.TBL_YG_TIAOJICFYP + " where YIGUANID = ? and TIAOJICFID = ?";
		this.jdbcTemplate.update(delete, new Object[]{yiguanId, tiaojicfId});
		String sql = "insert into " + Tables.TBL_YG_TIAOJICFYP + " (YIGUANID,TIAOJICFID,YAOPINMC,SHULIANG,JILIANGDW) values (?,?,?,?,?)";
		this.jdbcTemplate.batchUpdate(sql,new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Map<String,Object> data = yaopinList.get(i);
				ps.setLong(1, yiguanId);
				ps.setLong(2, tiaojicfId);
				ps.setString(3, data.get("mingcheng").toString());
				ps.setDouble(4, StringUtils.isEmpty(data.get("shuliang").toString()) ? 0 : Double.parseDouble(data.get("shuliang").toString()));
				ps.setString(5, data.get("jiliangdw").toString());
			}
			
			@Override
			public int getBatchSize() {
				return yaopinList.size();
			}
		});
	}
	
	public Long getYaopinIdByMc(String mingcheng) {
		String sql = "select ml.id from " + Tables.TBL_PT_TIAOJISPML + " ml left join " 
				+ Tables.TBL_PT_TIAOJISPBM + " bm  on ml.id = bm.TIAOJISPID "
				+ " where (bm.MINGCHENG = ? and bm.JINYONGBZ=0) or (ml.MINGCHENG = ?)";
		return this.jdbcTemplate.query(sql, new Object[]{mingcheng, mingcheng}, new ResultSetExtractor<Long>() {

			@Override
			public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					return rs.getLong("id");
				}
				return null;
			}
			
		});
	}
}
