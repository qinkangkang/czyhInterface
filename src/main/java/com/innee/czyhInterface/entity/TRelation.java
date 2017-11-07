package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_relation")
public class TRelation extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String fcustomerId;
	private String fbyCustomerId;
	private String fcsid;
	private Integer frelationType;
	private Date fcreateTime;

	// Constructors

	/** default constructor */
	public TRelation() {
	}

	/** minimal constructor */
	public TRelation(String id) {
		this.id = id;
	}

	// Property accessors
	@Column(name = "fCustomerId", length = 36)
	public String getFcustomerId() {
		return this.fcustomerId;
	}

	public void setFcustomerId(String fcustomerId) {
		this.fcustomerId = fcustomerId;
	}

	@Column(name = "fByCustomerId", length = 36)
	public String getFbyCustomerId() {
		return this.fbyCustomerId;
	}

	public void setFbyCustomerId(String fbyCustomerId) {
		this.fbyCustomerId = fbyCustomerId;
	}

	@Column(name = "fCSID", length = 36)
	public String getFcsid() {
		return this.fcsid;
	}

	public void setFcsid(String fcsid) {
		this.fcsid = fcsid;
	}

	@Column(name = "fRelationType")
	public Integer getFrelationType() {
		return this.frelationType;
	}

	public void setFrelationType(Integer frelationType) {
		this.frelationType = frelationType;
	}

	@Column(name = "fCreateTime", length = 19)
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

}