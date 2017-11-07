package com.innee.czyhInterface.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "t_customer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TCustomer extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private TSponsor TSponsor;
	private String fusername;
	private String fname;
	private String fphoto;
	private String fweixinId;
	private String fweixinUnionId;
	private String fweixinName;
	private String fregion;
	private Integer fsex;
	private String fphone;
	private String fpassword;
	private String fsalt;
	private Integer ftype;
	private String fbaby;
	private Integer fstatus;
	private String fticket;
	private Date fcreateTime;
	private Date fupdateTime;
	private Set<TOrder> TOrders = new HashSet<TOrder>(0);
	private Set<TFavorite> TFavorites = new HashSet<TFavorite>(0);
	private Set<TWxPay> TWxPaies = new HashSet<TWxPay>(0);
	private Set<TOrderVerification> TOrderVerificationsForFcustomerId = new HashSet<TOrderVerification>(0);
	private Set<TOrderVerification> TOrderVerificationsForFoperator = new HashSet<TOrderVerification>(0);
	private Set<TSponsorWithdraw> TSponsorWithdraws = new HashSet<TSponsorWithdraw>(0);
	private Set<TCouponDelivery> TCouponDeliveries = new HashSet<TCouponDelivery>(0);

	// Constructors

	/** default constructor */
	public TCustomer() {
	}

	/** minimal constructor */
	public TCustomer(String id) {
		this.id = id;
	}

	// Property accessors
	@Column(name = "fUsername")
	public String getFusername() {
		return this.fusername;
	}

	public void setFusername(String fusername) {
		this.fusername = fusername;
	}

	@Column(name = "fName")
	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	@Column(name = "fPhoto", length = 255)
	public String getFphoto() {
		return this.fphoto;
	}

	public void setFphoto(String fphoto) {
		this.fphoto = fphoto;
	}

	@Column(name = "fWeixinID")
	public String getFweixinId() {
		return this.fweixinId;
	}

	public void setFweixinId(String fweixinId) {
		this.fweixinId = fweixinId;
	}

	@Column(name = "fWeixinUnionID")
	public String getFweixinUnionId() {
		return this.fweixinUnionId;
	}

	public void setFweixinUnionId(String fweixinUnionId) {
		this.fweixinUnionId = fweixinUnionId;
	}

	@Column(name = "fWeixinName")
	public String getFweixinName() {
		return this.fweixinName;
	}

	public void setFweixinName(String fweixinName) {
		this.fweixinName = fweixinName;
	}

	@Column(name = "fRegion")
	public String getFregion() {
		return this.fregion;
	}

	public void setFregion(String fregion) {
		this.fregion = fregion;
	}

	@Column(name = "fSex")
	public Integer getFsex() {
		return this.fsex;
	}

	public void setFsex(Integer fsex) {
		this.fsex = fsex;
	}

	@Column(name = "fPhone")
	public String getFphone() {
		return this.fphone;
	}

	public void setFphone(String fphone) {
		this.fphone = fphone;
	}

	@Column(name = "fPassword")
	public String getFpassword() {
		return this.fpassword;
	}

	public void setFpassword(String fpassword) {
		this.fpassword = fpassword;
	}

	@Column(name = "fSalt", length = 32)
	public String getFsalt() {
		return this.fsalt;
	}

	public void setFsalt(String fsalt) {
		this.fsalt = fsalt;
	}

	@Column(name = "fType")
	public Integer getFtype() {
		return this.ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}

	@Column(name = "fBaby", length = 2048)
	public String getFbaby() {
		return this.fbaby;
	}

	public void setFbaby(String fbaby) {
		this.fbaby = fbaby;
	}

	@Column(name = "fStatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fTicket", length = 64)
	public String getFticket() {
		return this.fticket;
	}

	public void setFticket(String fticket) {
		this.fticket = fticket;
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

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TCustomer")
	public TSponsor getTSponsor() {
		return this.TSponsor;
	}

	public void setTSponsor(TSponsor TSponsor) {
		this.TSponsor = TSponsor;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TCustomer")
	public Set<TOrder> getTOrders() {
		return this.TOrders;
	}

	public void setTOrders(Set<TOrder> TOrders) {
		this.TOrders = TOrders;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TCustomer")
	public Set<TFavorite> getTFavorites() {
		return this.TFavorites;
	}

	public void setTFavorites(Set<TFavorite> TFavorites) {
		this.TFavorites = TFavorites;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TCustomer")
	public Set<TWxPay> getTWxPaies() {
		return this.TWxPaies;
	}

	public void setTWxPaies(Set<TWxPay> TWxPaies) {
		this.TWxPaies = TWxPaies;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TCustomerByFcustomerId")
	public Set<TOrderVerification> getTOrderVerificationsForFcustomerId() {
		return this.TOrderVerificationsForFcustomerId;
	}

	public void setTOrderVerificationsForFcustomerId(Set<TOrderVerification> TOrderVerificationsForFcustomerId) {
		this.TOrderVerificationsForFcustomerId = TOrderVerificationsForFcustomerId;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TCustomerByFoperator")
	public Set<TOrderVerification> getTOrderVerificationsForFoperator() {
		return this.TOrderVerificationsForFoperator;
	}

	public void setTOrderVerificationsForFoperator(Set<TOrderVerification> TOrderVerificationsForFoperator) {
		this.TOrderVerificationsForFoperator = TOrderVerificationsForFoperator;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TCustomer")
	public Set<TSponsorWithdraw> getTSponsorWithdraws() {
		return this.TSponsorWithdraws;
	}

	public void setTSponsorWithdraws(Set<TSponsorWithdraw> TSponsorWithdraws) {
		this.TSponsorWithdraws = TSponsorWithdraws;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TCustomer")
	public Set<TCouponDelivery> getTCouponDeliveries() {
		return this.TCouponDeliveries;
	}

	public void setTCouponDeliveries(Set<TCouponDelivery> TCouponDeliveries) {
		this.TCouponDeliveries = TCouponDeliveries;
	}
}