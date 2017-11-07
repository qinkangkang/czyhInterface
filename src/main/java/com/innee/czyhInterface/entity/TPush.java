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
@Table(name = "t_push")
public class TPush extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String ftitle;
	private String fcontent;
	private String fimage;
	private Integer ftype;
	private Integer ftargetType;
	private String ftargetObject;
	private Date fpushTime;
	private String fdescription;
	private String fpageTitle;
	private Integer fuserTag;
	private Date fvalidTime;
	private Integer fpushtimeType;
	private Integer fpushuserType;
	private Integer fstatus;
	private String fauditMessage;
	private Integer fauditStatus;
	private Date fauditTime;
	private Long foperator;
	private Date fcreateTime;
	private Set<TPushCustomer> TPushCustomers = new HashSet<TPushCustomer>(0);

	// Constructors

	/** default constructor */
	public TPush() {
	}

	/** minimal constructor */
	public TPush(String id) {
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

	@Column(name = "fContent")
	public String getFcontent() {
		return this.fcontent;
	}

	public void setFcontent(String fcontent) {
		this.fcontent = fcontent;
	}

	@Column(name = "fImage")
	public String getFimage() {
		return this.fimage;
	}

	public void setFimage(String fimage) {
		this.fimage = fimage;
	}

	@Column(name = "fType")
	public Integer getFtype() {
		return this.ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}

	@Column(name = "fTargetType")
	public Integer getFtargetType() {
		return this.ftargetType;
	}

	public void setFtargetType(Integer ftargetType) {
		this.ftargetType = ftargetType;
	}

	@Column(name = "fPushTime", length = 19)
	public Date getFpushTime() {
		return this.fpushTime;
	}

	public void setFpushTime(Date fpushTime) {
		this.fpushTime = fpushTime;
	}

	@Column(name = "fDescription")
	public String getFdescription() {
		return this.fdescription;
	}

	public void setFdescription(String fdescription) {
		this.fdescription = fdescription;
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

	@Column(name = "fPushTimeType")
	public Integer getFpushtimeType() {
		return fpushtimeType;
	}

	public void setFpushtimeType(Integer fpushtimeType) {
		this.fpushtimeType = fpushtimeType;
	}

	@Column(name = "fPushUserType")
	public Integer getFpushuserType() {
		return fpushuserType;
	}

	public void setFpushuserType(Integer fpushuserType) {
		this.fpushuserType = fpushuserType;
	}

	@Column(name = "fUserTag")
	public Integer getFuserTag() {
		return fuserTag;
	}

	public void setFuserTag(Integer fuserTag) {
		this.fuserTag = fuserTag;
	}

	@Column(name = "fValidTime", length = 19)
	public Date getFvalidTime() {
		return fvalidTime;
	}

	@Column(name = "fAuditStatus")
	public Integer getFauditStatus() {
		return fauditStatus;
	}

	public void setFauditStatus(Integer fauditStatus) {
		this.fauditStatus = fauditStatus;
	}

	public void setFvalidTime(Date fvalidTime) {
		this.fvalidTime = fvalidTime;
	}

	@Column(name = "fAuditMessage", length = 2048)
	public String getFauditMessage() {
		return fauditMessage;
	}

	public void setFauditMessage(String fauditMessage) {
		this.fauditMessage = fauditMessage;
	}

	@Column(name = "fAuditTime", length = 19)
	public Date getFauditTime() {
		return fauditTime;
	}

	public void setFauditTime(Date fauditTime) {
		this.fauditTime = fauditTime;
	}

	@Column(name = "fOperator")
	public Long getFoperator() {
		return foperator;
	}

	public void setFoperator(Long foperator) {
		this.foperator = foperator;
	}

	@Column(name = "fTargetObject")
	public String getFtargetObject() {
		return ftargetObject;
	}

	public void setFtargetObject(String ftargetObject) {
		this.ftargetObject = ftargetObject;
	}

	@Column(name = "fPageTitle")
	public String getFpageTitle() {
		return fpageTitle;
	}

	public void setFpageTitle(String fpageTitle) {
		this.fpageTitle = fpageTitle;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TPush")
	public Set<TPushCustomer> getTPushCustomers() {
		return this.TPushCustomers;
	}

	public void setTPushCustomers(Set<TPushCustomer> TPushCustomers) {
		this.TPushCustomers = TPushCustomers;
	}

}