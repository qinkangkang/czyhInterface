package com.innee.czyhInterface.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_configuration")
public class TConfiguration extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private Integer fcityId;
	private String fname;
	private String fkey;
	private String fvalue;

	// Constructors

	/** default constructor */
	public TConfiguration() {
	}

	/** minimal constructor */
	public TConfiguration(String id) {
		this.id = id;
	}

	// Property accessors
	@Column(name = "fCityId")
	public Integer getFcityId() {
		return this.fcityId;
	}

	public void setFcityId(Integer fcityId) {
		this.fcityId = fcityId;
	}

	@Column(name = "fName", length = 36)
	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	@Column(name = "fKey", length = 36)
	public String getFkey() {
		return this.fkey;
	}

	public void setFkey(String fkey) {
		this.fkey = fkey;
	}

	@Column(name = "fValue", length = 2048)
	public String getFvalue() {
		return this.fvalue;
	}

	public void setFvalue(String fvalue) {
		this.fvalue = fvalue;
	}

}