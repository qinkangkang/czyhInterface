package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "t_customer_ticket")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TCustomerTicket extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String fcustomerId;
	private Integer ftype;
	private String fticket;
	private Date fliveTime;
	private Date fcreateTime;
	private Date fupdateTime;

	// Constructors

	/** default constructor */
	public TCustomerTicket() {
	}

	/** minimal constructor */
	public TCustomerTicket(String id) {
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

	@Column(name = "fType")
	public Integer getFtype() {
		return this.ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}

	@Column(name = "fTicket", length = 64)
	public String getFticket() {
		return this.fticket;
	}

	public void setFticket(String fticket) {
		this.fticket = fticket;
	}

	@Column(name = "fLiveTime", length = 19)
	public Date getFliveTime() {
		return this.fliveTime;
	}

	public void setFliveTime(Date fliveTime) {
		this.fliveTime = fliveTime;
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