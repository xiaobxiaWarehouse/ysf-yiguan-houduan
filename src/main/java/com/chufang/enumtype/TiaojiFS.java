package com.chufang.enumtype;

public enum TiaojiFS {
	JIANZHU("煎煮", 1),
	ZHIGAO("熬膏", 2),
	ZHIWAN("制丸", 3),
	DAFEN("打粉", 4),
	WAIPEI("外配", 9);
	
	private String name;
	private int val;
	
	TiaojiFS(String name, int value) {
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
		TiaojiFS[] lxs = TiaojiFS.values();
		for (TiaojiFS lx : lxs) {
			if (lx.getValue() == val) {
				return lx.getDesc();
			}
		}
		return "" + val;
	}
}
