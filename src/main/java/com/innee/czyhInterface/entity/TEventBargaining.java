package com.innee.czyhInterface.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_goods_bargaining")
public class TEventBargaining extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String feventId;
	private String feventTitle;
	private String fsessionId;
	private String fsessionTitle;
	private String fspecId;
	private String fspecTitle;
	private String ftitle;
	private Date fbeginTime;
	private Date fendTime;
	private Long fimage;
	private Integer ftype;
	private String finputText;
	private String fpackageDesc;
	private BigDecimal fstartPrice;
	private BigDecimal fsettlementPrice;
	private BigDecimal ffloorPrice1;
	private BigDecimal ffloorPrice2;
	private BigDecimal ffloorPrice3;
	private Integer fstock1;
	private Integer fstock2;
	private Integer fstock3;
	private Integer fremainingStock1;
	private Integer fremainingStock2;
	private Integer fremainingStock3;
	private BigDecimal fmaxBargaining;
	private BigDecimal fminBargaining;
	private Long fcreaterId;
	private Integer fstatus;
	private Date fcreateTime;
	private Date fupdateTime;

	// Constructors

	/** default constructor */
	public TEventBargaining() {
	}

	/** minimal constructor */
	public TEventBargaining(String id) {
		this.id = id;
	}

	// Property accessors
	@Column(name = "fEventId", length = 36)
	public String getFeventId() {
		return this.feventId;
	}

	public void setFeventId(String feventId) {
		this.feventId = feventId;
	}

	@Column(name = "fEventTitle")
	public String getFeventTitle() {
		return this.feventTitle;
	}

	public void setFeventTitle(String feventTitle) {
		this.feventTitle = feventTitle;
	}

	@Column(name = "fSessionId", length = 36)
	public String getFsessionId() {
		return this.fsessionId;
	}

	public void setFsessionId(String fsessionId) {
		this.fsessionId = fsessionId;
	}

	@Column(name = "fSessionTitle")
	public String getFsessionTitle() {
		return this.fsessionTitle;
	}

	public void setFsessionTitle(String fsessionTitle) {
		this.fsessionTitle = fsessionTitle;
	}

	@Column(name = "fSpecId", length = 36)
	public String getFspecId() {
		return this.fspecId;
	}

	public void setFspecId(String fspecId) {
		this.fspecId = fspecId;
	}

	@Column(name = "fSpecTitle")
	public String getFspecTitle() {
		return this.fspecTitle;
	}

	public void setFspecTitle(String fspecTitle) {
		this.fspecTitle = fspecTitle;
	}

	@Column(name = "fTitle")
	public String getFtitle() {
		return this.ftitle;
	}

	public void setFtitle(String ftitle) {
		this.ftitle = ftitle;
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

	@Column(name = "fImage")
	public Long getFimage() {
		return this.fimage;
	}

	public void setFimage(Long fimage) {
		this.fimage = fimage;
	}

	@Column(name = "fType")
	public Integer getFtype() {
		return this.ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}

	@Column(name = "fInputText")
	public String getFinputText() {
		return this.finputText;
	}

	public void setFinputText(String finputText) {
		this.finputText = finputText;
	}

	@Column(name = "fPackageDesc")
	public String getFpackageDesc() {
		return this.fpackageDesc;
	}

	public void setFpackageDesc(String fpackageDesc) {
		this.fpackageDesc = fpackageDesc;
	}

	@Column(name = "fStartPrice", precision = 18)
	public BigDecimal getFstartPrice() {
		return this.fstartPrice;
	}

	public void setFstartPrice(BigDecimal fstartPrice) {
		this.fstartPrice = fstartPrice;
	}

	@Column(name = "fSettlementPrice", precision = 18)
	public BigDecimal getFsettlementPrice() {
		return this.fsettlementPrice;
	}

	public void setFsettlementPrice(BigDecimal fsettlementPrice) {
		this.fsettlementPrice = fsettlementPrice;
	}

	@Column(name = "fFloorPrice1", precision = 18)
	public BigDecimal getFfloorPrice1() {
		return this.ffloorPrice1;
	}

	public void setFfloorPrice1(BigDecimal ffloorPrice1) {
		this.ffloorPrice1 = ffloorPrice1;
	}

	@Column(name = "fFloorPrice2", precision = 18)
	public BigDecimal getFfloorPrice2() {
		return this.ffloorPrice2;
	}

	public void setFfloorPrice2(BigDecimal ffloorPrice2) {
		this.ffloorPrice2 = ffloorPrice2;
	}

	@Column(name = "fFloorPrice3", precision = 18)
	public BigDecimal getFfloorPrice3() {
		return this.ffloorPrice3;
	}

	public void setFfloorPrice3(BigDecimal ffloorPrice3) {
		this.ffloorPrice3 = ffloorPrice3;
	}

	@Column(name = "fStock1")
	public Integer getFstock1() {
		return this.fstock1;
	}

	public void setFstock1(Integer fstock1) {
		this.fstock1 = fstock1;
	}

	@Column(name = "fStock2")
	public Integer getFstock2() {
		return this.fstock2;
	}

	public void setFstock2(Integer fstock2) {
		this.fstock2 = fstock2;
	}

	@Column(name = "fStock3")
	public Integer getFstock3() {
		return this.fstock3;
	}

	public void setFstock3(Integer fstock3) {
		this.fstock3 = fstock3;
	}

	@Column(name = "fRemainingStock1")
	public Integer getFremainingStock1() {
		return this.fremainingStock1;
	}

	public void setFremainingStock1(Integer fremainingStock1) {
		this.fremainingStock1 = fremainingStock1;
	}

	@Column(name = "fRemainingStock2")
	public Integer getFremainingStock2() {
		return this.fremainingStock2;
	}

	public void setFremainingStock2(Integer fremainingStock2) {
		this.fremainingStock2 = fremainingStock2;
	}

	@Column(name = "fRemainingStock3")
	public Integer getFremainingStock3() {
		return this.fremainingStock3;
	}

	public void setFremainingStock3(Integer fremainingStock3) {
		this.fremainingStock3 = fremainingStock3;
	}

	@Column(name = "fMaxBargaining", precision = 18)
	public BigDecimal getFmaxBargaining() {
		return this.fmaxBargaining;
	}

	public void setFmaxBargaining(BigDecimal fmaxBargaining) {
		this.fmaxBargaining = fmaxBargaining;
	}

	@Column(name = "fMinBargaining", precision = 18)
	public BigDecimal getFminBargaining() {
		return this.fminBargaining;
	}

	public void setFminBargaining(BigDecimal fminBargaining) {
		this.fminBargaining = fminBargaining;
	}

	@Column(name = "fCreaterId")
	public Long getFcreaterId() {
		return this.fcreaterId;
	}

	public void setFcreaterId(Long fcreaterId) {
		this.fcreaterId = fcreaterId;
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

	@Column(name = "fUpdateTime", length = 19)
	public Date getFupdateTime() {
		return this.fupdateTime;
	}

	public void setFupdateTime(Date fupdateTime) {
		this.fupdateTime = fupdateTime;
	}

}