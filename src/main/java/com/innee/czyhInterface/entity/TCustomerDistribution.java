package com.innee.czyhInterface.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_customer_distribution")
public class TCustomerDistribution extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String fcustomerId;
	private String fbuyerId;
	private String forderId;
	private BigDecimal fdistributionRebateAmount;
	private Integer fstatus;
	private Date fcreateTime;
	private Date fupdateTime;

	// Constructors

	/** default constructor */
	public TCustomerDistribution() {
	}

	/** minimal constructor */
	public TCustomerDistribution(String id) {
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

	@Column(name = "fBuyerId", length = 36)
	public String getFbuyerId() {
		return this.fbuyerId;
	}

	public void setFbuyerId(String fbuyerId) {
		this.fbuyerId = fbuyerId;
	}

	@Column(name = "fOrderId", length = 36)
	public String getForderId() {
		return this.forderId;
	}

	public void setForderId(String forderId) {
		this.forderId = forderId;
	}

	@Column(name = "fDistributionRebateAmount", precision = 8)
	public BigDecimal getFdistributionRebateAmount() {
		return this.fdistributionRebateAmount;
	}

	public void setFdistributionRebateAmount(BigDecimal fdistributionRebateAmount) {
		this.fdistributionRebateAmount = fdistributionRebateAmount;
	}

	@Column(name = "fStatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
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