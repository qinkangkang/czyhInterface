package com.innee.czyhInterface.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_help_bargaining_detail")
public class THelpBargainingDetail extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String fbargainingId;
	private String fcustomerBargainingId;
	private String fhelperId;
	private Integer fkykType;
	private BigDecimal fchangeAmount;
	private BigDecimal fchangePrice;
	private Date fcreateTime;

	// Constructors

	/** default constructor */
	public THelpBargainingDetail() {
	}

	/** minimal constructor */
	public THelpBargainingDetail(String id) {
		this.id = id;
	}

	// Property accessors
	@Column(name = "fBargainingId", length = 36)
	public String getFbargainingId() {
		return this.fbargainingId;
	}

	public void setFbargainingId(String fbargainingId) {
		this.fbargainingId = fbargainingId;
	}

	@Column(name = "fCustomerBargainingId", length = 36)
	public String getFcustomerBargainingId() {
		return this.fcustomerBargainingId;
	}

	public void setFcustomerBargainingId(String fcustomerBargainingId) {
		this.fcustomerBargainingId = fcustomerBargainingId;
	}

	@Column(name = "fHelperId", length = 36)
	public String getFhelperId() {
		return this.fhelperId;
	}

	public void setFhelperId(String fhelperId) {
		this.fhelperId = fhelperId;
	}

	@Column(name = "fKykType")
	public Integer getFkykType() {
		return this.fkykType;
	}

	public void setFkykType(Integer fkykType) {
		this.fkykType = fkykType;
	}

	@Column(name = "fChangeAmount", precision = 18)
	public BigDecimal getFchangeAmount() {
		return this.fchangeAmount;
	}

	public void setFchangeAmount(BigDecimal fchangeAmount) {
		this.fchangeAmount = fchangeAmount;
	}

	@Column(name = "fChangePrice", precision = 18)
	public BigDecimal getFchangePrice() {
		return this.fchangePrice;
	}

	public void setFchangePrice(BigDecimal fchangePrice) {
		this.fchangePrice = fchangePrice;
	}

	@Column(name = "fCreateTime", length = 19)
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

}