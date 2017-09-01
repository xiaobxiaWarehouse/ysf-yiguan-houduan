package com.chufang.test.service;

import javax.annotation.Resource;

import org.junit.Test;

import com.chufang.entity.YG_YIGUANXX;
import com.chufang.service.YiGuanService;
import com.chufang.test.BaseTest;

public class YiguanTest extends BaseTest{
	
	@Resource
	private YiGuanService yiguanService;
	
//	@Test
	public void testGet() {
		System.out.println(yiguanService.queryYGXXById(1));
	}
	
	@Test
	public void testSave() {
		YG_YIGUANXX yiguanxx = new YG_YIGUANXX();
		yiguanxx.setId(1L);
		yiguanxx.setShengfen("addd");
		yiguanxx.setChengshi("as");
		yiguanxx.setQuxian("as33233");
		yiguanxx.setJiedaodz("收来打发");
		yiguanxx.setMingcheng("不告诉你");
		yiguanxx.setJiancheng("232323");
		yiguanxx.setLianxidh("11121212");
		yiguanxx.setLianxiren("whl");
		yiguanxx.setTangjimrfwsid(2);
		yiguanxx.setWanjimrfwsid(2);
		yiguanxx.setGaojimrfwsid(2);
		yiguanxx.setSanjimrfwsid(2);
		yiguanxx.setZhongyaopfkljmrfwsid(0);
		System.out.println(yiguanService.updateYGXX(yiguanxx));
	}
}
