package com.innee.czyhInterface.web.m.api.v1.wx;

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
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.service.v2.WxService;

@RestController("M_API_V1_WxController")
@RequestMapping(value = "/m/api/wx", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
	@PostMapping(value = "/getWxUserInfo")
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
	 * @param openID
	 *            微信openid
	 * @param subscribeTime
	 *            取消关注时间
	 * @return
	 */
	@PostMapping(value = "/updateSceneUser")
	public String updateSceneUser(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "openID", required = false) String openID,
			@RequestParam(value = "subscribeTime", required = false) Long subscribeTime) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = wxService.updateSceneUser(openID, subscribeTime);
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
	 * 新增地推用户接口
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param sceneStr
	 *            地推渠道
	 * @param openID
	 *            用户微信openid
	 * @param subscribeTime
	 *            关注时间
	 * @return
	 */
	@PostMapping(value = "/saveSceneUser")
	public String saveSceneUser(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "sceneStr", required = false) String sceneStr,
			@RequestParam(value = "openID", required = false) String openID,
			@RequestParam(value = "subscribeTime", required = false) Long subscribeTime) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = wxService.saveSceneUser(sceneStr, openID, subscribeTime);
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
	 * 设置地推用户的经纬度接口（只设置第一次推送的经纬度）
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param openID
	 *            用户的openid
	 * @param gps
	 *            微信推送用户的经纬度
	 * @return
	 */
	@PostMapping(value = "/setSceneGPS")
	public String setSceneGPS(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "openID", required = false) String openID,
			@RequestParam(value = "gps", required = false) String gps) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = wxService.setSceneGPS(openID, gps);
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
}