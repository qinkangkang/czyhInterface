package com.innee.czyhInterface.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TSysProvince entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_sys_province")
public class TSysProvince implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer proId;
	private String proName;
	private Integer fstatus;

	// Constructors

	/** default constructor */
	public TSysProvince() {
	}

	/** full constructor */
	public TSysProvince(String proName, Integer fstatus) {
		this.proName = proName;
		this.fstatus = fstatus;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "pro_id", unique = true, nullable = false)
	public Integer getProId() {
		return this.proId;
	}

	public void setProId(Integer proId) {
		this.proId = proId;
	}

	@Column(name = "pro_name", nullable = false)
	public String getProName() {
		return this.proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	@Column(name = "fStatus", nullable = false)
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

}