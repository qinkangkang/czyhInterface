package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_auto_show")
public class TAutoShow extends UuidEntity {

	private static final long serialVersionUID = 1L;

	private Date fcreateTime;
	private String fname;
	private String fphone;

	// Constructors

	/** default constructor */
	public TAutoShow() {
	}

	/** minimal constructor */
	public TAutoShow(String id) {
		this.id = id;
	}

	@Column(name = "fCreateTime", length = 19)
	public Date getFcreateTime() {
		return fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "fName", length = 19)
	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	@Column(name = "fPhone", length = 19)
	public String getFphone() {
		return fphone;
	}

	public void setFphone(String fphone) {
		this.fphone = fphone;
	}

}