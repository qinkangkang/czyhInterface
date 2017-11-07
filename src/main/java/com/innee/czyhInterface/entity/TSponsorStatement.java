package com.innee.czyhInterface.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "t_sponsor_statement")
public class TSponsorStatement extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private TSponsor TSponsor;
	private String fstatementNum;
	private Date fbeginTime;
	private Date fendTime;
	private BigDecimal famount;
	private BigDecimal forderChangelAmount;
	private BigDecimal fpadinAmount;
	private Long foperator;
	private Date ftime;
	private String fremark;
	private Integer fstatus;
	private Set<TSponsorBalance> TSponsorBalances = new HashSet<TSponsorBalance>(0);

	// Constructors

	/** default constructor */
	public TSponsorStatement() {
	}

	/** minimal constructor */
	public TSponsorStatement(String id) {
		this.id = id;
	}

	// Property accessors
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fSponsorID")
	public TSponsor getTSponsor() {
		return this.TSponsor;
	}

	public void setTSponsor(TSponsor TSponsor) {
		this.TSponsor = TSponsor;
	}

	@Column(name = "fStatementNum")
	public String getFstatementNum() {
		return this.fstatementNum;
	}

	public void setFstatementNum(String fstatementNum) {
		this.fstatementNum = fstatementNum;
	}

	@Column(name = "fBeginTime", length = 19)
	public Date getFbeginTime() {
		return this.fbeginTime;
	}

	public void setFbeginTime(Date fbeginTime) {
		this.fbeginTime = fbeginTime;
	}

	@Column(name = "fEndTime", length = 19)
	public Date getFendTime() {
		return this.fendTime;
	}

	public void setFendTime(Date fendTime) {
		this.fendTime = fendTime;
	}

	@Column(name = "fAmount", precision = 18)
	public BigDecimal getFamount() {
		return this.famount;
	}

	public void setFamount(BigDecimal famount) {
		this.famount = famount;
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

	@Column(name = "fStatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TSponsorStatement")
	public Set<TSponsorBalance> getTSponsorBalances() {
		return this.TSponsorBalances;
	}

	public void setTSponsorBalances(Set<TSponsorBalance> TSponsorBalances) {
		this.TSponsorBalances = TSponsorBalances;
	}
	
	@Column(name = "fOrderChangelAmount", precision = 18)
	public BigDecimal getForderChangelAmount() {
		return forderChangelAmount;
	}

	public void setForderChangelAmount(BigDecimal forderChangelAmount) {
		this.forderChangelAmount = forderChangelAmount;
	}

	@Column(name = "fPadinAmount", precision = 18)
	public BigDecimal getFpadinAmount() {
		return fpadinAmount;
	}

	public void setFpadinAmount(BigDecimal fpadinAmount) {
		this.fpadinAmount = fpadinAmount;
	}

}