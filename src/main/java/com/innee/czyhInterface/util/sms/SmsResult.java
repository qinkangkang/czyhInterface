package com.innee.czyhInterface.util.sms;

import java.io.Serializable;

public class SmsResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean success = false;

	private String response;

	private String content;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}