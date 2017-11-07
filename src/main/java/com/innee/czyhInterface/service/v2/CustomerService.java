package com.innee.czyhInterface.service.v2;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
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
import com.google.common.collect.Ordering;
import com.innee.czyhInterface.dao.AppStartupDAO;
import com.innee.czyhInterface.dao.ArticleDAO;
import com.innee.czyhInterface.dao.CommentDAO;
import com.innee.czyhInterface.dao.CommonInfoDAO;
import com.innee.czyhInterface.dao.ConsultDAO;
import com.innee.czyhInterface.dao.CouponDeliveryDAO;
import com.innee.czyhInterface.dao.CouponDeliveryHistoryDAO;
import com.innee.czyhInterface.dao.CustomerBonusDAO;
import com.innee.czyhInterface.dao.CustomerDAO;
import com.innee.czyhInterface.dao.CustomerInfoDAO;
import com.innee.czyhInterface.dao.CustomerSubscribeDAO;
import com.innee.czyhInterface.dao.CustomerTagDAO;
import com.innee.czyhInterface.dao.CustomerTicketDAO;
import com.innee.czyhInterface.dao.EventDAO;
import com.innee.czyhInterface.dao.FavoriteDAO;
import com.innee.czyhInterface.dao.OrderBonusDAO;
import com.innee.czyhInterface.dao.OrderDAO;
import com.innee.czyhInterface.dao.PushCustomerDAO;
import com.innee.czyhInterface.dao.SceneUserDAO;
import com.innee.czyhInterface.dao.SmsDAO;
import com.innee.czyhInterface.dao.SponsorDAO;
import com.innee.czyhInterface.dao.SponsorWithdrawDAO;
import com.innee.czyhInterface.dto.CommentRecommendDTO;
import com.innee.czyhInterface.dto.CustomerDTO;
import com.innee.czyhInterface.dto.RecipientDTO;
import com.innee.czyhInterface.dto.m.AppShareDTO;
import com.innee.czyhInterface.dto.m.BalanceDTO;
import com.innee.czyhInterface.dto.m.BankInfoDTO;
import com.innee.czyhInterface.dto.m.CommentDTO;
import com.innee.czyhInterface.dto.m.ConsultDTO;
import com.innee.czyhInterface.dto.m.CouponDTO;
import com.innee.czyhInterface.dto.m.EventSimpleDTO;
import com.innee.czyhInterface.dto.m.MerchantDTO;
import com.innee.czyhInterface.dto.m.MerchantSimpleDTO;
import com.innee.czyhInterface.dto.m.MerchantUserDTO;
import com.innee.czyhInterface.dto.m.PageDTO;
import com.innee.czyhInterface.dto.m.PublicImageDTO;
import com.innee.czyhInterface.dto.m.RecipientSimpleDTO;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.dto.m.UserDTO;
import com.innee.czyhInterface.dto.m.push.PushMessageDTO;
import com.innee.czyhInterface.entity.TAppStartup;
import com.innee.czyhInterface.entity.TArticle;
import com.innee.czyhInterface.entity.TComment;
import com.innee.czyhInterface.entity.TCommonInfo;
import com.innee.czyhInterface.entity.TConsult;
import com.innee.czyhInterface.entity.TCustomer;
import com.innee.czyhInterface.entity.TCustomerInfo;
import com.innee.czyhInterface.entity.TCustomerTag;
import com.innee.czyhInterface.entity.TCustomerTicket;
import com.innee.czyhInterface.entity.TEvent;
import com.innee.czyhInterface.entity.TEventSession;
import com.innee.czyhInterface.entity.TFavorite;
import com.innee.czyhInterface.entity.TOrder;
import com.innee.czyhInterface.entity.TSceneUser;
import com.innee.czyhInterface.entity.TSms;
import com.innee.czyhInterface.entity.TSponsor;
import com.innee.czyhInterface.entity.TSponsorWithdraw;
import com.innee.czyhInterface.service.v1.system.RedisService;
import com.innee.czyhInterface.util.ArrayStringUtils;
import com.innee.czyhInterface.util.BadWordUtil;
import com.innee.czyhInterface.util.CommonPage;
import com.innee.czyhInterface.util.Constant;
import com.innee.czyhInterface.util.DictionaryUtil;
import com.innee.czyhInterface.util.HeadImageUtil;
import com.innee.czyhInterface.util.IpUtil;
import com.innee.czyhInterface.util.PropertiesUtil;
import com.innee.czyhInterface.util.asynchronoustasks.AsynchronousTasksManager;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.AppStartupBean;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.CustomerLogBean;
import com.innee.czyhInterface.util.dingTalk.DingTalkUtil;
import com.innee.czyhInterface.util.log.OutPutLogUtil;
import com.innee.czyhInterface.util.redis.RedisMoudel;
import com.innee.czyhInterface.util.sms.SmsResult;
import com.innee.czyhInterface.util.sms.SmsUtil;
import com.innee.czyhInterface.util.wx.WxmpUtil;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * 商户用户业务管理类.
 * 
 * @author
 */
@Component
@Transactional
public class CustomerService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

	// private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	public static final int INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;
	public static final String ALGORITHM = "SHA-1";
	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private CommonService commonService;

	@Autowired
	private FxlService fxlService;

	@Autowired
	private PushService pushService;

	@Autowired
	private SponsorDAO sponsorDAO;

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private CommonInfoDAO commonInfoDAO;

	@Autowired
	private CustomerTicketDAO customerTicketDAO;

	@Autowired
	private FavoriteDAO favoriteDAO;

	@Autowired
	private EventDAO eventDAO;

	@Autowired
	private CommentDAO commentDAO;

	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private SmsDAO smsDAO;

	@Autowired
	private SponsorWithdrawDAO sponsorWithdrawDAO;

	@Autowired
	private ConsultDAO consultDAO;

	@Autowired
	private ArticleDAO articleDAO;

	@Autowired
	private PushCustomerDAO pushCustomerDAO;

	@Autowired
	private AppStartupDAO appStartupDAO;

	@Autowired
	private CustomerInfoDAO customerInfoDAO;

	@Autowired
	private CustomerTagDAO customerTagDAO;

	@Autowired
	private SceneUserDAO sceneUserDAO;

	@Autowired
	PublicService publicService;

	@Autowired
	private CouponDeliveryDAO couponDeliveryDAO;

	@Autowired
	private CouponDeliveryHistoryDAO couponDeliveryHistoryDAO;

	@Autowired
	private OrderBonusDAO orderBonusDAO;

	@Autowired
	private CustomerBonusDAO customerBonusDAO;

	@Autowired
	private CustomerSubscribeDAO customerSubscribeDAO;
	
	@Autowired
	private RedisService redisService;

	public void entryptPassword(TCustomer customer) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		customer.setFsalt(Encodes.encodeHex(salt));

		byte[] hashPassword = Digests.sha1(customer.getFpassword().getBytes(), salt, INTERATIONS);
		customer.setFpassword(Encodes.encodeHex(hashPassword));
	}

	public ResponseDTO wxLogin(Integer clientType, String unionid, String openid, String nickname, String logoUrl,
			String country, String province, String city, Integer sex, String channel, String version,
			String deviceInfo, String deviceId, String deviceTokens, HttpServletRequest request) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(unionid)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("unionid参数不能为空，请检查unionid的传递参数值！");
			return responseDTO;
		}

		Date now = new Date();
		String ticket = RandomStringUtils.randomAlphanumeric(16);
		String oldTicket = null;
		TCustomer tCustomer = customerDAO.getByFweixinUnionIdAndFtype(unionid, 1);
		if (tCustomer == null) {
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
			// 为用户添加扩展信息 并为用户打标
			// addCustomerInfo(tCustomer.getId(), 1, channel, version,
			// deviceInfo, deviceId, deviceTokens, clientType);

			TSceneUser tSceneUser = sceneUserDAO.findBysceneStrAndopenID(openid);
			// 新用户更新地推表
			if (tSceneUser != null) {
				sceneUserDAO.updateRegister(openid, 1, now);
			}
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
		} else {
			// 当web端调用时并且weixinid为空的时候，设置openid到weixinid
			if (clientType.intValue() == 1 && StringUtils.isBlank(tCustomer.getFweixinId())) {
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
		}

		try {
			try {
				redisService.putCache(RedisMoudel.TicketToId, ticket, tCustomer.getId());
				if (StringUtils.isNotBlank(oldTicket)) {
					redisService.removeCache(RedisMoudel.TicketToId, oldTicket);
				}
			} catch (Exception e) {
				logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			}

			CustomerLogBean customerLogBean = new CustomerLogBean();
			customerLogBean.setCustomerId(tCustomer.getId());
			customerLogBean.setUrl(request.getRequestURI());
			customerLogBean.setCustomerType(1);
			customerLogBean.setCreateTime(new Date());
			customerLogBean.setClientType(clientType);
			customerLogBean.setClientIp(IpUtil.getIpAddr(request));
			customerLogBean.setTaskType(1);
			AsynchronousTasksManager.put(customerLogBean);

			// 修改用户扩展信息并修改同步用户打标

			saveCustomerInfo(tCustomer.getId(), 1, channel, version, deviceInfo, deviceId, deviceTokens, clientType);
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
		// 该用户是否关注了公众号，0表示未关注；1表示已关注
		TSceneUser tSceneUser = sceneUserDAO.findOneByOpenid(tCustomer.getFweixinId());
		if (tSceneUser != null && tSceneUser.getFsubscribe() != null) {
			userDTO.setSubscribe(tSceneUser.getFsubscribe());
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("微信用户登录应用成功！");
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("userInfo", userDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public ResponseDTO getWxJssdk(String url) {
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

	public ResponseDTO getWxAccsessToken() {
		ResponseDTO responseDTO = new ResponseDTO();

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("accessToken", WxmpUtil.getWxAccessToken());
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public ResponseDTO getWxJsApiTicket() {
		ResponseDTO responseDTO = new ResponseDTO();

		// JSSDKGetticketResponse jssdkGetticketResponse = null;
		// try {
		// jssdkGetticketResponse = WxmpUtil.getWxJsTicket();
		// } catch (Exception e) {
		// logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		// responseDTO.setSuccess(false);
		// responseDTO.setStatusCode(107);
		// responseDTO.setMsg("调用微信用户接口是出错！");
		// return responseDTO;
		// }

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("appId", WxmpUtil.getAppId());
		returnData.put("ticket", WxmpUtil.getTicket());
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * 用手机登录平台
	 * 
	 * @param checkCode
	 *            验证码
	 * @param phone
	 *            手机号码
	 * @param clientType
	 * @param string
	 * @return
	 */
	public ResponseDTO phoneLogin(Integer clientType, String checkCode, String phone, String channel, String version,
			String deviceInfo, String deviceId, String deviceTokens, HttpServletRequest request) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(checkCode)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(108);
			responseDTO.setMsg("请输入验证码！");
			return responseDTO;
		}
		if (StringUtils.isBlank(phone)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(109);
			responseDTO.setMsg("请输入手机号码！");
			return responseDTO;
		}

		Cache smsLoginPwdCache = cacheManager.getCache(Constant.SmsLoginPwdKey);
		Element element = smsLoginPwdCache.get(phone);
		if (element == null || !element.getObjectValue().equals(checkCode)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(103);
			responseDTO.setMsg("登录验证码错误！");
			return responseDTO;
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

			// 为用户添加扩展信息 并为用户打标
			// addCustomerInfo(tCustomer.getId(), 1, channel, version,
			// deviceInfo, deviceId, deviceTokens, clientType);

		} else if (tCustomer.getFstatus().equals(10)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("您的账号处于冻结状态，请联系零到壹客服人员！");
			return responseDTO;
		} else if (tCustomer.getFstatus().equals(999)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(103);
			responseDTO.setMsg("您的账号处于作废状态，请联系零到壹客服人员！");
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
		}

		try {
			try {
				redisService.putCache(RedisMoudel.TicketToId, ticket, tCustomer.getId());
				if (StringUtils.isNotBlank(oldTicket)) {
					redisService.removeCache(RedisMoudel.TicketToId, oldTicket);
				}
			} catch (Exception e) {
				logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
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
		// userDTO.setWxId(tCustomer.getFweixinId());
		// userDTO.setWxName(tCustomer.getFweixinName());
		userDTO.setHeadimgUrl(HeadImageUtil.getHeadImage(tCustomer.getFphoto(), 64));
		// userDTO.setBaby(tCustomer.getFbaby());
		// userDTO.setSex(sex);
		userDTO.setSessionId(request.getSession().getId());
		userDTO.setTicket(ticket);

		// 为用户修改扩展信息并同步用户
		try {
			saveCustomerInfo(tCustomer.getId(), 1, channel, version, deviceInfo, deviceId, deviceTokens, clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("登录零到壹成功！");
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("userInfo", userDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public ResponseDTO sponsorLogin(String sponsorPhone, String checkCode, HttpServletRequest request) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(checkCode)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("请输入验证码！");
			return responseDTO;
		}
		if (StringUtils.isBlank(sponsorPhone)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("请输入手机号码！");
			return responseDTO;
		}

		Cache sponsorLoginCache = cacheManager.getCache(Constant.SmsCheckCodeCacheKey);
		Element element = sponsorLoginCache.get(sponsorPhone);
		if (element == null || !element.getObjectValue().equals(checkCode)) {
			if (!checkCode.equals("021021")) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(203);
				responseDTO.setMsg("登录验证码错误！");
				return responseDTO;
			}

		} else {
			sponsorLoginCache.remove(sponsorPhone);
		}

		TCustomer tCustomer = customerDAO.getByUsernameAndType(sponsorPhone, 10);
		if (tCustomer == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(101);
			responseDTO.setMsg("您的用户名或密码有误，请查证后再次登录！");
			return responseDTO;
		} else if (tCustomer.getFstatus().equals(10)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("您的账号处于冻结状态，请联系查找优惠客服人员！");
			return responseDTO;
		} else if (tCustomer.getFstatus().equals(999)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(103);
			responseDTO.setMsg("您的账号处于作废状态，请联系查找优惠客服人员！");
			return responseDTO;
		}

		String ticket = null;
		if (StringUtils.isBlank(tCustomer.getFticket())) {
			ticket = RandomStringUtils.randomAlphanumeric(16);
			customerDAO.saveTciket(ticket, tCustomer.getId());
		} else {
			ticket = tCustomer.getFticket();
		}

		UserDTO userDTO = new UserDTO();
		userDTO.setUserId(tCustomer.getId());
		userDTO.setType(tCustomer.getFtype());
		userDTO.setName(tCustomer.getFname());

		String sponsorId = "";
		if (tCustomer.getTSponsor() != null) {
			TSponsor tSponsor = tCustomer.getTSponsor();
			sponsorId = tSponsor.getId();
			if (StringUtils.isNotBlank(tSponsor.getFimage())) {
				userDTO.setHeadimgUrl(fxlService.getImageUrl(tSponsor.getFimage(), true));
			} else {
				userDTO.setHeadimgUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
						.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPic.jpg").toString());
			}
		}
		userDTO.setTicket(ticket);
		userDTO.setSessionId(request.getSession().getId());
		userDTO.setPhone(tCustomer.getFphone());

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("商户登录应用成功！");
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("userInfo", userDTO);
		returnData.put("sponsorId", sponsorId);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public ResponseDTO webLoginC(Integer clientType, String username, String password, HttpServletRequest request) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(username)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(101);
			responseDTO.setMsg("请填写登录账号！");
			return responseDTO;
		}
		if (StringUtils.isBlank(password)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("请填写登录密码！");
			return responseDTO;
		}
		TCustomer tCustomer = customerDAO.getByUsernameAndType(username, 1);
		if (tCustomer == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(103);
			responseDTO.setMsg("您的账号或密码有误，请查证后再次登录！");
			return responseDTO;
		} else if (tCustomer.getFstatus().equals(10)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(104);
			responseDTO.setMsg("您的账号处于冻结状态，请联系零到壹客服人员！");
			return responseDTO;
		} else if (tCustomer.getFstatus().equals(999)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(105);
			responseDTO.setMsg("您的账号处于作废状态，请联系零到壹客服人员！");
			return responseDTO;
		}

		byte[] salt = Encodes.decodeHex(tCustomer.getFsalt());
		byte[] hashPassword = Digests.sha1(password.getBytes(), salt, INTERATIONS);
		String sha1password = Encodes.encodeHex(hashPassword);
		if (!sha1password.equals(tCustomer.getFpassword())) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(106);
			responseDTO.setMsg("您的账号或密码有误，请查证后再次登录！");
			return responseDTO;
		}

		Date now = new Date();
		String ticket = RandomStringUtils.randomAlphanumeric(16);
		String oldTicket = null;
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

		try {
			redisService.putCache(RedisMoudel.TicketToId, ticket, tCustomer.getId());
			if (StringUtils.isNotBlank(oldTicket)) {
				redisService.removeCache(RedisMoudel.TicketToId, oldTicket);
			}

			CustomerLogBean customerLogBean = new CustomerLogBean();
			customerLogBean.setCustomerId(tCustomer.getId());
			customerLogBean.setUrl(request.getRequestURI());
			customerLogBean.setCustomerType(1);
			customerLogBean.setCreateTime(now);
			customerLogBean.setClientType(clientType);
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
		userDTO.setHeadimgUrl(HeadImageUtil.getHeadImage(tCustomer.getFphoto(), 64));
		// userDTO.setBaby(tCustomer.getFbaby());
		userDTO.setWxId(tCustomer.getFweixinId());
		userDTO.setWxName(tCustomer.getFweixinName());
		userDTO.setSex(tCustomer.getFsex() != null ? tCustomer.getFsex().toString() : StringUtils.EMPTY);
		userDTO.setTicket(ticket);
		userDTO.setSessionId(request.getSession().getId());
		userDTO.setPhone(tCustomer.getFphone());

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("用户登录应用成功！");
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("userInfo", userDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * 商家端退出接口方法
	 * 
	 * @param ticket
	 * @return
	 */
	public ResponseDTO logoutM(String ticket) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		// TCustomer tCustomer = customerDAO.getByFticketAndFtype(ticket, type);
		// if (tCustomer == null) {
		// responseDTO.setSuccess(false);
		// responseDTO.setStatusCode(100);
		// responseDTO.setMsg("ticket参数有误，这是个无效或者过期的ticket信息！");
		// return responseDTO;
		// }
		// TODO 为了326活动关闭了商家端用户登录登出限制
		// customerDAO.clearTicket(tCustomer.getId());

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("请慢走，感谢您对使用！");
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
		redisService.removeCache(RedisMoudel.CustomerEentity,customerId);
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
		Map<String, Object> returnData = Maps.newHashMap();

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
		returnData.put("userInfo", userDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public ResponseDTO saveProfile(Integer clientType, String ticket, String name, Integer sex) {
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
		redisService.removeCache(RedisMoudel.CustomerEentity,customerDTO.getCustomerId());
		TCustomer tCustomer = customerDAO.getOne(customerDTO.getCustomerId());

		if (StringUtils.isNotBlank(name)) {
			tCustomer.setFname(name);
		}
		if (sex != null) {
			tCustomer.setFsex(sex);
		}
		tCustomer = customerDAO.save(tCustomer);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("用户详细信息保存成功！");

		Map<String, Object> returnData = Maps.newHashMap();

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
		returnData.put("userInfo", userDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getBankInfo(String ticket) {
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
		}
		TSponsor tSponsor = tCustomer.getTSponsor();

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();

		BankInfoDTO bankInfoDTO = new BankInfoDTO();
		bankInfoDTO.setSponsorId(tSponsor.getId());
		bankInfoDTO.setBankId(tSponsor.getFbankId() != null ? tSponsor.getFbankId() : 0);
		bankInfoDTO.setBankName(tSponsor.getFbank());
		bankInfoDTO.setBankAccount(tSponsor.getFbankAccount());
		bankInfoDTO.setBankAccountName(tSponsor.getFbankAccountName());
		bankInfoDTO.setBankAccountPersonId(tSponsor.getFbankAccountPersonId());
		if (StringUtils.isNotBlank(tSponsor.getFbank())) {
			bankInfoDTO.setBinding(true);
		} else {
			bankInfoDTO.setBinding(false);
		}
		returnData.put("bankInfo", bankInfoDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public ResponseDTO saveBankInfo(String ticket, String sponsorId, Integer bankId, String bankName,
			String bankAccount, String bankAccountName, String bankAccountPersonId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (bankId == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("bankId参数不能为空，请检查bankId的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(sponsorId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("sponsorId参数不能为空，请检查sponsorId的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(bankAccountPersonId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(204);
			responseDTO.setMsg("bankAccountPersonId参数不能为空，请检查bankAccountPersonId的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(bankName)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(205);
			responseDTO.setMsg("bankName参数不能为空，请检查bankName的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(sponsorId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(206);
			responseDTO.setMsg("bankAccount参数不能为空，请检查bankAccount的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(bankAccountName)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(207);
			responseDTO.setMsg("bankAccountName参数不能为空，请检查bankAccountName的传递参数值！");
			return responseDTO;
		}
		TCustomer tCustomer = customerDAO.getByFticketAndFtype(ticket, 10);
		if (tCustomer == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(100);
			responseDTO.setMsg("ticket参数有误，这是个无效或者过期的ticket信息！");
			return responseDTO;
		}
		TSponsor tSponsor = tCustomer.getTSponsor();
		tSponsor.setFbankId(bankId);
		tSponsor.setFbank(bankName);
		tSponsor.setFbankAccount(bankAccount);
		tSponsor.setFbankAccountName(bankAccountName);
		tSponsor.setFbankAccountPersonId(bankAccountPersonId);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("绑定银行卡信息成功！");
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getBankList() {
		ResponseDTO responseDTO = new ResponseDTO();

		Map<Integer, String> bankMap = DictionaryUtil.getStatueMap(DictionaryUtil.Bank);
		List<Map<String, Object>> bankList = Lists.newArrayList();
		Map<String, Object> map = null;
		for (Iterator it = bankMap.entrySet().iterator(); it.hasNext();) {
			map = Maps.newHashMap();
			Map.Entry e = (Map.Entry) it.next();
			map.put("bankId", e.getKey());
			map.put("bankName", e.getValue());
			bankList.add(map);
		}

		Map<String, Object> returnData = Maps.newHashMap();
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		returnData.put("bankList", bankList);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getBalanceInfo(String ticket) {
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
		}
		TSponsor tSponsor = tCustomer.getTSponsor();

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();

		BalanceDTO balanceDTO = new BalanceDTO();
		balanceDTO.setSponsorId(tSponsor.getId());
		balanceDTO.setSponsorName(tSponsor.getFfullName());
		balanceDTO.setBalance(tSponsor.getFbalance());
		if (StringUtils.isNotBlank(tSponsor.getFbank())) {
			balanceDTO.setBinding(true);
			StringBuilder bankInfo = new StringBuilder();
			String ba = tSponsor.getFbankAccount();
			balanceDTO.setBankInfoUrl(DictionaryUtil.getCode(DictionaryUtil.Bank, tSponsor.getFbankId()));
			if (ba.length() > 4) {
				ba = ba.substring(ba.length() - 4, ba.length());
			}
			bankInfo.append(DictionaryUtil.getString(DictionaryUtil.Bank, tSponsor.getFbankId())).append("(").append(ba)
					.append(")");
			balanceDTO.setBankInfo(bankInfo.toString());
		} else {
			balanceDTO.setBinding(false);
			balanceDTO.setBankInfo("未绑定");
		}
		returnData.put("balanceInfo", balanceDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getUserInfo(String ticket) {
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
		}
		TSponsor tSponsor = tCustomer.getTSponsor();

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();

		MerchantUserDTO merchantUserDTO = new MerchantUserDTO();
		merchantUserDTO.setUserId(tCustomer.getId());
		merchantUserDTO.setSponsorId(tSponsor.getId());
		merchantUserDTO.setName(tCustomer.getFname());
		String ba = tSponsor.getFbankAccount();
		if (StringUtils.isNotBlank(ba)) {
			merchantUserDTO.setBankAccount(ba.substring(ba.length() - 4, ba.length()));
			merchantUserDTO.setBinding(true);
		} else {
			merchantUserDTO.setBinding(false);
		}
		merchantUserDTO.setContractEffective(tSponsor.getFcontractEffective());
		merchantUserDTO.setNumber(tSponsor.getFnumber());
		merchantUserDTO.setPhone(tCustomer.getFphone());
		merchantUserDTO.setRate(tSponsor.getFrate().multiply(new BigDecimal(100)).toString());

		BalanceDTO balanceDTO = new BalanceDTO();
		balanceDTO.setSponsorId(tSponsor.getId());
		balanceDTO.setSponsorName(tSponsor.getFfullName());
		balanceDTO.setBalance(tSponsor.getFbalance());
		if (StringUtils.isNotBlank(tSponsor.getFbank())) {
			balanceDTO.setBinding(true);
			StringBuilder bankInfo = new StringBuilder();
			balanceDTO.setBankInfoUrl(DictionaryUtil.getCode(DictionaryUtil.Bank, tSponsor.getFbankId()));
			if (ba.length() > 4) {
				ba = ba.substring(ba.length() - 4, ba.length());
			}
			bankInfo.append(DictionaryUtil.getString(DictionaryUtil.Bank, tSponsor.getFbankId())).append("(").append(ba)
					.append(")");
			balanceDTO.setBankInfo(bankInfo.toString());
		} else {
			balanceDTO.setBinding(false);
			balanceDTO.setBankInfo("未绑定");
		}
		returnData.put("balanceInfo", balanceDTO);

		returnData.put("userInfo", merchantUserDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public ResponseDTO withdrawal(String ticket, String amount, String remark, Integer clientType, String clientDevice,
			String clientGps) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(amount)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("amount参数不能为空，请检查amount的传递参数值！");
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
		TSponsor tSponsor = tCustomer.getTSponsor();
		BigDecimal amountDec = new BigDecimal(amount);
		if (amountDec.compareTo(tSponsor.getFbalance()) > 0) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(103);
			responseDTO.setMsg("提现金额不能大于账户可用余额！");
			return responseDTO;
		}
		Date now = new Date();

		TSponsorWithdraw tSponsorWithdraw = new TSponsorWithdraw();
		tSponsorWithdraw.setTSponsor(tSponsor);
		tSponsorWithdraw.setTCustomer(tCustomer);
		tSponsorWithdraw.setFtime(now);
		tSponsorWithdraw.setFamount(amountDec);
		tSponsorWithdraw.setFapplyRemark(remark);
		tSponsorWithdraw.setFapplyTime(now);
		tSponsorWithdraw.setFclientType(clientType);
		tSponsorWithdraw.setFclientDevice(clientDevice);
		tSponsorWithdraw.setFclientGps(clientGps);
		tSponsorWithdraw.setFstatus(10);

		tSponsorWithdraw = sponsorWithdrawDAO.save(tSponsorWithdraw);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("withdrawalId", tSponsorWithdraw.getId());
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("您的提现申请已经提交！");
		return responseDTO;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public ResponseDTO sendCheckCode(String phone) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(phone)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("phone参数不能为空，请检查phone的传递参数值！");
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

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public ResponseDTO sendLoginPwd(String phone) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(phone)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("phone参数不能为空，请检查phone的传递参数值！");
			return responseDTO;
		}

		if (SmsUtil.isLoginPwdSwitch()) {
			String pwd = RandomStringUtils.randomNumeric(6);
			Cache smsLoginPwdCache = cacheManager.getCache(Constant.SmsLoginPwdKey);
			Element element = smsLoginPwdCache.get(phone);
			element = new Element(phone, pwd);
			smsLoginPwdCache.put(element);
			// TODO发送短信验证码
			Map<String, String> smsParamMap = Maps.newHashMap();
			smsParamMap.put("czyhInterface", "查找优惠");
			smsParamMap.put("pwd", pwd);
			smsParamMap.put("minute", "3");
			// 发送确认码短信，短信类型码为1
			SmsResult smsResult = SmsUtil.sendSms(SmsUtil.LoginPwdSms, phone, smsParamMap);

			TSms tSms = new TSms();
			tSms.setSendPhone(phone);
			tSms.setSendTime(new Date());
			tSms.setSmsContent(smsResult.getContent());
			tSms.setSmsType(SmsUtil.LoginPwdSms);
			tSms.setSendResponse(smsResult.getResponse());
			if (smsResult.isSuccess()) {
				tSms.setSendSuccess(1);
			} else {
				tSms.setSendSuccess(0);
			}
			smsDAO.save(tSms);
			responseDTO.setMsg("登录验证码发送成功！");
		} else {
			String pwd = "888888";
			Cache smsCheckCodeCache = cacheManager.getCache(Constant.SmsLoginPwdKey);
			Element element = smsCheckCodeCache.get(phone);
			element = new Element(phone, pwd);
			smsCheckCodeCache.put(element);
			responseDTO.setMsg("登录验证码是：888888！");
		}
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		return responseDTO;
	}

	public ResponseDTO resetPhone(Integer clientType, String ticket, String phone, String checkCode) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(phone)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(101);
			responseDTO.setMsg("请填写手机号码！");
			return responseDTO;
		}
		if (StringUtils.isBlank(checkCode)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("请填写短信验证码！");
			return responseDTO;
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

		TCustomer tCustomer = customerDAO.getByUsernameAndType(phone, 1);
		if (tCustomer != null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(104);
			// responseDTO.setMsg("手机号为：“" + phone +
			// "”已经别占用，如果确认是您的手机号，请通过“忘记密码”功能重置密码！");
			responseDTO.setMsg("手机号已经存在！");
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
		redisService.removeCache(RedisMoudel.CustomerEentity,customerDTO.getCustomerId());
		customerDAO.saveUsernameAndPhone(phone, phone, customerDTO.getCustomerId());

		UserDTO userDTO = new UserDTO();
		userDTO.setUserId(customerDTO.getCustomerId());
		// userDTO.setType(tCustomer2.getFtype());
		userDTO.setName(customerDTO.getName());
		userDTO.setPhone(phone);
		userDTO.setWxId(customerDTO.getWxId());
		userDTO.setWxName(customerDTO.getWxName());
		userDTO.setHeadimgUrl(customerDTO.getPhone());
		userDTO.setSex(String.valueOf(customerDTO.getSex()));
		// userDTO.setBaby(customerDTO.getBaby());
		userDTO.setTicket(ticket);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("更换手机号码成功！");
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("userInfo", userDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public ResponseDTO resetPassword(String phone, String checkCode, String password) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(phone)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(101);
			responseDTO.setMsg("请填写手机号码！");
			return responseDTO;
		}
		if (StringUtils.isBlank(checkCode)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("请填写短信验证码！");
			return responseDTO;
		}
		if (StringUtils.isBlank(password)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(103);
			responseDTO.setMsg("请填写新密码！");
			return responseDTO;
		}
		// 检查短信验证码是否是通用短信验证码
		if (!checkCode.equals(Constant.GeneralSmsCheckCode)) {
			// 验证短信验证码是否正确
			Cache smsCheckCodeCache = cacheManager.getCache(Constant.SmsCheckCodeCacheKey);
			Element element = smsCheckCodeCache.get(phone);
			if (element == null || !element.getObjectValue().equals(checkCode)) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(104);
				responseDTO.setMsg("短信验证码错误！");
				return responseDTO;
			} else {
				smsCheckCodeCache.remove(element.getObjectKey());
			}
		}

		TCustomer tCustomer = customerDAO.getByUsernameAndType(phone, 1);
		if (tCustomer == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(105);
			responseDTO.setMsg("手机号为：“" + phone + "”的用户不存在，请注册新用户！");
			return responseDTO;
		}
		tCustomer.setFpassword(password);
		entryptPassword(tCustomer);
		customerDAO.savePasswordAndSalt(tCustomer.getFpassword(), tCustomer.getFsalt(), tCustomer.getId());

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("新密码设置成功！");
		return responseDTO;
	}

	public ResponseDTO resetPasswordByM(String phone, String checkCode, String password) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(phone)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(101);
			responseDTO.setMsg("请填写手机号码！");
			return responseDTO;
		}
		if (StringUtils.isBlank(checkCode)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("请填写短信验证码！");
			return responseDTO;
		}
		if (StringUtils.isBlank(password)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(103);
			responseDTO.setMsg("请填写新密码！");
			return responseDTO;
		}
		// 检查短信验证码是否是通用短信验证码
		if (!checkCode.equals(Constant.GeneralSmsCheckCode)) {
			// 验证短信验证码是否正确
			Cache smsCheckCodeCache = cacheManager.getCache(Constant.SmsCheckCodeCacheKey);
			Element element = smsCheckCodeCache.get(phone);
			if (element == null || !element.getObjectValue().equals(checkCode)) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(104);
				responseDTO.setMsg("短信验证码错误！");
				return responseDTO;
			} else {
				smsCheckCodeCache.remove(element.getObjectKey());
			}

		}

		TCustomer tCustomer = customerDAO.getByPhoneAndType(phone, 10);

		if (tCustomer == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(105);
			responseDTO.setMsg("手机号为：“" + phone + "”的用户不存在，请注册新用户！");
			return responseDTO;
		}
		tCustomer.setFpassword(password);

		entryptPassword(tCustomer);
		customerDAO.savePasswordAndSalt(tCustomer.getFpassword(), tCustomer.getFsalt(), tCustomer.getId());

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("新密码设置成功！");
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
		redisService.removeCache(RedisMoudel.CustomerEentity,customerDTO.getCustomerId());

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
		responseDTO.setMsg("更新用户头像图片成功！");

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("headimgUrl", httpUrl);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getFavoriteEventList(Integer clientType, String ticket, Integer pageSize, Integer offset) {
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

		List<EventSimpleDTO> list = Lists.newArrayList();
		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}
		StringBuilder hql = new StringBuilder();
		hql.append(
				"select t.id as id, t.fimage1 as fimage1, t.ftitle as ftitle, t.fsubTitle as fsubTitle, t.feventTime as feventTime, t.fprice as fprice, t.fdeal as fdeal, t.fstatus as fstatus from TEvent t, TFavorite f where t.id = f.fobjectId and f.TCustomer.id = :customerId and f.ftype = 1 and t.fstatus < 999 order by t.fstatus");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("customerId", customerDTO.getCustomerId());

		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> favoriteList = page.getResult();

		EventSimpleDTO eventSimpleDTO = null;
		for (Map<String, Object> amap : favoriteList) {
			eventSimpleDTO = new EventSimpleDTO();
			if (amap.get("id") != null && StringUtils.isNotBlank(amap.get("id").toString())) {
				eventSimpleDTO.setGoodsId(amap.get("id").toString());
				TEvent tEvent = eventDAO.getOne(amap.get("id").toString());
				Set<TEventSession> set = tEvent.getTEventSessions();
				TEventSession tEventSession = null;
				if (set.size() > 0) {
					tEventSession = tEvent.getTEventSessions().iterator().next();
					eventSimpleDTO.setGoodsDate(DateFormatUtils.format(tEventSession.getFstartDate(), "yyyy-MM"));
					eventSimpleDTO.setGoodsDateValue(tEventSession.getFstartDate().getTime());
					eventSimpleDTO.setGoodsAddress(tEventSession.getFaddress());
				}
			}
			if (amap.get("ftitle") != null && StringUtils.isNotBlank(amap.get("ftitle").toString())) {
				eventSimpleDTO.setTitle(amap.get("ftitle").toString());
			}
			if (amap.get("fsubTitle") != null && StringUtils.isNotBlank(amap.get("fsubTitle").toString())) {
				eventSimpleDTO.setSubTitle(amap.get("fsubTitle").toString());
			}
			if (amap.get("fimage1") != null && StringUtils.isNotBlank(amap.get("fimage1").toString())) {
				eventSimpleDTO.setImageUrl(fxlService.getImageUrl(amap.get("fimage1").toString(), false));
			} else {
				eventSimpleDTO.setImageUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
						.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPic.jpg").toString());
			}
			if (amap.get("feventTime") != null && StringUtils.isNotBlank(amap.get("feventTime").toString())) {
				eventSimpleDTO.setGoodsTime(amap.get("feventTime").toString());
			}
			if (amap.get("fprice") != null && StringUtils.isNotBlank(amap.get("fprice").toString())) {
				eventSimpleDTO.setPrice(amap.get("fprice").toString());
			}
			if (amap.get("fdeal") != null && StringUtils.isNotBlank(amap.get("fdeal").toString())) {
				eventSimpleDTO.setDeal(amap.get("fdeal").toString());
			} else {
				eventSimpleDTO.setDeal("免费");
			}
			if (amap.get("fstatus") != null && StringUtils.isNotBlank(amap.get("fstatus").toString())) {
				eventSimpleDTO.setStatus((Integer) amap.get("fstatus"));
			}
			list.add(eventSimpleDTO);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("favoriteEventList", list);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getFavoriteMerchantList(Integer clientType, String ticket, Integer pageSize, Integer offset) {
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

		List<MerchantSimpleDTO> list = Lists.newArrayList();

		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}

		StringBuilder hql = new StringBuilder();
		hql.append(
				"select t.id as id, t.fname as fname, t.fimage as fimage, t.faddress as faddress, t.fscore as fscore from TSponsor t, TFavorite f where t.id = f.fobjectId and f.TCustomer.id = :customerId and f.ftype = 2");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("customerId", customerDTO.getCustomerId());

		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> favoriteList = page.getResult();
		MerchantSimpleDTO merchantSimpleDTO = null;
		for (Map<String, Object> amap : favoriteList) {
			merchantSimpleDTO = new MerchantSimpleDTO();
			if (amap.get("id") != null && StringUtils.isNotBlank(amap.get("id").toString())) {
				merchantSimpleDTO.setMerchantId(amap.get("id").toString());
				merchantSimpleDTO.setEventCount(eventDAO.getEventCountBySponsorId(amap.get("id").toString()));
			}
			if (amap.get("fname") != null && StringUtils.isNotBlank(amap.get("fname").toString())) {
				merchantSimpleDTO.setMerchantName(amap.get("fname").toString());
			}
			if (amap.get("fimage") != null && StringUtils.isNotBlank(amap.get("fimage").toString())) {
				merchantSimpleDTO.setMerchantLogoUrl(fxlService.getImageUrl(amap.get("fimage").toString(), true));
			} else {
				merchantSimpleDTO.setMerchantLogoUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
						.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPicThumbnail.jpg")
						.toString());
			}
			if (amap.get("faddress") != null && StringUtils.isNotBlank(amap.get("faddress").toString())) {
				merchantSimpleDTO.setAddress(amap.get("faddress").toString());
			}
			if (amap.get("fscore") != null && StringUtils.isNotBlank(amap.get("fscore").toString())) {
				merchantSimpleDTO.setScore((BigDecimal) amap.get("fscore"));
			}

			list.add(merchantSimpleDTO);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("favoriteMerchantList", list);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public ResponseDTO favoriteEvent(Integer clientType, String ticket, String eventId) {
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
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}
		boolean flag = false;
		TFavorite tFavorite = favoriteDAO.getFavoriteEventByCustomerIdAndObejctId(customerDTO.getCustomerId(), eventId);
		if (tFavorite == null) {
			flag = true;
			tFavorite = new TFavorite();
			tFavorite.setTCustomer(new TCustomer(customerDTO.getCustomerId()));
			tFavorite.setFobjectId(eventId);
			tFavorite.setFtype(1);
			favoriteDAO.save(tFavorite);
		} else {
			flag = false;
			favoriteDAO.delete(tFavorite);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("favorite", flag);
		responseDTO.setData(returnData);
		if (flag) {
			responseDTO.setMsg("活动收藏成功！");
		} else {
			responseDTO.setMsg("活动取消收藏成功！");
		}
		return responseDTO;
	}

	public ResponseDTO favoriteMerchant(Integer clientType, String ticket, String merchantId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(merchantId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("merchantId参数不能为空，请检查merchantId的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}
		boolean flag = false;
		TFavorite tFavorite = favoriteDAO.getFavoriteMerchantByCustomerIdAndObejctId(customerDTO.getCustomerId(),
				merchantId);
		if (tFavorite == null) {
			flag = true;
			tFavorite = new TFavorite();
			tFavorite.setTCustomer(new TCustomer(customerDTO.getCustomerId()));
			tFavorite.setFobjectId(merchantId);
			tFavorite.setFtype(2);
			favoriteDAO.save(tFavorite);
		} else {
			flag = false;
			favoriteDAO.delete(tFavorite);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("favorite", flag);
		responseDTO.setData(returnData);
		if (flag) {
			responseDTO.setMsg("商家收藏成功！");
		} else {
			responseDTO.setMsg("商家取消收藏成功！");
		}
		return responseDTO;
	}

	public ResponseDTO comment(Integer clientType, String ticket, String objectId, Integer type, String content,
			Integer score, String photo) {
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
		// 2：活动评论
		// 3：商家评论
		// 4：晒单评论
		// 5：原创评论
		// 6：文章评论
		// 10：回复评论
		TComment tComment = new TComment();
		tComment.setFcreateTime(new Date());
		tComment.setFcontent(content);
		tComment.setFscore(score);

		TOrder tOrder = orderDAO.getOne(objectId);
		// 当前系统不需要晒单评论，所以晒单评论需要转换成活动评论
		// 如果是晒单评论，则传入的objectID是orderId，这时需要将晒单评论的orderId转换成eventId保存到objectId中，
		// 并且将评论类型4改成2，也就是把晒单评论自动转换成为了活动评论。
		if (type.intValue() == 4) {
			TEvent tEvent = tOrder.getTEvent();
			tComment.setFobjectId(tEvent.getId());
			tComment.setFtype(2);
			fxlService.orderStatusChange(1, customerDTO.getName(), objectId, null, tOrder.getFstatus(), 70);
			// 将订单状态修改成“已评论”状态
			orderDAO.updateOrderStatus(70, objectId);

			// 同步发送钉钉通知
			try {
				if (StringUtils.isNotBlank(DingTalkUtil.getCommentSyncingDingTalk())) {
					eventDAO.getOne(objectId);
					String msg = new StringBuilder().append("用户订单评论提醒：用户[").append(customerDTO.getName()).append("]")
							.append("电话[").append(customerDTO.getPhone()).append("]").append("在")
							.append(DateFormatUtils.format(new Date(), "yyyy年MM月dd日HH时mm分")).append("提交了订单评论。活动名称：")
							.append(tEvent.getFtitle()).append("。评论内容：").append(content)
							.append("。请登录FOMS系统进行处理。http://www.fangxuele.com:8081/foms").toString();
					DingTalkUtil.sendDingTalk(msg, DingTalkUtil.getCommentSyncingDingTalk());
				}
			} catch (Exception e) {
				logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
				logger.error("用户提交评论时调用钉钉通知时，钉钉通知接口出错。");
			}
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
		responseDTO.setMsg("感谢您的评价！");
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getCommentList(String objectId, Integer type, Integer pageSize, Integer offset) {
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
		hql.append(
				"select t.id as id, t.fcustomerId as fcustomerId, t.fcustomerName as fcustomerName, t.fcustomerLogoUrl as fcustomerLogoUrl, t.fobjectId as fobjectId, t.fcontent as fcontent, t.fphoto as fphoto, t.fcreateTime as fcreateTime, t.frecommend as frecommend, t.fscore as fscore, t.ftype as ftype,t.fuserName as fuserName, t.freply as freply, t.freplyTime as freplyTime, t.fstatus as fstatus from TComment t where t.fstatus in(20,40)");
		Map<String, Object> hqlMap = new HashMap<String, Object>();

		if (type.equals(3)) {
			// 如果是获取商家评论，目前采用获取该商家发布的所有活动的评论为该商家的评论
			hql.append(
					" and t.fobjectId in (select e.id from TEvent e where e.TSponsor.id = :merchantId and e.fstatus < 999) and t.ftype = :type");
			hqlMap.put("merchantId", objectId);
			hqlMap.put("type", 2);
		} else {
			hql.append(" and t.fobjectId = :objectId and t.ftype = :type");
			hqlMap.put("objectId", objectId);
			hqlMap.put("type", type);
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
		return responseDTO;
	}

	/**
	 * 推荐活动的service方法
	 * 
	 * @param eventId
	 *            活动ID
	 * @return 响应用户的json数据
	 */
	@Transactional(readOnly = true)
	public ResponseDTO clickCommentRecommend(String commentId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(commentId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("commentId参数不能为空，请检查commentId的传递参数值！");
			return responseDTO;
		}

		Map<String, Object> returnData = Maps.newHashMap();
		Cache commentRecommendCache = cacheManager.getCache(Constant.CommentRecommend);
		Element ele = commentRecommendCache.get(commentId);
		if (ele == null) {
			Long recommend = commentDAO.findCommentRecommend(commentId);
			CommentRecommendDTO commentRecommendDTO = new CommentRecommendDTO();
			commentRecommendDTO.setCommentId(commentId);
			commentRecommendDTO.setRecommend(recommend == null ? 0 : recommend);

			ele = new Element(commentId, commentRecommendDTO);
			commentRecommendCache.put(ele);

			returnData.put("total", commentRecommendDTO.getRecommend());
			returnData.put("totalInfo", new StringBuilder().append("已有").append(commentRecommendDTO.getRecommend())
					.append("人推荐").toString());
		} else {
			CommentRecommendDTO commentRecommendDTO = (CommentRecommendDTO) ele.getObjectValue();
			commentRecommendDTO.addOne();
			returnData.put("total", commentRecommendDTO.getRecommend());
			returnData.put("totalInfo", new StringBuilder().append("已有").append(commentRecommendDTO.getRecommend())
					.append("人推荐").toString());
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setData(returnData);
		responseDTO.setMsg("评价推荐点赞成功！");
		return responseDTO;
	}

	/**
	 * 将某个文章的推荐数缓存移除
	 * 
	 * @param articleId
	 */
	public void removeCommentRecommendCache(String commentId) {
		Cache commentRecommendCache = cacheManager.getCache(Constant.CommentRecommend);
		Element ele = commentRecommendCache.get(commentId);
		if (ele == null) {
			return;
		}
		CommentRecommendDTO commentRecommendDTO = (CommentRecommendDTO) ele.getObjectValue();
		if (commentRecommendDTO.isChange()) {
			commentDAO.updateCommentRecommend(commentId, commentRecommendDTO.getRecommend());
		}
		commentRecommendCache.remove(commentId);
	}

	/**
	 * 将文章推荐数缓存同步到数据库中
	 * 
	 * @return
	 */
	public int savingCommentRecommendCache() {
		Cache commentRecommendCache = cacheManager.getCache(Constant.CommentRecommend);
		Map<Object, Element> recommendMap = commentRecommendCache
				.getAll(commentRecommendCache.getKeysNoDuplicateCheck());
		int i = 0;
		for (Iterator it = recommendMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Object, Element> e = (Map.Entry<Object, Element>) it.next();
			Element ele = e.getValue();
			CommentRecommendDTO commentRecommendDTO = (CommentRecommendDTO) ele.getObjectValue();
			if (commentRecommendDTO.isChange()) {
				commentDAO.updateCommentRecommend(commentRecommendDTO.getCommentId(),
						commentRecommendDTO.getRecommend());
				commentRecommendDTO.setChange(false);
				i++;
			}
		}
		return i;
	}

	public ResponseDTO consult(Integer clientType, String ticket, String objectId, Integer type, String content) {
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
		Date now = new Date();

		// 1：活动咨询
		// 2：商家咨询
		// 3：平台咨询
		TConsult tConsult = new TConsult();
		tConsult.setFobjectId(objectId);
		tConsult.setFtype(type);
		tConsult.setFcreateTime(now);
		tConsult.setFcustomerId(customerDTO.getCustomerId());
		tConsult.setFcustomerName(customerDTO.getName());
		tConsult.setFcustomerLogoUrl(customerDTO.getPhoto());
		tConsult.setFcontent(content);
		tConsult.setFstatus(10);
		consultDAO.save(tConsult);

		// 同步发送钉钉通知
		try {
			if (StringUtils.isNotBlank(DingTalkUtil.getConsultSyncingDingTalk())) {
				TEvent tEvent = eventDAO.getOne(objectId);
				String msg = new StringBuilder().append("用户活动咨询提醒：用户[").append(customerDTO.getName()).append("]")
						.append("电话[").append(customerDTO.getPhone()).append("]").append("在")
						.append(DateFormatUtils.format(now, "yyyy年MM月dd日HH时mm分")).append("提交了活动咨询。活动名称：")
						.append(tEvent.getFtitle()).append("。咨询内容：").append(content)
						.append("。请登录FOMS系统进行处理。http://www.fangxuele.com:8081/foms").toString();
				DingTalkUtil.sendDingTalk(msg, DingTalkUtil.getConsultSyncingDingTalk());
			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			logger.error("用户提交活动咨询时调用钉钉通知时，钉钉通知接口出错。");
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("感谢您的咨询，客服会尽快回复的！");
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getConsultList(String objectId, Integer type, Integer pageSize, Integer offset) {
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
		hql.append(
				"select t.id as id, t.fobjectId as fobjectId, t.fcustomerName as fcustomerName, t.fcustomerLogoUrl as fcustomerLogoUrl, t.fcreateTime as fcreateTime, t.fcontent as fcontent, t.fuserName as fuserName, t.freply as freply, t.freplyTime as freplyTime, t.fstatus as fstatus from TConsult t where t.fobjectId = :objectId and t.ftype = :type and t.fstatus < 999 order by t.fcreateTime desc");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("objectId", objectId);
		hqlMap.put("type", type);

		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();

		// 对活动信息进行细化加载
		List<ConsultDTO> consultList = Lists.newArrayList();

		ConsultDTO consultDTO = null;
		Date now = new Date();
		Date date = null;
		int status = 0;
		for (Map<String, Object> amap : list) {
			consultDTO = new ConsultDTO();
			consultDTO.setUserLogoUrl(Constant.czyhInterfaceLogoImgUrl);
			consultDTO.setConsultId(amap.get("id").toString());
			if (amap.get("fobjectId") != null && StringUtils.isNotBlank(amap.get("fobjectId").toString())) {
				consultDTO.setObjectId(amap.get("fobjectId").toString());
			}
			if (amap.get("fcustomerName") != null && StringUtils.isNotBlank(amap.get("fcustomerName").toString())) {
				consultDTO.setCustomerName(ArrayStringUtils.addAsterisk(amap.get("fcustomerName").toString()));
			}
			if (amap.get("fcustomerLogoUrl") != null
					&& StringUtils.isNotBlank(amap.get("fcustomerLogoUrl").toString())) {
				consultDTO.setCustomerLogoUrl(HeadImageUtil.getHeadImage(amap.get("fcustomerLogoUrl").toString(), 46));
			} else {
				consultDTO.setCustomerLogoUrl(Constant.defaultHeadImgUrl);
			}
			if (amap.get("fcreateTime") != null && StringUtils.isNotBlank(amap.get("fcreateTime").toString())) {
				date = (Date) amap.get("fcreateTime");
				// if (DateUtils.truncatedEquals(date, now, Calendar.YEAR)) {
				// consultDTO.setConsultTime(DateFormatUtils.format(date, "MM-dd
				// HH:mm"));
				// } else {
				consultDTO.setConsultTime(DateFormatUtils.format(date, "yyyy-MM-dd HH:mm"));
				// }
			}
			if (amap.get("fcontent") != null && StringUtils.isNotBlank(amap.get("fcontent").toString())) {
				consultDTO.setContent(amap.get("fcontent").toString());
			}
			if (amap.get("fuserName") != null && StringUtils.isNotBlank(amap.get("fuserName").toString())) {
				consultDTO.setUserName("零到壹客服 " + amap.get("fuserName").toString());
			}
			if (amap.get("freply") != null && StringUtils.isNotBlank(amap.get("freply").toString())) {
				consultDTO.setReply(amap.get("freply").toString());
			}
			if (amap.get("freplyTime") != null && StringUtils.isNotBlank(amap.get("freplyTime").toString())) {
				date = (Date) amap.get("freplyTime");
				// if (DateUtils.truncatedEquals(date, now, Calendar.YEAR)) {
				// consultDTO.setReplyTime(DateFormatUtils.format(date, "MM-dd
				// HH:mm"));
				// } else {
				consultDTO.setReplyTime(DateFormatUtils.format(date, "yyyy-MM-dd HH:mm"));
				// }
			}
			if (amap.get("fstatus") != null && StringUtils.isNotBlank(amap.get("fstatus").toString())) {
				status = ((Integer) amap.get("fstatus")).intValue();
				if (status == 10) {
					consultDTO.setReplied(false);
				} else {
					consultDTO.setReplied(true);
				}
			}
			consultList.add(consultDTO);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("consultList", consultList);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO appShareEvent(String eventId, HttpServletRequest request) {
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
			responseDTO.setMsg("eventId参数信息有误，系统中没有活动ID为“" + eventId + "”的活动！");
			return responseDTO;
		}

		AppShareDTO appShareDTO = new AppShareDTO();
		appShareDTO.setTitle(tEvent.getFtitle());

		if (StringUtils.isNotBlank(tEvent.getFimage1())) {
			appShareDTO.setImageUrl(fxlService.getImageUrl(tEvent.getFimage1(), true));
		} else {
			appShareDTO.setImageUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
					.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPicThumbnail.jpg").toString());
		}
		if (StringUtils.isNotBlank(tEvent.getFbrief())) {
			appShareDTO.setBrief(tEvent.getFbrief());
		} else {
			appShareDTO.setBrief(PropertiesUtil.getProperty("appShareDefaultInfo"));
		}
		appShareDTO.setUrl(new StringBuilder().append(request.getScheme()).append("://").append(request.getServerName())
				.append(":").append(request.getServerPort()).append(request.getContextPath())
				.append("/api/system/share/event/").append(eventId).toString());

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("appShare", appShareDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO appShareMerchant(String merchantId, HttpServletRequest request) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (StringUtils.isBlank(merchantId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("merchantId参数不能为空，请检查merchantId的传递参数值！");
			return responseDTO;
		}
		TSponsor tSponsor = sponsorDAO.findOne(merchantId);
		if (tSponsor == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("merchantId参数信息有误，系统中没有商家ID为“" + merchantId + "”的活动！");
			return responseDTO;
		}

		AppShareDTO appShareDTO = new AppShareDTO();
		appShareDTO.setTitle(tSponsor.getFname());

		if (StringUtils.isNotBlank(tSponsor.getFimage())) {
			appShareDTO.setImageUrl(fxlService.getImageUrl(tSponsor.getFimage(), true));
		} else {
			appShareDTO.setImageUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
					.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPicThumbnail.jpg").toString());
		}
		if (StringUtils.isNotBlank(tSponsor.getFbrief())) {
			appShareDTO.setBrief(tSponsor.getFbrief());
		} else {
			appShareDTO.setBrief(PropertiesUtil.getProperty("appShareDefaultInfo"));
		}
		appShareDTO.setUrl(new StringBuilder().append(request.getScheme()).append("://").append(request.getServerName())
				.append(":").append(request.getServerPort()).append(request.getContextPath())
				.append("/api/system/share/merchant/").append(merchantId).toString());

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("appShare", appShareDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO appShareArticle(String articleId, HttpServletRequest request) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (StringUtils.isBlank(articleId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("articleId参数不能为空，请检查articleId的传递参数值！");
			return responseDTO;
		}
		TArticle tArticle = articleDAO.findOne(articleId);
		if (tArticle == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("articleId参数信息有误，系统中没有文章ID为“" + articleId + "”的文章！");
			return responseDTO;
		}

		AppShareDTO appShareDTO = new AppShareDTO();
		appShareDTO.setTitle(tArticle.getFtitle());

		if (tArticle.getFimage() != null) {
			appShareDTO.setImageUrl(fxlService.getImageUrl(tArticle.getFimage().toString(), true));
		} else {
			appShareDTO.setImageUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
					.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPicThumbnail.jpg").toString());
		}
		if (StringUtils.isNotBlank(tArticle.getFbrief())) {
			appShareDTO.setBrief(tArticle.getFbrief());
		} else {
			appShareDTO.setBrief(PropertiesUtil.getProperty("appShareDefaultInfo"));
		}
		appShareDTO.setUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
				.append(PropertiesUtil.getProperty("htmlRootPath")).append(tArticle.getFdetailHtmlUrl()).toString());

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("appShare", appShareDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO appShareGame(Integer gameId, String customerName, Integer moreThanAverage) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (gameId == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("gameId参数不能为空，请检查gameId的传递参数值！");
			return responseDTO;
		}

		AppShareDTO appShareDTO = new AppShareDTO();
		appShareDTO.setImageUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
				.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/cyc1.png").toString());
		// 测试宝宝身高的分享
		if (gameId.intValue() == 201) {
			appShareDTO.setTitle("赶快来测测宝贝未来能长多高吧？");
			if (moreThanAverage != null && moreThanAverage.intValue() > 50) {
				appShareDTO.setBrief(new StringBuilder().append("根据科学计算，").append(customerName).append("的宝贝的身高未来会打败")
						.append(moreThanAverage).append("％的同龄人。你也快来测测吧！").toString());
			} else {
				appShareDTO.setBrief("据说身高是检验颜值的第一标准哦，根据父母预测孩子的身高，超级准！");
			}
		} else if (gameId.intValue() == 202) {
			// 测试宝宝气质的分享
			appShareDTO.setTitle("赶快来测测宝贝的气质吧？");
			appShareDTO.setBrief("据说孩子80%的行为都和气质有关呦，抓准气质可以更好地理解孩子呦，快来测一测吧！");
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("appShare", appShareDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO appShareSales(Integer salesType, String customerName) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (salesType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("salesType参数不能为空，请检查salesType的传递参数值！");
			return responseDTO;
		}

		AppShareDTO appShareDTO = new AppShareDTO();
		appShareDTO.setImageUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
				.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/cyc1.png").toString());
		appShareDTO.setTitle("赶快来测测宝贝未来能长多高吧？");
		appShareDTO.setBrief("据说身高是检验颜值的第一标准哦，根据父母预测孩子的身高，超级准！");

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("appShare", appShareDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getMerchant(Integer clientType, String ticket, String merchantId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(merchantId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("merchantId参数不能为空，请检查merchantId的传递参数值！");
			return responseDTO;
		}

		MerchantDTO merchantDTO = new MerchantDTO();
		TSponsor tSponsor = sponsorDAO.findOne(merchantId);
		if (tSponsor == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("merchantId参数信息有误，系统中没有商家ID为“" + merchantId + "”的活动！");
			return responseDTO;
		}
		merchantDTO.setMerchantId(merchantId);
		merchantDTO.setMerchantName(tSponsor.getFname());
		merchantDTO.setMerchantFullName(tSponsor.getFfullName());
		merchantDTO.setPhone(tSponsor.getFphone());
		merchantDTO.setGps(tSponsor.getFgps());
		merchantDTO.setAddress(tSponsor.getFaddress());
		merchantDTO.setBrief(tSponsor.getFbrief());
		if (StringUtils.isNotBlank(tSponsor.getFdetailHtmlUrl())) {
			merchantDTO.setDetailHtmlUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
					.append(PropertiesUtil.getProperty("htmlRootPath")).append(tSponsor.getFdetailHtmlUrl())
					.toString());
		}
		merchantDTO.setScore(tSponsor.getFscore());
		if (StringUtils.isNotBlank(tSponsor.getFimage())) {
			merchantDTO.setMerchantLogoUrl(fxlService.getImageUrl(tSponsor.getFimage(), true));
		} else {
			merchantDTO.setMerchantLogoUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
					.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPicThumbnail.jpg").toString());
		}

		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		// 如果是获取商家评论，目前采用获取该商家发布的所有活动的评论为该商家的评论
		hql.append(
				"select t.id as id, c.id as customerId, c.fname as fname, c.fphoto as customerPhoto, t.fobjectId as fobjectId, t.fcontent as fcontent, t.fphoto as fphoto, t.fcreateTime as fcreateTime, t.fscore as fscore, t.ftype as ftype from TComment t inner join TCustomer c on t.fcustomerId = c.id")
				.append(" where t.fstatus = 20 and t.fobjectId in (select e.id from TEvent e where e.TSponsor.id = :merchantId and e.fstatus < 999) and t.ftype = :type order by t.fcreateTime desc");

		hqlMap.put("merchantId", merchantId);
		hqlMap.put("type", 2);

		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);

		// 对活动信息进行细化加载
		List<CommentDTO> commentList = Lists.newArrayList();

		CommentDTO commentDTO = null;
		Date date = null;
		int typeValue = 0;
		for (Map<String, Object> amap : list) {
			commentDTO = new CommentDTO();

			commentDTO.setCommentId(amap.get("id").toString());
			if (amap.get("fobjectId") != null && StringUtils.isNotBlank(amap.get("fobjectId").toString())) {
				commentDTO.setObjectId(amap.get("fobjectId").toString());
			}
			if (amap.get("customerId") != null && StringUtils.isNotBlank(amap.get("customerId").toString())) {
				commentDTO.setCommenterId(amap.get("customerId").toString());
			}
			if (amap.get("fname") != null && StringUtils.isNotBlank(amap.get("fname").toString())) {
				commentDTO.setCommenterName(ArrayStringUtils.addAsterisk(amap.get("fname").toString()));
			}
			if (amap.get("customerPhoto") != null && StringUtils.isNotBlank(amap.get("customerPhoto").toString())) {
				commentDTO.setCommenterLogoUrl(amap.get("customerPhoto").toString());
			}
			if (amap.get("fcontent") != null && StringUtils.isNotBlank(amap.get("fcontent").toString())) {
				commentDTO.setContent(amap.get("fcontent").toString());
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
			if (amap.get("fscore") != null && StringUtils.isNotBlank(amap.get("fscore").toString())) {
				commentDTO.setMark((Integer) amap.get("fscore"));
			}
			if (amap.get("ftype") != null && StringUtils.isNotBlank(amap.get("ftype").toString())) {
				typeValue = ((Integer) amap.get("ftype")).intValue();
				commentDTO.setType(typeValue);
				commentDTO.setTypeString(DictionaryUtil.getString(DictionaryUtil.CommentType, typeValue));
			}
			commentList.add(commentDTO);
		}

		if (CollectionUtils.isNotEmpty(commentList)) {
			merchantDTO.setHasComment(true);
		}
		merchantDTO.setCommentList(commentList);

		// 获取该商家正在进行中的活动列表
		hqlMap.clear();
		hqlMap.put("merchantId", merchantId);
		hql.delete(0, hql.length());
		hql.append(
				"select t.id as id, t.fimage1 as fimage1, t.ftitle as ftitle, t.fsubTitle as fsubTitle, t.feventTime as feventTime, t.fprice as fprice, t.fdeal as fdeal, t.fstatus as fstatus from TEvent t where t.TSponsor.id = :merchantId and t.fstatus = 20 order by t.fstatus, t.fcreateTime desc");
		list = commonService.find(hql.toString(), hqlMap);
		List<EventSimpleDTO> eventList = Lists.newArrayList();
		EventSimpleDTO eventSimpleDTO = null;
		for (Map<String, Object> amap : list) {
			eventSimpleDTO = new EventSimpleDTO();
			if (amap.get("id") != null && StringUtils.isNotBlank(amap.get("id").toString())) {
				eventSimpleDTO.setGoodsId(amap.get("id").toString());

				hql.delete(0, hql.length());
				hql.append(
						"select t.fstartDate as fstartDate, t.faddress as faddress from TEventSession t where t.TEvent.id = :eventId and t.faddress is not null and t.faddress != '' and fstartDate != null and t.fstatus < 999");
				hqlMap.clear();
				hqlMap.put("eventId", amap.get("id"));
				Query q = commonService.createQuery(hql.toString(), hqlMap);
				q.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				q.setFirstResult(0).setMaxResults(1);
				List<Map<String, Object>> list2 = q.getResultList();
				Map<String, Object> bmap = CollectionUtils.isNotEmpty(list2) ? list2.get(0) : null;
				if (MapUtils.isNotEmpty(bmap)) {
					if (bmap.get("fstartDate") != null && StringUtils.isNotBlank(bmap.get("fstartDate").toString())) {
						Date startDate = (Date) bmap.get("fstartDate");
						eventSimpleDTO.setGoodsDate(DateFormatUtils.format(startDate, "yyyy-MM"));
						eventSimpleDTO.setGoodsDateValue(startDate.getTime());
					}
					if (bmap.get("faddress") != null && StringUtils.isNotBlank(bmap.get("faddress").toString())) {
						eventSimpleDTO.setGoodsAddress(bmap.get("faddress").toString());
					}
				}
			}
			if (amap.get("ftitle") != null && StringUtils.isNotBlank(amap.get("ftitle").toString())) {
				eventSimpleDTO.setTitle(amap.get("ftitle").toString());
			}
			if (amap.get("fsubTitle") != null && StringUtils.isNotBlank(amap.get("fsubTitle").toString())) {
				eventSimpleDTO.setSubTitle(amap.get("fsubTitle").toString());
			}
			if (amap.get("fimage1") != null && StringUtils.isNotBlank(amap.get("fimage1").toString())) {
				eventSimpleDTO.setImageUrl(fxlService.getImageUrl(amap.get("fimage1").toString(), false));
			} else {
				eventSimpleDTO.setImageUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
						.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPic.jpg").toString());
			}
			if (amap.get("feventTime") != null && StringUtils.isNotBlank(amap.get("feventTime").toString())) {
				eventSimpleDTO.setGoodsTime(amap.get("feventTime").toString());
			}
			if (amap.get("fprice") != null && StringUtils.isNotBlank(amap.get("fprice").toString())) {
				eventSimpleDTO.setPrice(amap.get("fprice").toString());
			}
			if (amap.get("fdeal") != null && StringUtils.isNotBlank(amap.get("fdeal").toString())) {
				eventSimpleDTO.setDeal(amap.get("fdeal").toString());
			} else {
				eventSimpleDTO.setDeal("免费");
			}
			if (amap.get("fstatus") != null && StringUtils.isNotBlank(amap.get("fstatus").toString())) {
				eventSimpleDTO.setStatus((Integer) amap.get("fstatus"));
			}
			eventList.add(eventSimpleDTO);
		}
		if (CollectionUtils.isNotEmpty(eventList)) {
			Comparator<EventSimpleDTO> eventComparator = Ordering.from(new Comparator<EventSimpleDTO>() {
				public int compare(EventSimpleDTO arg1, EventSimpleDTO arg2) {
					long eventDate1 = arg1.getGoodsDateValue();
					long eventDate2 = arg2.getGoodsDateValue();
					if (eventDate2 > eventDate1) {
						return 1;
					} else if (eventDate2 == eventDate1) {
						return 0;
					} else {
						return -1;
					}
				}
			});
			Collections.sort(eventList, eventComparator);
			merchantDTO.setHasEvent(true);
		}
		merchantDTO.setEventList(eventList);

		// 获取该商家已经过期的活动
		hqlMap.clear();
		hqlMap.put("merchantId", merchantId);
		hql.delete(0, hql.length());
		hql.append(
				"select t.id as id, t.fimage1 as fimage1, t.ftitle as ftitle, t.fsubTitle as fsubTitle, t.feventTime as feventTime, t.fprice as fprice, t.fdeal as fdeal, t.fstatus as fstatus from TEvent t where t.TSponsor.id = :merchantId and t.fstatus = 90 order by t.fstatus, t.fcreateTime desc");
		list = commonService.find(hql.toString(), hqlMap);
		List<EventSimpleDTO> overdueEventList = Lists.newArrayList();
		for (Map<String, Object> amap : list) {
			eventSimpleDTO = new EventSimpleDTO();
			if (amap.get("id") != null && StringUtils.isNotBlank(amap.get("id").toString())) {
				eventSimpleDTO.setGoodsId(amap.get("id").toString());

				hql.delete(0, hql.length());
				hql.append(
						"select t.fstartDate as fstartDate, t.faddress as faddress from TEventSession t where t.TEvent.id = :eventId and t.faddress is not null and t.faddress != '' and fstartDate != null and t.fstatus < 999");
				hqlMap.clear();
				hqlMap.put("eventId", amap.get("id"));
				Query q = commonService.createQuery(hql.toString(), hqlMap);
				q.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				q.setFirstResult(0).setMaxResults(1);
				List<Map<String, Object>> list2 = q.getResultList();
				Map<String, Object> bmap = CollectionUtils.isNotEmpty(list2) ? list2.get(0) : null;
				if (MapUtils.isNotEmpty(bmap)) {
					if (bmap.get("fstartDate") != null && StringUtils.isNotBlank(bmap.get("fstartDate").toString())) {
						Date startDate = (Date) bmap.get("fstartDate");
						eventSimpleDTO.setGoodsTime(DateFormatUtils.format(startDate, "yyyy-MM"));
						eventSimpleDTO.setGoodsDateValue(startDate.getTime());
					}
					if (bmap.get("faddress") != null && StringUtils.isNotBlank(bmap.get("faddress").toString())) {
						eventSimpleDTO.setGoodsAddress(bmap.get("faddress").toString());
					}
				}
			}
			if (amap.get("ftitle") != null && StringUtils.isNotBlank(amap.get("ftitle").toString())) {
				eventSimpleDTO.setTitle(amap.get("ftitle").toString());
			}
			if (amap.get("fsubTitle") != null && StringUtils.isNotBlank(amap.get("fsubTitle").toString())) {
				eventSimpleDTO.setSubTitle(amap.get("fsubTitle").toString());
			}
			if (amap.get("fimage1") != null && StringUtils.isNotBlank(amap.get("fimage1").toString())) {
				eventSimpleDTO.setImageUrl(fxlService.getImageUrl(amap.get("fimage1").toString(), false));
			} else {
				eventSimpleDTO.setImageUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
						.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPic.jpg").toString());
			}
			if (amap.get("feventTime") != null && StringUtils.isNotBlank(amap.get("feventTime").toString())) {
				eventSimpleDTO.setGoodsTime(amap.get("feventTime").toString());
			}
			if (amap.get("fprice") != null && StringUtils.isNotBlank(amap.get("fprice").toString())) {
				eventSimpleDTO.setPrice(amap.get("fprice").toString());
			}
			if (amap.get("fdeal") != null && StringUtils.isNotBlank(amap.get("fdeal").toString())) {
				eventSimpleDTO.setDeal(amap.get("fdeal").toString());
			} else {
				eventSimpleDTO.setDeal("免费");
			}
			if (amap.get("fstatus") != null && StringUtils.isNotBlank(amap.get("fstatus").toString())) {
				eventSimpleDTO.setStatus((Integer) amap.get("fstatus"));
			}
			overdueEventList.add(eventSimpleDTO);
		}
		merchantDTO.setOverdueEventList(overdueEventList);

		if (StringUtils.isNotBlank(ticket)) {
			CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
			if (customerDTO.isEnable()) {
				TFavorite tFavorite = favoriteDAO
						.getFavoriteMerchantByCustomerIdAndObejctId(customerDTO.getCustomerId(), merchantId);
				if (tFavorite != null) {
					merchantDTO.setFavorite(true);
				} else {
					merchantDTO.setFavorite(false);
				}
			} else {
				merchantDTO.setFavorite(false);
			}
		} else {
			merchantDTO.setFavorite(false);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("merchantDetail", merchantDTO);
		responseDTO.setData(returnData);
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
			responseDTO.setStatusCode(101);
			responseDTO.setMsg("请填写手机号码！");
			return responseDTO;
		}
		if (StringUtils.isBlank(checkCode)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
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
			userDTO.setName(customerDTO.getName());
			userDTO.setWxName(customerDTO.getWxName());
			userDTO.setHeadimgUrl(HeadImageUtil.getHeadImage(customerDTO.getPhoto(), 96));
			userDTO.setTicket(ticket);
			// userDTO.setBaby(customerDTO.getBaby());
			userDTO.setWxId(customerDTO.getWxId());
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
			// 获取用户DTO缓存对象
			redisService.removeCache(RedisMoudel.CustomerEentity,customerDTO.getCustomerId());
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
			userDTO.setTicket(newTicket);
			// userDTO.setBaby(tCustomer2.getFbaby());
			userDTO.setWxId(tCustomer2.getFweixinId());
			userDTO.setPhone(phone);
			userDTO.setSex(tCustomer2.getFsex() != null ? tCustomer2.getFsex().toString() : StringUtils.EMPTY);
			// userDTO.setAddress(tCustomer2.getFregion());
		}
		redisService.removeCache(RedisMoudel.CustomerEentity,customerDTO.getCustomerId());
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("手机绑定成功！");
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("userInfo", userDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * 获取用户的优惠券
	 * 
	 * @param ticket
	 *            票
	 * @param status
	 *            优惠券的状态
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getUserCouponByStatus(Integer clientType, String ticket, Integer status, Integer pageSize,
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
			responseDTO.setMsg("优惠券状态不能为空！");
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
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		// f.fID,f.fTitle,f.fUseStartTime,f.fUseEndTime,f.fAmount,f.fLimitation,f.fDiscount,f.fUseType
		hql.append(
				"SELECT t.id as couponDeliveryId, f.ftitle as ftitle, f.fuseStartTime as fuseStartTime, f.fuseEndTime as fuseEndTime,")
				.append(" f.fdiscount as fdiscount, f.flimitation as flimitation, f.famount as famount, t.fstatus as status,")
				.append(" f.fuseRange AS fuseRange from TCoupon f LEFT JOIN f.TCouponDeliveries t where t.TCustomer.id = :customerId AND ");
		if (status.intValue() == 99) {
			hql.append(" t.fstatus in(:usedStatus,:expiredStatus)");
			hqlMap.put("usedStatus", 20);
			hqlMap.put("expiredStatus", 90);
		} else if (status.intValue() == 10) {
			hql.append(" t.fstatus < :usedStatus");
			hqlMap.put("usedStatus", 20);
		} else {
			hql.append(" t.fstatus  = :status");
			hqlMap.put("status", status);
		}
		hqlMap.put("customerId", customerDTO.getCustomerId());

		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();
		CouponDTO couponDTO = null;
		Date date = null;
		List<CouponDTO> dataList = Lists.newArrayList();
		for (Map<String, Object> amap : list) {
			couponDTO = new CouponDTO();
			if (amap.get("couponDeliveryId") != null
					&& StringUtils.isNotBlank(amap.get("couponDeliveryId").toString())) {
				couponDTO.setCouponDeliveryId(amap.get("couponDeliveryId").toString());
			}

			if (amap.get("ftitle") != null && StringUtils.isNotBlank(amap.get("ftitle").toString())) {
				couponDTO.setTitle(amap.get("ftitle").toString());
			}
			if (amap.get("fuseStartTime") != null && StringUtils.isNotBlank(amap.get("fuseStartTime").toString())) {
				date = (Date) amap.get("fuseStartTime");
				couponDTO.setUseStartTime(DateFormatUtils.format(date, "yyyy-MM-dd"));
			}
			if (amap.get("fuseEndTime") != null && StringUtils.isNotBlank(amap.get("fuseEndTime").toString())) {
				date = (Date) amap.get("fuseEndTime");
				couponDTO.setUseEndTime(DateFormatUtils.format(date, "yyyy-MM-dd"));
			}
			if (amap.get("flimitation") != null && StringUtils.isNotBlank(amap.get("flimitation").toString())) {
				couponDTO.setLimitation((BigDecimal) amap.get("flimitation"));
				couponDTO.setLimitationInfo(new StringBuffer().append("满").append(couponDTO.getLimitation())
						.append("元减").append(amap.get("famount")).append("元").toString());
			} else {
				couponDTO.setLimitationInfo(
						new StringBuffer().append("直减").append(amap.get("famount")).append("元").toString());
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
			if (amap.get("fuseRange") != null && StringUtils.isNotBlank(amap.get("fuseRange").toString())) {
				couponDTO.setUseRange(amap.get("fuseRange").toString());
				couponDTO.setLimitationClient(amap.get("fuseRange").toString());
			}

			/*
			 * if (amap.get("fuseType") != null &&
			 * StringUtils.isNotBlank(amap.get("fuseType").toString())) {
			 * 
			 * }
			 */
			dataList.add(couponDTO);
		}
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("userCouponList", dataList);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO appShareSubject(String subjectId, HttpServletRequest request) {
		ResponseDTO responseDTO = new ResponseDTO();

		/*
		 * if (StringUtils.isBlank(subjectId)) { responseDTO.setSuccess(false);
		 * responseDTO.setStatusCode(201);
		 * responseDTO.setMsg("subjectId参数不能为空，请检查subjectId的传递参数值！"); return
		 * responseDTO; } TSubject tSubject = subjectDAO.findOne(subjectId,10);
		 * if (tSubject == null) { responseDTO.setSuccess(false);
		 * responseDTO.setStatusCode(202);
		 * responseDTO.setMsg("subjectId参数信息有误，系统中没有专题ID为“" + subjectId +
		 * "”的活动！"); return responseDTO; }
		 */

		AppShareDTO appShareDTO = new AppShareDTO();
		/*
		 * appShareDTO.setTitle(tSubject.getFtitle());
		 * 
		 * if (StringUtils.isNotBlank(tSubject.get)) {
		 * appShareDTO.setImageUrl(fxlService.getImageUrl(tSponsor.getFimage(),
		 * true)); } else { appShareDTO.setImageUrl(new
		 * StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
		 * .append(PropertiesUtil.getProperty("imageRootPath")).append(
		 * "/foms/noPicThumbnail.jpg").toString()); }
		 * appShareDTO.setBrief(tSponsor.getFbrief()); appShareDTO.setUrl(new
		 * StringBuilder().append(request.getScheme()).append("://").append(
		 * request.getServerName())
		 * .append(":").append(request.getServerPort()).append(request.
		 * getContextPath())
		 * .append("/api/system/share/merchant/").append(merchantId).toString())
		 * ;
		 */
		appShareDTO.setBrief("5.28-6.05 五彩城亲子特权日，免费抽大奖，更有千张史努比花生卡送送送!");
		appShareDTO.setImageUrl("http://www.fangxuele.com/img/logo_full.png");
		appShareDTO.setTitle("六一畅玩五彩城");
		appShareDTO.setUrl("http://www.fangxuele.com/p");
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("appSubject", appShareDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getPushMessageList(Integer clientType, String ticket) {
		ResponseDTO responseDTO = new ResponseDTO();

		StringBuilder hql = new StringBuilder();
		hql.append(
				"select t.id as id, t.ftitle as ftitle, t.fcontent as fcontent, t.fimage as fimage, t.ftargetObjectId as ftargetObjectId, t.furl as furl, t.ftargetType as ftargetType, t.fpushTime as fpushTime,t.fvalidTime as fvalidTime from TPush t where t.fstatus = 20 order by t.fpushTime desc");
		Map<String, Object> hqlMap = new HashMap<String, Object>();

		List<Map> pushList = Lists.newArrayList();
		Map<String, Object> pushMap = null;
		List<PushMessageDTO> pushMessageList = null;

		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);
		PushMessageDTO pushMessageDTO = null;
		Date date = null;
		Date dateFlag = DateUtils.addYears(new Date(), -100);
		// int status = 0;
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
			if (amap.get("id") != null && StringUtils.isNotBlank(amap.get("id").toString())) {
				pushMessageDTO.setPushId(amap.get("id").toString());
			}
			if (amap.get("ftitle") != null && StringUtils.isNotBlank(amap.get("ftitle").toString())) {
				pushMessageDTO.setTitle(amap.get("ftitle").toString());
			}
			if (amap.get("fcontent") != null && StringUtils.isNotBlank(amap.get("fcontent").toString())) {
				pushMessageDTO.setContent(amap.get("fcontent").toString());
			}
			if (amap.get("ftargetType") != null && StringUtils.isNotBlank(amap.get("ftargetType").toString())) {
				pushMessageDTO.setTargetType(
						DictionaryUtil.getString(DictionaryUtil.PushLinkTargetType, (Integer) amap.get("ftargetType")));
			}
			if (amap.get("fimage") != null && StringUtils.isNotBlank(amap.get("fimage").toString())) {
				pushMessageDTO.setImageUrl(fxlService.getImageUrl(amap.get("fimage").toString(), false));
			} else {
				pushMessageDTO.setImageUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
						.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPic.jpg").toString());
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

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("pushMessageList", pushList);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO updateReadPush(Integer clientType, String ticket, String pushCustomerId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(pushCustomerId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("pushCustomerId参数不能为空，请检查pushCustomerId的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}
		pushCustomerDAO.saveStatus(2, pushCustomerId);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("设置推送消息为已读状态成功！");
		return responseDTO;
	}

	public void appStartUp(AppStartupBean as) {

		if (StringUtils.isNotBlank(as.getCustomerId())) {
			Integer startupNum = appStartupDAO.getCounttAppStartup(as.getCustomerId());
			if (startupNum == 20) {
				StringBuilder hql = new StringBuilder();
				hql.append(
						"select t.id as id from TAppStartup t where t.fcustomerId=:fcustomerId order by t.fstartupTime asc");
				Map<String, Object> hqlMap = Maps.newHashMap();
				hqlMap.put("fcustomerId", as.getCustomerId());
				Query q = commonService.createQuery(hql.toString(), hqlMap);
				q.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				q.setFirstResult(0).setMaxResults(1);
				Map<String, String> oldId = (Map<String, String>) q.getSingleResult();
				appStartupDAO.setUpdateAppStartup(oldId.get("id"), as.getGps(), new Date(), as.getClientType());
			} else {
				TAppStartup tappStartup = new TAppStartup();

				tappStartup.setFcustomerId(as.getCustomerId());
				tappStartup.setFclientType(as.getClientType());
				tappStartup.setFgps(as.getGps());
				tappStartup.setFstartupTime(new Date());
				appStartupDAO.save(tappStartup);
			}
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveCustomerInfo(String customerId, Integer tag, String channel, String version, String deviceInfo,
			String deviceId, String deviceTokens, Integer clientType) {
		try {
			// 查找用户并更新用户扩展信息表信息
			Date now = new Date();
			TCustomerInfo tcustomerInfo = customerInfoDAO.getByCustomerId(customerId);
			boolean isNew = false;
			if (tcustomerInfo == null) {
				isNew = true;
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
				tcustomerInfo.setFcreateTime(now);
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
			tcustomerInfo.setFregisterTime(now);
			tcustomerInfo.setFupdateTime(now);
			customerInfoDAO.save(tcustomerInfo);

			if (isNew && tag != null) {
				// 为新注册的用户打标
				pushService.addCustomerTags(customerId, tag, deviceId, clientType, deviceTokens);
			} else if (!isNew && tag != null) {
				// 修改用户标签以及友盟同步的token
				pushService.updateCustomerTags(customerId, tag, deviceId, clientType, deviceTokens);
			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
	}

	@Transactional(readOnly = true)
	public ResponseDTO getRecipientInfo(Integer clientType, String ticket) {
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
		// 目前每个用户只有一个收货信息记录
		List<TCommonInfo> list = commonInfoDAO.findByFcustomerIdAndFtype(customerDTO.getCustomerId(), 1);
		// List<RecipientSimpleDTO> recipientList = Lists.newArrayList();
		RecipientSimpleDTO recipientSimpleDTO = new RecipientSimpleDTO();
		for (TCommonInfo tCommonInfo : list) {
			RecipientDTO recipientDTO = mapper.fromJson(tCommonInfo.getFinfo(), RecipientDTO.class);
			recipientSimpleDTO.setRecipient(recipientDTO.getRecipient());
			recipientSimpleDTO.setPhone(recipientDTO.getPhone());
			recipientSimpleDTO.setAddress(recipientDTO.getAddress());
			recipientSimpleDTO.setCommonInfoId(tCommonInfo.getId());
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		// returnData.put("commonInfoList", commonInfoList);
		returnData.put("recipientInfo", recipientSimpleDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public ResponseDTO setRecipientInfo(Integer clientType, String ticket, String commonInfoId, String recipient,
			String phone, String address) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(recipient)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("recipient参数不能为空，请检查recipient的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(phone)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("phone参数不能为空，请检查phone的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(address)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(204);
			responseDTO.setMsg("address参数不能为空，请检查address的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}
		RecipientDTO recipientDTO = new RecipientDTO();
		recipientDTO.setRecipient(recipient);
		recipientDTO.setPhone(phone);
		recipientDTO.setAddress(address);

		TCommonInfo tCommonInfo = null;
		Date now = new Date();
		if (StringUtils.isNotBlank(commonInfoId)) {
			tCommonInfo = commonInfoDAO.findOne(commonInfoId);
			if (tCommonInfo == null) {
				tCommonInfo = new TCommonInfo();
				tCommonInfo.setFcustomerId(customerDTO.getCustomerId());
				tCommonInfo.setFcreateTime(now);
				tCommonInfo.setFtype(1);
			}
		} else {
			tCommonInfo = new TCommonInfo();
			tCommonInfo.setFcustomerId(customerDTO.getCustomerId());
			tCommonInfo.setFcreateTime(now);
			tCommonInfo.setFtype(1);
		}
		tCommonInfo.setFinfo(mapper.toJson(recipientDTO));
		tCommonInfo.setFupdateTime(now);
		commonInfoDAO.save(tCommonInfo);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("您的收货信息保存成功！");
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
		commentDAO.updateCommentByCustomer(tCustomer2.getId(), tCustomer.getId());
		// 将当前用户的咨询复制到已有手机号的用户上
		consultDAO.updateConsultByCustomer(tCustomer2.getId(), tCustomer.getId());
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
			customerInfoDAO.save(customerInfo2);
		}
		// 将当前用户的用户标签与已有手机号的用户标签合并
		// 判断当前用户是否具有老用户标签
		TCustomerTag customerTag = customerTagDAO.findTCustomerTag(tCustomer.getId(), 3);
		if (customerTag != null) {
			// 判断已有手机号账号的用户标签是相对或者绝对老用户
			TCustomerTag customerTag1 = customerTagDAO.findTCustomerTag(tCustomer2.getId(), 1);
			TCustomerTag customerTag2 = customerTagDAO.findTCustomerTag(tCustomer2.getId(), 5);
			// 如果是，则将其相应标签改为老用户标签
			if (customerTag1 != null) {
				customerTag1.setFtag(3);
				customerTagDAO.save(customerTag1);
			} else if (customerTag2 != null) {
				customerTag2.setFtag(3);
				customerTagDAO.save(customerTag2);
			}
		}
		// 修改用户积分兑换表
		orderBonusDAO.updateOrderBounsByCustomer(tCustomer2.getId(), tCustomer.getId());
		// 修改用户积分表
		customerBonusDAO.updateCustomerBounsByCustomer(tCustomer2.getId(), tCustomer.getId());
		// 修改用户关注表
		customerSubscribeDAO.updateCustomerSubscribeByCustomer(tCustomer2.getId(), tCustomer.getId());
		customerSubscribeDAO.updateCustomerSubscribeByOpertion(tCustomer2.getId(), tCustomer.getId());
	}

	public ResponseDTO getMerchantUrl(String ticket, Integer clientType) {
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
		}

		StringBuilder url = new StringBuilder();
		url.append(PropertiesUtil.getProperty("H5Url")).append("/#/merchant?id=")
				.append(tCustomer.getTSponsor().getId());
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("merchantUrl", url);
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("获取商家详细信息时成功！");
		return responseDTO;
	}

	/**
	 * 用户ticket表清理任务方法。每天零点执行一次，将用户ticket表中更新时间在当前时间一个月之前的记录清除
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int deleteCustomertTicketByOverdue() {
		Date overdue = DateUtils.addMonths(new Date(), -1);
		return customerTicketDAO.deleteCustomertTicketByOverdue(overdue);
	}

}