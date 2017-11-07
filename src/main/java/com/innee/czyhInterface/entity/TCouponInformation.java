package com.innee.czyhInterface.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "t_coupon_information")
public class TCouponInformation extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String ftitle;
	private String fcouponNum;
	private String fuseRange;
	private Date fuseStartTime;
	private Date fuseEndTime;
	private Integer fvalidDays;
	private BigDecimal famount;
	private BigDecimal fdiscount;
	private BigDecimal flimitation;
	private Integer fcity;
	private Integer fuseType;
	private Integer fuserPoint;
	private String fcouponDesc;
	private Long foperator;
	private Date fcreateTime;
	private Date fupdateTime;
	private Integer fcouponStatus;
	private Integer fcouponType;
	private Integer fcouponClass;
	private Set<TCouponObject> TCouponObjects = new HashSet<TCouponObject>(0);
	private Set<TCouponDelivery> TCouponDeliveries = new HashSet<TCouponDelivery>(0);
	private Set<TCouponDeliveryHistory> TCouponDeliveryHistorys = new HashSet<TCouponDeliveryHistory>(0);

	// Constructors

	/** default constructor */
	public TCouponInformation() {
	}

	/** minimal constructor */
	public TCouponInformation(String id) {
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

	@Column(name = "fUseRange")
	public String getFuseRange() {
		return this.fuseRange;
	}

	public void setFuseRange(String fuseRange) {
		this.fuseRange = fuseRange;
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
		return fuseEndTime;
	}

	public void setFuseEndTime(Date fuseEndTime) {
		this.fuseEndTime = fuseEndTime;
	}

	@Column(name = "fValidDays")
	public Integer getFvalidDays() {
		return this.fvalidDays;
	}

	public void setFvalidDays(Integer fvalidDays) {
		this.fvalidDays = fvalidDays;
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

	@Column(name = "fUseType")
	public Integer getFuseType() {
		return this.fuseType;
	}

	public void setFuseType(Integer fuseType) {
		this.fuseType = fuseType;
	}

	@Column(name = "fUserPoint")
	public Integer getFuserPoint() {
		return this.fuserPoint;
	}

	public void setFuserPoint(Integer fuserPoint) {
		this.fuserPoint = fuserPoint;
	}

	@Column(name = "fCouponDesc")
	public String getFcouponDesc() {
		return this.fcouponDesc;
	}

	public void setFcouponDesc(String fcouponDesc) {
		this.fcouponDesc = fcouponDesc;
	}

	@Column(name = "fOperator")
	public Long getFoperator() {
		return this.foperator;
	}

	public void setFoperator(Long foperator) {
		this.foperator = foperator;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TCouponInformation")
	public Set<TCouponObject> getTCouponObjects() {
		return this.TCouponObjects;
	}

	public void setTCouponObjects(Set<TCouponObject> TCouponObjects) {
		this.TCouponObjects = TCouponObjects;
	}

	public Integer getFcouponStatus() {
		return fcouponStatus;
	}

	public void setFcouponStatus(Integer fcouponStatus) {
		this.fcouponStatus = fcouponStatus;
	}

	public Integer getFcouponType() {
		return fcouponType;
	}

	public void setFcouponType(Integer fcouponType) {
		this.fcouponType = fcouponType;
	}

	public Integer getFcity() {
		return fcity;
	}

	public void setFcity(Integer fcity) {
		this.fcity = fcity;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TCouponInformation")
	public Set<TCouponDelivery> getTCouponDeliveries() {
		return this.TCouponDeliveries;
	}

	public void setTCouponDeliveries(Set<TCouponDelivery> TCouponDeliveries) {
		this.TCouponDeliveries = TCouponDeliveries;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TCouponInformation")
	public Set<TCouponDeliveryHistory> getTCouponDeliveryHistorys() {
		return TCouponDeliveryHistorys;
	}

	public void setTCouponDeliveryHistorys(Set<TCouponDeliveryHistory> tCouponDeliveryHistorys) {
		TCouponDeliveryHistorys = tCouponDeliveryHistorys;
	}

	@Column(name = "fCouponClass")
	public Integer getFcouponClass() {
		return fcouponClass;
	}

	public void setFcouponClass(Integer fcouponClass) {
		this.fcouponClass = fcouponClass;
	}
	
	

}