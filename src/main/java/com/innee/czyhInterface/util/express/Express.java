package com.innee.czyhInterface.util.express;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class Express {

	private final JSONObject rootJson = new JSONObject();

	Map<String, Object> MapBody = new HashMap<String, Object>();

	public String getParamBody() {
		return rootJson.toString();
	}

	public Map<String, Object> getMapBody() {
		return MapBody;
	}

	public void setMapBody(Map<String, Object> mapBody) {
		MapBody = mapBody;
	}

}
