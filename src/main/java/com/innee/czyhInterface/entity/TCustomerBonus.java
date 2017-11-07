package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_customer_bonus")
public class TCustomerBonus extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String fcustermerId;
	private Integer fbonus;
	private Date fcreateTime;
	private Integer ftype;
	private String fobject;

	// Constructors

	/** default constructor */
	public TCustomerBonus() {
	}

	/** minimal constructor */
	public TCustomerBonus(String id) {
		this.id = id;
	}

	@Column(name = "fCustermerID", length = 36)
	public String getFcustermerId() {
		return this.fcustermerId;
	}

	public void setFcustermerId(String fcustermerId) {
		this.fcustermerId = fcustermerId;
	}

	@Column(name = "fBonus")
	public Integer getFbonus() {
		return this.fbonus;
	}

	public void setFbonus(Integer fbonus) {
		this.fbonus = fbonus;
	}

	@Column(name = "fCreateTime", length = 19)
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "fType")
	public Integer getFtype() {
		return this.ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}

	@Column(name = "fObject", length = 36)
	public String getFobject() {
		return this.fobject;
	}

	public void setFobject(String fobject) {
		this.fobject = fobject;
	}

}