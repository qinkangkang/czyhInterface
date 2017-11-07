package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_goods_typeClass")
public class TGoodsTypeClass extends IdEntity {

	private static final long serialVersionUID = 1L;

	private String fclassName;
	private Integer fsponsorId;
	private Integer fstatus;
	private Long fcreaterId;
	private Date fupdateTime;
	private Date fcreateTime;

	// Constructors

	/** default constructor */
	public TGoodsTypeClass() {
	}

	@Column(name = "fClassName")
	public String getFclassName() {
		return this.fclassName;
	}

	public void setFclassName(String fclassName) {
		this.fclassName = fclassName;
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

}