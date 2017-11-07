package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_customer_event_browse")
public class TCustomerEventBrowse extends UuidEntity{

	private static final long serialVersionUID = 1L;
	
	private String fcustomerId;
	private String feventId;
	private Date fcreateTime;

	// Constructors

	/** default constructor */
	public TCustomerEventBrowse() {
	}

	/** minimal constructor */
	public TCustomerEventBrowse(String id) {
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

}