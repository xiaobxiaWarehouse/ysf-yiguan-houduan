package com.chufang.parameter;

public class TiaoJiSearchParam {
	private String kaishirq;
	private String jiezhirq;
	private long fuwushangid = 0;
	private int status = -1;
	private int pageNo =1;
	private String phone;
	private int isall = 0;
	public int getIsall() {
		return isall;
	}
	public void setIsall(int isall) {
		this.isall = isall;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	private String chufangbh;
	
	public String getKaishirq() {
		return kaishirq;
	}
	public void setKaishirq(String kaishirq) {
		this.kaishirq = kaishirq;
	}
	public String getJiezhirq() {
		return jiezhirq;
	}
	public void setJiezhirq(String jiezhirq) {
		this.jiezhirq = jiezhirq;
	}
	public long getFuwushangid() {
		return fuwushangid;
	}
	public void setFuwushangid(long fuwushangid) {
		this.fuwushangid = fuwushangid;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public String getChufangbh() {
		return chufangbh;
	}
	public void setChufangbh(String chufangbh) {
		this.chufangbh = chufangbh;
	}
}
