package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_customer_likes_dislikes")
public class TCustomerLikesDislikes extends UuidEntity {

	private static final long serialVersionUID = 1L;

	private String fcustomerId;
	private String feventId;
	private Integer ftype;
	private Date fcreateTime;

	// Constructors

	/** default constructor */
	public TCustomerLikesDislikes() {
	}

	/** minimal constructor */
	public TCustomerLikesDislikes(String id) {
		this.id = id;
	}

	@Column(name = "fCustomerId", length = 36)
	public String getFcustomerId() {
		return this.fcustomerId;
	}

	public void setFcustomerId(String fcustomerId) {
		this.fcustomerId = fcustomerId;
	}

	@Column(name = "fEventId", length = 36)
	public String getFeventId() {
		return this.feventId;
	}

	public void setFeventId(String feventId) {
		this.feventId = feventId;
	}

	@Column(name = "fType")
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