package com.innee.czyhInterface.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_app_channel_slider")
public class TAppChannelSlider extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private TAppChannelSetting TAppChannelSetting;
	private String fimage;
	private Integer furlType;
	private String fentityId;
	private String fentityTitle;
	private String fexternalUrl;
	private String fsliderText;
	private Integer forder;
	private Integer fisVisible;
	private Integer fsliderType;

	// Constructors

	/** default constructor */
	public TAppChannelSlider() {
	}

	/** minimal constructor */
	public TAppChannelSlider(String id) {
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

	@Column(name = "fImage", length = 255)
	public String getFimage() {
		return this.fimage;
	}

	public void setFimage(String fimage) {
		this.fimage = fimage;
	}

	@Column(name = "fUrlType")
	public Integer getFurlType() {
		return this.furlType;
	}

	public void setFurlType(Integer furlType) {
		this.furlType = furlType;
	}

	@Column(name = "fEntityId", length = 36)
	public String getFentityId() {
		return this.fentityId;
	}

	public void setFentityId(String fentityId) {
		this.fentityId = fentityId;
	}

	@Column(name = "fEntityTitle")
	public String getFentityTitle() {
		return this.fentityTitle;
	}

	public void setFentityTitle(String fentityTitle) {
		this.fentityTitle = fentityTitle;
	}

	@Column(name = "fExternalUrl")
	public String getFexternalUrl() {
		return this.fexternalUrl;
	}

	public void setFexternalUrl(String fexternalUrl) {
		this.fexternalUrl = fexternalUrl;
	}

	@Column(name = "fSliderText")
	public String getFsliderText() {
		return this.fsliderText;
	}

	public void setFsliderText(String fsliderText) {
		this.fsliderText = fsliderText;
	}

	@Column(name = "fOrder")
	public Integer getForder() {
		return this.forder;
	}

	public void setForder(Integer forder) {
		this.forder = forder;
	}

	@Column(name = "fIsVisible")
	public Integer getFisVisible() {
		return this.fisVisible;
	}

	public void setFisVisible(Integer fisVisible) {
		this.fisVisible = fisVisible;
	}

	@Column(name = "fSliderType")
	public Integer getFsliderType() {
		return this.fsliderType;
	}

	public void setFsliderType(Integer fsliderType) {
		this.fsliderType = fsliderType;
	}

}