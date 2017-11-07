package com.innee.czyhInterface.entity;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "t_goods_session")
public class TEventSession extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private TEvent TEvent;
	private String ftitle;
	private String fgps;
	private String faddress;
	private String flocation;
	private Date fstartDate;
	private Date fendDate;
	private String fstartTime;
	private String fendTime;
	// private Date fautoVerificationTime;
	private Date frefoundPeriod;
	private Date fdeadline;
	private Integer forder;
	private Integer fsalesFlag;
	private Integer fstatus;
	private Date fcreateTime;
	private Date fupdateTime;
	private Set<TEventSpec> TEventSpecs = new HashSet<TEventSpec>(0);
	private Set<TOrder> TOrders = new HashSet<TOrder>(0);

	// Constructors

	/** default constructor */
	public TEventSession() {
	}

	/** minimal constructor */
	public TEventSession(String id) {
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

	@Column(name = "fTitle")
	public String getFtitle() {
		return this.ftitle;
	}

	public void setFtitle(String ftitle) {
		this.ftitle = ftitle;
	}

	@Column(name = "fGPS", length = 255)
	public String getFgps() {
		return this.fgps;
	}

	public void setFgps(String fgps) {
		this.fgps = fgps;
	}

	@Column(name = "fAddress")
	public String getFaddress() {
		return this.faddress;
	}

	public void setFaddress(String faddress) {
		this.faddress = faddress;
	}

	@Column(name = "fLocation")
	public String getFlocation() {
		return this.flocation;
	}

	public void setFlocation(String flocation) {
		this.flocation = flocation;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fStartDate", length = 10)
	public Date getFstartDate() {
		return this.fstartDate;
	}

	public void setFstartDate(Date fstartDate) {
		this.fstartDate = fstartDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fEndDate", length = 10)
	public Date getFendDate() {
		return this.fendDate;
	}

	public void setFendDate(Date fendDate) {
		this.fendDate = fendDate;
	}

	@Column(name = "fStartTime", length = 32)
	public String getFstartTime() {
		return this.fstartTime;
	}

	public void setFstartTime(String fstartTime) {
		this.fstartTime = fstartTime;
	}

	@Column(name = "fEndTime", length = 32)
	public String getFendTime() {
		return this.fendTime;
	}

	public void setFendTime(String fendTime) {
		this.fendTime = fendTime;
	}

	@Column(name = "fRefoundPeriod", length = 19)
	public Date getFrefoundPeriod() {
		return this.frefoundPeriod;
	}

	public void setFrefoundPeriod(Date frefoundPeriod) {
		this.frefoundPeriod = frefoundPeriod;
	}

	@Column(name = "fDeadline", length = 19)
	public Date getFdeadline() {
		return this.fdeadline;
	}

	public void setFdeadline(Date fdeadline) {
		this.fdeadline = fdeadline;
	}

	@Column(name = "fOrder")
	public Integer getForder() {
		return this.forder;
	}

	public void setForder(Integer forder) {
		this.forder = forder;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TEventSession")
	public Set<TEventSpec> getTEventSpecs() {
		return this.TEventSpecs;
	}

	public void setTEventSpecs(Set<TEventSpec> TEventSpecs) {
		this.TEventSpecs = TEventSpecs;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TEventSession")
	public Set<TOrder> getTOrders() {
		return this.TOrders;
	}

	public void setTOrders(Set<TOrder> TOrders) {
		this.TOrders = TOrders;
	}
}