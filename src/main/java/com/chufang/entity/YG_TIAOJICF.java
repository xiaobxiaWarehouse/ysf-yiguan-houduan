package com.chufang.entity;

import com.chufang.annotation.Table;
import com.chufang.annotation.ID;
import com.chufang.annotation.Ignore;

import java.util.Date;

@Table("ysf_yg_tiaojicf")
public class YG_TIAOJICF {
	@ID
	private long id;

	private long yiguanid;

	private int chufangly;

	private String chufangtp;

	private String chufangbh;

	private int chufanglx;

	private Date jiuzhenrq;

	private String yishengxm;

	private String huanzhexm;

	private int xingbie = 3;

	@Ignore
	private int nianling;

	private String nianlingdw;

	private String zhenduan;

	private String maian;

	private String keshimc;

	private String feiyonglb;

	private String menzhenblh;

	private String huanzhelxdh;

	private String huanzhelxdz;

	@Ignore
	private int tieshu;

	@Ignore
	private int weishu;

	private String yongyaopl;

	private String yongfa;

	private String fuyongyq;

	private int fuwusid;

	private int tiaojifs;

	@Ignore
	private int tiaojixh;

	private int nongjianbz;

	private String tiaojibz;

	@Ignore
	private double chufangje;

	private int genfangbz;

	private int shouhuodwlx;

	private String jihuapsrq;

	private int jihuapssjfw;

	private String shouhuoren;

	private String shouhuolxdh;

	private String shouhuodz;

	private int zhuangtai;

	private Date chuangjiansj;

	private String chuangjianren;

	private Date tijiaosj;

	private String tijiaoren;

	private Date shenhesj;

	private String shenheren;

	private Date jieshousj;

	private String jieshouren;

	private Date zuofeisj;

	private String zuofeiren;

	private Date tuihuisj;

	private String tuihuiyy;

	private String tuihuiren;

	private Date wanchengsj;

	private String wanchengren;

	private Date zuihoudysj;

	private String tixingxx;

	@Ignore
	private int tongbuzt;

	private String tongbuxx;

	private Date tongbusj;
	
	@Ignore
	private int peisongfs;
	
	@Ignore
	private int kuaidigs;

	private String kuaididh;
	
	@Ignore
	private String fuwusmc;

	public String getFuwusmc() {
		return fuwusmc;
	}

	public void setFuwusmc(String fuwusmc) {
		this.fuwusmc = fuwusmc;
	}

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
	public int getChufangly() {
		return this.chufangly;
	}

	public void setChufangly(int chufangly) {
		this.chufangly = chufangly;
	}
	public String getChufangtp() {
		return this.chufangtp;
	}

	public void setChufangtp(String chufangtp) {
		this.chufangtp = chufangtp;
	}
	public String getChufangbh() {
		return this.chufangbh;
	}

	public void setChufangbh(String chufangbh) {
		this.chufangbh = chufangbh;
	}
	public int getChufanglx() {
		return this.chufanglx;
	}

	public void setChufanglx(int chufanglx) {
		this.chufanglx = chufanglx;
	}
	public Date getJiuzhenrq() {
		return this.jiuzhenrq;
	}

	public void setJiuzhenrq(Date jiuzhenrq) {
		this.jiuzhenrq = jiuzhenrq;
	}
	public String getYishengxm() {
		return this.yishengxm;
	}

	public void setYishengxm(String yishengxm) {
		this.yishengxm = yishengxm;
	}
	public String getHuanzhexm() {
		return this.huanzhexm;
	}

	public void setHuanzhexm(String huanzhexm) {
		this.huanzhexm = huanzhexm;
	}
	public int getXingbie() {
		return this.xingbie;
	}

	public void setXingbie(int xingbie) {
		this.xingbie = xingbie;
	}
	public int getNianling() {
		return this.nianling;
	}

	public void setNianling(int nianling) {
		this.nianling = nianling;
	}
	public String getNianlingdw() {
		return this.nianlingdw;
	}

	public void setNianlingdw(String nianlingdw) {
		this.nianlingdw = nianlingdw;
	}
	public String getZhenduan() {
		return this.zhenduan;
	}

	public void setZhenduan(String zhenduan) {
		this.zhenduan = zhenduan;
	}
	public String getMaian() {
		return this.maian;
	}

	public void setMaian(String maian) {
		this.maian = maian;
	}
	public String getKeshimc() {
		return this.keshimc;
	}

	public void setKeshimc(String keshimc) {
		this.keshimc = keshimc;
	}
	public String getFeiyonglb() {
		return this.feiyonglb;
	}

	public void setFeiyonglb(String feiyonglb) {
		this.feiyonglb = feiyonglb;
	}
	public String getMenzhenblh() {
		return this.menzhenblh;
	}

	public void setMenzhenblh(String menzhenblh) {
		this.menzhenblh = menzhenblh;
	}
	public String getHuanzhelxdh() {
		return this.huanzhelxdh;
	}

	public void setHuanzhelxdh(String huanzhelxdh) {
		this.huanzhelxdh = huanzhelxdh;
	}
	public String getHuanzhelxdz() {
		return this.huanzhelxdz;
	}

	public void setHuanzhelxdz(String huanzhelxdz) {
		this.huanzhelxdz = huanzhelxdz;
	}
	public int getTieshu() {
		return this.tieshu;
	}

	public void setTieshu(int tieshu) {
		this.tieshu = tieshu;
	}
	public int getWeishu() {
		return this.weishu;
	}

	public void setWeishu(int weishu) {
		this.weishu = weishu;
	}
	public String getYongyaopl() {
		return this.yongyaopl;
	}

	public void setYongyaopl(String yongyaopl) {
		this.yongyaopl = yongyaopl;
	}
	public String getYongfa() {
		return this.yongfa;
	}

	public void setYongfa(String yongfa) {
		this.yongfa = yongfa;
	}
	public String getFuyongyq() {
		return this.fuyongyq;
	}

	public void setFuyongyq(String fuyongyq) {
		this.fuyongyq = fuyongyq;
	}
	public int getFuwusid() {
		return this.fuwusid;
	}

	public void setFuwusid(int fuwusid) {
		this.fuwusid = fuwusid;
	}
	public int getTiaojifs() {
		return this.tiaojifs;
	}

	public void setTiaojifs(int tiaojifs) {
		this.tiaojifs = tiaojifs;
	}
	public int getTiaojixh() {
		return this.tiaojixh;
	}

	public void setTiaojixh(int tiaojixh) {
		this.tiaojixh = tiaojixh;
	}
	public int getNongjianbz() {
		return this.nongjianbz;
	}

	public void setNongjianbz(int nongjianbz) {
		this.nongjianbz = nongjianbz;
	}
	public String getTiaojibz() {
		return this.tiaojibz;
	}

	public void setTiaojibz(String tiaojibz) {
		this.tiaojibz = tiaojibz;
	}
	public double getChufangje() {
		return this.chufangje;
	}

	public void setChufangje(double chufangje) {
		this.chufangje = chufangje;
	}
	public int getGenfangbz() {
		return this.genfangbz;
	}

	public void setGenfangbz(int genfangbz) {
		this.genfangbz = genfangbz;
	}
	public int getShouhuodwlx() {
		return this.shouhuodwlx;
	}

	public void setShouhuodwlx(int shouhuodwlx) {
		this.shouhuodwlx = shouhuodwlx;
	}
	public String getJihuapsrq() {
		return this.jihuapsrq;
	}

	public void setJihuapsrq(String jihuapsrq) {
		this.jihuapsrq = jihuapsrq;
	}
	public int getJihuapssjfw() {
		return this.jihuapssjfw;
	}

	public void setJihuapssjfw(int jihuapssjfw) {
		this.jihuapssjfw = jihuapssjfw;
	}
	public String getShouhuoren() {
		return this.shouhuoren;
	}

	public void setShouhuoren(String shouhuoren) {
		this.shouhuoren = shouhuoren;
	}
	public String getShouhuolxdh() {
		return this.shouhuolxdh;
	}

	public void setShouhuolxdh(String shouhuolxdh) {
		this.shouhuolxdh = shouhuolxdh;
	}
	public String getShouhuodz() {
		return this.shouhuodz;
	}

	public void setShouhuodz(String shouhuodz) {
		this.shouhuodz = shouhuodz;
	}
	public int getZhuangtai() {
		return this.zhuangtai;
	}

	public void setZhuangtai(int zhuangtai) {
		this.zhuangtai = zhuangtai;
	}
	public Date getChuangjiansj() {
		return this.chuangjiansj;
	}

	public void setChuangjiansj(Date chuangjiansj) {
		this.chuangjiansj = chuangjiansj;
	}
	public String getChuangjianren() {
		return this.chuangjianren;
	}

	public void setChuangjianren(String chuangjianren) {
		this.chuangjianren = chuangjianren;
	}
	public Date getTijiaosj() {
		return this.tijiaosj;
	}

	public void setTijiaosj(Date tijiaosj) {
		this.tijiaosj = tijiaosj;
	}
	public String getTijiaoren() {
		return this.tijiaoren;
	}

	public void setTijiaoren(String tijiaoren) {
		this.tijiaoren = tijiaoren;
	}
	public Date getShenhesj() {
		return this.shenhesj;
	}

	public void setShenhesj(Date shenhesj) {
		this.shenhesj = shenhesj;
	}
	public String getShenheren() {
		return this.shenheren;
	}

	public void setShenheren(String shenheren) {
		this.shenheren = shenheren;
	}
	public Date getJieshousj() {
		return this.jieshousj;
	}

	public void setJieshousj(Date jieshousj) {
		this.jieshousj = jieshousj;
	}
	public String getJieshouren() {
		return this.jieshouren;
	}

	public void setJieshouren(String jieshouren) {
		this.jieshouren = jieshouren;
	}
	public Date getZuofeisj() {
		return this.zuofeisj;
	}

	public void setZuofeisj(Date zuofeisj) {
		this.zuofeisj = zuofeisj;
	}
	public String getZuofeiren() {
		return this.zuofeiren;
	}

	public void setZuofeiren(String zuofeiren) {
		this.zuofeiren = zuofeiren;
	}
	public Date getTuihuisj() {
		return this.tuihuisj;
	}

	public void setTuihuisj(Date tuihuisj) {
		this.tuihuisj = tuihuisj;
	}
	public String getTuihuiyy() {
		return this.tuihuiyy;
	}

	public void setTuihuiyy(String tuihuiyy) {
		this.tuihuiyy = tuihuiyy;
	}
	public String getTuihuiren() {
		return this.tuihuiren;
	}

	public void setTuihuiren(String tuihuiren) {
		this.tuihuiren = tuihuiren;
	}
	public Date getWanchengsj() {
		return this.wanchengsj;
	}

	public void setWanchengsj(Date wanchengsj) {
		this.wanchengsj = wanchengsj;
	}
	public String getWanchengren() {
		return this.wanchengren;
	}

	public void setWanchengren(String wanchengren) {
		this.wanchengren = wanchengren;
	}
	public Date getZuihoudysj() {
		return this.zuihoudysj;
	}

	public void setZuihoudysj(Date zuihoudysj) {
		this.zuihoudysj = zuihoudysj;
	}
	public String getTixingxx() {
		return this.tixingxx;
	}

	public void setTixingxx(String tixingxx) {
		this.tixingxx = tixingxx;
	}
	public int getTongbuzt() {
		return this.tongbuzt;
	}

	public void setTongbuzt(int tongbuzt) {
		this.tongbuzt = tongbuzt;
	}
	public String getTongbuxx() {
		return this.tongbuxx;
	}

	public void setTongbuxx(String tongbuxx) {
		this.tongbuxx = tongbuxx;
	}
	public Date getTongbusj() {
		return this.tongbusj;
	}

	public void setTongbusj(Date tongbusj) {
		this.tongbusj = tongbusj;
	}
	public int getPeisongfs() {
		return this.peisongfs;
	}

	public void setPeisongfs(int peisongfs) {
		this.peisongfs = peisongfs;
	}
	public int getKuaidigs() {
		return this.kuaidigs;
	}

	public void setKuaidigs(int kuaidigs) {
		this.kuaidigs = kuaidigs;
	}
	public String getKuaididh() {
		return this.kuaididh;
	}

	public void setKuaididh(String kuaididh) {
		this.kuaididh = kuaididh;
	}
}