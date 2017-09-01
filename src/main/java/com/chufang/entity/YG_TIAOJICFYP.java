package com.chufang.entity;

import com.chufang.annotation.Table;
import com.chufang.annotation.ID;
@Table("ysf_yg_tiaojicfyp")
public class YG_TIAOJICFYP {
	@ID
	private long id;

	private long yiguanid;

	private long tiaojicfid;

	private String yaopinmc;

	private double shuliang;

	private String jiliangdw;

	private String yaopintjbz;

	private int yiqubz;

	private double guolingjia;

	private double hanshuipj;

	private double chufangjia;

	private double jiaoyijia;

	private long fuwustjspid;

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public long getYiguanid() {
		return this.yiguanid;
	}

	public void setYiguanid(long yiguanid) {
		this.yiguanid = yiguanid;
	}
	public long getTiaojicfid() {
		return this.tiaojicfid;
	}

	public void setTiaojicfid(long tiaojicfid) {
		this.tiaojicfid = tiaojicfid;
	}
	public String getYaopinmc() {
		return this.yaopinmc;
	}

	public void setYaopinmc(String yaopinmc) {
		this.yaopinmc = yaopinmc;
	}
	public double getShuliang() {
		return this.shuliang;
	}

	public void setShuliang(double shuliang) {
		this.shuliang = shuliang;
	}
	public String getJiliangdw() {
		return this.jiliangdw;
	}

	public void setJiliangdw(String jiliangdw) {
		this.jiliangdw = jiliangdw;
	}
	public String getYaopintjbz() {
		return this.yaopintjbz;
	}

	public void setYaopintjbz(String yaopintjbz) {
		this.yaopintjbz = yaopintjbz;
	}
	public int getYiqubz() {
		return this.yiqubz;
	}

	public void setYiqubz(int yiqubz) {
		this.yiqubz = yiqubz;
	}
	public double getGuolingjia() {
		return this.guolingjia;
	}

	public void setGuolingjia(double guolingjia) {
		this.guolingjia = guolingjia;
	}
	public double getHanshuipj() {
		return this.hanshuipj;
	}

	public void setHanshuipj(double hanshuipj) {
		this.hanshuipj = hanshuipj;
	}
	public double getChufangjia() {
		return this.chufangjia;
	}

	public void setChufangjia(double chufangjia) {
		this.chufangjia = chufangjia;
	}
	public double getJiaoyijia() {
		return this.jiaoyijia;
	}

	public void setJiaoyijia(double jiaoyijia) {
		this.jiaoyijia = jiaoyijia;
	}
	public long getFuwustjspid() {
		return this.fuwustjspid;
	}

	public void setFuwustjspid(long fuwustjspid) {
		this.fuwustjspid = fuwustjspid;
	}
}