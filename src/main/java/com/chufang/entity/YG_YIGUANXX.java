package com.chufang.entity;

import com.chufang.annotation.Table;
import com.chufang.annotation.ID;
import java.util.Date;

@Table("ysf_yg_yiguanxx")
public class YG_YIGUANXX {
	@ID
	private long id;

	private String mingcheng;

	private String jiancheng;

	private int leixing;

	private String shengfen;

	private String chengshi;

	private String quxian;

	private String jiedaodz;

	private Date zhucesj;

	private String lianxidh;

	private String lianxiren;

	private String chufangyymbid;

	private Integer tangjimrfwsid;

	private Integer gaojimrfwsid;

	private Integer wanjimrfwsid;

	private Integer sanjimrfwsid;

	private Integer zhongyaopfkljmrfwsid;

	private int jinyongbz = 0;

	private String chuangjianren;

	private Date chuangjiansj;

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public String getMingcheng() {
		return this.mingcheng;
	}

	public void setMingcheng(String mingcheng) {
		this.mingcheng = mingcheng;
	}
	public String getJiancheng() {
		return this.jiancheng;
	}

	public void setJiancheng(String jiancheng) {
		this.jiancheng = jiancheng;
	}
	public int getLeixing() {
		return this.leixing;
	}

	public void setLeixing(int leixing) {
		this.leixing = leixing;
	}
	public String getShengfen() {
		return this.shengfen;
	}

	public void setShengfen(String shengfen) {
		this.shengfen = shengfen;
	}
	public String getChengshi() {
		return this.chengshi;
	}

	public void setChengshi(String chengshi) {
		this.chengshi = chengshi;
	}
	public String getQuxian() {
		return this.quxian;
	}

	public void setQuxian(String quxian) {
		this.quxian = quxian;
	}
	public String getJiedaodz() {
		return this.jiedaodz;
	}

	public void setJiedaodz(String jiedaodz) {
		this.jiedaodz = jiedaodz;
	}
	public Date getZhucesj() {
		return this.zhucesj;
	}

	public void setZhucesj(Date zhucesj) {
		this.zhucesj = zhucesj;
	}
	public String getLianxidh() {
		return this.lianxidh;
	}

	public void setLianxidh(String lianxidh) {
		this.lianxidh = lianxidh;
	}
	public String getLianxiren() {
		return this.lianxiren;
	}

	public void setLianxiren(String lianxiren) {
		this.lianxiren = lianxiren;
	}
	public String getChufangyymbid() {
		return this.chufangyymbid;
	}

	public void setChufangyymbid(String chufangyymbid) {
		this.chufangyymbid = chufangyymbid;
	}
	public Integer getTangjimrfwsid() {
		return this.tangjimrfwsid == null ? 0 : this.tangjimrfwsid;
	}

	public void setTangjimrfwsid(int tangjimrfwsid) {
		this.tangjimrfwsid = tangjimrfwsid;
	}
	public Integer getGaojimrfwsid() {
		return this.gaojimrfwsid == null ? 0 : this.gaojimrfwsid;
	}

	public void setGaojimrfwsid(int gaojimrfwsid) {
		this.gaojimrfwsid = gaojimrfwsid;
	}
	public Integer getWanjimrfwsid() {
		return this.wanjimrfwsid == null ? 0 : this.wanjimrfwsid;
	}

	public void setWanjimrfwsid(int wanjimrfwsid) {
		this.wanjimrfwsid = wanjimrfwsid;
	}
	public Integer getSanjimrfwsid() {
		return this.sanjimrfwsid == null ? 0 : this.sanjimrfwsid;
	}

	public void setSanjimrfwsid(int sanjimrfwsid) {
		this.sanjimrfwsid = sanjimrfwsid;
	}
	public Integer getZhongyaopfkljmrfwsid() {
		return this.zhongyaopfkljmrfwsid == null ? 0 : this.zhongyaopfkljmrfwsid;
	}

	public void setZhongyaopfkljmrfwsid(int zhongyaopfkljmrfwsid) {
		this.zhongyaopfkljmrfwsid = zhongyaopfkljmrfwsid;
	}
	public int getJinyongbz() {
		return this.jinyongbz;
	}

	public void setJinyongbz(int jinyongbz) {
		this.jinyongbz = jinyongbz;
	}
	public String getChuangjianren() {
		return this.chuangjianren;
	}

	public void setChuangjianren(String chuangjianren) {
		this.chuangjianren = chuangjianren;
	}
	public Date getChuangjiansj() {
		return this.chuangjiansj;
	}

	public void setChuangjiansj(Date chuangjiansj) {
		this.chuangjiansj = chuangjiansj;
	}
}