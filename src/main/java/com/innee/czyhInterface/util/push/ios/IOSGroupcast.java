package com.innee.czyhInterface.util.push.ios;

import com.alibaba.fastjson.JSONObject;
import com.innee.czyhInterface.util.push.IOSNotification;

public class IOSGroupcast extends IOSNotification {
	public IOSGroupcast(String appkey, String appMasterSecret) throws Exception {
		setAppMasterSecret(appMasterSecret);
		setPredefinedKeyValue("appkey", appkey);
		this.setPredefinedKeyValue("type", "groupcast");
	}

	public void setFilter(JSONObject filter) throws Exception {
		setPredefinedKeyValue("filter", filter);
	}
}
