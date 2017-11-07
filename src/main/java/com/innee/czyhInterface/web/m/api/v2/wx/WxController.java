package com.innee.czyhInterface.web.m.api.v2.wx;

import java.util.Date;

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
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.service.v2.WxService;

@RestController("M_API_V2_WxController")
@RequestMapping(value = "/m/api/v2/wx", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class WxController {

	private static final Logger logger = LoggerFactory.getLogger(WxController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private WxService wxService;

	/**
	 * 根据code获取用户授权信息
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/getWxUserInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public String getWxUserInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "code", required = false) String code) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = wxService.getWxUserInfo(code, request);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取用户信息出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 地推用户取消关注接口
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param sceneStr
	 *            地推渠道
	 * @param openid
	 *            微信openid
	 * @param subscribeTime
	 *            取消关注时间
	 * @return
	 */
	@RequestMapping(value = "/updateSceneUser", method = { RequestMethod.GET, RequestMethod.POST })
	public String updateSceneUser(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "openid", required = false) String openid,
			@RequestParam(value = "subscribeTime", required = false) Long subscribeTime) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = wxService.updateSceneUser(openid, subscribeTime);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("递推用户取消关注时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 新增地推用户接口
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param sceneStr
	 *            地推渠道
	 * @param openid
	 *            用户微信openid
	 * @param subscribeTime
	 *            关注时间
	 * 
	 * @return
	 */
	@RequestMapping(value = "/saveSceneUser", method = { RequestMethod.GET, RequestMethod.POST })
	public String saveSceneUser(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "sceneStr", required = false) String sceneStr,
			@RequestParam(value = "openid", required = false) String openid,
			@RequestParam(value = "subscribeTime", required = false) Long subscribeTime) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = wxService.saveSceneUser(sceneStr, openid, subscribeTime);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("新增递推用户时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 设置地推用户的经纬度接口（只设置第一次推送的经纬度）
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param openid
	 *            用户的openid
	 * @param gps
	 *            微信推送用户的经纬度
	 * @return
	 */
	@RequestMapping(value = "/setSceneGPS", method = { RequestMethod.GET, RequestMethod.POST })
	public String setSceneGPS(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "openid", required = false) String openid,
			@RequestParam(value = "gps", required = false) String gps) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = wxService.setSceneGPS(openid, gps);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取用户信息出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getPosterImage", method = { RequestMethod.GET, RequestMethod.POST })
	public String getPosterImage(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "qrcodeUrl", required = false) String qrcodeUrl,
			@RequestParam(value = "headUrl", required = false) String headUrl,
			@RequestParam(value = "openid", required = false) String openid) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = wxService.doPoster(qrcodeUrl, headUrl, openid);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("生成活动海报时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getChannelType", method = { RequestMethod.GET, RequestMethod.POST })
	public String getChannelType(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "openid", required = false) String openid) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = wxService.getChannelType(openid);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("判断用户渠道类型时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getOpenIdToCustomerId", method = { RequestMethod.GET, RequestMethod.POST })
	public String getOpenIdToCustomerId(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "openid", required = false) String openid) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = wxService.getOpenIdToCustomerId(openid);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("判断是用户是否存在！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getSceneUserExist", method = { RequestMethod.GET, RequestMethod.POST })
	public String getSceneUserExist(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "openid", required = false) String openid) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = wxService.getSceneUserExist(openid);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("判断是用户是否存在！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	/**
	 * 增加积分扫码关注
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param openId
	 * @return
	 */
	@RequestMapping(value = "/addBounsQrSubscribe", method = { RequestMethod.GET, RequestMethod.POST })
	public String addBounsQrSubscribe(HttpServletRequest request, HttpServletResponse response,
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
			@RequestParam(value = "qrCode", required = false) String qrCode,
			@RequestParam(value = "gps", required = false) String gps,
			@RequestParam(value = "subscribeTime", required = false) Long subscribeTime) {
		
		ResponseDTO responseDTO = null;
		try {
			responseDTO = wxService.addBounsQrSubscribe(clientType, unionid, openid, nickname, headimgurl, country,
					province, city, sex, qrCode, gps, subscribeTime, request);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("添加新用户！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/updateUserInfoWxPoster", method = { RequestMethod.GET, RequestMethod.POST })
	public String updateUserInfoWxPoster(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "openid", required = false) String openid,
			@RequestParam(value = "fpointCode", required = false) String fpointCode,
			@RequestParam(value = "finvalidTime", required = false) Date finvalidTime) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = wxService.updateUserInfoWxPoster(openid, fpointCode, finvalidTime);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("修改用户附加信息！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getUserInfoAndWxPoster", method = { RequestMethod.GET, RequestMethod.POST })
	public String getUserInfoAndWxPoster(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "openid", required = false) String openid) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = wxService.getUserInfoWxPoster(openid);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取用户附加信息！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getMAXPointCode", method = { RequestMethod.GET, RequestMethod.POST })
	public String getMAXPointCode(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = wxService.getMAXPointCode();
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取用户附加信息！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getPosterSUser", method = { RequestMethod.GET, RequestMethod.POST })
	public String getPosterSUser(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "openid", required = false) String openid) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = wxService.getPosterSUser(openid);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取海报关注人！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
}