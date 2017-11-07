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
@Table(name = "t_wx_pay")
public class TWxPay extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String forderId;
	private Integer forderType;
	private TCustomer TCustomer;
	private Integer fclientType;
	private Integer finOut;
	private String fcurrencyType;
	private BigDecimal fpayAmount;
	private Date fprePayTime;
	private String fppRequestInfo;
	private String fppResponseInfo;
	private Date fconfirmPayTime;
	private String fcpRequestInfo;
	private String fcpResponseInfo;
	private String ftransactionId;
	private Integer fstatus;
	private Date fcreateTime;
	private Date fupdateTime;

	// Constructors

	/** default constructor */
	public TWxPay() {
	}

	/** minimal constructor */
	public TWxPay(String id) {
		this.id = id;
	}

	// Property accessors
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fCustomerID")
	public TCustomer getTCustomer() {
		return this.TCustomer;
	}

	public void setTCustomer(TCustomer TCustomer) {
		this.TCustomer = TCustomer;
	}

	@Column(name = "fOrderID", length = 36)
	public String getForderId() {
		return this.forderId;
	}

	public void setForderId(String forderId) {
		this.forderId = forderId;
	}

	@Column(name = "fOrderType")
	public Integer getForderType() {
		return this.forderType;
	}

	public void setForderType(Integer forderType) {
		this.forderType = forderType;
	}

	@Column(name = "fClientType")
	public Integer getFclientType() {
		return this.fclientType;
	}

	public void setFclientType(Integer fclientType) {
		this.fclientType = fclientType;
	}

	@Column(name = "fInOut")
	public Integer getFinOut() {
		return this.finOut;
	}

	public void setFinOut(Integer finOut) {
		this.finOut = finOut;
	}

	@Column(name = "fCurrencyType", length = 32)
	public String getFcurrencyType() {
		return this.fcurrencyType;
	}

	public void setFcurrencyType(String fcurrencyType) {
		this.fcurrencyType = fcurrencyType;
	}

	@Column(name = "fPayAmount", precision = 18)
	public BigDecimal getFpayAmount() {
		return this.fpayAmount;
	}

	public void setFpayAmount(BigDecimal fpayAmount) {
		this.fpayAmount = fpayAmount;
	}

	@Column(name = "fPrePayTime", length = 19)
	public Date getFprePayTime() {
		return this.fprePayTime;
	}

	public void setFprePayTime(Date fprePayTime) {
		this.fprePayTime = fprePayTime;
	}

	@Column(name = "fPpRequestInfo", length = 2048)
	public String getFppRequestInfo() {
		return this.fppRequestInfo;
	}

	public void setFppRequestInfo(String fppRequestInfo) {
		this.fppRequestInfo = fppRequestInfo;
	}

	@Column(name = "fPpResponseInfo", length = 2048)
	public String getFppResponseInfo() {
		return this.fppResponseInfo;
	}

	public void setFppResponseInfo(String fppResponseInfo) {
		this.fppResponseInfo = fppResponseInfo;
	}

	@Column(name = "fConfirmPayTime", length = 19)
	public Date getFconfirmPayTime() {
		return this.fconfirmPayTime;
	}

	public void setFconfirmPayTime(Date fconfirmPayTime) {
		this.fconfirmPayTime = fconfirmPayTime;
	}

	@Column(name = "fCpRequestInfo", length = 2048)
	public String getFcpRequestInfo() {
		return this.fcpRequestInfo;
	}

	public void setFcpRequestInfo(String fcpRequestInfo) {
		this.fcpRequestInfo = fcpRequestInfo;
	}

	@Column(name = "fCpResponseInfo", length = 2048)
	public String getFcpResponseInfo() {
		return this.fcpResponseInfo;
	}

	public void setFcpResponseInfo(String fcpResponseInfo) {
		this.fcpResponseInfo = fcpResponseInfo;
	}

	@Column(name = "fTransactionId")
	public String getFtransactionId() {
		return this.ftransactionId;
	}

	public void setFtransactionId(String ftransactionId) {
		this.ftransactionId = ftransactionId;
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