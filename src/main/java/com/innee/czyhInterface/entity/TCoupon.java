package com.innee.czyhInterface.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_coupon")
public class TCoupon extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String ftitle;
	private String fcouponNum;
	private Integer fcity;
	private String fdescription;
	private String fnotice;
	private String fuseRange;
	private Date fdeliverStartTime;
	private Date fdeliverEndTime;
	private Date fuseStartTime;
	private Date fuseEndTime;
	private BigDecimal famount;
	private BigDecimal fdiscount;
	private BigDecimal flimitation;
	private Integer fcount;
	private Integer fsendCount;
	private Integer fuseType;
	private Integer fdeliverType;
	private Long foperator;
	private Integer fstatus;
	private Long fauditor;
	private Date fcreateTime;
	private Date fupdateTime;

	// Constructors

	/** default constructor */
	public TCoupon() {
	}

	/** minimal constructor */
	public TCoupon(String id) {
		this.id = id;
	}

	// Property accessors
	@Column(name = "fTitle")
	public String getFtitle() {
		return this.ftitle;
	}

	public void setFtitle(String ftitle) {
		this.ftitle = ftitle;
	}

	@Column(name = "fCouponNum")
	public String getFcouponNum() {
		return this.fcouponNum;
	}

	public void setFcouponNum(String fcouponNum) {
		this.fcouponNum = fcouponNum;
	}

	@Column(name = "fCity")
	public Integer getFcity() {
		return this.fcity;
	}

	public void setFcity(Integer fcity) {
		this.fcity = fcity;
	}

	@Column(name = "fDescription")
	public String getFdescription() {
		return this.fdescription;
	}

	public void setFdescription(String fdescription) {
		this.fdescription = fdescription;
	}

	@Column(name = "fNotice")
	public String getFnotice() {
		return this.fnotice;
	}

	public void setFnotice(String fnotice) {
		this.fnotice = fnotice;
	}

	@Column(name = "fUseRange")
	public String getFuseRange() {
		return this.fuseRange;
	}

	public void setFuseRange(String fuseRange) {
		this.fuseRange = fuseRange;
	}

	@Column(name = "fDeliverStartTime", length = 19)
	public Date getFdeliverStartTime() {
		return this.fdeliverStartTime;
	}

	public void setFdeliverStartTime(Date fdeliverStartTime) {
		this.fdeliverStartTime = fdeliverStartTime;
	}

	@Column(name = "fDeliverEndTIme", length = 19)
	public Date getFdeliverEndTime() {
		return this.fdeliverEndTime;
	}

	public void setFdeliverEndTime(Date fdeliverEndTime) {
		this.fdeliverEndTime = fdeliverEndTime;
	}

	@Column(name = "fUseStartTime", length = 19)
	public Date getFuseStartTime() {
		return this.fuseStartTime;
	}

	public void setFuseStartTime(Date fuseStartTime) {
		this.fuseStartTime = fuseStartTime;
	}

	@Column(name = "fUseEndTime", length = 19)
	public Date getFuseEndTime() {
		return this.fuseEndTime;
	}

	public void setFuseEndTime(Date fuseEndTime) {
		this.fuseEndTime = fuseEndTime;
	}

	@Column(name = "fAmount", precision = 8)
	public BigDecimal getFamount() {
		return this.famount;
	}

	public void setFamount(BigDecimal famount) {
		this.famount = famount;
	}

	@Column(name = "fDiscount", precision = 8)
	public BigDecimal getFdiscount() {
		return this.fdiscount;
	}

	public void setFdiscount(BigDecimal fdiscount) {
		this.fdiscount = fdiscount;
	}

	@Column(name = "fLimitation", precision = 18)
	public BigDecimal getFlimitation() {
		return this.flimitation;
	}

	public void setFlimitation(BigDecimal flimitation) {
		this.flimitation = flimitation;
	}

	@Column(name = "fCount")
	public Integer getFcount() {
		return this.fcount;
	}

	public void setFcount(Integer fcount) {
		this.fcount = fcount;
	}

	@Column(name = "fSendCount")
	public Integer getFsendCount() {
		return this.fsendCount;
	}

	public void setFsendCount(Integer fsendCount) {
		this.fsendCount = fsendCount;
	}

	@Column(name = "fUseType")
	public Integer getFuseType() {
		return this.fuseType;
	}

	public void setFuseType(Integer fuseType) {
		this.fuseType = fuseType;
	}

	@Column(name = "fDeliverType")
	public Integer getFdeliverType() {
		return this.fdeliverType;
	}

	public void setFdeliverType(Integer fdeliverType) {
		this.fdeliverType = fdeliverType;
	}

	@Column(name = "fOperator")
	public Long getFoperator() {
		return this.foperator;
	}

	public void setFoperator(Long foperator) {
		this.foperator = foperator;
	}

	@Column(name = "fStatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fAuditor")
	public Long getFauditor() {
		return this.fauditor;
	}

	public void setFauditor(Long fauditor) {
		this.fauditor = fauditor;
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