package com.chufang.entity;

import com.chufang.annotation.Table;
import com.chufang.annotation.ID;
import java.util.Date;

@Table("ysf_pt_yuangongxx")
public class PT_YUANGONGXX {
	@ID
	private int id;

	private int kehuid;

	private String xingming;

	private String quanxian;

	private Date zuihoudlsj;

	private long denglucs;

	private int guanliybz;

	private Date jiarusj;

	private int jinyongbz;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public int getKehuid() {
		return this.kehuid;
	}

	public void setKehuid(int kehuid) {
		this.kehuid = kehuid;
	}
	public String getXingming() {
		return this.xingming;
	}

	public void setXingming(String xingming) {
		this.xingming = xingming;
	}
	public String getQuanxian() {
		return this.quanxian;
	}

	public void setQuanxian(String quanxian) {
		this.quanxian = quanxian;
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
	public int getGuanliybz() {
		return this.guanliybz;
	}

	public void setGuanliybz(int guanliybz) {
		this.guanliybz = guanliybz;
	}
	public Date getJiarusj() {
		return this.jiarusj;
	}

	public void setJiarusj(Date jiarusj) {
		this.jiarusj = jiarusj;
	}
	public int getJinyongbz() {
		return this.jinyongbz;
	}

	public void setJinyongbz(int jinyongbz) {
		this.jinyongbz = jinyongbz;
	}
}