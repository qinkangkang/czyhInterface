package com.innee.czyhInterface.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_push_customer")
public class TPushCustomer extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private TPush TPush;
	private String fcustomerId;
	private Integer funread;
	private Integer fstatus;

	// Constructors

	/** default constructor */
	public TPushCustomer() {
	}

	/** minimal constructor */
	public TPushCustomer(String id) {
		this.id = id;
	}

	// Property accessors
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fPushID")
	public TPush getTPush() {
		return this.TPush;
	}

	public void setTPush(TPush TPush) {
		this.TPush = TPush;
	}

	@Column(name = "fCustomerId", length = 36)
	public String getFcustomerId() {
		return this.fcustomerId;
	}

	public void setFcustomerId(String fcustomerId) {
		this.fcustomerId = fcustomerId;
	}

	@Column(name = "fStatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fUnread")
	public Integer getFunread() {
		return funread;
	}

	public void setFunread(Integer funread) {
		this.funread = funread;
	}

}