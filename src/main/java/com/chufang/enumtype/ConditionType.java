package com.chufang.enumtype;

public enum ConditionType {
	EQ("="),
	LIKE("like"),
	GT(">="),
	LT("<="),
	LL("<"),
	GG(">"),
	IN("in"),
	OR("or");
	
	public String getType() {
		return type;
	}
	private String type;
	
	ConditionType(String type) {
		this.type = type;
	}
}
