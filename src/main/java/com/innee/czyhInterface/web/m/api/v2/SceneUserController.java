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
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.service.v2.SceneUserService;

@RestController("M_API_V2_SceneUserController")
@RequestMapping(value = "/m/api/v2/sceneUser", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SceneUserController {

	private static final Logger logger = LoggerFactory.getLogger(SceneUserController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private SceneUserService sceneUserService;

	@RequestMapping(value = "/getSceneUserInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public String getSceneUserInfo(HttpServletRequest request, HttpServletResponse response,
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
			@RequestParam(value = "subscribe", required = false) Boolean subscribe) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = sceneUserService.getSceneUserInfo(clientType, unionid, openid, nickname, headimgurl, country,
					province, city, sex, request, subscribe);
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
}