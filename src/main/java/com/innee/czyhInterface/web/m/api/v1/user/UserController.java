package com.innee.czyhInterface.web.m.api.v1.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.service.v1.user.UserService;
import com.innee.czyhInterface.util.IpUtil;

@RestController("M_API_V1_UserController")
@RequestMapping(value = "/m/api/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private UserService userService;

	/**
	 * 发送短信验证码
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param phone
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/sendLoginSms")
	public String sendLoginSms(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "sign", required = false) String sign) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = userService.sendLoginSms(phone, clientType, sign, IpUtil.getIpAddr(request));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(500);
			responseDTO.setMsg("发送短信校验码时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 发送短信验证码
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param phone
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/getRetToken")
	public String getRetToken(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false) Integer clientType) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = userService.getRetToken(clientType, IpUtil.getIpAddr(request));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(500);
			responseDTO.setMsg("获取令牌时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 手机验证码登录
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping(value = "/phoneLogin")
	public String phoneLogin(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "checkCode", required = false) String checkCode,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "channel", required = false) String channel,
			@RequestParam(value = "version", required = false) String version,
			@RequestParam(value = "deviceInfo", required = false) String deviceInfo,
			@RequestParam(value = "deviceId", required = false) String deviceId,
			@RequestParam(value = "deviceTokens", required = false) String deviceTokens,
			@RequestParam(value = "followCustomerId", required = false) String followCustomerId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = userService.phoneLogin(clientType, checkCode, phone, channel, version, deviceInfo, deviceId,
					deviceTokens, followCustomerId, request);
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

	/**
	 * 微信登录接口
	 * 
	 * @return
	 */
	@PostMapping(value = "/wechatLogin")
	public String wechatLogin(HttpServletRequest request, HttpServletResponse response,
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
			@RequestParam(value = "deviceTokens", required = false) String deviceTokens,
			@RequestParam(value = "followCustomerId", required = false) String followCustomerId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = userService.wechatLogin(clientType, unionid, openid, nickname, headimgurl, country, province,
					city, sex, channel, version, deviceInfo, deviceId, deviceTokens, followCustomerId, request);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(500);
			responseDTO.setMsg("微信用户登录时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = userService.logoutC(clientType, ticket, request);
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

	@PostMapping(value = "/getWechatJssdk")
	public String getWechatJssdk(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "url", required = false) String url) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = userService.getWechatJssdk(url);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(500);
			responseDTO.setMsg("微信用户获取JSSDK使用权限签名时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/getAddressList")
	public String getConsultList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "customerId", required = false) String customerId,
			@RequestParam(value = "defaultAddr", required = false) Integer defaultAddr,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = userService.getAddressList(customerId, defaultAddr, pageSize, offset);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(500);
			responseDTO.setMsg("获取用户收货地址列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/saveUpdateMeAddress")
	public String saveUpdateMeAddress(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "customerId", required = false) String customerId,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "region", required = false) String region,
			@RequestParam(value = "cityId", required = false) Integer cityId,
			@RequestParam(value = "street", required = false) String street,
			@RequestParam(value = "defaultstatus", required = false, defaultValue = "0") Integer defaultstatus) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = userService.saveUpdateMeAddress(id, customerId, name, phone, region, street, defaultstatus,
					cityId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(500);
			responseDTO.setMsg("用户修改收货地址信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/delAddress")
	public String delAddress(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "customerId", required = false) String customerId) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = userService.delAddress(id, customerId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(500);
			responseDTO.setMsg("用户删除收货地址信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 微信绑定手机
	 * 
	 * @return
	 */
	@PostMapping(value = "/bindPhone")
	public String bindPhone(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "checkCode", required = false) String checkCode) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = userService.bindPhone(clientType, ticket, phone, checkCode);
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
	 * 用户绑定微信
	 * 
	 * @return
	 */
	@PostMapping(value = "/bindWechat")
	public String bindWechat(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "wxName", required = false) String wxName,
			@RequestParam(value = "country", required = false) String country,
			@RequestParam(value = "province", required = false) String province,
			@RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "headimgurl", required = false) String headimgurl,
			@RequestParam(value = "sex", required = false) Integer sex,
			@RequestParam(value = "openid", required = false) String openid,
			@RequestParam(value = "unionid", required = false) String unionid) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = userService.bindWechat(clientType, ticket, wxName, country, province, city, headimgurl, sex,
					openid, unionid);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("绑定微信时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/getProfile")
	public String getProfile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = userService.getProfile(clientType, ticket);
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

	@PostMapping(value = "/saveProfile")
	public String saveProfile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "sex", required = false) Integer sex,
			@RequestParam(value = "baby", required = false) String baby) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = userService.saveProfile(clientType, ticket, name, sex, baby);
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

	@PostMapping(value = "/comment")
	public String comment(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "orderId", required = false) String orderId,
			@RequestParam(value = "objectId", required = false) String objectId,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "content", required = false) String content,
			@RequestParam(value = "mark", required = false) Integer score,
			@RequestParam(value = "photo", required = false) String photo) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = userService.comment(clientType, ticket, objectId, orderId, type, content, score, photo);
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

	@PostMapping(value = "/getCommentList")
	public String getCommentList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "objectId", required = false) String objectId,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "distinction", required = false, defaultValue = "0") Integer distinction,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = userService.getCommentList(objectId, type, distinction, pageSize, offset);
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

	@PostMapping(value = "/uploadLogoWeb")
	public String uploadLogo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "file", required = false) String file) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		ResponseDTO responseDTO = null;
		try {
			responseDTO = userService.uploadLogo(clientType, ticket, file, null, true);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("web上传用户头像图片时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/uploadLogoApp")
	public String appUploadLogo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "file", required = false) MultipartFile file) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = userService.uploadLogo(clientType, ticket, null, file, false);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("app上传用户头像图片时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/sendCheckCode")
	public String sendCheckCode(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "sign", required = false) String sign) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = userService.sendCheckCode(phone,clientType, sign, IpUtil.getIpAddr(request));
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

	@PostMapping(value = "/getDiscountNum")
	public String getDiscountNum(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = userService.getDiscountNum(ticket, clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取我的页面附加数据时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/checkCustomerNew")
	public String checkCustomerNew(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = userService.checkCustomerNew(ticket, clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("判断是否新用户时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
}