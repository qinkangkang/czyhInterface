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
@Table(name = "t_order_verification")
public class TOrderVerification extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private TOrder TOrder;
	private TCustomer TCustomerByFoperator;
	private TCustomer TCustomerByFcustomerId;
	private TSponsor TSponsor;
	private BigDecimal forderAmount;
	private BigDecimal forderOriginalAmount;
	private BigDecimal forderChangelAmount;
	private BigDecimal forderRate;
	private Integer fsettlementType;
	private Date fcreateTime;
	private Integer fclientType;
	private Integer fclientOperate;
	private String fclientGps;
	private String fclientDevice;
	private Integer fstatus;

	// Constructors

	/** default constructor */
	public TOrderVerification() {
	}

	/** minimal constructor */
	public TOrderVerification(String id) {
		this.id = id;
	}

	// Property accessors
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fOrderID")
	public TOrder getTOrder() {
		return this.TOrder;
	}

	public void setTOrder(TOrder TOrder) {
		this.TOrder = TOrder;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fOperator")
	public TCustomer getTCustomerByFoperator() {
		return this.TCustomerByFoperator;
	}

	public void setTCustomerByFoperator(TCustomer TCustomerByFoperator) {
		this.TCustomerByFoperator = TCustomerByFoperator;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fCustomerID")
	public TCustomer getTCustomerByFcustomerId() {
		return this.TCustomerByFcustomerId;
	}

	public void setTCustomerByFcustomerId(TCustomer TCustomerByFcustomerId) {
		this.TCustomerByFcustomerId = TCustomerByFcustomerId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fSponsorId")
	public TSponsor getTSponsor() {
		return this.TSponsor;
	}

	public void setTSponsor(TSponsor TSponsor) {
		this.TSponsor = TSponsor;
	}

	@Column(name = "fOrderAmount", precision = 18)
	public BigDecimal getForderAmount() {
		return this.forderAmount;
	}

	public void setForderAmount(BigDecimal forderAmount) {
		this.forderAmount = forderAmount;
	}

	@Column(name = "fOrderOriginalAmount", precision = 18)
	public BigDecimal getForderOriginalAmount() {
		return this.forderOriginalAmount;
	}

	public void setForderOriginalAmount(BigDecimal forderOriginalAmount) {
		this.forderOriginalAmount = forderOriginalAmount;
	}

	@Column(name = "fOrderRate", precision = 8)
	public BigDecimal getForderRate() {
		return this.forderRate;
	}

	public void setForderRate(BigDecimal forderRate) {
		this.forderRate = forderRate;
	}

	@Column(name = "fSettlementType")
	public Integer getFsettlementType() {
		return this.fsettlementType;
	}

	public void setFsettlementType(Integer fsettlementType) {
		this.fsettlementType = fsettlementType;
	}

	@Column(name = "fCreateTime", length = 19)
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "fClientType")
	public Integer getFclientType() {
		return this.fclientType;
	}

	public void setFclientType(Integer fclientType) {
		this.fclientType = fclientType;
	}

	@Column(name = "fClientOperate")
	public Integer getFclientOperate() {
		return this.fclientOperate;
	}

	public void setFclientOperate(Integer fclientOperate) {
		this.fclientOperate = fclientOperate;
	}

	@Column(name = "fClientGPS", length = 2048)
	public String getFclientGps() {
		return this.fclientGps;
	}

	public void setFclientGps(String fclientGps) {
		this.fclientGps = fclientGps;
	}

	@Column(name = "fClientDevice", length = 2048)
	public String getFclientDevice() {
		return this.fclientDevice;
	}

	public void setFclientDevice(String fclientDevice) {
		this.fclientDevice = fclientDevice;
	}

	@Column(name = "fStatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}
	
	@Column(name = "fOrderChangelAmount", precision = 18)
	public BigDecimal getForderChangelAmount() {
		return forderChangelAmount;
	}

	public void setForderChangelAmount(BigDecimal forderChangelAmount) {
		this.forderChangelAmount = forderChangelAmount;
	}

}