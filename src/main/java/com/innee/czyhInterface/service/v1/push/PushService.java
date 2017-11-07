package com.innee.czyhInterface.service.v1.push;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.dao.PushCustomerDAO;
import com.innee.czyhInterface.dao.PushCustomerInfoDAO;
import com.innee.czyhInterface.dao.PushDAO;
import com.innee.czyhInterface.dto.CustomerDTO;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.dto.m.push.PushMessageDTO;
import com.innee.czyhInterface.entity.TPush;
import com.innee.czyhInterface.entity.TPushCustomer;
import com.innee.czyhInterface.entity.TPushCustomerInfo;
import com.innee.czyhInterface.service.v2.CommonService;
import com.innee.czyhInterface.service.v2.FxlService;
import com.innee.czyhInterface.service.v2.PublicService;
import com.innee.czyhInterface.util.DictionaryUtil;
import com.innee.czyhInterface.util.PropertiesUtil;
import com.innee.czyhInterface.util.push.AndroidNotification;
import com.innee.czyhInterface.util.push.PushClient;
import com.innee.czyhInterface.util.push.android.AndroidBroadcast;
import com.innee.czyhInterface.util.push.android.AndroidUnicast;
import com.innee.czyhInterface.util.push.ios.IOSBroadcast;
import com.innee.czyhInterface.util.push.ios.IOSUnicast;

import net.sf.ehcache.CacheManager;

/**
 * 推送类接口
 * 
 * @author jinshengzhi
 *
 */
@Component("PushServiceV1")
@Transactional
public class PushService {

	private static final Logger logger = LoggerFactory.getLogger(PushService.class);

	private PushClient client = new PushClient();

	protected final JSONObject rootJson = new JSONObject();

	protected final String USER_AGENT = "Mozilla/5.0";

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	PublicService publicService;

	@Autowired
	private CommonService commonService;

	@Autowired
	private FxlService fxlService;

	@Autowired
	private PushCustomerDAO pushCustomerDAO;

	@Autowired
	private PushCustomerInfoDAO pushCustomerInfoDAO;

	@Autowired
	private PushDAO pushDAO;

	@Transactional(readOnly = true)
	public ResponseDTO getMessageList(Integer clientType, String ticket) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		StringBuilder hql = new StringBuilder();
		hql.append(
				"select t.id as id, t.ftitle as ftitle, t.fcontent as fcontent, t.fimage as fimage, t.ftype as ftype, t.ftargetType as ftargetType, t.ftargetObject as ftargetObject, t.fpushTime as fpushTime,t.fvalidTime as fvalidTime,t.fpageTitle as fpageTitle from TPush t ")
				.append(" where t.fstatus = 20 order by t.fpushTime desc");
		Map<String, Object> hqlMap = new HashMap<String, Object>();

		List<Map> pushList = Lists.newArrayList();
		Map<String, Object> pushMap = null;
		List<PushMessageDTO> pushMessageList = null;

		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);
		PushMessageDTO pushMessageDTO = null;
		Date date = null;
		Date dateFlag = DateUtils.addYears(new Date(), -100);

		for (Map<String, Object> amap : list) {
			pushMessageDTO = new PushMessageDTO();

			if (amap.get("fpushTime") != null && StringUtils.isNotBlank(amap.get("fpushTime").toString())) {
				date = (Date) amap.get("fpushTime");
				pushMessageDTO.setPushTime(DateFormatUtils.format(date, "yyyy年MM月dd日 HH:mm"));
				if (!DateUtils.truncatedEquals(dateFlag, date, Calendar.DAY_OF_MONTH)) {
					if (pushMessageList != null) {
						pushMap = Maps.newLinkedHashMap();
						pushMap.put("pushDate", DateFormatUtils.format(dateFlag, "yyyy年MM月dd日"));
						pushMap.put("pushList", pushMessageList);
						pushList.add(pushMap);
						pushMap = Maps.newLinkedHashMap();
					}
					pushMessageList = Lists.newArrayList();
					dateFlag = date;
				}
			}
			if (customerDTO.getCustomerId() != null) {
				pushMessageDTO.setCustomerId(customerDTO.getCustomerId());
			}

			if (amap.get("id") != null && StringUtils.isNotBlank(amap.get("id").toString())) {
				pushMessageDTO.setPushId(amap.get("id").toString());
			}
			if (amap.get("ftitle") != null && StringUtils.isNotBlank(amap.get("ftitle").toString())) {
				pushMessageDTO.setTitle(amap.get("ftitle").toString());
			}
			if (amap.get("fcontent") != null && StringUtils.isNotBlank(amap.get("fcontent").toString())) {
				pushMessageDTO.setContent(amap.get("fcontent").toString());
			}
			if (amap.get("fimage") != null && StringUtils.isNotBlank(amap.get("fimage").toString())) {
				pushMessageDTO.setImageUrl(fxlService.getImageUrl(amap.get("fimage").toString(), true));
			} else {
				pushMessageDTO.setImageUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
						.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPic.jpg").toString());
			}
			if (amap.get("ftargetType") != null && StringUtils.isNotBlank(amap.get("ftargetType").toString())) {
				pushMessageDTO.setTargetType(
						DictionaryUtil.getString(DictionaryUtil.PushLinkTargetType, (Integer) amap.get("ftargetType")));
			}
			if (amap.get("ftargetObject") != null && StringUtils.isNotBlank(amap.get("ftargetObject").toString())) {
				pushMessageDTO.setTargetObject(amap.get("ftargetObject").toString());
			}
			if (amap.get("fpageTitle") != null && StringUtils.isNotBlank(amap.get("fpageTitle").toString())) {
				pushMessageDTO.setPageTitle(amap.get("fpageTitle").toString());
			}
			TPushCustomer tPushCustomer = pushCustomerDAO.getUnread(customerDTO.getCustomerId(), amap.get("id").toString());
			if (tPushCustomer!=null && tPushCustomer.getFunread().intValue() == 1) {
				pushMessageDTO.setUnread(true);
			}
			try {
				if (amap.get("fvalidTime") != null && StringUtils.isNotBlank(amap.get("fvalidTime").toString())) {
					Date now = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date dates;
					dates = sdf.parse(amap.get("fvalidTime").toString());
					if (DateUtils.truncatedCompareTo(now, dates, Calendar.SECOND) >= 0) {
						pushMessageDTO.setValidTimeStatus(2);
					} else {
						pushMessageDTO.setValidTimeStatus(1);
					}

				}
			} catch (ParseException e) {

				logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			}

			pushMessageList.add(pushMessageDTO);
		}
		if (date != null) {
			pushMap = Maps.newLinkedHashMap();
			pushMap.put("pushDate", DateFormatUtils.format(date, "yyyy年MM月dd日"));
			pushMap.put("pushList", pushMessageList);
			pushList.add(pushMap);
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("pushMessageList", pushList);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("销推送信息列表加载成功！");
		return responseDTO;
	}

	public ResponseDTO updateReadPush(Integer clientType, String ticket, String pushId) {
		ResponseDTO responseDTO = new ResponseDTO();
		List<PushMessageDTO> pushList = Lists.newArrayList();

		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(pushId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("pushId参数不能为空，请检查pushId的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}
		// 设置已读状态
		TPushCustomer tPushCustomer = null;
		tPushCustomer = pushCustomerDAO.getUnread(customerDTO.getCustomerId(), pushId);
		if(tPushCustomer == null){
			tPushCustomer = new TPushCustomer();
			tPushCustomer.setFcustomerId(customerDTO.getCustomerId());
			tPushCustomer.setFstatus(20);
			tPushCustomer.setFunread(1);
			tPushCustomer.setTPush(new TPush(pushId));
			pushCustomerDAO.save(tPushCustomer);
		}

		responseDTO.setMsg("设置推送消息为已读状态成功！");
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("pushList", pushList);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("设置促销推送消息为已读状态成功！");
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getCustomerMessageList(Integer clientType, String ticket) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		StringBuilder hql = new StringBuilder();
		hql.append(
				"select t.id as id, t.ftitle as ftitle, t.fcontent as fcontent, t.fimage as fimage,t.ftype as ftype,t.ftargetType as ftargetType,t.ftargetObject as ftargetObject, t.fpushTime as fpushTime, t.fdescription as fdescription, t.fpageTitle as fpageTitle,t.funread as funread,t.fstatus as fstatus,t.fcustomerId as fcustomerId from TPushCustomerInfo t where")
				.append(" t.fcustomerId = :customerId").append(" and t.fstatus = 20 order by t.fpushTime desc");

		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("customerId", customerDTO.getCustomerId());

		List<Map> pushList = Lists.newArrayList();
		Map<String, Object> pushMap = null;
		List<PushMessageDTO> pushMessageList = null;

		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);
		PushMessageDTO pushMessageDTO = null;
		Date date = null;
		Date dateFlag = DateUtils.addYears(new Date(), -100);

		for (Map<String, Object> amap : list) {
			pushMessageDTO = new PushMessageDTO();

			if (amap.get("fpushTime") != null && StringUtils.isNotBlank(amap.get("fpushTime").toString())) {
				date = (Date) amap.get("fpushTime");
				pushMessageDTO.setPushTime(DateFormatUtils.format(date, "yyyy年MM月dd日 HH:mm"));
				if (!DateUtils.truncatedEquals(dateFlag, date, Calendar.DAY_OF_MONTH)) {
					if (pushMessageList != null) {
						pushMap = Maps.newLinkedHashMap();
						pushMap.put("pushDate", DateFormatUtils.format(dateFlag, "yyyy年MM月dd日"));
						pushMap.put("pushList", pushMessageList);
						pushList.add(pushMap);
						pushMap = Maps.newLinkedHashMap();
					}
					pushMessageList = Lists.newArrayList();
					dateFlag = date;
				}
			}
			if (amap.get("fcustomerId") != null && StringUtils.isNotBlank(amap.get("fcustomerId").toString())) {
				pushMessageDTO.setCustomerId(amap.get("fcustomerId").toString());
			}
			if (amap.get("id") != null && StringUtils.isNotBlank(amap.get("id").toString())) {
				pushMessageDTO.setPushId(amap.get("id").toString());
			}
			if (amap.get("ftitle") != null && StringUtils.isNotBlank(amap.get("ftitle").toString())) {
				pushMessageDTO.setTitle(amap.get("ftitle").toString());
			}
			if (amap.get("fcontent") != null && StringUtils.isNotBlank(amap.get("fcontent").toString())) {
				pushMessageDTO.setContent(amap.get("fcontent").toString());
			}
			// if (amap.get("fimage") != null &&
			// StringUtils.isNotBlank(amap.get("fimage").toString())) {
			// pushMessageDTO.setImageUrl(fxlService.getImageUrl(amap.get("fimage").toString(),
			// true));
			// } else {
			// pushMessageDTO.setImageUrl(new
			// StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
			// .append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPic.jpg").toString());
			// }
			if (amap.get("ftargetType") != null && StringUtils.isNotBlank(amap.get("ftargetType").toString())) {
				pushMessageDTO.setTargetType(
						DictionaryUtil.getString(DictionaryUtil.PushLinkTargetType, (Integer) amap.get("ftargetType")));
			}
			if (amap.get("ftargetObject") != null && StringUtils.isNotBlank(amap.get("ftargetObject").toString())) {
				pushMessageDTO.setTargetObject(amap.get("ftargetObject").toString());
			}
			if (amap.get("funread") != null && StringUtils.isNotBlank(amap.get("funread").toString())) {
				if (Integer.valueOf(amap.get("funread").toString()).intValue() == 1) {
					pushMessageDTO.setUnread(true);
				}
			}
			// try {
			// if (amap.get("fvalidTime") != null &&
			// StringUtils.isNotBlank(amap.get("fvalidTime").toString())) {
			// Date now = new Date();
			// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd
			// HH:mm:ss");
			// Date dates;
			// dates = sdf.parse(amap.get("fvalidTime").toString());
			// if (DateUtils.truncatedCompareTo(now, dates, Calendar.SECOND) >=
			// 0) {
			// pushMessageDTO.setValidTimeStatus(2);
			// } else {
			// pushMessageDTO.setValidTimeStatus(1);
			// }
			//
			// }
			// } catch (ParseException e) {
			//
			// logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			// }

			pushMessageList.add(pushMessageDTO);
		}
		if (date != null) {
			pushMap = Maps.newLinkedHashMap();
			pushMap.put("pushDate", DateFormatUtils.format(date, "yyyy年MM月dd日"));
			pushMap.put("pushList", pushMessageList);
			pushList.add(pushMap);
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("customerMessageList", pushList);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("系统推送信息列表加载成功！");
		return responseDTO;
	}

	public ResponseDTO updateCustomerReadPush(Integer clientType, String ticket, String pushId) {
		ResponseDTO responseDTO = new ResponseDTO();
		List<PushMessageDTO> pushList = Lists.newArrayList();

		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(pushId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("pushId参数不能为空，请检查pushId的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}
		pushCustomerInfoDAO.updatePushstatus(customerDTO.getCustomerId(), pushId, 1);

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("pushList", pushList);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("设置系统推送消息为已读状态成功！");
		return responseDTO;
	}

	public void savePushCustomerInfo(String fcustomerId, String ftitle, String fcontent, String fimage, String ftype,
			Integer ftargetType, String ftargetObject, Date fpushTime, String fdescription, String fpageTitle) {

		try {
			TPushCustomerInfo tPushCustomerInfo = new TPushCustomerInfo();
			tPushCustomerInfo.setFcustomerId(fcustomerId);
			tPushCustomerInfo.setFtitle(ftitle);
			tPushCustomerInfo.setFcontent(fcontent);
			tPushCustomerInfo.setFimage(fimage);
			tPushCustomerInfo.setFtype(ftype);
			tPushCustomerInfo.setFtargetType(ftargetType);
			tPushCustomerInfo.setFtargetObject(ftargetObject);
			tPushCustomerInfo.setFpushTime(fpushTime);
			tPushCustomerInfo.setFdescription(fdescription);
			tPushCustomerInfo.setFpageTitle(fpageTitle);
			tPushCustomerInfo.setFunread(0);
			tPushCustomerInfo.setFstatus(20);

			pushCustomerInfoDAO.save(tPushCustomerInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void pushTask(String pushId) {

		TPush tpush = pushDAO.findPushId(pushId);

		this.broadcastIOSAndAndroid(pushId, tpush.getFtitle(), tpush.getFcontent(), tpush.getFdescription(),
				tpush.getFtargetType(), tpush.getFtargetObject());

	}

	public void broadcastIOSAndAndroid(String pushId, String title, String content, String description,
			Integer ftargetType, String targetObject) {

		String targetType = DictionaryUtil.getString(DictionaryUtil.PushLinkTargetType, ftargetType);

		try {
			AndroidBroadcast androidBroadcast = new AndroidBroadcast(PropertiesUtil.getProperty("umappkey"),
					PropertiesUtil.getProperty("appMasterSecret"));
			androidBroadcast.setTicker("您有新的通知栏消息");
			androidBroadcast.setTitle(title);// 消息标题
			androidBroadcast.setText(content);// 消息内容
			androidBroadcast.setDescription(description);// 消息描述
			androidBroadcast.goAppAfterOpen();
			androidBroadcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);

			// androidBroadcast.setTestMode();// 测试环境
			androidBroadcast.setProductionMode();//生产环境 “上线后打开此注释切换生产模式”

			androidBroadcast.setExtraField(targetType,targetObject);

			client.send(androidBroadcast);

		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}

		try {
			IOSBroadcast broadcast = new IOSBroadcast(PropertiesUtil.getProperty("IOSUMAppKey"),
					PropertiesUtil.getProperty("IOSappMasterSecret"));

			broadcast.setAlert(content);// 推送的消息
			broadcast.setBadge(1);// 设置角标
			broadcast.setSound("default");// 设置声音
			broadcast.setDescription(description);
			broadcast.setProductionMode();//生产环境 “上线后打开此注释切换生产模式”

			//broadcast.setTestMode();// 测试环境

			broadcast.setCustomizedField("target_type", targetType);
			broadcast.setCustomizedField("target_id", targetObject);

			client.send(broadcast);

		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}

		pushDAO.saveUpdatePush(20, pushId);
	}

	/**
	 * 系统推送单播用
	 * 
	 * @param valueMap
	 */
	public void pushMessage(Integer ftargetType, String ftitle, String fcontent, String fdescription,
			String ftargetObjectId, String fdeviceToken, String fcustomerId, String fimage, String ftype,
			String fpageTitle) {

		String targetType = DictionaryUtil.getString(DictionaryUtil.PushLinkTargetType, ftargetType);

		Date date = new Date();
		try {
			AndroidUnicast androidUnicast = new AndroidUnicast(PropertiesUtil.getProperty("umappkey"),
					PropertiesUtil.getProperty("appMasterSecret"));

			androidUnicast.setDeviceToken(fdeviceToken);
			androidUnicast.setTicker("您有新的通知消息");
			androidUnicast.setTitle(ftitle);
			androidUnicast.setText(fcontent);
			androidUnicast.setDescription(fdescription);
			androidUnicast.goAppAfterOpen();
			androidUnicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);

			androidUnicast.setProductionMode();// 生产环境 “上线后打开此注释切换生产模式”
			// androidUnicast.setTestMode();// 测试环境

			androidUnicast.setExtraField(targetType,ftargetObjectId);

			client.send(androidUnicast);

		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}

		try {

			IOSUnicast unicast = new IOSUnicast(PropertiesUtil.getProperty("IOSUMAppKey"),
					PropertiesUtil.getProperty("IOSappMasterSecret"));

			unicast.setDeviceToken(fdeviceToken);
			unicast.setAlert(fcontent);
			unicast.setBadge(1);
			unicast.setSound("default");
			// unicast.setDescription(fdescription);
			unicast.setProductionMode();// 生产环境 “上线后打开此注释切换生产模式”
			//unicast.setTestMode();// 测试环境

			unicast.setCustomizedField("target_type", targetType);
			unicast.setCustomizedField("target_id", ftargetObjectId);

			client.send(unicast);

		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}

		savePushCustomerInfo(fcustomerId, ftitle, fcontent, fimage, ftype, ftargetType, ftargetObjectId, date,
				fdescription, fpageTitle);
	}

	/*
	 * 订单待付款通知
	 */
	public void toPaid(String orderNum, String orderId, String fdeviceToken, String fcustomerId) {
		this.pushMessage(10, "订单未付款通知", "您的订单:" + orderNum + "暂未付款，系统将在20分钟后自动取消订单，请在“我-待付款”中及时付款。。", "您有一个订单尚未付款", orderId,
				fdeviceToken, fcustomerId, "", "1", "");
	}

	/*
	 * 订单支付成功推送
	 */
	public void successPayment(String orderNum, String orderId, String fdeviceToken, String fcustomerId) {
		this.pushMessage(10, "订单支付成功通知", "您好，您的订单编号为:" + orderNum + "}付款已成功！请在“我-待发货”中查看物流动向。", "您有一个订单支付成功",
				orderId, fdeviceToken, fcustomerId, "", "1", "");
	}

	/*
	 * 物流发货推送
	 */
	public void confirmGoods(String orderNum, String express, String orderId, String fdeviceToken, String fcustomerId) {
		this.pushMessage(10, "订单物流发货通知", "您好，您的订单编号为：" + orderNum + "正在向您飞奔！请在“我-待收货”中查看物流状态。",
				"您的订单已发货", orderId, fdeviceToken, fcustomerId, "", "1", "");
	}

	/*
	 * 退款发起通知
	 */
	public void refund(String orderNum, String orderId, String fdeviceToken, String fcustomerId) {
		this.pushMessage(10, "订单退款审核成功通知", "您的订单编号为：" + orderNum + "退款已通过审核，退款金额将在0-7个工作日返回原支付账户，请注意查收~", "您的退款订单已审核通过",
				orderId, fdeviceToken, fcustomerId, "", "1", "");
	}

	/*
	 * 优惠券过期通知
	 */
	public void couponOverdue(String money, String fdeviceToken, String fcustomerId,
			String customerName) {
		this.pushMessage(8, "优惠券即将过期通知", "亲爱的" + customerName + "您有1张优惠券即将过期，快来查找优惠放肆买买买吧！", "您有一张优惠券即将过期",
				"mecoupon", fdeviceToken, fcustomerId, "", "1", "");
	}

	/*
	 * 积分到账通知
	 */
	public void bonusAccount(String bonus, String fdeviceToken, String fcustomerId, String customerName) {
		this.pushMessage(9, "积分到账通知", "亲爱的" + customerName + "，您的U币已到账，请在“我-U币社”中兑换你心水的商品哦~", "您有积分入账", "welfare",
				fdeviceToken, fcustomerId, "", "1", "");
	}

	/*
	 * 积分消耗通知
	 */
	public void bonusConsumption(String bonus, String goodsTitle, String fdeviceToken, String fcustomerId,
			String customerName) {
		this.pushMessage(9, "消耗积分通知",
				"恭喜您兑换商品" + goodsTitle + "成功！本次消耗" + bonus + "U币，邀请好友得更多U币！详情请【点击这里】",
				"您消耗了积分", "welfare", fdeviceToken, fcustomerId, "", "1", "");
	}

}