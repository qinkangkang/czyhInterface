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
import com.innee.czyhInterface.service.v2.DistributionService;

@RestController("M_API_V2_DistributionController")
@RequestMapping(value = "/m/api/v2/distribution", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class DistributionController {

	private static final Logger logger = LoggerFactory.getLogger(DistributionController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private DistributionService distributionService;

	@RequestMapping(value = "/getDistributionImage", method = { RequestMethod.GET, RequestMethod.POST })
	public String getDistributionImage(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "eventImage", required = false) String eventImage,
			@RequestParam(value = "openid", required = false) String openid,
			@RequestParam(value = "eventId", required = false) String eventId,
			@RequestParam(value = "eventTitle", required = false) String eventTitle,
			@RequestParam(value = "eventPrice", required = false) String eventPrice,
			@RequestParam(value = "eventfocus", required = false) String eventfocus,
			@RequestParam(value = "customerId", required = false) String customerId) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = distributionService.getDistributionImage(openid, eventImage, eventId, eventTitle, eventPrice,
					eventfocus, customerId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取返利海报时报错");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getSharePosterImage", method = { RequestMethod.GET, RequestMethod.POST })
	public String getSharePosterImage(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "openid", required = false) String openid,
			@RequestParam(value = "headUrl", required = false) String headUrl,
			@RequestParam(value = "customerId", required = false) String customerId) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = distributionService.getSharePosterImage(openid, headUrl, customerId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取分享海报时报错");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getMyReward", method = { RequestMethod.GET, RequestMethod.POST })
	public String getMyReward(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = distributionService.getMyReward(clientType, ticket);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取我的余额页面报错");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getFansOrderList", method = { RequestMethod.GET, RequestMethod.POST })
	public String getFansOrderList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = distributionService.getFansOrderList(pageSize, offset, clientType, ticket);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取粉丝订单列表报错");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getFansOrder", method = { RequestMethod.GET, RequestMethod.POST })
	public String getFansOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "orderId", required = false) String orderId,
			@RequestParam(value = "ticket", required = false) String ticket) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = distributionService.getFansOrder(clientType, ticket, orderId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取粉丝订单详情报错");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getWithdrawalHistory", method = { RequestMethod.GET, RequestMethod.POST })
	public String getWithdrawalHistory(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = distributionService.getWithdrawalHistory(pageSize, offset, clientType, ticket);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取历史提现列表报错");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getWithdrawalStatus", method = { RequestMethod.GET, RequestMethod.POST })
	public String getWithdrawalStatus(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "withdrawalId", required = false) String withdrawalId,
			@RequestParam(value = "ticket", required = false) String ticket) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = distributionService.getWithdrawalStatus(clientType, ticket, withdrawalId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取历史提现列表报错");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

}