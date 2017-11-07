package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_customer_event_recommend")
public class TCustomerEventRecommend extends UuidEntity {

	private static final long serialVersionUID = 1L;

	private String fcustomerId;
	private String feventId;
	private Date fcreateTime;
	private Integer forder;

	// Constructors

	/** default constructor */
	public TCustomerEventRecommend() {
	}

	/** minimal constructor */
	public TCustomerEventRecommend(String id) {
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

	@Column(name = "fCreateTime", length = 19)
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "fOrder")
	public Integer getForder() {
		return this.forder;
	}

	public void setForder(Integer forder) {
		this.forder = forder;
	}

}