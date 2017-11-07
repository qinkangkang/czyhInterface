package com.innee.czyhInterface.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_order_refund")
public class TOrderRefund extends UuidEntity {

	private static final long serialVersionUID = 1L;

	private String forderId;
	private Integer frefundType;
	private Integer frefundStatus;
	private String fgoodsStatus;
	private String frefundExpress;
	private Integer frefundExpressType;
	private BigDecimal fRefundTotal;
	private Integer fRefundReson;
	private String fRefundDesc;
	private Date fcreateTime;
	private Date fupdateTime;

	public TOrderRefund() {
	}

	public TOrderRefund(String id) {
		this.id = id;
	}

	@Column(name = "fOrderId", nullable = false, length = 36)
	public String getForderId() {
		return this.forderId;
	}

	public void setForderId(String forderId) {
		this.forderId = forderId;
	}

	@Column(name = "fRefundType")
	public Integer getFrefundType() {
		return frefundType;
	}

	public void setFrefundType(Integer frefundType) {
		this.frefundType = frefundType;
	}

	@Column(name = "fRefundStatus")
	public Integer getFrefundStatus() {
		return frefundStatus;
	}

	public void setFrefundStatus(Integer frefundStatus) {
		this.frefundStatus = frefundStatus;
	}
	
	@Column(name = "fGoodsStatus")
	public String getFgoodsStatus() {
		return fgoodsStatus;
	}

	public void setFgoodsStatus(String fgoodsStatus) {
		this.fgoodsStatus = fgoodsStatus;
	}

	@Column(name = "fRefundExpress")
	public String getFrefundExpress() {
		return frefundExpress;
	}

	public void setFrefundExpress(String frefundExpress) {
		this.frefundExpress = frefundExpress;
	}

	@Column(name = "fRefundExpressType")
	public Integer getFrefundExpressType() {
		return frefundExpressType;
	}

	public void setFrefundExpressType(Integer frefundExpressType) {
		this.frefundExpressType = frefundExpressType;
	}

	@Column(name = "fRefundTotal")
	public BigDecimal getfRefundTotal() {
		return fRefundTotal;
	}

	public void setfRefundTotal(BigDecimal fRefundTotal) {
		this.fRefundTotal = fRefundTotal;
	}

	@Column(name = "fRefundReson")
	public Integer getfRefundReson() {
		return fRefundReson;
	}

	public void setfRefundReson(Integer fRefundReson) {
		this.fRefundReson = fRefundReson;
	}

	@Column(name = "fRefundDesc")
	public String getfRefundDesc() {
		return fRefundDesc;
	}

	public void setfRefundDesc(String fRefundDesc) {
		this.fRefundDesc = fRefundDesc;
	}

	@Column(name = "fCreateTime", nullable = false, length = 19)
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}


	@Column(name = "fUpdateTime", nullable = false, length = 19)
	public Date getFupdateTime() {
		return this.fupdateTime;
	}

	public void setFupdateTime(Date fupdateTime) {
		this.fupdateTime = fupdateTime;
	}

}