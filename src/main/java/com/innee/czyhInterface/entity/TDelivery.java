package com.innee.czyhInterface.entity;

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
@Table(name = "t_delivery")
public class TDelivery extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String ftitle;
	private String fdescription;
	private String fnotice;
	private Integer fcity;
	private Integer fisPush;
	private String fpushContent;
	private String fuseRange;
	private Date fdeliveryStartTime;
	private Date fdeliveryEndTime;
	private Date fdeliveryCreateTime;
	private Integer fdeliverType;
	private Long foperator;
	private Integer fstatus;
	private Integer freciveLimit;
	private Integer freciveChannel;
	private Integer factivityType;
	private Long fauditor;
	private Date fcreateTime;
	private Date fupdateTime;
	private Set<TCouponDelivery> TCouponDeliveries = new HashSet<TCouponDelivery>(0);

	// Constructors

	/** default constructor */
	public TDelivery() {
	}

	/** minimal constructor */
	public TDelivery(String id) {
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

	@Column(name = "fCity")
	public Integer getFcity() {
		return this.fcity;
	}

	public void setFcity(Integer fcity) {
		this.fcity = fcity;
	}

	@Column(name = "fIsPush")
	public Integer getFisPush() {
		return this.fisPush;
	}

	public void setFisPush(Integer fisPush) {
		this.fisPush = fisPush;
	}

	@Column(name = "fPushContent")
	public String getFpushContent() {
		return this.fpushContent;
	}

	public void setFpushContent(String fpushContent) {
		this.fpushContent = fpushContent;
	}

	@Column(name = "fUseRange")
	public String getFuseRange() {
		return this.fuseRange;
	}

	public void setFuseRange(String fuseRange) {
		this.fuseRange = fuseRange;
	}

	@Column(name = "fDeliveryStartTime", length = 19)
	public Date getFdeliveryStartTime() {
		return this.fdeliveryStartTime;
	}

	public void setFdeliveryStartTime(Date fdeliveryStartTime) {
		this.fdeliveryStartTime = fdeliveryStartTime;
	}

	@Column(name = "fDeliveryEndTime", length = 19)
	public Date getFdeliveryEndTime() {
		return this.fdeliveryEndTime;
	}

	public void setFdeliveryEndTime(Date fdeliveryEndTime) {
		this.fdeliveryEndTime = fdeliveryEndTime;
	}

	@Column(name = "fDeliveryCreateTime", length = 19)
	public Date getFdeliveryCreateTime() {
		return this.fdeliveryCreateTime;
	}

	public void setFdeliveryCreateTime(Date fdeliveryCreateTime) {
		this.fdeliveryCreateTime = fdeliveryCreateTime;
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

	@Column(name = "fReciveLimit")
	public Integer getFreciveLimit() {
		return this.freciveLimit;
	}

	public void setFreciveLimit(Integer freciveLimit) {
		this.freciveLimit = freciveLimit;
	}

	
	@Column(name = "fReciveChannel")
	public Integer getFreciveChannel() {
		return freciveChannel;
	}

	public void setFreciveChannel(Integer freciveChannel) {
		this.freciveChannel = freciveChannel;
	}

	@Column(name = "fActivityType")
	public Integer getFactivityType() {
		return this.factivityType;
	}

	public void setFactivityType(Integer factivityType) {
		this.factivityType = factivityType;
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
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TDelivery")
	public Set<TCouponDelivery> getTCouponDeliveries() {
		return this.TCouponDeliveries;
	}

	public void setTCouponDeliveries(Set<TCouponDelivery> TCouponDeliveries) {
		this.TCouponDeliveries = TCouponDeliveries;
	}

}