package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_customer_recommend")
public class TCustomerRecommend extends UuidEntity {

	private static final long serialVersionUID = 1L;

	private String fcustomerId;
	private String feventId;
	private Date fcreateTime;

	// Constructors

	/** default constructor */
	public TCustomerRecommend() {
	}

	/** minimal constructor */
	public TCustomerRecommend(String id) {
		this.id = id;
	}

	@Column(name = "fCustomerId")
	public String getFcustomerId() {
		return this.fcustomerId;
	}

	public void setFcustomerId(String fcustomerId) {
		this.fcustomerId = fcustomerId;
	}

	@Column(name = "fEventId")
	public String getFeventId() {
		return this.feventId;
	}

	public void setFeventId(String feventId) {
		this.feventId = feventId;
	}

	@Column(name = "fCreateTime")
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

}