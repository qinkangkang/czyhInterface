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
@Table(name = "t_order_bonus")
public class TOrderBonus extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String fcustomerId;
	private TEventBonus TEventBonus;
	private String forderNum;
	private String fcustomerName;
	private String fcustomerPhone;
	private Date fcreateTime;
	private String fexpress;
	private String fremark;
	private String fnote;
	private Integer fstatus;
	private String freply;
	private Date freplyTime;
	private BigDecimal ftotal;
	private Integer fpayType;
	private Date fpayTime;

	// Constructors

	/** default constructor */
	public TOrderBonus() {
	}

	/** minimal constructor */
	public TOrderBonus(String id) {
		this.id = id;
	}

	@Column(name = "fCustomerID", length = 36)
	public String getFcustomerId() {
		return this.fcustomerId;
	}

	public void setFcustomerId(String fcustomerId) {
		this.fcustomerId = fcustomerId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fEventBonusID")
	public TEventBonus getTEventBonus() {
		return this.TEventBonus;
	}

	public void setTEventBonus(TEventBonus TEventBonus) {
		this.TEventBonus = TEventBonus;
	}

	@Column(name = "fOrderNum")
	public String getForderNum() {
		return this.forderNum;
	}

	public void setForderNum(String forderNum) {
		this.forderNum = forderNum;
	}

	@Column(name = "fCustomerName", length = 50)
	public String getFcustomerName() {
		return this.fcustomerName;
	}

	public void setFcustomerName(String fcustomerName) {
		this.fcustomerName = fcustomerName;
	}

	@Column(name = "fCustomerPhone", length = 50)
	public String getFcustomerPhone() {
		return this.fcustomerPhone;
	}

	public void setFcustomerPhone(String fcustomerPhone) {
		this.fcustomerPhone = fcustomerPhone;
	}

	@Column(name = "fExpress", length = 50)
	public String getFexpress() {
		return this.fexpress;
	}

	public void setFexpress(String fexpress) {
		this.fexpress = fexpress;
	}

	@Column(name = "fRemark", length = 50)
	public String getFremark() {
		return this.fremark;
	}

	public void setFremark(String fremark) {
		this.fremark = fremark;
	}

	@Column(name = "fNote", length = 2048)
	public String getFnote() {
		return this.fnote;
	}

	public void setFnote(String fnote) {
		this.fnote = fnote;
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

	@Column(name = "fReply")
	public String getFreply() {
		return this.freply;
	}

	public void setFreply(String freply) {
		this.freply = freply;
	}

	@Column(name = "fReplyTime", length = 19)
	public Date getFreplyTime() {
		return this.freplyTime;
	}

	public void setFreplyTime(Date freplyTime) {
		this.freplyTime = freplyTime;
	}

	@Column(name = "fTotal", precision = 18)
	public BigDecimal getFtotal() {
		return this.ftotal;
	}

	public void setFtotal(BigDecimal ftotal) {
		this.ftotal = ftotal;
	}

	@Column(name = "fPayType")
	public Integer getFpayType() {
		return this.fpayType;
	}

	public void setFpayType(Integer fpayType) {
		this.fpayType = fpayType;
	}

	@Column(name = "fPayTime", length = 19)
	public Date getFpayTime() {
		return this.fpayTime;
	}

	public void setFpayTime(Date fpayTime) {
		this.fpayTime = fpayTime;
	}

}