package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_customer_sign")
public class TCustomerSign extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String fcustomerId;
	private Integer fsingDay;
	private Date fsignTime;

	// Constructors

	/** default constructor */
	public TCustomerSign() {
	}

	/** minimal constructor */
	public TCustomerSign(String id) {
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

	@Column(name = "fSingDay")
	public Integer getFsingDay() {
		return this.fsingDay;
	}

	public void setFsingDay(Integer fsingDay) {
		this.fsingDay = fsingDay;
	}

	@Column(name = "fSignTime", length = 19)
	public Date getFsignTime() {
		return this.fsignTime;
	}

	public void setFsignTime(Date fsignTime) {
		this.fsignTime = fsignTime;
	}

}