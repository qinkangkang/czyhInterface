package com.innee.czyhInterface.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_favorite")
public class TFavorite extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private TCustomer TCustomer;
	private String fobjectId;
	private Integer ftype;

	// Constructors

	/** default constructor */
	public TFavorite() {
	}

	/** minimal constructor */
	public TFavorite(String id) {
		this.id = id;
	}

	// Property accessors
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fCustomerID")
	public TCustomer getTCustomer() {
		return this.TCustomer;
	}

	public void setTCustomer(TCustomer TCustomer) {
		this.TCustomer = TCustomer;
	}

	@Column(name = "fObjectID", length = 36)
	public String getFobjectId() {
		return this.fobjectId;
	}

	public void setFobjectId(String fobjectId) {
		this.fobjectId = fobjectId;
	}

	@Column(name = "fType")
	public Integer getFtype() {
		return this.ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}

}