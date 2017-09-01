package com.chufang.test.service;

import javax.annotation.Resource;

import org.junit.Test;

import com.chufang.service.FuwushangService;
import com.chufang.test.BaseTest;

public class FuwushangTest extends BaseTest {
	@Resource
	private FuwushangService fuwushangService;
	
	@Test
	public void query() {
		System.out.println(fuwushangService.queryFuwushangList(1, 1));
	}
	
//	@Test
	public void testGet() {
		System.out.println(fuwushangService.loadFuwushangById(1, 1));
	}
}
