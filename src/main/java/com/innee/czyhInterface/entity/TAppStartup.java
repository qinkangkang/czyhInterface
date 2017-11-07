package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_app_startup")
public class TAppStartup extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String fcustomerId;
	private String fgps;
	private Date fstartupTime;
	private Long fserialNumber;
	private Integer fclientType;
	private String fclientVersion;
	private String fclientDeviceId;
	private String fclientDeviceInfo;

	// Constructors

	/** default constructor */
	public TAppStartup() {
	}

	/** minimal constructor */
	public TAppStartup(String id) {
		this.id = id;
	}

	// Property accessors
	@Column(name = "fCustomerId", length = 36)
	public String getFcustomerId() {
		return this.fcustomerId;
	}

	public void setFcustomerId(String fcustomerId) {
		this.fcustomerId = fcustomerId;
	}

	@Column(name = "fClientType")
	public Integer getFclientType() {
		return this.fclientType;
	}

	public void setFclientType(Integer fclientType) {
		this.fclientType = fclientType;
	}

	@Column(name = "fGps")
	public String getFgps() {
		return this.fgps;
	}

	public void setFgps(String fgps) {
		this.fgps = fgps;
	}

	@Column(name = "fStartupTime", length = 19)
	public Date getFstartupTime() {
		return this.fstartupTime;
	}

	public void setFstartupTime(Date fstartupTime) {
		this.fstartupTime = fstartupTime;
	}

	@Column(name = "fSerialNumber")
	public Long getFserialNumber() {
		return this.fserialNumber;
	}

	public void setFserialNumber(Long fserialNumber) {
		this.fserialNumber = fserialNumber;
	}

	@Column(name = "fClientVersion")
	public String getFclientVersion() {
		return this.fclientVersion;
	}

	public void setFclientVersion(String fclientVersion) {
		this.fclientVersion = fclientVersion;
	}

	@Column(name = "fClientDeviceId")
	public String getFclientDeviceId() {
		return this.fclientDeviceId;
	}

	public void setFclientDeviceId(String fclientDeviceId) {
		this.fclientDeviceId = fclientDeviceId;
	}

	@Column(name = "fClientDeviceInfo", length = 2048)
	public String getFclientDeviceInfo() {
		return this.fclientDeviceInfo;
	}

	public void setFclientDeviceInfo(String fclientDeviceInfo) {
		this.fclientDeviceInfo = fclientDeviceInfo;
	}

}