package com.innee.czyhInterface.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "t_dictionary")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TDictionary extends IdEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private TDictionaryClass TDictionaryClass;
	private String name;
	private String code;
	private Integer value;
	private Integer status;

	// Constructors

	/** default constructor */
	public TDictionary() {
	}

	/** minimal constructor */
	public TDictionary(Long id, TDictionaryClass TDictionaryClass) {
		this.id = id;
		this.TDictionaryClass = TDictionaryClass;
	}

	// Property accessors
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "class_id", nullable = false)
	public TDictionaryClass getTDictionaryClass() {
		return this.TDictionaryClass;
	}

	public void setTDictionaryClass(TDictionaryClass TDictionaryClass) {
		this.TDictionaryClass = TDictionaryClass;
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

	@Column(name = "status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}