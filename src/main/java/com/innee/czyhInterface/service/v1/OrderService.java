package com.innee.czyhInterface.service.v1;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.security.utils.Cryptos;
import org.springside.modules.utils.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.cuter44.wxpay.resps.Notify;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.common.dict.ResponseConfigurationDict;
import com.innee.czyhInterface.dao.CommonInfoDAO;
import com.innee.czyhInterface.dao.CouponDeliveryDAO;
import com.innee.czyhInterface.dao.CouponInformationDAO;
import com.innee.czyhInterface.dao.CustomerBargainingDAO;
import com.innee.czyhInterface.dao.CustomerDAO;
import com.innee.czyhInterface.dao.CustomerInfoDAO;
import com.innee.czyhInterface.dao.CustomerSubscribeDAO;
import com.innee.czyhInterface.dao.CustomerTagDAO;
import com.innee.czyhInterface.dao.EventDAO;
import com.innee.czyhInterface.dao.EventSessionDAO;
import com.innee.czyhInterface.dao.EventSpecDAO;
import com.innee.czyhInterface.dao.OrderAmountChangeDAO;
import com.innee.czyhInterface.dao.OrderDAO;
import com.innee.czyhInterface.dao.OrderGoodsDAO;
import com.innee.czyhInterface.dao.OrderVerificationDAO;
import com.innee.czyhInterface.dao.SmsDAO;
import com.innee.czyhInterface.dao.TimingTaskDAO;
import com.innee.czyhInterface.dao.WxPayDAO;
import com.innee.czyhInterface.dto.CustomerDTO;
import com.innee.czyhInterface.dto.OrderRecipientDTO;
import com.innee.czyhInterface.dto.RecipientDTO;
import com.innee.czyhInterface.dto.b.OrderForMerchantDTO;
import com.innee.czyhInterface.dto.m.BuyDTO;
import com.innee.czyhInterface.dto.m.BuyInfoDTO;
import com.innee.czyhInterface.dto.m.CouponDTO;
import com.innee.czyhInterface.dto.m.EventSessionDTO;
import com.innee.czyhInterface.dto.m.EventSpecDTO;
import com.innee.czyhInterface.dto.m.OrderDTO;
import com.innee.czyhInterface.dto.m.PageDTO;
import com.innee.czyhInterface.dto.m.PayDTO;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.entity.TCommonInfo;
import com.innee.czyhInterface.entity.TCouponDelivery;
import com.innee.czyhInterface.entity.TCouponInformation;
import com.innee.czyhInterface.entity.TCustomer;
import com.innee.czyhInterface.entity.TCustomerInfo;
import com.innee.czyhInterface.entity.TCustomerSubscribe;
import com.innee.czyhInterface.entity.TCustomerTag;
import com.innee.czyhInterface.entity.TEvent;
import com.innee.czyhInterface.entity.TEventSession;
import com.innee.czyhInterface.entity.TEventSpec;
import com.innee.czyhInterface.entity.TOrder;
import com.innee.czyhInterface.entity.TOrderAmountChange;
import com.innee.czyhInterface.entity.TOrderGoods;
import com.innee.czyhInterface.entity.TOrderVerification;
import com.innee.czyhInterface.entity.TSponsor;
import com.innee.czyhInterface.entity.TTimingTask;
import com.innee.czyhInterface.entity.TWxPay;
import com.innee.czyhInterface.exception.ServiceException;
import com.innee.czyhInterface.service.v1.category.CategoryService;
import com.innee.czyhInterface.service.v2.CommonService;
import com.innee.czyhInterface.service.v2.CouponService;
import com.innee.czyhInterface.service.v2.FxlService;
import com.innee.czyhInterface.service.v2.WxService;
import com.innee.czyhInterface.util.CommonPage;
import com.innee.czyhInterface.util.ConfigurationUtil;
import com.innee.czyhInterface.util.Constant;
import com.innee.czyhInterface.util.DictionaryUtil;
import com.innee.czyhInterface.util.NumberUtil;
import com.innee.czyhInterface.util.PropertiesUtil;
import com.innee.czyhInterface.util.asynchronoustasks.AsynchronousTasksManager;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.EventSoldOutBean;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.OrderSendSmsBean;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.OrderUpdateCustomerInfoBean;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.OrderUpdateCustomerTagBean;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.OrderUpdateOrderGpsByIpBean;
import com.innee.czyhInterface.util.bmap.BmapUtil;
import com.innee.czyhInterface.util.dingTalk.DingTalkUtil;
import com.innee.czyhInterface.util.log.OutPutLogUtil;
import com.innee.czyhInterface.util.sms.SmsUtil;
import com.innee.czyhInterface.util.wx.WxPayResult;
import com.innee.czyhInterface.util.wx.WxPayUtil;

import ch.qos.logback.core.encoder.ByteArrayUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * czyhInterface业务管理类.
 * 
 * @author jinshengzhi
 */
@Component("OrderServiceV1")
@Transactional
public class OrderService {

	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private CommonService commonService;

	@Autowired
	private FxlService fxlService;

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private CommonInfoDAO commonInfoDAO;

	@Autowired
	private EventDAO eventDAO;

	@Autowired
	private EventSpecDAO eventSpecDAO;

	@Autowired
	private EventSessionDAO eventSessionDAO;

	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private WxPayDAO wxPayDAO;

	@Autowired
	private TimingTaskDAO timingTaskDAO;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private OrderVerificationDAO orderVerificationDAO;

	@Autowired
	private CouponDeliveryDAO couponDeliveryDAO;

	@Autowired
	private CouponService couponService;

	@Autowired
	private CouponInformationDAO couponInformationDAO;

	@Autowired
	private CustomerInfoDAO customerInfoDAO;

	@Autowired
	private WxService wxService;

	@Autowired
	private CustomerTagDAO customerTagDAO;

	@Autowired
	private OrderAmountChangeDAO orderAmountChangeDAO;

	@Autowired
	private CustomerBargainingDAO customerBargainingDAO;
	
	@Autowired
	private OrderGoodsDAO orderGoodsDAO;
	
	@Autowired
	private CustomerSubscribeDAO customerSubscribeDAO;

	@Transactional(readOnly = true)
	public ResponseDTO toBuy(String eventId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(eventId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("eventId参数不能为空，请检查eventId的传递参数值！");
			return responseDTO;
		}

		TEvent tEvent = eventDAO.findOne(eventId);
		if (tEvent == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("您输入的eventId参数有误，eventId=“" + eventId + "”的活动不存在！");
			return responseDTO;
		}
		if (!tEvent.getFstatus().equals(20)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(101);
			responseDTO.setMsg("本活动未上架，不能报名呦！");
			return responseDTO;
		}
		BuyDTO buyDTO = new BuyDTO();

		buyDTO.setEventId(eventId);
		/*buyDTO.setEventTime(tEvent.getFeventTime());
		buyDTO.setEventTitle(tEvent.getFtitle());
		buyDTO.setOriginalPrice(tEvent.getFprice().toString());
		buyDTO.setPresentPrice(tEvent.getFdeal());
		buyDTO.setStockFlag(tEvent.getFstockFlag());*/
		buyDTO.setQuota(fxlService.getQuota(eventId));
		if (StringUtils.isNotBlank(tEvent.getFimage1())) {
			String imageUrl = fxlService.getImageUrl(tEvent.getFimage1(), true);
			buyDTO.setImageUrl(imageUrl);
		} else {
			buyDTO.setImageUrl(new StringBuilder().append(PropertiesUtil.getProperty("fileServerUrl"))
					.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPicThumbnail.jpg").toString());
		}

		StringBuilder hql = new StringBuilder();
		hql.append(
				"select t.id as id, t.ftitle as ftitle, t.faddress as faddress, t.fstartDate as fstartDate, t.fendDate as fendDate, t.fstartTime as fstartTime, t.fendTime as fendTime from TEventSession t where t.TEvent.id = :eventId and (t.fdeadline is null or t.fdeadline > now()) and t.fstatus < 999 and t.fsalesFlag = 0 order by t.forder desc");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("eventId", eventId);

		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);

		List<EventSessionDTO> sessionList = Lists.newArrayList();
		EventSessionDTO sessionDTO = null;
		Date date = null;
		for (Map<String, Object> amap : list) {
			sessionDTO = new EventSessionDTO();
			if (amap.get("faddress") != null && StringUtils.isNotBlank(amap.get("faddress").toString())) {
				buyDTO.setAddress(amap.get("faddress").toString());
			}
			if (amap.get("id") != null && StringUtils.isNotBlank(amap.get("id").toString())) {
				sessionDTO.setSessionId(amap.get("id").toString());
			}
			if (amap.get("ftitle") != null && StringUtils.isNotBlank(amap.get("ftitle").toString())) {
				sessionDTO.setTitle(amap.get("ftitle").toString());
			}
			if (amap.get("fstartDate") != null && StringUtils.isNotBlank(amap.get("fstartDate").toString())) {
				date = (Date) amap.get("fstartDate");
				sessionDTO.setSessionStartDate(DateFormatUtils.format(date, "yyyy-MM-dd"));
			}
			if (amap.get("fendDate") != null && StringUtils.isNotBlank(amap.get("fendDate").toString())) {
				date = (Date) amap.get("fendDate");
				sessionDTO.setSessionEndDate(DateFormatUtils.format(date, "yyyy-MM-dd"));
			}
			if (amap.get("fstartTime") != null && StringUtils.isNotBlank(amap.get("fstartTime").toString())) {
				sessionDTO.setSessionStartTime(amap.get("fstartTime").toString());
			}
			if (amap.get("fendTime") != null && StringUtils.isNotBlank(amap.get("fendTime").toString())) {
				sessionDTO.setSessionEndTime(amap.get("fendTime").toString());
			}
			// 将场次下所属的规格放到场次DTO中
			hql.delete(0, hql.length());
			hql.append(
					"select t.id as id, t.ftitle as ftitle, t.fprice as fprice, t.fdeal as fdeal, t.fpostage as fpostage, t.fstock as fstock, t.fstockUnit as fstockUnit from TEventSpec t where t.TEvent.id = :eventId and t.TEventSession.id = :sessionId and t.fstatus < 999 order by t.forder desc");
			hqlMap.clear();
			hqlMap.put("eventId", eventId);
			hqlMap.put("sessionId", amap.get("id"));
			list = commonService.find(hql.toString(), hqlMap);

			List<EventSpecDTO> sessionSpecList = Lists.newArrayList();
			EventSpecDTO specDTO = null;
			for (Map<String, Object> bmap : list) {
				specDTO = new EventSpecDTO();
				if (bmap.get("id") != null && StringUtils.isNotBlank(bmap.get("id").toString())) {
					specDTO.setSpecId(bmap.get("id").toString());
				}
				if (bmap.get("ftitle") != null && StringUtils.isNotBlank(bmap.get("ftitle").toString())) {
					specDTO.setTitle(bmap.get("ftitle").toString());
				}
				if (bmap.get("fprice") != null && StringUtils.isNotBlank(bmap.get("fprice").toString())) {
					specDTO.setPrice((BigDecimal) bmap.get("fprice"));
				}
				if (bmap.get("fdeal") != null && StringUtils.isNotBlank(bmap.get("fdeal").toString())) {
					specDTO.setDeal((BigDecimal) bmap.get("fdeal"));
				}
				if (bmap.get("fpostage") != null && StringUtils.isNotBlank(bmap.get("fpostage").toString())) {
					specDTO.setPostage((BigDecimal) bmap.get("fpostage"));
				}
				if (bmap.get("fstock") != null && StringUtils.isNotBlank(bmap.get("fstock").toString())) {
					specDTO.setStock((Integer) bmap.get("fstock"));
				}
				if (bmap.get("fstockUnit") != null && StringUtils.isNotBlank(bmap.get("fstockUnit").toString())) {
					specDTO.setStockUnit(bmap.get("fstockUnit").toString());
				}
				sessionSpecList.add(specDTO);
			}
			sessionDTO.setSpecList(sessionSpecList);
			sessionList.add(sessionDTO);
		}
		buyDTO.setSessionList(sessionList);

		hql.delete(0, hql.length());
		hql.append(
				"select t.id as id, t.ftitle as ftitle, t.fprice as fprice, t.fdeal as fdeal, t.fpostage as fpostage, t.fstock as fstock, t.fstockUnit as fstockUnit from TEventSpec t where t.TEvent.id = :eventId and t.TEventSession.id is null and t.fstatus < 999 order by t.forder desc");
		hqlMap.clear();
		hqlMap.put("eventId", eventId);
		list = commonService.find(hql.toString(), hqlMap);

		List<EventSpecDTO> specList = Lists.newArrayList();
		EventSpecDTO specDTO = null;
		for (Map<String, Object> amap : list) {
			specDTO = new EventSpecDTO();
			if (amap.get("id") != null && StringUtils.isNotBlank(amap.get("id").toString())) {
				specDTO.setSpecId(amap.get("id").toString());
			}
			if (amap.get("sessionId") != null && StringUtils.isNotBlank(amap.get("sessionId").toString())) {
				specDTO.setSessionId(amap.get("sessionId").toString());
			}
			if (amap.get("ftitle") != null && StringUtils.isNotBlank(amap.get("ftitle").toString())) {
				specDTO.setTitle(amap.get("ftitle").toString());
			}
			if (amap.get("fprice") != null && StringUtils.isNotBlank(amap.get("fprice").toString())) {
				specDTO.setPrice((BigDecimal) amap.get("fprice"));
			}
			if (amap.get("fdeal") != null && StringUtils.isNotBlank(amap.get("fdeal").toString())) {
				specDTO.setDeal((BigDecimal) amap.get("fdeal"));
			}
			if (amap.get("fpostage") != null && StringUtils.isNotBlank(amap.get("fpostage").toString())) {
				specDTO.setPostage((BigDecimal) amap.get("fpostage"));
			}
			if (amap.get("fstock") != null && StringUtils.isNotBlank(amap.get("fstock").toString())) {
				specDTO.setStock((Integer) amap.get("fstock"));
			}
			if (amap.get("fstockUnit") != null && StringUtils.isNotBlank(amap.get("fstockUnit").toString())) {
				specDTO.setStockUnit(amap.get("fstockUnit").toString());
			}
			specList.add(specDTO);
		}
		buyDTO.setSpecList(specList);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("buyInfo", buyDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO toFillOrder(Integer clientType, String ticket, String eventId, String sessionId, String specId,
			Integer count) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(eventId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("eventId参数不能为空，请检查eventId的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(sessionId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("sessionId参数不能为空，请检查sessionId的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(specId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(204);
			responseDTO.setMsg("specId参数不能为空，请检查specId的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}
		// 限购规则器判断是否可以购买
		if (fxlService.isQuota(eventId, customerDTO.getCustomerId(), count)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(107);
			responseDTO.setMsg("本活动为限购商品，请给其他朋友留一些哟！");
			return responseDTO;
		}

		TEvent tEvent = eventDAO.findOne(eventId);
		if (tEvent == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(205);
			responseDTO.setMsg("您输入的eventId参数有误，eventId=“" + eventId + "”的活动不存在！");
			return responseDTO;
		}
		if (count == null || count.intValue() < 1) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(206);
			responseDTO.setMsg("您输入的count参数有误，购买数量参数不能为空或者小于1！");
			return responseDTO;
		}
		TEventSession tEventSession = eventSessionDAO.getOne(sessionId);

		TEventSpec tEventSpec = eventSpecDAO.getOne(specId);
		/*if (tEvent.getFstockFlag().equals(20)) {
			if (tEventSpec.getFstock().intValue() <= 0) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(105);
				responseDTO.setMsg("您购买的活动被抢光了，请关注零到壹其它活动");
				return responseDTO;
			}
			if (tEventSpec.getFstock().intValue() < count.intValue()) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(106);
				responseDTO.setMsg("您购买的数量超过库存数量，无法进行购买！");
				return responseDTO;
			}
		}
*/
		BigDecimal total = tEventSpec.getFdeal().multiply(new BigDecimal(count.intValue())).setScale(2,
				RoundingMode.HALF_UP);
		if (tEventSpec.getFpostage() != null) {
			total = total.add(tEventSpec.getFpostage()).setScale(2, RoundingMode.HALF_UP);
		}

		BuyInfoDTO buyInfoDTO = new BuyInfoDTO();
		String couponDeliveryId = null;
		BigDecimal maxAmount = BigDecimal.ZERO;
		// 获取出该订单可使用最优优惠券
		Map<String, Object> map = couponService.getCouponByOrder(clientType, tEvent, customerDTO, total);
		if (map.get("couponDeliveryId") != null) {
			couponDeliveryId = map.get("couponDeliveryId").toString();
			maxAmount = new BigDecimal(map.get("maxAmount").toString());
		}
		buyInfoDTO.setCouponDeliveryId(couponDeliveryId);
		buyInfoDTO.setAmount(maxAmount);
		buyInfoDTO.setOrderType(tEvent.getForderType());
	//	buyInfoDTO.setAppointment(tEvent.getFappointment());
		buyInfoDTO.setReturnReplace(tEvent.getFreturn());
		buyInfoDTO.setUsePreferential(tEvent.getFusePreferential());
		buyInfoDTO.setEventId(eventId);
		buyInfoDTO.setEventTitle(tEvent.getFtitle());
		buyInfoDTO.setSessionId(sessionId);
		buyInfoDTO.setSessionTitle(tEventSession.getFtitle());
		buyInfoDTO.setSpecId(specId);
		buyInfoDTO.setSpecTitle(tEventSpec.getFtitle());
		int specPerson = 0;
		if (tEventSpec.getFadult() != null) {
			specPerson += tEventSpec.getFadult().intValue();
		}
		if (tEventSpec.getFchild() != null) {
			specPerson += tEventSpec.getFchild().intValue();
		}
		buyInfoDTO.setSpecPerson(specPerson * count.intValue());
		buyInfoDTO.setPrice(tEventSpec.getFprice());
		buyInfoDTO.setDeal(tEventSpec.getFdeal());
		buyInfoDTO.setCount(count);
		buyInfoDTO.setPostage(tEventSpec.getFpostage());
		buyInfoDTO.setReceivableTotal(total);
		if (total.subtract(maxAmount).compareTo(BigDecimal.ZERO) < 0) {
			buyInfoDTO.setTotal(BigDecimal.ZERO);
		} else {
			buyInfoDTO.setTotal(total.subtract(maxAmount));
		}
		if (tEvent.getForderType().intValue() == 2) {
			List<TCommonInfo> tCommonInfoList = commonInfoDAO.findByFcustomerIdAndFtype(customerDTO.getCustomerId(), 1);
			if (CollectionUtils.isNotEmpty(tCommonInfoList)) {
				TCommonInfo tCommonInfo = tCommonInfoList.get(0);
				if (StringUtils.isNotBlank(tCommonInfo.getFinfo())) {
					RecipientDTO recipientDTO = mapper.fromJson(tCommonInfo.getFinfo(), RecipientDTO.class);
					if (recipientDTO != null) {
						buyInfoDTO.setRecipient(recipientDTO.getRecipient());
						buyInfoDTO.setPhone(recipientDTO.getPhone());
						buyInfoDTO.setAddress(recipientDTO.getAddress());
					}
				}
			}
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("buyInfo", buyInfoDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public ResponseDTO toPayOrder(Integer clientType, String ticket, String eventId, String sessionId, String specId,
			Integer count, Integer payClientType, String recipient, String phone, String address, String insuranceInfo,
			String remark, Integer payType, String couponDeliveryId, String ip, Integer fchannel, String gps,
			String deviceId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(eventId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("eventId参数不能为空，请检查eventId的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(sessionId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("sessionId参数不能为空，请检查sessionId的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(specId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(204);
			responseDTO.setMsg("specId参数不能为空，请检查specId的传递参数值！");
			return responseDTO;
		}
		if (payType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(205);
			responseDTO.setMsg("payType参数不能为空，请检查payType的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}
		// 限购规则器判断是否可以购买
		if (fxlService.isQuota(eventId, customerDTO.getCustomerId(), count)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(107);
			responseDTO.setMsg("本活动为限购商品，请给其他朋友留一些哟！");
			return responseDTO;
		}

		TEvent tEvent = eventDAO.findOne(eventId);
		if (tEvent == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(205);
			responseDTO.setMsg("您输入的eventId参数有误，eventId=“" + eventId + "”的活动不存在！");
			return responseDTO;
		}
		if (count == null || count.intValue() < 1) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(206);
			responseDTO.setMsg("您输入的count参数有误，购买数量参数不能为空或者小于1！");
			return responseDTO;
		}
		String orderNum = NumberUtil.getOrderNum(1);
		TSponsor tSponsor = tEvent.getTSponsor();
		TOrder tOrder = new TOrder();

		TEventSession tEventSession = eventSessionDAO.getOne(sessionId);

		TEventSpec tEventSpec = eventSpecDAO.getOne(specId);
		/*if (tEvent.getFstockFlag().equals(20)) {
			if (tEventSpec.getFstock().intValue() <= 0) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(105);
				responseDTO.setMsg("您购买的活动被抢光了，请关注零到壹其它活动");
				return responseDTO;
			}
			if (tEventSpec.getFstock().intValue() < count.intValue()) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(106);
				responseDTO.setMsg("您购买的数量超过库存数量，无法进行购买！");
				return responseDTO;
			}
			eventSpecDAO.subtractStock(count, specId);
		}*/
		//eventDAO.subStock(count, eventId);
		fxlService.addCustomerBuy(eventId, customerDTO.getCustomerId(), count);

		tOrder.setTEvent(tEvent);
		tOrder.setFcityId(tEvent.getFcity());
		tOrder.setFeventTitle(tEvent.getFtitle());
		tOrder.setForderType(tEvent.getForderType());
		//tOrder.setFappointment(tEvent.getFappointment());
		tOrder.setFreturn(tEvent.getFreturn());
		tOrder.setFusePreferential(tEvent.getFusePreferential());
		tOrder.setFverificationType(tEvent.getFverificationType());
		// 获取到活动类目缓存对象
		// 获取到活动类目缓存对象
		String type = categoryService.getCategoryA(tEvent.getFtypeA());
		tOrder.setFtypeA(type != null ? type : StringUtils.EMPTY);
		tOrder.setTSponsor(tSponsor);
		tOrder.setFsponsorName(tSponsor.getFname());
		tOrder.setFsponsorFullName(tSponsor.getFfullName());
		tOrder.setFsponsorPhone(tSponsor.getFphone());
		tOrder.setFsponsorNumber(tSponsor.getFnumber());
		tOrder.setForderNum(orderNum);
		tOrder.setTCustomer(new TCustomer(customerDTO.getCustomerId()));
		tOrder.setFcustomerName(customerDTO.getName());
		if (StringUtils.isNotBlank(customerDTO.getPhone())) {
			tOrder.setFcustomerPhone(customerDTO.getPhone());
		} else {
			tOrder.setFcustomerPhone(phone);
		}
		tOrder.setFcustomerSex(DictionaryUtil.getString(DictionaryUtil.Sex, customerDTO.getSex()));
		tOrder.setTEventSession(tEventSession);
		tOrder.setFsessionTitle(tEventSession.getFtitle());
		tOrder.setTEventSpec(tEventSpec);
//		tOrder.setFspecTitle(tEventSpec.getFtitle());
		tOrder.setFcount(count);
		tOrder.setFpostage(tEventSpec.getFpostage());
		//tOrder.setFstockFlag(tEvent.getFstockFlag());
		tOrder.setFprice(tEventSpec.getFdeal());
		BigDecimal total = tEventSpec.getFdeal().multiply(new BigDecimal(count.intValue())).setScale(2,
				RoundingMode.HALF_UP);
		if (tEventSpec.getFpostage() != null) {
			total = total.add(tEventSpec.getFpostage()).setScale(2, RoundingMode.HALF_UP);
		}
		tOrder.setFtotal(total);
		tOrder.setFreceivableTotal(total);
		tOrder.setFremark(remark);
		OrderRecipientDTO recipientDTO = new OrderRecipientDTO();
		recipientDTO.setRecipient(recipient);
		recipientDTO.setPhone(phone);
		recipientDTO.setAddress(address);
		recipientDTO.setInsuranceInfo(insuranceInfo);
		int specPerson = 0;
		if (tEventSpec.getFadult() != null) {
			specPerson += tEventSpec.getFadult().intValue();
		}
		if (tEventSpec.getFchild() != null) {
			specPerson += tEventSpec.getFchild().intValue();
		}
		recipientDTO.setSpecPerson(specPerson * count.intValue());
		String recipientJson = mapper.toJson(recipientDTO);
		tOrder.setFrecipient(recipientJson);
		Date now = new Date();
		tOrder.setFcreateTime(now);
		Date fdate = null;
		if (Constant.getUnPayFailureMinute() != 0) {
			fdate = DateUtils.addMinutes(now, Constant.getUnPayFailureMinute());
			tOrder.setFunPayFailureTime(fdate);
		}
		tOrder.setFlockFlag(0);
		// 增加1.下单渠道 2.下单地址(下单地址定位失败则用ip定位转换经纬度存入)3.唯一标识
		boolean gpsIsError = false;
		if (StringUtils.isBlank(gps)) {
			gpsIsError = true;
		} else {
			if (!gps.contains(",") || gps.contains("E")) {
				gpsIsError = true;
			}
			String[] gpsa = StringUtils.split(gps, ',');
			if (gpsa.length != 2) {
				gpsIsError = true;
			} else if (gpsa.length == 2) {
				if (!NumberUtils.isCreatable(gpsa[0]) || !NumberUtils.isCreatable(gpsa[1])) {
					gpsIsError = true;
				}
			} else {
				gpsIsError = true;
			}
		}

		tOrder.setFchannel(fchannel);
		tOrder.setFgps(gps);
		tOrder.setFdeviceId(deviceId);
		tOrder.setFsource(1);
		tOrder = orderDAO.save(tOrder);

		// 如果规格被买完，则发送钉钉推送
		if (tEventSpec.getFstock().intValue() == count.intValue()) {
			EventSoldOutBean eso = new EventSoldOutBean();
			eso.setEventTitle(tOrder.getFeventTitle());
			eso.setSessionTitle(tOrder.getFsessionTitle());
//			eso.setSpecTitle(tOrder.getFspecTitle());
			eso.setTaskType(10);
			AsynchronousTasksManager.put(eso);
		}

		// 如果传入的gps格式信息错误，将使用百度地图IP地址定位方式
		if (gpsIsError) {
			OrderUpdateOrderGpsByIpBean ouog = new OrderUpdateOrderGpsByIpBean();
			ouog.setIp(ip);
			ouog.setOrderId(tOrder.getId());
			ouog.setTaskType(6);
			AsynchronousTasksManager.put(ouog);
		}
		// 如果用户选择了优惠券进行付款，则进行优惠券抵扣
		// 判断优惠券是否可用
		TCustomer customer = customerDAO.findOne(customerDTO.getCustomerId());
		TCouponDelivery tCouponDelivery = couponDeliveryDAO.getCouponbycustomer(couponDeliveryId, customer.getId(),
				customer.getFcreateTime());
		if (StringUtils.isNotBlank(couponDeliveryId)) {
			if (!couponService.chackCouponIsOK(customer, couponDeliveryId, tCouponDelivery)) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(106);
				responseDTO.setMsg("您选定的无效的优惠券，暂无法进行下单！");
				return responseDTO;
			}
			// TODO 只考虑的满减的情况，要加上打折的计算规则
			TCouponInformation tCoupon = couponInformationDAO.findOne(tCouponDelivery.getTCouponInformation().getId());
			total = tOrder.getFreceivableTotal().subtract(tCoupon.getFamount());
			if (total.compareTo(BigDecimal.ZERO) < 0) {
				total = BigDecimal.ZERO;
			}
			tOrder.setFtotal(total);
			tOrder.setFchangeAmount(tCoupon.getFamount());
			tOrder.setFchangeAmountInstruction("使用了优惠券抵扣了" + tCoupon.getFamount().toString() + "元");
			tOrder = orderDAO.save(tOrder);
			// 如果是零元单，无需支付，所以将优惠券直接变更为20（已使用状态）
			couponService.usecoupon(customer, tCouponDelivery, tOrder);

			// 保存优惠金额
			TOrderAmountChange tOrderAmountChange = new TOrderAmountChange();
			tOrderAmountChange.setFbonusChange(0);
			tOrderAmountChange.setFbargainChange(BigDecimal.ZERO);
			tOrderAmountChange.setFcouponChange(tCoupon.getFamount());
			tOrderAmountChange.setFcreateTime(tOrder.getFcreateTime());
			tOrderAmountChange.setForderId(tOrder.getId());
			tOrderAmountChange.setFotherChange(BigDecimal.ZERO);
			tOrderAmountChange.setFspellChange(BigDecimal.ZERO);
			orderAmountChangeDAO.save(tOrderAmountChange);

		}

		Map<String, Object> returnData = Maps.newHashMap();
		// 如果订单是则返回支付成功信息，如果是非零元单则返回支付信息
		if (tOrder.getFtotal().compareTo(BigDecimal.ZERO) == 0) {
			// 如果是零元单，则直接记录订单状态变更为已支付状态
			fxlService.orderStatusChange(1, customerDTO.getName(), tOrder.getId(), null, 0, 20);
			// 变更订单状态为已支付和支付类型是零元单支付
			orderDAO.updateOrderStatusAndPayType(20, 90, clientType,tOrder.getId());
			// 更改用户附加信息表
			OrderUpdateCustomerInfoBean ouci = new OrderUpdateCustomerInfoBean();
			ouci.setCreateTime(now);
			ouci.setCustomerId(customer.getId());
			ouci.setOrderId(tOrder.getId());
			ouci.setTotal(tOrder.getFtotal());
			ouci.setTaskType(2);
			AsynchronousTasksManager.put(ouci);

			// 添加线程任务发送购买成功通知短信
			OrderSendSmsBean oss = new OrderSendSmsBean();
			oss.setCreateTime(now);
			oss.setCustomerId(customer.getId());
			oss.setCustomerName(tOrder.getFcustomerName());
			oss.setCustomerPhone(tOrder.getFcustomerPhone());
			oss.setEventTitle(tOrder.getFeventTitle());
			oss.setOrderId(tOrder.getId());
			oss.setOrderNum(tOrder.getForderNum());
			oss.setTaskType(3);
			AsynchronousTasksManager.put(oss);

			returnData.put("zero", true);
			returnData.put("orderId", tOrder.getId());
			responseDTO.setMsg("订单支付成功，带着宝宝去体验吧！");
		} else {
			// 如果是非零元单，则记录订单状态变更为未支付状态
			fxlService.orderStatusChange(1, customerDTO.getName(), tOrder.getId(), null, 0, 10);
			// 将添加订单超时未支付取消定时任务
			if (fdate != null) {
				TTimingTask timingTask = new TTimingTask();
				timingTask.setEntityId(tOrder.getId());
				timingTask.setTaskTime(fdate.getTime());
				timingTask.setTaskType(7);
				timingTaskDAO.save(timingTask);
			}
			String orderPushWeiXin = ConfigurationUtil
					.getPropertiesValue(ResponseConfigurationDict.RESPONSE_PROPERTIES_UNPAIDORDER);
			if (StringUtils.isNotBlank(orderPushWeiXin) && orderPushWeiXin.equals("1")) {
				// 添加发送订单未支付通知模板消息
				TTimingTask timingTask = new TTimingTask();
				timingTask.setEntityId(tOrder.getId());
				timingTask.setTaskTime(DateUtils.addMinutes(fdate, -20).getTime());
				timingTask.setTaskType(18);
				timingTaskDAO.save(timingTask);
			}

			// 根据用户选择的支付类型，来进行条用不同的支付接口
			WxPayResult wxPayResult = null;
			if (payType.intValue() == 20) {
				// 调用微信支付操作
				try {
					// TODO根据支付方式调用不同的支付接口，获取prePayId返回给前台
					String nonceStr = RandomStringUtils.randomAlphanumeric(32);

					// ip = "192.168.1.1";
					if (payClientType.intValue() == 1) {
						wxPayResult = WxPayUtil.wxPay(tOrder.getForderNum(), tOrder.getFeventTitle(),
								tOrder.getFtotal().multiply(new BigDecimal(100)).intValue(), customerDTO.getWxId(), now,
								DateUtils.addDays(now, 1), ip, nonceStr);
					} else {
						wxPayResult = WxPayUtil.wxAppPay(tOrder.getForderNum(), tOrder.getFeventTitle(),
								tOrder.getFtotal().multiply(new BigDecimal(100)).intValue(), now,
								DateUtils.addDays(now, 1), ip, nonceStr);
					}
					logger.info(wxPayResult.getResponse());

					TWxPay tWxPay = wxPayDAO.getByOrderIdAndInOutAndStatus(tOrder.getId(), 1, 10);
					if (tWxPay == null) {
						tWxPay = new TWxPay();
						tWxPay.setFclientType(payClientType);
						tWxPay.setFinOut(1);
						tWxPay.setFcreateTime(now);
						tWxPay.setForderId(tOrder.getId());
						tWxPay.setForderType(1);
						tWxPay.setTCustomer(tOrder.getTCustomer());
						tWxPay.setFstatus(10);
					}
					tWxPay.setFupdateTime(now);
					tWxPay.setFppResponseInfo(wxPayResult.getResponse());
					wxPayDAO.save(tWxPay);

				} catch (Exception e) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("orderId", tOrder.getId());
					map.put("customerId", customerDTO.getCustomerId());
					OutPutLogUtil.printLoggger(e, map, logger);
					responseDTO.setSuccess(false);
					responseDTO.setStatusCode(105);
					responseDTO.setMsg("调用微信支付接口时出错，请稍后再进行支付！");
					return responseDTO;
				}
			}
			// 变更订单状态为待支付和支付类型是相应支付类型
			orderDAO.updateOrderStatusAndPayType(10, payType,clientType, tOrder.getId());

			PayDTO payDTO = new PayDTO();
			payDTO.setAppId(wxPayResult.getAppId());
			payDTO.setPartnerId(wxPayResult.getPartnerId());
			payDTO.setPayType(payType);
			payDTO.setOrderId(tOrder.getId());
			payDTO.setOrderNum(tOrder.getForderNum());
			payDTO.setPrepayId(wxPayResult.getPrepayId());
			payDTO.setNonceStr(wxPayResult.getNonceStrVal());
			payDTO.setPaySign(wxPayResult.getPaySign());
			payDTO.setTimestamp(wxPayResult.getTimestamp());
			payDTO.setSignType(wxPayResult.getSignType());
			payDTO.setPayPackage(wxPayResult.getPayPackage());

			returnData.put("zero", false);
			returnData.put("payInfo", payDTO);
		}
		// 添加线程任务修改客户附加信息表
		OrderUpdateCustomerTagBean ouct = new OrderUpdateCustomerTagBean();
		ouct.setCreateTime(now);
		ouct.setCustomerId(customer.getId());
		ouct.setOrderId(tOrder.getId());
		ouct.setTotal(tOrder.getFtotal());
		ouct.setTaskType(5);
		AsynchronousTasksManager.put(ouct);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public ResponseDTO againToFillOrder(Integer clientType, String ticket, String orderId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(orderId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("orderId参数不能为空，请检查orderId的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		TOrder tOrder = orderDAO.findOne(orderId);
		if (tOrder == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(204);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不存在！");
			return responseDTO;
		}
		if (!customerDTO.getCustomerId().equals(tOrder.getTCustomer().getId())) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(205);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不属于该客户！");
			return responseDTO;
		}
		if (tOrder.getFstatus().intValue() == 20) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(104);
			responseDTO.setMsg("该订单已完成支付，请勿重复操作！");
			return responseDTO;
		}
		if (tOrder.getFlockFlag().intValue() == 1) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(105);
			responseDTO.setMsg("您的订单已锁定状态，暂无法进行操作！");
			return responseDTO;
		}

		OrderDTO orderDTO = new OrderDTO();
		if (StringUtils.isNotBlank(tOrder.getFrecipient())) {
			OrderRecipientDTO recipientDTO = mapper.fromJson(tOrder.getFrecipient(), OrderRecipientDTO.class);
			if (recipientDTO != null) {
//				orderDTO.setRecipient(recipientDTO.getRecipient());
//				orderDTO.setPhone(recipientDTO.getPhone());
//				orderDTO.setAddress(recipientDTO.getAddress());
//				orderDTO.setSpecPerson(recipientDTO.getSpecPerson());
//				orderDTO.setInsuranceInfo(recipientDTO.getInsuranceInfo());
			}
		}

		orderDTO.setOrderId(tOrder.getId());
		orderDTO.setOrderNum(tOrder.getForderNum());
		orderDTO.setOrderType(tOrder.getForderType());
//		orderDTO.setAppointment(tOrder.getFappointment());
//		orderDTO.setReturnReplace(tOrder.getFreturn());
//		orderDTO.setUsePreferential(tOrder.getFusePreferential());
//		orderDTO.setVerificationType(tOrder.getFverificationType());
		orderDTO.setGoodsId(tOrder.getTEvent().getId());
		orderDTO.setGoodsTitle(tOrder.getFeventTitle());
		TEvent tEvent = tOrder.getTEvent();
//		orderDTO.setGoodsImageUrl(fxlService.getImageUrl(tEvent.getFimage1(), true));
		TEventSession tEventSession = tOrder.getTEventSession();
//		orderDTO.setEventAddress(tEventSession.getFaddress());
//		orderDTO.setValidityStart(tEventSession.getFstartDate() != null
//				? DateFormatUtils.format(tEventSession.getFstartDate(), "yyyy-MM-dd") : StringUtils.EMPTY);
//		orderDTO.setValidityEnd(tEventSession.getFendDate() != null
//				? DateFormatUtils.format(tEventSession.getFendDate(), "yyyy-MM-dd") : StringUtils.EMPTY);
//		orderDTO.setSession(tOrder.getFsessionTitle());
//		orderDTO.setSessionTitle(tOrder.getFsessionTitle());
//		orderDTO.setSpec(tOrder.getFspecTitle());
//		orderDTO.setSpecTitle(tOrder.getFspecTitle());
//		orderDTO.setPrice(tOrder.getFprice());
//		orderDTO.setCount(tOrder.getFcount());
//		orderDTO.setPostage(tOrder.getFpostage());
//		orderDTO.setAmount(tOrder.getFchangeAmount() == null ? BigDecimal.ZERO : tOrder.getFchangeAmount());
//		orderDTO.setChangeAmount(tOrder.getFchangeAmount());
//		orderDTO.setChangeAmountInstruction(tOrder.getFchangeAmountInstruction());
//		orderDTO.setReceivableTotal(tOrder.getFreceivableTotal());
//		orderDTO.setCustomer(tOrder.getFcustomerName());
//		orderDTO.setCustomerPhone(tOrder.getFcustomerPhone());
		orderDTO.setTotal(tOrder.getFtotal());
		orderDTO.setStatus(tOrder.getFstatus());
		orderDTO.setStatusString(DictionaryUtil.getString(DictionaryUtil.OrderStatus, tOrder.getFstatus()));
//		orderDTO.setRemark(tOrder.getFremark());

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("orderInfo", orderDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public ResponseDTO againToPayOrder(Integer clientType, String ticket, String orderId, Integer payClientType,
			String recipient, String phone, String address, String insuranceInfo, String remark, Integer payType,
			String ip) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(orderId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("orderId参数不能为空，请检查orderId的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		TOrder tOrder = orderDAO.findOne(orderId);
		if (tOrder == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(204);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不存在！");
			return responseDTO;
		}
		if (!customerDTO.getCustomerId().equals(tOrder.getTCustomer().getId())) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(205);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不属于该客户！");
			return responseDTO;
		}
		if (tOrder.getFstatus().intValue() == 20) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(104);
			responseDTO.setMsg("该订单已完成支付，请勿重复操作！");
			return responseDTO;
		}
		if (tOrder.getFlockFlag().intValue() == 1) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(105);
			responseDTO.setMsg("您的订单已锁定状态，暂无法进行操作！");
			return responseDTO;
		}

		OrderRecipientDTO recipientDTO = null;
		if (StringUtils.isNotBlank(tOrder.getFrecipient())) {
			recipientDTO = mapper.fromJson(tOrder.getFrecipient(), OrderRecipientDTO.class);
		} else {
			recipientDTO = new OrderRecipientDTO();
		}
		recipientDTO.setRecipient(recipient);
		recipientDTO.setPhone(phone);
		recipientDTO.setAddress(address);
		recipientDTO.setInsuranceInfo(insuranceInfo);
		recipientDTO.setSpecPerson(recipientDTO.getSpecPerson());
		String recipientJson = mapper.toJson(recipientDTO);

		Map<String, Object> returnData = Maps.newHashMap();

		// 根据用户选择的支付类型，来进行条用不同的支付接口
		WxPayResult wxPayResult = null;
		if (payType.intValue() == 20) {
			// 调用微信支付操作
			try {
				// TODO根据支付方式调用不同的支付接口，获取prePayId返回给前台
				String nonceStr = RandomStringUtils.randomAlphanumeric(32);

				// ip = "192.168.1.1";
				Date now = new Date();
				if (payClientType.intValue() == 1) {
					wxPayResult = WxPayUtil.wxPay(tOrder.getForderNum(), tOrder.getFeventTitle(),
							tOrder.getFtotal().multiply(new BigDecimal(100)).intValue(), customerDTO.getWxId(), now,
							DateUtils.addMinutes(now, 6), ip, nonceStr);
				} else {
					wxPayResult = WxPayUtil.wxAppPay(tOrder.getForderNum(), tOrder.getFeventTitle(),
							tOrder.getFtotal().multiply(new BigDecimal(100)).intValue(), now,
							DateUtils.addMinutes(now, 6), ip, nonceStr);
				}
				logger.info(wxPayResult.getResponse());
				orderDAO.updateOrderPayTypeAndRecipientAndRemark(payType, recipientJson, remark, orderId);

				TWxPay tWxPay = wxPayDAO.getByOrderIdAndInOutAndStatus(tOrder.getId(), 1, 10);
				if (tWxPay == null) {
					tWxPay = new TWxPay();
					tWxPay.setFclientType(payClientType);
					tWxPay.setFinOut(1);
					tWxPay.setFcreateTime(now);
					tWxPay.setForderId(tOrder.getId());
					tWxPay.setForderType(1);
					tWxPay.setTCustomer(tOrder.getTCustomer());
					tWxPay.setFstatus(10);
				}
				tWxPay.setFupdateTime(now);
				tWxPay.setFppResponseInfo(wxPayResult.getResponse());
				wxPayDAO.save(tWxPay);

			} catch (Exception e) {
				logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(105);
				responseDTO.setMsg("调用微信支付接口时出错，请稍后再进行支付！");
				return responseDTO;
			}
		}

		PayDTO payDTO = new PayDTO();
		payDTO.setAppId(wxPayResult.getAppId());
		payDTO.setPartnerId(wxPayResult.getPartnerId());
		payDTO.setPayType(payType);
		payDTO.setOrderId(tOrder.getId());
		payDTO.setOrderNum(tOrder.getForderNum());
		payDTO.setPrepayId(wxPayResult.getPrepayId());
		payDTO.setNonceStr(wxPayResult.getNonceStrVal());
		payDTO.setPaySign(wxPayResult.getPaySign());
		payDTO.setTimestamp(wxPayResult.getTimestamp());
		payDTO.setSignType(wxPayResult.getSignType());
		payDTO.setPayPackage(wxPayResult.getPayPackage());

		returnData.put("zero", false);
		returnData.put("payInfo", payDTO);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public ResponseDTO carnivalOrder(String customerId, String eventId, String sessionId, String specId, Integer count,
			String phone) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(customerId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("customerId参数不能为空，请检查customerId的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(eventId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("eventId参数不能为空，请检查eventId的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(sessionId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("sessionId参数不能为空，请检查sessionId的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(specId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(204);
			responseDTO.setMsg("specId参数不能为空，请检查specId的传递参数值！");
			return responseDTO;
		}

		CustomerDTO customerDTO = fxlService.getCustomerByCustomerId(customerId);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		TEvent tEvent = eventDAO.findOne(eventId);
		if (tEvent == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(205);
			responseDTO.setMsg("您输入的eventId参数有误，eventId=“" + eventId + "”的活动不存在！");
			return responseDTO;
		}
		if (count == null || count.intValue() < 1) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(206);
			responseDTO.setMsg("您输入的count参数有误，购买数量参数不能为空或者小于1！");
			return responseDTO;
		}
		String orderNum = NumberUtil.getOrderNum(1);
		TSponsor tSponsor = tEvent.getTSponsor();
		TOrder tOrder = new TOrder();

		TEventSession tEventSession = eventSessionDAO.getOne(sessionId);

		TEventSpec tEventSpec = eventSpecDAO.getOne(specId);
		/*if (tEvent.getFstockFlag().equals(20)) {
			if (tEventSpec.getFstock().intValue() <= 0) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(105);
				responseDTO.setMsg("您购买的活动被抢光了，请关注零到壹其它活动");
				return responseDTO;
			}
			if (tEventSpec.getFstock().intValue() < count.intValue()) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(106);
				responseDTO.setMsg("您购买的数量超过库存数量，无法进行购买！");
				return responseDTO;
			}
			eventSpecDAO.subtractStock(count, specId);
		}*/
		//eventDAO.subStock(count, eventId);

		tOrder.setTEvent(tEvent);
		tOrder.setFcityId(tEvent.getFcity());
		tOrder.setFeventTitle(tEvent.getFtitle());
		tOrder.setForderType(tEvent.getForderType());
		//tOrder.setFappointment(tEvent.getFappointment());
		tOrder.setFreturn(tEvent.getFreturn());
		tOrder.setFusePreferential(tEvent.getFusePreferential());
		tOrder.setFverificationType(tEvent.getFverificationType());
		// 获取到活动类目缓存对象
		// 获取到活动类目缓存对象
		String type = categoryService.getCategoryA(tEvent.getFtypeA());
		tOrder.setFtypeA(type != null ? type : StringUtils.EMPTY);
		tOrder.setTSponsor(tSponsor);
		tOrder.setFsponsorName(tSponsor.getFname());
		tOrder.setFsponsorFullName(tSponsor.getFfullName());
		tOrder.setFsponsorPhone(tSponsor.getFphone());
		tOrder.setFsponsorNumber(tSponsor.getFnumber());
		tOrder.setForderNum(orderNum);
		tOrder.setTCustomer(new TCustomer(customerDTO.getCustomerId()));
		tOrder.setFcustomerName(customerDTO.getName());
		if (StringUtils.isNotBlank(customerDTO.getPhone())) {
			tOrder.setFcustomerPhone(customerDTO.getPhone());
		} else {
			tOrder.setFcustomerPhone(phone);
		}
		tOrder.setFcustomerSex(DictionaryUtil.getString(DictionaryUtil.Sex, customerDTO.getSex()));
		tOrder.setTEventSession(tEventSession);
		tOrder.setFsessionTitle(tEventSession.getFtitle());
		tOrder.setTEventSpec(tEventSpec);
//		tOrder.setFspecTitle(tEventSpec.getFtitle());
		tOrder.setFcount(count);
		tOrder.setFpostage(tEventSpec.getFpostage());
		//tOrder.setFstockFlag(tEvent.getFstockFlag());
		tOrder.setFprice(tEventSpec.getFdeal());
		BigDecimal total = tEventSpec.getFdeal().multiply(new BigDecimal(count.intValue())).setScale(2,
				RoundingMode.HALF_UP);
		if (tEventSpec.getFpostage() != null) {
			total = total.add(tEventSpec.getFpostage()).setScale(2, RoundingMode.HALF_UP);
		}
		tOrder.setFtotal(total);
		tOrder.setFreceivableTotal(total);
		tOrder.setFremark("2016北京城市科学节奖品订单");
		Date now = new Date();
		tOrder.setFcreateTime(now);
		tOrder.setFlockFlag(0);
		tOrder.setFchannel(101);

		tOrder = orderDAO.save(tOrder);

		Map<String, Object> returnData = Maps.newHashMap();
		// 如果是零元单，则直接记录订单状态变更为已支付状态
		fxlService.orderStatusChange(1, customerDTO.getName(), tOrder.getId(), null, 0, 20);
		// 变更订单状态为已支付和支付类型是零元单支付
		orderDAO.updateOrderStatusAndPayType(20, 90,1, tOrder.getId());

		returnData.put("zero", true);
		returnData.put("orderId", tOrder.getId());
		responseDTO.setMsg("订单支付成功，带着宝宝去体验吧！");

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public ResponseDTO cancelNotPayOrder(Integer clientType, String ticket, String orderId, String refundReason) {
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

		TOrder tOrder = orderDAO.findOne(orderId);
		if (tOrder == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不存在！");
			return responseDTO;
		}
		if (!customerDTO.getCustomerId().equals(tOrder.getTCustomer().getId())) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不属于该客户！");
			return responseDTO;
		}
		if (tOrder.getFlockFlag().intValue() == 1) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(103);
			responseDTO.setMsg("您的订单已锁定状态，暂无法进行操作！");
			return responseDTO;
		}
		if (tOrder.getFstatus().intValue() != 10) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(104);
			responseDTO.setMsg("您的订单不是未支付状态，请走退款流程！");
			return responseDTO;
		}
		fxlService.orderStatusChange(1, customerDTO.getName(), orderId, refundReason, tOrder.getFstatus(), 100);
		orderDAO.updateOrderStatusAndRefundTimeAndRefundReason(100, new Date(), refundReason, orderId);
		// 删除订单超时未支付取消订单任务
		timingTaskDAO.clearTimeTaskByEntityIdAndTaskType(tOrder.getId(), 7);
		// 将订单占用的库存数量退回
		eventSpecDAO.addStock(tOrder.getFcount(), tOrder.getTEventSpec().getId());
		// 如果用了优惠券，则释放该优惠券使用状态
		couponService.backCoupon(orderId);
		//eventDAO.addStock(tOrder.getFcount(), tOrder.getTEvent().getId());
		fxlService.subCustomerBuy(tOrder.getTEvent().getId(), customerDTO.getCustomerId(), tOrder.getFcount());

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("取消未支付订单成功！");
		return responseDTO;
	}

	public ResponseDTO cancelPayOrder(Integer clientType, String ticket, String orderId, String refundReason) {
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

		TOrder tOrder = orderDAO.findOne(orderId);
		if (tOrder == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不存在！");
			return responseDTO;
		}
		if (!customerDTO.getCustomerId().equals(tOrder.getTCustomer().getId())) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不属于该客户！");
			return responseDTO;
		}
		if (tOrder.getFlockFlag().intValue() == 1) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(103);
			responseDTO.setMsg("您的订单已锁定状态，暂无法进行操作！");
			return responseDTO;
		}
		if (tOrder.getFstatus().intValue() >= 60) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(104);
			responseDTO.setMsg("您的订单已经消费无法取消！");
			return responseDTO;
		}

		Date now = new Date();
		// 如果是零元单，直接改变订单状态为“退款完成”
		if (tOrder.getFtotal().compareTo(BigDecimal.ZERO) == 0) {
			fxlService.orderStatusChange(1, customerDTO.getName(), orderId, refundReason, tOrder.getFstatus(), 120);
			orderDAO.updateOrderStatusAndRefundTimeAndRefundReason(120, now, refundReason, orderId);
			// 将订单占用的库存数量退回
			eventSpecDAO.addStock(tOrder.getFcount(), tOrder.getTEventSpec().getId());
			// 将客户附加信息表数据改回
			try {
				backCustomerInfo(tOrder);
			} catch (Exception e) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("orderId", tOrder.getId());
				map.put("customerId", customerDTO.getCustomerId());
				OutPutLogUtil.printLoggger(e, map, logger);
			}

			//eventDAO.addStock(tOrder.getFcount(), tOrder.getTEvent().getId());
			fxlService.subCustomerBuy(tOrder.getTEvent().getId(), customerDTO.getName(), tOrder.getFcount());
			responseDTO.setMsg("订单取消成功！");
		} else {
			// 非零元单则将状态改为“申请退款”，等待运维人员审核退款
			fxlService.orderStatusChange(1, customerDTO.getName(), orderId, refundReason, tOrder.getFstatus(), 110);
			orderDAO.updateOrderStatusAndRefundTimeAndRefundReason(110, now, refundReason, orderId);
			responseDTO.setMsg("退款申请已成功提交！");
			try {
				wxService.pushRefundMsg(tOrder, customerDTO);
			} catch (Exception e) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("customerId", tOrder.getTCustomer().getId());
				map.put("orderId", tOrder.getId());
				OutPutLogUtil.printLoggger(e, map, logger);
				logger.error("用户取消订单时发送微信退款申请模板消息时，发送微信退款申请模板消息接口出错。");
			}
		}

		// 同步发送钉钉通知
		try {
			if (StringUtils.isNotBlank(DingTalkUtil.getRefundSyncingDingTalk())) {
				String msg = new StringBuilder().append("用户订单退款提醒：用户[").append(tOrder.getFcustomerName()).append("]")
						.append("电话[").append(tOrder.getFcustomerPhone()).append("]").append("在")
						.append(DateFormatUtils.format(now, "yyyy年MM月dd日HH时mm分")).append("提交了订单退款").append("[")
						.append(tOrder.getFeventTitle()).append("]").append("【订单号：").append(tOrder.getForderNum())
						.append("】").append("。请登录FOMS系统进行处理。http://www.fangxuele.com:8081/foms").toString();
				DingTalkUtil.sendDingTalk(msg, DingTalkUtil.getRefundSyncingDingTalk());
			}
		} catch (Exception e) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("orderId", tOrder.getId());
			map.put("customerId", customerDTO.getCustomerId());
			OutPutLogUtil.printLoggger(e, map, logger);
			logger.error("用户取消订单时调用钉钉通知时，钉钉通知接口出错。");
		}

		// 删除订单超时未支付取消订单任务
		timingTaskDAO.clearTimeTaskByEntityIdAndTaskType(tOrder.getId(), 7);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		return responseDTO;
	}

	public ResponseDTO viewOrderForCustomer(Integer clientType, String ticket, String orderId) {
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

		TOrder tOrder = orderDAO.findOne(orderId);
		if (tOrder == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不存在！");
			return responseDTO;
		}
		if (!customerDTO.getCustomerId().equals(tOrder.getTCustomer().getId())) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不属于该客户！");
			return responseDTO;
		}

		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setOrderId(tOrder.getId());
		orderDTO.setOrderNum(tOrder.getForderNum());
		orderDTO.setOrderType(tOrder.getForderType());
//		orderDTO.setAppointment(tOrder.getFappointment());
//		orderDTO.setReturnReplace(tOrder.getFreturn());
//		orderDTO.setUsePreferential(tOrder.getFusePreferential());
//		orderDTO.setVerificationType(tOrder.getFverificationType());
		orderDTO.setGoodsId(tOrder.getTEvent().getId());
		orderDTO.setGoodsTitle(tOrder.getFeventTitle());
		TEvent tEvent = tOrder.getTEvent();
//		orderDTO.setGoodsImageUrl(fxlService.getImageUrl(tEvent.getFimage1(), false));
		TEventSession tEventSession = tOrder.getTEventSession();
//		orderDTO.setEventAddress(tEventSession.getFaddress());
//		orderDTO.setValidityStart(tEventSession.getFstartDate() != null
//				? DateFormatUtils.format(tEventSession.getFstartDate(), "yyyy年MM月dd日") : StringUtils.EMPTY);
//		orderDTO.setValidityEnd(tEventSession.getFendDate() != null
//				? DateFormatUtils.format(tEventSession.getFendDate(), "yyyy年MM月dd日") : StringUtils.EMPTY);
//		orderDTO.setSession(tOrder.getFsessionTitle());
//		orderDTO.setSessionTitle(tOrder.getFsessionTitle());
//		orderDTO.setSpec(tOrder.getFspecTitle());
//		orderDTO.setSpecTitle(tOrder.getFspecTitle());
//		orderDTO.setPrice(tOrder.getFprice());
//		orderDTO.setCount(tOrder.getFcount());
//		orderDTO.setPostage(tOrder.getFpostage());
//		orderDTO.setReceivableTotal(tOrder.getFreceivableTotal());
		orderDTO.setTotal(tOrder.getFtotal());
//		orderDTO.setAmount(tOrder.getFchangeAmount());
//		orderDTO.setChangeAmount(tOrder.getFchangeAmount());
//		orderDTO.setChangeAmountInstruction(tOrder.getFchangeAmountInstruction());
//		orderDTO.setCustomer(tOrder.getFcustomerName());
//		orderDTO.setCustomerPhone(tOrder.getFcustomerPhone());
		orderDTO.setStatus(tOrder.getFstatus());
		orderDTO.setStatusString(DictionaryUtil.getString(DictionaryUtil.OrderStatus, tOrder.getFstatus()));
//		orderDTO.setRemark(tOrder.getFremark());
		if (StringUtils.isNotBlank(tOrder.getFrecipient())) {
			OrderRecipientDTO recipientDTO = mapper.fromJson(tOrder.getFrecipient(), OrderRecipientDTO.class);
			if (recipientDTO != null) {
//				orderDTO.setRecipient(recipientDTO.getRecipient());
//				orderDTO.setPhone(recipientDTO.getPhone());
//				orderDTO.setAddress(recipientDTO.getAddress());
//				orderDTO.setSpecPerson(recipientDTO.getSpecPerson());
//				orderDTO.setInsuranceInfo(recipientDTO.getInsuranceInfo());
			}
		}

//		if (tOrder.getFstatus().intValue() == 20) {
//			// 商家编号作为加密的秘钥
//			String fsponsorNumber = tOrder.getFsponsorNumber();
//			// 生成手工核销编码
//			orderDTO.setVerificationCodeManual(Long.toHexString(Long.valueOf(orderDTO.getOrderNum())));
//			// 生成自动核销编码
//			StringBuilder allKey = new StringBuilder();
//			for (int i = 0; i < 3; i++) {
//				allKey.append(fsponsorNumber);
//			}
//			logger.info("allKey：" + allKey);
//			String orderNumAndSponsorNum = new StringBuilder().append(orderDTO.getOrderNum()).append("###")
//					.append(fsponsorNumber).toString();
//			logger.info("orderNumAndSponsorNum：" + orderNumAndSponsorNum);
//			String verificationCodeAuto = null;
//			try {
//				verificationCodeAuto = ByteArrayUtil.toHexString(Cryptos.aesEncrypt(orderNumAndSponsorNum.getBytes(),
//						allKey.substring(0, 16).toString().getBytes()));
//				logger.info("加密结果：" + verificationCodeAuto);
//				orderDTO.setVerificationCodeAuto(verificationCodeAuto);
//			} catch (Exception e) {
//				Map<String, String> map = new HashMap<String, String>();
//				map.put("orderId", tOrder.getId());
//				map.put("customerId", customerDTO.getCustomerId());
//				OutPutLogUtil.printLoggger(e, map, logger);
//				responseDTO.setSuccess(false);
//				responseDTO.setStatusCode(105);
//				responseDTO.setMsg("生成订单二维码时出错！");
//				return responseDTO;
//			}
//		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("orderInfo", orderDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void sendPayZeroSuccessSms(TOrder tOrder) {
		if (SmsUtil.isPayZeroSuccessSwitch()) {
			StringBuilder orderInfo = new StringBuilder();
			orderInfo.append("[").append(tOrder.getFeventTitle()).append("]").append("【订单号：")
					.append(tOrder.getForderNum()).append("】");

			Map<String, String> smsParamMap = Maps.newHashMap();
			smsParamMap.put("orderName", orderInfo.toString());
			smsParamMap.put("orderList", "【我的-我的报名】");

			// 发送订单支付成功通知短信
//			SmsResult smsResult = SmsUtil.sendSms(SmsUtil.PaySuccessSms, tOrder.getFcustomerPhone(), smsParamMap);
//
//			TSms tSms = new TSms();
//			tSms.setSendPhone(tOrder.getFcustomerPhone());
//			tSms.setSendTime(new Date());
//			tSms.setSmsContent(smsResult.getContent());
//			tSms.setSmsType(SmsUtil.PaySuccessSms);
//			tSms.setSendResponse(smsResult.getResponse());
//			if (smsResult.isSuccess()) {
//				tSms.setSendSuccess(1);
//			} else {
//				tSms.setSendSuccess(0);
//			}
//			smsDAO.save(tSms);
		}
	}

	public String wxPayConfirm(Notify notify) {

		String orderNum = notify.getOutTradeNo();
		TOrder tOrder = orderDAO.getByFstatusAndForderNum(10, orderNum);
		if (tOrder == null) {
			return "<xml><return_code>FAIL</return_code><return_msg>ORDER_NOT_EXIST</return_msg></xml>";
		}
		Date now = new Date();
		String returnStr = "<xml><return_code>SUCCESS</return_code></xml>";

		TWxPay tWxPay = wxPayDAO.getByOrderIdAndInOutAndStatus(tOrder.getId(), 1, 10);
		if (tWxPay == null) {
			tWxPay = new TWxPay();
			tWxPay.setFinOut(1);
			tWxPay.setFcreateTime(now);
			tWxPay.setForderId(tOrder.getId());
			tWxPay.setForderType(1);
			tWxPay.setTCustomer(tOrder.getTCustomer());
		}
		int status = 0;
		// isResultCodeSuccess()方法表示支付是否成功，是交易成功的标志
		if (notify.isResultCodeSuccess()) {
			int total = notify.getTotalFeeFen();
			if (total != 0) {
				BigDecimal payTotal = new BigDecimal(notify.getTotalFeeFen()).divide(new BigDecimal(100)).setScale(2,
						RoundingMode.HALF_UP);
				// 如果支付金额大于等于订单总价，表示是支付完成
				if (tOrder.getFtotal().compareTo(payTotal) <= 0) {
					// 支付成功，更改订单状态为20（已支付）
					status = 20;
					// 微信支付表的状态为已支付
					tWxPay.setFstatus(30);
				} else {
					// 支付金额小于订单金额，更改订单状态为15（部分支付）
					status = 15;
					// 微信支付表的状态为部分支付
					tWxPay.setFstatus(20);
				}
			} else {
				// TODO 支付零元的情况没有处理
			}
		} else {
			// 支付失败，更改订单状态为11（支付失败）
			status = 11;
			// 微信支付表的状态为支付失败
			tWxPay.setFstatus(90);
			returnStr = "<xml><return_code>FAIL</return_code><return_msg>ORDER_NOT_EXIST</return_msg></xml>";
		}
		fxlService.orderStatusChange(1, tOrder.getFcustomerName(), tOrder.getId(), null, tOrder.getFstatus(), status);
		orderDAO.updateOrderStatusAndPayTime(status, now, tOrder.getId());

		// 删除订单超时未支付取消订单任务
		timingTaskDAO.clearTimeTaskByEntityIdAndTaskType(tOrder.getId(), 7);
		timingTaskDAO.clearTimeTaskByEntityIdAndTaskType(tOrder.getId(), 18);
		// 下单成功付款之后，改变用户附加信息表中的下单金额
		// 添加线程任务修改客户附加信息表
		OrderUpdateCustomerInfoBean ouci = new OrderUpdateCustomerInfoBean();
		ouci.setCreateTime(now);
		ouci.setCustomerId(tOrder.getTCustomer().getId());
		ouci.setOrderId(tOrder.getId());
		ouci.setTotal(tOrder.getFtotal());
		ouci.setTaskType(2);
		AsynchronousTasksManager.put(ouci);

		try {
			// 发送支付成功微信模板消息
			String orderPushWeiXin = ConfigurationUtil
					.getPropertiesValue(ResponseConfigurationDict.RESPONSE_PROPERTIES_PAYMENTSUCCESSFUL);
			if (StringUtils.isNotBlank(notify.getOpenid())) {
				if (StringUtils.isNotBlank(orderPushWeiXin) && orderPushWeiXin.equals("1")) {
					wxService.pushPayMentSuccessfulWeChatTemplateMsg(tOrder, notify.getOpenid());
				}
			} else {
				/*
				 * Cache customerEentityCache =
				 * cacheManager.getCache(Constant.CustomerEentity); Element
				 * elementB =
				 * customerEentityCache.get(tOrder.getTCustomer().getId());
				 * CustomerDTO customerDTO = null; if (elementB != null) {
				 * customerDTO = (CustomerDTO) elementB.getObjectValue(); if
				 * (StringUtils.isNotBlank(orderPushWeiXin) &&
				 * orderPushWeiXin.equals("1") &&
				 * StringUtils.isNotBlank(customerDTO.getWxId())) {
				 * wxService.pushPayMentSuccessfulWeChatTemplateMsg(tOrder,
				 * customerDTO.getWxId()); } }
				 */
				CustomerDTO customerDTO = fxlService.getCustomerByCustomerId(tOrder.getTCustomer().getId());
				if (StringUtils.isNotBlank(customerDTO.getWxId()) && StringUtils.isNotBlank(orderPushWeiXin)
						&& orderPushWeiXin.equals("1")) {
					wxService.pushPayMentSuccessfulWeChatTemplateMsg(tOrder, customerDTO.getWxId());
				}
			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
		// 删除未支付取消前20分钟发送微信提醒消息
		timingTaskDAO.clearTimeTaskByEntityIdAndTaskType(tOrder.getId(), 18);

		if (SmsUtil.isPaySuccessSwitch()) {
			OrderSendSmsBean oss = new OrderSendSmsBean();
			oss.setCreateTime(now);
			oss.setCustomerId(tOrder.getTCustomer().getId());
			oss.setCustomerName(tOrder.getFcustomerName());
			oss.setCustomerPhone(tOrder.getFcustomerPhone());
			oss.setEventTitle(tOrder.getFeventTitle());
			oss.setOrderId(tOrder.getId());
			oss.setOrderNum(tOrder.getForderNum());
			oss.setTaskType(4);
			AsynchronousTasksManager.put(oss);
		}

		// 如果是砍一砍活动，则修改用户砍一砍状态
		if (tOrder.getFsource() != null && tOrder.getFsource().intValue() == 20) {
			customerBargainingDAO.updateStatusPay(tOrder.getId(), 40, tOrder.getFpayTime());
		}

		tWxPay.setFconfirmPayTime(notify.getDateProperty("time_end"));
		tWxPay.setFupdateTime(now);
		if (StringUtils.isNotBlank(notify.getProperty("fee_type"))) {
			tWxPay.setFcurrencyType(notify.getProperty("fee_type"));
		}
		tWxPay.setFcpRequestInfo(notify.getReturnMsg() != null ? notify.getReturnMsg().getReturnMsg()
				: notify.getProperties().toString());
		tWxPay.setFcpResponseInfo(returnStr);
		tWxPay.setFtransactionId(notify.getTransactionId());
		wxPayDAO.save(tWxPay);
		return returnStr;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getOrderListForCustomer(Integer clientType, String ticket, Integer status, Integer pageSize,
			Integer offset) {

		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (status == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(1102);
			responseDTO.setMsg("status参数不能为空，请检查status的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}

		List<OrderDTO> orderList = Lists.newArrayList();

		StringBuilder hql = new StringBuilder();
		hql.append(
				"select t.id as id, t.TEvent.id as eventId, t.feventTitle as feventTitle, t.TEventSession.id as sessionId, t.fsessionTitle as fsessionTitle, t.fspecTitle as fspecTitle, t.forderNum as forderNum, t.fprice as fprice, t.fcount as fcount, t.fpostage as fpostage, t.freceivableTotal as freceivableTotal, t.ftotal as ftotal, t.fchangeAmount as fchangeAmount, t.fchangeAmountInstruction as fchangeAmountInstruction, t.fsponsorNumber as fsponsorNumber, t.forderType as forderType, t.fappointment as fappointment, t.freturn as freturn, t.fstatus as fstatus, t.fusePreferential as fusePreferential, t.fverificationType as fverificationType, t.frecipient as frecipient from TOrder t where t.TCustomer.id = :customerId");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("customerId", customerDTO.getCustomerId());
		if (status.intValue() == 0) {
			hql.append(" and t.fstatus < 900 order by t.fcreateTime desc");
		} else if (status.intValue() == 100) {
			hql.append(" and (t.fstatus between 100 and 199) order by t.frefundTime desc");
		} else if (status.intValue() == 10) {
			hql.append(" and (t.fstatus between 10 and 19) order by t.fcreateTime desc");
		} else if (status.intValue() == 20) {
			hql.append(" and (t.fstatus between 20 and 59) order by t.fcreateTime desc");
		} else if (status.intValue() == 60) {
			hql.append(" and t.fstatus = 60 order by t.fverificationTime desc");
		} else if (status.intValue() == 70) {
			hql.append(" and t.fstatus = 70 order by t.fverificationTime desc");
		} else {
			hql.append(" and t.fstatus = :status order by t.fcreateTime desc");
			hqlMap.put("status", status);
		}

		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();

		OrderDTO orderDTO = null;
		int statusValue = 0;
		for (Map<String, Object> amap : list) {
			orderDTO = new OrderDTO();
			if (amap.get("id") != null && StringUtils.isNotBlank(amap.get("id").toString())) {
				orderDTO.setOrderId(amap.get("id").toString());
			}
			if (amap.get("forderNum") != null && StringUtils.isNotBlank(amap.get("forderNum").toString())) {
				orderDTO.setOrderNum(amap.get("forderNum").toString());

				if (amap.get("fstatus") != null && StringUtils.isNotBlank(amap.get("fstatus").toString())) {
					statusValue = ((Integer) amap.get("fstatus")).intValue();
					orderDTO.setStatus(statusValue);
					if (statusValue == 109) {
						orderDTO.setStatusString("超时取消");
					} else {
						orderDTO.setStatusString(DictionaryUtil.getString(DictionaryUtil.OrderStatus, statusValue));
					}

					if (statusValue == 20) {
//						if (amap.get("sessionId") != null && StringUtils.isNotBlank(amap.get("sessionId").toString())) {
//							TEventSession tEventSession = eventSessionDAO.getOne(amap.get("sessionId").toString());
////							orderDTO.setEventAddress(tEventSession.getFaddress());
//							orderDTO.setValidityStart(tEventSession.getFstartDate() != null
//									? DateFormatUtils.format(tEventSession.getFstartDate(), "yyyy-MM-dd")
//									: StringUtils.EMPTY);
//							orderDTO.setValidityEnd(tEventSession.getFendDate() != null
//									? DateFormatUtils.format(tEventSession.getFendDate(), "yyyy-MM-dd")
//									: StringUtils.EMPTY);
//
//						}
						if (amap.get("frecipient") != null
								&& StringUtils.isNotBlank(amap.get("frecipient").toString())) {
							OrderRecipientDTO recipientDTO = mapper.fromJson(amap.get("frecipient").toString(),
									OrderRecipientDTO.class);
							if (recipientDTO != null) {
//								orderDTO.setRecipient(recipientDTO.getRecipient());
//								orderDTO.setPhone(recipientDTO.getPhone());
//								orderDTO.setAddress(recipientDTO.getAddress());
//								orderDTO.setSpecPerson(recipientDTO.getSpecPerson());
//								orderDTO.setInsuranceInfo(recipientDTO.getInsuranceInfo());
							}
						}
//						// 商家编号作为加密的秘钥
//						String fsponsorNumber = amap.get("fsponsorNumber").toString();
//						// 生成手工核销编码
//						orderDTO.setVerificationCodeManual(Long.toHexString(Long.valueOf(orderDTO.getOrderNum())));
//						// 生成自动核销编码
//						StringBuilder allKey = new StringBuilder();
//						for (int i = 0; i < 3; i++) {
//							allKey.append(fsponsorNumber);
//						}
//						logger.info("allKey：" + allKey);
//						String orderNumAndSponsorNum = new StringBuilder().append(orderDTO.getOrderNum()).append("###")
//								.append(fsponsorNumber).toString();
//						logger.info("orderNumAndSponsorNum：" + orderNumAndSponsorNum);
//						String verificationCodeAuto = null;
//						try {
//							verificationCodeAuto = ByteArrayUtil.toHexString(Cryptos.aesEncrypt(
//									orderNumAndSponsorNum.getBytes(), allKey.substring(0, 16).toString().getBytes()));
//							logger.info("加密结果：" + verificationCodeAuto);
//							orderDTO.setVerificationCodeAuto(verificationCodeAuto);
//						} catch (Exception e) {
//							Map<String, String> map = new HashMap<String, String>();
//							map.put("customerId", customerDTO.getCustomerId());
//							OutPutLogUtil.printLoggger(e, map, logger);
//							responseDTO.setSuccess(false);
//							responseDTO.setStatusCode(105);
//							responseDTO.setMsg("订单二维码错误！");
//							return responseDTO;
//						}
					}
				}
			}
			if (amap.get("eventId") != null && StringUtils.isNotBlank(amap.get("eventId").toString())) {
				TEvent tEvent = eventDAO.getOne(amap.get("eventId").toString());
				orderDTO.setGoodsId(tEvent.getId());
//				if (StringUtils.isNotBlank(tEvent.getFimage1())) {
//					orderDTO.setGoodsImageUrl(fxlService.getImageUrl(tEvent.getFimage1(), false));
//				} else {
//					orderDTO.setGoodsImageUrl(new StringBuilder().append(PropertiesUtil.getProperty("fileServerUrl"))
//							.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPic.jpg").toString());
//				}
			}
			if (amap.get("feventTitle") != null && StringUtils.isNotBlank(amap.get("feventTitle").toString())) {
				orderDTO.setGoodsTitle(amap.get("feventTitle").toString());
			}
//			if (amap.get("fsessionTitle") != null && StringUtils.isNotBlank(amap.get("fsessionTitle").toString())) {
//				orderDTO.setSession(amap.get("fsessionTitle").toString());
//				orderDTO.setSessionTitle(amap.get("fsessionTitle").toString());
//			}
//			if (amap.get("fspecTitle") != null && StringUtils.isNotBlank(amap.get("fspecTitle").toString())) {
//				orderDTO.setSpec(amap.get("fspecTitle").toString());
//				orderDTO.setSpecTitle(amap.get("fspecTitle").toString());
//			}
//			if (amap.get("fprice") != null && StringUtils.isNotBlank(amap.get("fprice").toString())) {
//				orderDTO.setPrice((BigDecimal) amap.get("fprice"));
//			}
//			if (amap.get("fcount") != null && StringUtils.isNotBlank(amap.get("fcount").toString())) {
//				orderDTO.setCount((Integer) amap.get("fcount"));
//			}
//			if (amap.get("fpostage") != null && StringUtils.isNotBlank(amap.get("fpostage").toString())) {
//				orderDTO.setPostage((BigDecimal) amap.get("fpostage"));
//			}
//			if (amap.get("freceivableTotal") != null
//					&& StringUtils.isNotBlank(amap.get("freceivableTotal").toString())) {
//				orderDTO.setReceivableTotal((BigDecimal) amap.get("freceivableTotal"));
//			}
			if (amap.get("ftotal") != null && StringUtils.isNotBlank(amap.get("ftotal").toString())) {
				orderDTO.setTotal((BigDecimal) amap.get("ftotal"));
			}
//			if (amap.get("fchangeAmount") != null && StringUtils.isNotBlank(amap.get("fchangeAmount").toString())) {
//				orderDTO.setChangeAmount((BigDecimal) amap.get("fchangeAmount"));
//			}
//			if (amap.get("fchangeAmountInstruction") != null
//					&& StringUtils.isNotBlank(amap.get("fchangeAmountInstruction").toString())) {
//				orderDTO.setChangeAmountInstruction(amap.get("fchangeAmountInstruction").toString());
//			}
//			if (amap.get("forderType") != null && StringUtils.isNotBlank(amap.get("forderType").toString())) {
//				orderDTO.setOrderType((Integer) amap.get("forderType"));
//			}
//			if (amap.get("fappointment") != null && StringUtils.isNotBlank(amap.get("fappointment").toString())) {
//				orderDTO.setAppointment((Integer) amap.get("fappointment"));
//			}
//			if (amap.get("freturn") != null && StringUtils.isNotBlank(amap.get("freturn").toString())) {
//				orderDTO.setReturnReplace((Integer) amap.get("freturn"));
//			}
//			if (amap.get("fusePreferential") != null
//					&& StringUtils.isNotBlank(amap.get("fusePreferential").toString())) {
//				orderDTO.setReturnReplace((Integer) amap.get("fusePreferential"));
//			}
//			if (amap.get("fverificationType") != null
//					&& StringUtils.isNotBlank(amap.get("fverificationType").toString())) {
//				orderDTO.setVerificationType((Integer) amap.get("fverificationType"));
//			}
			orderList.add(orderDTO);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("orderList", orderList);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getOrderVerificationCode(Integer clientType, String ticket, String orderId) {
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

		TOrder tOrder = orderDAO.findOne(orderId);
		if (tOrder == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不存在！");
			return responseDTO;
		}
		if (!customerDTO.getCustomerId().equals(tOrder.getTCustomer().getId())) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不属于该客户！");
			return responseDTO;
		}
		if (tOrder.getFstatus().intValue() != 20) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(103);
			responseDTO.setMsg("该订单不是待出行状态，无法获取验证码信息！");
			return responseDTO;
		}

		// 商家编号作为加密的秘钥
		String fsponsorNumber = tOrder.getFsponsorNumber();
		// 生成手工核销编码
		String verificationCodeManual = Long.toHexString(Long.valueOf(tOrder.getForderNum()));
		// 生成自动核销编码
		StringBuilder allKey = new StringBuilder();
		for (int i = 0; i < 3; i++) {
			allKey.append(fsponsorNumber);
		}
		logger.info("allKey：" + allKey);
		String orderNumAndSponsorNum = new StringBuilder().append(tOrder.getForderNum()).append("###")
				.append(fsponsorNumber).toString();
		logger.info("orderNumAndSponsorNum：" + orderNumAndSponsorNum);
		String verificationCodeAuto = null;
		try {
			verificationCodeAuto = ByteArrayUtil.toHexString(Cryptos.aesEncrypt(orderNumAndSponsorNum.getBytes(),
					allKey.substring(0, 16).toString().getBytes()));
			logger.info("加密结果：" + verificationCodeAuto);
		} catch (Exception e) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("customerId", customerDTO.getCustomerId());
			map.put("orderId", orderId);
			OutPutLogUtil.printLoggger(e, map, logger);
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(105);
			responseDTO.setMsg("订单二维码生成时出错！");
			return responseDTO;
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("verificationCodeAuto", verificationCodeAuto);
		returnData.put("verificationCodeManual", verificationCodeManual);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public ResponseDTO verificationConfirm(String ticket, Integer verificationType, String orderId, Integer clientType,
			String clientGPS, String clientDevice) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (verificationType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("verificationType参数不能为空，请检查verificationType的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(orderId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("orderId参数不能为空，请检查orderId的传递参数值！");
			return responseDTO;
		}

		TCustomer tCustomer = customerDAO.getByFticketAndFtype(ticket, 10);
		if (tCustomer == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(100);
			responseDTO.setMsg("您提供的Ticket信息有误，请重新登录零到壹！");
			return responseDTO;
		} else if (tCustomer.getFstatus().equals(10)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(101);
			responseDTO.setMsg("您的账号处于冻结状态，请联系零到壹客服人员！");
			return responseDTO;
		} else if (tCustomer.getFstatus().equals(999)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("您的账号处于作废状态，请联系零到壹客服人员！");
			return responseDTO;
		}

		TOrder tOrder = orderDAO.findOne(orderId);
		if (tOrder == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(104);
			responseDTO.setMsg("本订单不是有效订单！");
			return responseDTO;
		}
		if (!tOrder.getTSponsor().getId().equals(tCustomer.getTSponsor().getId())) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(105);
			responseDTO.setMsg("本订单不属于本商家！");
			return responseDTO;
		}
		if (tOrder.getFlockFlag().intValue() == 1) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(106);
			responseDTO.setMsg("您的订单已锁定状态，暂无法进行操作！");
			return responseDTO;
		}
		if (tOrder.getFstatus().intValue() >= 60) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(107);
			responseDTO.setMsg("本订单是已核销过订单！");
			return responseDTO;
		}
		if (tOrder.getFstatus().intValue() != 20) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(108);
			responseDTO.setMsg("本订单不是可核销订单！");
			return responseDTO;
		}
		fxlService.orderStatusChange(2, tCustomer.getFname(), tOrder.getId(), null, tOrder.getFstatus(), 60);
		Date now = new Date();
		orderDAO.updateOrderStatusAndFverificationTime(orderId, 60, now);
		TSponsor tSponsor = tCustomer.getTSponsor();
		TEvent event = eventDAO.findOne(tOrder.getTEvent().getId());

		TOrderVerification tOrderVerification = new TOrderVerification();
		tOrderVerification.setTCustomerByFcustomerId(tOrder.getTCustomer());
		tOrderVerification.setTCustomerByFoperator(tCustomer);
		tOrderVerification.setTOrder(tOrder);
		tOrderVerification.setTSponsor(tSponsor);
		tOrderVerification.setFcreateTime(now);
		tOrderVerification.setFclientOperate(verificationType);
		tOrderVerification.setForderOriginalAmount(tOrder.getFtotal());
		tOrderVerification.setFsettlementType(event.getFsettlementType());
		BigDecimal originalOrderAmount = tOrder.getFtotal();
		if (event.getFsettlementType().intValue() == 20) {
			tOrderVerification.setForderRate(tSponsor.getFrate());
			BigDecimal rate = tSponsor.getFrate() != null ? tSponsor.getFrate() : BigDecimal.ZERO;
			BigDecimal res = BigDecimal.ONE.subtract(rate, new MathContext(2, RoundingMode.HALF_UP));
			BigDecimal orderAmount = originalOrderAmount.multiply(res, new MathContext(2, RoundingMode.DOWN));
			tOrderVerification.setForderAmount(orderAmount);
		} else if (event.getFsettlementType().intValue() == 30) {
			TEventSpec tEventSpec = eventSpecDAO.findOne(tOrder.getTEventSpec().getId());
			tOrderVerification.setForderAmount(tEventSpec.getFsettlementPrice());
		}
		if (clientType != null) {
			tOrderVerification.setFclientType(clientType);
		}
		if (StringUtils.isNotBlank(clientGPS)) {
			tOrderVerification.setFclientGps(clientGPS);
		}
		if (StringUtils.isNotBlank(clientDevice)) {
			tOrderVerification.setFclientDevice(clientDevice);
		}

		orderVerificationDAO.save(tOrderVerification);

		if (SmsUtil.isVerificationSuccessSwitch()) {
			StringBuilder orderInfo = new StringBuilder();
			orderInfo.append("[").append(tOrder.getFeventTitle()).append("]").append("【订单号：")
					.append(tOrder.getForderNum()).append("】");

			Map<String, String> smsParamMap = Maps.newHashMap();
			smsParamMap.put("orderName", orderInfo.toString());
			smsParamMap.put("platform", "【零到壹】");
			smsParamMap.put("customerServiceTel", "010-53689210");

			// 发送订单核销成功通知
//			SmsResult smsResult = SmsUtil.sendSms(SmsUtil.VerificationSuccessSms, tOrder.getFcustomerPhone(),
//					smsParamMap);
//
//			TSms tSms = new TSms();
//			tSms.setSendPhone(tOrder.getFcustomerPhone());
//			tSms.setSendTime(new Date());
//			tSms.setSmsContent(smsResult.getContent());
//			tSms.setSmsType(SmsUtil.VerificationSuccessSms);
//			tSms.setSendResponse(smsResult.getResponse());
//			if (smsResult.isSuccess()) {
//				tSms.setSendSuccess(1);
//			} else {
//				tSms.setSendSuccess(0);
//			}
//			smsDAO.save(tSms);
		}
		// 核销确认之后添加定时任务发送订单评价提醒模板消息
		String orderPushWeiXin = ConfigurationUtil
				.getPropertiesValue(ResponseConfigurationDict.RESPONSE_PROPERTIES_ORDEREVALUATION);
		if (StringUtils.isNotBlank(orderPushWeiXin) && orderPushWeiXin.equals("1")) {
			TTimingTask timingTask = new TTimingTask();
			timingTask.setEntityId(tOrder.getId());
			timingTask.setTaskTime((DateUtils.addHours(now, 2)).getTime());
			timingTask.setTaskType(17);
			timingTaskDAO.save(timingTask);
		}
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("订单核销成功！");
		return responseDTO;
	}

	

	/**
	 * 获取用户订单带出行，待支付，待评价总数 service
	 * 
	 * @param ticket
	 *            票
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getOrderNumByStatus(Integer clientType, String ticket) {
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
		TCustomer customer = customerDAO.findOne(customerDTO.getCustomerId());
		// TODO Status：10（待支付），20（带出行），60（待评价）,70(已完成)
		Integer toBePaidNum = orderDAO.countOrderNumByStatus(customerDTO.getCustomerId(), 10);
		Integer toBeTravelNum = orderDAO.countOrderNumByStatus(customerDTO.getCustomerId(), 20);
		Integer toBeEvaluatedNum = orderDAO.countOrderNumByStatus(customerDTO.getCustomerId(), 60);
		Integer completedNum = orderDAO.countOrderNumByStatus(customerDTO.getCustomerId(), 70);
		Integer couponNum = couponService.couponNumByStatus(customer);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("toBePaidNum", toBePaidNum);
		returnData.put("toBeTravelNum", toBeTravelNum);
		returnData.put("toBeEvaluatedNum", toBeEvaluatedNum);
		returnData.put("completedNum", completedNum);
		returnData.put("couponNum", couponNum);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * 获取用户可用的优惠券Service
	 * 
	 * @param ticket
	 *            票
	 * @param orderId
	 *            订单ID
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getCouponListByOrder(Integer clientType, String ticket, String orderId) {

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

		TOrder tOrder = orderDAO.findOne(orderId);
		if (tOrder == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不存在！");
			return responseDTO;
		}

		if (tOrder.getFlockFlag().intValue() == 1) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(103);
			responseDTO.setMsg("您的订单已锁定状态，暂无法进行操作！");
			return responseDTO;
		}
		if (tOrder.getFstatus().intValue() != 10) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(104);
			responseDTO.setMsg("您的订单不是未支付状态！");
			return responseDTO;
		}

		if (!customerDTO.getCustomerId().equals(tOrder.getTCustomer().getId())) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不属于该客户！");
			return responseDTO;
		}

		StringBuilder hql = new StringBuilder();
		hql.append(
				"SELECT t.id as couponDeliveryId, f.ftitle as ftitle, f.fuseStartTime as fuseStartTime, f.fuseEndTime as fuseEndTime, c.fobjectId as objectId, f.fdiscount as fdiscount, f.flimitation as flimitation, f.famount as famount, c.fuseType as useType ")
				.append(" from TCoupon f inner join f.TCouponDeliveries t left join f.TCouponObjects c where t.TCustomer.id = :customerId AND t.fstatus < :status AND f.flimitation <= :total ");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("customerId", customerDTO.getCustomerId());
		hqlMap.put("status", 20);
		hqlMap.put("total", tOrder.getFreceivableTotal());

		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);
		List<CouponDTO> dataList = Lists.newArrayList();
		CouponDTO couponDTO = null;
		Date date = null;
		for (Map<String, Object> amap : list) {
			if (amap.get("useType") != null && StringUtils.isNotBlank(amap.get("useType").toString())) {
				int useType = (Integer) amap.get("useType");
				couponDTO = new CouponDTO();
				boolean isAdd = false;
				String objectId = "";
				if (amap.get("objectId") != null && StringUtils.isNotBlank(amap.get("objectId").toString())) {
					objectId = amap.get("objectId").toString();
				} else if (useType != 10) {
					break;
				}
				if (useType == 10) {
					isAdd = true;
				} else if (useType == 20) {
					if (tOrder.getTSponsor().getId().equals(objectId)) {
						isAdd = true;
					}
				} else if (useType == 30) {
					TEvent event = tOrder.getTEvent();
					if (event.getFtypeA().toString().equals(objectId)) {
						isAdd = true;
					}
				} else {
					if (tOrder.getTEvent().getId().equals(objectId)) {
						isAdd = true;
					}
				}
				if (isAdd) {
					if (amap.get("couponDeliveryId") != null
							&& StringUtils.isNotBlank(amap.get("couponDeliveryId").toString())) {
						couponDTO.setCouponDeliveryId(amap.get("couponDeliveryId").toString());
					}

					if (amap.get("ftitle") != null && StringUtils.isNotBlank(amap.get("ftitle").toString())) {
						couponDTO.setTitle(amap.get("ftitle").toString());
					}
					if (amap.get("fuseStartTime") != null
							&& StringUtils.isNotBlank(amap.get("fuseStartTime").toString())) {
						date = (Date) amap.get("fuseStartTime");
						couponDTO.setUseStartTime(DateFormatUtils.format(date, "yyyy-MM-dd"));
					}
					if (amap.get("fuseEndTime") != null && StringUtils.isNotBlank(amap.get("fuseEndTime").toString())) {
						date = (Date) amap.get("fuseEndTime");
						couponDTO.setUseEndTime(DateFormatUtils.format(date, "yyyy-MM-dd"));
					}
					if (amap.get("flimitation") != null && StringUtils.isNotBlank(amap.get("flimitation").toString())) {
						couponDTO.setLimitation((BigDecimal) amap.get("flimitation"));
						couponDTO.setLimitationInfo("满" + amap.get("flimitation") + "元减" + amap.get("famount") + "元");
					} else {
						couponDTO.setLimitationInfo("直减" + amap.get("famount") + "元");
					}
					if (amap.get("fdiscount") != null && StringUtils.isNotBlank(amap.get("fdiscount").toString())) {
						BigDecimal discount = ((BigDecimal) amap.get("fdiscount")).multiply(new BigDecimal(10));
						couponDTO.setDiscount(discount);
					}
					if (amap.get("famount") != null && StringUtils.isNotBlank(amap.get("famount").toString())) {
						couponDTO.setAmount((BigDecimal) amap.get("famount"));
					}
					if (amap.get("status") != null && StringUtils.isNotBlank(amap.get("status").toString())) {
						couponDTO.setStatus((Integer) amap.get("status"));
					}
					couponDTO.setLimitationClient("仅限移动端下单使用");
					/*
					 * if (amap.get("fuseType") != null &&
					 * StringUtils.isNotBlank(amap.get("fuseType").toString()))
					 * {
					 * 
					 * }
					 */
					dataList.add(couponDTO);
				}
			}
		}
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("userCouponList", dataList);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getCouponListByEvent(Integer clientType, String ticket, String eventId, BigDecimal total) {

		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}

		if (total == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(205);
			responseDTO.setMsg("total参数不能为空，请检查total的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		TEvent tEvent = eventDAO.findOne(eventId);
		if (tEvent == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("您输入的eventId参数有误，eventId=“" + eventId + "”的订单不存在！");
			return responseDTO;
		}

		if (tEvent.getFstatus().intValue() != 20) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(103);
			responseDTO.setMsg("该活动已下架！");
			return responseDTO;
		}

		StringBuilder hql = new StringBuilder();
		hql.append(
				"SELECT t.id as couponDeliveryId, f.ftitle as ftitle, f.fuseStartTime as fuseStartTime, f.fuseEndTime as fuseEndTime, c.fobjectId as objectId, f.fdiscount as fdiscount, f.flimitation as flimitation, f.famount as famount, c.fuseType as useType ")
				.append(" from TCoupon f inner join f.TCouponDeliveries t left join f.TCouponObjects c where t.TCustomer.id = :customerId AND t.fstatus < :status");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("customerId", customerDTO.getCustomerId());
		hqlMap.put("status", 20);

		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);
		List<CouponDTO> dataList = Lists.newArrayList();
		CouponDTO couponDTO = null;
		Date date = null;
		for (Map<String, Object> amap : list) {
			if (amap.get("useType") != null && StringUtils.isNotBlank(amap.get("useType").toString())) {
				int useType = (Integer) amap.get("useType");
				couponDTO = new CouponDTO();
				boolean isAdd = false;
				String objectId = "";
				if (amap.get("objectId") != null && StringUtils.isNotBlank(amap.get("objectId").toString())) {
					objectId = amap.get("objectId").toString();
				} else if (useType != 10) {
					break;
				}
				if (amap.get("flimitation") != null && StringUtils.isNotBlank(amap.get("flimitation").toString())) {
					BigDecimal limitation = (BigDecimal) amap.get("flimitation");
					if (total.compareTo(limitation) > -1) {
						if (useType == 10) {
							isAdd = true;
						} else if (useType == 20) {
							if (tEvent.getTSponsor().getId().equals(objectId)) {
								isAdd = true;
							}
						} else if (useType == 30) {

							if (tEvent.getFtypeA().toString().equals(objectId)) {
								isAdd = true;
							}
						} else {
							if (tEvent.getId().equals(objectId)) {
								isAdd = true;
							}
						}
					}
				}
				if (isAdd) {
					if (amap.get("couponDeliveryId") != null
							&& StringUtils.isNotBlank(amap.get("couponDeliveryId").toString())) {
						couponDTO.setCouponDeliveryId(amap.get("couponDeliveryId").toString());
					}

					if (amap.get("ftitle") != null && StringUtils.isNotBlank(amap.get("ftitle").toString())) {
						couponDTO.setTitle(amap.get("ftitle").toString());
					}
					if (amap.get("fuseStartTime") != null
							&& StringUtils.isNotBlank(amap.get("fuseStartTime").toString())) {
						date = (Date) amap.get("fuseStartTime");
						couponDTO.setUseStartTime(DateFormatUtils.format(date, "yyyy-MM-dd"));
					}
					if (amap.get("fuseEndTime") != null && StringUtils.isNotBlank(amap.get("fuseEndTime").toString())) {
						date = (Date) amap.get("fuseEndTime");
						couponDTO.setUseEndTime(DateFormatUtils.format(date, "yyyy-MM-dd"));
					}
					if (amap.get("flimitation") != null && StringUtils.isNotBlank(amap.get("flimitation").toString())) {
						couponDTO.setLimitation((BigDecimal) amap.get("flimitation"));
						couponDTO.setLimitationInfo("满" + amap.get("flimitation") + "元减" + amap.get("famount") + "元");
					} else {
						couponDTO.setLimitationInfo("直减" + amap.get("famount") + "元");
					}
					if (amap.get("fdiscount") != null && StringUtils.isNotBlank(amap.get("fdiscount").toString())) {
						BigDecimal discount = ((BigDecimal) amap.get("fdiscount")).multiply(new BigDecimal(10));
						couponDTO.setDiscount(discount);
					}
					if (amap.get("famount") != null && StringUtils.isNotBlank(amap.get("famount").toString())) {
						couponDTO.setAmount((BigDecimal) amap.get("famount"));
					}
					if (amap.get("status") != null && StringUtils.isNotBlank(amap.get("status").toString())) {
						couponDTO.setStatus((Integer) amap.get("status"));
					}
					couponDTO.setLimitationClient("仅限移动端下单使用");
					/*
					 * if (amap.get("fuseType") != null &&
					 * StringUtils.isNotBlank(amap.get("fuseType").toString()))
					 * {
					 * 
					 * }
					 */
					dataList.add(couponDTO);
				}
			}
		}
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("userCouponList", dataList);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * 用户手动核销订单
	 * 
	 * @param ticket
	 * @param verificationType
	 * @param orderId
	 * @param clientType
	 * @param clientGPS
	 * @param clientDevice
	 * @return
	 */
	public ResponseDTO verificationConfirmCustomer(String ticket, Integer verificationType, String orderId,
			Integer clientType, String clientGPS, String clientDevice, String ip) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (verificationType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("verificationType参数不能为空，请检查verificationType的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(orderId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("orderId参数不能为空，请检查orderId的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(clientGPS)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(204);
			responseDTO.setMsg("clientGPS参数不能为空，请检查clientGPS的传递参数值！");
			return responseDTO;
		}

		TCustomer tCustomer = customerDAO.getByFticketAndFtype(ticket, 10);
		if (tCustomer == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(100);
			responseDTO.setMsg("您提供的Ticket信息有误，请重新登录零到壹！");
			return responseDTO;
		} else if (tCustomer.getFstatus().equals(10)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(101);
			responseDTO.setMsg("您的账号处于冻结状态，请联系零到壹客服人员！");
			return responseDTO;
		} else if (tCustomer.getFstatus().equals(999)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("您的账号处于作废状态，请联系零到壹客服人员！");
			return responseDTO;
		}

		TOrder tOrder = orderDAO.findOne(orderId);
		if (tOrder == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(104);
			responseDTO.setMsg("本订单不是有效订单！");
			return responseDTO;
		}
		if (!tOrder.getTSponsor().getId().equals(tCustomer.getTSponsor().getId())) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(105);
			responseDTO.setMsg("本订单不属于本商家！");
			return responseDTO;
		}
		if (tOrder.getFlockFlag().intValue() == 1) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(106);
			responseDTO.setMsg("您的订单已锁定状态，暂无法进行操作！");
			return responseDTO;
		}
		if (tOrder.getFstatus().intValue() == 60) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(107);
			responseDTO.setMsg("本订单是已核销过订单！");
			return responseDTO;
		}
		if (tOrder.getFstatus().intValue() != 20) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(108);
			responseDTO.setMsg("本订单不是可核销订单！");
			return responseDTO;
		}
		fxlService.orderStatusChange(2, tCustomer.getFname(), tOrder.getId(), null, tOrder.getFstatus(), 60);
		Date now = new Date();
		orderDAO.updateOrderStatusAndFverificationTime(orderId, 60, now);
		TSponsor tSponsor = tCustomer.getTSponsor();

		TOrderVerification tOrderVerification = new TOrderVerification();
		tOrderVerification.setTCustomerByFcustomerId(tOrder.getTCustomer());
		tOrderVerification.setTCustomerByFoperator(tCustomer);
		tOrderVerification.setTOrder(tOrder);
		tOrderVerification.setTSponsor(tSponsor);
		tOrderVerification.setFcreateTime(now);
		tOrderVerification.setFclientOperate(verificationType);
		tOrderVerification.setForderOriginalAmount(tOrder.getFtotal());
		tOrderVerification.setForderRate(tSponsor.getFrate());
		BigDecimal originalOrderAmount = tOrder.getFtotal();
		BigDecimal rate = tSponsor.getFrate() != null ? tSponsor.getFrate() : BigDecimal.ZERO;
		BigDecimal res = BigDecimal.ONE.subtract(rate, new MathContext(2, RoundingMode.HALF_UP));
		BigDecimal orderAmount = originalOrderAmount.multiply(res, new MathContext(2, RoundingMode.DOWN));
		tOrderVerification.setForderAmount(orderAmount);
		if (clientType != null) {
			tOrderVerification.setFclientType(clientType);
		}

		if (StringUtils.isNotBlank(clientDevice)) {
			tOrderVerification.setFclientDevice(clientDevice);
		}
		// 判断地址是否传入
		boolean gpsIsError = false;
		if (StringUtils.isBlank(clientGPS)) {
			gpsIsError = true;
		} else {

			if (!clientGPS.contains(",") || clientGPS.contains("E")) {
				gpsIsError = true;
			}
			String[] gpsa = StringUtils.split(clientGPS, ',');
			if (gpsa.length != 2) {
				gpsIsError = true;
			} else if (gpsa.length == 2) {
				if (!NumberUtils.isCreatable(gpsa[0]) || !NumberUtils.isCreatable(gpsa[1])) {
					gpsIsError = true;
				}
			} else {
				gpsIsError = true;
			}
		}
		// 如果传入的gps格式信息错误，将使用百度地图IP地址定位方式
		if (gpsIsError) {
			clientGPS = BmapUtil.getGpsByIp(ip);
		}
		tOrderVerification.setFclientGps(clientGPS);
		orderVerificationDAO.save(tOrderVerification);

		if (SmsUtil.isVerificationSuccessSwitch()) {
			StringBuilder orderInfo = new StringBuilder();
			orderInfo.append("[").append(tOrder.getFeventTitle()).append("]").append("【订单号：")
					.append(tOrder.getForderNum()).append("】");

			Map<String, String> smsParamMap = Maps.newHashMap();
			smsParamMap.put("orderName", orderInfo.toString());
			smsParamMap.put("platform", "【零到壹】");
			smsParamMap.put("customerServiceTel", "010-53689210");

			// 发送订单核销成功通知
//			SmsResult smsResult = SmsUtil.sendSms(SmsUtil.VerificationSuccessSms, tOrder.getFcustomerPhone(),
//					smsParamMap);
//
//			TSms tSms = new TSms();
//			tSms.setSendPhone(tOrder.getFcustomerPhone());
//			tSms.setSendTime(new Date());
//			tSms.setSmsContent(smsResult.getContent());
//			tSms.setSmsType(SmsUtil.VerificationSuccessSms);
//			tSms.setSendResponse(smsResult.getResponse());
//			if (smsResult.isSuccess()) {
//				tSms.setSendSuccess(1);
//			} else {
//				tSms.setSendSuccess(0);
//			}
//			smsDAO.save(tSms);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("订单核销成功！");
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getOrderCodeForAward(Integer clientType, String customerId, String orderId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(customerId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("customerId参数不能为空，请检查customerId的传递参数值！");
			return responseDTO;
		}
		TCustomer customer = customerDAO.findOne(customerId);
		if (customer == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("customerId参数不能为空，请检查customerId的传递参数值！");
			return responseDTO;
		}

		TOrder tOrder = orderDAO.findOne(orderId);
		if (tOrder == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不存在！");
			return responseDTO;
		}
		if (!customer.getId().equals(tOrder.getTCustomer().getId())) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不属于该客户！");
			return responseDTO;
		}

		// 商家编号作为加密的秘钥
		String fsponsorNumber = tOrder.getFsponsorNumber();
		// 生成手工核销编码
		String verificationCodeManual = Long.toHexString(Long.valueOf(tOrder.getForderNum()));
		// 生成自动核销编码
		StringBuilder allKey = new StringBuilder();
		for (int i = 0; i < 3; i++) {
			allKey.append(fsponsorNumber);
		}
		logger.info("allKey：" + allKey);
		String orderNumAndSponsorNum = new StringBuilder().append(tOrder.getForderNum()).append("###")
				.append(fsponsorNumber).toString();
		logger.info("orderNumAndSponsorNum：" + orderNumAndSponsorNum);
		String verificationCodeAuto = null;
		try {
			verificationCodeAuto = ByteArrayUtil.toHexString(Cryptos.aesEncrypt(orderNumAndSponsorNum.getBytes(),
					allKey.substring(0, 16).toString().getBytes()));
			logger.info("加密结果：" + verificationCodeAuto);
		} catch (Exception e) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("customerId", customerId);
			map.put("orderId", orderId);
			OutPutLogUtil.printLoggger(e, map, logger);
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(105);
			responseDTO.setMsg("订单二维码生成时出错！");
			return responseDTO;
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("verificationCodeAuto", verificationCodeAuto);
		returnData.put("verificationCodeManual", verificationCodeManual);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	// 下单成功付款之后，改变用户附加信息表中的下单金额
	public void updateCustomerInfo(TOrder tOrder) {
		try {
			TCustomerInfo tCustomerInfo = customerInfoDAO.getByCustomerId(tOrder.getTCustomer().getId());
			tCustomerInfo.setForderTotal(tCustomerInfo.getForderTotal().add(tOrder.getFtotal()));
			tCustomerInfo.setFpayOrderNumber(tCustomerInfo.getFpayOrderNumber() + 1);
			tCustomerInfo.setForderNumber(tCustomerInfo.getForderNumber() + 1);
			if (tOrder.getFtotal().compareTo(BigDecimal.ZERO) == 0) {
				tCustomerInfo.setFpayZeroOrderNumber(tCustomerInfo.getFpayZeroOrderNumber() + 1);
			}
			if (tCustomerInfo.getFfirstOrderTime() == null) {
				tCustomerInfo.setFfirstOrderTime(tOrder.getFcreateTime());
			}
			customerInfoDAO.save(tCustomerInfo);
		} catch (Exception e) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("customerId", tOrder.getTCustomer().getId());
			map.put("orderId", tOrder.getId());
			OutPutLogUtil.printLoggger(e, map, logger);
		}
	}

	// 退款之后，改变用户附加信息表中的下单金额
	public void backCustomerInfo(TOrder tOrder) {
		try {
			TCustomerInfo tCustomerInfo = customerInfoDAO.getByCustomerId(tOrder.getTCustomer().getId());
			tCustomerInfo.setForderTotal(tCustomerInfo.getForderTotal().subtract(tOrder.getFtotal()));
			tCustomerInfo.setFpayOrderNumber(tCustomerInfo.getFpayOrderNumber() - 1);
			tCustomerInfo.setForderNumber(tCustomerInfo.getForderNumber() - 1);
			if (tOrder.getFtotal().compareTo(BigDecimal.ZERO) == 0) {
				tCustomerInfo.setFpayZeroOrderNumber(tCustomerInfo.getFpayZeroOrderNumber() - 1);
			}
			if (tCustomerInfo.getFfirstOrderTime().compareTo(tOrder.getFcreateTime()) == 0) {
				tCustomerInfo.setFfirstOrderTime(null);
			}
			customerInfoDAO.save(tCustomerInfo);
		} catch (Exception e) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("customerId", tOrder.getTCustomer().getId());
			map.put("orderId", tOrder.getId());
			OutPutLogUtil.printLoggger(e, map, logger);
		}
	}

	// 下单成功发送短信与钉钉
	@Transactional
	public void sendSMS(TOrder tOrder, Date now) {
		StringBuilder orderInfo = new StringBuilder();
		orderInfo.append("[").append(tOrder.getFeventTitle()).append("]").append("【订单号：").append(tOrder.getForderNum())
				.append("】");

		Map<String, String> smsParamMap = Maps.newHashMap();
		smsParamMap.put("orderName", orderInfo.toString());
		smsParamMap.put("orderList", "【我的-我的报名】");

		String phone = tOrder.getFcustomerPhone();
		// 发送订单支付成功通知短信
//		SmsResult smsResult = SmsUtil.sendSms(SmsUtil.PaySuccessSms, phone, smsParamMap);
//
//		TSms tSms = new TSms();
//		tSms.setSendPhone(tOrder.getFcustomerPhone());
//		tSms.setSendTime(now);
//		tSms.setSmsContent(smsResult.getContent());
//		tSms.setSmsType(SmsUtil.PaySuccessSms);
//		tSms.setSendResponse(smsResult.getResponse());
//		if (smsResult.isSuccess()) {
//			tSms.setSendSuccess(1);
//		} else {
//			tSms.setSendSuccess(0);
//		}
//		smsDAO.save(tSms);

		// 同步发送钉钉通知
		try {
			if (StringUtils.isNotBlank(DingTalkUtil.getPaySuccessSyncingDingTalk())) {
				String msg = new StringBuilder().append("用户订单支付提醒：用户[").append(tOrder.getFcustomerName()).append("]")
						.append("电话[").append(tOrder.getFcustomerPhone()).append("]").append("在")
						.append(DateFormatUtils.format(now, "yyyy年MM月dd日HH时mm分")).append("购买并支付了").append(orderInfo)
						.append("。请登录FOMS系统进行处理。http://www.fangxuele.com:8081/foms").toString();
				DingTalkUtil.sendDingTalk(msg, DingTalkUtil.getPaySuccessSyncingDingTalk());
			}
		} catch (Exception e) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("customerId", tOrder.getTCustomer().getId());
			map.put("orderId", tOrder.getId());
			OutPutLogUtil.printLoggger(e, map, logger);
			logger.error("微信支付订单时调用钉钉通知时，钉钉通知接口出错。");
		}
	}

	// 下单成功付款之后，改变用户标签
	@Transactional
	public void updateCustomerTag(TOrder tOrder) {
		try {
			TCustomerTag customerTag1 = customerTagDAO.findTCustomerTag(tOrder.getTCustomer().getId(), 1);
			TCustomerTag customerTag5 = customerTagDAO.findTCustomerTag(tOrder.getTCustomer().getId(), 5);
			if (customerTag1 != null) {
				if (tOrder.getFtotal().compareTo(BigDecimal.ZERO) == 0) {
					customerTag1.setFtag(5);

				} else {
					customerTag1.setFtag(3);
				}
				customerTagDAO.save(customerTag1);
			} else if (customerTag5 != null) {
				if (tOrder.getFtotal().compareTo(BigDecimal.ZERO) == 0) {

				} else {
					customerTag5.setFtag(3);
					customerTagDAO.save(customerTag5);
				}
			}

		} catch (Exception e) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("customerId", tOrder.getTCustomer().getId());
			map.put("orderId", tOrder.getId());
			OutPutLogUtil.printLoggger(e, map, logger);
		}
	}

	// 退款之后，改变用户订单表中的gps
	public void updateOrderGpsByIp(TOrder tOrder, String ip) {
		String gps = BmapUtil.updateOrderGps(ip, logger);
		orderDAO.updateOrderGps(gps, tOrder.getId());
	}
	
	public ResponseDTO verificationConfirm(String ticket, String verificationCode ,Integer clientType) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(verificationCode)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("verificationCode参数不能为空，请检查verificationCode的传递参数值！");
			return responseDTO;
		}

		TCustomer tCustomer = customerDAO.getByFticketAndFtype(ticket, 10);
		if (tCustomer == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(100);
			responseDTO.setMsg("您提供的Ticket信息有误，请重新登录零到壹！");
			return responseDTO;
		} else if (tCustomer.getFstatus().equals(10)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(101);
			responseDTO.setMsg("您的账号处于冻结状态，请联系零到壹客服人员！");
			return responseDTO;
		} else if (tCustomer.getFstatus().equals(999)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("您的账号处于作废状态，请联系零到壹客服人员！");
			return responseDTO;
		}
		
		TOrder tOrder = orderDAO.findByCode(tCustomer.getTSponsor().getId(), verificationCode);
		if (tOrder == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(104);
			responseDTO.setMsg("本订单不是有效订单！");
			return responseDTO;
		}
		if (!tOrder.getTSponsor().getId().equals(tCustomer.getTSponsor().getId())) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(105);
			responseDTO.setMsg("本订单不属于本商家！");
			return responseDTO;
		}
		if (tOrder.getFstatus().intValue() >= 60) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(107);
			responseDTO.setMsg("本订单是已核销过订单！");
			return responseDTO;
		}
		if (tOrder.getFstatus().intValue() != 20) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(108);
			responseDTO.setMsg("本订单不是可核销订单！");
			return responseDTO;
		}
		fxlService.orderStatusChange(2, tCustomer.getFname(), tOrder.getId(), null, tOrder.getFstatus(), 70);
		Date now = new Date();
		orderDAO.updateOrderStatusAndFverificationTime(tOrder.getId(), 70, now);
		tOrder.setFstatus(70);
		
		TOrderGoods tOrderGoods = orderGoodsDAO.findByOrderId(tOrder.getId()).get(0);
		TEvent tEvent = eventDAO.findOne(tOrderGoods.getFeventId());
		
		OrderForMerchantDTO orderForMerchantDTO = new OrderForMerchantDTO();
		orderForMerchantDTO.setOrderId(tOrder.getId());
		orderForMerchantDTO.setOrderNum(tOrder.getForderNum());
		orderForMerchantDTO.setCount(tOrderGoods.getFcount());
		orderForMerchantDTO.setGoodsImage(fxlService.getImageUrl(tEvent.getFimage1(), false));
		orderForMerchantDTO.setGoodsTitle(tOrderGoods.getFeventTitle());
		orderForMerchantDTO.setSpec(tOrderGoods.getFspec());
		orderForMerchantDTO.setStatus(tOrder.getFstatus());
		if(tOrder.getFstatus()==60||tOrder.getFstatus()==70){
			orderForMerchantDTO.setStatusString("已核销");
		}else{
			orderForMerchantDTO.setStatusString("未核销");
		}
		orderForMerchantDTO.setTotal(tOrder.getFtotal().toString());
		
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("orderForMerchantDTO", orderForMerchantDTO);
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("订单核销成功！");
		return responseDTO;
	}
	
	@Transactional(readOnly = true)
	public ResponseDTO getOrderListForMerchant(String ticket, Integer status, Integer pageSize,
			Integer offset) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (status == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("status参数不能为空，请检查status的传递参数值！");
			return responseDTO;
		}
		TCustomer tCustomer = customerDAO.getByFticketAndFtype(ticket, 10);
		if (tCustomer == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(100);
			responseDTO.setMsg("ticket参数有误，这是个无效或者过期的ticket信息！");
			return responseDTO;
		} else if (tCustomer.getFstatus().equals(10)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(101);
			responseDTO.setMsg("您的账号处于冻结状态，请联系零到壹客服人员！");
			return responseDTO;
		} else if (tCustomer.getFstatus().equals(999)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("您的账号处于作废状态，请联系零到壹客服人员！");
			return responseDTO;
		}

		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}

		List<OrderForMerchantDTO> orderList = Lists.newArrayList();

		StringBuilder hql = new StringBuilder();
		hql.append(
				"select t.id as id, g.feventTitle as feventTitle, t.forderNum as forderNum, t.ftotal as ftotal,  t.fstatus as fstatus ")
		.append(",g.fcount as fcount,g.fspec as fspec,e.fimage1 as fimage1 ")
		.append("from TOrder t inner join TOrderGoods g on t.id = g.forderId inner join TEvent e on e.id = g.feventId ")
		.append("where t.TSponsor.id = :sponsorId and t.fsellModel = 1");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("sponsorId", tCustomer.getTSponsor().getId());
		if (status.intValue() == 10) {
			hql.append(" and (t.fstatus between 60 and 70) order by t.fcreateTime desc");
		} else if (status.intValue() == 20) {
			hql.append(" and (t.fstatus between 10 and 20) order by t.fcreateTime desc");
		} 

		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();

		OrderForMerchantDTO orderForMerchantDTO = null;
		int statusValue = 0;
		for (Map<String, Object> amap : list) {
			orderForMerchantDTO = new OrderForMerchantDTO(); 
			if (amap.get("id") != null && StringUtils.isNotBlank(amap.get("id").toString())) {
				orderForMerchantDTO.setOrderId(amap.get("id").toString());
			}
			if (amap.get("forderNum") != null && StringUtils.isNotBlank(amap.get("forderNum").toString())) {
				orderForMerchantDTO.setOrderNum(amap.get("forderNum").toString());
			}
			if (amap.get("feventTitle") != null && StringUtils.isNotBlank(amap.get("feventTitle").toString())) {
				orderForMerchantDTO.setGoodsTitle(amap.get("feventTitle").toString());
			}
			if (amap.get("fimage1") != null && StringUtils.isNotBlank(amap.get("fimage1").toString())) {
				orderForMerchantDTO.setGoodsImage(fxlService.getImageUrl(amap.get("fimage1").toString(), false));
			}
			if (amap.get("fspec") != null && StringUtils.isNotBlank(amap.get("fspec").toString())) {
				orderForMerchantDTO.setSpec(amap.get("fspec").toString());
			}
			if (amap.get("fcount") != null && StringUtils.isNotBlank(amap.get("fcount").toString())) {
				orderForMerchantDTO.setCount((Integer) amap.get("fcount"));
			}
			if (amap.get("ftotal") != null && StringUtils.isNotBlank(amap.get("ftotal").toString())) {
				orderForMerchantDTO.setTotal(amap.get("ftotal").toString());
			}
			if (amap.get("fstatus") != null && StringUtils.isNotBlank(amap.get("fstatus").toString())) {
				statusValue = ((Integer) amap.get("fstatus")).intValue();
				orderForMerchantDTO.setStatus(statusValue);
				if(statusValue==60||statusValue==70){
					orderForMerchantDTO.setStatusString("已核销");
				}else{
					orderForMerchantDTO.setStatusString("未核销");
				}
			}

			orderList.add(orderForMerchantDTO);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("orderList", orderList);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}
	
	@Transactional(readOnly = true)
	public ResponseDTO getStatusNum(String ticket) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		TCustomer tCustomer = customerDAO.getByFticketAndFtype(ticket, 10);
		if (tCustomer == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(100);
			responseDTO.setMsg("ticket参数有误，这是个无效或者过期的ticket信息！");
			return responseDTO;
		} else if (tCustomer.getFstatus().equals(10)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(101);
			responseDTO.setMsg("您的账号处于冻结状态，请联系零到壹客服人员！");
			return responseDTO;
		} else if (tCustomer.getFstatus().equals(999)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("您的账号处于作废状态，请联系零到壹客服人员！");
			return responseDTO;
		}

		StringBuilder hql = new StringBuilder();
		hql.append("select t.id as id,t.fstatus as fstatus from TOrder t where t.TSponsor.id = :sponsorId and t.fsellModel = 1");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("sponsorId", tCustomer.getTSponsor().getId());

		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);

		Integer verificedNum = 0;
		Integer NotVerificNum = 0;
		Integer statusValue = 0;
		for (Map<String, Object> amap : list) {
			if (amap.get("fstatus") != null || StringUtils.isNotBlank(amap.get("fstatus").toString())) {
				statusValue = Integer.valueOf(amap.get("fstatus").toString());
				if (statusValue.intValue() >= 10 && statusValue.intValue() <= 20) {
					NotVerificNum++;
				} else if (statusValue.intValue() >= 60 && statusValue.intValue() <= 70) {
					verificedNum++;
				} 
			}
		}
		Map<String, Object> statusNum = Maps.newHashMap();
		statusNum.put("verificedNum", verificedNum);
		statusNum.put("NotVerificNum", NotVerificNum);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("statusNum", statusNum);
		responseDTO.setData(returnData);
		return responseDTO;
	}
	
	// 下单成功付款之后，改变用户附加信息表中的下单金额
	public void updateCustomerInfo(OrderUpdateCustomerInfoBean bean) {
		try {
			TCustomerInfo tCustomerInfo = customerInfoDAO.getByCustomerId(bean.getCustomerId());
			tCustomerInfo.setForderTotal(tCustomerInfo.getForderTotal().add(bean.getTotal()));
			tCustomerInfo.setFpayOrderNumber(tCustomerInfo.getFpayOrderNumber() + 1);
			tCustomerInfo.setForderNumber(tCustomerInfo.getForderNumber() + 1);
			if (bean.getTotal().compareTo(BigDecimal.ZERO) == 0) {
				tCustomerInfo.setFpayZeroOrderNumber(tCustomerInfo.getFpayZeroOrderNumber() + 1);
			}
			if (tCustomerInfo.getFfirstOrderTime() == null) {
				tCustomerInfo.setFfirstOrderTime(bean.getCreateTime());
			}
			customerInfoDAO.save(tCustomerInfo);
			
			List<TCustomerSubscribe> customerSubscribe = customerSubscribeDAO.getByOpertionId(bean.getCustomerId(), 2);
			if(customerSubscribe!=null && customerSubscribe.size()==1){
				
			}
		} catch (Exception e) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("customerId", bean.getCustomerId());
			map.put("orderId", bean.getOrderId());
			OutPutLogUtil.printLoggger(e, map, logger);
			throw new ServiceException("下单时异步写入订单统计到用户附加信息表出错。");
		}
	}
	
	
}