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
@Table(name = "t_goods_spec")
public class TEventSpec extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private TEvent TEvent;
	private TEventSession TEventSession;
	private String ftitle;
	private String fdescription;
	private BigDecimal fprice;
	private BigDecimal fdeal;
	private BigDecimal fsettlementPrice;
	private BigDecimal fpointsPrice;
	private BigDecimal fdistributionRebateAmount;
	private BigDecimal fdistributionRebateRatio;
	private Integer fadult;
	private Integer fchild;
	private Integer forder;
	private BigDecimal fpostage;
	private Integer ftotal;
	private Integer fstock;
	private Integer fsalesFlag;
	private Integer fstatus;
	private String fexternalGoodsCode;
	private Integer frealNameType;
	private Date fcreateTime;
	private Date fupdateTime;
	private Set<TOrder> TOrders = new HashSet<TOrder>(0);

	// Constructors

	/** default constructor */
	public TEventSpec() {
	}

	/** minimal constructor */
	public TEventSpec(String id) {
		this.id = id;
	}

	// Property accessors
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fEventID")
	public TEvent getTEvent() {
		return this.TEvent;
	}

	public void setTEvent(TEvent TEvent) {
		this.TEvent = TEvent;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fSessionID")
	public TEventSession getTEventSession() {
		return this.TEventSession;
	}

	public void setTEventSession(TEventSession TEventSession) {
		this.TEventSession = TEventSession;
	}

	@Column(name = "fTitle")
	public String getFtitle() {
		return this.ftitle;
	}

	public void setFtitle(String ftitle) {
		this.ftitle = ftitle;
	}

	@Column(name = "fDescription")
	public String getFdescription() {
		return this.fdescription;
	}

	public void setFdescription(String fdescription) {
		this.fdescription = fdescription;
	}

	@Column(name = "fPrice", precision = 18)
	public BigDecimal getFprice() {
		return this.fprice;
	}

	public void setFprice(BigDecimal fprice) {
		this.fprice = fprice;
	}

	@Column(name = "fDeal", precision = 18)
	public BigDecimal getFdeal() {
		return this.fdeal;
	}

	public void setFdeal(BigDecimal fdeal) {
		this.fdeal = fdeal;
	}

	@Column(name = "fSettlementPrice", precision = 18)
	public BigDecimal getFsettlementPrice() {
		return this.fsettlementPrice;
	}

	public void setFsettlementPrice(BigDecimal fsettlementPrice) {
		this.fsettlementPrice = fsettlementPrice;
	}

	@Column(name = "fPointsPrice")
	public BigDecimal getFpointsPrice() {
		return fpointsPrice;
	}

	public void setFpointsPrice(BigDecimal fpointsPrice) {
		this.fpointsPrice = fpointsPrice;
	}

	@Column(name = "fDistributionRebateAmount", precision = 8)
	public BigDecimal getFdistributionRebateAmount() {
		return this.fdistributionRebateAmount;
	}

	public void setFdistributionRebateAmount(BigDecimal fdistributionRebateAmount) {
		this.fdistributionRebateAmount = fdistributionRebateAmount;
	}

	@Column(name = "fDistributionRebateRatio", precision = 8)
	public BigDecimal getFdistributionRebateRatio() {
		return this.fdistributionRebateRatio;
	}

	public void setFdistributionRebateRatio(BigDecimal fdistributionRebateRatio) {
		this.fdistributionRebateRatio = fdistributionRebateRatio;
	}

	@Column(name = "fAdult")
	public Integer getFadult() {
		return this.fadult;
	}

	public void setFadult(Integer fadult) {
		this.fadult = fadult;
	}

	@Column(name = "fChild")
	public Integer getFchild() {
		return this.fchild;
	}

	public void setFchild(Integer fchild) {
		this.fchild = fchild;
	}

	@Column(name = "fOrder")
	public Integer getForder() {
		return this.forder;
	}

	public void setForder(Integer forder) {
		this.forder = forder;
	}

	@Column(name = "fPostage", precision = 18)
	public BigDecimal getFpostage() {
		return this.fpostage;
	}

	public void setFpostage(BigDecimal fpostage) {
		this.fpostage = fpostage;
	}

	@Column(name = "fTotal")
	public Integer getFtotal() {
		return this.ftotal;
	}

	public void setFtotal(Integer ftotal) {
		this.ftotal = ftotal;
	}

	@Column(name = "fStock")
	public Integer getFstock() {
		return this.fstock;
	}

	public void setFstock(Integer fstock) {
		this.fstock = fstock;
	}

	@Column(name = "fSalesFlag")
	public Integer getFsalesFlag() {
		return this.fsalesFlag;
	}

	public void setFsalesFlag(Integer fsalesFlag) {
		this.fsalesFlag = fsalesFlag;
	}

	@Column(name = "fStatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fExternalGoodsCode")
	public String getFexternalGoodsCode() {
		return this.fexternalGoodsCode;
	}

	public void setFexternalGoodsCode(String fexternalGoodsCode) {
		this.fexternalGoodsCode = fexternalGoodsCode;
	}

	@Column(name = "fRealNameType")
	public Integer getFrealNameType() {
		return this.frealNameType;
	}

	public void setFrealNameType(Integer frealNameType) {
		this.frealNameType = frealNameType;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TEventSpec")
	public Set<TOrder> getTOrders() {
		return this.TOrders;
	}

	public void setTOrders(Set<TOrder> TOrders) {
		this.TOrders = TOrders;
	}

}