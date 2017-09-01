package com.chufang.service.impl;

import java.util.Arrays;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chufang.consts.ChufangConsts;
import com.chufang.consts.ErrorCode;
import com.chufang.entity.PT_KEHUXX;
import com.chufang.entity.Page;
import com.chufang.entity.YG_YUANGONGXX;
import com.chufang.entity.ext.YuangongExt1;
import com.chufang.entity.ext.YuangongExt2;
import com.chufang.parameter.YuangongParam;
import com.chufang.repository.PT_KEHUXXDao;
import com.chufang.repository.YG_YUANGONGXXDao;
import com.chufang.service.YuangongService;

import net.sf.json.JSONObject;

@Service
public class YuangongServiceImpl extends CommonService implements YuangongService {
	private final static Logger logger = LoggerFactory.getLogger(YuangongServiceImpl.class);
	
	@Resource
	private YG_YUANGONGXXDao yuanggongDao;
	
	@Resource
	private PT_KEHUXXDao kehuDao;
	
	@Override
	public JSONObject query(long yiguanId, String shouji, int zhuangtai, int pageNo) {
		JSONObject rst = generateRst();
		Page<YuangongExt1> page = new Page<YuangongExt1>();
		page.setPageNo(pageNo);
		yuanggongDao.page(page, yiguanId, shouji, zhuangtai);
		
		rst.put("data", page.getData());
		rst.put("page", generatePage(page.getPageNo(), page.getTotalCount(), page.getTotalPage()));
		
		return rst;
	}

	@Override
	@Transactional
	public JSONObject save(long yiguanId, YuangongParam yuangong) {
		JSONObject rst = generateRst();
		
//		if (kehuxx != null && kehuxx.getId() != yuangong.getYuangongid()) {
//			rst.put(ChufangConsts.RC, ErrorCode.ACCOUNT_NOT_EXIST);
//			return rst;
//		}
		YG_YUANGONGXX ygYonghu = new YG_YUANGONGXX();
		ygYonghu.setGangwei(yuangong.getGangwei());
		if (("," + yuangong.getQuanxian() + ",").contains(",1,")) {
			ygYonghu.setGuanliybz(1);
		} else {
			ygYonghu.setGuanliybz(0);
		}
		ygYonghu.setQuanxian(yuangong.getQuanxian());
		ygYonghu.setYiguanid(yiguanId);
		ygYonghu.setXingming(yuangong.getXingming());
		ygYonghu.setJiarusj(new Date());
		ygYonghu.setJinyongbz(yuangong.getJinyongbz());
		ygYonghu.setGangwei(yuangong.getGangwei());
		if (yuangong.getYuangongid() == 0) {
			PT_KEHUXX kehuxx = kehuDao.getKehuXXByShouJi(yuangong.getShoujihm());
			if (kehuxx == null) {
				logger.info("new yuangong....yiguanId=" + yiguanId);
				kehuxx = new PT_KEHUXX();
				kehuxx.setXingming(yuangong.getXingming());
				kehuxx.setMima(DigestUtils.md5Hex(yuangong.getShoujihm()));
				kehuxx.setShoujihm(Long.parseLong(yuangong.getShoujihm()));
				long kehuId = kehuDao.newKehuxx(kehuxx, Arrays.asList(new String[]{"shoujihm","mima","xingming"}));
				ygYonghu.setKehuid(new Long(kehuId).intValue());
			} else {
				YG_YUANGONGXX ygxx = yuanggongDao.getYgYonghuByCustId(kehuxx.getId(), yiguanId);
				if (ygxx != null) {
					rst.put(ChufangConsts.RC, ErrorCode.ACCOUNT_EXISTS);
					return rst;
				}
				ygYonghu.setKehuid(kehuxx.getId());
			}
			yuanggongDao.newYgYonghu(ygYonghu);
		} else {
			
			YG_YUANGONGXX tmpYuangongxx = yuanggongDao.getYgYonghuByCustId(yuangong.getYuangongid(), yiguanId);
			if (tmpYuangongxx == null) {
				rst.put(ChufangConsts.RC, ErrorCode.NO_MATCH_DATA);
				logger.info("fail to find yuangognid= " + yuangong.getYuangongid() + ",yiguanId=" + yiguanId);
				return rst;
			}
			ygYonghu.setId(yuangong.getYuangongid());
			kehuDao.updateBaseInfoById(tmpYuangongxx.getKehuid(), yuangong.getShoujihm(), 
					yuangong.getXingming(), 0, null);
			ygYonghu.setId(tmpYuangongxx.getId());
			int updateRst = yuanggongDao.updateById(ygYonghu);
			if (updateRst == -1) {
				rst.put(ChufangConsts.RC, ChufangConsts.FAIL);
			}
			logger.info("update yuangong....yiguanId=" + yiguanId + ".rst=" + updateRst );
		}
		
		return rst;
	}

	@Override
	public JSONObject updateZhuangTai(long yiguanId, int id, int zhuangtai) {
		JSONObject rst = generateRst();
		YuangongExt2 yuangongext2 = yuanggongDao.getYuangongExt2ById(yiguanId, id);
		if (yuangongext2 == null) {
			rst.put(ChufangConsts.RC, ErrorCode.NO_MATCH_DATA);
			return rst;
		}
		if (zhuangtai != 1 && zhuangtai != 0) {
			rst.put(ChufangConsts.RC, ErrorCode.INVALID_PARAM);
			return rst;
		}
		
		yuanggongDao.updateJinyongBZ(yiguanId, id, zhuangtai);
		
		return rst;
	}
	
	@Override
	public JSONObject getById(long yiguanId, long id) {
		JSONObject rst = generateRst();
		YuangongExt2 yuangongext2 = yuanggongDao.getYuangongExt2ById(yiguanId, id);
		if (yuangongext2 == null) {
			rst.put(ChufangConsts.RC, ErrorCode.NO_MATCH_DATA);
			return rst;
		}
		rst.putAll(JSONObject.fromObject(yuangongext2));
		return rst;
	}

	@Override
	public JSONObject getSelfXinxi(long id) {
		JSONObject rst = generateRst();
		PT_KEHUXX kehu = kehuDao.getKehuxxById(id);
		rst.put("shoujihm", kehu.getShoujihm());
		rst.put("xingbie", kehu.getXingbie());
		rst.put("xingming", kehu.getXingming());
		return rst;
	}
	
	@Override
	public JSONObject getKaifangys(long yiguanId, int isall) {
		JSONObject rst = generateRst();
		rst.put("data", yuanggongDao.getKaifangYs(yiguanId, isall));
		return rst;
	}

}
