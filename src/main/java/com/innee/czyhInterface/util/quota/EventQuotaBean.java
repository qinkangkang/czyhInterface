package com.innee.czyhInterface.util.quota;

import java.io.Serializable;

public class EventQuotaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String eventId;

	private int quotaCount = 0;

	// 限购类型：1表示限购下单总数，0表示可用订单总数
	private int quotaType = 0;

	// 限购周期：0表示全部周期，1表示每天，2表示每周，3表示每月，4表示每年
	private int quotaCycle = 0;

	public int getQuotaCount() {
		return quotaCount;
	}

	public void setQuotaCount(int quotaCount) {
		this.quotaCount = quotaCount;
	}

	public int getQuotaType() {
		return quotaType;
	}

	public void setQuotaType(int quotaType) {
		this.quotaType = quotaType;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public int getQuotaCycle() {
		return quotaCycle;
	}

	public void setQuotaCycle(int quotaCycle) {
		this.quotaCycle = quotaCycle;
	}

}