package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_timed_spike")
public class TTimedSpike extends UuidEntity {

	private static final long serialVersionUID = 1L;

	private String ftitle;
	private Date fstartTime;
	private Date fendTime;
	private Integer fstatus;
	private Date fcreatetime;

	// Constructors

	/** default constructor */
	public TTimedSpike() {
	}

	/** minimal constructor */
	public TTimedSpike(String id) {
		this.id = id;
	}

	@Column(name = "fTitle")
	public String getFtitle() {
		return this.ftitle;
	}

	public void setFtitle(String ftitle) {
		this.ftitle = ftitle;
	}

	@Column(name = "fStartTime", length = 19)
	public Date getFstartTime() {
		return this.fstartTime;
	}

	public void setFstartTime(Date fstartTime) {
		this.fstartTime = fstartTime;
	}

	@Column(name = "fEndTime", length = 19)
	public Date getFendTime() {
		return this.fendTime;
	}

	public void setFendTime(Date fendTime) {
		this.fendTime = fendTime;
	}

	@Column(name = "fStatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fCreatetime", length = 19)
	public Date getFcreatetime() {
		return this.fcreatetime;
	}

	public void setFcreatetime(Date fcreatetime) {
		this.fcreatetime = fcreatetime;
	}

}