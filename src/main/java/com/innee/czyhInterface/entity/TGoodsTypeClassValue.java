package com.innee.czyhInterface.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "t_goods_typeClassValue")
public class TGoodsTypeClassValue extends IdEntity {

	
	private static final long serialVersionUID = 1L;
	
	private String fvalue;
	private Integer fextendClassId;
	private String fextendClassName;
	private Integer fsponsorId;
	private Integer fstatus;
	private Long fcreaterId;
	private Date fupdateTime;
	private Date fcreateTime;

	// Constructors

	/** default constructor */
	public TGoodsTypeClassValue() {
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "fValue")
	public String getFvalue() {
		return this.fvalue;
	}

	public void setFvalue(String fvalue) {
		this.fvalue = fvalue;
	}

	@Column(name = "fExtendClassId")
	public Integer getFextendClassId() {
		return this.fextendClassId;
	}

	public void setFextendClassId(Integer fextendClassId) {
		this.fextendClassId = fextendClassId;
	}

	@Column(name = "fSponsorId")
	public Integer getFsponsorId() {
		return this.fsponsorId;
	}

	public void setFsponsorId(Integer fsponsorId) {
		this.fsponsorId = fsponsorId;
	}

	@Column(name = "fStatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fCreaterID")
	public Long getFcreaterId() {
		return this.fcreaterId;
	}

	public void setFcreaterId(Long fcreaterId) {
		this.fcreaterId = fcreaterId;
	}

	@Column(name = "fUpdateTime", length = 19)
	public Date getFupdateTime() {
		return this.fupdateTime;
	}

	public void setFupdateTime(Date fupdateTime) {
		this.fupdateTime = fupdateTime;
	}

	@Column(name = "fCreateTime", length = 19)
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "fExtendClassName")
	public String getFextendClassName() {
		return fextendClassName;
	}

	public void setFextendClassName(String fextendClassName) {
		this.fextendClassName = fextendClassName;
	}

}