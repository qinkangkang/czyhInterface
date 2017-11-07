package com.innee.czyhInterface.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_goods_ext_info")
public class TEventExtInfo extends UuidEntity {

	private static final long serialVersionUID = 1L;
	// Fields
	private String feventId;
	private Integer forder;
	private String fname;
	private String fprompt;
	private Integer fisRequired;
	private Integer fisEveryone;
	private Integer fstatus;

	// Constructors

	/** default constructor */
	public TEventExtInfo() {
	}

	/** minimal constructor */
	public TEventExtInfo(String id) {
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

	@Column(name = "fOrder")
	public Integer getForder() {
		return this.forder;
	}

	public void setForder(Integer forder) {
		this.forder = forder;
	}

	@Column(name = "fName")
	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	@Column(name = "fPrompt")
	public String getFprompt() {
		return this.fprompt;
	}

	public void setFprompt(String fprompt) {
		this.fprompt = fprompt;
	}

	@Column(name = "fIsRequired")
	public Integer getFisRequired() {
		return this.fisRequired;
	}

	public void setFisRequired(Integer fisRequired) {
		this.fisRequired = fisRequired;
	}

	@Column(name = "fIsEveryone")
	public Integer getFisEveryone() {
		return this.fisEveryone;
	}

	public void setFisEveryone(Integer fisEveryone) {
		this.fisEveryone = fisEveryone;
	}

	@Column(name = "fStatus")
	public Integer getFstatus() {
		return fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	
}