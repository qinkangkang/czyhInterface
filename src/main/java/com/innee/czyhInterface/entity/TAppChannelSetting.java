package com.innee.czyhInterface.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "t_app_channel_setting")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TAppChannelSetting extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String fcode;
	private String ftitle;
	private String fsubTitle;
	private Integer fcity;
	private Integer ffrontType;
	private Integer fdefaultOrderType;
	private Integer forder;
	private String ficon;
	private Integer fisVisible;
	private String fpromotion;
	private Integer fallEvent;
	private Integer ftype;
	private Integer fwebType;
	private Set<TAppChannelSlider> TAppChannelSliders = new HashSet<TAppChannelSlider>(0);
	private Set<TAppChannelEvent> TAppChannelEvents = new HashSet<TAppChannelEvent>(0);

	// Constructors

	/** default constructor */
	public TAppChannelSetting() {
	}

	/** minimal constructor */
	public TAppChannelSetting(String id) {
		this.id = id;
	}

	// Property accessors
	@Column(name = "fCode")
	public String getFcode() {
		return this.fcode;
	}

	public void setFcode(String fcode) {
		this.fcode = fcode;
	}

	@Column(name = "fTitle")
	public String getFtitle() {
		return this.ftitle;
	}

	public void setFtitle(String ftitle) {
		this.ftitle = ftitle;
	}

	@Column(name = "fSubTitle")
	public String getFsubTitle() {
		return this.fsubTitle;
	}

	public void setFsubTitle(String fsubTitle) {
		this.fsubTitle = fsubTitle;
	}

	@Column(name = "fCity")
	public Integer getFcity() {
		return this.fcity;
	}

	public void setFcity(Integer fcity) {
		this.fcity = fcity;
	}

	@Column(name = "fFrontType")
	public Integer getFfrontType() {
		return this.ffrontType;
	}

	public void setFfrontType(Integer ffrontType) {
		this.ffrontType = ffrontType;
	}

	@Column(name = "fDefaultOrderType")
	public Integer getFdefaultOrderType() {
		return this.fdefaultOrderType;
	}

	public void setFdefaultOrderType(Integer fdefaultOrderType) {
		this.fdefaultOrderType = fdefaultOrderType;
	}

	@Column(name = "fOrder")
	public Integer getForder() {
		return this.forder;
	}

	public void setForder(Integer forder) {
		this.forder = forder;
	}

	@Column(name = "fIcon")
	public String getFicon() {
		return this.ficon;
	}

	public void setFicon(String ficon) {
		this.ficon = ficon;
	}

	@Column(name = "fIsVisible")
	public Integer getFisVisible() {
		return this.fisVisible;
	}

	public void setFisVisible(Integer fisVisible) {
		this.fisVisible = fisVisible;
	}

	@Column(name = "fPromotion", length = 32)
	public String getFpromotion() {
		return this.fpromotion;
	}

	public void setFpromotion(String fpromotion) {
		this.fpromotion = fpromotion;
	}

	@Column(name = "fAllEvent")
	public Integer getFallEvent() {
		return this.fallEvent;
	}

	public void setFallEvent(Integer fallEvent) {
		this.fallEvent = fallEvent;
	}

	@Column(name = "fType")
	public Integer getFtype() {
		return ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}

	@Column(name = "fWebType")
	public Integer getFwebType() {
		return this.fwebType;
	}

	public void setFwebType(Integer fwebType) {
		this.fwebType = fwebType;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TAppChannelSetting")
	public Set<TAppChannelSlider> getTAppChannelSliders() {
		return this.TAppChannelSliders;
	}

	public void setTAppChannelSliders(Set<TAppChannelSlider> TAppChannelSliders) {
		this.TAppChannelSliders = TAppChannelSliders;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TAppChannelSetting")
	public Set<TAppChannelEvent> getTAppChannelEvents() {
		return this.TAppChannelEvents;
	}

	public void setTAppChannelEvents(Set<TAppChannelEvent> TAppChannelEvents) {
		this.TAppChannelEvents = TAppChannelEvents;
	}

}