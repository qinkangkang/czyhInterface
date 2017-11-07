package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TCustomerLevel entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_customer_level")
public class TCustomerLevel extends UuidEntity {

	private static final long serialVersionUID = 1L;

	private String ftitle;
	private Integer flevel;
	private Integer fgrowthValue;
	private Date fupdateTime;
	private Date fcreateTime;

	/** default constructor */
	public TCustomerLevel() {
	}

	/** minimal constructor */
	public TCustomerLevel(String id) {
		this.id = id;
	}

	@Column(name = "fTitle")
	public String getFtitle() {
		return this.ftitle;
	}

	public void setFtitle(String ftitle) {
		this.ftitle = ftitle;
	}

	@Column(name = "fLevel")
	public Integer getFlevel() {
		return this.flevel;
	}

	public void setFlevel(Integer flevel) {
		this.flevel = flevel;
	}

	@Column(name = "fGrowthValue")
	public Integer getFgrowthValue() {
		return this.fgrowthValue;
	}

	public void setFgrowthValue(Integer fgrowthValue) {
		this.fgrowthValue = fgrowthValue;
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