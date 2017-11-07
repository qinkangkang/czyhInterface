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
@Table(name = "t_order")
public class TOrder extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private TSponsor TSponsor;
	private TCustomer TCustomer;
	private TEvent TEvent;
	private TEventSpec TEventSpec;
	private TEventSession TEventSession;
	private String forderNum;
	private String fverificationCode;
	private BigDecimal fprice;
	private Integer fcount;
	private BigDecimal fpostage;
	private BigDecimal fchangeAmount;
	private String fchangeAmountInstruction;
	private BigDecimal freceivableTotal;
	private BigDecimal ftotal;
	private String fremark;
	private String fcsRemark;
	private Integer fstatus;
	private String frecipient;
	private String fexpress;
	private Integer fstockFlag;
	private Date fcreateTime;
	private Date funPayFailureTime;
	private Integer fpayType;
	private String frefundReason;
	private String freply;
	private Date freplyTime;
	private Date fpayTime;
	private Date fverificationTime;
	private Date frefundTime;
	private Integer flockFlag;
	private Integer fcityId;
	private Integer forderType;
	private Integer fappointment;
	private Integer freturn;
	private Integer fusePreferential;
	private Integer fverificationType;
	private String fsponsorName;
	private String fsponsorFullName;
	private String fsponsorPhone;
	private String fsponsorNumber;
	private String fcustomerPhone;
	private String fcustomerName;
	private String fcustomerSex;
	private String feventTitle;
	private String ftypeA;
	private String fsessionTitle;
	private String fgoodsImage;
	private Integer fchannel;
	private String fgps;
	private String fdeviceId;
	private Integer fsource;
	private Integer fsellModel;
	private Integer fpromotionModel;
	private String fspec;
	private Integer fspecModel;
	private Set<TOrderStatusChange> TOrderStatusChanges = new HashSet<TOrderStatusChange>(0);
	private Set<TOrderVerification> TOrderVerifications = new HashSet<TOrderVerification>(0);
	private Set<TCouponDeliveryHistory> TCouponDeliverieHistorys = new HashSet<TCouponDeliveryHistory>(0);
	// Constructors

	/** default constructor */
	public TOrder() {
	}

	/** minimal constructor */
	public TOrder(String id) {
		this.id = id;
	}

	// Property accessors
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fSponsorId")
	public TSponsor getTSponsor() {
		return this.TSponsor;
	}

	public void setTSponsor(TSponsor TSponsor) {
		this.TSponsor = TSponsor;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fCustomerID")
	public TCustomer getTCustomer() {
		return this.TCustomer;
	}

	public void setTCustomer(TCustomer TCustomer) {
		this.TCustomer = TCustomer;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fEventSpecID")
	public TEventSpec getTEventSpec() {
		return this.TEventSpec;
	}

	public void setTEventSpec(TEventSpec TEventSpec) {
		this.TEventSpec = TEventSpec;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fEventSessionID")
	public TEventSession getTEventSession() {
		return this.TEventSession;
	}

	public void setTEventSession(TEventSession TEventSession) {
		this.TEventSession = TEventSession;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fEventID")
	public TEvent getTEvent() {
		return this.TEvent;
	}

	public void setTEvent(TEvent TEvent) {
		this.TEvent = TEvent;
	}

	@Column(name = "fOrderNum")
	public String getForderNum() {
		return this.forderNum;
	}

	public void setForderNum(String forderNum) {
		this.forderNum = forderNum;
	}
	
	@Column(name = "fVerificationCode")
	public String getFverificationCode() {
		return fverificationCode;
	}

	public void setFverificationCode(String fverificationCode) {
		this.fverificationCode = fverificationCode;
	}

	@Column(name = "fPrice", precision = 18)
	public BigDecimal getFprice() {
		return this.fprice;
	}

	public void setFprice(BigDecimal fprice) {
		this.fprice = fprice;
	}

	@Column(name = "fCount")
	public Integer getFcount() {
		return this.fcount;
	}

	public void setFcount(Integer fcount) {
		this.fcount = fcount;
	}

	@Column(name = "fPostage", precision = 8)
	public BigDecimal getFpostage() {
		return this.fpostage;
	}

	public void setFpostage(BigDecimal fpostage) {
		this.fpostage = fpostage;
	}

	@Column(name = "fChangeAmount", precision = 18)
	public BigDecimal getFchangeAmount() {
		return this.fchangeAmount;
	}

	public void setFchangeAmount(BigDecimal fchangeAmount) {
		this.fchangeAmount = fchangeAmount;
	}

	@Column(name = "fChangeAmountInstruction")
	public String getFchangeAmountInstruction() {
		return this.fchangeAmountInstruction;
	}

	public void setFchangeAmountInstruction(String fchangeAmountInstruction) {
		this.fchangeAmountInstruction = fchangeAmountInstruction;
	}

	@Column(name = "fReceivableTotal", precision = 18)
	public BigDecimal getFreceivableTotal() {
		return this.freceivableTotal;
	}

	public void setFreceivableTotal(BigDecimal freceivableTotal) {
		this.freceivableTotal = freceivableTotal;
	}

	@Column(name = "fTotal", precision = 18)
	public BigDecimal getFtotal() {
		return this.ftotal;
	}

	public void setFtotal(BigDecimal ftotal) {
		this.ftotal = ftotal;
	}

	@Column(name = "fRemark")
	public String getFremark() {
		return this.fremark;
	}

	public void setFremark(String fremark) {
		this.fremark = fremark;
	}

	@Column(name = "fCsRemark", length = 2048)
	public String getFcsRemark() {
		return this.fcsRemark;
	}

	public void setFcsRemark(String fcsRemark) {
		this.fcsRemark = fcsRemark;
	}

	@Column(name = "fStatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fRecipient", length = 2048)
	public String getFrecipient() {
		return this.frecipient;
	}

	public void setFrecipient(String frecipient) {
		this.frecipient = frecipient;
	}

	@Column(name = "fExpress", length = 2048)
	public String getFexpress() {
		return this.fexpress;
	}

	public void setFexpress(String fexpress) {
		this.fexpress = fexpress;
	}

	@Column(name = "fStockFlag")
	public Integer getFstockFlag() {
		return this.fstockFlag;
	}

	public void setFstockFlag(Integer fstockFlag) {
		this.fstockFlag = fstockFlag;
	}

	@Column(name = "fCreateTime", length = 19)
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "fUnPayFailureTime", length = 19)
	public Date getFunPayFailureTime() {
		return this.funPayFailureTime;
	}

	public void setFunPayFailureTime(Date funPayFailureTime) {
		this.funPayFailureTime = funPayFailureTime;
	}

	@Column(name = "fPayType")
	public Integer getFpayType() {
		return this.fpayType;
	}

	public void setFpayType(Integer fpayType) {
		this.fpayType = fpayType;
	}

	@Column(name = "fRefundReason")
	public String getFrefundReason() {
		return this.frefundReason;
	}

	public void setFrefundReason(String frefundReason) {
		this.frefundReason = frefundReason;
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

	@Column(name = "fPayTime", length = 19)
	public Date getFpayTime() {
		return this.fpayTime;
	}

	public void setFpayTime(Date fpayTime) {
		this.fpayTime = fpayTime;
	}

	@Column(name = "fVerificationTime", length = 19)
	public Date getFverificationTime() {
		return this.fverificationTime;
	}

	public void setFverificationTime(Date fverificationTime) {
		this.fverificationTime = fverificationTime;
	}

	@Column(name = "fRefundTime", length = 19)
	public Date getFrefundTime() {
		return this.frefundTime;
	}

	public void setFrefundTime(Date frefundTime) {
		this.frefundTime = frefundTime;
	}

	@Column(name = "fLockFlag")
	public Integer getFlockFlag() {
		return flockFlag;
	}

	public void setFlockFlag(Integer flockFlag) {
		this.flockFlag = flockFlag;
	}

	@Column(name = "fCityID")
	public Integer getFcityId() {
		return this.fcityId;
	}

	public void setFcityId(Integer fcityId) {
		this.fcityId = fcityId;
	}

	@Column(name = "fOrderType")
	public Integer getForderType() {
		return this.forderType;
	}

	public void setForderType(Integer forderType) {
		this.forderType = forderType;
	}

	@Column(name = "fAppointment")
	public Integer getFappointment() {
		return this.fappointment;
	}

	public void setFappointment(Integer fappointment) {
		this.fappointment = fappointment;
	}

	@Column(name = "fReturn")
	public Integer getFreturn() {
		return this.freturn;
	}

	public void setFreturn(Integer freturn) {
		this.freturn = freturn;
	}

	@Column(name = "fUsePreferential")
	public Integer getFusePreferential() {
		return this.fusePreferential;
	}

	public void setFusePreferential(Integer fusePreferential) {
		this.fusePreferential = fusePreferential;
	}

	@Column(name = "fVerificationType")
	public Integer getFverificationType() {
		return this.fverificationType;
	}

	public void setFverificationType(Integer fverificationType) {
		this.fverificationType = fverificationType;
	}

	@Column(name = "fSponsorName")
	public String getFsponsorName() {
		return this.fsponsorName;
	}

	public void setFsponsorName(String fsponsorName) {
		this.fsponsorName = fsponsorName;
	}

	@Column(name = "fSponsorFullName")
	public String getFsponsorFullName() {
		return this.fsponsorFullName;
	}

	public void setFsponsorFullName(String fsponsorFullName) {
		this.fsponsorFullName = fsponsorFullName;
	}

	@Column(name = "fSponsorPhone")
	public String getFsponsorPhone() {
		return this.fsponsorPhone;
	}

	public void setFsponsorPhone(String fsponsorPhone) {
		this.fsponsorPhone = fsponsorPhone;
	}

	@Column(name = "fSponsorNumber")
	public String getFsponsorNumber() {
		return this.fsponsorNumber;
	}

	public void setFsponsorNumber(String fsponsorNumber) {
		this.fsponsorNumber = fsponsorNumber;
	}

	@Column(name = "fCustomerPhone")
	public String getFcustomerPhone() {
		return this.fcustomerPhone;
	}

	public void setFcustomerPhone(String fcustomerPhone) {
		this.fcustomerPhone = fcustomerPhone;
	}

	@Column(name = "fCustomerName")
	public String getFcustomerName() {
		return this.fcustomerName;
	}

	public void setFcustomerName(String fcustomerName) {
		this.fcustomerName = fcustomerName;
	}

	@Column(name = "fCustomerSex")
	public String getFcustomerSex() {
		return this.fcustomerSex;
	}

	public void setFcustomerSex(String fcustomerSex) {
		this.fcustomerSex = fcustomerSex;
	}

	@Column(name = "fEventTitle")
	public String getFeventTitle() {
		return this.feventTitle;
	}

	public void setFeventTitle(String feventTitle) {
		this.feventTitle = feventTitle;
	}

	@Column(name = "fTypeA")
	public String getFtypeA() {
		return this.ftypeA;
	}

	public void setFtypeA(String ftypeA) {
		this.ftypeA = ftypeA;
	}

	@Column(name = "fSessionTitle")
	public String getFsessionTitle() {
		return this.fsessionTitle;
	}

	public void setFsessionTitle(String fsessionTitle) {
		this.fsessionTitle = fsessionTitle;
	}

	@Column(name = "fGoodsImage")
	public String getFgoodsImage() {
		return fgoodsImage;
	}

	public void setFgoodsImage(String fgoodsImage) {
		this.fgoodsImage = fgoodsImage;
	}

	@Column(name = "fChannel")
	public Integer getFchannel() {
		return this.fchannel;
	}

	public void setFchannel(Integer fchannel) {
		this.fchannel = fchannel;
	}

	@Column(name = "fGps")
	public String getFgps() {
		return this.fgps;
	}

	public void setFgps(String fgps) {
		this.fgps = fgps;
	}

	@Column(name = "fDeviceId")
	public String getFdeviceId() {
		return this.fdeviceId;
	}

	public void setFdeviceId(String fdeviceId) {
		this.fdeviceId = fdeviceId;
	}

	@Column(name = "fSource")
	public Integer getFsource() {
		return this.fsource;
	}

	public void setFsource(Integer fsource) {
		this.fsource = fsource;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TOrder")
	public Set<TOrderStatusChange> getTOrderStatusChanges() {
		return this.TOrderStatusChanges;
	}

	public void setTOrderStatusChanges(Set<TOrderStatusChange> TOrderStatusChanges) {
		this.TOrderStatusChanges = TOrderStatusChanges;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TOrder")
	public Set<TOrderVerification> getTOrderVerifications() {
		return this.TOrderVerifications;
	}

	public void setTOrderVerifications(Set<TOrderVerification> TOrderVerifications) {
		this.TOrderVerifications = TOrderVerifications;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TOrder")
	public Set<TCouponDeliveryHistory> getTCouponDeliverieHistorys() {
		return TCouponDeliverieHistorys;
	}

	public void setTCouponDeliverieHistorys(Set<TCouponDeliveryHistory> tCouponDeliverieHistorys) {
		TCouponDeliverieHistorys = tCouponDeliverieHistorys;
	}

	public Integer getFsellModel() {
		return fsellModel;
	}

	public void setFsellModel(Integer fsellModel) {
		this.fsellModel = fsellModel;
	}

	public String getFspec() {
		return fspec;
	}

	public void setFspec(String fspec) {
		this.fspec = fspec;
	}

	public Integer getFspecModel() {
		return fspecModel;
	}

	public void setFspecModel(Integer fspecModel) {
		this.fspecModel = fspecModel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getFpromotionModel() {
		return fpromotionModel;
	}

	public void setFpromotionModel(Integer fpromotionModel) {
		this.fpromotionModel = fpromotionModel;
	}
}