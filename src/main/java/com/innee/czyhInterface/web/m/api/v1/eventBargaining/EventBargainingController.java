package com.innee.czyhInterface.web.m.api.v1.eventBargaining;

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
import com.innee.czyhInterface.service.v1.eventBargaining.EventBargainingService;
import com.innee.czyhInterface.service.v2.FxlService;
import com.innee.czyhInterface.util.IpUtil;

/**
 * 砍一砍
 * 
 * @author jinshengzhi
 *
 */
@RestController("M_API_V1_BargainingController")
@RequestMapping(value = "/m/api/bargaining", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class EventBargainingController {

	private static Logger logger = LoggerFactory.getLogger(EventBargainingController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private FxlService fxlService;

	@Autowired
	private EventBargainingService eventBargainingService;

	@PostMapping(value = "/eventBargainingList")
	public String eventBargainingList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = eventBargainingService.eventBargainingList(pageSize, offset);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取砍一砍商品列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 翻译活动专用接口
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param customerBargainingId
	 * @return
	 */
	@PostMapping(value = "/getGamesType")
	public String getGamesType(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "eventId", required = false) String eventId) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = eventBargainingService.getGamesType(eventId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取活动翻译接口出错");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/***************************************** 零到壹 ********************************************/

	@PostMapping(value = "/getMyBargain" )
	public String getMyBargain(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "goodsBargainingId", required = false) String eventBargainingId,
			@RequestParam(value = "customerBargainingId", required = false, defaultValue = "") String customerBargainingId,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = eventBargainingService.getMyBargain(eventBargainingId, ticket, clientType,
					customerBargainingId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("用户获取自己砍价首页报错");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/helpBargain" )
	public String helpBargain(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "customerBargainingId", required = false) String customerBargainingId,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "sign", required = false) String sign,
			@RequestParam(value = "ticket", required = false) String ticket) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = eventBargainingService.helpBargain(ticket, clientType,sign, customerBargainingId,IpUtil.getIpAddr(request));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("用户砍一砍报错");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/bargainToPayOrder" )
	public String bargainToPayOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "addressId", required = false) String addressId,
			@RequestParam(value = "remark", required = false) String remark,
			@RequestParam(value = "payType", required = false) Integer payType,
			@RequestParam(value = "payClientType", required = false, defaultValue = "1") Integer payClientType,
			@RequestParam(value = "channel", required = false) Integer channel,
			@RequestParam(value = "gps", required = false) String gps,
			@RequestParam(value = "customerBargainingId", required = false) String customerBargainingId,
			@RequestParam(value = "deviceId", required = false) String deviceId,
			@RequestParam(value = "sign", required = false) String sign) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = eventBargainingService.bargainToPayOrder(clientType, ticket, payClientType, addressId, remark,
					payType, IpUtil.getIpAddr(request), channel, gps, customerBargainingId, deviceId,sign);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("砍一砍获取支付信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/getbargainList" )
	public String getbargainList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "goodsBargainingId", required = false) String eventBargainingId,
			@RequestParam(value = "pageSize", required = false, defaultValue = "100") Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = eventBargainingService.getbargainList(ticket, eventBargainingId,
					pageSize, offset, clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取砍价英雄榜出错");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/getbargainHelpList" )
	public String getbargainHelpList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "customerBargainingId", required = false) String customerBargainingId,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = eventBargainingService.getbargainHelpList(ticket, customerBargainingId, pageSize, offset,
					clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取帮砍用户出错");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/getbargainShare" )
	public String getbargainShare(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "customerBargainingId", required = false) String customerBargainingId) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = eventBargainingService.getbargainShare(customerBargainingId, request);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("分享当前砍一砍出错");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	@PostMapping(value = "/bargainToFillOrder" )
	public String bargainToFillOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "customerBargainingId", required = false) String customerBargainingId) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = eventBargainingService.bargainTFillOrder(clientType, ticket, customerBargainingId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取砍一砍订单支付页面出错");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/getbargainShareApp" )
	public String getbargainShareApp(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "goodsBargainingId", required = false) String goodsBargainingId,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = eventBargainingService.getbargainShareApp(goodsBargainingId, ticket, clientType, request);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("分享当前砍一砍出错");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	@PostMapping(value = "/getbargaining" )
	public String getbargaining(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = eventBargainingService.getbargaining(ticket, pageSize, offset,
					clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取正在砍价出错");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	@PostMapping(value = "/getbarrage" )
	public String getbarrage(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "customerBargainingId", required = false) String customerBargainingId,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = eventBargainingService.getbarrage(ticket, customerBargainingId, pageSize, offset, clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取弹幕列表出错");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
}