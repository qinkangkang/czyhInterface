package com.innee.czyhInterface.util.pingPay.bean;

import com.pingplusplus.model.EventData;

public class Event {

	private String id;
	private String object;
	private Boolean livemode;
	private Long created;
	private EventData data;
	private Integer pendingWebhooks;
	private String type;
	private String request;

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public EventData getData() {
		return data;
	}

	public void setData(EventData data) {
		this.data = data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getLivemode() {
		return livemode;
	}

	public void setLivemode(Boolean livemode) {
		this.livemode = livemode;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public Integer getPendingWebhooks() {
		return pendingWebhooks;
	}

	public void setPendingWebhooks(Integer pendingWebhooks) {
		this.pendingWebhooks = pendingWebhooks;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
