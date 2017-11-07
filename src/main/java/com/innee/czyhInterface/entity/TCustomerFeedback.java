package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TCustomerFeedback entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_customer_feedback")
public class TCustomerFeedback extends UuidEntity {

	private static final long serialVersionUID = 1L;

	private String fcustomerId;
	private String fmessage;
	private Integer ftype;
	private Date fcreateTime;

	// Constructors

	/** default constructor */
	public TCustomerFeedback() {
	}

	/** minimal constructor */
	public TCustomerFeedback(String id) {
		this.id = id;
	}

	@Column(name = "fCustomerId", length = 36)
	public String getFcustomerId() {
		return this.fcustomerId;
	}

	public void setFcustomerId(String fcustomerId) {
		this.fcustomerId = fcustomerId;
	}

	@Column(name = "fmessage", length = 2048)
	public String getFmessage() {
		return this.fmessage;
	}

	public void setFmessage(String fmessage) {
		this.fmessage = fmessage;
	}

	@Column(name = "ftype")
	public Integer getFtype() {
		return this.ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}

	@Column(name = "fCreateTime", length = 19)
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

}