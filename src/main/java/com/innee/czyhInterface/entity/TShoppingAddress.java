package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "t_shopping_address")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TShoppingAddress extends UuidEntity {

	private static final long serialVersionUID = 1L;

	private String fcustomerId;
	private String fname;
	private String fphone;
	private String fregion;
	private String fstreet;
	private String faddress;
	private Integer fdefault;
	private Integer fstauts;
	private Integer fcityId;
	private Date fcreateTime;
	private Date fupdateTime;

	// Constructors

	/** default constructor */
	public TShoppingAddress() {
	}

	/** minimal constructor */
	public TShoppingAddress(String id) {
		this.id = id;
	}

	@Column(name = "fCustomerId", length = 36)
	public String getFcustomerId() {
		return this.fcustomerId;
	}

	public void setFcustomerId(String fcustomerId) {
		this.fcustomerId = fcustomerId;
	}

	@Column(name = "fName", length = 72)
	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	@Column(name = "fPhone", length = 11)
	public String getFphone() {
		return this.fphone;
	}

	public void setFphone(String fphone) {
		this.fphone = fphone;
	}

	@Column(name = "fRegion")
	public String getFregion() {
		return this.fregion;
	}

	public void setFregion(String fregion) {
		this.fregion = fregion;
	}

	@Column(name = "fStreet")
	public String getFstreet() {
		return this.fstreet;
	}

	public void setFstreet(String fstreet) {
		this.fstreet = fstreet;
	}

	@Column(name = "fAddress")
	public String getFaddress() {
		return this.faddress;
	}

	public void setFaddress(String faddress) {
		this.faddress = faddress;
	}

	@Column(name = "fdefault")
	public Integer getFdefault() {
		return this.fdefault;
	}

	public void setFdefault(Integer fdefault) {
		this.fdefault = fdefault;
	}

	@Column(name = "fCreateTime", length = 19)
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "fUpdateTime", length = 19)
	public Date getFupdateTime() {
		return this.fupdateTime;
	}

	public void setFupdateTime(Date fupdateTime) {
		this.fupdateTime = fupdateTime;
	}

	@Column(name = "fstauts", length = 19)
	public Integer getFstauts() {
		return fstauts;
	}

	public void setFstauts(Integer fstauts) {
		this.fstauts = fstauts;
	}

	@Column(name = "fCityId", length = 19)
	public Integer getFcityId() {
		return fcityId;
	}

	public void setFcityId(Integer fcityId) {
		this.fcityId = fcityId;
	}

}