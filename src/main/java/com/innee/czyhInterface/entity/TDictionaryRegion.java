package com.innee.czyhInterface.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_dictionary_region")
public class TDictionaryRegion extends IdEntity {

	private static final long serialVersionUID = 1L;
	private Integer cityValue;
	private String name;
	private Integer value;
	private Integer fstatus;

	// Constructors

	/** default constructor */
	public TDictionaryRegion() {
	}

	/** minimal constructor */
	public TDictionaryRegion(Long id) {
		this.id = id;
	}

	@Column(name = "cityValue", nullable = false)
	public Integer getCityValue() {
		return this.cityValue;
	}

	public void setCityValue(Integer cityValue) {
		this.cityValue = cityValue;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "value", unique = true, nullable = false)
	public Integer getValue() {
		return this.value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	@Column(name = "fStatus", nullable = false)
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

}