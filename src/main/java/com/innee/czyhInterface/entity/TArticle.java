package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "t_article")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TArticle extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private Integer fcityId;
	private String ftitle;
	private String ftype;
	private Long fimage;
	private String fbrief;
	private String fdetail;
	private String fdetailHtmlUrl;
	private Integer fstatus;
	private Long frecommend;
	private Long fcomment;
	private Integer forder;
	private Integer fartType;
	private String fartCity;
	private Date fcreateTime;
	private Date fupdateTime;

	// Constructors

	/** default constructor */
	public TArticle() {
	}

	/** minimal constructor */
	public TArticle(String id) {
		this.id = id;
	}

	// Property accessors
	@Column(name = "fCityId")
	public Integer getFcityId() {
		return this.fcityId;
	}

	public void setFcityId(Integer fcityId) {
		this.fcityId = fcityId;
	}

	@Column(name = "fTitle")
	public String getFtitle() {
		return this.ftitle;
	}

	public void setFtitle(String ftitle) {
		this.ftitle = ftitle;
	}

	@Column(name = "fType", length = 32)
	public String getFtype() {
		return this.ftype;
	}

	public void setFtype(String ftype) {
		this.ftype = ftype;
	}

	@Column(name = "fImage")
	public Long getFimage() {
		return this.fimage;
	}

	public void setFimage(Long fimage) {
		this.fimage = fimage;
	}

	@Column(name = "fBrief")
	public String getFbrief() {
		return this.fbrief;
	}

	public void setFbrief(String fbrief) {
		this.fbrief = fbrief;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "fDetail", columnDefinition = "longtext")
	public String getFdetail() {
		return this.fdetail;
	}

	public void setFdetail(String fdetail) {
		this.fdetail = fdetail;
	}

	@Column(name = "fDetailHtmlUrl", length = 2048)
	public String getFdetailHtmlUrl() {
		return this.fdetailHtmlUrl;
	}

	public void setFdetailHtmlUrl(String fdetailHtmlUrl) {
		this.fdetailHtmlUrl = fdetailHtmlUrl;
	}

	@Column(name = "fStatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fRecommend")
	public Long getFrecommend() {
		return this.frecommend;
	}

	public void setFrecommend(Long frecommend) {
		this.frecommend = frecommend;
	}

	@Column(name = "fComment")
	public Long getFcomment() {
		return this.fcomment;
	}

	public void setFcomment(Long fcomment) {
		this.fcomment = fcomment;
	}

	@Column(name = "fOrder")
	public Integer getForder() {
		return this.forder;
	}

	public void setForder(Integer forder) {
		this.forder = forder;
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

	@Column(name = "fArtType", length = 19)
	public Integer getFartType() {
		return fartType;
	}

	public void setFartType(Integer fartType) {
		this.fartType = fartType;
	}

	@Column(name = "fArtCity", length = 19)
	public String getFartCity() {
		return fartCity;
	}

	public void setFartCity(String fartCity) {
		this.fartCity = fartCity;
	}

	
}