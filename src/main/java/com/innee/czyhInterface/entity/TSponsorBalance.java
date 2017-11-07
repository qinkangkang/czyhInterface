package com.innee.czyhInterface.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_sponsor_balance")
public class TSponsorBalance extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private TSponsorStatement TSponsorStatement;
	private TSponsor TSponsor;
	private BigDecimal famount;
	private Integer ftype;
	private String fobjectId;
	private String fobjectNum;
	private BigDecimal foriginalAmount;
	private BigDecimal forderChangelAmount;
	private Integer fstatus;
	private Long foperator;
	private Date ftime;
	private String fremark;

	// Constructors

	/** default constructor */
	public TSponsorBalance() {
	}

	/** minimal constructor */
	public TSponsorBalance(String id) {
		this.id = id;
	}

	// Property accessors
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fStatementID")
	public TSponsorStatement getTSponsorStatement() {
		return this.TSponsorStatement;
	}

	public void setTSponsorStatement(TSponsorStatement TSponsorStatement) {
		this.TSponsorStatement = TSponsorStatement;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fSponsorID")
	public TSponsor getTSponsor() {
		return this.TSponsor;
	}

	public void setTSponsor(TSponsor TSponsor) {
		this.TSponsor = TSponsor;
	}

	@Column(name = "fObjectID", length = 36)
	public String getFobjectId() {
		return this.fobjectId;
	}

	public void setFobjectId(String fobjectId) {
		this.fobjectId = fobjectId;
	}

	@Column(name = "fObjectNum")
	public String getFobjectNum() {
		return this.fobjectNum;
	}

	public void setFobjectNum(String fobjectNum) {
		this.fobjectNum = fobjectNum;
	}

	@Column(name = "fAmount", precision = 18)
	public BigDecimal getFamount() {
		return this.famount;
	}

	public void setFamount(BigDecimal famount) {
		this.famount = famount;
	}

	@Column(name = "fType")
	public Integer getFtype() {
		return this.ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}

	@Column(name = "fOriginalAmount", precision = 18)
	public BigDecimal getForiginalAmount() {
		return this.foriginalAmount;
	}

	public void setForiginalAmount(BigDecimal foriginalAmount) {
		this.foriginalAmount = foriginalAmount;
	}

	@Column(name = "fStatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fOperator")
	public Long getFoperator() {
		return this.foperator;
	}

	public void setFoperator(Long foperator) {
		this.foperator = foperator;
	}

	@Column(name = "fTime", length = 19)
	public Date getFtime() {
		return this.ftime;
	}

	public void setFtime(Date ftime) {
		this.ftime = ftime;
	}

	@Column(name = "fRemark", length = 2048)
	public String getFremark() {
		return this.fremark;
	}

	public void setFremark(String fremark) {
		this.fremark = fremark;
	}

	@Column(name = "fOrderChangelAmount", precision = 18)
	public BigDecimal getForderChangelAmount() {
		return forderChangelAmount;
	}

	public void setForderChangelAmount(BigDecimal forderChangelAmount) {
		this.forderChangelAmount = forderChangelAmount;
	}

}