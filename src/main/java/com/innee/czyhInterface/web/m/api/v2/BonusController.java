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
import com.innee.czyhInterface.service.v2.BonusService;
import com.innee.czyhInterface.util.IpUtil;
import com.innee.czyhInterface.util.PromptInfoUtil;

@RestController("M_API_V2_BonusController")
@RequestMapping(value = "/m/api/v2/bonus", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class BonusController {

	private static final Logger logger = LoggerFactory.getLogger(BonusController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private BonusService bonusService;

	@RequestMapping(value = "/changeBonus", method = { RequestMethod.GET, RequestMethod.POST })
	public String changeBonus(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "openid", required = false) String openid,
			@RequestParam(value = "qrcode", required = false) String qrcode,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "gps", required = false) String gps,
			@RequestParam(value = "subscribeTime", required = false) Long subscribeTime) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = bonusService.changeBonus(openid, qrcode, type, gps, subscribeTime, IpUtil.getIpAddr(request));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.czyhInterface_BONUS, "czyhInterface.web.bonus.changeBonus.failure"));
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/myBonusDeail", method = { RequestMethod.GET, RequestMethod.POST })
	public String myBonusDeail(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = bonusService.myBonusDeail(ticket, clientType, pageSize, offset);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO
					.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.czyhInterface_BONUS, "czyhInterface.web.bonus.myBonusDeail.failure"));
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/bonusEventList", method = { RequestMethod.GET, RequestMethod.POST })
	public String bonusEventList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = bonusService.bonusEventList(pageSize, offset, ticket, clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO
					.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.czyhInterface_BONUS, "czyhInterface.web.bonus.bonusEventList.failure"));
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getUserBonus", method = { RequestMethod.GET, RequestMethod.POST })
	public String getUserBonus(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = bonusService.getUserBonus(ticket, clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO
					.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.czyhInterface_BONUS, "czyhInterface.web.bonus.getUserBonus.failure"));
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/bonusOrderList", method = { RequestMethod.GET, RequestMethod.POST })
	public String bonusOrderList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = bonusService.bonusOrderList(pageSize, offset, ticket, clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO
					.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.czyhInterface_BONUS, "czyhInterface.web.bonus.bonusOrderList.failure"));
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/convertOrder", method = { RequestMethod.GET, RequestMethod.POST })
	public String convertOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "bonusEventId", required = false) String bonusEventId,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "remark", required = false) String remark,
			@RequestParam(value = "payType", required = false) Integer payType,
			@RequestParam(value = "payClientType", required = false, defaultValue = "1") Integer payClientType) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = bonusService.convertOrder(ticket, clientType, bonusEventId, name, phone, address, 
					remark, payType ,payClientType ,IpUtil.getIpAddr(request));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO
					.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.czyhInterface_BONUS, "czyhInterface.web.bonus.convertOrder.failure"));
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	@RequestMapping(value = "/myFansDeail", method = { RequestMethod.GET, RequestMethod.POST })
	public String myFansDeail(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = bonusService.myFansDeail(ticket, clientType, pageSize, offset);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO
					.setMsg("获取粉丝列表出错");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	
	@RequestMapping(value = "/addBonusAttention", method = { RequestMethod.GET, RequestMethod.POST })
	public String addBonusAttention(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "openid", required = false) String openid,
			@RequestParam(value = "qrcode", required = false) String qrcode) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = bonusService.AddBonusAttention(openid, qrcode);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.czyhInterface_BONUS, "czyhInterface.web.bonus.changeBonus.failure"));
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	@RequestMapping(value = "/addBonusInvitation", method = { RequestMethod.GET, RequestMethod.POST })
	public String addBonusInvitation(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "openid", required = false) String openid,
			@RequestParam(value = "gps", required = false) String gps,
			@RequestParam(value = "qrcode", required = false) String qrcode) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = bonusService.invitation(openid, qrcode, gps);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.czyhInterface_BONUS, "czyhInterface.web.bonus.changeBonus.failure"));
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/reduceBonusCancel", method = { RequestMethod.GET, RequestMethod.POST })
	public String reduceBonusCancel(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "openid", required = false) String openid,
			@RequestParam(value = "subscribeTime", required = false) Long subscribeTime) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = bonusService.reduceBonusCancel(openid, subscribeTime);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.czyhInterface_BONUS, "czyhInterface.web.bonus.changeBonus.failure"));
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
}