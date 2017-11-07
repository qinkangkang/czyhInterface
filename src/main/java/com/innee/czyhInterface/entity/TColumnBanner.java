package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.joda.time.DateTime;

@Entity
@Table(name = "t_column_banner")
public class TColumnBanner extends UuidEntity {

	private static final long serialVersionUID = 1L;

	private Integer ftag;
	private String fimageUrl;
	private String fchannelId;
	private Date fseckillTime;
	private Integer fstatus;
	private Integer ftype;

	// Constructors

	/** default constructor */
	public TColumnBanner() {
	}

	/** minimal constructor */
	public TColumnBanner(String id) {
		this.id = id;
	}

	@Column(name = "fTag")
	public Integer getFtag() {
		return this.ftag;
	}

	public void setFtag(Integer ftag) {
		this.ftag = ftag;
	}

	@Column(name = "fStatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fImageUrl")
	public String getFimageUrl() {
		return fimageUrl;
	}

	public void setFimageUrl(String fimageUrl) {
		this.fimageUrl = fimageUrl;
	}

	@Column(name = "fChannelId")
	public String getFchannelId() {
		return fchannelId;
	}

	public void setFchannelId(String fchannelId) {
		this.fchannelId = fchannelId;
	}

	@Column(name = "fSeckillTime")
	public Date getFseckillTime() {
		return fseckillTime;
	}

	public void setFseckillTime(Date fseckillTime) {
		this.fseckillTime = fseckillTime;
	}

	@Column(name = "fType")
	public Integer getFtype() {
		return ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}

}