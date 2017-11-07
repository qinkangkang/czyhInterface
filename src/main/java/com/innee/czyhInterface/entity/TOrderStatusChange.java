package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_order_status_change")
public class TOrderStatusChange extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private TOrder TOrder;
	private Integer fbeforeStatus;
	private Integer fafterStatus;
	private String fchangeReason;
	private Integer foperatorType;
	private String foperatorId;
	private Date fcreateTime;
	private Date fupdateTime;

	// Constructors

	/** default constructor */
	public TOrderStatusChange() {
	}

	/** minimal constructor */
	public TOrderStatusChange(String id) {
		this.id = id;
	}

	// Property accessors
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fOrderID")
	public TOrder getTOrder() {
		return this.TOrder;
	}

	public void setTOrder(TOrder TOrder) {
		this.TOrder = TOrder;
	}

	@Column(name = "fBeforeStatus")
	public Integer getFbeforeStatus() {
		return this.fbeforeStatus;
	}

	public void setFbeforeStatus(Integer fbeforeStatus) {
		this.fbeforeStatus = fbeforeStatus;
	}

	@Column(name = "fAfterStatus")
	public Integer getFafterStatus() {
		return this.fafterStatus;
	}

	public void setFafterStatus(Integer fafterStatus) {
		this.fafterStatus = fafterStatus;
	}

	@Column(name = "fChangeReason")
	public String getFchangeReason() {
		return this.fchangeReason;
	}

	public void setFchangeReason(String fchangeReason) {
		this.fchangeReason = fchangeReason;
	}

	@Column(name = "fOperatorType")
	public Integer getFoperatorType() {
		return this.foperatorType;
	}

	public void setFoperatorType(Integer foperatorType) {
		this.foperatorType = foperatorType;
	}

	@Column(name = "fOperatorId")
	public String getFoperatorId() {
		return this.foperatorId;
	}

	public void setFoperatorId(String foperatorId) {
		this.foperatorId = foperatorId;
	}

	@Column(name = "fCreateTime", length = 19)
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "fUpdateTime", length = 19)
	public Date getFupdateTime() {
		return this.fupdateTime;
	}

	public void setFupdateTime(Date fupdateTime) {
		this.fupdateTime = fupdateTime;
	}

}