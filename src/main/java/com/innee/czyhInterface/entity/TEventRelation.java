package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_goods_relation")
public class TEventRelation extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String feventId;
	private String fbyEventId;
	private Integer frelationType;
	private Integer forder;
	private Date fcreateTime;

	// Constructors

	/** default constructor */
	public TEventRelation() {
	}

	/** minimal constructor */
	public TEventRelation(String id) {
		this.id = id;
	}

	// Property accessors
	@Column(name = "fEventId", length = 36)
	public String getFeventId() {
		return this.feventId;
	}

	public void setFeventId(String feventId) {
		this.feventId = feventId;
	}

	@Column(name = "fByEventId", length = 36)
	public String getFbyEventId() {
		return this.fbyEventId;
	}

	public void setFbyEventId(String fbyEventId) {
		this.fbyEventId = fbyEventId;
	}

	@Column(name = "fRelationType")
	public Integer getFrelationType() {
		return this.frelationType;
	}

	public void setFrelationType(Integer frelationType) {
		this.frelationType = frelationType;
	}

	@Column(name = "fOrder")
	public Integer getForder() {
		return this.forder;
	}

	public void setForder(Integer forder) {
		this.forder = forder;
	}

	@Column(name = "fCreateTime", length = 19)
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

}