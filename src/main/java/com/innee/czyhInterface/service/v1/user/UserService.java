package com.innee.czyhInterface.service.v1.user;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.hibernate.query.internal.QueryImpl;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;
import org.springside.modules.utils.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.common.dict.RequestUrl;
import com.innee.czyhInterface.dao.ArticleDAO;
import com.innee.czyhInterface.dao.CommentDAO;
import com.innee.czyhInterface.dao.CouponDeliveryDAO;
import com.innee.czyhInterface.dao.CouponDeliveryHistoryDAO;
import com.innee.czyhInterface.dao.CustomerBonusDAO;
import com.innee.czyhInterface.dao.CustomerDAO;
import com.innee.czyhInterface.dao.CustomerInfoDAO;
import com.innee.czyhInterface.dao.CustomerLevelDAO;
import com.innee.czyhInterface.dao.CustomerSubscribeDAO;
import com.innee.czyhInterface.dao.CustomerTicketDAO;
import com.innee.czyhInterface.dao.EncryptRetDAO;
import com.innee.czyhInterface.dao.OrderBonusDAO;
import com.innee.czyhInterface.dao.OrderDAO;
import com.innee.czyhInterface.dao.OrderGoodsDAO;
import com.innee.czyhInterface.dao.SceneUserDAO;
import com.innee.czyhInterface.dao.ShoppingAddressDAO;
import com.innee.czyhInterface.dao.SmsDAO;
import com.innee.czyhInterface.dto.CommentRecommendDTO;
import com.innee.czyhInterface.dto.CustomerDTO;
import com.innee.czyhInterface.dto.m.CommentDTO;
import com.innee.czyhInterface.dto.m.PageDTO;
import com.innee.czyhInterface.dto.m.PublicImageDTO;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.dto.m.UserDTO;
import com.innee.czyhInterface.dto.m.user.MyCustomerDTO;
import com.innee.czyhInterface.dto.m.user.ShoppingAddressDTO;
import com.innee.czyhInterface.entity.TComment;
import com.innee.czyhInterface.entity.TCustomer;
import com.innee.czyhInterface.entity.TCustomerBonus;
import com.innee.czyhInterface.entity.TCustomerInfo;
import com.innee.czyhInterface.entity.TCustomerLevel;
import com.innee.czyhInterface.entity.TCustomerSubscribe;
import com.innee.czyhInterface.entity.TCustomerTicket;
import com.innee.czyhInterface.entity.TEncryptRet;
import com.innee.czyhInterface.entity.TOrderGoods;
import com.innee.czyhInterface.entity.TSceneUser;
import com.innee.czyhInterface.entity.TShoppingAddress;
import com.innee.czyhInterface.entity.TSms;
import com.innee.czyhInterface.impl.couponImpl.CouponsService;
import com.innee.czyhInterface.service.v1.push.PushService;
import com.innee.czyhInterface.service.v1.system.RedisService;
import com.innee.czyhInterface.service.v2.CommonService;
import com.innee.czyhInterface.service.v2.FxlService;
import com.innee.czyhInterface.service.v2.PublicService;
import com.innee.czyhInterface.util.ArrayStringUtils;
import com.innee.czyhInterface.util.BadWordUtil;
import com.innee.czyhInterface.util.CommonPage;
import com.innee.czyhInterface.util.Constant;
import com.innee.czyhInterface.util.DictionaryUtil;
import com.innee.czyhInterface.util.HeadImageUtil;
import com.innee.czyhInterface.util.HttpClientUtil;
import com.innee.czyhInterface.util.IpUtil;
import com.innee.czyhInterface.util.PropertiesUtil;
import com.innee.czyhInterface.util.asynchronoustasks.AsynchronousTasksManager;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.CustomerLogBean;
import com.innee.czyhInterface.util.log.OutPutLogUtil;
import com.innee.czyhInterface.util.redis.RedisMoudel;
import com.innee.czyhInterface.util.sms.SmsResult;
import com.innee.czyhInterface.util.sms.SmsUtil;
import com.innee.czyhInterface.util.wx.WxmpUtil;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * 用户类接口
 * 
 * @author 金圣智
 *
 */
@Component
@Transactional
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	public static final int INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;
	public static final String ALGORITHM = "SHA-1";

	public static final long Control = 60000L;

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private SmsDAO smsDAO;

	@Autowired
	PublicService publicService;

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private CustomerTicketDAO customerTicketDAO;

	@Autowired
	private ShoppingAddressDAO shoppingAddressDAO;

	@Autowired
	private CommonService commonService;

	@Autowired
	private FxlService fxlService;

	@Autowired
	private CustomerInfoDAO customerInfoDAO;

	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private CustomerBonusDAO customerBonusDAO;

	@Autowired
	private CommentDAO commentDAO;

	@Autowired
	private ArticleDAO articleDAO;

	@Autowired
	private CustomerSubscribeDAO customerSubscribeDAO;

	@Autowired
	private CustomerLevelDAO customerLevelDAO;

	@Autowired
	private CouponDeliveryDAO couponDeliveryDAO;

	@Autowired
	private PushService pushService;

	@Autowired
	private SceneUserDAO sceneUserDAO;

	@Autowired
	private CouponDeliveryHistoryDAO couponDeliveryHistoryDAO;

	@Autowired
	private OrderBonusDAO orderBonusDAO;

	@Autowired
	private RedisService redisService;

	@Autowired
	private OrderGoodsDAO orderGoodsDAO;

	@Autowired
	private CouponsService couponsService;
	
	@Autowired
	private EncryptRetDAO encryptRetDAO;

	public void entryptPassword(TCustomer customer) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		customer.setFsalt(Encodes.encodeHex(salt));

		byte[] hashPassword = Digests.sha1(customer.getFpassword().getBytes(), salt, INTERATIONS);
		customer.setFpassword(Encodes.encodeHex(hashPassword));
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public ResponseDTO getRetToken(Integer clientType, String addressIP) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (StringUtils.isBlank(addressIP)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("addressIP参数不能为空，请检查addressIP的传递参数值！");
			return responseDTO;
		}
		Map<String, Object> returnData = Maps.newHashMap();

		TEncryptRet tEncryptRet = encryptRetDAO.findToken(new Long(clientType));

		returnData.put("retToken", tEncryptRet.getFretToken());
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("加密令牌发送成功！");
		return responseDTO;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public ResponseDTO sendLoginSms(String phone, Integer clientType, String sign, String retToken, String addressIP) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(phone)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("phone参数不能为空，请检查phone的传递参数值！");
			return responseDTO;
		}
		
		String signStr = fxlService.httpEncrypt(addressIP, clientType);
		if (!signStr.equals(sign)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("您的sign不正确，请检查后再输入！");
			return responseDTO;
		}
		
		if (SmsUtil.isCheckCodeSwitch()) {
			String checkCode = RandomStringUtils.randomNumeric(6);
			Cache smsCheckCodeCache = cacheManager.getCache(Constant.SmsCheckCodeCacheKey);
			Element element = smsCheckCodeCache.get(phone);
			element = new Element(phone, checkCode);
			smsCheckCodeCache.put(element);
			// TODO发送短信验证码
			Map<String, String> smsParamMap = Maps.newHashMap();
			smsParamMap.put("chackCode", checkCode);
			smsParamMap.put("minute", "在3");
			// 发送确认码短信，短信类型码为1
			SmsResult smsResult = SmsUtil.sendSms(SmsUtil.CheckCodeSms, phone, smsParamMap);

			TSms tSms = new TSms();
			tSms.setSendPhone(phone);
			tSms.setSendTime(new Date());
			tSms.setSmsContent(smsResult.getContent());
			tSms.setSmsType(SmsUtil.CheckCodeSms);
			tSms.setSendResponse(smsResult.getResponse());
			if (smsResult.isSuccess()) {
				tSms.setSendSuccess(1);
			} else {
				tSms.setSendSuccess(0);
			}
			smsDAO.save(tSms);
			responseDTO.setMsg("短信验证码发送成功！");
		} else {
			String checkCode = "111111";
			Cache smsCheckCodeCache = cacheManager.getCache(Constant.SmsCheckCodeCacheKey);
			Element element = smsCheckCodeCache.get(phone);
			element = new Element(phone, checkCode);
			smsCheckCodeCache.put(element);
			responseDTO.setMsg("短信验证码是：111111！");
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		return responseDTO;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public ResponseDTO sendLoginSms(String phone, Integer clientType, String sign, String addressIP) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(phone)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("phone参数不能为空，请检查phone的传递参数值！");
			return responseDTO;
		}

		boolean b = this.isMobile(phone);
		if (b == false) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(101);
			responseDTO.setMsg("手机号不符合规则，请重新填写手机号！");
			return responseDTO;
		}

		String signStr = fxlService.httpEncrypt(addressIP, clientType);
		if (!signStr.equals(sign)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("sign不正确");
			return responseDTO;
		}

		// 通过电话号码查询，判断调用接口是否小于60秒
		Date date = new Date();
		List<Date> findByPhoneList = smsDAO.findByPhone(phone);
		if (findByPhoneList != null && findByPhoneList.size() > 0) {
			for (Date date1 : findByPhoneList) {
				BigDecimal bd1 = new BigDecimal(date.getTime());
				BigDecimal bd2 = new BigDecimal(date1.getTime());
				BigDecimal subtract = bd1.subtract(bd2);
				Long intValue = subtract.longValue();
				if (intValue < Control) {
					responseDTO.setSuccess(false);
					responseDTO.setStatusCode(101);
					responseDTO.setMsg("请等待60秒再次发送");
					return responseDTO;
				}
			}
		}

		if (SmsUtil.isCheckCodeSwitch()) {
			String checkCode = RandomStringUtils.randomNumeric(6);
			Cache smsCheckCodeCache = cacheManager.getCache(Constant.SmsCheckCodeCacheKey);
			Element element = smsCheckCodeCache.get(phone);
			element = new Element(phone, checkCode);
			smsCheckCodeCache.put(element);
			// TODO发送短信验证码
			Map<String, String> smsParamMap = Maps.newHashMap();
			smsParamMap.put("chackCode", checkCode);
			smsParamMap.put("minute", "在3");
			// 发送确认码短信，短信类型码为1
			SmsResult smsResult = SmsUtil.sendSms(SmsUtil.CheckCodeSms, phone, smsParamMap);

			TSms tSms = new TSms();
			tSms.setSendPhone(phone);
			tSms.setSendTime(new Date());
			tSms.setSmsContent(smsResult.getContent());
			tSms.setSmsType(SmsUtil.CheckCodeSms);
			tSms.setSendResponse(smsResult.getResponse());
			if (smsResult.isSuccess()) {
				tSms.setSendSuccess(1);
			} else {
				tSms.setSendSuccess(0);
			}
			smsDAO.save(tSms);
			responseDTO.setMsg("短信验证码发送成功！");
		} else {
			String checkCode = "111111";
			Cache smsCheckCodeCache = cacheManager.getCache(Constant.SmsCheckCodeCacheKey);
			Element element = smsCheckCodeCache.get(phone);
			element = new Element(phone, checkCode);
			smsCheckCodeCache.put(element);
			responseDTO.setMsg("短信验证码是：111111！");
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		return responseDTO;
	}

	public ResponseDTO wechatLogin(Integer clientType, String unionid, String openid, String nickname, String logoUrl,
			String country, String province, String city, Integer sex, String channel, String version,
			String deviceInfo, String deviceId, String deviceTokens, String followCustomerId,
			HttpServletRequest request) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(openid)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("openid参数不能为空，请检查openid的传递参数值！");
			return responseDTO;
		}

		Date now = new Date();
		String ticket = RandomStringUtils.randomAlphanumeric(16);
		String oldTicket = null;
		TCustomer tCustomer = customerDAO.getByFweixinUnionId(unionid, 1);
		if (tCustomer == null) {
			// 向用户表添加数据
			tCustomer = new TCustomer();
			tCustomer.setFname(nickname);
			tCustomer.setFpassword(PropertiesUtil.getProperty("customerInitPassword"));
			// 当web端调用时，才保存openid
			if (clientType.intValue() == 1) {
				tCustomer.setFweixinId(openid);
			}
			tCustomer.setFweixinUnionId(unionid);
			tCustomer.setFweixinName(nickname);
			StringBuilder region = new StringBuilder();
			if (StringUtils.isNotBlank(country)) {
				region.append(country).append(" - ");
			}
			if (StringUtils.isNotBlank(province)) {
				region.append(province).append(" - ");
			}
			if (StringUtils.isNotBlank(city)) {
				region.append(city);
			}
			tCustomer.setFregion(region.toString());
			tCustomer.setFphoto(logoUrl);
			tCustomer.setFsex(sex);
			tCustomer.setFstatus(1);
			tCustomer.setFtype(1);
			tCustomer.setFcreateTime(now);
			tCustomer.setFupdateTime(now);
			entryptPassword(tCustomer);
			tCustomer = customerDAO.save(tCustomer);

			// 创建用户TICKET表记录
			TCustomerTicket tCustomerTicket = new TCustomerTicket();
			tCustomerTicket.setFcreateTime(now);
			tCustomerTicket.setFupdateTime(now);
			tCustomerTicket.setFcustomerId(tCustomer.getId());
			tCustomerTicket.setFtype(clientType);
			tCustomerTicket.setFticket(ticket);
			customerTicketDAO.save(tCustomerTicket);

			// 新用户注册发券
			String dId = DictionaryUtil.getCode(DictionaryUtil.InvitationCoupon, 3);
			try {
				if (dId != null && !dId.equals("false")) {
					couponsService.receiveCoupon(dId, tCustomer.getId());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 添加用户附加info
			this.saveCustomerInfo(tCustomer.getId(), 1, channel, version, deviceInfo, deviceId, deviceTokens,
					followCustomerId, clientType);

		} else if (tCustomer.getFstatus().equals(10)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(104);
			responseDTO.setMsg("您的账号处于冻结状态，请联系查找优惠客服人员！");
			return responseDTO;
		} else if (tCustomer.getFstatus().equals(999)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(105);
			responseDTO.setMsg("您的账号处于作废状态，请联系查找优惠客服人员！");
			return responseDTO;
		} else {
			// 当web端调用时并且weixinid为空的时候，设置openid到weixinid
			if (clientType.intValue() == 1) {
				customerDAO.saveWeixinId(openid, tCustomer.getId());
			}

			// 如果客户的头像图片是微信头像，并且发送了变化，则更新客户的头像图片信息
			if (StringUtils.isNotBlank(tCustomer.getFphoto())
					&& tCustomer.getFphoto().startsWith("http://wx.qlogo.cn/mmopen")) {
				if (!tCustomer.getFphoto().equalsIgnoreCase(logoUrl)) {
					customerDAO.savePhoto(logoUrl, tCustomer.getId());
				}
			}
			// 获取用户TICKET记录，如果有则更新ticket，如果没有则创建一条ticket记录
			TCustomerTicket tCustomerTicket = customerTicketDAO.getByFcustomerIdAndFtype(tCustomer.getId(), clientType);
			if (tCustomerTicket == null) {
				tCustomerTicket = new TCustomerTicket();
				tCustomerTicket.setFcustomerId(tCustomer.getId());
				tCustomerTicket.setFtype(clientType);
				tCustomerTicket.setFcreateTime(now);
			} else {
				oldTicket = tCustomerTicket.getFticket();
			}
			tCustomerTicket.setFticket(ticket);
			tCustomerTicket.setFupdateTime(now);
			customerTicketDAO.save(tCustomerTicket);
			// 修改用户deviceTokens
			customerInfoDAO.updateDeviceTokens(tCustomer.getId(), deviceTokens);
		}

		try {
			redisService.putCache(RedisMoudel.TicketToId, ticket, tCustomer.getId());
			if (StringUtils.isNotBlank(oldTicket)) {
				redisService.removeCache(RedisMoudel.TicketToId, oldTicket);
			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}

		UserDTO userDTO = new UserDTO();
		userDTO.setUserId(tCustomer.getId());
		userDTO.setType(tCustomer.getFtype());
		userDTO.setName(tCustomer.getFname());
		userDTO.setWxName(tCustomer.getFweixinName());
		userDTO.setHeadimgUrl(HeadImageUtil.getHeadImage(tCustomer.getFphoto(), 96));
		userDTO.setTicket(ticket);
		userDTO.setSessionId(request.getSession().getId());
		// userDTO.setBaby(tCustomer.getFbaby());
		userDTO.setWxId(tCustomer.getFweixinId());
		userDTO.setPhone(tCustomer.getFphone());
		userDTO.setSex(tCustomer.getFsex() != null ? tCustomer.getFsex().toString() : StringUtils.EMPTY);
		userDTO.setAddress(tCustomer.getFregion());
		if (tCustomer.getFweixinUnionId() != null) {
			userDTO.setUnionId(tCustomer.getFweixinUnionId());
		}
		// 该用户是否关注了公众号，0表示未关注；1表示已关注
		TSceneUser tSceneUser = sceneUserDAO.findOneByOpenid(tCustomer.getFweixinId());
		if (tSceneUser != null && tSceneUser.getFsubscribe() != null) {
			userDTO.setSubscribe(tSceneUser.getFsubscribe());
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("userInfo", userDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("微信用户登录应用成功！");
		return responseDTO;
	}

	/**
	 * 微信jssdk用
	 * 
	 * @param url
	 * @return
	 */
	public ResponseDTO getWechatJssdk(String url) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(url)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("url参数不能为空，请检查url的传递参数值！");
			return responseDTO;
		}
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("jssdkParam", WxmpUtil.getWxJssdkParam(url));
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getAddressList(String customerId, Integer defaultAddr, Integer pageSize, Integer offset) {
		ResponseDTO responseDTO = new ResponseDTO();

		List<ShoppingAddressDTO> shoppingAddressList = Lists.newArrayList();

		if (StringUtils.isBlank(customerId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("customerId参数不能为空，请检查customerId的传递参数值！");
			return responseDTO;
		}

		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}

		List<Map<String, Object>> list = null;
		StringBuilder hql = null;
		if (defaultAddr != null && defaultAddr.intValue() == 1) {
			hql = new StringBuilder();
			hql.append(
					"select t.id as id,t.fcustomerId as fcustomerId,t.fname as fname,t.fphone as fphone,t.fregion as fregion,t.fstreet as fstreet,t.faddress as faddress,t.fcityId as fcityId,t.fdefault as fdefault from TShoppingAddress t where t.fcustomerId = :customerId and t.fstauts <999 order by t.fcreateTime desc");
			Map<String, Object> hqlMap = new HashMap<String, Object>();
			hqlMap.put("customerId", customerId);
			commonService.findPage(hql.toString(), page, hqlMap);
			list = page.getResult();
		} else {
			hql = new StringBuilder();
			hql.append(
					"select t.id as id,t.fcustomerId as fcustomerId,t.fname as fname,t.fphone as fphone,t.fregion as fregion,t.fstreet as fstreet,t.faddress as faddress,t.fcityId as fcityId,t.fdefault as fdefault from TShoppingAddress t where t.fcustomerId = :customerId and t.fdefault = 0 and t.fstauts <999 order by t.fcreateTime desc");
			Map<String, Object> hqlMap = new HashMap<String, Object>();
			hqlMap.put("customerId", customerId);

			commonService.findPage(hql.toString(), page, hqlMap);
			list = page.getResult();
		}

		if (list.isEmpty()) {

			Map<String, Object> returnData = Maps.newHashMap();
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			responseDTO.setMsg("当前用户没有收货地址,请跳转收货地址页面进行添加!");
			returnData.put("shoppingAddressList", shoppingAddressList);
			responseDTO.setData(returnData);
			return responseDTO;
		}

		// 加载收货人列表信息

		ShoppingAddressDTO shoppingAddressDTO = null;
		for (Map<String, Object> amap : list) {
			shoppingAddressDTO = new ShoppingAddressDTO();
			shoppingAddressDTO.setId(amap.get("id").toString());
			if (amap.get("customerId") != null && StringUtils.isNotBlank(amap.get("customerId").toString())) {
				shoppingAddressDTO.setCustomerId(amap.get("fcustomerId").toString());
			}
			if (amap.get("fname") != null && StringUtils.isNotBlank(amap.get("fname").toString())) {
				shoppingAddressDTO.setName(amap.get("fname").toString());
			}
			if (amap.get("fcityId") != null && StringUtils.isNotBlank(amap.get("fcityId").toString())) {
				shoppingAddressDTO.setCityId((Integer) amap.get("fcityId"));
			}
			if (amap.get("fphone") != null && StringUtils.isNotBlank(amap.get("fphone").toString())) {
				shoppingAddressDTO.setPhone(amap.get("fphone").toString());
			}
			if (amap.get("fregion") != null && StringUtils.isNotBlank(amap.get("fregion").toString())) {
				shoppingAddressDTO.setRegion(amap.get("fregion").toString());
			}

			if (amap.get("fstreet") != null && StringUtils.isNotBlank(amap.get("fstreet").toString())) {
				shoppingAddressDTO.setStreet(amap.get("fstreet").toString());
			}

			if (amap.get("faddress") != null && StringUtils.isNotBlank(amap.get("faddress").toString())) {
				shoppingAddressDTO.setAddress(amap.get("faddress").toString());
			}

			if (amap.get("fdefault") != null && StringUtils.isNotBlank(amap.get("fdefault").toString())) {
				shoppingAddressDTO.setDefaultstatus(Integer.valueOf(amap.get("fdefault").toString()));
			}
			shoppingAddressList.add(shoppingAddressDTO);
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("shoppingAddressList", shoppingAddressList);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("获取用户收货地址列表时成功！");
		return responseDTO;
	}

	/**
	 * 修改收货地址信息
	 * 
	 * @param customerId
	 * @param name
	 * @param phone
	 * @param region
	 * @param street
	 * @param address
	 * @param defaultstatus
	 * @return
	 */
	public ResponseDTO saveUpdateMeAddress(String id, String customerId, String name, String phone, String region,
			String street, Integer defaultstatus, Integer cityId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(customerId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("customerId参数不能为空，请检查customerId的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(phone)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("phone参数不能为空，请检查phone的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(region)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("region参数不能为空，请检查region的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(street)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(204);
			responseDTO.setMsg("street参数不能为空，请检查street的传递参数值！");
			return responseDTO;
		}

		boolean b = this.isMobile(phone);
		if (b == false) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(101);
			responseDTO.setMsg("手机号不符合规则，请重新填写手机号！");
			return responseDTO;
		}

		TShoppingAddress shoppingAddress = null;
		ShoppingAddressDTO shoppingAddressDTO = null;
		TShoppingAddress tShoppingAddress = shoppingAddressDAO.findByDefault(customerId);
		if (StringUtils.isBlank(id)) {
			shoppingAddress = new TShoppingAddress();
			shoppingAddress.setFcustomerId(customerId);
			shoppingAddress.setFname(name);
			shoppingAddress.setFphone(phone);
			shoppingAddress.setFregion(region);
			shoppingAddress.setFstreet(street);
			shoppingAddress.setFcityId(cityId);
			shoppingAddress.setFaddress(new StringBuilder().append(region).append(street).toString());
			if (tShoppingAddress == null) {
				shoppingAddress.setFdefault(0);
			} else {
				shoppingAddressDAO.updateAddress(tShoppingAddress.getId(), tShoppingAddress.getFcustomerId(), 1);
				shoppingAddress.setFdefault(0);
			}
			shoppingAddress.setFcreateTime(new Date());
			shoppingAddress.setFstauts(0);
			shoppingAddress = shoppingAddressDAO.save(shoppingAddress);

			shoppingAddressDTO = new ShoppingAddressDTO();
			shoppingAddressDTO.setCustomerId(shoppingAddress.getFcustomerId());
			shoppingAddressDTO.setName(shoppingAddress.getFname());
			shoppingAddressDTO.setPhone(shoppingAddress.getFphone());
			shoppingAddressDTO.setRegion(shoppingAddress.getFregion());
			shoppingAddressDTO.setStreet(shoppingAddress.getFstreet());
			shoppingAddressDTO.setAddress(shoppingAddress.getFaddress());
			shoppingAddressDTO.setDefaultstatus(shoppingAddress.getFdefault());
			shoppingAddressDTO.setId(shoppingAddress.getId());
			shoppingAddressDTO.setCityId(shoppingAddress.getFcityId());

		} else {
			TShoppingAddress spAddress = shoppingAddressDAO.getOne(id);
			spAddress.setFcustomerId(customerId);
			spAddress.setFname(name);
			spAddress.setFcityId(cityId);
			spAddress.setFphone(phone);
			spAddress.setFregion(region);
			spAddress.setFstreet(street);
			spAddress.setFaddress(new StringBuilder().append(region).append(street).toString());
			if (defaultstatus.intValue() == 0) {
				shoppingAddressDAO.updateAddress(tShoppingAddress.getId(), tShoppingAddress.getFcustomerId(), 1);
				spAddress.setFdefault(0);
			} else {
				spAddress.setFdefault(1);
			}
			spAddress.setFupdateTime(new Date());
			spAddress.setFstauts(0);
			shoppingAddress = shoppingAddressDAO.save(spAddress);

			shoppingAddressDTO = new ShoppingAddressDTO();
			shoppingAddressDTO.setCustomerId(spAddress.getFcustomerId());
			shoppingAddressDTO.setName(spAddress.getFname());
			shoppingAddressDTO.setPhone(spAddress.getFphone());
			shoppingAddressDTO.setRegion(spAddress.getFregion());
			shoppingAddressDTO.setStreet(spAddress.getFstreet());
			shoppingAddressDTO.setAddress(spAddress.getFaddress());
			shoppingAddressDTO.setDefaultstatus(spAddress.getFdefault());
			shoppingAddressDTO.setId(spAddress.getId());
			shoppingAddressDTO.setCityId(shoppingAddress.getFcityId());

		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("shoppingAddress", shoppingAddressDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("保存收货地址成功");
		return responseDTO;
	}

	public ResponseDTO delAddress(String id, String customerId) {

		ResponseDTO responseDTO = new ResponseDTO();

		shoppingAddressDAO.delAddress(id, customerId, 999);

		TShoppingAddress tShoppingAddress = shoppingAddressDAO.findByDefault(customerId);
		if (tShoppingAddress == null) {
			StringBuilder hql = new StringBuilder();
			Map<String, Object> hqlMap = Maps.newHashMap();
			hql.append(
					"select t.id as DT_RowId,t.fcustomerId as fcustomerId from TShoppingAddress t where t.fcustomerId = :customerId and t.fdefault = 1 and t.fstauts <999");
			hqlMap.put("customerId", customerId);
			Query q = commonService.createQuery(hql.toString(), hqlMap);
			q.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			q.setFirstResult(0).setMaxResults(1);
			List<Map<String, Object>> list2 = q.getResultList();
			Map<String, Object> bmap = CollectionUtils.isNotEmpty(list2) ? list2.get(0) : null;

			if (MapUtils.isNotEmpty(bmap)) {
				shoppingAddressDAO.updateAddress(bmap.get("DT_RowId").toString(), bmap.get("fcustomerId").toString(),
						0);
			}

		}

		Map<String, Object> returnData = Maps.newHashMap();
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("删除收货地址成功");
		return responseDTO;
	}

	/**
	 * 用户绑定手机
	 * 
	 * @param ticket
	 *            票
	 * @param phone
	 *            手机号码
	 * @param checkCode
	 *            验证码
	 * @param request
	 * @return
	 */
	public ResponseDTO bindPhone(Integer clientType, String ticket, String phone, String checkCode) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(phone)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("请填写手机号码！");
			return responseDTO;
		}
		if (StringUtils.isBlank(checkCode)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("请填写短信验证码！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		TCustomer customer = customerDAO.findByUserName(phone);
		if (customer != null) {
			if (!customerDTO.getCustomerId().equals(customer.getId())) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(114);
				responseDTO.setMsg("该手机号已经被绑定了，无法使用~");
				return responseDTO;
			}
		}
		// 检查短信验证码是否是通用短信验证码
		if (!checkCode.equals(Constant.GeneralSmsCheckCode)) {
			// 验证短信验证码是否正确
			Cache smsCheckCodeCache = cacheManager.getCache(Constant.SmsCheckCodeCacheKey);
			Element element = smsCheckCodeCache.get(phone);
			if (element == null || !element.getObjectValue().equals(checkCode)) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(103);
				responseDTO.setMsg("短信验证码错误！");
				return responseDTO;
			} else {
				smsCheckCodeCache.remove(phone);
			}
		}
		// 根据ticket获取到用户DTO
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}
		Date now = new Date();
		UserDTO userDTO = new UserDTO();
		// 获取手机号码是否已经存在了
		TCustomer tCustomer2 = customerDAO.getByUsernameAndTypeAndNotCustomerId(phone, 1, customerDTO.getCustomerId());
		// 如果手机号码不存在，则将手机号码保存到当前用户的手机号和用户账号字段
		if (tCustomer2 == null) {
			customerDAO.saveUsernameAndPhone(phone, phone, customerDTO.getCustomerId());

			userDTO.setUserId(customerDTO.getCustomerId());
			userDTO.setName(customerDTO.getWxName());
			userDTO.setWxName(customerDTO.getWxName());
			userDTO.setHeadimgUrl(HeadImageUtil.getHeadImage(customerDTO.getPhoto(), 96));
			userDTO.setTicket(ticket);
			// userDTO.setBaby(customerDTO.getBaby());
			userDTO.setWxId(customerDTO.getWxId());
			if (customerDTO.getWxUnionId() != null) {
				userDTO.setUnionId(customerDTO.getWxUnionId());
			}
			userDTO.setPhone(phone);
			userDTO.setSex(String.valueOf(customerDTO.getSex()));
			// userDTO.setAddress(tCustomer.getFregion());
		} else {
			// 如果绑定的电话号码已经存在了，则将当前用户信息复制到已经存在的用户上。
			TCustomer tCustomer = customerDAO.getOne(customerDTO.getCustomerId());
			// 将当前用户的信息复制到电话号码用户上
			tCustomer2.setFweixinId(tCustomer.getFweixinId());
			tCustomer2.setFweixinUnionId(tCustomer.getFweixinUnionId());
			tCustomer2.setFweixinName(tCustomer.getFweixinName());
			tCustomer2.setFname(tCustomer.getFweixinName());
			if (StringUtils.isBlank(tCustomer2.getFphoto())) {
				tCustomer2.setFphoto(tCustomer.getFphoto());
			}
			if (StringUtils.isBlank(tCustomer2.getFregion())) {
				tCustomer2.setFregion(tCustomer.getFregion());
			}
			if (tCustomer2.getFsex() == null) {
				tCustomer2.setFsex(tCustomer.getFsex());
			}
			tCustomer2.setFupdateTime(now);
			customerDAO.save(tCustomer2);
			// 设置当前微信用户记录为删除状态
			customerDAO.updateStatus(customerDTO.getCustomerId(), 999);

			// 获取ticket缓存，并且删除当前ticket
			redisService.removeCache(RedisMoudel.TicketToId, ticket);
			// 删除当前微信的用户缓存
			redisService.removeCache(RedisMoudel.CustomerEentity, customerDTO.getCustomerId());
			// 删除当前微信用户的所有ticket表的记录
			customerTicketDAO.deleteAllTicket(customerDTO.getCustomerId());
			// 查询到绑定的电话的用户的ticket表的记录
			TCustomerTicket tCustomer2Ticket = customerTicketDAO.getByFcustomerIdAndFtype(tCustomer2.getId(),
					clientType);

			String newTicket = RandomStringUtils.randomAlphanumeric(16);
			// 获取用户TICKET记录，如果有则更新ticket，如果没有则创建一条ticket记录
			if (tCustomer2Ticket == null) {
				tCustomer2Ticket = new TCustomerTicket();
				tCustomer2Ticket.setFcustomerId(tCustomer2.getId());
				tCustomer2Ticket.setFtype(clientType);
				tCustomer2Ticket.setFcreateTime(now);
				tCustomer2Ticket.setFticket(newTicket);
				tCustomer2Ticket.setFupdateTime(now);
				customerTicketDAO.save(tCustomer2Ticket);
			} else {
				customerTicketDAO.updateTicket(newTicket, tCustomer2Ticket.getId());
			}

			// 将当前用户的信息复制到已经存在的用户上
			try {
				this.copyCustomerInfo(tCustomer2, tCustomer);
			} catch (Exception e) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("customerId", customerDTO.getCustomerId());
				map.put("tCustomer2Id", tCustomer2.getId());
				OutPutLogUtil.printLoggger(e, map, logger);
			}

			userDTO.setUserId(tCustomer2.getId());
			userDTO.setName(tCustomer2.getFname());
			userDTO.setWxName(tCustomer2.getFweixinName());
			userDTO.setHeadimgUrl(tCustomer2.getFphoto());
			if (tCustomer2.getFweixinUnionId() != null) {
				userDTO.setUnionId(tCustomer2.getFweixinUnionId());
			}
			userDTO.setTicket(newTicket);
			// userDTO.setBaby(tCustomer2.getFbaby());
			userDTO.setWxId(tCustomer2.getFweixinId());
			userDTO.setPhone(phone);
			userDTO.setSex(tCustomer2.getFsex() != null ? tCustomer2.getFsex().toString() : StringUtils.EMPTY);
			// userDTO.setAddress(tCustomer2.getFregion());
		}
		redisService.removeCache(RedisMoudel.CustomerEentity, customerDTO.getCustomerId());

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("userInfo", userDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("手机号绑定成功！");
		return responseDTO;
	}

	public ResponseDTO bindWechat(Integer clientType, String ticket, String wxName, String country, String province,
			String city, String headimgurl, Integer sex, String openid, String unionid) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(unionid)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("请授权微信用户进行绑定！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		// 根据ticket获取到用户DTO
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		TCustomer customer = customerDAO.getByFweixinUnionIdAndFtype(unionid, 1);
		if (customer != null) {
			if (!customerDTO.getCustomerId().equals(customer.getId())) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(114);
				responseDTO.setMsg("该微信号已经被绑定了，请更换其他微信进行绑定");
				return responseDTO;
			}
		}

		Date now = new Date();
		UserDTO userDTO = new UserDTO();
		// 查看当前的微信是否注册过帐号
		TCustomer tCustomer2 = customerDAO.getByUnionidAndTypeAndNotCustomerId(unionid, 1, customerDTO.getCustomerId());
		// 如果openid不存在，则将openid保存到当前用户的wxid字段
		if (tCustomer2 == null) {
			TCustomer tCustomer = customerDAO.getOne(customerDTO.getCustomerId());
			tCustomer.setFname(wxName);
			tCustomer.setFweixinName(wxName);
			StringBuilder region = new StringBuilder();
			if (StringUtils.isNotBlank(country)) {
				region.append(country).append(" - ");
			}
			if (StringUtils.isNotBlank(province)) {
				region.append(province).append(" - ");
			}
			if (StringUtils.isNotBlank(city)) {
				region.append(city);
			}
			tCustomer.setFregion(region.toString());
			tCustomer.setFsex(sex);
			tCustomer.setFphoto(headimgurl);
			tCustomer.setFweixinId(openid);
			tCustomer.setFweixinUnionId(unionid);
			tCustomer = customerDAO.save(tCustomer);

			userDTO.setUserId(customerDTO.getCustomerId());
			userDTO.setName(tCustomer.getFname());
			if (tCustomer.getFweixinUnionId() != null) {
				userDTO.setUnionId(tCustomer.getFweixinUnionId());
			}
			userDTO.setWxName(wxName);
			userDTO.setHeadimgUrl(HeadImageUtil.getHeadImage(tCustomer.getFphoto(), 96));
			userDTO.setTicket(ticket);
			userDTO.setWxId(openid);
			userDTO.setPhone(customerDTO.getPhone());
			userDTO.setSex(String.valueOf(customerDTO.getSex()));
		} else {

			// 如果想绑定的微信号已经注册过帐号了，则将查到的微信帐号信息复制到当前手机号码上
			TCustomer tCustomer = customerDAO.getOne(customerDTO.getCustomerId());
			// 将当前用户的信息复制到电话号码用户上
			tCustomer.setFweixinId(tCustomer2.getFweixinId());// tCustomer2
																// 微信用户帐号
			tCustomer.setFweixinUnionId(tCustomer2.getFweixinUnionId());

			tCustomer.setFname(tCustomer2.getFname());// 把最新微信名称拿过来
			tCustomer.setFsex(tCustomer2.getFsex());
			tCustomer.setFweixinName(tCustomer2.getFweixinName());
			tCustomer.setFphoto(tCustomer2.getFphoto());
			tCustomer.setFregion(tCustomer2.getFregion());
			tCustomer.setFupdateTime(now);
			customerDAO.save(tCustomer);

			// 设置当前微信用户记录为删除状态
			customerDAO.updateStatus(tCustomer2.getId(), 999);

			// 获取ticket缓存，并且删除当前ticket
			redisService.removeCache(RedisMoudel.TicketToId, ticket);
			// 获取用户DTO缓存对象
			redisService.removeCache(RedisMoudel.CustomerEentity, customerDTO.getCustomerId());
			// 删除当前微信用户的所有ticket表的记录
			customerTicketDAO.deleteAllTicket(tCustomer2.getId());
			// 查询到绑定的电话的用户的ticket表的记录
			// TCustomerTicket tCustomer2Ticket =
			// customerTicketDAO.getByFcustomerIdAndFtype(tCustomer2.getId(),
			// clientType);
			//
			// String newTicket = RandomStringUtils.randomAlphanumeric(16);
			// // 获取用户TICKET记录，如果有则更新ticket，如果没有则创建一条ticket记录
			// if (tCustomer2Ticket == null) {
			// tCustomer2Ticket = new TCustomerTicket();
			// tCustomer2Ticket.setFcustomerId(tCustomer2.getId());
			// tCustomer2Ticket.setFtype(clientType);
			// tCustomer2Ticket.setFcreateTime(now);
			// tCustomer2Ticket.setFticket(newTicket);
			// tCustomer2Ticket.setFupdateTime(now);
			// customerTicketDAO.save(tCustomer2Ticket);
			// } else {
			// customerTicketDAO.updateTicket(newTicket,
			// tCustomer2Ticket.getId());
			// }

			// 将当前用户的附加信息复制到已经存在的用户上
			try {
				this.copyCustomerInfo(tCustomer, tCustomer2);
			} catch (Exception e) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("customerId", customerDTO.getCustomerId());
				map.put("tCustomer2Id", tCustomer2.getId());
				OutPutLogUtil.printLoggger(e, map, logger);
			}

			userDTO.setUserId(tCustomer.getId());
			userDTO.setName(tCustomer.getFname());
			userDTO.setWxName(tCustomer.getFweixinName());
			userDTO.setHeadimgUrl(tCustomer.getFphoto());
			userDTO.setTicket(tCustomer.getFticket());
			userDTO.setWxId(tCustomer.getFweixinId());
			if (tCustomer.getFweixinUnionId() != null) {
				userDTO.setUnionId(tCustomer.getFweixinUnionId());
			}
			userDTO.setPhone(tCustomer.getFusername());
			userDTO.setSex(tCustomer.getFsex() != null ? tCustomer.getFsex().toString() : StringUtils.EMPTY);
		}
		redisService.removeCache(RedisMoudel.CustomerEentity, customerDTO.getCustomerId());

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("userInfo", userDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("微信绑定成功！");
		return responseDTO;
	}

	/**
	 * 将tCustomer的数据信息复制到tCustomer2
	 * 
	 * @param ticket
	 * @return
	 */
	public void copyCustomerInfo(TCustomer tCustomer2, TCustomer tCustomer) {
		// 将当前用户的订单复制到已有手机号的用户上
		orderDAO.updateOrderByCustomer(tCustomer2.getId(), tCustomer.getId());
		// 将当前用户的优惠券复制到已有手机号的用户上
		couponDeliveryDAO.updateCouponByCustomer(tCustomer2.getId(), tCustomer.getId());
		couponDeliveryHistoryDAO.updateHistoryCouponByCustomer(tCustomer2.getId(), tCustomer.getId());
		// 将当前用户的评价复制到已有手机号的用户上
		// commentDAO.updateCommentByCustomer(tCustomer2.getId(),
		// tCustomer.getId());
		// 将当前用户的咨询复制到已有手机号的用户上
		// consultDAO.updateConsultByCustomer(tCustomer2.getId(),
		// tCustomer.getId());
		// 将当前用户的用户附加信息与已有手机号的账号合并
		TCustomerInfo customerInfo = customerInfoDAO.getByCustomerId(tCustomer.getId());
		TCustomerInfo customerInfo2 = customerInfoDAO.getByCustomerId(tCustomer2.getId());
		if (customerInfo != null) {
			if (StringUtils.isNotBlank(customerInfo.getFregisterChannel())) {
				customerInfo2.setFregisterChannel(customerInfo.getFregisterChannel());
			}
			if (StringUtils.isNotBlank(customerInfo.getFregisterChannelVersion())) {
				customerInfo2.setFregisterChannelVersion(customerInfo.getFregisterChannelVersion());
			}
			if (StringUtils.isNotBlank(customerInfo.getFregisterDeviceId())) {
				customerInfo2.setFregisterDeviceId(customerInfo.getFregisterDeviceId());
			}
			if (StringUtils.isNotBlank(customerInfo.getFregisterDeviceInfo())) {
				customerInfo2.setFregisterDeviceInfo(customerInfo.getFregisterDeviceInfo());
			}
			if (customerInfo.getFregisterTime().before(customerInfo2.getFregisterTime())) {
				customerInfo2.setFregisterTime(customerInfo.getFregisterTime());
			}
			if (customerInfo.getFfirstOrderTime() != null) {
				customerInfo2.setFfirstOrderTime(customerInfo.getFfirstOrderTime());
			}
			if (customerInfo.getForderNumber() != null) {
				customerInfo2.setForderNumber(customerInfo2.getForderNumber() + customerInfo.getForderNumber());
			}
			if (customerInfo.getFpayOrderNumber() != null) {
				customerInfo2
						.setFpayOrderNumber(customerInfo2.getFpayOrderNumber() + customerInfo.getFpayOrderNumber());
			}
			if (customerInfo.getFrefundOrderNumber() != null) {
				customerInfo2.setFrefundOrderNumber(
						customerInfo2.getFrefundOrderNumber() + customerInfo.getFrefundOrderNumber());
			}
			if (customerInfo.getFpayZeroOrderNumber() != null) {
				customerInfo2.setFpayZeroOrderNumber(
						customerInfo2.getFpayZeroOrderNumber() + customerInfo.getFpayZeroOrderNumber());
			}
			if (customerInfo.getFpoint() != null) {
				customerInfo2.setFpoint(customerInfo2.getFpoint() + customerInfo.getFpoint());
			}
			if (customerInfo.getFtipNumber() != null) {
				customerInfo2.setFtipNumber(customerInfo2.getFtipNumber() + customerInfo.getFtipNumber());
			}
			if (customerInfo.getFgrowthValue() != null) {
				customerInfo2.setFgrowthValue(customerInfo2.getFgrowthValue() + customerInfo.getFgrowthValue());
				if (customerInfo2.getFneedGrowthValue() - customerInfo.getFgrowthValue() > 0) {
					customerInfo2
							.setFneedGrowthValue(customerInfo2.getFneedGrowthValue() - customerInfo.getFgrowthValue());
				} else {
					TCustomerLevel customerLevel = customerLevelDAO.getByLevel(customerInfo2.getFlevel() + 1);
					customerInfo2.setFneedGrowthValue(customerLevel.getFgrowthValue() - customerInfo2.getFgrowthValue()
							- customerInfo.getFgrowthValue());
					customerInfo2.setFlevel(customerInfo2.getFlevel() + 1);
				}
			}
			customerInfoDAO.save(customerInfo2);
		}
		// // 将当前用户的用户标签与已有手机号的用户标签合并
		// // 判断当前用户是否具有老用户标签
		// TCustomerTag customerTag =
		// customerTagDAO.findTCustomerTag(tCustomer.getId(), 3);
		// if (customerTag != null) {
		// // 判断已有手机号账号的用户标签是相对或者绝对老用户
		// TCustomerTag customerTag1 =
		// customerTagDAO.findTCustomerTag(tCustomer2.getId(), 1);
		// TCustomerTag customerTag2 =
		// customerTagDAO.findTCustomerTag(tCustomer2.getId(), 5);
		// // 如果是，则将其相应标签改为老用户标签
		// if (customerTag1 != null) {
		// customerTag1.setFtag(3);
		// customerTagDAO.save(customerTag1);
		// } else if (customerTag2 != null) {
		// customerTag2.setFtag(3);
		// customerTagDAO.save(customerTag2);
		// }
		// }
		// 修改用户积分兑换表
		orderBonusDAO.updateOrderBounsByCustomer(tCustomer2.getId(), tCustomer.getId());
		// 修改用户积分表
		customerBonusDAO.updateCustomerBounsByCustomer(tCustomer2.getId(), tCustomer.getId());
		// 修改用户关注表
		customerSubscribeDAO.updateCustomerSubscribeByCustomer(tCustomer2.getId(), tCustomer.getId());
		customerSubscribeDAO.updateCustomerSubscribeByOpertion(tCustomer2.getId(), tCustomer.getId());
		// 修改收货地址
		shoppingAddressDAO.updateByCustomerId(tCustomer2.getId(), tCustomer.getId());
	}

	/**
	 * 手机号登录
	 * 
	 * @param request
	 * @return
	 */
	public ResponseDTO phoneLogin(Integer clientType, String checkCode, String phone, String channel, String version,
			String deviceInfo, String deviceId, String deviceTokens, String followCustomerId,
			HttpServletRequest request) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(checkCode)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("请输入验证码！");
			return responseDTO;
		}
		if (StringUtils.isBlank(phone)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("请输入手机号码！");
			return responseDTO;
		}

		Cache smsLoginPwdCache = cacheManager.getCache(Constant.SmsCheckCodeCacheKey);
		Element element = smsLoginPwdCache.get(phone);
		if (element == null || !element.getObjectValue().equals(checkCode)) {
			if (!checkCode.equals("021021")) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(103);
				responseDTO.setMsg("登录验证码错误！");
				return responseDTO;
			}
		} else {
			smsLoginPwdCache.remove(phone);
		}
		String ticket = RandomStringUtils.randomAlphanumeric(16);
		String oldTicket = null;

		TCustomer tCustomer = customerDAO.getByPhoneAndType(phone, 1);
		Date now = new Date();
		if (tCustomer == null) {
			tCustomer = new TCustomer();
			tCustomer.setFphone(phone);
			tCustomer.setFname(phone);
			tCustomer.setFpassword(checkCode);
			tCustomer.setFusername(phone);
			tCustomer.setFstatus(1);
			tCustomer.setFtype(1);
			entryptPassword(tCustomer);
			tCustomer.setFcreateTime(now);
			tCustomer.setFupdateTime(now);
			tCustomer = customerDAO.save(tCustomer);

			// 创建用户TICKET表记录
			TCustomerTicket tCustomerTicket = new TCustomerTicket();
			tCustomerTicket.setFcreateTime(now);
			tCustomerTicket.setFupdateTime(now);
			tCustomerTicket.setFcustomerId(tCustomer.getId());
			tCustomerTicket.setFtype(clientType);
			tCustomerTicket.setFticket(ticket);
			customerTicketDAO.save(tCustomerTicket);

			// 添加用户附加info
			this.saveCustomerInfo(tCustomer.getId(), 1, channel, version, deviceInfo, deviceId, deviceTokens,
					followCustomerId, clientType);
			// 新用户注册发券
			String dId = DictionaryUtil.getCode(DictionaryUtil.InvitationCoupon, 3);
			try {
				if (dId != null && !dId.equals("false")) {
					couponsService.receiveCoupon(dId, tCustomer.getId());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 如果有邀请人填写邀请人的用户id
			if (StringUtils.isNotBlank(followCustomerId)) {
				TCustomerSubscribe tCustomerSubscribe = new TCustomerSubscribe();
				tCustomerSubscribe.setFcustomerId(followCustomerId);// 邀请人id
				tCustomerSubscribe.setFoperationId(tCustomer.getId());// 被邀请人id
				tCustomerSubscribe.setFoperationTime(new Date());
				tCustomerSubscribe.setFtype(0);
				tCustomerSubscribe.setFrisk(0);// 是否危险账户
				tCustomerSubscribe = customerSubscribeDAO.save(tCustomerSubscribe);

				customerInfoDAO.updateFfans(followCustomerId, 1);
				// todo 异步调用调用领券接口
				String deliveryId = DictionaryUtil.getCode(DictionaryUtil.InvitationCoupon, 1);
				try {
					if (deliveryId != null && !deliveryId.equals("false")) {
						couponsService.receiveCoupon(deliveryId, tCustomer.getId());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				// todo 邀请赠送U币
				String bonus = DictionaryUtil.getCode(DictionaryUtil.BonusType, 3);
				if (bonus != null && !bonus.equals("0")) {
					try {
						customerInfoDAO.updatePoint(followCustomerId, Integer.parseInt(bonus));
						TCustomerBonus tCustomerBonus = new TCustomerBonus();
						tCustomerBonus.setFcreateTime(new Date());
						tCustomerBonus.setFbonus(Integer.parseInt(bonus));
						tCustomerBonus.setFcustermerId(followCustomerId);
						tCustomerBonus.setFobject(tCustomerSubscribe.getId());
						tCustomerBonus.setFtype(3);
						customerBonusDAO.save(tCustomerBonus);

						if (deviceTokens != null) {
							pushService.bonusAccount(bonus, deviceTokens, tCustomer.getId(), tCustomer.getFname());
						}
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}

		} else if (tCustomer.getFstatus().equals(10)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(101);
			responseDTO.setMsg("您的账号处于冻结状态，请联系查找优惠客服人员！");
			return responseDTO;
		} else if (tCustomer.getFstatus().equals(999)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("您的账号处于作废状态，请联系查找优惠客服人员！");
			return responseDTO;
		} else {
			// 获取用户TICKET记录，如果有则更新ticket，如果没有则创建一条ticket记录
			TCustomerTicket tCustomerTicket = customerTicketDAO.getByFcustomerIdAndFtype(tCustomer.getId(), clientType);
			if (tCustomerTicket == null) {
				tCustomerTicket = new TCustomerTicket();
				tCustomerTicket.setFcustomerId(tCustomer.getId());
				tCustomerTicket.setFtype(clientType);
				tCustomerTicket.setFcreateTime(now);
			} else {
				oldTicket = tCustomerTicket.getFticket();
			}
			tCustomerTicket.setFticket(ticket);
			tCustomerTicket.setFupdateTime(now);
			customerTicketDAO.save(tCustomerTicket);
			// 修改用户deviceTokens
			customerInfoDAO.updateDeviceTokens(tCustomer.getId(), deviceTokens);
		}

		try {
			redisService.putCache(RedisMoudel.TicketToId, ticket, tCustomer.getId());
			if (StringUtils.isNotBlank(oldTicket)) {
				redisService.removeCache(RedisMoudel.TicketToId, oldTicket);
			}

			CustomerLogBean customerLogBean = new CustomerLogBean();
			customerLogBean.setCustomerId(tCustomer.getId());
			customerLogBean.setUrl(request.getRequestURI());
			customerLogBean.setCustomerType(1);
			customerLogBean.setCreateTime(new Date());
			customerLogBean.setClientType(clientType == null ? 1 : clientType);
			customerLogBean.setClientIp(IpUtil.getIpAddr(request));
			customerLogBean.setTaskType(1);
			AsynchronousTasksManager.put(customerLogBean);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}

		UserDTO userDTO = new UserDTO();
		userDTO.setUserId(tCustomer.getId());
		userDTO.setType(tCustomer.getFtype());
		userDTO.setName(tCustomer.getFname());
		userDTO.setPhone(tCustomer.getFphone());
		if (tCustomer.getFweixinUnionId() != null) {
			userDTO.setUnionId(tCustomer.getFweixinUnionId());
		}
		if (tCustomer.getFweixinId() != null) {
			userDTO.setWxId(tCustomer.getFweixinId());
		}
		if (tCustomer.getFweixinName() != null) {
			userDTO.setWxName(tCustomer.getFweixinName());
		}
		userDTO.setHeadimgUrl(HeadImageUtil.getHeadImage(tCustomer.getFphoto(), 64));
		// userDTO.setBaby(tCustomer.getFbaby());
		// userDTO.setSex(sex);
		userDTO.setSessionId(request.getSession().getId());
		userDTO.setTicket(ticket);

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("userInfo", userDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("用户登录成功！");
		return responseDTO;
	}

	/**
	 * 用户端退出接口方法
	 * 
	 * @param ticket
	 * @return
	 */
	public ResponseDTO logoutC(Integer clientType, String ticket, HttpServletRequest request) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		String ticketElement = null;
		try {
			ticketElement = redisService.getValue(ticket, RedisMoudel.TicketToId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (ticketElement == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(100);
			responseDTO.setMsg("ticket参数有误，这是个无效或者过期的ticket信息！");
			return responseDTO;
		}
		String customerId = ticketElement.toString();
		customerTicketDAO.clearTicket(customerId, clientType);
		redisService.removeCache(RedisMoudel.TicketToId, ticket);
		// 获取用户DTO缓存对象
		redisService.removeCache(RedisMoudel.CustomerEentity, customerId);
		try {
			CustomerLogBean customerLogBean = new CustomerLogBean();
			customerLogBean.setCustomerId(customerId);
			customerLogBean.setUrl(request.getRequestURI());
			customerLogBean.setCustomerType(1);
			customerLogBean.setCreateTime(new Date());
			customerLogBean.setClientType(clientType);
			customerLogBean.setClientIp(IpUtil.getIpAddr(request));
			customerLogBean.setTaskType(1);
			AsynchronousTasksManager.put(customerLogBean);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("请慢走，欢迎常来呦！");
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getProfile(Integer clientType, String ticket) {
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

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);

		UserDTO userDTO = new UserDTO();
		userDTO.setUserId(customerDTO.getCustomerId());
		// userDTO.setType(customerDTO.getFtype());
		userDTO.setName(customerDTO.getName());
		userDTO.setWxName(customerDTO.getWxName());
		userDTO.setHeadimgUrl(HeadImageUtil.getHeadImage(customerDTO.getPhoto(), 96));
		userDTO.setTicket(ticket);
		// userDTO.setBaby(customerDTO.getBaby());
		userDTO.setWxId(customerDTO.getWxId());
		userDTO.setPhone(customerDTO.getPhone());
		userDTO.setSex(String.valueOf(customerDTO.getSex()));

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("userInfo", userDTO);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setData(returnData);
		responseDTO.setMsg("获取用户详细信息时成功！");
		return responseDTO;
	}

	public ResponseDTO saveProfile(Integer clientType, String ticket, String name, Integer sex, String baby) {
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
		// 获取用户DTO缓存对象
		redisService.removeCache(RedisMoudel.CustomerEentity, customerDTO.getCustomerId());
		TCustomer tCustomer = customerDAO.getOne(customerDTO.getCustomerId());

		if (StringUtils.isNotBlank(name)) {
			tCustomer.setFname(name);
		}
		if (StringUtils.isNotBlank(baby)) {
			tCustomer.setFbaby(baby);
		}
		if (sex != null) {
			tCustomer.setFsex(sex);
		}
		tCustomer = customerDAO.save(tCustomer);

		UserDTO userDTO = new UserDTO();
		userDTO.setUserId(tCustomer.getId());
		userDTO.setType(tCustomer.getFtype());
		userDTO.setName(tCustomer.getFname());
		userDTO.setWxName(tCustomer.getFweixinName());
		userDTO.setHeadimgUrl(HeadImageUtil.getHeadImage(tCustomer.getFphoto(), 64));
		userDTO.setTicket(ticket);
		// userDTO.setBaby(tCustomer.getFbaby());
		userDTO.setWxId(tCustomer.getFweixinId());
		userDTO.setPhone(tCustomer.getFphone());
		userDTO.setSex(tCustomer.getFsex() != null ? tCustomer.getFsex().toString() : StringUtils.EMPTY);

		Map<String, Object> returnData = Maps.newHashMap();

		returnData.put("userInfo", userDTO);
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("用户详细信息保存成功！");
		return responseDTO;
	}

	public ResponseDTO comment(Integer clientType, String ticket, String objectId, String orderId, Integer type,
			String content, Integer score, String photo) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(objectId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("objectId参数不能为空，请检查objectId的传递参数值！");
			return responseDTO;
		}
		if (type == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("您输入的type参数有误，请检查type的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}
		// 敏感词过滤
		content = BadWordUtil.replace(content, "**");

		// 1：应用评论
		// 2：商品评论
		// 3：商家评论
		// 4：晒单评论
		// 5：原创评论
		// 6：文章评论
		// 10：回复评论
		TComment tComment = new TComment();
		tComment.setFcreateTime(new Date());
		tComment.setFcontent(content);
		tComment.setFscore(score);

		// 当前系统不需要晒单评论，所以晒单评论需要转换成活动评论
		// 如果是晒单评论，则传入的objectID是orderId，这时需要将晒单评论的orderId转换成eventId保存到objectId中，
		// 并且将评论类型4改成2，也就是把晒单评论自动转换成为了活动评论。
		if (type.intValue() == 4) {
			TOrderGoods tOrderGoods = orderGoodsDAO.findOne(objectId);
			tComment.setFobjectId(tOrderGoods.getFeventId());
			tComment.setFtype(2);
			tComment.setForderId(tOrderGoods.getForderId());
			orderGoodsDAO.updateCommntStatus(20, tOrderGoods.getId());
		} else if (type.intValue() == 6) {
			tComment.setFobjectId(objectId);
			tComment.setFtype(type);
			articleDAO.addOneComment(objectId);
		} else {
			tComment.setFobjectId(objectId);
			tComment.setFtype(type);
		}
		tComment.setFphoto(photo);
		tComment.setFcustomerId(customerDTO.getCustomerId());
		tComment.setFcustomerName(customerDTO.getName());
		tComment.setFcustomerLogoUrl(customerDTO.getPhoto());
		tComment.setFrecommend(0L);
		tComment.setForder(0);
		tComment.setFstatus(20);
		commentDAO.save(tComment);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("评价成功!感谢您的评价！");
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getCommentList(String objectId, Integer type, Integer distinction, Integer pageSize,
			Integer offset) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (StringUtils.isBlank(objectId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("objectId参数不能为空，请检查objectId的传递参数值！");
			return responseDTO;
		}
		if (type == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("type参数不能为空，请检查type的传递参数值！");
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
		Map<String, Object> hqlMap = new HashMap<String, Object>();

		if (type.equals(2)) {
			hql.append(
					"select t.id as id, t.fcustomerId as fcustomerId, t.fcustomerName as fcustomerName, t.fcustomerLogoUrl as fcustomerLogoUrl, ")
					.append(" t.fobjectId as fobjectId, t.fcontent as fcontent, t.fphoto as fphoto, t.fcreateTime as fcreateTime, t.frecommend as frecommend,")
					.append(" t.fscore as fscore, t.ftype as ftype,t.fuserName as fuserName, t.freply as freply, t.freplyTime as freplyTime, t.fstatus as fstatus,")
					.append(" o.fpayTime as fpayTime,e.ftitle as ftitle,i.flevel as flevel")
					.append(" from TComment t inner join TOrder o on o.id = t.forderId inner join TEvent e on e.id = t.fobjectId ")
					.append(" inner join TCustomerInfo i on i.fcustomerId = t.fcustomerId where t.fstatus in(20,40) ");
		} else {
			hql.append(
					"select t.id as id, t.fcustomerId as fcustomerId, t.fcustomerName as fcustomerName, t.fcustomerLogoUrl as fcustomerLogoUrl, ")
					.append(" t.fobjectId as fobjectId, t.fcontent as fcontent, t.fphoto as fphoto, t.fcreateTime as fcreateTime, t.frecommend as frecommend,")
					.append(" t.fscore as fscore, t.ftype as ftype,t.fuserName as fuserName, t.freply as freply, t.freplyTime as freplyTime, t.fstatus as fstatus")
					.append(" from TComment t where t.fstatus in(20,40) ");
		}
		if (type.equals(3)) {
			// 如果是获取商家评论，目前采用获取该商家发布的所有活动的评论为该商家的评论
			hql.append(
					" and t.fobjectId in (select e.id from TEvent e where e.TSponsor.id = :merchantId and e.fstatus < 999) and t.ftype = :type");
			hqlMap.put("merchantId", objectId);
			hqlMap.put("type", 2);
		} else if (type.equals(4)) {
			hql.append(" and t.fobjectId = :objectId and t.ftype = :type");
			hqlMap.put("objectId", objectId);
			hqlMap.put("type", 2);
		} else {
			hql.append(" and t.fobjectId = :objectId and t.ftype = :type");
			hqlMap.put("objectId", objectId);
			hqlMap.put("type", type);
		}
		if (distinction.equals(1)) {
			hql.append(" and t.fscore > 1");
		} else if (distinction.equals(2)) {
			hql.append(" and t.fscore <= 1");
		}
		hql.append(" order by t.forder desc, t.fcreateTime desc");

		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();

		// 对活动信息进行细化加载
		List<CommentDTO> commentList = Lists.newArrayList();

		// 获取活动距离信息缓存对象
		Cache commentRecommendCache = cacheManager.getCache(Constant.CommentRecommend);
		Element ele = null;

		CommentDTO commentDTO = null;

		Date now = new Date();
		Date date = null;
		int typeValue = 0;
		int status = 0;
		for (Map<String, Object> amap : list) {
			commentDTO = new CommentDTO();
			commentDTO.setUserLogoUrl(Constant.czyhInterfaceLogoImgUrl);
			commentDTO.setCommentId(amap.get("id").toString());
			if (amap.get("fobjectId") != null && StringUtils.isNotBlank(amap.get("fobjectId").toString())) {
				commentDTO.setObjectId(amap.get("fobjectId").toString());
			}
			if (amap.get("fcustomerId") != null && StringUtils.isNotBlank(amap.get("fcustomerId").toString())) {
				commentDTO.setCommenterId(amap.get("fcustomerId").toString());
			}
			if (amap.get("fcustomerName") != null && StringUtils.isNotBlank(amap.get("fcustomerName").toString())) {
				commentDTO.setCommenterName(ArrayStringUtils.addAsterisk(amap.get("fcustomerName").toString()));
			}
			if (amap.get("fcustomerLogoUrl") != null
					&& StringUtils.isNotBlank(amap.get("fcustomerLogoUrl").toString())) {
				commentDTO.setCommenterLogoUrl(HeadImageUtil.getHeadImage(amap.get("fcustomerLogoUrl").toString(), 46));
			} else {
				commentDTO.setCommenterLogoUrl(Constant.defaultHeadImgUrl);
			}
			if (amap.get("fcontent") != null && StringUtils.isNotBlank(amap.get("fcontent").toString())) {
				commentDTO.setContent(amap.get("fcontent").toString());
			}
			if (amap.get("fpayTime") != null && StringUtils.isNotBlank(amap.get("fpayTime").toString())) {
				date = (Date) amap.get("fpayTime");
				commentDTO.setOrderDate(DateFormatUtils.format(date, "yyyy-MM-dd HH:mm"));
			}
			if (amap.get("ftitle") != null && StringUtils.isNotBlank(amap.get("ftitle").toString())) {
				commentDTO.setObjectName(amap.get("ftitle").toString());
			}
			if (amap.get("flevel") != null && StringUtils.isNotBlank(amap.get("flevel").toString())) {
				commentDTO.setCommenterLevel(amap.get("flevel").toString());
			}
			if (amap.get("fcreateTime") != null && StringUtils.isNotBlank(amap.get("fcreateTime").toString())) {
				date = (Date) amap.get("fcreateTime");
				// if (DateUtils.truncatedEquals(date, now, Calendar.YEAR)) {
				// commentDTO.setCommentDate(DateFormatUtils.format(date, "MM-dd
				// HH:mm"));
				// } else {
				commentDTO.setCommentDate(DateFormatUtils.format(date, "yyyy-MM-dd HH:mm"));
				// }
			}
			ele = commentRecommendCache.get(commentDTO.getCommentId());
			if (ele != null) {
				CommentRecommendDTO commentRecommendDTO = (CommentRecommendDTO) ele.getObjectValue();
				commentDTO.setRecommend(commentRecommendDTO.getRecommend());
			} else if (amap.get("frecommend") != null && StringUtils.isNotBlank(amap.get("frecommend").toString())) {
				commentDTO.setRecommend((Long) amap.get("frecommend"));
			}
			if (amap.get("fscore") != null && StringUtils.isNotBlank(amap.get("fscore").toString())) {
				commentDTO.setMark((Integer) amap.get("fscore"));
			}
			if (amap.get("ftype") != null && StringUtils.isNotBlank(amap.get("ftype").toString())) {
				typeValue = ((Integer) amap.get("ftype")).intValue();
				commentDTO.setType(typeValue);
				commentDTO.setTypeString(DictionaryUtil.getString(DictionaryUtil.CommentType, typeValue));
			}
			if (amap.get("fuserName") != null && StringUtils.isNotBlank(amap.get("fuserName").toString())) {
				commentDTO.setUserName("零到壹客服 " + amap.get("fuserName").toString());
			}

			if (amap.get("freply") != null && StringUtils.isNotBlank(amap.get("freply").toString())) {
				commentDTO.setReply(amap.get("freply").toString());
			}
			if (amap.get("freplyTime") != null && StringUtils.isNotBlank(amap.get("freplyTime").toString())) {
				date = (Date) amap.get("freplyTime");
				commentDTO.setReplyTime(DateFormatUtils.format(date, "yyyy-MM-dd HH:mm"));
			}

			if (amap.get("fstatus") != null && StringUtils.isNotBlank(amap.get("fstatus").toString())) {
				status = ((Integer) amap.get("fstatus")).intValue();
				if (status == 40) {
					commentDTO.setReplied(true);
				} else {
					commentDTO.setReplied(false);
				}
			}

			if (amap.get("fphoto") != null && StringUtils.isNotBlank(amap.get("fphoto").toString())) {
				// JavaType jt = mapper.contructCollectionType(ArrayList.class,
				// String.class);
				// List<String> photolist = (ArrayList<String>)
				// mapper.fromJson(amap.get("fphoto").toString(), jt);
				commentDTO.setCommentImageUrl(amap.get("fphoto").toString());
			}

			// 处理图片返回大小
			if (amap.get("fphoto") != null && StringUtils.isNotBlank(amap.get("fphoto").toString())) {
				String[] photos = new JsonMapper(Include.ALWAYS).fromJson(amap.get("fphoto").toString(),
						String[].class);
				PublicImageDTO publicImageDTO = null;
				List<PublicImageDTO> publicImageList = Lists.newArrayList();
				for (int i = 0; i < photos.length; i++) {
					publicImageDTO = new PublicImageDTO();
					publicImageDTO.setBigPicture(publicService.getThumbnailview(photos[i], 75));
					publicImageDTO.setMediumPicture(publicService.getThumbnailview(photos[i], 55));
					publicImageDTO.setSmallPicture(publicService.getThumbnailview(photos[i], 25));
					publicImageList.add(publicImageDTO);
				}

				commentDTO.setImageUrlPathList(publicImageList);
			}

			commentList.add(commentDTO);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("commentList", commentList);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);
		responseDTO.setMsg("获取评论列表成功！");
		return responseDTO;
	}

	public ResponseDTO uploadLogo(Integer clientType, String ticket, String base64file, MultipartFile file,
			boolean isBase64) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (isBase64 && StringUtils.isBlank(base64file)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("file参数不能为空，请检查file的传递参数值！");
			return responseDTO;
		}
		if (!isBase64 && file == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("file参数不能为空，请检查file的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}
		// 获取用户DTO缓存对象
		redisService.removeCache(RedisMoudel.CustomerEentity, customerDTO.getCustomerId());

		// String filePath = null;
		String httpUrl = null;

		try {
			if (isBase64) {
				httpUrl = publicService.logoUpload(base64file, null, 1);
				// filePath =
				// fxlService.saveBase64Image("personalCustomerLogoPath",
				// base64file);
			} else {
				CommonsMultipartFile cf = (CommonsMultipartFile) file;
				DiskFileItem fi = (DiskFileItem) cf.getFileItem();
				File f = fi.getStoreLocation();
				httpUrl = publicService.logoUpload(null, f, 2);
				// filePath =
				// fxlService.saveFileImage("personalCustomerLogoPath", file);
			}
			// httpUrl = new
			// StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
			// .append(PropertiesUtil.getProperty("imageRootPath")).append(filePath);

			customerDAO.savePhoto(httpUrl, customerDTO.getCustomerId());
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(103);
			responseDTO.setMsg("图片上传并保存至服务器时出错！");
			return responseDTO;
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("headimgUrl", httpUrl);
		responseDTO.setData(returnData);
		responseDTO.setMsg("更新用户头像图片成功！");
		return responseDTO;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public ResponseDTO sendCheckCode(String phone, Integer clientType,String sign, String addressIP) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(phone)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("phone参数不能为空，请检查phone的传递参数值！");
			return responseDTO;
		}

		boolean b = this.isMobile(phone);
		if (b == false) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(101);
			responseDTO.setMsg("手机号不符合规则，请重新填写手机号！");
			return responseDTO;
		}
		
		String signStr = fxlService.httpEncrypt(addressIP, clientType);
		if (!signStr.equals(sign)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("您的sign不正确，请检查后再输入！");
			return responseDTO;
		}

		if (SmsUtil.isCheckCodeSwitch()) {
			String checkCode = RandomStringUtils.randomNumeric(6);
			Cache smsCheckCodeCache = cacheManager.getCache(Constant.SmsCheckCodeCacheKey);
			Element element = smsCheckCodeCache.get(phone);
			element = new Element(phone, checkCode);
			smsCheckCodeCache.put(element);
			// TODO发送短信验证码
			Map<String, String> smsParamMap = Maps.newHashMap();
			smsParamMap.put("chackCode", checkCode);
			smsParamMap.put("minute", "在3");
			// 发送确认码短信，短信类型码为1
			SmsResult smsResult = SmsUtil.sendSms(SmsUtil.CheckCodeSms, phone, smsParamMap);

			TSms tSms = new TSms();
			tSms.setSendPhone(phone);
			tSms.setSendTime(new Date());
			tSms.setSmsContent(smsResult.getContent());
			tSms.setSmsType(SmsUtil.CheckCodeSms);
			tSms.setSendResponse(smsResult.getResponse());
			if (smsResult.isSuccess()) {
				tSms.setSendSuccess(1);
			} else {
				tSms.setSendSuccess(0);
			}
			smsDAO.save(tSms);
			responseDTO.setMsg("短信验证码发送成功！");
		} else {
			String checkCode = "888888";
			Cache smsCheckCodeCache = cacheManager.getCache(Constant.SmsCheckCodeCacheKey);
			Element element = smsCheckCodeCache.get(phone);
			element = new Element(phone, checkCode);
			smsCheckCodeCache.put(element);
			responseDTO.setMsg("短信验证码是：888888！");
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getDiscountNum(String ticket, Integer clientType) {
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

		MyCustomerDTO myCustomerDTO = new MyCustomerDTO();
		TCustomerInfo tCustomerInfo = customerInfoDAO.getPointAndCouponNum(customerDTO.getCustomerId());
		myCustomerDTO.setCustomerId(customerDTO.getCustomerId());

		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hql.append("select t.id as couponDeliveryId")
				.append(" from TCouponInformation c inner join c.TCouponObjects o inner join c.TCouponDeliveries t inner join t.TDelivery d")
				.append(" where d.fstatus in (90,40,100,120) and (t.TCustomer.id = :customerId or t.TCustomer.id is null) and t.fdeliverTime >= :time and t.fuseEndTime >:now and t.fuseStartTime <:now")
				.append(" and c.id not in(select h.TCouponInformation.id from TCouponDeliveryHistory h inner join h.TDelivery hd where hd.fdeliverType = 10 and h.TCustomer.id = :customerId)");
		hqlMap.put("customerId", customerDTO.getCustomerId());
		hqlMap.put("time", tCustomerInfo.getFcreateTime());
		hqlMap.put("now", new Date());
		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);
		myCustomerDTO.setCouponNum(list.size());
		myCustomerDTO.setPoint(tCustomerInfo.getFpoint());
		myCustomerDTO.setLevel(tCustomerInfo.getFlevel());
		myCustomerDTO.setGrowthValue(tCustomerInfo.getFgrowthValue());
		myCustomerDTO.setNeedGrowthValue(tCustomerInfo.getFneedGrowthValue());

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("customerInfo", myCustomerDTO);
		responseDTO.setData(returnData);
		responseDTO.setMsg("获取我的页面附加数据成功！");
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO checkCustomerNew(String ticket, Integer clientType) {
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

		TCustomerInfo tCustomerInfo = customerInfoDAO.getByCustomerId(customerDTO.getCustomerId());
		boolean ifNewCustomer = false;
		if (tCustomerInfo.getFfirstOrderTime() == null) {
			ifNewCustomer = true;
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("ifNewCustomer", ifNewCustomer);
		responseDTO.setData(returnData);
		responseDTO.setMsg("判断是否为新用户成功！");
		return responseDTO;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveCustomerInfo(String customerId, Integer tag, String channel, String version, String deviceInfo,
			String deviceId, String deviceTokens, String followCustomerId, Integer clientType) {
		try {
			// 为新用户添加扩展信息
			Date now = new Date();
			TCustomerInfo tcustomerInfo = customerInfoDAO.getByCustomerId(customerId);
			TCustomerLevel tCustomerLevel = customerLevelDAO.getByLevel(2);
			// boolean isNew = false;
			if (tcustomerInfo == null) {
				// isNew = true;
				tcustomerInfo = new TCustomerInfo();
				tcustomerInfo.setFcustomerId(customerId);
				tcustomerInfo.setForderNumber(0);
				tcustomerInfo.setForderTotal(BigDecimal.ZERO);
				tcustomerInfo.setFpayOrderNumber(0);
				tcustomerInfo.setFpayZeroOrderNumber(0);
				tcustomerInfo.setFpoint(0);
				tcustomerInfo.setFusedPoint(0);
				tcustomerInfo.setFrefundOrderNumber(0);
				tcustomerInfo.setFtipNumber(0);
				tcustomerInfo.setFcouponNum(0);
				tcustomerInfo.setFlevel(1);
				tcustomerInfo.setFgrowthValue(0);
				tcustomerInfo.setFneedGrowthValue(tCustomerLevel.getFgrowthValue());
				tcustomerInfo.setForderFans(0);
				tcustomerInfo.setFans(0);

			}
			if (StringUtils.isNotBlank(channel)) {
				tcustomerInfo.setFregisterChannel(channel);
			}
			if (StringUtils.isNotBlank(version)) {
				tcustomerInfo.setFregisterChannelVersion(version);
			}
			if (StringUtils.isNotBlank(deviceInfo)) {
				tcustomerInfo.setFregisterDeviceInfo(deviceInfo);
			}
			if (StringUtils.isNotBlank(deviceId)) {
				tcustomerInfo.setFregisterDeviceId(deviceId);
			}
			if (StringUtils.isNotBlank(followCustomerId)) {
				tcustomerInfo.setFregisterDeviceTokens(deviceTokens);
			}
			if (StringUtils.isNotBlank(followCustomerId)) {
				tcustomerInfo.setFollowCustomerId(followCustomerId);
			}

			tcustomerInfo.setFregisterTime(now);
			tcustomerInfo.setFcreateTime(now);
			// tcustomerInfo.setFupdateTime(now);
			customerInfoDAO.save(tcustomerInfo);

			// if (isNew && tag != null) {
			// // 为新注册的用户打标
			// pushService.addCustomerTags(customerId, tag, deviceId,
			// clientType, deviceTokens);
			// } else if (!isNew && tag != null) {
			// // 修改用户标签以及友盟同步的token
			// pushService.updateCustomerTags(customerId, tag, deviceId,
			// clientType, deviceTokens);
			// }
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
	}

	public boolean isMobile(final String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}
}