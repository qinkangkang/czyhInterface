package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_predict_height")
public class TPredictHeight extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String fcustomerId;
	private Integer fbabaHeight;
	private Integer fmamaHeight;
	private Integer fbabySex;
	private Integer fbabyHeight;
	private Integer fmoreThanAverage;
	private Date fcreateTime;
	private Date fupdateTime;

	// Constructors

	/** default constructor */
	public TPredictHeight() {
	}

	/** minimal constructor */
	public TPredictHeight(String id) {
		this.id = id;
	}

	// Property accessors
	@Column(name = "fCustomerId", length = 36)
	public String getFcustomerId() {
		return this.fcustomerId;
	}

	public void setFcustomerId(String fcustomerId) {
		this.fcustomerId = fcustomerId;
	}

	@Column(name = "fBabaHeight")
	public Integer getFbabaHeight() {
		return this.fbabaHeight;
	}

	public void setFbabaHeight(Integer fbabaHeight) {
		this.fbabaHeight = fbabaHeight;
	}

	@Column(name = "fMamaHeight")
	public Integer getFmamaHeight() {
		return this.fmamaHeight;
	}

	public void setFmamaHeight(Integer fmamaHeight) {
		this.fmamaHeight = fmamaHeight;
	}

	@Column(name = "fBabySex")
	public Integer getFbabySex() {
		return this.fbabySex;
	}

	public void setFbabySex(Integer fbabySex) {
		this.fbabySex = fbabySex;
	}

	@Column(name = "fBabyHeight")
	public Integer getFbabyHeight() {
		return this.fbabyHeight;
	}

	public void setFbabyHeight(Integer fbabyHeight) {
		this.fbabyHeight = fbabyHeight;
	}

	@Column(name = "fMoreThanAverage")
	public Integer getFmoreThanAverage() {
		return this.fmoreThanAverage;
	}

	public void setFmoreThanAverage(Integer fmoreThanAverage) {
		this.fmoreThanAverage = fmoreThanAverage;
	}

	@Column(name = "fCreateTime", length = 19)
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "fUpdateTime", length = 19)
	public Date getFupdateTime() {
		return this.fupdateTime;
	}

	public void setFupdateTime(Date fupdateTime) {
		this.fupdateTime = fupdateTime;
	}

}