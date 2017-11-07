package com.innee.czyhInterface.web.m.api.v1;

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
import com.innee.czyhInterface.service.v2.PersonalizedService;


@RestController("M_API_V1_PersonalizedController")
@RequestMapping(value = "/m/api/personalized", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PersonalizedController {

	private static final Logger logger = LoggerFactory.getLogger(PersonalizedController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);
	
	@Autowired
	private PersonalizedService personalizedService;
	
	@RequestMapping(value = "/getStatusRecommend", method = { RequestMethod.GET, RequestMethod.POST })
	public String getStatusRecommend(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "callback", required = false) String callback) {
		
		ResponseDTO responseDTO = null;
		try {
			responseDTO = personalizedService.getStatusRecommend(ticket, clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("判断当前用户是否填写过推荐信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	@RequestMapping(value = "/getUserRecommendList", method = { RequestMethod.GET, RequestMethod.POST })
	public String getUserRecommendList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "offset", required = false) Integer offset,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "callback", required = false) String callback) {
		
		ResponseDTO responseDTO = null;
		try {
			responseDTO = personalizedService.getUserRecommendList(ticket, clientType, offset, pageSize,
					request.getSession(true).getId());
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取用户推荐信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	@RequestMapping(value = "/addUserRecommend", method = { RequestMethod.GET, RequestMethod.POST })
	public String addUserRecommend(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "sex", required = false) Integer sex,
			@RequestParam(value = "age", required = false) Integer age,
			@RequestParam(value = "interest", required = false) String interest,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "callback", required = false) String callback) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = personalizedService.addUserRecommend(ticket,sex,age,clientType,interest);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("添加用户个性推荐信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	
	@RequestMapping(value = "/getInterestsList", method = { RequestMethod.GET, RequestMethod.POST })
	public String getInterestsList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "callback", required = false) String callback) {
		ResponseDTO responseDTO = new ResponseDTO();
		
		try {
			responseDTO = personalizedService.getInterestsList(size);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取个性推荐兴趣点列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	@RequestMapping(value = "/deleteInterests", method = { RequestMethod.GET, RequestMethod.POST })
	public String deleteInterests(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "eventId", required = false) String eventId,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "callback", required = false) String callback) {
		ResponseDTO responseDTO = new ResponseDTO();
		
		try {
			responseDTO = personalizedService.deleteInterests(ticket,eventId,clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取个性推荐兴趣点列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	
	@RequestMapping(value = "/addUserFeedBack", method = { RequestMethod.GET, RequestMethod.POST })
	public String addUserFeedBack(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "message", required = false) String message,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "callback", required = false) String callback) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = personalizedService.addUserFeedBack(ticket,message,clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("添加用户个性推荐信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
}