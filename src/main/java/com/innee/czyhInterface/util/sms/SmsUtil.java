package com.innee.czyhInterface.util.sms;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.util.PropertiesUtil;
import com.innee.czyhInterface.util.express.MD5Util;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

/**
 * 短信发送容器
 * 
 * @author jinshengzhi
 *
 */
public class SmsUtil {

	private static Logger logger = LoggerFactory.getLogger(SmsUtil.class);

	public static final int CheckCodeSms = 1;

	public static final int ExpressSuccessSms = 2;

	public static final int DeliveryCodeSms = 3;

	public static final int TimeOutNoPaySms = 4;

	public static final int RefundSuccessSms = 5;

	public static final int LoginPwdSms = 6;

	public static final int QuestionnaireSurveySms = 7;

	public static final int ThankSms = 8;

	private static final int OrderNoticeSms = 9;

	/**
	 * 模板类型: 验证码 模板名称: 零到壹短信验证码 模板ID: SMS_35810160 模板内容:
	 * 您的验证码是：${chackCode},${minute}分钟内有效。 为保障您的账号安全，请勿将验证码短信转发给他人。
	 * 为用户提供短信验证码功能，来进行手机绑定、手机号登录、找回信息等操作
	 */
	private static final String CheckCodeSmsId = "SMS_35810160";

	/**
	 * 模板类型: 短信通知 模板名称: 零到壹用户订单发货通知 模板ID: SMS_52495249 模板内容:
	 * 您的订单${orderId}已经支付成功，您可以在${express}中查看订单详细信息。
	 */
	private static final String ExpressSuccessSmsId = "SMS_52495249";

	/**
	 * 模板类型: 短信通知 模板名称: 零到壹订单提货码通知通知 模板ID: SMS_52390190 模板内容:
	 * 您的订单${orderId}已经消费成功。如有问题，可关注${num}微信公众号咨询或拨打客服电话。
	 * 
	 */
	private static final String DeliveryCodeSmsId = "SMS_52390190";

	/**
	 * 模板类型: 短信通知 模板名称: 零到壹用户订单未支付提醒 模板ID: SMS_6370369 模板内容:
	 * 您的订单${orderName}还未支付，${minute}分钟后系统会自动取消订单，请您尽快完成支付。如有问题，可关注${platform}
	 * 微信公众号咨询或拨打客服电话：${customerServiceTel} 申请说明: 当用户距离下单时间一段时间后，系统提示用户进行支付 确定
	 */
	private static final String TimeOutNoPaySmsId = "SMS_6370369";

	/**
	 * 模板类型: 短信通知 模板名称: 零到壹订单取消退款成功通知 模板ID: SMS_6140559 模板内容:
	 * 您的订单${orderName}退款${refundAmount}元已经到账，请查收。如有问题，可关注${platform}
	 * 微信公众号咨询或拨打客服电话：${customerServiceTel} 申请说明: 用户的订单申请取消后，零到壹进行退款，如果退款成功后通知用户
	 */
	private static final String RefundSuccessSmsId = "SMS_6140559";

	/**
	 * 模板类型: 验证码 模板名称: 零到壹用户手机验证短信 模板ID: SMS_10365304 模板内容:
	 * 您的验证码是：${pwd}，在${minute}分钟内有效。如非您本人操作，可忽略本信息。感谢您使用${czyhInterface}。 申请说明:
	 * 零到壹用户在登录或者绑定手机时获取的验证码通知短信
	 */
	private static final String LoginPwdSmsId = "SMS_10365304";

	/**
	 * 模板类型: 短信通知 模板名称: 零到壹用户消费订单后评价活动的短信通知 模板ID: SMS_9550094 模板内容:
	 * 您收到本短信是${why}，为了让您得到更好的体验，${evaluate}。填写完毕后，${reward}。http://dwz.cn/
	 * 3mOOz1 申请说明: 零到壹用户消费订单后提醒用户进行活动评价的短信通知
	 */
	private static final String QuestionnaireSurveySmsId = "SMS_9550094";

	/**
	 * 模板类型: 短信通知 模板名称: 零到壹用户评价活动后奖励红包通知短信 模板ID: SMS_9700230 模板内容:
	 * 亲，感谢您的积极评价！红包已经奉上，请在${czyhInterface}登陆后在${couponList}中查看或直接在下单时使用。Have
	 * Fun! 申请说明: 零到壹用户在参加完成活动后评价了活动，给用户奖励红包的通知短信
	 */
	private static final String ThankSmsId = "SMS_9700230";

	/**
	 * 模板类型: 短信通知 模板名称: 零到壹用户下订单后通知短信 模板ID: SMS_16330375 模板内容:
	 * 您报名的活动“${eventTitle}”,订单编号：${orderNum}，兑换码为：${cdkey}，可拨电话给${sponsorName}
	 * 进行预约，预约电话：${sponsorPhone}，谢谢！ 申请说明:
	 * 用户在零到壹平台报名购买活动后，零到壹平台将通过发送兑换码的方式通知用户进行与商家预约活动。
	 */
	private static final String OrderNoticeSmsId = "SMS_16330375";

	private static boolean CheckCodeSwitch = true;

	private static boolean PaySuccessSwitch = true;

	private static boolean PayZeroSuccessSwitch = true;

	private static boolean VerificationSuccessSwitch = true;

	private static boolean TimeOutNoPaySwitch = true;

	private static boolean RefundSuccessSwitch = true;

	private static boolean LoginPwdSwitch = true;

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	private static String SmsServerUrl = null;

	private static String Appkey = null;

	private static String Appsecret = null;

	private static String ChenkCodeSignName = null;

	private static TaobaoClient client = null;

	public static void init() {
		Appkey = PropertiesUtil.getProperty("Appkey");
		Appsecret = PropertiesUtil.getProperty("Appsecret");
		SmsServerUrl = PropertiesUtil.getProperty("tabaoServiceUrl");
		ChenkCodeSignName = PropertiesUtil.getProperty("ChenkCodeSignName");

		client = new DefaultTaobaoClient(SmsServerUrl, Appkey, Appsecret);
	}

	/**
	 * 发送短信验证码方法
	 * 
	 * @param smsType
	 * @param phone
	 * @param smsParamMap
	 * @return
	 */
	public static SmsResult sendSms(int smsType, String phone, Map<String, String> smsParamMap) {
		SmsResult smsResult = new SmsResult();

		AlibabaAliqinFcSmsNumSendRequest request = new AlibabaAliqinFcSmsNumSendRequest();
		// 公共回传参数，在“消息返回”中会透传回该参数；举例：用户可以传入自己下级的会员ID，在消息返回时，该会员ID会包含在内，
		// 用户可以根据该会员ID识别是哪位会员使用了你的应用
		request.setExtend("foms");
		// 短信类型，传入值请填写normal
		request.setSmsType("normal");
		// 短信模板变量，传参规则{"key":"value"}，key的名字须和申请模板中的变量名一致，多个变量之间以逗号隔开。
		// 示例：针对模板“验证码${code}，您正在进行${product}身份验证，打死不要告诉别人哦！”，传参时需传入{"code":"1234","product":"alidayu"}
		String paramJson = mapper.toJson(smsParamMap);
		smsResult.setContent(paramJson);
		request.setSmsParamString(paramJson);
		// req.setSmsParamString("{\"code\":\"1234\",\"product\":\"【零到壹】\",\"item\":\"阿里大鱼\"}");
		// 短信接收号码。支持单个或多个手机号码，传入号码为11位手机号码，不能加0或+86。
		// 群发短信需传入多个号码，以英文逗号分隔，一次调用最多传入200个号码。示例：18600000000,13911111111,13322222222
		request.setRecNum(phone);

		// smsType为1表示是验证码短信
		if (smsType == CheckCodeSms) {
			// 短信签名，传入的短信签名必须是在阿里大鱼“管理中心-短信签名管理”中的可用签名。如“阿里大鱼”已在短信签名管理中通过审核，
			// 则可传入”阿里大鱼“（传参时去掉引号）作为短信签名。短信效果示例：【阿里大鱼】欢迎使用阿里大鱼服务。
			request.setSmsFreeSignName(ChenkCodeSignName);
			// 短信模板ID，传入的模板必须是在阿里大鱼“管理中心-短信模板管理”中的可用模板。示例：SMS_585014
			request.setSmsTemplateCode(CheckCodeSmsId);
		} else if (smsType == ExpressSuccessSms) {
			// smsType为2表示是订单发货成功通知
			request.setSmsFreeSignName(ChenkCodeSignName);
			request.setSmsTemplateCode(ExpressSuccessSmsId);
		} else if (smsType == DeliveryCodeSms) {
			// smsType为3表示是订单提货码发送通知
			request.setSmsFreeSignName(ChenkCodeSignName);
			request.setSmsTemplateCode(DeliveryCodeSmsId);
		} else if (smsType == TimeOutNoPaySms) {
			// smsType为4表示是超时未支付取消订单通知
			request.setSmsFreeSignName(ChenkCodeSignName);
			request.setSmsTemplateCode(TimeOutNoPaySmsId);
		} else if (smsType == RefundSuccessSms) {
			// smsType为5表示是退款成功通知
			request.setSmsFreeSignName(ChenkCodeSignName);
			request.setSmsTemplateCode(RefundSuccessSmsId);
		} else if (smsType == LoginPwdSms) {
			// smsType为6表示是登录登录验证码短信
			request.setSmsFreeSignName(ChenkCodeSignName);
			request.setSmsTemplateCode(LoginPwdSmsId);
		} else if (smsType == QuestionnaireSurveySms) {
			// smsType为7表示是问卷调查通知短信
			request.setSmsFreeSignName(ChenkCodeSignName);
			request.setSmsTemplateCode(QuestionnaireSurveySmsId);
		} else if (smsType == ThankSms) {
			// smsType为7表示是问卷调查通知短信
			request.setSmsFreeSignName(ChenkCodeSignName);
			request.setSmsTemplateCode(ThankSmsId);
		} else if (smsType == OrderNoticeSms) {
			// smsType为7表示是问卷调查通知短信
			request.setSmsFreeSignName(ChenkCodeSignName);
			request.setSmsTemplateCode(OrderNoticeSmsId);
		}

		AlibabaAliqinFcSmsNumSendResponse response = null;
		try {
			response = client.execute(request);
			if (response.getResult() != null) {
				smsResult.setSuccess(response.getResult().getSuccess());
			}
			smsResult.setResponse(response.getBody());
		} catch (ApiException e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
		return smsResult;
	}

	public static void main(String[] args) {
		/*String url = sendSmsEncrypt("0:0:0:0:0:0:0:1", "123456", 1);

		System.out.println(url);*/
		// SmsUtil.init();
		// Map<String, String> smsParamMap = Maps.newHashMap();
		// // smsParamMap.put("chackCode",
		// // "【感谢你在这两天五彩城活动的辛苦和努力，我是志国欧巴，我在地球总部，我爱你们！】");
		// // smsParamMap.put("minute", "【本条信息费用由琪琪爸爸赞助，在此表示感谢！】");
		// // System.out.println(SmsUtil.sendSms(1, "15300197095",
		// // smsParamMap).getResponse());
		// // SmsUtil.sendSms(1,
		// //
		// "13801089087,13671271380,15300197095,13126656255,18511697715,18611992989,13011807565,18010150316,13522560613,18811710377,18610526906,15811077481,18513622012",
		// // smsParamMap);
		//
		// StringBuilder orderInfo = new StringBuilder();
		// orderInfo.append("[乐仕堡拓展乐园，6店通用]【订单号：16101700079】");
		// smsParamMap.put("orderName", orderInfo.toString());
		// smsParamMap.put("orderList", "【我的-我的报名】");
		// SmsResult smsResult = SmsUtil.sendSms(SmsUtil.ExpressSuccessSms,
		// "15810716415", smsParamMap);
		// System.out.println(mapper.toJson(smsResult));
		//
		// // smsParamMap.put("why", "因为曾在零到壹购买过亲子活动产品");
		// // smsParamMap.put("evaluate", "需要亲点击链接填写问卷");
		// // smsParamMap.put("reward", "会奉上20元零到壹红包哟");
		// // SmsResult smsResult =
		// SmsUtil.sendSms(SmsUtil.QuestionnaireSurveySms,
		// // "13801089087", smsParamMap);
		// // System.out.println(mapper.toJson(smsResult));
		//
		// // smsParamMap.put("czyhInterface", "零到壹微信公众号");
		// // smsParamMap.put("couponList", "[我的—我的优惠券]");
		// // SmsResult smsResult = SmsUtil.sendSms(SmsUtil.ThankSms,
		// // "15300197095", smsParamMap);
		// // System.out.println(mapper.toJson(smsResult));
		//
		// // try {
		// // List<List<Object>> list = ReadExcel.readExcel(new
		// // File("d:/xkqh.xlsx"));
		// // list.remove(0);
		// // for (List<Object> list2 : list) {
		// // // System.out.println(object.getClass().getName());
		// // // System.out.println(object.toString());
		// // orderInfo.delete(0, orderInfo.length());
		// //
		// //
		// orderInfo.append("卡号【").append(list2.get(3).toString()).append("】密码【").append(list2.get(4).toString())
		// // .append("】");
		// // smsParamMap.put("eventTitle", "【10元抢购星空琴行14课时成人钢琴课！】");
		// // smsParamMap.put("orderNum", list2.get(1).toString());
		// // smsParamMap.put("cdkey", orderInfo.toString());
		// // smsParamMap.put("sponsorName", "【星空琴行】");
		// // smsParamMap.put("sponsorPhone", " ☏ 4006-528-123 ");
		// // SmsResult smsResult = SmsUtil.sendSms(SmsUtil.OrderNoticeSms,
		// // list2.get(2).toString(), smsParamMap);
		// // System.out.print(list2.get(1).toString() + " -- ");
		// // System.out.print(list2.get(2).toString() + " -- ");
		// // System.out.print(list2.get(3).toString() + " -- ");
		// // System.out.println(list2.get(4).toString());
		// // System.out.println(mapper.toJson(smsResult));
		// //
		// // }
		// // } catch (IOException e) {
		// //
		// logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		// // }

	}

	public static boolean isCheckCodeSwitch() {
		return CheckCodeSwitch;
	}

	public static void setCheckCodeSwitch(boolean checkCodeSwitch) {
		CheckCodeSwitch = checkCodeSwitch;
	}

	public static boolean isPaySuccessSwitch() {
		return PaySuccessSwitch;
	}

	public static void setPaySuccessSwitch(boolean paySuccessSwitch) {
		PaySuccessSwitch = paySuccessSwitch;
	}

	public static boolean isVerificationSuccessSwitch() {
		return VerificationSuccessSwitch;
	}

	public static void setVerificationSuccessSwitch(boolean verificationSuccessSwitch) {
		VerificationSuccessSwitch = verificationSuccessSwitch;
	}

	public static boolean isTimeOutNoPaySwitch() {
		return TimeOutNoPaySwitch;
	}

	public static void setTimeOutNoPaySwitch(boolean timeOutNoPaySwitch) {
		TimeOutNoPaySwitch = timeOutNoPaySwitch;
	}

	public static boolean isRefundSuccessSwitch() {
		return RefundSuccessSwitch;
	}

	public static void setRefundSuccessSwitch(boolean refundSuccessSwitch) {
		RefundSuccessSwitch = refundSuccessSwitch;
	}

	public static boolean isLoginPwdSwitch() {
		return LoginPwdSwitch;
	}

	public static void setLoginPwdSwitch(boolean loginPwdSwitch) {
		LoginPwdSwitch = loginPwdSwitch;
	}

	public static boolean isPayZeroSuccessSwitch() {
		return PayZeroSuccessSwitch;
	}

	public static void setPayZeroSuccessSwitch(boolean payZeroSuccessSwitch) {
		PayZeroSuccessSwitch = payZeroSuccessSwitch;
	}
}