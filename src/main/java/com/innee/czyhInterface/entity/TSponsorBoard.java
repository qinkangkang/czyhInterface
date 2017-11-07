package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_sponsor_board")
public class TSponsorBoard extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String ftitle;
	private Date ftime;
	private String fgps;
	private String fbrief;
	private String fdetail;

	// Constructors

	/** default constructor */
	public TSponsorBoard() {
	}

	/** minimal constructor */
	public TSponsorBoard(String id) {
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

	@Column(name = "fTime", length = 19)
	public Date getFtime() {
		return this.ftime;
	}

	public void setFtime(Date ftime) {
		this.ftime = ftime;
	}

	@Column(name = "fGPS", length = 2048)
	public String getFgps() {
		return this.fgps;
	}

	public void setFgps(String fgps) {
		this.fgps = fgps;
	}

	@Column(name = "fBrief", length = 2048)
	public String getFbrief() {
		return this.fbrief;
	}

	public void setFbrief(String fbrief) {
		this.fbrief = fbrief;
	}

	@Column(name = "fDetail")
	public String getFdetail() {
		return this.fdetail;
	}

	public void setFdetail(String fdetail) {
		this.fdetail = fdetail;
	}

}