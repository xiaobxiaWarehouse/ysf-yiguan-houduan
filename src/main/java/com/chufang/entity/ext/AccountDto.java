package com.chufang.entity.ext;

public class AccountDto {
	private String shouji;
	private String xingming;
	private String gangwei;
	private int juese;
	private int jingrongbz;
	private int xingbie = 0;

	public int getXingbie() {
		return xingbie;
	}
	public void setXingbie(int xingbie) {
		this.xingbie = xingbie;
	}
	public String getShouji() {
		return shouji;
	}
	public void setShouji(String shouji) {
		this.shouji = shouji;
	}
	public String getXingming() {
		return xingming;
	}
	public void setXingming(String xingming) {
		this.xingming = xingming;
	}
	public String getGangwei() {
		return gangwei;
	}
	public void setGangwei(String gangwei) {
		this.gangwei = gangwei;
	}

	public int getJuese() {
		return juese;
	}
	public void setJuese(int juese) {
		this.juese = juese;
	}
	public int getJingrongbz() {
		return jingrongbz;
	}
	public void setJingrongbz(int jingrongbz) {
		this.jingrongbz = jingrongbz;
	}
}
