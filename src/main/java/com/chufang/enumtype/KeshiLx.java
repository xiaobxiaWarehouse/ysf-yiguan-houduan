package com.chufang.enumtype;

public enum KeshiLx {
	QUANKE("全科", 1),
	NEIKE("内科", 2),
	ERKE("儿科", 3),
	FUKE("妇科", 4),
	YANKE("眼科", 5),
	ERBIHOUKE("耳鼻喉科", 6),
	KOUQIANGKE("口腔科", 7),
	PIFUKE("皮肤科", 8),
	ZHONGYIKE("中医科", 9),
	ZHENNJIUKE("针灸推拿科", 10),
	MENZHEN("门诊药房", 11),
	SHOUFEISHI("收费室", 12),
	WAIKE("外科", 13),
	;
	
	private String name;
	private int val;
	
	KeshiLx(String name, int value) {
		this.name = name;
		this.val = value;
	}
	
	public int getValue() {
		return this.val;
	}
	
	public String getDesc() {
		return this.name;
	}
	
	public String getDescByVal(int val) {
		KeshiLx[] keshis = KeshiLx.values();
		for (KeshiLx lx : keshis) {
			if (lx.getValue() == val) {
				return lx.getDesc();
			}
		}
		return "" + val;
	}
}
