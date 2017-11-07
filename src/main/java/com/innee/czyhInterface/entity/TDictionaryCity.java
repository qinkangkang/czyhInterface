package com.innee.czyhInterface.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_dictionary_city")
public class TDictionaryCity extends IdEntity {

	private static final long serialVersionUID = 1L;
	// Fields

	private String name;
	private String code;
	private Integer value;

	/** default constructor */
	public TDictionaryCity() {
	}

	/** minimal constructor */
	public TDictionaryCity(Long id) {
		this.id = id;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "code")
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "value")
	public Integer getValue() {
		return this.value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

}