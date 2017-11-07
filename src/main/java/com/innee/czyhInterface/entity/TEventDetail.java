package com.innee.czyhInterface.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_goods_detail")
public class TEventDetail extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private TEvent TEvent;
	private Integer ftype;
	private String fcontent;
	private Integer ftypeicon;
	private Integer forder;

	// Constructors

	/** default constructor */
	public TEventDetail() {
	}

	/** minimal constructor */
	public TEventDetail(String id) {
		this.id = id;
	}

	// Property accessors
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fEventID")
	public TEvent getTEvent() {
		return this.TEvent;
	}

	public void setTEvent(TEvent TEvent) {
		this.TEvent = TEvent;
	}

	@Column(name = "fType")
	public Integer getFtype() {
		return this.ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}

	@Column(name = "fContent", length = 2048)
	public String getFcontent() {
		return this.fcontent;
	}

	public void setFcontent(String fcontent) {
		this.fcontent = fcontent;
	}

	@Column(name = "fTypeicon")
	public Integer getFtypeicon() {
		return ftypeicon;
	}

	public void setFtypeicon(Integer ftypeicon) {
		this.ftypeicon = ftypeicon;
	}

	@Column(name = "fOrder")
	public Integer getForder() {
		return forder;
	}

	public void setForder(Integer forder) {
		this.forder = forder;
	}

}