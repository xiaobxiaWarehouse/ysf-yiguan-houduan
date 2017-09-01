package com.chufang.entity.ext;

public class YaopinExt {
	private String mingcheng;
	private double shuliang;
	private double guolingjia;
	private double hansuipj;
	private double jiaoyijia;
	private double chufangjia;
	
	public double getChufangjia() {
		return chufangjia;
	}
	public void setChufangjia(double chufangjia) {
		this.chufangjia = chufangjia;
	}
	public String getMingcheng() {
		return mingcheng;
	}
	public void setMingcheng(String mingcheng) {
		this.mingcheng = mingcheng;
	}
	public double getShuliang() {
		return shuliang/1000;
	}
	public void setShuliang(double shuliang) {
		this.shuliang = shuliang;
	}
	public double getGuolingjia() {
		return guolingjia;
	}
	public void setGuolingjia(double guolingjia) {
		this.guolingjia = guolingjia;
	}
	public double getHansuipj() {
		return hansuipj;
	}
	public void setHansuipj(double hansuipj) {
		this.hansuipj = hansuipj;
	}
	public double getJiaoyijia() {
		return jiaoyijia;
	}
	public void setJiaoyijia(double jiaoyijia) {
		this.jiaoyijia = jiaoyijia;
	}
}
