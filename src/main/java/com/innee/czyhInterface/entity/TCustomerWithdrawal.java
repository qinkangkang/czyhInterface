package com.innee.czyhInterface.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_customer_withdrawal")
public class TCustomerWithdrawal extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String fcustomerId;
	private String fwithdrawalNum;
	private BigDecimal famount;
	private Date fapplyTime;
	private String fapplyRemark;
	private Date ftoAccountTime;
	private String ftoAccountRemark;
	private Long fauditor;
	private Date fauditTime;
	private String fauditRemark;
	private Integer fstatus;

	// Constructors

	/** default constructor */
	public TCustomerWithdrawal() {
	}

	/** minimal constructor */
	public TCustomerWithdrawal(String id) {
		this.id = id;
	}

	// Property accessors
	@Column(name = "fCustomerID", length = 36)
	public String getFcustomerId() {
		return this.fcustomerId;
	}

	public void setFcustomerId(String fcustomerId) {
		this.fcustomerId = fcustomerId;
	}

	@Column(name = "fWithdrawalNum")
	public String getFwithdrawalNum() {
		return this.fwithdrawalNum;
	}

	public void setFwithdrawalNum(String fwithdrawalNum) {
		this.fwithdrawalNum = fwithdrawalNum;
	}

	@Column(name = "fAmount", precision = 18)
	public BigDecimal getFamount() {
		return this.famount;
	}

	public void setFamount(BigDecimal famount) {
		this.famount = famount;
	}

	@Column(name = "fApplyTime", length = 19)
	public Date getFapplyTime() {
		return this.fapplyTime;
	}

	public void setFapplyTime(Date fapplyTime) {
		this.fapplyTime = fapplyTime;
	}

	@Column(name = "fApplyRemark")
	public String getFapplyRemark() {
		return this.fapplyRemark;
	}

	public void setFapplyRemark(String fapplyRemark) {
		this.fapplyRemark = fapplyRemark;
	}

	@Column(name = "fToAccountTime", length = 19)
	public Date getFtoAccountTime() {
		return this.ftoAccountTime;
	}

	public void setFtoAccountTime(Date ftoAccountTime) {
		this.ftoAccountTime = ftoAccountTime;
	}

	@Column(name = "fToAccountRemark")
	public String getFtoAccountRemark() {
		return this.ftoAccountRemark;
	}

	public void setFtoAccountRemark(String ftoAccountRemark) {
		this.ftoAccountRemark = ftoAccountRemark;
	}

	@Column(name = "fAuditor")
	public Long getFauditor() {
		return this.fauditor;
	}

	public void setFauditor(Long fauditor) {
		this.fauditor = fauditor;
	}

	@Column(name = "fAuditTime", length = 19)
	public Date getFauditTime() {
		return this.fauditTime;
	}

	public void setFauditTime(Date fauditTime) {
		this.fauditTime = fauditTime;
	}

	@Column(name = "fAuditRemark", length = 2048)
	public String getFauditRemark() {
		return this.fauditRemark;
	}

	public void setFauditRemark(String fauditRemark) {
		this.fauditRemark = fauditRemark;
	}

	@Column(name = "fStatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

}