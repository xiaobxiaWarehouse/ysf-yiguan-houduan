package com.chufang.entity;

import com.chufang.annotation.Table;
import com.chufang.annotation.ID;
import java.util.Date;

@Table("test")

public class Test {
	@ID
	private long id;

	private String name;

	private int birth;

	private Date createtime;

	private double weight;

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public int getBirth() {
		return this.birth;
	}

	public void setBirth(int birth) {
		this.birth = birth;
	}
	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public double getWeight() {
		return this.weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
}