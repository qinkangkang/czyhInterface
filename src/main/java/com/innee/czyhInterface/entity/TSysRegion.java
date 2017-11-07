package com.innee.czyhInterface.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * TSysRegion entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_sys_region")
public class TSysRegion implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1L;
	private Integer regionId;
	private TSysCity TSysCity;
	private String regionName;
	private String isDisabled;
	private String regionDesc;

	// Constructors

	/** default constructor */
	public TSysRegion() {
	}

	/** full constructor */
	public TSysRegion(TSysCity TSysCity, String regionName, String isDisabled, String regionDesc) {
		this.TSysCity = TSysCity;
		this.regionName = regionName;
		this.isDisabled = isDisabled;
		this.regionDesc = regionDesc;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "region_id", unique = true, nullable = false)
	public Integer getRegionId() {
		return this.regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "city_id")
	public TSysCity getTSysCity() {
		return this.TSysCity;
	}

	public void setTSysCity(TSysCity TSysCity) {
		this.TSysCity = TSysCity;
	}

	@Column(name = "region_name")
	public String getRegionName() {
		return this.regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	@Column(name = "is_disabled")
	public String getIsDisabled() {
		return this.isDisabled;
	}

	public void setIsDisabled(String isDisabled) {
		this.isDisabled = isDisabled;
	}

	@Column(name = "region_desc")
	public String getRegionDesc() {
		return this.regionDesc;
	}

	public void setRegionDesc(String regionDesc) {
		this.regionDesc = regionDesc;
	}

}