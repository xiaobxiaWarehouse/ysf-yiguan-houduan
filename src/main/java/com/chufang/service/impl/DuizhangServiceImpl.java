package com.chufang.service.impl;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.chufang.consts.ChufangConsts;
import com.chufang.consts.ErrorCode;
import com.chufang.entity.FWS_FUWUSXX;
import com.chufang.entity.Page;
import com.chufang.entity.YG_TIAOJICF;
import com.chufang.entity.ext.ChufangLXDetail;
import com.chufang.entity.ext.DuizhangExt;
import com.chufang.entity.ext.YaopinExt;
import com.chufang.enumtype.ChufangLX;
import com.chufang.repository.DuizhangRepository;
import com.chufang.repository.FWS_FUWUSXXDao;
import com.chufang.repository.YG_TiaoJiCFDao;
import com.chufang.service.DuizhangService;
import com.chufang.service.YiGuanService;
import com.chufang.util.DateUtil;
import com.chufang.util.NumberUtil;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Service
public class DuizhangServiceImpl extends CommonService implements DuizhangService {

	@Resource
	private DuizhangRepository duizhangDao;
	
	@Resource
	private FWS_FUWUSXXDao fuwushangDao;
	
	@Resource
	private YiGuanService yiguanService;
	
	@Resource
	private YG_TiaoJiCFDao tiaojiDao;
	
	private final static Logger logger = LoggerFactory.getLogger(DuizhangServiceImpl.class);
	
	
	@Override
	public JSONObject query(long yiguanId, String startDate, String endDate, int pageNo) {
		JSONObject rst = generateRst();
		
		//判断选择日期是否超过1个月。
		if (StringUtils.isEmpty(startDate)) {
			startDate = "2017-08-06";
		}
		
		if (StringUtils.isEmpty(endDate)) {
			endDate = DateUtil.dt10FromDate(new Date());
		}
		int oneFullMonthFlag = -1;
		Calendar c = Calendar.getInstance();
		c.setTime(DateUtil.extractTimeFromStr(startDate));
		String endDayOfMonth = "" + c.getActualMaximum(Calendar.DAY_OF_MONTH), tmpDateStr = null;
		List<Long> fuwushangIdList = new ArrayList<Long>();
		
//		//同一个月
		if (startDate.substring(0,7).equals(endDate.substring(0, 7))) {
			if (startDate.substring(9,10).equals("01")
					&& endDate.substring(9,10).equals(endDayOfMonth)) { //判断整月，则直接写月份。否则写什么时间到什么时间.
				oneFullMonthFlag = 1; //滿月
			} else {
				oneFullMonthFlag = 0;  //一個月。
			}
		}
		
		Page<DuizhangExt> page = new Page<DuizhangExt>();
		page.setPageNo(pageNo);
		duizhangDao.queryDuizhang(yiguanId, startDate, endDate, page);
		
		List<DuizhangExt> duizhangList = page.getData();
		
		for (DuizhangExt duizhang : duizhangList) {
			duizhang.setYaopinzl(extractYaopingCount(duizhang.getYaopinids()));
			if (oneFullMonthFlag == 1) {
				duizhang.setYuefen(startDate.split("-") + "年" + new Integer(startDate.split("-")[1]+"月"));
				duizhang.setKaishirq(startDate);
				duizhang.setJiezhirq(endDate);
			} else if (oneFullMonthFlag == 0) {
				duizhang.setYuefen(startDate + "至" + endDate);
				duizhang.setKaishirq(startDate);
				duizhang.setJiezhirq(endDate);
			} else {
				if (!fuwushangIdList.contains(duizhang.getFuwushangid())) { //前面没有数据
					c.setTime(DateUtil.extractTimeFromStr(startDate));
					if (duizhang.getKaishirq().compareTo(startDate) <= 0) { //第一個月匹配的。取小
						duizhang.setYuefen(startDate + "至" + duizhang.getKaishirq().substring(0,8) + c.getMaximum(Calendar.DAY_OF_MONTH));
						duizhang.setKaishirq(startDate);
						duizhang.setJiezhirq(duizhang.getKaishirq().substring(0,8) + c.getMaximum(Calendar.DAY_OF_MONTH));
					} else {
						duizhang.setYuefen(duizhang.getKaishirq() + "至" + duizhang.getKaishirq().substring(0,8) + c.getMaximum(Calendar.DAY_OF_MONTH));
						duizhang.setJiezhirq(duizhang.getKaishirq().substring(0,8) + c.getMaximum(Calendar.DAY_OF_MONTH));
					}
					fuwushangIdList.add(duizhang.getFuwushangid()); //表示已經有了數據
				} else {
					if (endDate.substring(0,7).replace("-", "").equals(duizhang.getYuefen())) { //最後一個月數據
						duizhang.setYuefen(endDate.substring(0,8) + "01至" + endDate);
						duizhang.setKaishirq(endDate.substring(0,8) + "01");
						duizhang.setJiezhirq(endDate);
					} else {
						tmpDateStr = duizhang.getYuefen().substring(0,4) + "-" + duizhang.getYuefen().substring(5,6) +"-01";
						duizhang.setYuefen(duizhang.getYuefen().substring(0,4) + "年" + new Integer(duizhang.getYuefen().substring(5,6)) + "月");
						duizhang.setKaishirq(tmpDateStr);
						c.setTime(DateUtil.extractTimeFromStr(tmpDateStr));
						duizhang.setJiezhirq(duizhang.getYuefen().substring(0,4) + "-" + duizhang.getYuefen().substring(5,6) + "-" + c.getMaximum(Calendar.DAY_OF_MONTH));
					}
				}
			}
		}
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[]{"yaopinids","month"});
		
		rst.put("data", JSONArray.fromObject(duizhangList, config));
		return rst;
	}

	private int extractYaopingCount(String yaopinIds) {
		if (StringUtils.isNotEmpty(yaopinIds)) {
			Set<String> set = new HashSet<String>(Arrays.asList(yaopinIds.split(",")));    
	        return set.size(); 
		} else {
			return 0;
		}
	}

	@Override
	public JSONObject pageYaopin(long yiguanId, String kaishirq, String jiezhirq, long fuwushangId, int chufanglx, int pageNo) {
		JSONObject rst = generateRst();
		Page<YaopinExt> page = new Page<YaopinExt>();
		page.setPageSize(24);
		page.setPageNo(pageNo);
		
		duizhangDao.pageYaopingList(yiguanId, fuwushangId, kaishirq,jiezhirq, chufanglx, page);
		
		rst.put("page", generatePage(page.getPageNo(), page.getTotalCount(), page.getTotalPage()));
		rst.put("data", page.getData());
		return rst;
	}

	@Override
	public JSONObject querySummary(long yiguanId, long fuwushangId, String kaishirq, String jiezhirq) {
		JSONObject rst = generateRst();
		FWS_FUWUSXX fuwushang = fuwushangDao.loadFwsById(fuwushangId);
		if (fuwushang == null) {
			rst.put(ChufangConsts.RC, ErrorCode.NO_MATCH_DATA);
			return rst;
		}
		//获取处方类型列表
		List<ChufangLXDetail> lxDetailList = duizhangDao.getChufangLXList(yiguanId, fuwushangId, kaishirq, jiezhirq);
		double chufanghj = 0.0, guolinhj = 0.0, hansuipjhj = 0.0, jiaoyijhj = 0.0;
		int chufangzl = 0, yaopinzl = 0;
		String tmpYaopinids = "";
		//统计合
		for (ChufangLXDetail detail : lxDetailList) {
			chufanghj += detail.getChufanghj();
			guolinhj += detail.getGuolinhj();
			hansuipjhj += detail.getHansuipjhj();
			jiaoyijhj += detail.getJiaoyijhj();
			chufangzl += detail.getChufangzl();
			detail.setYaopinzl(extractYaopingCount(detail.getYaopinids()));
			tmpYaopinids += detail.getYaopinids(); //各种类型可能存在相同的药，放后面统一去重
		}
		yaopinzl = extractYaopingCount(tmpYaopinids);//统一去重
		rst.put("fuwushangmc", fuwushang.getMingcheng());
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[]{"yaopinids"});
		rst.put("chufanglxlist", JSONArray.fromObject(lxDetailList, config));
		rst.put("chufanghj", chufanghj);
		rst.put("guolinhj", guolinhj);
		rst.put("hansuipjhj", hansuipjhj);
		rst.put("jiaoyijhj", jiaoyijhj);
		rst.put("chufangzl", chufangzl);
		rst.put("yaopinzl", yaopinzl);
		rst.put("yuefen", kaishirq + "至" + jiezhirq);
		return rst;
	}

	@Override
	public void duizhangExport(HttpServletRequest request, HttpServletResponse response, long yiguanId, String startDate, String endDate,
			long fuwushangId) {
		WritableCellFormat center = new WritableCellFormat();
		JSONObject rst = querySummary(yiguanId, fuwushangId, startDate, endDate);
		if (rst.getInt(ChufangConsts.RC) == ChufangConsts.OK) {
			JSONObject yiguanxx = yiguanService.queryYGXXById(yiguanId);
			String yiguanmc = yiguanxx.getString("mingcheng");
			
			WritableWorkbook wb = null;
			try {
				String fileName = yiguanmc+"委托调剂对账-" + DateUtil.dt14FromDate(new Date());
				String userAgent = request.getHeader("user-agent").toLowerCase();  
				  
				if (userAgent.contains("msie") || userAgent.contains("like gecko") ) {  
				        // win10 ie edge 浏览器 和其他系统的ie  
				    fileName = URLEncoder.encode(fileName, "UTF-8");  
				} else {  
				        // fe  
				    fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");  
				}
				//杭州一脉堂诊所委托调剂对账-20170809 141412
				 response.addHeader("Content-Disposition", "attachment;filename="  
		                    + fileName + ".xls");   
		            response.setContentType("application/vnd.ms-excel;charset=utf-8");  
				
				//水平居中对齐
		        center.setAlignment(Alignment.CENTRE);
				wb = Workbook.createWorkbook(response.getOutputStream());
				WritableSheet sheet = wb.createSheet("对账", 0);
				for (int v = 0; v < 9; v++) {
					if (v >= 2) {
						sheet.setColumnView(v, 20);
					} else {
						sheet.setColumnView(v, 30);
					}
					
				}
				
				sheet.mergeCells(0,0,8,0);
				sheet.addCell(new Label(0, 0, yiguanmc, center));
				sheet.addCell(new Label(0, 1, "药事服务商"));
				sheet.addCell(new Label(1, 1, rst.getString("fuwushangmc")));
				sheet.addCell(new Label(3, 1, "月份"));
				sheet.addCell(new Label(4, 1, startDate + "至" + endDate));
				sheet.addCell(new Label(0, 2, "处方总数量：" + rst.getInt("chufangzl"), center));
				sheet.addCell(new Label(1, 2, "药品种类：" + rst.getInt("yaopinzl")));
				sheet.mergeCells(3,2,4,2);
				sheet.addCell(new Label(3, 2, "处方金额合计：" + NumberUtil.formatYuan(rst.getDouble("chufanghj")) + "元", center)); 
				sheet.mergeCells(5,2,6,2);
				sheet.addCell(new Label(5, 2, "国零价合计：" + NumberUtil.formatYuan(rst.getDouble("guolinhj")) + "元", center));
				sheet.mergeCells(7,2,8,2);
				sheet.addCell(new Label(7, 2, "含税批价合计：" + NumberUtil.formatYuan(rst.getDouble("hansuipjhj")) + "元", center));
				
				int i = 3;
				sheet.mergeCells(0,i,8,i);
				sheet.addCell(new Label(0, i, "处方类型统计"));
				
				JSONArray chufangLxList = rst.getJSONArray("chufanglxlist");
				JSONObject item = null;
				i++;
				sheet.addCell(new Label(0, i,"序号"));
				sheet.addCell(new Label(1, i,"处方类型"));
				sheet.addCell(new Label(2, i,"处方数量"));
				sheet.addCell(new Label(3, i,"药品种类"));
				sheet.addCell(new Label(4, i,"处方金额合计（元）"));
				sheet.addCell(new Label(5, i,"国零价合计（元）"));
				sheet.addCell(new Label(6, i,"含税批价合计（元）"));
				sheet.addCell(new Label(7, i,"交易价合计（元）"));
				i++;
				int xulie = 0;
				for (int index = 0; index < chufangLxList.size(); index++) {
					item = chufangLxList.getJSONObject(index);
					sheet.addCell(new Label(0, i,"" + (++xulie)));
					sheet.addCell(new Label(1, i, ChufangLX.GAOJI.getNameByValue(item.getInt("chufanglx"))));
					sheet.addCell(new Label(2, i,item.getString("chufangzl")));
					sheet.addCell(new Label(3, i,item.getString("yaopinzl")));
					sheet.addCell(new Label(4, i,NumberUtil.formatYuan(item.getDouble("chufanghj"))));
					sheet.addCell(new Label(5, i,NumberUtil.formatYuan(item.getDouble("guolinhj"))));
					sheet.addCell(new Label(6, i,NumberUtil.formatYuan(item.getDouble("hansuipjhj"))));
					sheet.addCell(new Label(7, i,NumberUtil.formatYuan(item.getDouble("jiaoyijhj"))));
				}
				i+= chufangLxList.size();
				sheet.mergeCells(0,i,8,i);
				sheet.addCell(new Label(0, i, "药品明细统计"));
				i++;
				sheet.addCell(new Label(0, i,"序号"));
				sheet.addCell(new Label(1, i,"药品名称"));
				sheet.addCell(new Label(2, i,"数量（千克）"));
				sheet.addCell(new Label(3, i,"国零价（元）"));
				sheet.addCell(new Label(4, i,"含税批价（元）"));
				sheet.addCell(new Label(5, i,"交易价（元）"));
				
				Page<YaopinExt> page = new Page<YaopinExt>();
				page.setPageSize(Integer.MAX_VALUE);
				
				duizhangDao.pageYaopingList(yiguanId, fuwushangId, startDate, endDate, -1, page);
				List<YaopinExt> yaopinList = page.getData();
				i++;
				xulie = 0;
				for (YaopinExt yaopin : yaopinList) {
					sheet.addCell(new Label(0, i,"" + (++xulie)));
					sheet.addCell(new Label(1, i, yaopin.getMingcheng()));
					sheet.addCell(new Label(2, i, NumberUtil.formatYuan(yaopin.getShuliang())));
					sheet.addCell(new Label(3, i,NumberUtil.formatYuan(yaopin.getGuolingjia())));
					sheet.addCell(new Label(4, i,NumberUtil.formatYuan(yaopin.getHansuipj())));
					sheet.addCell(new Label(5, i,NumberUtil.formatYuan(yaopin.getJiaoyijia())));
					i++;
				}
				for (int v = 0; v < sheet.getRows(); v++) {
					sheet.setRowView(v, 400, false);
				}
				wb.write();
			} catch (Exception e) {
				logger.error("fail generate, ", e);
			} finally {
				if (wb != null) {
					try {
						wb.close();
					} catch (Exception e) {}
				}
			}
		
		}
	}
}
