package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "t_calendar")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TCalendar extends IdEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private Integer cityId;
	private Integer eventDate;
	private Integer eventNum;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public TCalendar() {
	}

	// Property accessors
	@Column(name = "cityId")
	public Integer getCityId() {
		return this.cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	@Column(name = "eventDate")
	public Integer getEventDate() {
		return this.eventDate;
	}

	public void setEventDate(Integer eventDate) {
		this.eventDate = eventDate;
	}

	@Column(name = "eventNum")
	public Integer getEventNum() {
		return this.eventNum;
	}

	public void setEventNum(Integer eventNum) {
		this.eventNum = eventNum;
	}

	@Column(name = "updateTime", length = 19)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}