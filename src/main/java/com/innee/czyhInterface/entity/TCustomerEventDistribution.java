package com.innee.czyhInterface.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_customer_event_distribution")
public class TCustomerEventDistribution extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String fcustomerId;
	private String feventId;
	private String feventTitle;
	private Integer fdistributionCount;
	private Integer fbuyCount;
	private BigDecimal forderTotal;
	private BigDecimal fdistributionRewardAmount;
	private Date fcreateTime;
	private Date fupdateTime;

	// Constructors

	/** default constructor */
	public TCustomerEventDistribution() {
	}

	/** minimal constructor */
	public TCustomerEventDistribution(String id) {
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

	@Column(name = "fEventId", length = 36)
	public String getFeventId() {
		return this.feventId;
	}

	public void setFeventId(String feventId) {
		this.feventId = feventId;
	}

	@Column(name = "fEventTitle")
	public String getFeventTitle() {
		return this.feventTitle;
	}

	public void setFeventTitle(String feventTitle) {
		this.feventTitle = feventTitle;
	}

	@Column(name = "fDistributionCount")
	public Integer getFdistributionCount() {
		return this.fdistributionCount;
	}

	public void setFdistributionCount(Integer fdistributionCount) {
		this.fdistributionCount = fdistributionCount;
	}

	@Column(name = "fBuyCount")
	public Integer getFbuyCount() {
		return this.fbuyCount;
	}

	public void setFbuyCount(Integer fbuyCount) {
		this.fbuyCount = fbuyCount;
	}

	@Column(name = "fOrderTotal", precision = 18)
	public BigDecimal getForderTotal() {
		return this.forderTotal;
	}

	public void setForderTotal(BigDecimal forderTotal) {
		this.forderTotal = forderTotal;
	}

	@Column(name = "fDistributionRewardAmount", precision = 18)
	public BigDecimal getFdistributionRewardAmount() {
		return this.fdistributionRewardAmount;
	}

	public void setFdistributionRewardAmount(BigDecimal fdistributionRewardAmount) {
		this.fdistributionRewardAmount = fdistributionRewardAmount;
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