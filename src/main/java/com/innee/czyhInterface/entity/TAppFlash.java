package com.innee.czyhInterface.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "t_app_flash")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TAppFlash extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private Integer fcity;
	private Integer fisVisible;
	private String fimage;
	private Integer furlType;
	private String fentityId;
	private String fentityTitle;
	private String fexternalUrl;
	private Integer fhtmlType;

	// Constructors

	/** default constructor */
	public TAppFlash() {
	}

	/** minimal constructor */
	public TAppFlash(String id) {
		this.id = id;
	}

	// Property accessors
	@Column(name = "fCity")
	public Integer getFcity() {
		return this.fcity;
	}

	public void setFcity(Integer fcity) {
		this.fcity = fcity;
	}

	@Column(name = "fIsVisible")
	public Integer getFisVisible() {
		return this.fisVisible;
	}

	public void setFisVisible(Integer fisVisible) {
		this.fisVisible = fisVisible;
	}

	@Column(name = "fImage")
	public String getFimage() {
		return this.fimage;
	}

	public void setFimage(String fimage) {
		this.fimage = fimage;
	}

	@Column(name = "fUrlType")
	public Integer getFurlType() {
		return this.furlType;
	}

	public void setFurlType(Integer furlType) {
		this.furlType = furlType;
	}

	@Column(name = "fEntityId", length = 36)
	public String getFentityId() {
		return this.fentityId;
	}

	public void setFentityId(String fentityId) {
		this.fentityId = fentityId;
	}

	@Column(name = "fEntityTitle")
	public String getFentityTitle() {
		return this.fentityTitle;
	}

	public void setFentityTitle(String fentityTitle) {
		this.fentityTitle = fentityTitle;
	}

	@Column(name = "fExternalUrl")
	public String getFexternalUrl() {
		return this.fexternalUrl;
	}

	public void setFexternalUrl(String fexternalUrl) {
		this.fexternalUrl = fexternalUrl;
	}

	@Column(name = "fHtmlType")
	public Integer getFhtmlType() {
		return fhtmlType;
	}

	public void setFhtmlType(Integer fhtmlType) {
		this.fhtmlType = fhtmlType;
	}

}