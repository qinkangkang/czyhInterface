package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_scene_scan_info")
public class TSceneScanInfo extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String fsceneCode;
	private String fopenId;
	private Integer fscanType;
	private Date fsstime;
	private Date fcreateTime;

	// Constructors

	/** default constructor */
	public TSceneScanInfo() {
	}

	/** minimal constructor */
	public TSceneScanInfo(String id) {
		this.id = id;
	}

	// Property accessors
	@Column(name = "fSceneCode")
	public String getFsceneCode() {
		return this.fsceneCode;
	}

	public void setFsceneCode(String fsceneCode) {
		this.fsceneCode = fsceneCode;
	}

	@Column(name = "fOpenID")
	public String getFopenId() {
		return this.fopenId;
	}

	public void setFopenId(String fopenId) {
		this.fopenId = fopenId;
	}

	@Column(name = "fScanType")
	public Integer getFscanType() {
		return this.fscanType;
	}

	public void setFscanType(Integer fscanType) {
		this.fscanType = fscanType;
	}

	@Column(name = "fSSTime", length = 19)
	public Date getFsstime() {
		return this.fsstime;
	}

	public void setFsstime(Date fsstime) {
		this.fsstime = fsstime;
	}

	@Column(name = "fCreateTime", length = 19)
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

}