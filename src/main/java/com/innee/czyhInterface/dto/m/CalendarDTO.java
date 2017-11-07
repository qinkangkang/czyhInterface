package com.innee.czyhInterface.dto.m;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class CalendarDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String day;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String week;

	private int viewFlag = 0;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sessionNum;

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public int getViewFlag() {
		return viewFlag;
	}

	public void setViewFlag(int viewFlag) {
		this.viewFlag = viewFlag;
	}

	public String getSessionNum() {
		return sessionNum;
	}

	public void setSessionNum(String sessionNum) {
		this.sessionNum = sessionNum;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

}