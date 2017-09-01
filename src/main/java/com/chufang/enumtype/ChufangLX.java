package com.chufang.enumtype;

public enum ChufangLX {
	TANGJI("汤剂", 1),
	GAOJI("膏剂", 2),
	WANJI("丸剂", 3),
	SANJI("散剂", 4),
	ZHONGYAOPFKLJ("中药配方颗粒", 9);
	/**
	 * 1-汤剂
2-膏剂
3-丸剂
4-散剂
9-中药配方颗粒
	 */
	
	private String name;
	private int val;
	
	ChufangLX(String name, int value) {
		this.name = name;
		this.val = value;
	}
	
	public int getValue() {
		return this.val;
	}
	
	public String getDesc() {
		return this.name;
	}
	
	public String getNameByValue(int val) {
		ChufangLX[] lxs = ChufangLX.values();
		for (ChufangLX lx : lxs) {
			if (lx.getValue() == val) {
				return lx.getDesc();
			}
		}
		return "" + val;
	}
}
