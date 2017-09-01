package com.chufang.entity;

import com.chufang.annotation.Table;
import java.util.Date;

@Table("ysf_yg_tiaojicfsbjg")
public class YG_TIAOJICFSBJG {
	private long tiaojicfid;

	private long yiguanid;

	private String ocrwenben;

	private String yuyifxjg;

	private double wanzhengdu;

	private String jiaoduijy;

	private Date kaishisj;

	private Date jieshusj;

	private int zhuangtai;

	private String shibaiyy;

	public long getTiaojicfid() {
		return this.tiaojicfid;
	}

	public void setTiaojicfid(long tiaojicfid) {
		this.tiaojicfid = tiaojicfid;
	}
	public long getYiguanid() {
		return this.yiguanid;
	}

	public void setYiguanid(long yiguanid) {
		this.yiguanid = yiguanid;
	}
	public String getOcrwenben() {
		return this.ocrwenben;
	}

	public void setOcrwenben(String ocrwenben) {
		this.ocrwenben = ocrwenben;
	}
	public String getYuyifxjg() {
		return this.yuyifxjg;
	}

	public void setYuyifxjg(String yuyifxjg) {
		this.yuyifxjg = yuyifxjg;
	}
	public double getWanzhengdu() {
		return this.wanzhengdu;
	}

	public void setWanzhengdu(double wanzhengdu) {
		this.wanzhengdu = wanzhengdu;
	}
	public String getJiaoduijy() {
		return this.jiaoduijy;
	}

	public void setJiaoduijy(String jiaoduijy) {
		this.jiaoduijy = jiaoduijy;
	}
	public Date getKaishisj() {
		return this.kaishisj;
	}

	public void setKaishisj(Date kaishisj) {
		this.kaishisj = kaishisj;
	}
	public Date getJieshusj() {
		return this.jieshusj;
	}

	public void setJieshusj(Date jieshusj) {
		this.jieshusj = jieshusj;
	}
	public int getZhuangtai() {
		return this.zhuangtai;
	}

	public void setZhuangtai(int zhuangtai) {
		this.zhuangtai = zhuangtai;
	}
	public String getShibaiyy() {
		return this.shibaiyy;
	}

	public void setShibaiyy(String shibaiyy) {
		this.shibaiyy = shibaiyy;
	}
}