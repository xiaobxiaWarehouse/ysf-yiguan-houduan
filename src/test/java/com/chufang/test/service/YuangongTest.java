package com.chufang.test.service;

import javax.annotation.Resource;

import org.junit.Test;

import com.chufang.parameter.YuangongParam;
import com.chufang.service.YuangongService;
import com.chufang.test.BaseTest;

public class YuangongTest extends BaseTest {

	@Resource
	private YuangongService yuangongService;
	
//	@Test
	public void testQuery() {
		System.out.println(yuangongService.query(1, "", -1, 1));
	}
	
//	@Test
	public void testGet() {
		System.out.println(yuangongService.getById(1, 1));
	}
	
//	@Test
	public void testUpdate() {
		System.out.println(yuangongService.updateZhuangTai(1, 222, 0));
	}
	
	@Test
	public void testSave() {
		YuangongParam yuangong = new YuangongParam();
		yuangong.setGangwei(1);
		yuangong.setJinyongbz(0);
		yuangong.setQuanxian("管理员");
		yuangong.setShoujihm("13575775451");
		yuangong.setXingming("wewe");
		System.out.println(yuangongService.save(1, yuangong));
	}
}
