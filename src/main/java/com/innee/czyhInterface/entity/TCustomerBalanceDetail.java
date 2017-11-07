package com.innee.czyhInterface.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_customer_balance_detail")
public class TCustomerBalanceDetail extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String fcustomerId;
	private Integer finOut;
	private BigDecimal famount;
	private Integer fitemType;
	private String fitemDescribe;
	private Date fcreateTime;

	// Constructors

	/** default constructor */
	public TCustomerBalanceDetail() {
	}

	/** minimal constructor */
	public TCustomerBalanceDetail(String id) {
		this.id = id;
	}

	// Property accessors
	@Column(name = "fCustomerId", length = 36)
	public String getFcustomerId() {
		return this.fcustomerId;
	}

	public void setFcustomerId(String fcustomerId) {
		this.fcustomerId = fcustomerId;
	}

	@Column(name = "fInOut")
	public Integer getFinOut() {
		return this.finOut;
	}

	public void setFinOut(Integer finOut) {
		this.finOut = finOut;
	}

	@Column(name = "fAmount", precision = 18)
	public BigDecimal getFamount() {
		return this.famount;
	}

	public void setFamount(BigDecimal famount) {
		this.famount = famount;
	}

	@Column(name = "fItemType")
	public Integer getFitemType() {
		return this.fitemType;
	}

	public void setFitemType(Integer fitemType) {
		this.fitemType = fitemType;
	}

	@Column(name = "fItemDescribe")
	public String getFitemDescribe() {
		return this.fitemDescribe;
	}

	public void setFitemDescribe(String fitemDescribe) {
		this.fitemDescribe = fitemDescribe;
	}

	@Column(name = "fCreateTime", length = 19)
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

}