package com.innee.czyhInterface.util.quota;

import java.io.Serializable;
import java.util.Map;

public class CustomerQuotaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String customerId;

	private Map<String, Integer> eventBuyMap;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Map<String, Integer> getEventBuyMap() {
		return eventBuyMap;
	}

	public void setEventBuyMap(Map<String, Integer> eventBuyMap) {
		this.eventBuyMap = eventBuyMap;
	}

}