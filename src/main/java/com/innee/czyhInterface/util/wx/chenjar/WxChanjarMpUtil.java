package com.innee.czyhInterface.util.wx.chenjar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WxChanjarMpUtil {

	private static final Logger logger = LoggerFactory.getLogger(WxChanjarMpUtil.class);

	private static WxMpServiceInstance instance = WxMpServiceInstance.getInstance();

	public static WxMpServiceInstance getInstance() {
		return instance;
	}

}