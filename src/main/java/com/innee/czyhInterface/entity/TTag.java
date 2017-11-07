package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TTag entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_tag")
public class TTag extends UuidEntity {

	private static final long serialVersionUID = 1L;

	private Integer ftag;
	private Integer ftype;
	private String fimageUrl;
	private String fdes;
	private Integer forder;
	private Integer fisDisplay;
	private Date fcreateTime;

	// Constructors

	/** default constructor */
	public TTag() {
	}

	/** minimal constructor */
	public TTag(String id) {
		this.id = id;
	}

	@Column(name = "fTag")
	public Integer getFtag() {
		return this.ftag;
	}

	public void setFtag(Integer ftag) {
		this.ftag = ftag;
	}

	@Column(name = "fType")
	public Integer getFtype() {
		return this.ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}

	@Column(name = "fImageUrl", nullable = false)
	public String getFimageUrl() {
		return this.fimageUrl;
	}

	public void setFimageUrl(String fimageUrl) {
		this.fimageUrl = fimageUrl;
	}

	@Column(name = "fDes", nullable = false)
	public String getFdes() {
		return this.fdes;
	}

	public void setFdes(String fdes) {
		this.fdes = fdes;
	}

	@Column(name = "fOrder")
	public Integer getForder() {
		return this.forder;
	}

	public void setForder(Integer forder) {
		this.forder = forder;
	}

	@Column(name = "fCreateTime", length = 19)
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "fIsDisplay")
	public Integer getFisDisplay() {
		return fisDisplay;
	}

	public void setFisDisplay(Integer fisDisplay) {
		this.fisDisplay = fisDisplay;
	}

}