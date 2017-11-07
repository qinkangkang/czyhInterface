package com.innee.czyhInterface.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_order_ext_info")
public class TOrderExtInfo extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String forderId;
	private Integer forder;
	private String fname;
	private String fprompt;
	private String fvalue;
	private Integer fisRequired;
	private Integer fisEveryone;

	// Constructors

	/** default constructor */
	public TOrderExtInfo() {
	}

	/** minimal constructor */
	public TOrderExtInfo(String id) {
		this.id = id;
	}

	// Property accessors
	@Column(name = "fOrderId", length = 36)
	public String getForderId() {
		return this.forderId;
	}

	public void setForderId(String forderId) {
		this.forderId = forderId;
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

	@Column(name = "fValue", length = 2048)
	public String getFvalue() {
		return this.fvalue;
	}

	public void setFvalue(String fvalue) {
		this.fvalue = fvalue;
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

}