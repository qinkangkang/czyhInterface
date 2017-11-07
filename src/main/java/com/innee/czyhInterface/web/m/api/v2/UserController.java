package com.innee.czyhInterface.web.m.api.v2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.service.v2.CouponService;
import com.innee.czyhInterface.service.v2.CustomerService;

@RestController("M_API_V2_UserController")
@RequestMapping(value = "/m/api/v2/nouse/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CouponService couponService;

	/**
	 * 使用手机获取验证码登录平台
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param phone
	 *            请求发登录短信的手机号码
	 * @return
	 */
	@RequestMapping(value = "/sendLoginPwd", method = { RequestMethod.GET, RequestMethod.POST })
	public String sendLoginPwd(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "phone", required = false) String phone) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.sendLoginPwd(phone);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("发送短信校验码时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/sendCheckCode", method = { RequestMethod.GET, RequestMethod.POST })
	public String sendCheckCode(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "phone", required = false) String phone) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.sendCheckCode(phone);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("发送短信校验码时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/wxLogin", method = { RequestMethod.GET, RequestMethod.POST })
	public String wxLogin(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "unionid", required = false) String unionid,
			@RequestParam(value = "openid", required = false) String openid,
			@RequestParam(value = "nickname", required = false) String nickname,
			@RequestParam(value = "headimgurl", required = false) String headimgurl,
			@RequestParam(value = "country", required = false) String country,
			@RequestParam(value = "province", required = false) String province,
			@RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "sex", required = false) Integer sex,
			@RequestParam(value = "channel", required = false) String channel,
			@RequestParam(value = "version", required = false) String version,
			@RequestParam(value = "deviceInfo", required = false) String deviceInfo,
			@RequestParam(value = "deviceId", required = false) String deviceId,
			@RequestParam(value = "deviceTokens", required = false) String deviceTokens) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.wxLogin(clientType, unionid, openid, nickname, headimgurl, country, province,
					city, sex, channel, version, deviceInfo, deviceId, deviceTokens, request);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("微信用户登录时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 用手机登录平台
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param checkCode
	 *            短信验证码
	 * @param phone
	 *            登录的手机号码
	 * @return
	 */
	@RequestMapping(value = "/phoneLogin", method = { RequestMethod.GET, RequestMethod.POST })
	public String phoneLogin(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "checkCode", required = false) String checkCode,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "channel", required = false) String channel,
			@RequestParam(value = "version", required = false) String version,
			@RequestParam(value = "deviceInfo", required = false) String deviceInfo,
			@RequestParam(value = "deviceId", required = false) String deviceId,
			@RequestParam(value = "deviceTokens", required = false) String deviceTokens) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.phoneLogin(clientType, checkCode, phone, channel, version, deviceInfo,
					deviceId, deviceTokens, request);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("手机验证登录时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/webLogin", method = { RequestMethod.GET, RequestMethod.POST })
	public String webLogin(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "password", required = false) String password) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.webLoginC(clientType, phone, password, request);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("用户登录时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/logout", method = { RequestMethod.GET, RequestMethod.POST })
	public String logout(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.logoutC(clientType, ticket, request);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("用户登录时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/resetPhone", method = { RequestMethod.GET, RequestMethod.POST })
	public String resetPhone(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "checkCode", required = false) String checkCode) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.resetPhone(clientType, ticket, phone, checkCode);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("更换手机号码时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/resetPassword", method = { RequestMethod.GET, RequestMethod.POST })
	public String resetPassword(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "checkCode", required = false) String checkCode,
			@RequestParam(value = "password", required = false) String password) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.resetPassword(phone, checkCode, password);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("重置用户密码时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/uploadLogo", method = { RequestMethod.GET, RequestMethod.POST })
	public String uploadLogo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "file", required = false) String file) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.uploadLogo(clientType, ticket, file, null, true);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("上传用户头像图片时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/appUploadLogo", method = { RequestMethod.GET, RequestMethod.POST })
	public String appUploadLogo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "file", required = false) MultipartFile file) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.uploadLogo(clientType, ticket, null, file, false);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("上传用户头像图片时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getProfile", method = { RequestMethod.GET, RequestMethod.POST })
	public String getProfile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.getProfile(clientType, ticket);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取用户详细信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/saveProfile", method = { RequestMethod.GET, RequestMethod.POST })
	public String saveProfile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "sex", required = false) Integer sex) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.saveProfile(clientType, ticket, name, sex);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("保存用户详细信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getFavoriteEventList", method = { RequestMethod.GET, RequestMethod.POST })
	public String getFavoriteEventList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.getFavoriteEventList(clientType, ticket, pageSize, offset);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取用户已收藏活动列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getFavoriteMerchantList", method = { RequestMethod.GET, RequestMethod.POST })
	public String getFavoriteMerchantList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.getFavoriteMerchantList(clientType, ticket, pageSize, offset);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取用户已收藏商家列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/favoriteEvent", method = { RequestMethod.GET, RequestMethod.POST })
	public String favoriteEvent(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "eventId", required = false) String eventId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.favoriteEvent(clientType, ticket, eventId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("收藏活动时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/favoriteMerchant", method = { RequestMethod.GET, RequestMethod.POST })
	public String favoriteMerchant(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "merchantId", required = false) String merchantId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.favoriteMerchant(clientType, ticket, merchantId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("收藏商家时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/comment", method = { RequestMethod.GET, RequestMethod.POST })
	public String comment(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "objectId", required = false) String objectId,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "content", required = false) String content,
			@RequestParam(value = "mark", required = false) Integer score,
			@RequestParam(value = "photo", required = false) String photo) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.comment(clientType, ticket, objectId, type, content, score, photo);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("用户提交评价时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getCommentList", method = { RequestMethod.GET, RequestMethod.POST })
	public String getCommentList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "objectId", required = false) String objectId,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.getCommentList(objectId, type, pageSize, offset);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取评论列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 给活动评价点赞的方法
	 * 
	 * @param request
	 *            用户请求
	 * @param response
	 *            服务器响应
	 * @param callback
	 *            jsonp跨域回调参数
	 * @param eventId
	 *            活动ID
	 * @return 响应用户的json数据
	 */
	@RequestMapping(value = "/clickCommentRecommend", method = { RequestMethod.GET, RequestMethod.POST })
	public String clickCommentRecommend(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "commentId", required = false) String commentId) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.clickCommentRecommend(commentId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("推荐这个活动时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/consult", method = { RequestMethod.GET, RequestMethod.POST })
	public String consult(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "objectId", required = false) String objectId,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "content", required = false) String content) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.consult(clientType, ticket, objectId, type, content);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("用户提交咨询时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getConsultList", method = { RequestMethod.GET, RequestMethod.POST })
	public String getConsultList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "objectId", required = false) String objectId,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.getConsultList(objectId, type, pageSize, offset);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取咨询列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/appShareEvent", method = { RequestMethod.GET, RequestMethod.POST })
	public String appShareEvent(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "eventId", required = false) String eventId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.appShareEvent(eventId, request);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("APP获取活动分享时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/appShareMerchant", method = { RequestMethod.GET, RequestMethod.POST })
	public String appShareMerchant(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "merchantId", required = false) String merchantId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.appShareMerchant(merchantId, request);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("APP获取商家分享时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/appShareArticle", method = { RequestMethod.GET, RequestMethod.POST })
	public String appShareArticle(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "articleId", required = false) String articleId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.appShareArticle(articleId, request);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("APP获取文章分享时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/appShareGame", method = { RequestMethod.GET, RequestMethod.POST })
	public String appShareGame(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "gameId", required = false) Integer gameId,
			@RequestParam(value = "customerName", required = false) String customerName,
			@RequestParam(value = "moreThanAverage", required = false) Integer moreThanAverage) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.appShareGame(gameId, customerName, moreThanAverage);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("APP获取游戏分享时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/appShareSales", method = { RequestMethod.GET, RequestMethod.POST })
	public String appShareSales(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "salesType", required = false) Integer salesType,
			@RequestParam(value = "customerName", required = false) String customerName) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.appShareSales(salesType, customerName);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("促销活动分享时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getWxJssdk", method = { RequestMethod.GET, RequestMethod.POST })
	public String getWxJssdk(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "url", required = false) String url) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.getWxJssdk(url);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("微信用户获取JSSDK使用权限签名时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getWxAccsessToken", method = { RequestMethod.GET, RequestMethod.POST })
	public String getWxAccsessToken(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.getWxAccsessToken();
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("微信用户获取AccsessToken时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getWxJsApiTicket", method = { RequestMethod.GET, RequestMethod.POST })
	public String getWxJsApiTicket(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.getWxJsApiTicket();
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("微信用户获取JsApiTicket时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 用户绑定手机
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param ticket
	 *            票
	 * @param phone
	 *            手机号码
	 * @param checkCode
	 *            验证码
	 * @return
	 */
	@RequestMapping(value = "/bindPhone", method = { RequestMethod.GET, RequestMethod.POST })
	public String bindPhone(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "checkCode", required = false) String checkCode) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.bindPhone(clientType, ticket, phone, checkCode);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("更换手机号码时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 获取用户的优惠券
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param ticket
	 *            票
	 * @param status
	 *            优惠卷的状态
	 * @return
	 */
	@RequestMapping(value = "/getUserCouponByStatus", method = { RequestMethod.GET, RequestMethod.POST })
	public String getUserCouponByStatus(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = couponService.getUserCouponByStatus(clientType, ticket, status, pageSize, offset);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取优惠券时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 用户领取优惠券
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param couponId
	 *            优惠券id
	 * @param ticket
	 *            用户票
	 * @return
	 */
	@RequestMapping(value = "/receiveCoupon", method = { RequestMethod.GET, RequestMethod.POST })
	public String receiveCoupon(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "couponId", required = false) String couponId,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "type", required = false, defaultValue = "0") Integer type) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = couponService.receiveCoupon(clientType, couponId, ticket, type);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("领取优惠券出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 专题分享
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param merchantId
	 * @return
	 */
	@RequestMapping(value = "/appShareSubject", method = { RequestMethod.GET, RequestMethod.POST })
	public String appShareSubject(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "subjectId", required = false) String subjectId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.appShareSubject(subjectId, request);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("APP获取商家分享时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getPushMessageList", method = { RequestMethod.GET, RequestMethod.POST })
	public String getPushMessageList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.getPushMessageList(clientType, ticket);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取用户推送信息列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/updateReadPush", method = { RequestMethod.GET, RequestMethod.POST })
	public String updateReadPush(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "pushCustomerId", required = false) String pushCustomerId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.updateReadPush(clientType, ticket, pushCustomerId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("设置推送消息为已读状态时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getRecipientInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public String getRecipientInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.getRecipientInfo(clientType, ticket);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("查看收货信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/setRecipientInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public String setRecipientInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "commonInfoId", required = false) String commonInfoId,
			@RequestParam(value = "recipient", required = false) String recipient,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "address", required = false) String address) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.setRecipientInfo(clientType, ticket, commonInfoId, recipient, phone, address);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("保存收货信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

}