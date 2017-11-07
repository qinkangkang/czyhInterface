package com.innee.czyhInterface.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_goods_typeClass_category")
public class TGoodsTypeClassCategory extends IdEntity {

	private static final long serialVersionUID = 1L;
	
	private Integer fcategoryId;
	private Integer ftypeId;

	// Constructors

	/** default constructor */
	public TGoodsTypeClassCategory() {
	}


	@Column(name = "fCategoryId")
	public Integer getFcategoryId() {
		return this.fcategoryId;
	}

	public void setFcategoryId(Integer fcategoryId) {
		this.fcategoryId = fcategoryId;
	}

	@Column(name = "fTypeId")
	public Integer getFtypeId() {
		return this.ftypeId;
	}

	public void setFtypeId(Integer ftypeId) {
		this.ftypeId = ftypeId;
	}

}