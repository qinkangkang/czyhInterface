package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_public_images")
public class TPublicImages extends UuidEntity {

	private static final long serialVersionUID = 1L;
	
	private String furl;
	private Integer ftype;
	private Integer isThumbnail;
	private String thumbnailUrl;
	private Integer fstatus;
	private Date fcreateTime;

	// Constructors

	/** default constructor */
	public TPublicImages() {
	}

	/** minimal constructor */
	public TPublicImages(String id) {
		this.id = id;
	}

	/** full constructor */
	public TPublicImages(String furl, Integer ftype,
			Integer isThumbnail, String thumbnailUrl, Integer fstatus,
			Date fcreateTime) {
		this.furl = furl;
		this.ftype = ftype;
		this.isThumbnail = isThumbnail;
		this.thumbnailUrl = thumbnailUrl;
		this.fstatus = fstatus;
		this.fcreateTime = fcreateTime;
	}

	// Property accessors

	@Column(name = "fUrl", length = 2048)
	public String getFurl() {
		return this.furl;
	}

	public void setFurl(String furl) {
		this.furl = furl;
	}

	@Column(name = "ftype")
	public Integer getFtype() {
		return this.ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}

	@Column(name = "isThumbnail")
	public Integer getIsThumbnail() {
		return this.isThumbnail;
	}

	public void setIsThumbnail(Integer isThumbnail) {
		this.isThumbnail = isThumbnail;
	}

	@Column(name = "thumbnailUrl", length = 2048)
	public String getThumbnailUrl() {
		return this.thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	@Column(name = "fstatus")
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