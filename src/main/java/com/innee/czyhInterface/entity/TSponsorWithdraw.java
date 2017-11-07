package com.innee.czyhInterface.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_sponsor_withdraw")
public class TSponsorWithdraw extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private TSponsor TSponsor;
	private TCustomer TCustomer;
	private BigDecimal famount;
	private Integer fstatus;
	private Date fapplyTime;
	private String fapplyRemark;
	private Date ftoAccountTime;
	private String ftoAccountRemark;
	private Integer fclientType;
	private String fclientDevice;
	private String fclientGps;
	private Long foperator;
	private Date ftime;
	private String fremark;

	// Constructors

	/** default constructor */
	public TSponsorWithdraw() {
	}

	/** minimal constructor */
	public TSponsorWithdraw(String id) {
		this.id = id;
	}

	// Property accessors
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fSponsorID")
	public TSponsor getTSponsor() {
		return this.TSponsor;
	}

	public void setTSponsor(TSponsor TSponsor) {
		this.TSponsor = TSponsor;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fApplyer")
	public TCustomer getTCustomer() {
		return this.TCustomer;
	}

	public void setTCustomer(TCustomer TCustomer) {
		this.TCustomer = TCustomer;
	}

	@Column(name = "fAmount", precision = 18)
	public BigDecimal getFamount() {
		return this.famount;
	}

	public void setFamount(BigDecimal famount) {
		this.famount = famount;
	}

	@Column(name = "fStatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fApplyTime", length = 19)
	public Date getFapplyTime() {
		return this.fapplyTime;
	}

	public void setFapplyTime(Date fapplyTime) {
		this.fapplyTime = fapplyTime;
	}

	@Column(name = "fApplyRemark", length = 2048)
	public String getFapplyRemark() {
		return this.fapplyRemark;
	}

	public void setFapplyRemark(String fapplyRemark) {
		this.fapplyRemark = fapplyRemark;
	}

	@Column(name = "fToAccountTime", length = 19)
	public Date getFtoAccountTime() {
		return this.ftoAccountTime;
	}

	public void setFtoAccountTime(Date ftoAccountTime) {
		this.ftoAccountTime = ftoAccountTime;
	}

	@Column(name = "fToAccountRemark", length = 2048)
	public String getFtoAccountRemark() {
		return this.ftoAccountRemark;
	}

	public void setFtoAccountRemark(String ftoAccountRemark) {
		this.ftoAccountRemark = ftoAccountRemark;
	}

	@Column(name = "fClientType")
	public Integer getFclientType() {
		return this.fclientType;
	}

	public void setFclientType(Integer fclientType) {
		this.fclientType = fclientType;
	}

	@Column(name = "fClientDevice", length = 2048)
	public String getFclientDevice() {
		return this.fclientDevice;
	}

	public void setFclientDevice(String fclientDevice) {
		this.fclientDevice = fclientDevice;
	}

	@Column(name = "fClientGPS", length = 2048)
	public String getFclientGps() {
		return this.fclientGps;
	}

	public void setFclientGps(String fclientGps) {
		this.fclientGps = fclientGps;
	}

	@Column(name = "fOperator")
	public Long getFoperator() {
		return this.foperator;
	}

	public void setFoperator(Long foperator) {
		this.foperator = foperator;
	}

	@Column(name = "fTime", length = 19)
	public Date getFtime() {
		return this.ftime;
	}

	public void setFtime(Date ftime) {
		this.ftime = ftime;
	}

	@Column(name = "fRemark", length = 2048)
	public String getFremark() {
		return this.fremark;
	}

	public void setFremark(String fremark) {
		this.fremark = fremark;
	}

}