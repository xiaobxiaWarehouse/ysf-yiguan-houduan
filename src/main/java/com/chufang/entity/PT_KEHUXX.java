package com.chufang.entity;

import com.chufang.annotation.Table;
import com.chufang.annotation.ID;
import java.util.Date;

@Table("ysf_pt_kehuxx")
public class PT_KEHUXX {
	@ID
	private int id;

	private String uuid;

	private long shoujihm;

	private String mima;

	private String xingming;

	private String nicheng;

	private String touxianguri;

	private int xingbie;

	private Date chushengrq;

	private int zhuangtai;

	private Date zhucesj;

	private int mimacwcs;

	private Date zuihoudlsj;

	private long denglucs;

	private String zuihoudlip;

	private String zuihoudlsf;

	private String zuihoudlcs;

	private String zuihoudlqx;

	private String beizhu;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public long getShoujihm() {
		return this.shoujihm;
	}

	public void setShoujihm(long shoujihm) {
		this.shoujihm = shoujihm;
	}
	public String getMima() {
		return this.mima;
	}

	public void setMima(String mima) {
		this.mima = mima;
	}
	public String getXingming() {
		return this.xingming;
	}

	public void setXingming(String xingming) {
		this.xingming = xingming;
	}
	public String getNicheng() {
		return this.nicheng;
	}

	public void setNicheng(String nicheng) {
		this.nicheng = nicheng;
	}
	public String getTouxianguri() {
		return this.touxianguri;
	}

	public void setTouxianguri(String touxianguri) {
		this.touxianguri = touxianguri;
	}
	public int getXingbie() {
		return this.xingbie;
	}

	public void setXingbie(int xingbie) {
		this.xingbie = xingbie;
	}
	public Date getChushengrq() {
		return this.chushengrq;
	}

	public void setChushengrq(Date chushengrq) {
		this.chushengrq = chushengrq;
	}
	public int getZhuangtai() {
		return this.zhuangtai;
	}

	public void setZhuangtai(int zhuangtai) {
		this.zhuangtai = zhuangtai;
	}
	public Date getZhucesj() {
		return this.zhucesj;
	}

	public void setZhucesj(Date zhucesj) {
		this.zhucesj = zhucesj;
	}
	public int getMimacwcs() {
		return this.mimacwcs;
	}

	public void setMimacwcs(int mimacwcs) {
		this.mimacwcs = mimacwcs;
	}
	public Date getZuihoudlsj() {
		return this.zuihoudlsj;
	}

	public void setZuihoudlsj(Date zuihoudlsj) {
		this.zuihoudlsj = zuihoudlsj;
	}
	public long getDenglucs() {
		return this.denglucs;
	}

	public void setDenglucs(long denglucs) {
		this.denglucs = denglucs;
	}
	public String getZuihoudlip() {
		return this.zuihoudlip;
	}

	public void setZuihoudlip(String zuihoudlip) {
		this.zuihoudlip = zuihoudlip;
	}
	public String getZuihoudlsf() {
		return this.zuihoudlsf;
	}

	public void setZuihoudlsf(String zuihoudlsf) {
		this.zuihoudlsf = zuihoudlsf;
	}
	public String getZuihoudlcs() {
		return this.zuihoudlcs;
	}

	public void setZuihoudlcs(String zuihoudlcs) {
		this.zuihoudlcs = zuihoudlcs;
	}
	public String getZuihoudlqx() {
		return this.zuihoudlqx;
	}

	public void setZuihoudlqx(String zuihoudlqx) {
		this.zuihoudlqx = zuihoudlqx;
	}
	public String getBeizhu() {
		return this.beizhu;
	}

	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}
}