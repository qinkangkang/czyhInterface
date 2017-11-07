package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_customer_poster")
public class TCustomerPoster extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String fcustomerId;
	private String fposterId;
	private String fimage;
	private String fcustomerCode;
	private Date fstartTime;
	private Date fendTime;
	private Integer fstatus;
	private Date fcreateTime;

	// Constructors

	/** default constructor */
	public TCustomerPoster() {
	}

	/** minimal constructor */
	public TCustomerPoster(String id) {
		this.id = id;
	}

	@Column(name = "fCustomerID", length = 36)
	public String getFcustomerId() {
		return this.fcustomerId;
	}

	public void setFcustomerId(String fcustomerId) {
		this.fcustomerId = fcustomerId;
	}

	@Column(name = "fPosterID", length = 36)
	public String getFposterId() {
		return this.fposterId;
	}

	public void setFposterId(String fposterId) {
		this.fposterId = fposterId;
	}

	@Column(name = "fImage")
	public String getFimage() {
		return this.fimage;
	}

	public void setFimage(String fimage) {
		this.fimage = fimage;
	}

	@Column(name = "fCustomerCode")
	public String getFcustomerCode() {
		return this.fcustomerCode;
	}

	public void setFcustomerCode(String fcustomerCode) {
		this.fcustomerCode = fcustomerCode;
	}

	@Column(name = "fStartTime", length = 19)
	public Date getFstartTime() {
		return this.fstartTime;
	}

	public void setFstartTime(Date fstartTime) {
		this.fstartTime = fstartTime;
	}

	@Column(name = "fEndTime", length = 19)
	public Date getFendTime() {
		return this.fendTime;
	}

	public void setFendTime(Date fendTime) {
		this.fendTime = fendTime;
	}

	@Column(name = "fStatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fCreateTime", length = 19)
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

}