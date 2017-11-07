package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_encrypt_ret")
public class TEncryptRet extends IdEntity {

	private static final long serialVersionUID = 1L;

	private String fretToken;
	private Integer fstatus;
	private Date fupdateTime;

	// Constructors

	/** default constructor */
	public TEncryptRet() {
	}

	/** minimal constructor */
	public TEncryptRet(Long id) {
		this.id = id;
	}

	@Column(name = "fRetToken")
	public String getFretToken() {
		return this.fretToken;
	}

	public void setFretToken(String fretToken) {
		this.fretToken = fretToken;
	}

	@Column(name = "fStatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fUpdateTime", length = 19)
	public Date getFupdateTime() {
		return this.fupdateTime;
	}

	public void setFupdateTime(Date fupdateTime) {
		this.fupdateTime = fupdateTime;
	}

}