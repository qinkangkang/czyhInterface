package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_coupon_channel")
public class TCouponChannel extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String fdeliveryId;
	private String fcouponName;
	private String fsubtitle;
	private String fuseRange;
	private Integer forder;
	private Date fbeginTime;
	private Date fendTime;
	private Integer fstatus;
	private Date fcreateTime;
	private Date fupdateTime;

	// Constructors

	/** default constructor */
	public TCouponChannel() {
	}

	/** minimal constructor */
	public TCouponChannel(String id) {
		this.id = id;
	}

	// Property accessors
	@Column(name = "fDeliveryId", length = 36)
	public String getFdeliveryId() {
		return this.fdeliveryId;
	}

	public void setFdeliveryId(String fdeliveryId) {
		this.fdeliveryId = fdeliveryId;
	}

	@Column(name = "fCouponName")
	public String getFcouponName() {
		return this.fcouponName;
	}

	public void setFcouponName(String fcouponName) {
		this.fcouponName = fcouponName;
	}

	@Column(name = "fSubtitle")
	public String getFsubtitle() {
		return this.fsubtitle;
	}

	public void setFsubtitle(String fsubtitle) {
		this.fsubtitle = fsubtitle;
	}

	@Column(name = "fOrder")
	public Integer getForder() {
		return this.forder;
	}

	public void setForder(Integer forder) {
		this.forder = forder;
	}

	@Column(name = "fUseRange")
	public String getFuseRange() {
		return fuseRange;
	}

	public void setFuseRange(String fuseRange) {
		this.fuseRange = fuseRange;
	}

	@Column(name = "fBeginTime", length = 19)
	public Date getFbeginTime() {
		return this.fbeginTime;
	}

	public void setFbeginTime(Date fbeginTime) {
		this.fbeginTime = fbeginTime;
	}

	@Column(name = "fEndTime", length = 19)
	public Date getFendTime() {
		return this.fendTime;
	}

	public void setFendTime(Date fendTime) {
		this.fendTime = fendTime;
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
		return fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "fUpdateTime", length = 19)
	public Date getFupdateTime() {
		return fupdateTime;
	}

	public void setFupdateTime(Date fupdateTime) {
		this.fupdateTime = fupdateTime;
	}
	
	

}