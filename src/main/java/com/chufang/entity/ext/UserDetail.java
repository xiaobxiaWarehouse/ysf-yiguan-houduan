package com.chufang.entity.ext;

public class UserDetail {
	private long id;
	private String quanxian;
	private int admin;
	private long yiguanId = 0;
	private String xingming;
	
	public UserDetail(long id, String quanxian, int admin, long yiguanId, String xingming) {
		this.id = id;
		this.quanxian = quanxian;
		this.admin = admin;
		this.yiguanId = yiguanId;
		this.xingming = xingming;
	}
	
	public String getXingming() {
		return xingming;
	}

	public long getYiguanId() {
		return yiguanId;
	}

	public long getId() {
		return id;
	}

	public String getQuanxian() {
		return quanxian;
	}

	public void setQuanxian(String quanxian) {
		this.quanxian = quanxian;
	}

	public int getAdmin() {
		return admin;
	}

	public void setAdmin(int admin) {
		this.admin = admin;
	}

}
