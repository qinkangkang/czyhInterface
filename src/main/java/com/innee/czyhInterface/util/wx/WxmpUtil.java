package com.innee.czyhInterface.util.wx;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springside.modules.utils.Exceptions;

import com.github.cuter44.nyafx.crypto.CryptoBase;
import com.github.cuter44.nyafx.text.URLBuilder;
import com.github.cuter44.wxmp.WxmpFactory;
import com.github.cuter44.wxmp.reqs.JSSDKGetticket;
import com.github.cuter44.wxmp.reqs.TokenClientCredential;
import com.github.cuter44.wxmp.resps.JSSDKGetticketResponse;
import com.github.cuter44.wxmp.resps.TokenClientCredentialResponse;
import com.github.cuter44.wxmp.util.TokenKeeper;
import com.innee.czyhInterface.dto.m.WxJssdkDTO;
import com.innee.czyhInterface.exception.ServiceException;

public class WxmpUtil {

	private static final Logger logger = LoggerFactory.getLogger(WxmpUtil.class);

	public static String getAppId() {
		WxmpFactory factory = WxmpFactory.getDefaultInstance();
		return factory.getConf().getProperty("appid");
	}

	public static String getTicket() {
		WxmpFactory factory = WxmpFactory.getDefaultInstance();
		TokenKeeper tokenKeeper = factory.getTokenKeeper();
		return tokenKeeper.getJSSDKTicket();
	}

	public static String getWxAccessToken() {
		WxmpFactory factory = WxmpFactory.getDefaultInstance();

		TokenClientCredential clientCredential = (TokenClientCredential) factory
				.instantiate(TokenClientCredential.class);
		TokenClientCredentialResponse tokenClientCredentialResponse = null;
		try {
			tokenClientCredentialResponse = clientCredential.build().execute();
		} catch (IOException e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			throw new ServiceException("获取AccessToken时出错！");
		}
		return tokenClientCredentialResponse.getAccessToken();
	}

	public static WxJssdkDTO getWxJssdkParam(String url) {
		WxmpFactory factory = WxmpFactory.getDefaultInstance();
		factory.getConf().getProperty("appid");

		TokenKeeper tokenKeeper = factory.getTokenKeeper();
		String appId = factory.getConf().getProperty("appid");
		Long timestamp = System.currentTimeMillis();
		CryptoBase crypto = CryptoBase.getInstance();
		String nonceStr = CryptoBase.byteToHex(crypto.randomBytes(8));

		String ticket = tokenKeeper.getJSSDKTicket();

		URLBuilder ub = new URLBuilder().appendParam("jsapi_ticket", ticket).appendParam("noncestr", nonceStr)
				.appendParam("timestamp", timestamp.toString()).appendParam("url", url);

		String signature = null;
		try {
			signature = CryptoBase.byteToHex(crypto.SHA1Digest(ub.toString().getBytes("utf-8")));
		} catch (UnsupportedEncodingException e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			throw new ServiceException("进行微信签名加密时出错！");
		}

		WxJssdkDTO wxJssdkDTO = new WxJssdkDTO();
		wxJssdkDTO.setAppId(appId);
		wxJssdkDTO.setTimestamp(timestamp.toString());
		wxJssdkDTO.setNonceStr(nonceStr);
		wxJssdkDTO.setSignature(signature);
		return wxJssdkDTO;
	}

	public static JSSDKGetticketResponse getWxJsTicket() throws UnsupportedEncodingException, IOException {
		WxmpFactory factory = WxmpFactory.getDefaultInstance();

		// TokenClientCredential clientCredential = (TokenClientCredential)
		// factory
		// .instantiate(TokenClientCredential.class);
		// TokenClientCredentialResponse tokenClientCredentialResponse =
		// clientCredential.build().execute();

		JSSDKGetticket jssdkGetticket = (JSSDKGetticket) factory.instantiateWithToken(JSSDKGetticket.class);

		JSSDKGetticketResponse jssdkGetticketResponse = jssdkGetticket.build().execute();
		return jssdkGetticketResponse;

	}
}
