package com.innee.czyhInterface.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_order_goods")
public class TOrderGoods extends UuidEntity {

	private static final long serialVersionUID = 1L;
	private String forderId;
	private String feventId;
	private String fgoodsSkuId;
	private String feventTitle;
	private String fgoodsSubTitle;
	private BigDecimal fprice;
	private String fspec;
	private Integer fcount;
	private Date fcreateTime;
	private BigDecimal ftotalPrice;
	private Integer fpromotionModel;
	private Integer fstatus;

	// Constructors

	/** default constructor */
	public TOrderGoods() {
	}

	/** minimal constructor */
	public TOrderGoods(String id) {
		this.id = id;
	}

	@Column(name = "fOrderID", length = 36)
	public String getForderId() {
		return this.forderId;
	}

	public void setForderId(String forderId) {
		this.forderId = forderId;
	}

	@Column(name = "fEventId", length = 36)
	public String getFeventId() {
		return feventId;
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

	@Column(name = "fPrice", precision = 18)
	public BigDecimal getFprice() {
		return this.fprice;
	}

	public void setFprice(BigDecimal fprice) {
		this.fprice = fprice;
	}

	@Column(name = "fSpec")
	public String getFspec() {
		return this.fspec;
	}

	public void setFspec(String fspec) {
		this.fspec = fspec;
	}

	@Column(name = "fCount")
	public Integer getFcount() {
		return this.fcount;
	}

	public void setFcount(Integer fcount) {
		this.fcount = fcount;
	}

	@Column(name = "fCreateTime", length = 19)
	public Date getFcreateTime() {
		return fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "fTotalPrice", precision = 18)
	public BigDecimal getFtotalPrice() {
		return this.ftotalPrice;
	}

	public void setFtotalPrice(BigDecimal ftotalPrice) {
		this.ftotalPrice = ftotalPrice;
	}

	public Integer getFpromotionModel() {
		return fpromotionModel;
	}

	public void setFpromotionModel(Integer fpromotionModel) {
		this.fpromotionModel = fpromotionModel;
	}

	@Column(name = "fGoodsSubTitle")
	public String getFgoodsSubTitle() {
		return fgoodsSubTitle;
	}

	public void setFgoodsSubTitle(String fgoodsSubTitle) {
		this.fgoodsSubTitle = fgoodsSubTitle;
	}

	@Column(name = "fStatus")
	public Integer getFstatus() {
		return fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fGoodsSkuId")
	public String getFgoodsSkuId() {
		return fgoodsSkuId;
	}

	public void setFgoodsSkuId(String fgoodsSkuId) {
		this.fgoodsSkuId = fgoodsSkuId;
	}

}