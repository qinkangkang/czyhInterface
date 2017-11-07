package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_app_channel_event")
public class TAppChannelEvent extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private TAppChannelSetting TAppChannelSetting;
	private TEvent TEvent;
	private Integer forder;
	private Date fpublishTime;
	private Long fpublisher;

	// Constructors

	/** default constructor */
	public TAppChannelEvent() {
	}

	/** minimal constructor */
	public TAppChannelEvent(String id) {
		this.id = id;
	}

	// Property accessors
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fChannelID")
	public TAppChannelSetting getTAppChannelSetting() {
		return this.TAppChannelSetting;
	}

	public void setTAppChannelSetting(TAppChannelSetting TAppChannelSetting) {
		this.TAppChannelSetting = TAppChannelSetting;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fEventID")
	public TEvent getTEvent() {
		return this.TEvent;
	}

	public void setTEvent(TEvent TEvent) {
		this.TEvent = TEvent;
	}

	@Column(name = "fPublisher")
	public Long getFpublisher() {
		return this.fpublisher;
	}

	public void setFpublisher(Long fpublisher) {
		this.fpublisher = fpublisher;
	}

	@Column(name = "fOrder")
	public Integer getForder() {
		return this.forder;
	}

	public void setForder(Integer forder) {
		this.forder = forder;
	}

	@Column(name = "fPublishTime", length = 19)
	public Date getFpublishTime() {
		return this.fpublishTime;
	}

	public void setFpublishTime(Date fpublishTime) {
		this.fpublishTime = fpublishTime;
	}
}