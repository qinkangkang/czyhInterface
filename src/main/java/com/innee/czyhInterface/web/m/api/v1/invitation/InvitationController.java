package com.innee.czyhInterface.web.m.api.v1.invitation;

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
import com.innee.czyhInterface.service.v1.coupon.CouponService;
import com.innee.czyhInterface.service.v1.invitation.InvitationService;

@RestController("M_API_V1_InvitationController")
@RequestMapping(value = "/m/api/invitation", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class InvitationController {

	private static final Logger logger = LoggerFactory.getLogger(InvitationController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private InvitationService invitationService;

	/**
	 * 邀请分享
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param code
	 * @return
	 */
	@PostMapping(value = "/appShareSign")
	public String appShareGoods(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = invitationService.appShareSign(ticket, clientType,request);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("分享邀请朋友时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 获取邀请人列表
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param ticket
	 * @param clientType
	 * @return
	 */
	@PostMapping(value = "/getInvitationList")
	public String getInvitationList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = invitationService.getInvitationList(ticket, clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取邀请人列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	/**
	 * 获取邀请人列表排行榜
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param ticket
	 * @param clientType
	 * @return
	 */
	@PostMapping(value = "/getInvitationTop")
	public String getInvitationTop(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = invitationService.getInvitationTop(ticket, clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取邀请人排行榜时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

}