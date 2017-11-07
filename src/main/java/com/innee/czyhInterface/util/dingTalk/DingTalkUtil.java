package com.innee.czyhInterface.util.dingTalk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JavaType;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.util.HttpClientUtil;
import com.innee.czyhInterface.util.PropertiesUtil;

public class DingTalkUtil {

	private static Logger logger = LoggerFactory.getLogger(DingTalkUtil.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	// 钉钉推送通知总开关
	private static boolean DingTalkSwitch = true;

	// 设置支付成功同步发送钉钉通知
	private static String paySuccessSyncingDingTalk = "09191841013524,09191841281399";

	// 设置用户退款同步发送钉钉通知
	private static String refundSyncingDingTalk = "09191841013524,09191841281399";

	// 设置用户咨询同步发送钉钉通知
	private static String consultSyncingDingTalk = "";

	// 设置用户评论同步发送钉钉通知
	private static String commentSyncingDingTalk = "";

	// 设置活动售罄发送钉钉通知
	private static String eventSoldOutDingTalk = "";

	// 设置活动下架同步发送钉钉通知
	private static String eventOffSaleDingTalk = "";

	// 设置下单时活动规格售罄发送钉钉通知
	private static String eventSpecNoneDingTalk = "";

	// 追加订单备注同步发送钉钉通知
	private static String addOrderRemarkDingTalk = "";

	public static String getAddOrderRemarkDingTalk() {
		return addOrderRemarkDingTalk;
	}

	public static void setAddOrderRemarkDingTalk(String addOrderRemarkDingTalk) {
		DingTalkUtil.addOrderRemarkDingTalk = addOrderRemarkDingTalk;
	}

	public static String getPaySuccessSyncingDingTalk() {
		return paySuccessSyncingDingTalk;
	}

	public static void setPaySuccessSyncingDingTalk(String paySuccessSyncingDingTalk) {
		DingTalkUtil.paySuccessSyncingDingTalk = paySuccessSyncingDingTalk;
	}

	public static String getRefundSyncingDingTalk() {
		return refundSyncingDingTalk;
	}

	public static void setRefundSyncingDingTalk(String refundSyncingDingTalk) {
		DingTalkUtil.refundSyncingDingTalk = refundSyncingDingTalk;
	}

	public static String getConsultSyncingDingTalk() {
		return consultSyncingDingTalk;
	}

	public static void setConsultSyncingDingTalk(String consultSyncingDingTalk) {
		DingTalkUtil.consultSyncingDingTalk = consultSyncingDingTalk;
	}

	public static boolean isDingTalkSwitch() {
		return DingTalkSwitch;
	}

	public static void setDingTalkSwitch(boolean dingTalkSwitch) {
		DingTalkSwitch = dingTalkSwitch;
	}

	public static String getEventSoldOutDingTalk() {
		return eventSoldOutDingTalk;
	}

	public static void setEventSoldOutDingTalk(String eventSoldOutDingTalk) {
		DingTalkUtil.eventSoldOutDingTalk = eventSoldOutDingTalk;
	}

	public static String getEventOffSaleDingTalk() {
		return eventOffSaleDingTalk;
	}

	public static void setEventOffSaleDingTalk(String eventOffSaleDingTalk) {
		DingTalkUtil.eventOffSaleDingTalk = eventOffSaleDingTalk;
	}

	public static String getEventSpecNoneDingTalk() {
		return eventSpecNoneDingTalk;
	}

	public static void setEventSpecNoneDingTalk(String eventSpecNoneDingTalk) {
		DingTalkUtil.eventSpecNoneDingTalk = eventSpecNoneDingTalk;
	}

	/**
	 * 发送钉钉公告消息
	 * 
	 * @param msg
	 *            发送的信息
	 * @param sendUsers
	 *            消息接收者，多个接收者用“,”来分割
	 *            '分隔(比如：userId1,userId2,userId2）特殊情况：指定为@all，则向该企业应用的全部成员发送
	 * @return
	 */
	public static boolean sendDingTalk(String msg, String toUsers) {
		/*if (!DingTalkSwitch) {
			return true;
		}*/
		if (StringUtils.isBlank(msg) || StringUtils.isBlank(toUsers)) {
			return false;
		}
		toUsers = toUsers.replaceAll(",", "|");
		boolean b = false;
		String result = "";
		String accessToken = getAccessToken();
		
		
		try {
			DingDTO dingDTO = new DingDTO();
			dingDTO.setAgentid("85641952");
			Map<String, Object> content = new HashMap<String, Object>();
			content.put("content", msg);
			dingDTO.setText(content);
			dingDTO.setTouser(toUsers);
			dingDTO.setMsgtype("text");
			String url = "https://oapi.dingtalk.com/message/send?access_token="+accessToken;
			result = HttpClientUtil.callUrlPostByJson(url, mapper.toJson(dingDTO));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
		if (result == null) {
			return false;
		} else {
			b = true;
		}
		return b;
	}

	public static String getCommentSyncingDingTalk() {
		return commentSyncingDingTalk;
	}

	public static void setCommentSyncingDingTalk(String commentSyncingDingTalk) {
		DingTalkUtil.commentSyncingDingTalk = commentSyncingDingTalk;
	}
	
	public static String getAccessToken() {
		String CorpId = PropertiesUtil.getProperty("CorpId");
		String CorpSecret = PropertiesUtil.getProperty("CorpSecret");
		String accessToken = "";
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("corpid", CorpId);
			params.put("corpsecret", CorpSecret);
			String url = "https://oapi.dingtalk.com/gettoken";
			accessToken = HttpClientUtil.callUrlGet(url, params);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
		Map<String, Object> params = mapper.fromJson(accessToken, Map.class);
		if((Integer)params.get("errcode")==0){
			return params.get("access_token").toString();
		}else{
			return accessToken;
		}
	}
}