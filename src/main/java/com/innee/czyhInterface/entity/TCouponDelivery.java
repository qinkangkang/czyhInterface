package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_coupon_delivery")
public class TCouponDelivery extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private TCouponInformation TCouponInformation;
	private TCustomer TCustomer;
	private Date fdeliverTime;
	private TDelivery TDelivery;
	private Date fuseStartTime;
	private Date fuseEndTime;
	private String ffromOrderId;

	// Constructors

	/** default constructor */
	public TCouponDelivery() {
	}

	/** minimal constructor */
	public TCouponDelivery(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fCouponID")
	public TCouponInformation getTCouponInformation() {
		return TCouponInformation;
	}

	public void setTCouponInformation(TCouponInformation tCouponInformation) {
		TCouponInformation = tCouponInformation;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fDeliveryId")
	public TDelivery getTDelivery() {
		return TDelivery;
	}

	public void setTDelivery(TDelivery tDelivery) {
		TDelivery = tDelivery;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fCustomerID")
	public TCustomer getTCustomer() {
		return this.TCustomer;
	}

	public void setTCustomer(TCustomer TCustomer) {
		this.TCustomer = TCustomer;
	}

	@Column(name = "fDeliverTime", length = 19)
	public Date getFdeliverTime() {
		return this.fdeliverTime;
	}

	public void setFdeliverTime(Date fdeliverTime) {
		this.fdeliverTime = fdeliverTime;
	}

	@Column(name = "fUseStartTime")
	public Date getFuseStartTime() {
		return fuseStartTime;
	}

	public void setFuseStartTime(Date fuseStartTime) {
		this.fuseStartTime = fuseStartTime;
	}

	@Column(name = "fUseEndTime")
	public Date getFuseEndTime() {
		return fuseEndTime;
	}

	public void setFuseEndTime(Date fuseEndTime) {
		this.fuseEndTime = fuseEndTime;
	}
	
	@Column(name = "fFromOrder")
	public String getFfromOrderId() {
		return ffromOrderId;
	}

	public void setFfromOrderId(String ffromOrderId) {
		this.ffromOrderId = ffromOrderId;
	}

}