package com.chufang.entity;

import com.chufang.annotation.Table;
import com.chufang.annotation.ID;
import java.util.Date;

@Table("ysf_fws_fuwusxx")
public class FWS_FUWUSXX {
	@ID
	private int id;

	private String mingcheng;

	private String jiancheng;

	private String shengfen;

	private String chengshi;

	private String quxian;

	private String jiedaodz;

	private Date zhucesj;

	private String tiaojifw;

	private String miyao;

	private String shenfenbs;

	private int jinyongbz;

	private String chuangjianren;

	private Date chuangjiansj;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
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
	public String getTiaojifw() {
		return this.tiaojifw;
	}

	public void setTiaojifw(String tiaojifw) {
		this.tiaojifw = tiaojifw;
	}
	public String getMiyao() {
		return this.miyao;
	}

	public void setMiyao(String miyao) {
		this.miyao = miyao;
	}
	public String getShenfenbs() {
		return this.shenfenbs;
	}

	public void setShenfenbs(String shenfenbs) {
		this.shenfenbs = shenfenbs;
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