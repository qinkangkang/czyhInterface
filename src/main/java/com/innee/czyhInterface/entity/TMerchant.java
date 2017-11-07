package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_merchant")
public class TMerchant extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private TSponsor TSponsor;
	private String fusername;
	private String fname;
	private String fphone;
	private String fpassword;
	private String fsalt;
	private Integer fstatus;
	private String fticket;
	private Integer frole;
	private Date fcreateTime;
	private Date fupdateTime;

	// Constructors

	/** default constructor */
	public TMerchant() {
	}

	/** minimal constructor */
	public TMerchant(String id) {
		this.id = id;
	}

	// Property accessors
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fSponsorId")
	public TSponsor getTSponsor() {
		return this.TSponsor;
	}

	public void setTSponsor(TSponsor TSponsor) {
		this.TSponsor = TSponsor;
	}

	@Column(name = "fUsername")
	public String getFusername() {
		return this.fusername;
	}

	public void setFusername(String fusername) {
		this.fusername = fusername;
	}

	@Column(name = "fName")
	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	@Column(name = "fPhone")
	public String getFphone() {
		return this.fphone;
	}

	public void setFphone(String fphone) {
		this.fphone = fphone;
	}

	@Column(name = "fPassword")
	public String getFpassword() {
		return this.fpassword;
	}

	public void setFpassword(String fpassword) {
		this.fpassword = fpassword;
	}

	@Column(name = "fSalt")
	public String getFsalt() {
		return this.fsalt;
	}

	public void setFsalt(String fsalt) {
		this.fsalt = fsalt;
	}

	@Column(name = "fStatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fTicket", length = 64)
	public String getFticket() {
		return this.fticket;
	}

	public void setFticket(String fticket) {
		this.fticket = fticket;
	}

	@Column(name = "fRole")
	public Integer getFrole() {
		return this.frole;
	}

	public void setFrole(Integer frole) {
		this.frole = frole;
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