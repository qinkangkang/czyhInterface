package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_customer_baby")
public class TCustomerBaby extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String fcustomerId;
	private String fname;
	private Date fbirthday;
	private Integer fage;
	private Integer fsex;
	private String flikeType;
	private Date fcreateTime;
	private Date fupdateTime;
	private Integer forder;

	// Constructors

	/** default constructor */
	public TCustomerBaby() {
	}

	/** minimal constructor */
	public TCustomerBaby(String id) {
		this.id = id;
	}

	@Column(name = "fCustomerId", length = 36)
	public String getFcustomerId() {
		return this.fcustomerId;
	}

	public void setFcustomerId(String fcustomerId) {
		this.fcustomerId = fcustomerId;
	}

	@Column(name = "fName")
	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	@Column(name = "fBirthday", length = 19)
	public Date getFbirthday() {
		return this.fbirthday;
	}

	public void setFbirthday(Date fbirthday) {
		this.fbirthday = fbirthday;
	}

	@Column(name = "fAge")
	public Integer getFage() {
		return this.fage;
	}

	public void setFage(Integer fage) {
		this.fage = fage;
	}

	@Column(name = "fLikeType", length = 2048)
	public String getFlikeType() {
		return this.flikeType;
	}

	public void setFlikeType(String flikeType) {
		this.flikeType = flikeType;
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

	@Column(name = "fOrder")
	public Integer getForder() {
		return this.forder;
	}

	public void setForder(Integer forder) {
		this.forder = forder;
	}

	@Column(name = "fSex")
	public Integer getFsex() {
		return fsex;
	}

	public void setFsex(Integer fsex) {
		this.fsex = fsex;
	}

}