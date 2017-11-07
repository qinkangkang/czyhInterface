package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_coupon_activitycoupons")
public class TCouponActivitycoupons extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String fcouponId;
	private Integer num;
	private String fdeliveryId;
	private Integer fdeliveryCount;
	private Integer fsendCount;
	private Integer fuseCount;
	private Date fcreatetime;

	// Constructors

	/** default constructor */
	public TCouponActivitycoupons() {
	}

	/** minimal constructor */
	public TCouponActivitycoupons(String id) {
		this.id = id;
	}

	// Property accessors
	@Column(name = "fCouponId")
	public String getFcouponId() {
		return fcouponId;
	}

	public void setFcouponId(String fcouponId) {
		this.fcouponId = fcouponId;
	}

	@Column(name = "num")
	public Integer getNum() {
		return this.num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	@Column(name = "fDeliveryId")
	public String getFdeliveryId() {
		return this.fdeliveryId;
	}

	public void setFdeliveryId(String fdeliveryId) {
		this.fdeliveryId = fdeliveryId;
	}

	@Column(name = "fDeliveryCount")
	public Integer getFdeliveryCount() {
		return this.fdeliveryCount;
	}

	public void setFdeliveryCount(Integer fdeliveryCount) {
		this.fdeliveryCount = fdeliveryCount;
	}

	@Column(name = "fSendCount")
	public Integer getFsendCount() {
		return this.fsendCount;
	}

	public void setFsendCount(Integer fsendCount) {
		this.fsendCount = fsendCount;
	}

	@Column(name = "fUseCount")
	public Integer getFuseCount() {
		return fuseCount;
	}

	public void setFuseCount(Integer fuseCount) {
		this.fuseCount = fuseCount;
	}

	@Column(name = "fCreatetime", length = 1)
	public Date getFcreatetime() {
		return fcreatetime;
	}

	public void setFcreatetime(Date fcreatetime) {
		this.fcreatetime = fcreatetime;
	}
	

}