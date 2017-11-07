package com.innee.czyhInterface.util;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang3.SystemUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.dto.m.AppVersionDTO;
import com.innee.czyhInterface.util.dingTalk.DingTalkUtil;
import com.innee.czyhInterface.util.sms.SmsUtil;

public class Constant {

	// 系统属性设置文件
	public static final String SYSTEM_SETTING_FILE = "classpath:/system.properties";

	// 敏感词文件
	public static final String BAD_WORD_FILE = "classpath:wordfilter.txt";

	// 项目文件根路径
	public static String RootPath = null;

	// 系统默认的语言
	public static final String defaultLanguage = "zh_CN";

	// 英文语言
	public static final String englishLanguage = "en_US";

	public static final String sceneStr = "qrscene_";

	// 水印图片对象
	public static BufferedImage watermarkFile = null;

	// 水印图片对象（小）
	public static BufferedImage watermarkSmFile = null;

	// 短信验证码缓存Key
	public static final String SmsCheckCodeCacheKey = "SmsCheckCode";

	// 登录验证码缓存Key
	public static final String SmsLoginPwdKey = "SmsLoginPwd";

	// 上下架场次计数缓存Key
	public static final String OnOffSale = "OnOffSale";

	// 商家核销订单商家用户ID与订单ID绑定缓存Key
	public static final String VerificationOrder = "VerificationOrder";

	// 活动库存缓存Key
	public static final String EventStock = "EventStock";

	// 活动类目缓存Key
	public static final String EventCategory = "EventCategory";

	// 用户id与ticket对应关系缓存Key
	//public static final String TicketToId = "TicketToId";

	// 控制Http SessionId为键值保存当前所有上架活动的距离缓存Key
	public static final String SessionIdEventDistance = "SessionIdEventDistance";

	// 所有活动推荐数缓存Key
	public static final String EventRecommend = "EventRecommend";

	// 所有文章推荐数缓存Key
	public static final String ArticleRecommend = "ArticleRecommend";

	// 所有评价推荐数缓存Key
	public static final String CommentRecommend = "CommentRecommend";

	// 活动限购缓存Key
	public static final String EventQuota = "EventQuota";

	// 客户已购买限制数缓存Key
	public static final String CustomerQuota = "CustomerQuota";

	// 用户缓存KEY
	//public static final String CustomerEentity = "Customer";

	// 七牛用户上传图片缓存的token KEY
	public static final String QnUserUploadToken = "QnUserUploadToken";

	// 七牛用户上传头像缓存的token KEY
	public static final String QnUserUploadTokenLogo = "QnUserUploadTokenLogo";

	// 通用短信验证码
	public static final String GeneralSmsCheckCode = "336699";

	// 配置文件缓存
	public static final String Configuration = "Configuration";

	// 零到壹客服电话
	public static final String czyhInterfaceServiceTel = "010-53689210";
	
	// 微信订单评价返积分提醒模板消息first内容
	public static String orderEvaluationBonusFirst = null;
	
	// 微信订单评价反积分提醒模板消息备注内容
	public static String orderEvaluationBonusRemark = null;
	
	// 微信订单评价返现提醒模板消息first内容
	public static String orderCashBackFirst = null;
	
	// 微信订单评价返现提醒模板消息备注内容
	public static String orderCashBackRemark = null;

	// WEB端地址
	public static String H5Url = null;

	// 用户默认头像
	public static String defaultHeadImgUrl = null;

	// 零到壹LOGO
	public static String czyhInterfaceLogoImgUrl = null;

	// 未支付订单有效期（分钟数），下订单后未支付到指定时间后，系统将自动取消该订单
	// 如果为“0”表示订单永不过期
	private static int unPayFailureMinute = 30;
	
	// 发送订单未支付推送
	private static int toPaid = 20;

	// 微信push接口URL
	public static String weChatPushUrl = null;

	// 订单详情URL
	public static String orderDetail = null;

	// 活动详情URL
	public static String eventDetail = null;

	// 微信退款申请模板消息first内容
	public static String refundApplicationFirst = null;

	// 微信退款申请模板消息备注内容
	public static String refundApplicationRemark = null;

	// 微信订单支付成功模板消息first内容
	public static String paymentSuccessfulFirst = null;

	// 微信订单支付成功模板消息备注内容
	public static String paymentSuccessfulRemark = null;

	// 微信订单评价提醒模板消息first内容
	public static String orderEvaluationFirst = null;

	// 砍一砍快结束备注内容
	public static String barTimeOutRemark = null;

	// 砍一砍快结束first内容
	public static String barTimeOutFirst = null;

	// 砍一砍快结束备注内容
	public static String barRemainCountRemark = null;

	// 砍一砍快结束first内容
	public static String barRemainCountFirst = null;

	// 参加砍一砍提示内容
	public static String bargainJoinRemark = null;

	// 参加砍一砍提示first内容
	public static String bargainJoinFirst = null;

	// 微信订单评价提醒模板消息备注内容
	public static String orderEvaluationRemark = null;

	// 微信订单未支付通知模板消息first内容
	public static String unpaidOrderFirst = null;

	// 微信订单未支付通知模板消息备注内容
	public static String unpaidOrderRemark = null;

	// 前端个人用户订单状态页面
	public static String orderStatusUrl = null;

	// 退款状态页面
	public static String cancelOrderUrl = null;

	// 砍一砍首页地址
	public static String bargaininglUrl = null;

	// 移动端用户日志过滤列表
	public static ArrayList<String> cLogFilterList = Lists.newArrayList();

	// 移动端版本信息Map
	private static Map<Integer, AppVersionDTO> appVersionMap = Maps.newHashMap();
	
	// 移动端版本信息Map
	private static Map<Integer, AppVersionDTO> sponsorVersionMap = Maps.newHashMap();

	public static void init() {
		if (SystemUtils.IS_OS_WINDOWS) {
			RootPath = PropertiesUtil.getProperty("windowsRootPath");
		} else if (SystemUtils.IS_OS_MAC_OSX) {
			RootPath = PropertiesUtil.getProperty("macRootPath");
		} else {
			RootPath = PropertiesUtil.getProperty("linuxRootPath");
		}

		orderStatusUrl = PropertiesUtil.getProperty("orderStatusUrl");

		cancelOrderUrl = PropertiesUtil.getProperty("cancelOrderUrl");

		weChatPushUrl = PropertiesUtil.getProperty("weChatPushUrl");

		orderDetail = PropertiesUtil.getProperty("orderDetail");

		eventDetail = PropertiesUtil.getProperty("eventDetail");

		bargaininglUrl = PropertiesUtil.getProperty("bargaininglUrl");

		refundApplicationFirst = PropertiesUtil.getProperty("refundApplicationFirst");

		refundApplicationRemark = PropertiesUtil.getProperty("refundApplicationRemark");

		paymentSuccessfulFirst = PropertiesUtil.getProperty("paymentSuccessfulFirst");

		paymentSuccessfulRemark = PropertiesUtil.getProperty("paymentSuccessfulRemark");

		orderEvaluationFirst = PropertiesUtil.getProperty("orderEvaluationFirst");

		orderEvaluationRemark = PropertiesUtil.getProperty("orderEvaluationRemark");

		unpaidOrderFirst = PropertiesUtil.getProperty("unpaidOrderFirst");

		unpaidOrderRemark = PropertiesUtil.getProperty("unpaidOrderRemark");

		barTimeOutFirst = PropertiesUtil.getProperty("barTimeOutFirst");

		barTimeOutRemark = PropertiesUtil.getProperty("barTimeOutRemark");

		barRemainCountFirst = PropertiesUtil.getProperty("barRemainCountFirst");

		barRemainCountRemark = PropertiesUtil.getProperty("barRemainCountRemark");

		bargainJoinFirst = PropertiesUtil.getProperty("bargainJoinFirst");

		bargainJoinRemark = PropertiesUtil.getProperty("bargainJoinRemark");
		
		orderEvaluationBonusFirst = PropertiesUtil.getProperty("orderEvaluationBonusFirst");
		
		orderEvaluationBonusRemark = PropertiesUtil.getProperty("orderEvaluationBonusRemark");
		
		orderCashBackFirst = PropertiesUtil.getProperty("orderCashBackFirst");
		
		orderCashBackRemark = PropertiesUtil.getProperty("orderCashBackRemark");

		defaultHeadImgUrl = new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
				.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/customerHeadimg.jpg").toString();

		czyhInterfaceLogoImgUrl = new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
				.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/czyhInterfaceLogo.jpg").toString();

		// 设置WEB端地址
		H5Url = PropertiesUtil.getProperty("H5Url");

		// 设置默认短信开关
		if (PropertiesUtil.getProperty("CheckCodeSwitch").toString().equals("1")) {
			SmsUtil.setCheckCodeSwitch(true);
		} else {
			SmsUtil.setCheckCodeSwitch(false);
		}
		if (PropertiesUtil.getProperty("PaySuccessSwitch").toString().equals("1")) {
			SmsUtil.setPaySuccessSwitch(true);
		} else {
			SmsUtil.setPaySuccessSwitch(false);
		}
		if (PropertiesUtil.getProperty("PayZeroSuccessSwitch").toString().equals("1")) {
			SmsUtil.setPayZeroSuccessSwitch(true);
		} else {
			SmsUtil.setPayZeroSuccessSwitch(false);
		}
		if (PropertiesUtil.getProperty("VerificationSuccessSwitch").toString().equals("1")) {
			SmsUtil.setVerificationSuccessSwitch(true);
		} else {
			SmsUtil.setVerificationSuccessSwitch(false);
		}
		if (PropertiesUtil.getProperty("TimeOutNoPaySwitch").toString().equals("1")) {
			SmsUtil.setTimeOutNoPaySwitch(true);
		} else {
			SmsUtil.setTimeOutNoPaySwitch(false);
		}
		if (PropertiesUtil.getProperty("RefundSuccessSwitch").toString().equals("1")) {
			SmsUtil.setRefundSuccessSwitch(true);
		} else {
			SmsUtil.setRefundSuccessSwitch(false);
		}
		if (PropertiesUtil.getProperty("LoginPwdSwitch").toString().equals("1")) {
			SmsUtil.setLoginPwdSwitch(true);
		} else {
			SmsUtil.setLoginPwdSwitch(false);
		}
		if (PropertiesUtil.getProperty("DingTalkSwitch").toString().equals("1")) {
			DingTalkUtil.setDingTalkSwitch(true);
		} else {
			DingTalkUtil.setDingTalkSwitch(false);
		}

		// cLogFilterList.add("getChannelSliderList");
		cLogFilterList.add("getEventDetail");
		cLogFilterList.add("getMerchant");
		cLogFilterList.add("toPayOrder");
		cLogFilterList.add("againToPayOrder");
		// cLogFilterList.add("cancelPayOrder");
		cLogFilterList.add("appShareEvent");
		cLogFilterList.add("appShareMerchant");
		cLogFilterList.add("appShareArticle");
	}

	public static String getH5EventUrl() {
		return new StringBuilder().append(H5Url).append("/#/event-basic?id=").toString();
	}

	public static String getH5MerchantUrl() {
		return new StringBuilder().append(H5Url).append("/#/merchant?id=").toString();
	}

	public static String getH5ArticleUrl() {
		return new StringBuilder().append(H5Url).append("/#/article-detail?id=").toString();
	}

	public static int getUnPayFailureMinute() {
		return unPayFailureMinute;
	}

	public static void setUnPayFailureMinute(int unPayFailureMinute) {
		Constant.unPayFailureMinute = unPayFailureMinute;
	}

	public static Map<Integer, AppVersionDTO> getAppVersionMap() {
		return appVersionMap;
	}

	public static void setAppVersionMap(Map<Integer, AppVersionDTO> appVersionMap) {
		Constant.appVersionMap = appVersionMap;
	}

	public static Map<Integer, AppVersionDTO> getSponsorVersionMap() {
		return sponsorVersionMap;
	}

	public static void setSponsorVersionMap(Map<Integer, AppVersionDTO> sponsorVersionMap) {
		Constant.sponsorVersionMap = sponsorVersionMap;
	}

	public static int getToPaid() {
		return toPaid;
	}

	public static void setToPaid(int toPaid) {
		Constant.toPaid = toPaid;
	}
}