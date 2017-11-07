package com.innee.czyhInterface.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_customer_bargaining")
public class TCustomerBargaining extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String fbargainingId;
	private String fcustomerId;
	private BigDecimal fstartPrice;
	private BigDecimal fendPrice;
	private BigDecimal fdefaultFloorPrice;
	private Integer fdefaultLevel;
	private Integer fbargainingCount;
	private Integer fstatus;
	private String fbargainingNum;
	private String forderId;
	private Date fcreateTime;
	private Date fdeadline;
	private Date forderTime;
	private Date fpayTime;

	// Constructors

	/** default constructor */
	public TCustomerBargaining() {
	}

	/** minimal constructor */
	public TCustomerBargaining(String id) {
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

	@Column(name = "fCustomerId", length = 36)
	public String getFcustomerId() {
		return this.fcustomerId;
	}

	public void setFcustomerId(String fcustomerId) {
		this.fcustomerId = fcustomerId;
	}

	@Column(name = "fStartPrice", precision = 18)
	public BigDecimal getFstartPrice() {
		return this.fstartPrice;
	}

	public void setFstartPrice(BigDecimal fstartPrice) {
		this.fstartPrice = fstartPrice;
	}

	@Column(name = "fEndPrice", precision = 18)
	public BigDecimal getFendPrice() {
		return this.fendPrice;
	}

	public void setFendPrice(BigDecimal fendPrice) {
		this.fendPrice = fendPrice;
	}

	@Column(name = "fDefaultFloorPrice", precision = 18)
	public BigDecimal getFdefaultFloorPrice() {
		return this.fdefaultFloorPrice;
	}

	public void setFdefaultFloorPrice(BigDecimal fdefaultFloorPrice) {
		this.fdefaultFloorPrice = fdefaultFloorPrice;
	}

	@Column(name = "fDefaultLevel")
	public Integer getFdefaultLevel() {
		return this.fdefaultLevel;
	}

	public void setFdefaultLevel(Integer fdefaultLevel) {
		this.fdefaultLevel = fdefaultLevel;
	}

	@Column(name = "fBargainingCount")
	public Integer getFbargainingCount() {
		return this.fbargainingCount;
	}

	public void setFbargainingCount(Integer fbargainingCount) {
		this.fbargainingCount = fbargainingCount;
	}

	@Column(name = "fStatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fBargainingNum", length = 19)
	public String getFbargainingNum() {
		return fbargainingNum;
	}

	public void setFbargainingNum(String fbargainingNum) {
		this.fbargainingNum = fbargainingNum;
	}

	@Column(name = "fOrderId", length = 36)
	public String getForderId() {
		return this.forderId;
	}

	public void setForderId(String forderId) {
		this.forderId = forderId;
	}

	@Column(name = "fCreateTime", length = 19)
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "fDeadline", length = 19)
	public Date getFdeadline() {
		return this.fdeadline;
	}

	public void setFdeadline(Date fdeadline) {
		this.fdeadline = fdeadline;
	}

	@Column(name = "fOrderTime", length = 19)
	public Date getForderTime() {
		return this.forderTime;
	}

	public void setForderTime(Date forderTime) {
		this.forderTime = forderTime;
	}

	@Column(name = "fPayTime", length = 19)
	public Date getFpayTime() {
		return this.fpayTime;
	}

	public void setFpayTime(Date fpayTime) {
		this.fpayTime = fpayTime;
	}

}