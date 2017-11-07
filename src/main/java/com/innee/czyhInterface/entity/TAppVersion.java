package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_app_version")
public class TAppVersion extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private Integer fappType;
	private Date freleaseDate;
	private String fversionNum;
	private Long fversionValue;
	private Long fforceUpgradeValue;
	private String fdescription;
	private String fappUrl;
	private Date fcreateTime;
	private Integer fpathway;

	// Constructors

	/** default constructor */
	public TAppVersion() {
	}

	/** minimal constructor */
	public TAppVersion(String id) {
		this.id = id;
	}

	// Property accessors
	@Column(name = "fAppType")
	public Integer getFappType() {
		return this.fappType;
	}

	public void setFappType(Integer fappType) {
		this.fappType = fappType;
	}

	@Column(name = "fReleaseDate", length = 19)
	public Date getFreleaseDate() {
		return this.freleaseDate;
	}

	public void setFreleaseDate(Date freleaseDate) {
		this.freleaseDate = freleaseDate;
	}

	@Column(name = "fVersionNum")
	public String getFversionNum() {
		return this.fversionNum;
	}

	public void setFversionNum(String fversionNum) {
		this.fversionNum = fversionNum;
	}

	@Column(name = "fVersionValue")
	public Long getFversionValue() {
		return this.fversionValue;
	}

	public void setFversionValue(Long fversionValue) {
		this.fversionValue = fversionValue;
	}

	@Column(name = "fForceUpgradeValue")
	public Long getFforceUpgradeValue() {
		return this.fforceUpgradeValue;
	}

	public void setFforceUpgradeValue(Long fforceUpgradeValue) {
		this.fforceUpgradeValue = fforceUpgradeValue;
	}

	@Column(name = "fDescription", length = 2048)
	public String getFdescription() {
		return this.fdescription;
	}

	public void setFdescription(String fdescription) {
		this.fdescription = fdescription;
	}

	@Column(name = "fAppUrl")
	public String getFappUrl() {
		return this.fappUrl;
	}

	public void setFappUrl(String fappUrl) {
		this.fappUrl = fappUrl;
	}

	@Column(name = "fCreateTime", length = 19)
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}
	@Column(name = "fPathway", length = 19)
	public Integer getFpathway() {
		return fpathway;
	}

	public void setFpathway(Integer fpathway) {
		this.fpathway = fpathway;
	}

}