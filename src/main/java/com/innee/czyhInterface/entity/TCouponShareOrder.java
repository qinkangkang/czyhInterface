package com.innee.czyhInterface.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_coupon_share_order")
public class TCouponShareOrder extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private BigDecimal fstartMoney;
	private BigDecimal fendMoney;
	private String fdeliveryId;
	private String fprompt;
	private String ftitle;
	private String fdescription;
	private String fphoto;

	// Constructors

	/** default constructor */
	public TCouponShareOrder() {
	}

	/** minimal constructor */
	public TCouponShareOrder(String id) {
		this.id = id;
	}

	// Property accessors
	@Column(name = "fStartMoney", precision = 18)
	public BigDecimal getFstartMoney() {
		return this.fstartMoney;
	}

	public void setFstartMoney(BigDecimal fstartMoney) {
		this.fstartMoney = fstartMoney;
	}

	@Column(name = "fEndMoney", precision = 18)
	public BigDecimal getFendMoney() {
		return this.fendMoney;
	}

	public void setFendMoney(BigDecimal fendMoney) {
		this.fendMoney = fendMoney;
	}

	@Column(name = "fDeliveryId", length = 36)
	public String getFdeliveryId() {
		return this.fdeliveryId;
	}

	public void setFdeliveryId(String fdeliveryId) {
		this.fdeliveryId = fdeliveryId;
	}

	@Column(name = "fPrompt")
	public String getFprompt() {
		return this.fprompt;
	}

	public void setFprompt(String fprompt) {
		this.fprompt = fprompt;
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

	@Column(name = "fPhoto")
	public String getFphoto() {
		return this.fphoto;
	}

	public void setFphoto(String fphoto) {
		this.fphoto = fphoto;
	}

}