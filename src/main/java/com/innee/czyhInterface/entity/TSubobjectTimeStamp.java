package com.innee.czyhInterface.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_subobject_time_stamp")
public class TSubobjectTimeStamp extends UuidEntity {

	private static final long serialVersionUID = 1L;
	private Integer fsubObject;
	private Date fsubUpdateTime;
	private Date fsubCreateTime;

	/** default constructor */
	public TSubobjectTimeStamp() {
	}

	/** minimal constructor */
	public TSubobjectTimeStamp(String id) {
		this.id = id;
	}

	@Column(name = "fSubObject", nullable = false)
	public Integer getFsubObject() {
		return this.fsubObject;
	}

	public void setFsubObject(Integer fsubObject) {
		this.fsubObject = fsubObject;
	}

	@Column(name = "fSubUpdateTime", length = 19)
	public Date getFsubUpdateTime() {
		return this.fsubUpdateTime;
	}

	public void setFsubUpdateTime(Date fsubUpdateTime) {
		this.fsubUpdateTime = fsubUpdateTime;
	}

	@Column(name = "fSubCreateTime", length = 19)
	public Date getFsubCreateTime() {
		return this.fsubCreateTime;
	}

	public void setFsubCreateTime(Timestamp fsubCreateTime) {
		this.fsubCreateTime = fsubCreateTime;
	}

}