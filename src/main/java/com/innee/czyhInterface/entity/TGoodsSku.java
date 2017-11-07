package com.innee.czyhInterface.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "t_goods_sku")
public class TGoodsSku extends UuidEntity {

	private static final long serialVersionUID = 1L;

	
	private String fgoodsId;
	private Integer ftotal;
	private Integer fstock;
	private BigDecimal fprice;
	private BigDecimal fpriceMoney;
	private Long fclassTypeValue1;
	private Long fclassTypeValue2;
	private String fimage;
	private Long fcreaterId;
	private Date fcreateDate;
	private Date fupdateDate;
	private Integer flag;//0为默认选择sku;
	private  Integer flimitation;//限购数量
	private Integer fhavingImage;//是否上传图片
	private String fgoodsNO;//商品编号
	

	// Constructors
	@Column(name = "fLimitation")
	public Integer getFlimitation() {
		return flimitation;
	}
	
	public void setFlimitation(Integer flimitation) {
		this.flimitation = flimitation;
	}
	
	
	
	

	@Column(name="flag")
	public Integer getFlag() {
		return flag;
	}

	@Column(name = "fHavingImage")
	public Integer getFhavingImage() {
		return fhavingImage;
	}

	public void setFhavingImage(Integer fhavingImage) {
		this.fhavingImage = fhavingImage;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	/** default constructor */
	public TGoodsSku() {
	}

	/** minimal constructor */
	public TGoodsSku(String id) {
		this.id = id;
	}
	

	@Column(name = "fCreateDate", length = 19)
	public Date getFcreateDate() {
		return this.fcreateDate;
	}

	public void setFcreateDate(Date fcreateDate) {
		this.fcreateDate = fcreateDate;
	}

	@Column(name = "fUpdateDate", length = 19)
	public Date getFupdateDate() {
		return this.fupdateDate;
	}

	public void setFupdateDate(Date fupdateDate) {
		this.fupdateDate = fupdateDate;
	}

	@Column(name = "fGoodsId", length = 36)
	public String getFgoodsId() {
		return this.fgoodsId;
	}

	public void setFgoodsId(String fgoodsId) {
		this.fgoodsId = fgoodsId;
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

	@Column(name = "fprice", precision = 18)
	public BigDecimal getFprice() {
		return this.fprice;
	}

	public void setFprice(BigDecimal fprice) {
		this.fprice = fprice;
	}

	@Column(name = "fPriceMoney", precision = 18)
	public BigDecimal getFpriceMoney() {
		return this.fpriceMoney;
	}

	public void setFpriceMoney(BigDecimal fpriceMoney) {
		this.fpriceMoney = fpriceMoney;
	}

	@Column(name = "fClassTypeValue1", length = 36)
	public Long getFclassTypeValue1() {
		return this.fclassTypeValue1;
	}

	public void setFclassTypeValue1(Long fclassTypeValue1) {
		this.fclassTypeValue1 = fclassTypeValue1;
	}

	@Column(name = "fClassTypeValue2", length = 36)
	public Long getFclassTypeValue2() {
		return this.fclassTypeValue2;
	}

	public void setFclassTypeValue2(Long fclassTypeValue2) {
		this.fclassTypeValue2 = fclassTypeValue2;
	}

	@Column(name = "fImage")
	public String getFimage() {
		return this.fimage;
	}

	public void setFimage(String fimage) {
		this.fimage = fimage;
	}
	
	@Column(name = "fCreaterID")
	public Long getFcreaterId() {
		return this.fcreaterId;
	}

	public void setFcreaterId(Long fcreaterId) {
		this.fcreaterId = fcreaterId;
	}
	
	@Column(name = "fGoodsNo")
	public String getFgoodsNO() {
		return fgoodsNO;
	}

	public void setFgoodsNO(String fgoodsNO) {
		this.fgoodsNO = fgoodsNO;
	}

}