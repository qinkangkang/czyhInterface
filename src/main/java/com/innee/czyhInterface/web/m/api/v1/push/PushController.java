package com.innee.czyhInterface.web.m.api.v1.push;

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
import com.innee.czyhInterface.service.v1.push.PushService;
import com.innee.czyhInterface.service.v2.CustomerService;

@RestController("M_API_V1_PushController")
@RequestMapping(value = "/m/api/push", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PushController {

	private static final Logger logger = LoggerFactory.getLogger(PushController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private PushService pushService;

	@PostMapping(value = "/getMessageList")
	public String getMessageList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = pushService.getMessageList(clientType, ticket);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获促销推送信息列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/updateReadPush")
	public String updateReadPush(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "pushId", required = false) String pushId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = pushService.updateReadPush(clientType, ticket, pushId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("设置促销推送消息为已读状态时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/getCustomerMessageList")
	public String getCustomerMessageList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = pushService.getCustomerMessageList(clientType, ticket);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取系统推送信息列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/updateCustomerReadPush")
	public String updateCustomerReadPush(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "pushId", required = false) String pushId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = pushService.updateCustomerReadPush(clientType, ticket, pushId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("设置系统推送消息为已读状态时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	@PostMapping(value = "/push")
	public String push(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "ticket", required = false) String ticket) {

		ResponseDTO responseDTO = null;
		try {
			if(type==1){
				pushService.couponOverdue("5yuan", ticket, "3b91f4b7-e398-4f07-82ad-d13bce46e21a", "ll");
			}else if(type==2){
				pushService.toPaid("1234567", "64be0a71-1eb3-48d1-bb42-4a96a1c4ff88", ticket, "3b91f4b7-e398-4f07-82ad-d13bce46e21a");
			}else if(type==3){
				pushService.successPayment("1234567", "64be0a71-1eb3-48d1-bb42-4a96a1c4ff88", ticket, "3b91f4b7-e398-4f07-82ad-d13bce46e21a");
			}else if(type==4){
				pushService.confirmGoods("1234567","123213213", "64be0a71-1eb3-48d1-bb42-4a96a1c4ff88", ticket, "3b91f4b7-e398-4f07-82ad-d13bce46e21a");
			}else if(type==5){
				pushService.refund("1234567", "64be0a71-1eb3-48d1-bb42-4a96a1c4ff88", ticket, "3b91f4b7-e398-4f07-82ad-d13bce46e21a");
			}else if(type==6){
				pushService.bonusAccount("100",ticket,"3b91f4b7-e398-4f07-82ad-d13bce46e21a","老郭");
			}else if(type==7){
				pushService.bonusConsumption("100","大白菜",ticket,"3b91f4b7-e398-4f07-82ad-d13bce46e21a","老郭");
			}else{
				pushService.pushTask(ticket);
			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("设置系统推送消息为已读状态时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	

}