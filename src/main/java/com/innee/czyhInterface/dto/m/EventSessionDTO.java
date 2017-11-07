package com.innee.czyhInterface.dto.m;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class EventSessionDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sessionId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String title;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sessionStartDate;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sessionEndDate;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sessionStartTime;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sessionEndTime;

	private int limitation = 0;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String limitationType;

	private List<EventSpecDTO> specList;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSessionStartDate() {
		return sessionStartDate;
	}

	public void setSessionStartDate(String sessionStartDate) {
		this.sessionStartDate = sessionStartDate;
	}

	public String getSessionEndDate() {
		return sessionEndDate;
	}

	public void setSessionEndDate(String sessionEndDate) {
		this.sessionEndDate = sessionEndDate;
	}

	public String getSessionStartTime() {
		return sessionStartTime;
	}

	public void setSessionStartTime(String sessionStartTime) {
		this.sessionStartTime = sessionStartTime;
	}

	public String getSessionEndTime() {
		return sessionEndTime;
	}

	public void setSessionEndTime(String sessionEndTime) {
		this.sessionEndTime = sessionEndTime;
	}

	public String getLimitationType() {
		return limitationType;
	}

	public void setLimitationType(String limitationType) {
		this.limitationType = limitationType;
	}

	public int getLimitation() {
		return limitation;
	}

	public void setLimitation(int limitation) {
		this.limitation = limitation;
	}

	public List<EventSpecDTO> getSpecList() {
		return specList;
	}

	public void setSpecList(List<EventSpecDTO> specList) {
		this.specList = specList;
	}

}