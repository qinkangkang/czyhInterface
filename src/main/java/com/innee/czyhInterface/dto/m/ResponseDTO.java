package com.innee.czyhInterface.dto.m;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class ResponseDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean success = false;

	private int statusCode = 0;

	private String msg = StringUtils.EMPTY;

	private Map<String, Object> data = null;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

}