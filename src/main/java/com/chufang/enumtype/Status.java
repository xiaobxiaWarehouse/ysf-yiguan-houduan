package com.chufang.enumtype;

public enum Status {
	SAVING("保存", 1),
	SUBMITED("已提交", 2),
	WAITING_RECEIVE("待接收", 3),
	ADJUST("调剂中", 4),
	RETUNED("已退回", 5),
	ABORTING("已作废", 6),
	DONE("已完成", 7),
	REVOKE("撤销", 100),
	;
	
	private String name;
	private int val;
	
	Status(String name, int value) {
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
		Status[] statuses = Status.values();
		for (Status status : statuses) {
			if (status.getValue() == val) {
				return status.getDesc();
			}
		}
		return "" + val;
	}
}
