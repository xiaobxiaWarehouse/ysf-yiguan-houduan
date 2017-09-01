package com.chufang.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.chufang.entity.Page;
import com.chufang.entity.ext.ChufangLXDetail;
import com.chufang.entity.ext.DuizhangExt;
import com.chufang.entity.ext.YaopinExt;
import com.chufang.enumtype.Status;
import com.chufang.rowmaper.CustomerMapper;
import com.chufang.util.DateUtil;

@Repository
public class DuizhangRepository extends BaseRepository<DuizhangExt> {
	
	public void queryDuizhang(long yiguanId, String startDate, String endDate, Page<DuizhangExt> page) {
		StringBuilder sql = new StringBuilder("select date_format(cf.WANCHENGSJ,'%Y%m') as yuefen, "
				+ "date_format(cf.WANCHENGSJ,'%Y%m') as month,"
				+ "date_format(cf.WANCHENGSJ,'%Y-%m-%d') as kaishirq,"
				+ "(select MINGCHENG from " + Tables.TBL_FWS_FUWUSXX + " where id = cf.FUWUSID) as fuwushangmc,"
				+ "cf.FUWUSID as fuwushangid,"
				+ "sum(yp.guolinjhj) as guolinjhj,"
				+ "sum(yp.hansuipjhj) as hansuipjhj,"
				+ "sum(cf.CHUFANGJE) as chufangjezj,"
				+ "sum(yp.jiaoyijhj) as jiaoyijhj, count(1) as chufangzl, group_concat(yp.yaopinids) as yaopinids "
				+ " from " + Tables.TBL_YG_TIAOJICF + " cf, "
				+ "(select TIAOJICFID, group_concat(FUWUSTJSPID) as yaopinids,"
				+ " sum(ifnull(GUOLINGJIA,0) * SHULIANG) as guolinjhj, sum(ifnull(HANSHUIPJ,0) * SHULIANG) as hansuipjhj, "
				+ " sum(ifnull(CHUFANGJIA,0) * SHULIANG) as chufangjezj,sum(ifnull(JIAOYIJIA,0)*SHULIANG) as jiaoyijhj "
				+ " from " + Tables.TBL_YG_TIAOJICFYP + " where YIGUANID=? and FUWUSTJSPID IS NOT NULL group by TIAOJICFID) yp ");
		sql.append(" where cf.YIGUANID = ? and cf.ZHUANGTAI in (?,?) and yp.TIAOJICFID = cf.id");
		
		List<Object> params = new ArrayList<Object>();
		params.add(yiguanId);
		params.add(yiguanId);
		params.add(Status.DONE.getValue());
		params.add(Status.ADJUST.getValue());
		if (StringUtils.isNotEmpty(startDate)) {
			sql.append(" and cf.WANCHENGSJ >= ?");
			params.add(DateUtil.getBeginDateTime(DateUtil.extractTimeFromStr(startDate)));
		}
		
		if (StringUtils.isNotEmpty(endDate)) {
			sql.append(" and cf.WANCHENGSJ <= ?");
			params.add(DateUtil.getEndDateTime(DateUtil.extractTimeFromStr(endDate)));
		}
		sql.append(" group by date_format(cf.WANCHENGSJ,'%Y%m'), cf.FUWUSID order by date_format(cf.WANCHENGSJ,'%Y%m'), cf.fuwusid");
		
		super.query(sql.toString(), params.toArray(), page, new CustomerMapper<DuizhangExt>(DuizhangExt.class));
	}
	
	public List<ChufangLXDetail> getChufangLXList(long yiguanId, long fuwushangId, String kaishirq, String jiezhirq) {
		StringBuilder sql = new StringBuilder("select cf.chufanglx, "
				+ "sum(yp.guolinjhj * cf.tieshu) as guolinhj,"
				+ "sum(yp.hansuipjhj * cf.tieshu) as hansuipjhj,"
				+ "sum(cf.chufangje) as chufanghj,"
				+ "sum(yp.jiaoyijhj * cf.tieshu) as jiaoyijhj, "
				+ "count(1) as chufangzl, group_concat(yp.yaopingids) as yaopinids "
				+ " from " + Tables.TBL_YG_TIAOJICF + " cf, "
				+ "(select TIAOJICFID, group_concat(FUWUSTJSPID) as yaopingids,"
				+ " sum(ifnull(GUOLINGJIA,0) * SHULIANG) as guolinjhj, sum(ifnull(HANSHUIPJ,0) * SHULIANG) as hansuipjhj, "
				+ " sum(ifnull(CHUFANGJIA,0) * SHULIANG) as chufangjezj,sum(ifnull(JIAOYIJIA,0)*SHULIANG) as jiaoyijhj "
				+ " from " + Tables.TBL_YG_TIAOJICFYP + " where YIGUANID=? and FUWUSTJSPID IS NOT NULL group by TIAOJICFID) yp ");
		sql.append(" where cf.YIGUANID = ? and cf.ZHUANGTAI in (?,?) and yp.TIAOJICFID = cf.id "
				+ " and date_format(cf.WANCHENGSJ,'%Y-%m-%d') between ? and ? and cf.FUWUSID = ? "
				+ " group by cf.chufanglx ");
		List<Object> params = new ArrayList<Object>();
		params.add(yiguanId);
		params.add(yiguanId);
		params.add(Status.DONE.getValue());
		params.add(Status.ADJUST.getValue());
		params.add(kaishirq);
		params.add(jiezhirq);
		params.add(fuwushangId);
		return this.jdbcTemplate.query(sql.toString(), params.toArray(), new CustomerMapper<ChufangLXDetail>(ChufangLXDetail.class));
	}
	
	public void pageYaopingList(long yiguanId, long fuwushangId, String kaishirq, String jiezhirq, 
			int chufanglx, Page<YaopinExt> page) {
		StringBuilder sql = new StringBuilder("select max(YAOPINMC) as mingcheng,"
				+ "ifnull(sum(SHULIANG * tmp.tieshu),0) as shuliang, sum(ifnull(GUOLINGJIA,0)*SHULIANG * tmp.tieshu) as guolingjia, "
				+ "sum(ifnull(HANSHUIPJ,0)*SHULIANG * tmp.tieshu) as hansuipj,"
				+ "sum(ifnull(JIAOYIJIA,0)*SHULIANG * tmp.tieshu) as jiaoyijia,"
				+ "0 as chufangjia "
				+ " from " + Tables.TBL_YG_TIAOJICFYP + " yp, ("
				+ "select id, tieshu from " + Tables.TBL_YG_TIAOJICF + " where date_format(WANCHENGSJ,'%Y-%m-%d') between ? and ? "
						+ " and zhuangtai in (?,?) and YIGUANID = ? ");
		List<Object> params = new ArrayList<Object>();
		params.add(kaishirq);
		params.add(jiezhirq);
		params.add(Status.DONE.getValue());
		params.add(Status.ADJUST.getValue());
		params.add(yiguanId);
		
		if (chufanglx >-1) {
			sql.append(" and CHUFANGLX = ?");
			params.add(chufanglx);
		}
		sql.append(" ) tmp where yp.TIAOJICFID = tmp.id and yp.FUWUSTJSPID > 0 group by FUWUSTJSPID order by yp.id");

		super.query(sql.toString(), params.toArray(), page, new CustomerMapper<YaopinExt>(YaopinExt.class));
	}
	
}
