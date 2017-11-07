package com.innee.czyhInterface.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "t_goods_category")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TEventCategory extends IdEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String name;
	private String code;
	private Integer level;
	private Integer value;
	private Long parentId;
	private String imageA;
	private String imageB;

	// Constructors

	/** default constructor */
	public TEventCategory() {
	}

	public TEventCategory(Long id) {
		this.id = id;
	}

	// Property accessors
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

	@Column(name = "level")
	public Integer getLevel() {
		return this.level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Column(name = "value")
	public Integer getValue() {
		return this.value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	@Column(name = "parentId")
	public Long getParentId() {
		return this.parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Column(name = "imageA")
	public String getImageA() {
		return this.imageA;
	}

	public void setImageA(String imageA) {
		this.imageA = imageA;
	}

	@Column(name = "imageB")
	public String getImageB() {
		return imageB;
	}

	public void setImageB(String imageB) {
		this.imageB = imageB;
	}

}