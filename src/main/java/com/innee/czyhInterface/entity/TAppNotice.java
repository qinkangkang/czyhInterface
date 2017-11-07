package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "t_app_notice")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TAppNotice extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String fnoticeName;
	private Date fnoticeTime;
	private Integer fnoticeType;
	private String fnoticeUrl;
	private String fnoticeId;
	private String fnoticeTitle;
	private Integer forder;

	// Constructors

	/** default constructor */
	public TAppNotice() {
	}

	/** minimal constructor */
	public TAppNotice(String id) {
		this.id = id;
	}

	// Property accessors

	@Column(name = "fNoticeName")
	public String getFnoticeName() {
		return fnoticeName;
	}

	public void setFnoticeName(String fnoticeName) {
		this.fnoticeName = fnoticeName;
	}

	@Column(name = "fNoticeTime")
	public Date getFnoticeTime() {
		return fnoticeTime;
	}
	
	public void setFnoticeTime(Date fnoticeTime) {
		this.fnoticeTime = fnoticeTime;
	}
	
	@Column(name = "fNoticeType")
	public Integer getFnoticeType() {
		return fnoticeType;
	}


	public void setFnoticeType(Integer fnoticeType) {
		this.fnoticeType = fnoticeType;
	}

	@Column(name = "fNoticeUrl")
	public String getFnoticeUrl() {
		return fnoticeUrl;
	}

	public void setFnoticeUrl(String fnoticeUrl) {
		this.fnoticeUrl = fnoticeUrl;
	}

	@Column(name = "fNoticeId")
	public String getFnoticeId() {
		return fnoticeId;
	}

	public void setFnoticeId(String fnoticeId) {
		this.fnoticeId = fnoticeId;
	}

	@Column(name = "fOrder")
	public Integer getForder() {
		return this.forder;
	}

	public void setForder(Integer forder) {
		this.forder = forder;
	}

	@Column(name = "fNoticeTitle")
	public String getFnoticeTitle() {
		return fnoticeTitle;
	}

	public void setFnoticeTitle(String fnoticeTitle) {
		this.fnoticeTitle = fnoticeTitle;
	}

}