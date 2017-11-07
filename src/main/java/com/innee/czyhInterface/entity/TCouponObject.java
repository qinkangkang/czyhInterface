package com.innee.czyhInterface.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_coupon_object")
public class TCouponObject extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private TCouponInformation TCouponInformation;
	private String fobjectId;
	private Integer fuseType;
	private String fobjectTitle;

	// Constructors

	/** default constructor */
	public TCouponObject() {
	}

	/** minimal constructor */
	public TCouponObject(String id) {
		this.id = id;
	}

	// Property accessors
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fCouponID")
	public TCouponInformation getTCouponInformation() {
		return this.TCouponInformation;
	}

	public void setTCouponInformation(TCouponInformation TCouponInformation) {
		this.TCouponInformation = TCouponInformation;
	}

	@Column(name = "fObjectID", length = 36)
	public String getFobjectId() {
		return this.fobjectId;
	}

	public void setFobjectId(String fobjectId) {
		this.fobjectId = fobjectId;
	}

	@Column(name = "fUseType")
	public Integer getFuseType() {
		return this.fuseType;
	}

	public void setFuseType(Integer fuseType) {
		this.fuseType = fuseType;
	}
	
	@Column(name = "fObjectTitle")
	public String getFobjectTitle() {
		return fobjectTitle;
	}

	public void setFobjectTitle(String fobjectTitle) {
		this.fobjectTitle = fobjectTitle;
	}

}