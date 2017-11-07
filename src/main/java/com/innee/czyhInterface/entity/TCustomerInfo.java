package com.innee.czyhInterface.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "t_customer_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TCustomerInfo extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String fcustomerId;
	private String fregisterChannel;
	private String fregisterChannelVersion;
	private String fregisterDeviceId;
	private String fregisterDeviceInfo;
	private String fregisterDeviceTokens;
	private Date fregisterTime;
	private Date ffirstOrderTime;
	private Integer forderNumber;
	private Integer fpayOrderNumber;
	private Integer frefundOrderNumber;
	private BigDecimal forderTotal;
	private Integer fpoint;
	private Integer fcouponNum;
	private Integer flevel;
	private Integer fgrowthValue;
	private Integer fneedGrowthValue;
	private Integer fpayZeroOrderNumber;
	private Integer ftipNumber;
	private Integer fusedPoint;
	private String fposterImage;
	private String fqRcodeImage;
	private String fpointCode;
	private Date finvalidTime;
	private Integer fjfFlag;
	private Integer fkykFlag;
	private Integer fans;
	private Integer forderFans;
	private String followCustomerId;
	private Date fcreateTime;
	private Date fupdateTime;

	// Constructors

	/** default constructor */
	public TCustomerInfo() {
	}

	/** minimal constructor */
	public TCustomerInfo(String id) {
		this.id = id;
	}

	// Property accessors
	@Column(name = "fCustomerId", length = 36)
	public String getFcustomerId() {
		return this.fcustomerId;
	}

	public void setFcustomerId(String fcustomerId) {
		this.fcustomerId = fcustomerId;
	}

	@Column(name = "fRegisterChannel")
	public String getFregisterChannel() {
		return this.fregisterChannel;
	}

	public void setFregisterChannel(String fregisterChannel) {
		this.fregisterChannel = fregisterChannel;
	}

	@Column(name = "fRegisterChannelVersion")
	public String getFregisterChannelVersion() {
		return this.fregisterChannelVersion;
	}

	public void setFregisterChannelVersion(String fregisterChannelVersion) {
		this.fregisterChannelVersion = fregisterChannelVersion;
	}

	@Column(name = "fRegisterDeviceId")
	public String getFregisterDeviceId() {
		return this.fregisterDeviceId;
	}

	public void setFregisterDeviceId(String fregisterDeviceId) {
		this.fregisterDeviceId = fregisterDeviceId;
	}

	@Column(name = "fRegisterDeviceInfo", length = 2048)
	public String getFregisterDeviceInfo() {
		return this.fregisterDeviceInfo;
	}

	public void setFregisterDeviceInfo(String fregisterDeviceInfo) {
		this.fregisterDeviceInfo = fregisterDeviceInfo;
	}

	@Column(name = "fRegisterTime", length = 19)
	public Date getFregisterTime() {
		return this.fregisterTime;
	}

	public void setFregisterTime(Date fregisterTime) {
		this.fregisterTime = fregisterTime;
	}

	@Column(name = "fFirstOrderTime", length = 19)
	public Date getFfirstOrderTime() {
		return this.ffirstOrderTime;
	}

	public void setFfirstOrderTime(Date ffirstOrderTime) {
		this.ffirstOrderTime = ffirstOrderTime;
	}

	@Column(name = "fOrderNumber")
	public Integer getForderNumber() {
		return this.forderNumber;
	}

	public void setForderNumber(Integer forderNumber) {
		this.forderNumber = forderNumber;
	}

	@Column(name = "fPayOrderNumber")
	public Integer getFpayOrderNumber() {
		return this.fpayOrderNumber;
	}

	public void setFpayOrderNumber(Integer fpayOrderNumber) {
		this.fpayOrderNumber = fpayOrderNumber;
	}

	@Column(name = "fRefundOrderNumber")
	public Integer getFrefundOrderNumber() {
		return this.frefundOrderNumber;
	}

	public void setFrefundOrderNumber(Integer frefundOrderNumber) {
		this.frefundOrderNumber = frefundOrderNumber;
	}

	@Column(name = "fOrderTotal", precision = 18)
	public BigDecimal getForderTotal() {
		return this.forderTotal;
	}

	public void setForderTotal(BigDecimal forderTotal) {
		this.forderTotal = forderTotal;
	}

	@Column(name = "fPoint")
	public Integer getFpoint() {
		return this.fpoint;
	}

	public void setFpoint(Integer fpoint) {
		this.fpoint = fpoint;
	}

	@Column(name = "fPayZeroOrderNumber")
	public Integer getFpayZeroOrderNumber() {
		return fpayZeroOrderNumber;
	}

	public void setFpayZeroOrderNumber(Integer fpayZeroOrderNumber) {
		this.fpayZeroOrderNumber = fpayZeroOrderNumber;
	}

	@Column(name = "fTipNumber")
	public Integer getFtipNumber() {
		return ftipNumber;
	}

	public void setFtipNumber(Integer ftipNumber) {
		this.ftipNumber = ftipNumber;
	}

	@Column(name = "fPosterImage")
	public String getFposterImage() {
		return fposterImage;
	}

	public void setFposterImage(String fposterImage) {
		this.fposterImage = fposterImage;
	}

	@Column(name = "fQRcodeImage")
	public String getFqRcodeImage() {
		return fqRcodeImage;
	}

	public void setFqRcodeImage(String fqRcodeImage) {
		this.fqRcodeImage = fqRcodeImage;
	}

	@Column(name = "fUsedPoint")
	public Integer getFusedPoint() {
		return fusedPoint;
	}

	public void setFusedPoint(Integer fusedPoint) {
		this.fusedPoint = fusedPoint;
	}

	@Column(name = "fPointCode")
	public String getFpointCode() {
		return this.fpointCode;
	}

	public void setFpointCode(String fpointCode) {
		this.fpointCode = fpointCode;
	}

	@Column(name = "fInvalidTime", length = 19)
	public Date getFinvalidTime() {
		return this.finvalidTime;
	}

	public void setFinvalidTime(Date finvalidTime) {
		this.finvalidTime = finvalidTime;
	}

	@Column(name = "fJfFlag")
	public Integer getFjfFlag() {
		return this.fjfFlag;
	}

	public void setFjfFlag(Integer fjfFlag) {
		this.fjfFlag = fjfFlag;
	}

	@Column(name = "fKykFlag")
	public Integer getFkykFlag() {
		return this.fkykFlag;
	}

	public void setFkykFlag(Integer fkykFlag) {
		this.fkykFlag = fkykFlag;
	}

	@Column(name = "fFans")
	public Integer getFans() {
		return fans;
	}

	public void setFans(Integer fans) {
		this.fans = fans;
	}

	@Column(name = "fFollowCustomerId")
	public String getFollowCustomerId() {
		return followCustomerId;
	}

	public void setFollowCustomerId(String followCustomerId) {
		this.followCustomerId = followCustomerId;
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

	@Column(name = "fCouponNum", length = 19)
	public Integer getFcouponNum() {
		return fcouponNum;
	}

	public void setFcouponNum(Integer fcouponNum) {
		this.fcouponNum = fcouponNum;
	}

	@Column(name = "fLevel")
	public Integer getFlevel() {
		return flevel;
	}

	public void setFlevel(Integer flevel) {
		this.flevel = flevel;
	}

	@Column(name = "fGrowthValue")
	public Integer getFgrowthValue() {
		return fgrowthValue;
	}

	public void setFgrowthValue(Integer fgrowthValue) {
		this.fgrowthValue = fgrowthValue;
	}

	@Column(name = "fNeedGrowthValue")
	public Integer getFneedGrowthValue() {
		return fneedGrowthValue;
	}

	public void setFneedGrowthValue(Integer fneedGrowthValue) {
		this.fneedGrowthValue = fneedGrowthValue;
	}

	@Column(name = "fRegisterDeviceTokens")
	public String getFregisterDeviceTokens() {
		return fregisterDeviceTokens;
	}

	public void setFregisterDeviceTokens(String fregisterDeviceTokens) {
		this.fregisterDeviceTokens = fregisterDeviceTokens;
	}
	
	@Column(name = "fOrderFans")
	public Integer getForderFans() {
		return forderFans;
	}

	public void setForderFans(Integer forderFans) {
		this.forderFans = forderFans;
	}

}