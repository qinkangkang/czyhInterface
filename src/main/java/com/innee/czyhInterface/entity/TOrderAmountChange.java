package com.innee.czyhInterface.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_order_amount_change")
public class TOrderAmountChange extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String forderId;
	private BigDecimal fcouponChange;
	private BigDecimal fbargainChange;
	private Integer fbonusChange;
	private BigDecimal fspellChange;
	private BigDecimal fotherChange;
	private Date fcreateTime;

	// Constructors

	/** default constructor */
	public TOrderAmountChange() {
	}

	/** minimal constructor */
	public TOrderAmountChange(String id) {
		this.id = id;
	}

	// Property accessors
	@Column(name = "fOrderID", length = 36)
	public String getForderId() {
		return this.forderId;
	}

	public void setForderId(String forderId) {
		this.forderId = forderId;
	}

	@Column(name = "fCouponChange", precision = 18)
	public BigDecimal getFcouponChange() {
		return this.fcouponChange;
	}

	public void setFcouponChange(BigDecimal fcouponChange) {
		this.fcouponChange = fcouponChange;
	}

	@Column(name = "fBargainChange", precision = 18)
	public BigDecimal getFbargainChange() {
		return this.fbargainChange;
	}

	public void setFbargainChange(BigDecimal fbargainChange) {
		this.fbargainChange = fbargainChange;
	}

	@Column(name = "fBonusChange")
	public Integer getFbonusChange() {
		return this.fbonusChange;
	}

	public void setFbonusChange(Integer fbonusChange) {
		this.fbonusChange = fbonusChange;
	}

	@Column(name = "fSpellChange", precision = 18)
	public BigDecimal getFspellChange() {
		return this.fspellChange;
	}

	public void setFspellChange(BigDecimal fspellChange) {
		this.fspellChange = fspellChange;
	}

	@Column(name = "fOtherChange", precision = 18)
	public BigDecimal getFotherChange() {
		return this.fotherChange;
	}

	public void setFotherChange(BigDecimal fotherChange) {
		this.fotherChange = fotherChange;
	}

	@Column(name = "fCreateTime", length = 19)
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

}