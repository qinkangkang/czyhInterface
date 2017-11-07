package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "t_subject")
public class TSubject extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String ftitle;
	private String fdetail;
	private String fdetailHtmlUrl;
	private Integer fstatus;
	private Date fcreateTime;
	private Date fupdateTime;

	// Constructors

	/** default constructor */
	public TSubject() {
	}

	/** minimal constructor */
	public TSubject(String id) {
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

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "fDetail", columnDefinition = "longtext")
	public String getFdetail() {
		return this.fdetail;
	}

	public void setFdetail(String fdetail) {
		this.fdetail = fdetail;
	}

	@Column(name = "fDetailHtmlUrl", length = 2048)
	public String getFdetailHtmlUrl() {
		return this.fdetailHtmlUrl;
	}

	public void setFdetailHtmlUrl(String fdetailHtmlUrl) {
		this.fdetailHtmlUrl = fdetailHtmlUrl;
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