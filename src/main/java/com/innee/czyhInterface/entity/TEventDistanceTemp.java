package com.innee.czyhInterface.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "t_goods_distance_temp")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TEventDistanceTemp extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String fhsid;
	private String feventId;
	private Integer fdistance;
	private Integer fweight;
	private Long fcreateTime;

	// Constructors

	/** default constructor */
	public TEventDistanceTemp() {
	}

	/** minimal constructor */
	public TEventDistanceTemp(String id) {
		this.id = id;
	}

	// Property accessors
	@Column(name = "fHsid", length = 32)
	public String getFhsid() {
		return this.fhsid;
	}

	public void setFhsid(String fhsid) {
		this.fhsid = fhsid;
	}

	@Column(name = "fEventID", length = 36)
	public String getFeventId() {
		return this.feventId;
	}

	public void setFeventId(String feventId) {
		this.feventId = feventId;
	}

	@Column(name = "fDistance")
	public Integer getFdistance() {
		return this.fdistance;
	}

	public void setFdistance(Integer fdistance) {
		this.fdistance = fdistance;
	}

	@Column(name = "fWeight")
	public Integer getFweight() {
		return this.fweight;
	}

	public void setFweight(Integer fweight) {
		this.fweight = fweight;
	}

	@Column(name = "fCreateTime")
	public Long getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Long fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

}