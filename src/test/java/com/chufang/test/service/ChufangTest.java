package com.chufang.test.service;

import javax.annotation.Resource;

import org.junit.Test;

import com.chufang.enumtype.Status;
import com.chufang.parameter.ChufangParam;
import com.chufang.parameter.TiaoJiSearchParam;
import com.chufang.service.YG_TiaoJiCFService;
import com.chufang.test.BaseTest;

public class ChufangTest extends BaseTest {

	@Resource
	private YG_TiaoJiCFService tiaojiCfService;

	
//	@Test
	public void testQuery() {
		TiaoJiSearchParam para = new TiaoJiSearchParam();
		para.setPageNo(1);
		para.setKaishirq("2017-08-17");
		para.setJiezhirq("2017-08-17");
		para.setStatus(1);
		System.out.println(tiaojiCfService.queryTiaoJiCf(para, 1));
	}
	
//	@Test
	public void testStatics() {
		System.out.println(tiaojiCfService.queryStatics(7));
	}
	
//	@Test
	public void testUpdateStatus() {
		System.out.println(tiaojiCfService.updateTiaojiCFStatus(1, Status.REVOKE.getValue(), 1L));
	}
	
//	@Test
	public void testDetail() {
		System.out.println(tiaojiCfService.getDetailById(1, 1));
	}
	
//	@Test
	public void testLstAddress() {
		System.out.println(tiaojiCfService.queryLatestAddress("2", "3"));
	}
	
//	@Test
	public void testGetDianziCf() {
		System.out.println(tiaojiCfService.getDianziCf(1, 1));
	}
	
//	@Test
	public void testSave() throws Exception {
		ChufangParam chufang = new ChufangParam();
		chufang.setTiaojicfid(0L);
		chufang.setChufanglx(1);
		chufang.setChufangtp("7ba96364c24440f1849591652eca23c9.png");
		chufang.setFuwushangid(1);
		chufang.setKaifangys("王医生2");;
		chufang.setGenfangbz(1);
		chufang.setLianxidh("1356565441");;
		chufang.setNongjianbz(1);
		chufang.setPeisongdz("这件打蜡科技时代fa");
		chufang.setPeisongrq("2017-09-08");
		chufang.setShangwubz(1);
		chufang.setShouhuodwlx(2);
		chufang.setShoujianrxm("来说的");
		chufang.setTiaojibz("不要太冷");
		chufang.setTiaojifs(2);
		chufang.setXinchufangtpbz(0);
		chufang.setZhuangtai(Status.SAVING.getValue());
		System.out.println(tiaojiCfService.saveTiaojiCf(chufang, 1, 1L));
		Thread.currentThread().sleep(1000*5);
	}
}
