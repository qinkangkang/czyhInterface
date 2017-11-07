package com.innee.czyhInterface.util.wx.chenjar;

import com.innee.czyhInterface.util.PropertiesUtil;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;

public class WxMpServiceInstance {
	private WxMpService wxMpService;

	public WxMpService getWxMpService() {
		return wxMpService;
	}

	public void setWxMpService(WxMpService wxMpService) {
		this.wxMpService = wxMpService;
	}

	public WxMpConfigStorage getWxMpConfigStorage() {
		return wxMpConfigStorage;
	}

	public void setWxMpConfigStorage(WxMpConfigStorage wxMpConfigStorage) {
		this.wxMpConfigStorage = wxMpConfigStorage;
	}

	public WxMpMessageRouter getWxMpMessageRouter() {
		return wxMpMessageRouter;
	}

	public void setWxMpMessageRouter(WxMpMessageRouter wxMpMessageRouter) {
		this.wxMpMessageRouter = wxMpMessageRouter;
	}

	private WxMpConfigStorage wxMpConfigStorage;
	private WxMpMessageRouter wxMpMessageRouter;

	private static WxMpServiceInstance instance = null;

	public static WxMpServiceInstance getInstance() {
		if (instance == null) {
			instance = new WxMpServiceInstance();
		}
		return instance;
	}

	public WxMpServiceInstance() {
		WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
		config.setAppId(PropertiesUtil.getProperty("appId"));
		config.setSecret(PropertiesUtil.getProperty("secret"));
		config.setToken(PropertiesUtil.getProperty("token"));
		config.setAesKey(PropertiesUtil.getProperty("aesKey")); 
		
		wxMpConfigStorage = config; 
		wxMpService = new WxMpServiceImpl();
		wxMpService.setWxMpConfigStorage(config);
		wxMpMessageRouter = new WxMpMessageRouter(wxMpService);
	}
}
