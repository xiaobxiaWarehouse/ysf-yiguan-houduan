package com.chufang.enumtype;

public enum PeiSongMB {
	GEREN("个人", 1),
	YIGUAN("医馆", 2);
	
	private String name;
	private int val;
	
	PeiSongMB(String name, int value) {
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
		PeiSongMB[] lxs = PeiSongMB.values();
		for (PeiSongMB lx : lxs) {
			if (lx.getValue() == val) {
				return lx.getDesc();
			}
		}
		return "" + val;
	}
}
