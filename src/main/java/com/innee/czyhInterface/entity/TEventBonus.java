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
@Table(name = "t_goods_bonus")
public class TEventBonus extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private TEvent TEvent;
	private String ftitle;
	private String fprompt;
	private Date fstartDate;
	private Date fendDate;
	private BigDecimal fprice;
	private Integer fbonus;
	private BigDecimal fdeal;
	private Date fuseDate;
	private String fusePerson;
	private Integer fstorage;
	private Integer fstock;
	private String funit;
	private String faddress;
	private String fuseType;
	private String fuseNote;
	private Integer ftype;
	private Integer flimitation;
	private Integer forder;
	private Integer fstatus;
	private Long fcreaterId;
	private Date fcreateTime;
	private Date fupdateTime;

	// Constructors

	/** default constructor */
	public TEventBonus() {
	}

	/** minimal constructor */
	public TEventBonus(String id) {
		this.id = id;
	}

	// Property accessors
	@Column(name = "fTitle")
	public String getFtitle() {
		return this.ftitle;
	}

	public void setFtitle(String ftitle) {
		this.ftitle = ftitle;
	}

	@Column(name = "fPrompt")
	public String getFprompt() {
		return this.fprompt;
	}

	public void setFprompt(String fprompt) {
		this.fprompt = fprompt;
	}

	@Column(name = "fStartDate", length = 19)
	public Date getFstartDate() {
		return this.fstartDate;
	}

	public void setFstartDate(Date fstartDate) {
		this.fstartDate = fstartDate;
	}

	@Column(name = "fEndDate", length = 19)
	public Date getFendDate() {
		return this.fendDate;
	}

	public void setFendDate(Date fendDate) {
		this.fendDate = fendDate;
	}

	@Column(name = "fPrice", precision = 18)
	public BigDecimal getFprice() {
		return fprice;
	}

	public void setFprice(BigDecimal fprice) {
		this.fprice = fprice;
	}

	@Column(name = "fBonus")
	public Integer getFbonus() {
		return this.fbonus;
	}

	public void setFbonus(Integer fbonus) {
		this.fbonus = fbonus;
	}

	@Column(name = "fDeal", precision = 18)
	public BigDecimal getFdeal() {
		return this.fdeal;
	}

	public void setFdeal(BigDecimal fdeal) {
		this.fdeal = fdeal;
	}

	@Column(name = "fUseDate", length = 19)
	public Date getFuseDate() {
		return this.fuseDate;
	}

	public void setFuseDate(Date fuseDate) {
		this.fuseDate = fuseDate;
	}

	@Column(name = "fUsePerson")
	public String getFusePerson() {
		return this.fusePerson;
	}

	public void setFusePerson(String fusePerson) {
		this.fusePerson = fusePerson;
	}

	@Column(name = "fStorage")
	public Integer getFstorage() {
		return this.fstorage;
	}

	public void setFstorage(Integer fstorage) {
		this.fstorage = fstorage;
	}

	@Column(name = "fUnit")
	public String getFunit() {
		return this.funit;
	}

	public void setFunit(String funit) {
		this.funit = funit;
	}

	@Column(name = "fAddress")
	public String getFaddress() {
		return this.faddress;
	}

	public void setFaddress(String faddress) {
		this.faddress = faddress;
	}

	@Column(name = "fUseType")
	public String getFuseType() {
		return this.fuseType;
	}

	public void setFuseType(String fuseType) {
		this.fuseType = fuseType;
	}

	@Column(name = "fUseNote")
	public String getFuseNote() {
		return this.fuseNote;
	}

	public void setFuseNote(String fuseNote) {
		this.fuseNote = fuseNote;
	}

	@Column(name = "fStock")
	public Integer getFstock() {
		return fstock;
	}

	public void setFstock(Integer fstock) {
		this.fstock = fstock;
	}

	@Column(name = "fType")
	public Integer getFtype() {
		return this.ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}

	@Column(name = "fLimitation")
	public Integer getFlimitation() {
		return this.flimitation;
	}

	public void setFlimitation(Integer flimitation) {
		this.flimitation = flimitation;
	}

	@Column(name = "fOrder")
	public Integer getForder() {
		return this.forder;
	}

	public void setForder(Integer forder) {
		this.forder = forder;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fEventID")
	public TEvent getTEvent() {
		return this.TEvent;
	}

	public void setTEvent(TEvent TEvent) {
		this.TEvent = TEvent;
	}

}