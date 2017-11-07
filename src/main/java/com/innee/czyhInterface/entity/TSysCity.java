package com.innee.czyhInterface.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * TSysCity entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_sys_city")
public class TSysCity implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1L;
	private Integer cityId;
	private String cityName;
	private String isHot;
	private Integer proId;
	private String isDisabled;
	private String cityDesc;
	private String firstLetter;
	private Set<TSysRegion> TSysRegions = new HashSet<TSysRegion>(0);

	// Constructors

	/** default constructor */
	public TSysCity() {
	}

	/** full constructor */
	public TSysCity(String cityName, String isHot, Integer proId,
			String isDisabled, String cityDesc, String firstLetter,
			Set<TSysRegion> TSysRegions) {
		this.cityName = cityName;
		this.isHot = isHot;
		this.proId = proId;
		this.isDisabled = isDisabled;
		this.cityDesc = cityDesc;
		this.firstLetter = firstLetter;
		this.TSysRegions = TSysRegions;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "city_id", unique = true, nullable = false)
	public Integer getCityId() {
		return this.cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	@Column(name = "city_name", length = 10)
	public String getCityName() {
		return this.cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	@Column(name = "is_hot", length = 10)
	public String getIsHot() {
		return this.isHot;
	}

	public void setIsHot(String isHot) {
		this.isHot = isHot;
	}

	@Column(name = "pro_id")
	public Integer getProId() {
		return this.proId;
	}

	public void setProId(Integer proId) {
		this.proId = proId;
	}

	@Column(name = "is_disabled", length = 10)
	public String getIsDisabled() {
		return this.isDisabled;
	}

	public void setIsDisabled(String isDisabled) {
		this.isDisabled = isDisabled;
	}

	@Column(name = "city_desc", length = 10)
	public String getCityDesc() {
		return this.cityDesc;
	}

	public void setCityDesc(String cityDesc) {
		this.cityDesc = cityDesc;
	}

	@Column(name = "first_letter", length = 10)
	public String getFirstLetter() {
		return this.firstLetter;
	}

	public void setFirstLetter(String firstLetter) {
		this.firstLetter = firstLetter;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TSysCity")
	public Set<TSysRegion> getTSysRegions() {
		return this.TSysRegions;
	}

	public void setTSysRegions(Set<TSysRegion> TSysRegions) {
		this.TSysRegions = TSysRegions;
	}

}