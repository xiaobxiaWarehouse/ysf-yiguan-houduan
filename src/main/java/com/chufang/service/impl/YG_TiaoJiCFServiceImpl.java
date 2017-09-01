package com.chufang.service.impl;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.chufang.consts.ChufangConsts;
import com.chufang.consts.ErrorCode;
import com.chufang.entity.PT_KEHUXX;
import com.chufang.entity.Page;
import com.chufang.entity.YG_TIAOJICF;
import com.chufang.entity.YG_TIAOJICFSBJG;
import com.chufang.entity.YG_TIAOJICFYP;
import com.chufang.entity.YG_YUANGONGXX;
import com.chufang.entity.ext.StatusSummary;
import com.chufang.enumtype.ChufangLX;
import com.chufang.enumtype.Status;
import com.chufang.enumtype.TiaojiFS;
import com.chufang.parameter.ChufangParam;
import com.chufang.parameter.TiaoJiSearchParam;
import com.chufang.repository.YG_TIAOJICFSBJGDao;
import com.chufang.repository.YG_TiaoJiCFDao;
import com.chufang.repository.YG_TiaoJiCFYPDao;
import com.chufang.service.AuthService;
import com.chufang.service.FuwushangService;
import com.chufang.service.HttpService;
import com.chufang.service.OCRService;
import com.chufang.service.OSSService;
import com.chufang.service.YG_TiaoJiCFService;
import com.chufang.service.YiGuanService;
import com.chufang.util.DateUtil;
import com.chufang.util.NumberUtil;
import com.chufang.util.StringUtil;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class YG_TiaoJiCFServiceImpl extends CommonService implements YG_TiaoJiCFService {

	private final static Logger logger = LoggerFactory.getLogger(YG_TiaoJiCFServiceImpl.class);
	
	@Resource
	private YG_TiaoJiCFDao tiaojicfDao;
	
	@Resource
	private YG_TIAOJICFSBJGDao tiaojicfsbDao;
	
	@Resource
	private YG_TiaoJiCFYPDao tiaojicfypDao;
	
	@Resource
	private AuthService authService;
	
	@Resource
	private OCRService ocrService;
	
	@Resource
	private HttpService httpService;
	
	@Resource
	private FuwushangService fuwushangService;
	
	@Resource
	private OSSService ossService;
	
	private ExecutorService extService = Executors.newFixedThreadPool(10);
	
	@Value("${yuyi.parse.host}")
	private String YUYI_PARSE_HOST;
	
	@Value("${oss.endpoint}")
	private String OSS_END_POINT;
	
	@Value("${bucket.name}")
	private String BUCKET_NAME;
	
	@Override
	public JSONObject queryTiaoJiCf(TiaoJiSearchParam searchParam, long yiguanId) {
		Page<YG_TIAOJICF> pager = new Page<YG_TIAOJICF>();
		pager.setPageNo(searchParam.getPageNo());
		tiaojicfDao.pageByYGTiaoJiCf(searchParam.getKaishirq(),searchParam.getJiezhirq(), searchParam.getFuwushangid(),
				searchParam.getStatus(), searchParam.getChufangbh(), searchParam.getPhone(),searchParam.getIsall(), pager, yiguanId);
		JSONObject rst = generateRst();
		JSONArray dataList = new JSONArray();
		for (YG_TIAOJICF tiaojicf : pager.getData()) {
			dataList.add(wrapTiaojiCf(tiaojicf));
		}
		rst.put("dataList", dataList);
		rst.put("page", generatePage(pager.getPageNo(), pager.getTotalCount(), pager.getTotalPage()));
		rst.put("summary", buildSummary(searchParam, yiguanId));
		return rst;
	}
	
	@Override
	public void export(HttpServletRequest request, HttpServletResponse response, TiaoJiSearchParam searchParam, long yiguanId) {
		Page<YG_TIAOJICF> pager = new Page<YG_TIAOJICF>();
		pager.setPageNo(1);
		pager.setPageSize(Integer.MAX_VALUE);
		tiaojicfDao.pageByYGTiaoJiCf(searchParam.getKaishirq(),searchParam.getJiezhirq(), searchParam.getFuwushangid(),
				searchParam.getStatus(), searchParam.getChufangbh(), searchParam.getPhone(),searchParam.getIsall(), pager, yiguanId);
		
		List<YG_TIAOJICF> list = pager.getData();
		WritableCellFormat center = new WritableCellFormat();
		WritableWorkbook wb = null;
		try {
			String fileName = "查询调剂-" + DateUtil.dt14FromDate(new Date());
			String userAgent = request.getHeader("user-agent").toLowerCase();  
			
			if (userAgent.contains("msie") || userAgent.contains("like gecko") ) {  
			        // win10 ie edge 浏览器 和其他系统的ie  
			    fileName = URLEncoder.encode(fileName, "UTF-8");  
			} else {  
			    fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");  
			}
			//杭州一脉堂诊所委托调剂对账-20170809 141412
			 response.addHeader("Content-Disposition", "attachment;filename="  
	                    + fileName + ".xls");   
	         response.setContentType("application/vnd.ms-excel;charset=utf-8");  
	        wb = Workbook.createWorkbook(response.getOutputStream());
	        center.setAlignment(Alignment.CENTRE);
	        
	        WritableSheet sheet = wb.createSheet("调剂查询", 0);
	        for (int v = 0; v <= 9; v++) {
				sheet.setColumnView(v, 20);
			}
	        
	        int i = 0;
			sheet.addCell(new Label(0, i,"序号",center));
			sheet.addCell(new Label(1, i,"处方编号",center));
			sheet.addCell(new Label(2, i,"患者姓名",center));
			sheet.addCell(new Label(3, i,"提交时间",center));
			sheet.addCell(new Label(4, i,"开方医生",center));
			sheet.addCell(new Label(5, i,"处方类型",center));
			sheet.addCell(new Label(6, i,"服务商名称",center));
			sheet.addCell(new Label(7, i,"调剂方式",center));
			sheet.addCell(new Label(8, i,"配送目标",center));
			sheet.addCell(new Label(9, i,"状态",center));
			int xulie = 1;
			i++;
			for (YG_TIAOJICF tiaojicf : list) {
				sheet.addCell(new Label(0, i,"" + xulie,center));
				sheet.addCell(new Label(1, i, StringUtils.isEmpty(tiaojicf.getChufangbh()) ? "识别中" : tiaojicf.getChufangbh(),center));
				sheet.addCell(new Label(2, i, StringUtils.isEmpty(tiaojicf.getHuanzhexm()) ? "识别中" : tiaojicf.getHuanzhexm(),center));
				sheet.addCell(new Label(3, i, tiaojicf.getTijiaosj() == null ? "" : DateUtil.dt14LongFormat(tiaojicf.getTijiaosj()),center));
				sheet.addCell(new Label(4, i, tiaojicf.getYishengxm(),center));
				sheet.addCell(new Label(5, i, ChufangLX.GAOJI.getNameByValue(tiaojicf.getChufanglx()),center));
				sheet.addCell(new Label(6, i, tiaojicf.getFuwusmc(),center));
				sheet.addCell(new Label(7, i, TiaojiFS.DAFEN.getNameByValue(tiaojicf.getTiaojifs()),center));
				sheet.addCell(new Label(8, i, tiaojicf.getShouhuodwlx() == 1 ? "医馆" : (tiaojicf.getShouhuodwlx() == 2 ? "个人":""),center));
				sheet.addCell(new Label(9, i, Status.DONE.getNameByValue(tiaojicf.getZhuangtai()),center));
				i++;
				xulie++;
			}
			
			for (int v = 0; v < sheet.getRows(); v++) {
				sheet.setRowView(v, 400, false);
			}
			wb.write();
		} catch (Exception e) {
			logger.error("fail to generate tiaojicf report", e);
		} finally {
			if (wb != null) {
				try {
					wb.close();
				} catch (Exception e) {}
			}
		}
				
	}
	
	private JSONObject buildSummary(TiaoJiSearchParam searchParam, long yiguanId) {
		JSONObject rst = new JSONObject();
		rst.put("baocunzl", 0);
		rst.put("yitijiaozl", 0);
		rst.put("wanchengzl", 0);
		rst.put("tiaojizhongzl", 0);
		rst.put("yituihuizl", 0);
		rst.put("daijieshouzl", 0);
		if (searchParam.getStatus() <=0 && StringUtils.isEmpty(searchParam.getKaishirq()) && StringUtils.isEmpty(searchParam.getJiezhirq())) {
			List<StatusSummary> dataList = tiaojicfDao.getStatusSummary(yiguanId, searchParam.getKaishirq(),searchParam.getJiezhirq());
			for (StatusSummary data : dataList) {
				if (data.getZhuangtai() == Status.ADJUST.getValue()) {
					rst.put("tiaojizhongzl", data.getAmount());
				} else if (data.getZhuangtai() == Status.SAVING.getValue()) {
					rst.put("baocunzl", data.getAmount());
				} else if (data.getZhuangtai() == Status.SUBMITED.getValue()) {
					rst.put("yitijiaozl", data.getAmount());
				} else if (data.getZhuangtai() == Status.RETUNED.getValue()) {
					rst.put("yituihuizl", data.getAmount());
				} else if (data.getZhuangtai() == Status.DONE.getValue()) {
					rst.put("wanchengzl", data.getAmount());
				} else if (data.getZhuangtai() == Status.WAITING_RECEIVE.getValue()) {
					rst.put("daijieshouzl", data.getAmount());
				}
			}
		} else {
			rst.put("baocunzl", tiaojicfDao.getStatusCnt(yiguanId, searchParam.getKaishirq(), searchParam.getJiezhirq(), Status.SAVING.getValue()));
			rst.put("yitijiaozl", tiaojicfDao.getStatusCnt(yiguanId, searchParam.getKaishirq(), searchParam.getJiezhirq(), Status.SUBMITED.getValue()));
//			rst.put("wanchengzl", tiaojicfDao.getStatusCnt(yiguanId, searchParam.getKaishirq(), searchParam.getJiezhirq(), Status.DONE.getValue()));
			rst.put("tiaojizhongzl", tiaojicfDao.getStatusCnt(yiguanId, searchParam.getKaishirq(), searchParam.getJiezhirq(), Status.ADJUST.getValue()));
			rst.put("yituihuizl", tiaojicfDao.getStatusCnt(yiguanId, searchParam.getKaishirq(), searchParam.getJiezhirq(), Status.RETUNED.getValue()));
			rst.put("daijieshouzl", tiaojicfDao.getStatusCnt(yiguanId, searchParam.getKaishirq(), searchParam.getJiezhirq(), Status.WAITING_RECEIVE.getValue()));		}
		return rst;
	}
	
	private JSONObject wrapTiaojiCf(YG_TIAOJICF tiaojicf) {
		JSONObject rst = new JSONObject();
		rst.put("id", tiaojicf.getId());
		rst.put("chufangbh", tiaojicf.getChufangbh() == null ? "" : tiaojicf.getChufangbh());
		rst.put("huanzhexm", tiaojicf.getHuanzhexm() == null ? "" : tiaojicf.getHuanzhexm());
		if (tiaojicf.getZhuangtai() == Status.SAVING.getValue()) {
			rst.put("tijiaosj", "未提交");
		} else {
			rst.put("tijiaosj", tiaojicf.getChuangjiansj() == null ? "未提交" : DateUtil.dt14LongFormat(tiaojicf.getChuangjiansj()));
		}
		rst.put("kaifangys", tiaojicf.getYishengxm());
		rst.put("chufanglx", tiaojicf.getChufanglx());
		rst.put("fuwushangmc", tiaojicf.getFuwusmc());
		rst.put("peisongmb", tiaojicf.getShouhuodwlx());
		rst.put("zhuangtai", tiaojicf.getZhuangtai());
		rst.put("tiaojifs", tiaojicf.getTiaojifs());
		return rst;
	}

	@Override
	public JSONObject queryStatics(int day, long yigaunId) {
		JSONObject rst = generateRst();
		List<Map<String, Object>> dataList = new ArrayList<>();
		Map<String, Object> data = null;
		Calendar startTime = Calendar.getInstance();
		startTime.add(Calendar.DAY_OF_YEAR, -day+1);
		for (int i = 1; i <= day; i++) {
			data = new HashMap<String, Object>();
			data.put("chuangjiansj",DateUtil.dt10FromDate(startTime.getTime()));
			data.put("shuliang",0);
			dataList.add(data);
			startTime.add(Calendar.DAY_OF_YEAR, 1);
		}
	
		Calendar now = Calendar.getInstance();
		Date endDate = now.getTime();
		startTime.add(Calendar.DAY_OF_YEAR, -day+1);
		Date beginDate =startTime.getTime();
		List<Map<String, Object>> dbDataList = tiaojicfDao.queryLatestStaticsData(beginDate, endDate, yigaunId);
		for (Map<String, Object> dbData : dbDataList) {
			for (Map<String, Object> tmpData : dataList) {
				if (dbData.get("chuangjiansj").equals(tmpData.get("chuangjiansj"))) {
					tmpData.put("shuliang", dbData.get("shuliang"));
					break;
				}
			}
		}
		rst.put("data", dataList);
		return rst;
	}
	
	@Override
	public JSONObject saveTiaojiCf(final ChufangParam chufang, final long userId, final long yiguanId) {
		JSONObject rst = generateRst();
//		chufang.setChufangtp(String.format(OSS_END_POINT, (BUCKET_NAME + ".")) + "/" + chufang.getChufangtp());
		if (StringUtils.isEmpty(chufang.getChufangtp())
				|| (chufang.getZhuangtai() != Status.SAVING.getValue() && 
				chufang.getZhuangtai() != Status.SUBMITED.getValue())) {
			rst.put(ChufangConsts.RC, ErrorCode.INVALID_PARAM);
			logger.error("fail to get chufangtp.userId=" + userId);
			return rst;
		}
		YG_TIAOJICF tiaojicf = new YG_TIAOJICF();
		PT_KEHUXX kehuxx = authService.getKehuxxById(userId);
		JSONObject fuwushangxx = fuwushangService.loadFuwushangById(chufang.getFuwushangid());
		if (fuwushangxx == null) {
			rst.put(ChufangConsts.RC, ErrorCode.INVALID_PARAM);
			logger.error("fuwushangxx can not be found.fuwushangxxId=" + chufang.getFuwushangid());
			return rst;
		}
		YG_YUANGONGXX ygYGxx = authService.getYgYuanGongXX(userId);
		
		if (ygYGxx == null) {
			rst.put(ChufangConsts.RC, ErrorCode.INVALID_PARAM);
			logger.error("fail to get getYgYuanGongXX.userId=" + userId);
			return rst;
		}
		tiaojicf.setChuangjiansj(new Date());
		if (chufang.getZhuangtai() == Status.SUBMITED.getValue()) {
			tiaojicf.setTijiaosj(new Date());
			tiaojicf.setTijiaoren(kehuxx.getXingming());
		}
		tiaojicf.setChuangjianren(kehuxx.getXingming());
		
		/**
		 * private String kaifangys;
	private String chufanglx;
	private long fuwushangid;
	private int tiaojifs;
	private int nongjianbz;
	private String tiaojibz;
	private int shouhuodwlx;
	private int genfangbz;
	private String peisongrq;
	private int shangwubz;
	private String shoujianrxm;
	private String lianxidh;
	private String peisongdz;
	private String chufangtp;
		 */
		tiaojicf.setChufangly(chufang.getChufangly());
		tiaojicf.setYishengxm(chufang.getKaifangys());
		tiaojicf.setChufanglx(chufang.getChufanglx());
		tiaojicf.setFuwusid(chufang.getFuwushangid());
		tiaojicf.setNongjianbz(chufang.getNongjianbz());
		tiaojicf.setTiaojibz(chufang.getTiaojibz());
		tiaojicf.setShouhuodwlx(chufang.getShouhuodwlx());
		tiaojicf.setGenfangbz(chufang.getGenfangbz());
		tiaojicf.setJihuapsrq(chufang.getPeisongrq());
		tiaojicf.setJihuapssjfw(chufang.getShangwubz());
		tiaojicf.setShouhuoren(chufang.getShoujianrxm());
		tiaojicf.setShouhuolxdh(chufang.getLianxidh());
		tiaojicf.setShouhuodz(chufang.getPeisongdz());
		tiaojicf.setChufangtp(chufang.getChufangtp());
		tiaojicf.setZhuangtai(chufang.getZhuangtai());
		tiaojicf.setTiaojifs(chufang.getTiaojifs());
		tiaojicf.setYiguanid(yiguanId);
		
		final YG_TIAOJICFSBJG tiaojicfsb = new YG_TIAOJICFSBJG();
		tiaojicfsb.setYiguanid(ygYGxx.getYiguanid());
		
		if (chufang.getTiaojicfid() == 0) {
		
			final long tiaojiID = tiaojicfDao.newTiaoji(tiaojicf);
			chufang.setTiaojicfid(tiaojiID);
			tiaojicfsb.setTiaojicfid(new Long(tiaojiID).intValue());
			if (chufang.getZhuangtai() == Status.SUBMITED.getValue()) {
				extService.execute(new Runnable() {
					
					@Override
					public void run() {
						try {
							JSONObject ocrRst = ocrService.processImgFile(ossService.getPrivateAccessURL(chufang.getChufangtp()));
							if (ocrRst.getInt(ChufangConsts.RC) == ChufangConsts.OK) {
								tiaojicfsb.setOcrwenben(ocrRst.getString("data"));
								//{"header":{"title":"","type":"","no":"212630","date":"2017年07月09日"},"customer":{"name":"殷粉粉","sex":"女","age":"47","fee_type":"职工在职","client_no":"A22837242","dept":"中医科","address":"浙江中烟工业有限责任公司杭州卷炬","mobile":"13819451520","reason":";结膜炎,肝肾亏虚","medical_record":""},"drugs":[{"name":"柴胡","g":"10"},{"name":"防风","g":"10"},{"name":"青箱子","g":"10"},{"name":"千里光","g":"10"},{"name":"密蒙花","g":"10"},{"name":"枸杞子","g":"15"},{"name":"生白术","g":"30"},{"name":"阳春砂","g":"6"},{"name":"炒枳实","g":""},{"name":"当归","g":"10"},{"name":"决明子","g":"15"},{"name":"浙贝母","g":"15"},{"name":"牡蛎","g":"30"},{"name":"蜜甘草","g":"6"},{"name":"泽泻","g":"10"},{"name":"骨碎补","g":"15"},{"name":"续断","g":"10"},{"name":"杜仲","g":"10"}],"bottom":{"tie":"","wei":"","doctor":""}}
		
								JSONObject parseRst = httpService.processRequest(YUYI_PARSE_HOST, ocrRst.getJSONObject("data"));
								if (parseRst != null) {
									processYuyifenxiJG(parseRst, chufang.getTiaojicfid(), yiguanId, null, tiaojicfsb, chufang.getShouhuodwlx(),
											chufang.getShoujianrxm(), chufang.getLianxidh(), chufang.getPeisongdz());
								} else {
									tiaojicfsb.setYuyifxjg("");
									tiaojicfsb.setZhuangtai(0);
								}
								
							} else {
								tiaojicfsb.setShibaiyy(ocrRst.getString(ChufangConsts.ERR_MSG));
								tiaojicfsb.setYuyifxjg("");
								tiaojicfsb.setZhuangtai(0);
							}
							tiaojicfsbDao.newYgTiaoJicfSB(tiaojicfsb);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		} else {
			tiaojicf.setId(chufang.getTiaojicfid());
			tiaojicfsb.setTiaojicfid(chufang.getTiaojicfid());
			
			if (chufang.getZhuangtai() == Status.SUBMITED.getValue()) {
				extService.execute(new Runnable() {
					
					@Override
					public void run() {
						try {
							JSONObject ocrRst = ocrService.processImgFile(ossService.getPrivateAccessURL(chufang.getChufangtp()));
							if (ocrRst.getInt(ChufangConsts.RC) == ChufangConsts.OK) {
								JSONObject parseRst = httpService.processRequest(YUYI_PARSE_HOST, ocrRst.getJSONObject("data"));
								processYuyifenxiJG(parseRst, chufang.getTiaojicfid(), yiguanId, null, tiaojicfsb, chufang.getShouhuodwlx(), 
										chufang.getShoujianrxm(), chufang.getLianxidh(), chufang.getPeisongdz());
								tiaojicfsb.setOcrwenben(ocrRst.toString());
								
								tiaojicfsbDao.updateYgTiaojicfSB(chufang.getTiaojicfid(), tiaojicfsb.getZhuangtai(), ocrRst.getString("data"), tiaojicfsb.getYuyifxjg(), null);
							} else {
								tiaojicfsbDao.updateYgTiaojicfSB(chufang.getTiaojicfid(), 0, "", "", ocrRst.getString(ChufangConsts.ERR_MSG));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
			int updateRst = tiaojicfDao.updateTiaoji(tiaojicf);
			logger.info("update chufangId(" + chufang.getTiaojicfid() + "), rst = " + updateRst);
		}
		rst.put("tiaojicfid", chufang.getTiaojicfid());
		return rst;
	}

	private void processYuyifenxiJG(JSONObject parseRst, long tiaojicfId, long yiguanId, String ocrErrMsg, 
			YG_TIAOJICFSBJG tiaojicfsb, int peisongbm, String shouhuoren, String shouhuolxdh, String shouhuodz) {
		tiaojicfsb.setYiguanid(yiguanId);
		if (parseRst != null) {
			tiaojicfsb.setYuyifxjg(parseRst.toString());
			Date jiuzhenDate = null;
			String jiuzhenrq = extractFieldValue("date","header", parseRst);
			if (StringUtils.isNotEmpty(jiuzhenrq)) {
				try {
					jiuzhenDate = DateUtil.dt10FromStr(jiuzhenrq);
				} catch (Exception e) {}
			}
			String sex = extractFieldValue("sex","customer", parseRst);
			String age = extractFieldValue("age","customer", parseRst);
			int intAge = 0;
			try {
				intAge = age.equals("") ? 0 : Integer.parseInt(age);
			} catch (Exception e) {}
			String tie = extractFieldValue("tie","bottom", parseRst);
			String wei = extractFieldValue("wei","bottom", parseRst);
			double percent = 0.0;
			try {
				if ( parseRst.containsKey("percentage")) {
					percent = Double.parseDouble(parseRst.getString("percentage").replace("%", ""));
				}
			} catch (Exception e){}
			tiaojicfDao.updateTiaojicfParseRst(tiaojicfId,
					extractFieldValue("no","header", parseRst), jiuzhenDate, 
					extractFieldValue("name","customer", parseRst), 
					sex.equals("女") ? 2 : (sex.equals("男") ? 1 : 3), 
					intAge,
					extractFieldValue("client_no","customer", parseRst),
					extractFieldValue("dept","customer", parseRst), 
					extractFieldValue("mobile","customer", parseRst), 
					extractFieldValue("reason","customer", parseRst),
					extractFieldValue("address","customer", parseRst),
					extractFieldValue("fee_type","customer", parseRst), 
					StringUtils.isEmpty(tie) ? 0 : Integer.parseInt(tie),
					StringUtils.isEmpty(wei) ? 0 : Integer.parseInt(wei), peisongbm,
					StringUtils.isEmpty(shouhuoren) ? true : false,
							StringUtils.isEmpty(shouhuolxdh) ?  true : false,
									StringUtils.isEmpty(shouhuodz) ? true : false);
			JSONArray drugs = parseRst.getJSONArray("drugs");
			try {
				List<Map<String, Object>> yaopinList = new ArrayList<Map<String, Object>>();
				Map<String, Object> yaopin = null;
				for (int j = 0; j < drugs.size(); j++) {
					yaopin = new HashMap<String, Object>();
					yaopin.put("mingcheng", drugs.getJSONObject(j).getString("name"));
					yaopin.put("shuliang", drugs.getJSONObject(j).getString("g"));
					yaopin.put("jiliangdw", "g");
//					yaopin.put("yaopinid", tiaojicfsbDao.getYaopinIdByMc(drugs.getJSONObject(j).getString("name")));
					yaopinList.add(yaopin);
				}
				tiaojicfsbDao.batchInsertYP(tiaojicfId, yiguanId, yaopinList);
				tiaojicfsb.setZhuangtai(1);
				tiaojicfsb.setWanzhengdu(percent);
			} catch (Exception e) {
				logger.error("fail to insert yaopin info", e);
				tiaojicfsb.setZhuangtai(0);
				tiaojicfsb.setShibaiyy(e.toString());
			}
		} else {
			logger.warn("yuyi rst = " + parseRst);
			tiaojicfsb.setYuyifxjg("");
			tiaojicfsb.setZhuangtai(0);
			tiaojicfsb.setShibaiyy("语义解析失败");
		}
	}
	
	private String extractFieldValue(String fieldName, String objKey, JSONObject data) {
		if (data.containsKey(objKey) && data.getJSONObject(objKey).containsKey(fieldName)) {
			return data.getJSONObject(objKey).getString(fieldName);
		} else {
			return "";
		}
	}
	
	@Override
	public JSONObject updateTiaojiCFStatus(final long chufangId, int status, long userId, final long yiguanId) {
		JSONObject rst = generateRst();
		PT_KEHUXX kehuxx = authService.getKehuxxById(userId);
		int updateCnt = -1;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("zhuangtai", status);
		if (status == Status.ABORTING.getValue()) {
			params.put("ZUOFEISJ", new Date());
			params.put("ZUOFEIREN", kehuxx.getXingming());
			updateCnt = tiaojicfDao.updateStatus(chufangId,new int[]{Status.SAVING.getValue(), Status.RETUNED.getValue()}, params);
		} else if (status == Status.REVOKE.getValue()) {
			params.put("zhuangtai", Status.SAVING.getValue());
			updateCnt = tiaojicfDao.updateStatus(chufangId, new int[]{Status.SUBMITED.getValue(), Status.WAITING_RECEIVE.getValue()}, params);
		} else if (status == Status.SUBMITED.getValue()) {
			final YG_TIAOJICFSBJG tiaojicfsb = new YG_TIAOJICFSBJG();
			final YG_TIAOJICF tiaojicf = tiaojicfDao.getTiaojicfById(chufangId, yiguanId);
			if (tiaojicf == null) {
				rst.put(ChufangConsts.RC, ErrorCode.NO_MATCH_DATA);
				logger.error("chufnagid= " + chufangId + ", yiguanId=" + yiguanId);
				return rst;
			}

			params.put("tijiaosj", new Date());
			params.put("tijiaoren", kehuxx.getXingming());
			tiaojicfsb.setTiaojicfid(chufangId);
			tiaojicfsb.setYiguanid(yiguanId);
			extService.execute(new Runnable() {
				
				@Override
				public void run() {
					try {
						JSONObject ocrRst = ocrService.processImgFile(ossService.getPrivateAccessURL(tiaojicf.getChufangtp()));
						if (ocrRst.getInt(ChufangConsts.RC) == ChufangConsts.OK) {
							tiaojicfsb.setOcrwenben(ocrRst.getString("data"));
							//{"header":{"title":"","type":"","no":"212630","date":"2017年07月09日"},"customer":{"name":"殷粉粉","sex":"女","age":"47","fee_type":"职工在职","client_no":"A22837242","dept":"中医科","address":"浙江中烟工业有限责任公司杭州卷炬","mobile":"13819451520","reason":";结膜炎,肝肾亏虚","medical_record":""},"drugs":[{"name":"柴胡","g":"10"},{"name":"防风","g":"10"},{"name":"青箱子","g":"10"},{"name":"千里光","g":"10"},{"name":"密蒙花","g":"10"},{"name":"枸杞子","g":"15"},{"name":"生白术","g":"30"},{"name":"阳春砂","g":"6"},{"name":"炒枳实","g":""},{"name":"当归","g":"10"},{"name":"决明子","g":"15"},{"name":"浙贝母","g":"15"},{"name":"牡蛎","g":"30"},{"name":"蜜甘草","g":"6"},{"name":"泽泻","g":"10"},{"name":"骨碎补","g":"15"},{"name":"续断","g":"10"},{"name":"杜仲","g":"10"}],"bottom":{"tie":"","wei":"","doctor":""}}
	
							JSONObject parseRst = httpService.processRequest(YUYI_PARSE_HOST, ocrRst.getJSONObject("data"));
							if (parseRst != null) {
								processYuyifenxiJG(parseRst, chufangId, yiguanId, null, tiaojicfsb, tiaojicf.getShouhuodwlx(),
										tiaojicf.getShouhuoren(), tiaojicf.getShouhuolxdh(), tiaojicf.getShouhuodz());
							} else {
								tiaojicfsb.setYuyifxjg("");
								tiaojicfsb.setZhuangtai(0);
							}
							
						} else {
							tiaojicfsb.setOcrwenben(ocrRst.getString(ChufangConsts.ERR_MSG));
							tiaojicfsb.setYuyifxjg("");
							tiaojicfsb.setZhuangtai(0);
						}
						tiaojicfsbDao.newYgTiaoJicfSB(tiaojicfsb);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			params.put("zhuangtai", Status.SUBMITED.getValue());
			updateCnt = tiaojicfDao.updateStatus(chufangId, new int[]{Status.SAVING.getValue()}, params);
		}
		
		if (updateCnt == 0) {
			rst.put(ChufangConsts.RC, ErrorCode.STATUS_NOT_SYNC);
		}
		
		return rst;
	}

	@Override
	public JSONObject getDetailById(long tiaojicfId, long yiguanId) {
		JSONObject rst = generateRst();
		YG_TIAOJICF tiaojicf = tiaojicfDao.getTiaojicfById(tiaojicfId, yiguanId);
		if (tiaojicf == null) {
			rst.put(ChufangConsts.RC, ErrorCode.NO_MATCH_DATA);
			logger.warn("fail to find chufang id = " + tiaojicfId + ", yiguanid = " + yiguanId);
			return rst;
		}
		/**
		 * private String kaifangys;
	private int chufanglx;
	private int fuwushangid;
	private int tiaojifs;
	private int nongjianbz;
	private String tiaojibz;
	private int shouhuodwlx;
	private int genfangbz;
	private String peisongrq;
	private int shangwubz;
	private String shoujianrxm;
	private String lianxidh;
	private String peisongdz;
	private String chufangtp;
	private int zhuangtai;
	private int chufangid = 0;
	private int xinchufangtpbz = 0; //新处方扫描标记
		 */
		rst.put("kaifangys",tiaojicf.getYishengxm());
		rst.put("chufanglx", tiaojicf.getChufanglx());
		rst.put("fuwushangid", tiaojicf.getFuwusid());
		rst.put("tiaojifs", tiaojicf.getTiaojifs());
		rst.put("nongjianbz", tiaojicf.getNongjianbz());
		rst.put("tiaojibz", tiaojicf.getTiaojibz() == null ? "" : tiaojicf.getTiaojibz());
		rst.put("genfangbz", tiaojicf.getGenfangbz());
		rst.put("shouhuodwlx", tiaojicf.getShouhuodwlx()); //個人，公司
		rst.put("peisongrq", tiaojicf.getJihuapsrq() == null ? "" : tiaojicf.getJihuapsrq());
		rst.put("shangwubz", tiaojicf.getJihuapssjfw());
		rst.put("shoujianrenxm", tiaojicf.getShouhuoren());
		rst.put("lianxidh", tiaojicf.getShouhuolxdh());
		rst.put("chufangtp", tiaojicf.getChufangtp() == null ? "" : ossService.getPrivateAccessURL(tiaojicf.getChufangtp()));
		rst.put("zhuangtai", tiaojicf.getZhuangtai());
		rst.put("tiaojicfid", tiaojicfId);
		rst.put("peisongdz", tiaojicf.getShouhuodz());
		
		rst.putAll(extractLiShi(tiaojicf));
		return rst;
	}
	
	private JSONObject extractLiShi(YG_TIAOJICF tiaojicf) {
		JSONObject rst = new JSONObject();
		JSONArray array = new JSONArray();
		JSONObject lishi = new JSONObject();
		lishi.put("zhuangtaims", "保存");
		lishi.put("shijian", DateUtil.dtfen14FromDate(tiaojicf.getChuangjiansj()));
		array.add(lishi);
		
		if (tiaojicf.getZhuangtai() == Status.SUBMITED.getValue()) {
			submit(tiaojicf, array);
		} else if (tiaojicf.getZhuangtai() == Status.ABORTING.getValue()) {
			if (tiaojicf.getTuihuisj() != null) {
				tuihui(tiaojicf, array);
				rst.put("tuihuiyy", tiaojicf.getTuihuiyy());
			}
			tuihui(tiaojicf, array);
			lishi = new JSONObject();
			lishi.put("zhuangtaims", "已作废");
			lishi.put("shijian", DateUtil.dtfen14FromDate(tiaojicf.getTuihuisj()));
			array.add(lishi);
		} else if (tiaojicf.getZhuangtai() == Status.RETUNED.getValue()) {
			submit(tiaojicf, array);
			tuihui(tiaojicf, array);
		} else if (tiaojicf.getZhuangtai() == Status.WAITING_RECEIVE.getValue()) {
			submit(tiaojicf, array);
			shenhe(tiaojicf,array);
		} else if (tiaojicf.getZhuangtai() == Status.ADJUST.getValue()) {
			submit(tiaojicf, array);
			shenhe(tiaojicf,array);
			jieshou(tiaojicf,array);
		} else if (tiaojicf.getZhuangtai() == Status.DONE.getValue()) {
			submit(tiaojicf, array);
			shenhe(tiaojicf, array);
			jieshou(tiaojicf, array);
			wancheng(tiaojicf, array);
		}
		rst.put("zhuangtails", array);
		
		return rst;
	}
	
	private void submit(YG_TIAOJICF tiaojicf, JSONArray array) {
		if (tiaojicf.getTijiaosj() != null) {
			JSONObject lishi = new JSONObject();
			lishi.put("zhuangtaims", "已提交");
			lishi.put("shijian", DateUtil.dtfen14FromDate(tiaojicf.getTijiaosj()));
			array.add(lishi);
		}
	}
	
	
	
	private void tuihui(YG_TIAOJICF tiaojicf, JSONArray array) {
		
		if (tiaojicf.getTuihuisj() != null) {
			JSONObject lishi = new JSONObject();
			lishi.put("zhuangtaims", "已退回");
			lishi.put("shijian", DateUtil.dtfen14FromDate(tiaojicf.getTuihuisj()));
			lishi.put("tuihuiyy", tiaojicf.getTuihuiyy());
			array.add(lishi);
		}
		
	}
	
	private void shenhe(YG_TIAOJICF tiaojicf, JSONArray array) {
		if (tiaojicf.getShenhesj() == null) {
			JSONObject lishi = new JSONObject();
			lishi.put("zhuangtaims", "已审核");
			lishi.put("shijian", DateUtil.dtfen14FromDate(tiaojicf.getShenhesj()));
			array.add(lishi);
		}
	}
	
	private void jieshou(YG_TIAOJICF tiaojicf, JSONArray array) {
		if (tiaojicf.getJieshousj() != null) {
			JSONObject lishi = new JSONObject();
			lishi.put("zhuangtaims", "调剂中");
			lishi.put("shijian", DateUtil.dtfen14FromDate(tiaojicf.getJieshousj()));
			array.add(lishi);
		}
	}
	
	private void wancheng(YG_TIAOJICF tiaojicf, JSONArray array) {
		if (tiaojicf.getWanchengsj() != null) {
			JSONObject lishi = new JSONObject();
			lishi.put("zhuangtaims", "已完成");
			lishi.put("shijian", DateUtil.dtfen14FromDate(tiaojicf.getWanchengsj()));
			array.add(lishi);
		}
	}

	@Override
	public JSONObject getDianziCf(long tiaojicfId, long yiguanId) {
		JSONObject rst = generateRst();
		YG_TIAOJICF tiaojicf = tiaojicfDao.getTiaojicfById(tiaojicfId, yiguanId);
		if (tiaojicf == null) {
			rst.put(ChufangConsts.RC, ErrorCode.NO_MATCH_DATA);
			return rst;
		}
		rst.put("chufangbh", tiaojicf.getChufangbh() == null ? "" : tiaojicf.getChufangbh());
		rst.put("menzhengblh", tiaojicf.getMenzhenblh() == null ? "" : tiaojicf.getMenzhenblh());
		rst.put("xingming", tiaojicf.getHuanzhexm());
		rst.put("xingbie", tiaojicf.getXingbie());
		rst.put("nianling", tiaojicf.getNianling());
		rst.put("lianxidh", tiaojicf.getHuanzhelxdh() == null ? "" : tiaojicf.getHuanzhelxdh() );
		rst.put("jiuzhenrq", tiaojicf.getJiuzhenrq() == null ? "" : DateUtil.dt14LongFormat(tiaojicf.getJiuzhenrq()));
		rst.put("jiuzhenks", tiaojicf.getKeshimc() == null ? "" :  tiaojicf.getKeshimc());
		rst.put("feiyonglb", tiaojicf.getFeiyonglb() == null ? "" : tiaojicf.getFeiyonglb());
		rst.put("dizhi", tiaojicf.getHuanzhelxdz() == null ? "" : tiaojicf.getHuanzhelxdz());
		rst.put("zhenduan", tiaojicf.getZhenduan() == null ? "" : tiaojicf.getZhenduan());
		rst.put("jishu", tiaojicf.getTieshu() == 0 ? "" : tiaojicf.getTieshu());
		rst.put("yisheng", tiaojicf.getYishengxm());
		rst.put("beizhu", tiaojicf.getTiaojibz());
		rst.put("yongyaopl", tiaojicf.getYongyaopl());
		rst.put("weishu", tiaojicf.getWeishu());
		rst.put("taiojifs", tiaojicf.getTiaojifs());
		rst.put("nianlingdw", tiaojicf.getNianlingdw());
		
		List<YG_TIAOJICFYP> ypList = tiaojicfypDao.getYaopinList(tiaojicfId, yiguanId);
		JSONArray array = new JSONArray();
		JSONObject yp = null;
		for (YG_TIAOJICFYP yaopin : ypList) {
			yp = new JSONObject();
			if (StringUtils.isNotEmpty(yaopin.getYaopintjbz())) {
				yp.put("mingcheng", yaopin.getYaopinmc() + "(" + yaopin.getYaopintjbz() + ")");
			} else {
				yp.put("mingcheng", yaopin.getYaopinmc());
			}
			yp.put("shuliang", NumberUtil.formatYuan(yaopin.getShuliang()) + yaopin.getJiliangdw());
			array.add(yp);
		}
		
		rst.put("yaopinlb", array);
		return rst;
		
	}

	public JSONObject getDianziCfBK(long tiaojicfId, long yiguanId) {
		JSONObject rst = generateRst();
		YG_TIAOJICFSBJG tiaojicfSBJG = tiaojicfsbDao.getTiaojisbJG(tiaojicfId, yiguanId);
		if (tiaojicfSBJG == null) {
			rst.put(ChufangConsts.RC, ErrorCode.NO_MATCH_DATA);
			return rst;
		}
		String sbjg = tiaojicfSBJG.getYuyifxjg();
		if (StringUtils.isEmpty(sbjg)) {
			rst.put(ChufangConsts.RC, ErrorCode.YUYI_FENXI_FAIL);
			return rst;
		}
		try {
			JSONObject data = JSONObject.fromObject(sbjg);
			if (data.containsKey("header")) {
				setDianziField("chufangbh","no", data.getJSONObject("header"), rst);
				setDianziField("menzhengblh","menzhengblh", data.getJSONObject("header"), rst);
				setDianziField("jiuzhenrq","date", data.getJSONObject("header"), rst);
			}
			if (data.containsKey("customer")) {
				setDianziField("xingming","name", data.getJSONObject("customer"), rst);
				setDianziField("xingbie","sex", data.getJSONObject("customer"), rst);
				setDianziField("nianning","age", data.getJSONObject("customer"), rst);
				setDianziField("lianxidh","mobile", data.getJSONObject("customer"), rst);
				setDianziField("dizhi","address", data.getJSONObject("customer"), rst);
				setDianziField("zhenduan","reason", data.getJSONObject("customer"), rst);
				setDianziField("feiyonglb","fee_type", data.getJSONObject("customer"), rst);
			}
			if (data.containsKey("drugs")) {
				JSONArray drugs = data.getJSONArray("drugs");
				rst.put("drugs", drugs);
				/**
				 * "drugs": [{"name": "柴胡", "g": "10"}, {"name": "青箱子", "g": ""}, 
					{"name": "千里光", "g": "10"}, {"name": "密蒙花", "g": "10"}]
				 */
			}
			if (data.containsKey("bottom")) {
				setDianziField("wei","wei", data.getJSONObject("bottom"), rst);
				setDianziField("tie","tie", data.getJSONObject("bottom"), rst);
			}
		} catch (Exception e) {
			logger.error("fail to extract txt=" + sbjg);
			rst.put(ChufangConsts.RC, ErrorCode.YUYI_FENXI_FAIL);
			return rst;
		}
		return rst;
	}
	
	private void setDianziField(String key, String dataKey, JSONObject data, JSONObject rst) {
		if (data.containsKey(dataKey)) {
			rst.put(key, data.getString(dataKey));
		} else {
			rst.put(key, "");
		}
	}

	@Override
	public JSONObject queryLatestAddress(String shouji, String xingming) {
		JSONObject rst = generateRst();
		rst.put("peisongdz", "");
		String address = tiaojicfDao.getLatestAddress(shouji, xingming);
		if (address != null) {
			rst.put("peisongdz", address);
		}
		return rst;
	}

	@Override
	public JSONObject getSaveToken(HttpServletRequest request) {
		request.getSession().setAttribute(ChufangConsts.SAVE_TOKEN_KEY, StringUtil.getRandomString(10));
		return generateRst();
	}

	@Override
	public JSONObject getYongyaopl() {
		JSONObject rst = generateRst();
		return rst;
	}

	@Override
	public JSONObject getYongfa() {
		JSONObject rst = generateRst();
		return rst;
	}
}
