package com.innee.czyhInterface.service.v2;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.cuter44.wxpay.resps.Notify;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.dao.CustomerBonusDAO;
import com.innee.czyhInterface.dao.CustomerDAO;
import com.innee.czyhInterface.dao.CustomerInfoDAO;
import com.innee.czyhInterface.dao.CustomerSubscribeDAO;
import com.innee.czyhInterface.dao.EventBonusDAO;
import com.innee.czyhInterface.dao.OrderBonusDAO;
import com.innee.czyhInterface.dao.RelationDAO;
import com.innee.czyhInterface.dao.SceneUserDAO;
import com.innee.czyhInterface.dao.WxPayDAO;
import com.innee.czyhInterface.dto.CustomerDTO;
import com.innee.czyhInterface.dto.m.PageDTO;
import com.innee.czyhInterface.dto.m.PayDTO;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.entity.TCustomer;
import com.innee.czyhInterface.entity.TCustomerBonus;
import com.innee.czyhInterface.entity.TCustomerInfo;
import com.innee.czyhInterface.entity.TCustomerSubscribe;
import com.innee.czyhInterface.entity.TEventBonus;
import com.innee.czyhInterface.entity.TOrderBonus;
import com.innee.czyhInterface.entity.TRelation;
import com.innee.czyhInterface.entity.TSceneUser;
import com.innee.czyhInterface.entity.TWxPay;
import com.innee.czyhInterface.exception.ServiceException;
import com.innee.czyhInterface.util.CommonPage;
import com.innee.czyhInterface.util.Constant;
import com.innee.czyhInterface.util.DictionaryUtil;
import com.innee.czyhInterface.util.HeadImageUtil;
import com.innee.czyhInterface.util.HttpClientUtil;
import com.innee.czyhInterface.util.NumberUtil;
import com.innee.czyhInterface.util.PromptInfoUtil;
import com.innee.czyhInterface.util.bmap.BmapGpsLocationBean;
import com.innee.czyhInterface.util.bmap.LbsCloud;
import com.innee.czyhInterface.util.log.OutPutLogUtil;
import com.innee.czyhInterface.util.wx.WxPayResult;
import com.innee.czyhInterface.util.wx.WxPayUtil;

/**
 * 积分
 * 
 * @author qinkangkang
 *
 */
@Component
@Transactional
public class BonusService {

	private static final Logger logger = LoggerFactory.getLogger(BonusService.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private CustomerBonusDAO customerBonusDAO;

	@Autowired
	private CustomerSubscribeDAO customerSubscribeDAO;

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private SceneUserDAO sceneUserDAO;

	@Autowired
	private FxlService fxlService;

	@Autowired
	private CommonService commonService;

	@Autowired
	private EventBonusDAO eventBonusDAO;

	@Autowired
	private OrderBonusDAO orderBonusDAO;

	@Autowired
	private CustomerInfoDAO customerInfoDAO;

	@Autowired
	private RelationDAO relationDAO;

	@Autowired
	private WxPayDAO wxPayDAO;

	/**
	 * 用户关注或取消公众号赠送积分
	 * 
	 * @return
	 */
	public ResponseDTO changeBonus(String openid, String qrcode, Integer type, String gps, Long subscribeTime,
			String ip) {

		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(openid)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("openid参数不能为空，请检查openid的传递参数值！");
			return responseDTO;
		}
		if (type == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("type参数不能为空，请检查type的传递参数值！");
			return responseDTO;
		}

		boolean addBonus = false;
		boolean gpsIsError = false;
		if (StringUtils.isBlank(gps)) {
			gpsIsError = true;
			addBonus = true;
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

		// 如果传入的gps格式信息错误，将使用百度地图IP地址定位方式
		if (!gpsIsError) {
			LinkedHashMap<String, Object> paramsMap = Maps.newLinkedHashMap();
			try {
				paramsMap.put("ak", LbsCloud.AK);
				// paramsMap.put("callback", "renderReverse");
				String[] gpsa = StringUtils.split(gps, ',');
				paramsMap.put("location", new StringBuilder().append(gpsa[1]).append(",").append(gpsa[0]).toString());
				paramsMap.put("output", "json");
				paramsMap.put("pois", "0");

				String sn = LbsCloud.getSn(LbsCloud.LOCATION_URI_GPS, paramsMap);
				paramsMap.put("sn", sn);

				String json = HttpClientUtil.callUrlGet(LbsCloud.LBS_URL + LbsCloud.LOCATION_URI_GPS, paramsMap);
				BmapGpsLocationBean bmapGpsLocationBean = mapper.fromJson(json, BmapGpsLocationBean.class);
				// 返回状态码如果为0，表示获取活动距离成功
				if (bmapGpsLocationBean.getStatus() == 0) {
					Integer adcode = bmapGpsLocationBean.getResult().getAddressComponent().getAdcode();
					if (adcode.intValue() >= 110000 || adcode.intValue() <= 110999) {
						addBonus = true;
					}
				} else {
					String errorInfo = new StringBuilder().append("去百度LBS云坐标信息获取城市码时出错，状态码：")
							.append(bmapGpsLocationBean.getStatus()).append("；提示信息：").append("；GPS：").append(gpsa)
							.toString();
					logger.error(errorInfo);
					throw new ServiceException(errorInfo);
				}
			} catch (Exception e) {
				logger.error("去百度LBS云坐标信息获取城市码时出错");
			}
		}

		TCustomer customer = customerDAO.findOneByOpenId(openid);
		String customerId = customer.getId();
		String fromCustomerId = null;
		Integer risk = 0;
		if (type == 0) {
			List<TCustomerSubscribe> tCustomerSubscribe = customerSubscribeDAO.getByCustomer(customerId);
			fromCustomerId = tCustomerSubscribe.get(0).getFoperationId();
			if (tCustomerSubscribe.get(0).getFrisk() == 1) {
				risk = 1;
			}
		} else if (type == 1) {
			TCustomerInfo customerInfo = customerInfoDAO.findOneByfpointCode(qrcode);
			fromCustomerId = customerInfo.getFcustomerId();
		}
		// 在积分用户关注表中添加数据
		Date now = new Date();
		TCustomerSubscribe tCustomerSubscribe = new TCustomerSubscribe();
		tCustomerSubscribe.setFcustomerId(customerId);
		tCustomerSubscribe.setFoperationId(fromCustomerId);
		tCustomerSubscribe.setFoperationTime(now);
		tCustomerSubscribe.setFtype(type);
		tCustomerSubscribe.setFgps(gps);
		if (type == 1) {
			if (addBonus) {
				tCustomerSubscribe.setFrisk(0);// 0是正常添加积分
			} else {
				tCustomerSubscribe.setFrisk(1);
			}
		} else if (type == 0) {
			if (risk == 1) {
				tCustomerSubscribe.setFrisk(1);
			} else {
				tCustomerSubscribe.setFrisk(0);
			}
		}
		tCustomerSubscribe = customerSubscribeDAO.save(tCustomerSubscribe);

		if (type == 1) {
			// 如果是添加关注
			// 查询递推表判断用户是否是首次关注
			if (addBonus) {
				TCustomer tCustomer = customerDAO.findOne(customerId);
				if (tCustomer != null && StringUtils.isNotBlank(tCustomer.getFweixinId())) {
					TCustomerBonus tCustomerBonus = new TCustomerBonus();
					tCustomerBonus.setFcreateTime(now);
					tCustomerBonus.setFbonus(Integer.parseInt(DictionaryUtil.getCode(DictionaryUtil.BonusType, 10)));
					tCustomerBonus.setFcustermerId(customerId);
					tCustomerBonus.setFobject(tCustomerSubscribe.getId());
					tCustomerBonus.setFtype(10);
					customerBonusDAO.save(tCustomerBonus);
					customerInfoDAO.updatePoint(customerId,
							Integer.parseInt(DictionaryUtil.getCode(DictionaryUtil.BonusType, 10)));
					TCustomerBonus invitationBonus = new TCustomerBonus();
					invitationBonus.setFcreateTime(now);
					invitationBonus.setFbonus(Integer.parseInt(DictionaryUtil.getCode(DictionaryUtil.BonusType, 30)));
					invitationBonus.setFcustermerId(fromCustomerId);
					invitationBonus.setFobject(tCustomerSubscribe.getId());
					invitationBonus.setFtype(30);
					customerBonusDAO.save(invitationBonus);
					customerInfoDAO.updatePoint(fromCustomerId,
							Integer.parseInt(DictionaryUtil.getCode(DictionaryUtil.BonusType, 30)));
				}
			}
		} else if (type == 0 && risk == 0) {
			// 如果是取消关注
			List<TCustomerBonus> Bonus = customerBonusDAO.findByCustomerId(customerId);
			if (Bonus == null || Bonus.size() == 0) {
				TCustomerBonus tCustomerBonus = new TCustomerBonus();
				tCustomerBonus.setFcreateTime(now);
				tCustomerBonus.setFbonus(Integer.parseInt(DictionaryUtil.getCode(DictionaryUtil.BonusType, 20)));
				tCustomerBonus.setFcustermerId(customerId);
				tCustomerBonus.setFobject(tCustomerSubscribe.getId());
				tCustomerBonus.setFtype(20);
				customerBonusDAO.save(tCustomerBonus);
				customerInfoDAO.updatePoint(customerId,
						Integer.parseInt(DictionaryUtil.getCode(DictionaryUtil.BonusType, 20)));
				TCustomerBonus invitationBonus = new TCustomerBonus();
				invitationBonus.setFcreateTime(now);
				invitationBonus.setFbonus(Integer.parseInt(DictionaryUtil.getCode(DictionaryUtil.BonusType, 40)));
				invitationBonus.setFcustermerId(fromCustomerId);
				invitationBonus.setFobject(tCustomerSubscribe.getId());
				invitationBonus.setFtype(40);
				customerBonusDAO.save(invitationBonus);
				customerInfoDAO.updatePoint(fromCustomerId,
						Integer.parseInt(DictionaryUtil.getCode(DictionaryUtil.BonusType, 40)));
				// 取消关注时添加递推表中取消时间
				TSceneUser tSceneUser = sceneUserDAO.findBysceneStrAndopenID(openid);
				if (tSceneUser != null) {
					sceneUserDAO.setUnSubscribe(tSceneUser.getId(), 0, 1, new Date(subscribeTime * 1000L));
				}

			}
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		if (type == 0) {
			responseDTO.setMsg(
					PromptInfoUtil.getPrompt(PromptInfoUtil.czyhInterface_BONUS, "czyhInterface.service.bonus.changeBonus.unSubscribe"));
		} else if (type == 1) {
			responseDTO.setMsg(
					PromptInfoUtil.getPrompt(PromptInfoUtil.czyhInterface_BONUS, "czyhInterface.service.bonus.changeBonus.subscribe"));
		}
		return responseDTO;
	}

	/**
	 * 我的积分列表
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO myBonusDeail(String ticket, Integer clientType, Integer pageSize, Integer offset) {

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
		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}
		StringBuilder hql = new StringBuilder();
		hql.append(
				"select t.id as id, t.fbonus as fbonus,t.fcreateTime as fcreateTime,t.ftype as ftype,o.fname as username,o.fphoto as photo ")
				.append("from TCustomerBonus t left join TCustomerSubscribe s on s.id = t.fobject ")
				.append("left join TCustomer o on o.id = s.fcustomerId ")
				.append("where t.fcustermerId = :customerId order by fcreateTime desc");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("customerId", customerDTO.getCustomerId());
		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();
		List<Map<String, Object>> bonusList = Lists.newArrayList();
		int i = 1;
		for (Map<String, Object> aMap : list) {
			Map<String, Object> bonusMap = new HashMap<String, Object>();
			bonusMap.put("id", aMap.get("id"));
			bonusMap.put("bonus", aMap.get("fbonus"));
			if (aMap.get("username") != null && StringUtils.isNotBlank(aMap.get("username").toString())) {
				bonusMap.put("userName", aMap.get("username").toString());
			} else {
				bonusMap.put("userName", "系统发放");
			}
			if (((Integer) aMap.get("fbonus")).intValue() > 0) {
				bonusMap.put("plus", true);
			} else {
				bonusMap.put("plus", false);
			}
			if (aMap.get("photo") != null && StringUtils.isNotBlank(aMap.get("photo").toString())) {
				bonusMap.put("photo", HeadImageUtil.getHeadImage(aMap.get("photo").toString(), 46));
			} else {
				bonusMap.put("photo", Constant.defaultHeadImgUrl);
			}
			bonusMap.put("type",
					DictionaryUtil.getString(DictionaryUtil.BonusType, Integer.parseInt(aMap.get("ftype").toString())));
			bonusMap.put("createTime", DateFormatUtils.format((Date) aMap.get("fcreateTime"), "yyyy-MM-dd"));
			bonusMap.put("number", page.getOffset() + (i++));
			bonusList.add(bonusMap);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("bonusList", bonusList);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * 积分商城列表
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO bonusEventList(Integer pageSize, Integer offset, String ticket, Integer clientType) {
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

		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}

		// 查询该用户兑换过几次该商品

		TCustomerInfo customerInfo = customerInfoDAO.getByCustomerId(customerDTO.getCustomerId());
		Integer count = 0;
		if (customerInfo != null) {
			count = customerInfo.getFpoint();
		}
		StringBuilder hql = new StringBuilder();
		hql.append(
				"select t.id as bonusEventId, t.TEvent.ftitle as ftitle, t.TEvent.id as eventId, t.fprompt as fprompt, t.fstartDate as fstartDate, t.ftype as type, t.flimitation as flimitation,")
				.append(" t.fendDate as fendDate, t.fstock as fstock, t.fusePerson as fusePerson, t.fstatus as fstatus, t.fprice as fprice, t.fuseNote as fuseNote,")
				.append(" t.TEvent.fimage1 as fimage1, t.fbonus as fbonus, t.faddress as faddress, t.fuseType as fuseType,t.fdeal as fbonusPrice from TEventBonus t where t.fstatus = 20 ");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hql.append(" order by t.forder asc,t.fcreateTime desc ");
		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();

		List<Map<String, Object>> bonusEventList = Lists.newArrayList();

		Date date = null;
		for (Map<String, Object> amap : list) {
			Map<String, Object> bonusEventMap = new HashMap<String, Object>();
			if (amap.get("bonusEventId") != null && StringUtils.isNotBlank(amap.get("bonusEventId").toString())) {
				bonusEventMap.put("bonusEventId", amap.get("bonusEventId"));
			}
			if (amap.get("eventId") != null && StringUtils.isNotBlank(amap.get("eventId").toString())) {
				bonusEventMap.put("eventId", amap.get("eventId"));
			}
			if (amap.get("fprompt") != null && StringUtils.isNotBlank(amap.get("fprompt").toString())) {
				bonusEventMap.put("prompt", amap.get("fprompt"));
			} else {
				bonusEventMap.put("prompt", StringUtils.EMPTY);
			}
			if (amap.get("ftitle") != null && StringUtils.isNotBlank(amap.get("ftitle").toString())) {
				bonusEventMap.put("title", amap.get("ftitle"));
			}
			if (amap.get("fbonus") != null && StringUtils.isNotBlank(amap.get("fbonus").toString())) {
				bonusEventMap.put("bonus", amap.get("fbonus"));
			}
			if (amap.get("fbonusPrice") != null && StringUtils.isNotBlank(amap.get("fbonusPrice").toString())
					&& ((BigDecimal) amap.get("fbonusPrice")).compareTo(BigDecimal.ZERO) > 0) {
				bonusEventMap.put("bonusPrice", amap.get("fbonusPrice"));
			} else {
				bonusEventMap.put("bonusPrice", StringUtils.EMPTY);
			}
			if (amap.get("fprice") != null && StringUtils.isNotBlank(amap.get("fprice").toString())) {
				bonusEventMap.put("price", amap.get("fprice"));
			}
			if (amap.get("fimage1") != null && StringUtils.isNotBlank(amap.get("fimage1").toString())) {
				bonusEventMap.put("imageUrl", fxlService.getImageUrl(amap.get("fimage1").toString(), false));
			}
			if (amap.get("faddress") != null && StringUtils.isNotBlank(amap.get("faddress").toString())) {
				bonusEventMap.put("address", amap.get("faddress"));
			} else {
				bonusEventMap.put("faddress", StringUtils.EMPTY);
			}
			if (amap.get("fusePerson") != null && StringUtils.isNotBlank(amap.get("fusePerson").toString())) {
				bonusEventMap.put("usePerson", amap.get("fusePerson"));
			}
			if (amap.get("fuseType") != null && StringUtils.isNotBlank(amap.get("fuseType").toString())) {
				bonusEventMap.put("convertWay", (amap.get("fuseType").toString()));
			}
			if (amap.get("type") != null && StringUtils.isNotBlank(amap.get("type").toString())) {
				if (((Integer) amap.get("type")).intValue() == 20) {
					bonusEventMap.put("ifNeedAddress", true);
				} else {
					bonusEventMap.put("ifNeedAddress", false);
				}

			}
			if (amap.get("fuseNote") != null && StringUtils.isNotBlank(amap.get("fuseNote").toString())) {
				bonusEventMap.put("useNote", amap.get("fuseNote"));
			} else {
				bonusEventMap.put("useNote", StringUtils.EMPTY);
			}
			StringBuilder availableTime = new StringBuilder();
			if (amap.get("fendDate") != null && StringUtils.isNotBlank(amap.get("fendDate").toString())) {
				date = (Date) amap.get("fendDate");
				availableTime.append(DateFormatUtils.format(date, "yyyy年MM月dd日")).append("截止兑换");
				bonusEventMap.put("availableTime", availableTime.toString());
			}

			// 先判断商品是否已经抢光了，如果抢光了则直接提示
			boolean isCovert = false;
			if (amap.get("flimitation") != null && StringUtils.isNotBlank(amap.get("flimitation").toString())) {
				if (Integer.parseInt(amap.get("flimitation").toString()) > 0) {
					List<TOrderBonus> orderBonuList = orderBonusDAO.findByCunstomerAndEvent(customerDTO.getCustomerId(),
							amap.get("bonusEventId").toString());
					if (orderBonuList.size() >= Integer.parseInt(amap.get("flimitation").toString())) {
						isCovert = true;
					}
				}
			}
			if (isCovert) {
				bonusEventMap.put("ifCanConvert", false);
				bonusEventMap.put("convertInfo", PromptInfoUtil.getPrompt(PromptInfoUtil.czyhInterface_BONUS,
						"czyhInterface.service.bonus.bonusEventList.hasChange"));
			} else {
				if (amap.get("fstock") != null && StringUtils.isNotBlank(amap.get("fstock").toString())) {
					if ((Integer.parseInt(amap.get("fstock").toString())) <= 0) {
						bonusEventMap.put("ifCanConvert", false);
						bonusEventMap.put("convertInfo", PromptInfoUtil.getPrompt(PromptInfoUtil.czyhInterface_BONUS,
								"czyhInterface.service.bonus.bonusEventList.soldOut"));
					} else {
						if (count.intValue() < Integer.parseInt(amap.get("fbonus").toString())
								&& amap.get("fbonus") != null
								&& StringUtils.isNotBlank(amap.get("fbonus").toString())) {
							bonusEventMap.put("ifCanConvert", false);
							bonusEventMap.put("convertInfo", PromptInfoUtil.getPrompt(PromptInfoUtil.czyhInterface_BONUS,
									"czyhInterface.service.bonus.bonusEventList.lackBonus"));
						} else {
							bonusEventMap.put("ifCanConvert", true);
							bonusEventMap.put("convertInfo", PromptInfoUtil.getPrompt(PromptInfoUtil.czyhInterface_BONUS,
									"czyhInterface.service.bonus.bonusEventList.nowChange"));
						}
					}
				} else {
					bonusEventMap.put("ifCanConvert", false);
					bonusEventMap.put("convertInfo", PromptInfoUtil.getPrompt(PromptInfoUtil.czyhInterface_BONUS,
							"czyhInterface.service.bonus.bonusEventList.soldOut"));
				}
			}

			bonusEventList.add(bonusEventMap);
		}
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("eventBonusList", bonusEventList);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * 我的积分首页
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getUserBonus(String ticket, Integer clientType) {

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

		TSceneUser sceneUser = sceneUserDAO.findOneByOpenid(customerDTO.getWxId());
		String date = null;
		if (sceneUser != null && sceneUser.getFsubscribeTime() != null) {
			date = DateFormatUtils.format(sceneUser.getFsubscribeTime(), "yyyy-MM-dd HH:mm");
		} else {
			TCustomer customer = customerDAO.findOne(customerDTO.getCustomerId());
			date = DateFormatUtils.format(customer.getFcreateTime(), "yyyy-MM-dd HH:mm");
		}

		List<TRelation> fans = relationDAO.findByType(customerDTO.getCustomerId(), 1);
		TCustomerInfo customerInfo = customerInfoDAO.getByCustomerId(customerDTO.getCustomerId());
		Integer bonusCount = 0;
		if (customerInfo != null) {
			bonusCount = customerInfo.getFpoint();
		}
		Map<String, Object> userBonusInfo = new HashMap<String, Object>();
		userBonusInfo.put("name", customerDTO.getWxName());
		userBonusInfo.put("photo", HeadImageUtil.getHeadImage(customerDTO.getPhoto(), 96));
		userBonusInfo.put("subscribeTime", date);
		userBonusInfo.put("fansNumber", fans.size());
		userBonusInfo.put("bonusCount", bonusCount);
		if (bonusCount.intValue() > 0) {
			userBonusInfo.put("plus", true);
		} else {
			userBonusInfo.put("plus", false);
		}
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("userBonusInfo", userBonusInfo);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * 我的兑换列表
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO bonusOrderList(Integer pageSize, Integer offset, String ticket, Integer clientType) {
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

		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}

		StringBuilder hql = new StringBuilder();
		hql.append(
				"select t.id as orderBounsId,t.TEventBonus.TEvent.id as eventId,t.TEventBonus.TEvent.ftitle as ftitle, ")
				.append(" t.TEventBonus.TEvent.fimage1 as fimage1,t.TEventBonus.fprice as fprice,t.TEventBonus.fbonus as fbonus,")
				.append(" t.fexpress as faddress,t.fcreateTime as fcreateTime,t.fcustomerName as fcustomerName,t.fcustomerPhone as fcustomerPhone,")
				.append(" t.fremark as fremark,t.freply as fnote,t.fstatus as fstatus,t.TEventBonus.fdeal as fbonusPrice from TOrderBonus t where t.fstatus < 999");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hql.append(" and t.fcustomerId = :fcustomerId");
		hqlMap.put("fcustomerId", customerDTO.getCustomerId());
		hql.append(" order by t.fcreateTime desc ");
		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();

		List<Map<String, Object>> bonusOrderList = Lists.newArrayList();

		// Date date = null;
		for (Map<String, Object> amap : list) {
			Map<String, Object> bonusOrderMap = new HashMap<String, Object>();
			if (amap.get("orderBounsId") != null && StringUtils.isNotBlank(amap.get("orderBounsId").toString())) {
				bonusOrderMap.put("orderBounsId", amap.get("orderBounsId"));
			}
			if (amap.get("eventId") != null && StringUtils.isNotBlank(amap.get("eventId").toString())) {
				bonusOrderMap.put("eventId", amap.get("eventId"));
			}
			if (amap.get("ftitle") != null && StringUtils.isNotBlank(amap.get("ftitle").toString())) {
				bonusOrderMap.put("title", amap.get("ftitle"));
			}
			if (amap.get("fbonus") != null && StringUtils.isNotBlank(amap.get("fbonus").toString())) {
				bonusOrderMap.put("bonus", amap.get("fbonus"));
			}
			if (amap.get("fbonusPrice") != null && StringUtils.isNotBlank(amap.get("fbonusPrice").toString())) {
				bonusOrderMap.put("bonusPrice", amap.get("fbonusPrice"));
			} else {
				bonusOrderMap.put("bonusPrice", StringUtils.EMPTY);
			}
			if (amap.get("fprice") != null && StringUtils.isNotBlank(amap.get("fprice").toString())) {
				bonusOrderMap.put("price", amap.get("fprice"));
			}
			if (amap.get("fimage1") != null && StringUtils.isNotBlank(amap.get("fimage1").toString())) {
				bonusOrderMap.put("imageUrl", fxlService.getImageUrl(amap.get("fimage1").toString(), false));
			}
			if (amap.get("faddress") != null && StringUtils.isNotBlank(amap.get("faddress").toString())) {
				bonusOrderMap.put("address", amap.get("faddress"));
			}
			if (amap.get("fcreateTime") != null && StringUtils.isNotBlank(amap.get("fcreateTime").toString())) {
				bonusOrderMap.put("createTime", DateFormatUtils.format((Date) amap.get("fcreateTime"), "yyyy-MM-dd"));
			}
			if (amap.get("fcustomerName") != null && StringUtils.isNotBlank(amap.get("fcustomerName").toString())) {
				bonusOrderMap.put("customerName", amap.get("fcustomerName"));
			} else {
				bonusOrderMap.put("customerName", StringUtils.EMPTY);
			}
			if (amap.get("fcustomerPhone") != null && StringUtils.isNotBlank(amap.get("fcustomerPhone").toString())) {
				bonusOrderMap.put("customerPhone", amap.get("fcustomerPhone"));
			} else {
				bonusOrderMap.put("customerPhone", StringUtils.EMPTY);
			}
			if (amap.get("fremark") != null && StringUtils.isNotBlank(amap.get("fremark").toString())) {
				bonusOrderMap.put("remark", amap.get("fremark"));
			} else {
				bonusOrderMap.put("remark", StringUtils.EMPTY);
			}
			if (amap.get("fnote") != null && StringUtils.isNotBlank(amap.get("fnote").toString())) {
				bonusOrderMap.put("note", amap.get("fnote"));
			} else {
				bonusOrderMap.put("note", StringUtils.EMPTY);
			}
			if (amap.get("fstatus") != null && StringUtils.isNotBlank(amap.get("fstatus").toString())) {
				bonusOrderMap.put("status", DictionaryUtil.getString(DictionaryUtil.OrderBonusType,
						Integer.parseInt(amap.get("fstatus").toString())));
			}
			bonusOrderList.add(bonusOrderMap);
		}
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("bonusOrderList", bonusOrderList);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * 兑换商城商品
	 * 
	 * @return
	 */
	public ResponseDTO convertOrder(String ticket, Integer clientType, String bonusEventId, String name, String phone,
			String address, String remark, Integer payType, Integer payClientType, String ip) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(bonusEventId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("bonusEventId参数不能为空，请检查bonusEventId的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(name)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("name参数不能为空，请检查name的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(phone)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(204);
			responseDTO.setMsg("phone参数不能为空，请检查phone的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		// 校验是否限购
		TEventBonus teventBonus = eventBonusDAO.findOne(bonusEventId);
		if (teventBonus.getFtype().intValue() == 20 && StringUtils.isBlank(address)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(205);
			responseDTO.setMsg("address参数不能为空，请检查address的传递参数值！");
			return responseDTO;
		}
		if (teventBonus.getFlimitation().intValue() > 0) {
			List<TOrderBonus> orderBonuList = orderBonusDAO.findByCunstomerAndEvent(customerDTO.getCustomerId(),
					teventBonus.getId());
			if (orderBonuList.size() >= teventBonus.getFlimitation().intValue()) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(105);
				responseDTO.setMsg(
						PromptInfoUtil.getPrompt(PromptInfoUtil.czyhInterface_BONUS, "czyhInterface.service.bonus.convertOrder.changed"));
				return responseDTO;
			}
		}
		// 校验库存
		if (teventBonus.getFstock().intValue() <= 0) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(103);
			responseDTO.setMsg(
					PromptInfoUtil.getPrompt(PromptInfoUtil.czyhInterface_BONUS, "czyhInterface.service.bonus.convertOrder.soldOut"));
			return responseDTO;
		}
		// 校验积分是否满足兑换
		TCustomerInfo customerInfo = customerInfoDAO.getByCustomerId(customerDTO.getCustomerId());
		Integer count = 0;
		if (customerInfo != null) {
			count = customerInfo.getFpoint();
		}
		if (count.intValue() < teventBonus.getFbonus().intValue()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(104);
			responseDTO.setMsg(
					PromptInfoUtil.getPrompt(PromptInfoUtil.czyhInterface_BONUS, "czyhInterface.service.bonus.convertOrder.lackBonus"));
			return responseDTO;
		}
		Date now = new Date();

		Map<String, Object> returnData = Maps.newHashMap();
		// 保存兑换订单
		TOrderBonus tOrderBonus = new TOrderBonus();
		tOrderBonus.setFcreateTime(now);
		tOrderBonus.setFcustomerId(customerDTO.getCustomerId());
		tOrderBonus.setFcustomerName(name);
		tOrderBonus.setFcustomerPhone(phone);
		tOrderBonus.setFexpress(address);
		tOrderBonus.setFremark(remark);
		tOrderBonus.setTEventBonus(teventBonus);
		tOrderBonus.setForderNum(NumberUtil.getOtherOrderNum(1, "JF"));
		tOrderBonus.setFtotal(teventBonus.getFdeal());

		if (teventBonus.getFdeal() == null || teventBonus.getFdeal().compareTo(BigDecimal.ZERO) == 0) {
			tOrderBonus.setFstatus(10);
			tOrderBonus = orderBonusDAO.save(tOrderBonus);
			returnData.put("zero", true);
			returnData.put("orderId", tOrderBonus.getId());
			this.subtractStock(teventBonus, now, tOrderBonus);
		} else {
			tOrderBonus.setFpayType(payType);
			tOrderBonus.setFstatus(999);
			tOrderBonus = orderBonusDAO.save(tOrderBonus);
			// 根据用户选择的支付类型，来进行条用不同的支付接口
			WxPayResult wxPayResult = null;
			if (payType.intValue() == 20) {
				// 调用微信支付操作
				try {
					// TODO根据支付方式调用不同的支付接口，获取prePayId返回给前台
					String nonceStr = RandomStringUtils.randomAlphanumeric(32);

					// ip = "192.168.1.1";
					if (payClientType.intValue() == 1) {
						wxPayResult = WxPayUtil.wxPay(tOrderBonus.getForderNum(),
								tOrderBonus.getTEventBonus().getTEvent().getFtitle(),
								tOrderBonus.getFtotal().multiply(new BigDecimal(100)).intValue(), customerDTO.getWxId(),
								now, DateUtils.addDays(now, 1), ip, nonceStr);
					} else {
						wxPayResult = WxPayUtil.wxAppPay(tOrderBonus.getForderNum(),
								tOrderBonus.getTEventBonus().getTEvent().getFtitle(),
								tOrderBonus.getFtotal().multiply(new BigDecimal(100)).intValue(), now,
								DateUtils.addDays(now, 1), ip, nonceStr);
					}
					logger.info(wxPayResult.getResponse());

					TWxPay tWxPay = wxPayDAO.getByOrderIdAndInOutAndStatus(tOrderBonus.getId(), 1, 10);
					if (tWxPay == null) {
						tWxPay = new TWxPay();
						tWxPay.setFclientType(payClientType);
						tWxPay.setFinOut(1);
						tWxPay.setFcreateTime(now);
						tWxPay.setForderId(tOrderBonus.getId());
						tWxPay.setForderType(2);
						tWxPay.setTCustomer(customerDAO.findOne(customerDTO.getCustomerId()));
						tWxPay.setFstatus(10);
					}
					tWxPay.setFupdateTime(now);
					tWxPay.setFppResponseInfo(wxPayResult.getResponse());
					wxPayDAO.save(tWxPay);

				} catch (Exception e) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("orderId", tOrderBonus.getId());
					map.put("customerId", customerDTO.getCustomerId());
					OutPutLogUtil.printLoggger(e, map, logger);
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
			payDTO.setOrderId(tOrderBonus.getId());
			payDTO.setOrderNum(tOrderBonus.getForderNum());
			payDTO.setPrepayId(wxPayResult.getPrepayId());
			payDTO.setNonceStr(wxPayResult.getNonceStrVal());
			payDTO.setPaySign(wxPayResult.getPaySign());
			payDTO.setTimestamp(wxPayResult.getTimestamp());
			payDTO.setSignType(wxPayResult.getSignType());
			payDTO.setPayPackage(wxPayResult.getPayPackage());

			returnData.put("zero", false);
			returnData.put("payInfo", payDTO);
		}
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setData(returnData);
		responseDTO.setMsg(
				PromptInfoUtil.getPrompt(PromptInfoUtil.czyhInterface_BONUS, "czyhInterface.service.bonus.convertOrder.changeSuccess"));
		return responseDTO;
	}

	/**
	 * 我的积分列表
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO myFansDeail(String ticket, Integer clientType, Integer pageSize, Integer offset) {

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
		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}
		StringBuilder hql = new StringBuilder();
		hql.append("select r.id as id, c.fname as fname,r.fcreateTime as fcreateTime,s.frisk as frisk ")
				.append("from TRelation r inner join TCustomerSubscribe s on r.fcsid = s.id ")
				.append("inner join TCustomer c on c.id = r.fbyCustomerId ")
				.append("where r.fcustomerId = :customerId and r.frelationType = 1 order by r.fcreateTime desc");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("customerId", customerDTO.getCustomerId());
		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();
		List<Map<String, Object>> bonusList = Lists.newArrayList();
		int i = 1;
		for (Map<String, Object> aMap : list) {
			Map<String, Object> bonusMap = new HashMap<String, Object>();
			bonusMap.put("id", aMap.get("id"));
			if (Integer.parseInt(aMap.get("frisk").toString()) == 0) {
				bonusMap.put("bonus", "+10");
			} else {
				bonusMap.put("bonus", 0);
			}
			if (aMap.get("fname") != null && StringUtils.isNotBlank(aMap.get("fname").toString())) {
				bonusMap.put("name", aMap.get("fname"));
			} else {
				bonusMap.put("name", "");
			}
			bonusMap.put("explain",
					DictionaryUtil.getString(DictionaryUtil.GpsRisk, Integer.parseInt(aMap.get("frisk").toString())));
			bonusMap.put("createTime", DateFormatUtils.format((Date) aMap.get("fcreateTime"), "yyyy-MM-dd"));
			bonusMap.put("number", page.getOffset() + (i++));
			bonusList.add(bonusMap);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("fansList", bonusList);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * 用户关注公众号赠送积分
	 * 
	 * @return
	 */
	public ResponseDTO AddBonusAttention(String openid, String qrcode) {

		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(openid)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("openid参数不能为空，请检查openid的传递参数值！");
			return responseDTO;
		}

		TCustomerInfo customerInfo = customerInfoDAO.findOneByfpointCode(qrcode);
		String fromCustomerId = null;
		try {
			fromCustomerId = customerInfo.getFcustomerId();
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			logger.warn("当前扫码客户openId：" + openid + "；识别海报二维码：" + qrcode);
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("客户识别海报二维码信息中的海报主人的标识码信息出错，海报已经过期！");
			return responseDTO;
		}

		TCustomer customer = customerDAO.findOneByOpenId(openid);
		String customerId = customer.getId();
		// 在积分用户关注表中添加数据
		Date now = new Date();
		TCustomerSubscribe tCustomerSubscribe = new TCustomerSubscribe();
		tCustomerSubscribe.setFcustomerId(customerId);
		tCustomerSubscribe.setFoperationId(fromCustomerId);
		tCustomerSubscribe.setFoperationTime(now);
		tCustomerSubscribe.setFtype(1);
		tCustomerSubscribe.setFgps(null);
		// 未授权地理信息
		tCustomerSubscribe.setFrisk(1);
		tCustomerSubscribe = customerSubscribeDAO.save(tCustomerSubscribe);

		// 添加用户关注记录表
		TRelation tRelation = null;
		tRelation = relationDAO.findByUser(fromCustomerId, customerId);
		if (tRelation != null) {
			tRelation.setFrelationType(1);// 1 关注
			tRelation.setFcsid(tCustomerSubscribe.getId());
			tRelation.setFcreateTime(now);
			relationDAO.save(tRelation);
		} else {
			tRelation = new TRelation();
			tRelation.setFcustomerId(fromCustomerId);
			tRelation.setFbyCustomerId(customerId);
			tRelation.setFrelationType(1);// 1 关注
			tRelation.setFcsid(tCustomerSubscribe.getId());
			tRelation.setFcreateTime(now);
			relationDAO.save(tRelation);
		}

		TCustomer tCustomer = customerDAO.findOne(customerId);
		if (tCustomer != null && StringUtils.isNotBlank(tCustomer.getFweixinId())) {
			TCustomerBonus tCustomerBonus = new TCustomerBonus();
			tCustomerBonus.setFcreateTime(now);
			tCustomerBonus.setFbonus(Integer.parseInt(DictionaryUtil.getCode(DictionaryUtil.BonusType, 10)));
			tCustomerBonus.setFcustermerId(customerId);
			tCustomerBonus.setFobject(tCustomerSubscribe.getId());
			tCustomerBonus.setFtype(10);
			customerBonusDAO.save(tCustomerBonus);
			customerInfoDAO.updatePoint(customerId,
					Integer.parseInt(DictionaryUtil.getCode(DictionaryUtil.BonusType, 10)));
		}

		TSceneUser tSceneUser = sceneUserDAO.findOneByOpenid(openid);
		tSceneUser.setFbounsCustomer(1);
		sceneUserDAO.save(tSceneUser);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO
				.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.czyhInterface_BONUS, "czyhInterface.service.bonus.changeBonus.subscribe"));
		return responseDTO;
	}

	/**
	 * 邀请用户关注公众号赠送积分
	 * 
	 * @return
	 */
	public ResponseDTO invitation(String openid, String qrcode, String gps) {

		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(openid)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("openid参数不能为空，请检查openid的传递参数值！");
			return responseDTO;
		}

		boolean addBonus = false;
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

		// 如果传入的gps格式信息错误，将使用百度地图IP地址定位方式
		if (!gpsIsError) {
			LinkedHashMap<String, Object> paramsMap = Maps.newLinkedHashMap();
			try {
				paramsMap.put("ak", LbsCloud.AK);
				// paramsMap.put("callback", "renderReverse");
				String[] gpsa = StringUtils.split(gps, ',');
				paramsMap.put("location", new StringBuilder().append(gpsa[1]).append(",").append(gpsa[0]).toString());
				paramsMap.put("output", "json");
				paramsMap.put("pois", "0");

				String sn = LbsCloud.getSn(LbsCloud.LOCATION_URI_GPS, paramsMap);
				paramsMap.put("sn", sn);

				String json = HttpClientUtil.callUrlGet(LbsCloud.LBS_URL + LbsCloud.LOCATION_URI_GPS, paramsMap);
				BmapGpsLocationBean bmapGpsLocationBean = mapper.fromJson(json, BmapGpsLocationBean.class);
				// 返回状态码如果为0，表示获取活动距离成功
				if (bmapGpsLocationBean.getStatus() == 0) {
					Integer adcode = bmapGpsLocationBean.getResult().getAddressComponent().getAdcode();
					if (adcode.intValue() >= 110000 && adcode.intValue() <= 110999) {
						addBonus = true;
					}
				} else {
					String errorInfo = new StringBuilder().append("去百度LBS云坐标信息获取城市码时出错，状态码：")
							.append(bmapGpsLocationBean.getStatus()).append("；提示信息：").append("；GPS：").append(gpsa)
							.toString();
					logger.error(errorInfo);
					throw new ServiceException(errorInfo);
				}
			} catch (Exception e) {
				logger.error("去百度LBS云坐标信息获取城市码时出错");
			}
		}

		TCustomerInfo customerInfo = customerInfoDAO.findOneByfpointCode(qrcode);
		if (customerInfo == null) {
			logger.error(new StringBuilder().append("当前扫码客户openId：").append(openid).append("；识别海报二维码：").append(qrcode)
					.append("。客户二维码海报已过期！").toString());
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("客户识别海报二维码信息中的海报主人的标识码信息已经过期！");
			return responseDTO;
		}
		String fromCustomerId = customerInfo.getFcustomerId();

		TCustomer customer = customerDAO.findOneByOpenId(openid);
		String customerId = customer.getId();

		TRelation tRelation = relationDAO.findByUser(fromCustomerId, customerId);
		TCustomerSubscribe tCustomerSubscribe = customerSubscribeDAO.findOne(tRelation.getFcsid());

		// 在积分用户关注表中添加数据
		Date now = new Date();
		tCustomerSubscribe.setFoperationTime(now);
		tCustomerSubscribe.setFgps(gps);
		if (addBonus) {
			tCustomerSubscribe.setFrisk(0);
		} else {
			tCustomerSubscribe.setFrisk(2);
		}
		tCustomerSubscribe = customerSubscribeDAO.save(tCustomerSubscribe);
		if (addBonus) {
			TCustomer tCustomer = customerDAO.findOne(customerId);
			if (tCustomer != null && StringUtils.isNotBlank(tCustomer.getFweixinId())) {
				TCustomerBonus invitationBonus = new TCustomerBonus();
				invitationBonus.setFcreateTime(now);
				invitationBonus.setFbonus(Integer.parseInt(DictionaryUtil.getCode(DictionaryUtil.BonusType, 30)));
				invitationBonus.setFcustermerId(fromCustomerId);
				invitationBonus.setFobject(tCustomerSubscribe.getId());
				invitationBonus.setFtype(30);
				customerBonusDAO.save(invitationBonus);
				customerInfoDAO.updatePoint(fromCustomerId,
						Integer.parseInt(DictionaryUtil.getCode(DictionaryUtil.BonusType, 30)));
			}
		}
		Map<String, Object> returnData = Maps.newHashMap();
		if (addBonus) {
			returnData.put("cityId", 1);
		} else {
			returnData.put("cityId", 2);
		}
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO
				.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.czyhInterface_BONUS, "czyhInterface.service.bonus.changeBonus.subscribe"));
		return responseDTO;
	}

	/**
	 * 用户关注或取消公众号赠送积分
	 * 
	 * @return
	 */
	public ResponseDTO reduceBonusCancel(String openid, Long subscribeTime) {

		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(openid)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("openid参数不能为空，请检查openid的传递参数值！");
			return responseDTO;
		}
		if (subscribeTime == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("subscribeTime参数不能为空，请检查subscribeTime的传递参数值！");
			return responseDTO;
		}

		TCustomer customer = customerDAO.findOneByOpenId(openid);
		String customerId = customer.getId();

		List<TCustomerSubscribe> tCustomerSubscribes = customerSubscribeDAO.getByCustomer(customerId);
		if (CollectionUtils.isEmpty(tCustomerSubscribes)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(204);
			responseDTO.setMsg("subscribeTime参数不能为空，请检查subscribeTime的传递参数值！");
			return responseDTO;
		}
		String fromCustomerId = tCustomerSubscribes.get(0).getFoperationId();

		TRelation relation = relationDAO.findByUser(fromCustomerId, customerId);
		TCustomerSubscribe customerSubscribe = customerSubscribeDAO.findOne(relation.getFcsid());
		// 在积分用户关注表中添加数据
		Date now = new Date();

		// 判断是否影响上线减过十分
		StringBuilder hql = new StringBuilder();
		hql.append("select b.id as id from TCustomerBonus b where b.fcustermerId in ").append(
				"(select s.foperationId as foperationId from TCustomerSubscribe s where s.fcustomerId = :customerId) and b.ftype = 40");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("customerId", customerId);
		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);
		if (CollectionUtils.isEmpty(list)) {
			TCustomerSubscribe tCustomerSubscribe = new TCustomerSubscribe();
			tCustomerSubscribe.setFcustomerId(customerId);
			tCustomerSubscribe.setFoperationId(fromCustomerId);
			tCustomerSubscribe.setFoperationTime(now);
			tCustomerSubscribe.setFtype(0);
			if (StringUtils.isNotBlank(customerSubscribe.getFgps())) {
				tCustomerSubscribe.setFgps(customerSubscribe.getFgps());
			}
			if (customerSubscribe.getFrisk() == 0) {
				tCustomerSubscribe.setFrisk(0);// 0是正常添加积分
			} else {
				tCustomerSubscribe.setFrisk(1);
			}
			tCustomerSubscribe = customerSubscribeDAO.save(tCustomerSubscribe);

			relation.setFcreateTime(now);
			relation.setFrelationType(2);// 0 取消
			relation.setFcsid(tCustomerSubscribe.getId());
			relationDAO.save(relation);

			// 减分则置为新用户
			if (customerSubscribe.getFrisk() == 1) {
				TSceneUser tSceneUser = sceneUserDAO.findOneByOpenid(openid);
				tSceneUser.setFbounsCustomer(0);
				sceneUserDAO.save(tSceneUser);
			}
			if (customerSubscribe.getFrisk() == 0) {
				// 如果是取消关注
				TCustomerBonus tCustomerBonus = new TCustomerBonus();
				tCustomerBonus.setFcreateTime(now);
				tCustomerBonus.setFbonus(Integer.parseInt(DictionaryUtil.getCode(DictionaryUtil.BonusType, 20)));
				tCustomerBonus.setFcustermerId(customerId);
				tCustomerBonus.setFobject(tCustomerSubscribe.getId());
				tCustomerBonus.setFtype(20);
				customerBonusDAO.save(tCustomerBonus);
				customerInfoDAO.updatePoint(customerId,
						Integer.parseInt(DictionaryUtil.getCode(DictionaryUtil.BonusType, 20)));
				TCustomerBonus invitationBonus = new TCustomerBonus();
				invitationBonus.setFcreateTime(now);
				invitationBonus.setFbonus(Integer.parseInt(DictionaryUtil.getCode(DictionaryUtil.BonusType, 40)));
				invitationBonus.setFcustermerId(fromCustomerId);
				invitationBonus.setFobject(tCustomerSubscribe.getId());
				invitationBonus.setFtype(40);
				customerBonusDAO.save(invitationBonus);
				customerInfoDAO.updatePoint(fromCustomerId,
						Integer.parseInt(DictionaryUtil.getCode(DictionaryUtil.BonusType, 40)));
			} else {
				TCustomerBonus tCustomerBonus = new TCustomerBonus();
				tCustomerBonus.setFcreateTime(now);
				tCustomerBonus.setFbonus(Integer.parseInt(DictionaryUtil.getCode(DictionaryUtil.BonusType, 20)));
				tCustomerBonus.setFcustermerId(customerId);
				tCustomerBonus.setFobject(tCustomerSubscribe.getId());
				tCustomerBonus.setFtype(20);
				customerBonusDAO.save(tCustomerBonus);
				customerInfoDAO.updatePoint(customerId,
						Integer.parseInt(DictionaryUtil.getCode(DictionaryUtil.BonusType, 20)));
			}
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg(
				PromptInfoUtil.getPrompt(PromptInfoUtil.czyhInterface_BONUS, "czyhInterface.service.bonus.changeBonus.unSubscribe"));
		return responseDTO;
	}

	public ResponseDTO runData() {

		ResponseDTO responseDTO = new ResponseDTO();

		List<String> ids = customerSubscribeDAO.findtFcustomerId();
		TRelation tRelation = null;
		for (String id : ids) {
			TCustomerSubscribe customerSubscribe = customerSubscribeDAO.findByCustomer(id).get(0);
			tRelation = relationDAO.findByUser(customerSubscribe.getFoperationId(), customerSubscribe.getFcustomerId());
			if (tRelation != null) {
				if (customerSubscribe.getFtype() == 1) {
					tRelation.setFrelationType(1);// 1 关注
				} else {
					tRelation.setFrelationType(2);
				}
				tRelation.setFcsid(customerSubscribe.getId());
				tRelation.setFcreateTime(customerSubscribe.getFoperationTime());
			} else {
				tRelation = new TRelation();
				tRelation.setFcustomerId(customerSubscribe.getFoperationId());
				tRelation.setFbyCustomerId(customerSubscribe.getFcustomerId());
				if (customerSubscribe.getFtype() == 1) {
					tRelation.setFrelationType(1);// 1 关注
				} else {
					tRelation.setFrelationType(2);
				}
				tRelation.setFcsid(customerSubscribe.getId());
				tRelation.setFcreateTime(customerSubscribe.getFoperationTime());
			}
			relationDAO.save(tRelation);
		}
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public void subtractStock(TEventBonus teventBonus, Date now, TOrderBonus tOrderBonus) {
		// 减去对应积分商城库存
		teventBonus.setFstock(teventBonus.getFstock() - 1);
		eventBonusDAO.save(teventBonus);
		// 添加兑换订单消耗积分记录
		TCustomerBonus tCustomerBonus = new TCustomerBonus();
		tCustomerBonus.setFcreateTime(now);
		tCustomerBonus.setFbonus(-teventBonus.getFbonus());
		tCustomerBonus.setFcustermerId(tOrderBonus.getFcustomerId());
		tCustomerBonus.setFobject(tOrderBonus.getId());
		tCustomerBonus.setFtype(50);
		customerBonusDAO.save(tCustomerBonus);
		// 更改用户总积分
		customerInfoDAO.updatePointAndUsePoint(tOrderBonus.getFcustomerId(), -teventBonus.getFbonus(),
				teventBonus.getFbonus());
	}

	public String wxPayConfirm(Notify notify) {

		String orderNum = notify.getOutTradeNo();
		TOrderBonus tOrderBonus = orderBonusDAO.getByFstatusAndForderNum(999, orderNum);
		if (tOrderBonus == null) {
			return "<xml><return_code>FAIL</return_code><return_msg>ORDER_NOT_EXIST</return_msg></xml>";
		}
		Date now = new Date();
		String returnStr = "<xml><return_code>SUCCESS</return_code></xml>";

		TWxPay tWxPay = wxPayDAO.getByOrderIdAndInOutAndStatus(tOrderBonus.getId(), 1, 10);
		if (tWxPay == null) {
			tWxPay = new TWxPay();
			tWxPay.setFinOut(1);
			tWxPay.setFcreateTime(now);
			tWxPay.setForderId(tOrderBonus.getId());
			tWxPay.setForderType(2);
			tWxPay.setTCustomer(new TCustomer(tOrderBonus.getFcustomerId()));
		}
		int status = 0;
		// isResultCodeSuccess()方法表示支付是否成功，是交易成功的标志
		if (notify.isResultCodeSuccess()) {
			int total = notify.getTotalFeeFen();
			if (total != 0) {
				BigDecimal payTotal = new BigDecimal(notify.getTotalFeeFen()).divide(new BigDecimal(100)).setScale(2,
						RoundingMode.HALF_UP);
				// 如果支付金额大于等于订单总价，表示是支付完成
				if (tOrderBonus.getFtotal().compareTo(payTotal) <= 0) {
					// 支付成功，更改订单状态为20（已支付）
					status = 10;
					// 微信支付表的状态为已支付
					tWxPay.setFstatus(30);
				} else {
					// 支付金额小于订单金额，更改订单状态为15（部分支付）
					status = 15;
					// 微信支付表的状态为部分支付
					tWxPay.setFstatus(20);
				}
				// 支付成功之后同步修改库存
				this.subtractStock(tOrderBonus.getTEventBonus(), now, tOrderBonus);

			}
		} else {
			// 支付失败，更改订单状态为11（支付失败）
			status = 999;
			// 微信支付表的状态为支付失败
			tWxPay.setFstatus(90);
			returnStr = "<xml><return_code>FAIL</return_code><return_msg>ORDER_NOT_EXIST</return_msg></xml>";
		}
		// 更改积分订单状态
		orderBonusDAO.updatePayStatus(status, tOrderBonus.getId());

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
	public void initOtherSerial() {
		String orderNum = orderBonusDAO.getMaxOrderNum(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH));
		if (StringUtils.isBlank(orderNum)) {
			NumberUtil.setOtherSerial(0L);
		} else {
			NumberUtil.setOtherSerial(Long.valueOf(orderNum.substring(8, 13)));
		}
	}

}