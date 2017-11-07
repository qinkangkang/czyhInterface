package com.innee.czyhInterface.util.push;

import com.alibaba.fastjson.JSONObject;

public class Tag extends IOSNotification {

	protected final JSONObject rootJson = new JSONObject();

	String key;

	String device_tokens;

	String tag;

	String time;

	public String getDevice_tokens() {
		return device_tokens;
	}

	public void setDevice_tokens(String device_tokens) {
		this.device_tokens = device_tokens;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
