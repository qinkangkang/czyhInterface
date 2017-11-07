package com.innee.czyhInterface.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * TPayinfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_payinfo")
public class TPayInfo extends UuidEntity {

	private static final long serialVersionUID = 1L;

	private String forderId;
	private String forderNum;
	private Integer forderType;
	private String fcustomerId;
	private Integer fclientType;
	private Integer finOut;
	private String fcurrencyType;
	private BigDecimal fpayAmount;
	private Integer fpayType;
	private String fpayId;
	private String fpayUrl;
	private Integer fstatus;
	private String fchannel;
	private Date fcreateTime;
	private Date fconfirmPayTime;

	/** default constructor */
	public TPayInfo() {
	}

	/** minimal constructor */
	public TPayInfo(String id) {
		this.id = id;
	}

	@Column(name = "fOrderType")
	public Integer getForderType() {
		return this.forderType;
	}

	public void setForderType(Integer forderType) {
		this.forderType = forderType;
	}

	@Column(name = "fCustomerID", length = 36)
	public String getFcustomerId() {
		return this.fcustomerId;
	}

	public void setFcustomerId(String fcustomerId) {
		this.fcustomerId = fcustomerId;
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

	@Column(name = "fCurrencyType")
	public String getFcurrencyType() {
		return this.fcurrencyType;
	}

	public void setFcurrencyType(String fcurrencyType) {
		this.fcurrencyType = fcurrencyType;
	}

	@Column(name = "fPayAmount", precision = 18)
	public BigDecimal getFpayAmount() {
		return fpayAmount;
	}

	public void setFpayAmount(BigDecimal fpayAmount) {
		this.fpayAmount = fpayAmount;
	}

	@Column(name = "fPayType")
	public Integer getFpayType() {
		return this.fpayType;
	}

	public void setFpayType(Integer fpayType) {
		this.fpayType = fpayType;
	}

	@Column(name = "fPayId")
	public String getFpayId() {
		return this.fpayId;
	}

	public void setFpayId(String fpayId) {
		this.fpayId = fpayId;
	}

	@Column(name = "fPayUrl")
	public String getFpayUrl() {
		return this.fpayUrl;
	}

	public void setFpayUrl(String fpayUrl) {
		this.fpayUrl = fpayUrl;
	}

	@Column(name = "fStatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fChannel")
	public String getFchannel() {
		return this.fchannel;
	}

	public void setFchannel(String fchannel) {
		this.fchannel = fchannel;
	}

	@Column(name = "fCreateTime", length = 19)
	public Date getFcreateTime() {
		return fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "fConfirmPayTime", length = 19)
	public Date getFconfirmPayTime() {
		return fconfirmPayTime;
	}

	public void setFconfirmPayTime(Date fconfirmPayTime) {
		this.fconfirmPayTime = fconfirmPayTime;
	}

	@Column(name = "fOrderId")
	public String getForderId() {
		return forderId;
	}

	public void setForderId(String forderId) {
		this.forderId = forderId;
	}

	@Column(name = "fOrderNum", length = 36)
	public String getForderNum() {
		return forderNum;
	}

	public void setForderNum(String forderNum) {
		this.forderNum = forderNum;
	}

}