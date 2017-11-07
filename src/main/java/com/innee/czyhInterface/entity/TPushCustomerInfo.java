package com.innee.czyhInterface.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_push_customer_info")
public class TPushCustomerInfo extends UuidEntity {

	private static final long serialVersionUID = 1L;

	private String fcustomerId;
	private String ftitle;
	private String fcontent;
	private String fimage;
	private String ftype;
	private Integer ftargetType;
	private String ftargetObject;
	private Date fpushTime;
	private String fdescription;
	private String fpageTitle;
	private Integer funread;
	private Integer fstatus;
	private Date fcreateTime;

	// Constructors

	/** default constructor */
	public TPushCustomerInfo() {
	}

	@Column(name = "fCustomerId", length = 36)
	public String getFcustomerId() {
		return this.fcustomerId;
	}

	public void setFcustomerId(String fcustomerId) {
		this.fcustomerId = fcustomerId;
	}

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
	public String getFtype() {
		return this.ftype;
	}

	public void setFtype(String ftype) {
		this.ftype = ftype;
	}

	@Column(name = "fTargetType")
	public Integer getFtargetType() {
		return this.ftargetType;
	}

	public void setFtargetType(Integer ftargetType) {
		this.ftargetType = ftargetType;
	}

	@Column(name = "fTargetObject")
	public String getFtargetObject() {
		return this.ftargetObject;
	}

	public void setFtargetObject(String ftargetObject) {
		this.ftargetObject = ftargetObject;
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

	@Column(name = "fPageTitle")
	public String getFpageTitle() {
		return this.fpageTitle;
	}

	public void setFpageTitle(String fpageTitle) {
		this.fpageTitle = fpageTitle;
	}

	@Column(name = "fUnread")
	public Integer getFunread() {
		return this.funread;
	}

	public void setFunread(Integer funread) {
		this.funread = funread;
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

}