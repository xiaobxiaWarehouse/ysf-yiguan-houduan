package com.chufang.test.service;

import javax.annotation.Resource;

import org.junit.Test;

import com.chufang.service.DuizhangService;
import com.chufang.test.BaseTest;

public class DuizhangTest extends BaseTest {

	@Resource
	private DuizhangService duizhangService;
	
//	@Test
	public void testDuizhang() {
		System.out.println(duizhangService.query(1, "", "", 1));
	}
	
//	@Test
	public void testSummary() {
		System.out.println(duizhangService.querySummary(1, 1, "2017-07-01", "2017-09-01"));
	}
	
	@Test
	public void testPageYaopin() {
		long yiguanId = 1L;
		long fuwushangId = 1L;
		System.out.println(duizhangService.pageYaopin(yiguanId, "2017-07-01", "2017-09-01", fuwushangId , -1, 1));
	}
}
