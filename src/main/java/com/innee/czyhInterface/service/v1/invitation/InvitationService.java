package com.innee.czyhInterface.service.v1.invitation;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.common.dict.RequestUrl;
import com.innee.czyhInterface.dto.CustomerDTO;
import com.innee.czyhInterface.dto.m.AppShareDTO;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.impl.invitationImpl.InvitationsService;
import com.innee.czyhInterface.service.v2.FxlService;
import com.innee.czyhInterface.util.Constant;
import com.innee.czyhInterface.util.DictionaryUtil;
import com.innee.czyhInterface.util.HttpClientUtil;
import com.innee.czyhInterface.util.PropertiesUtil;

/**
 * 邀请有礼service
 * 
 * @author 金圣智
 *
 */
@Component("InvitationServiceV1")
@Transactional
public class InvitationService {

	private static final Logger logger = LoggerFactory.getLogger(InvitationService.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private FxlService fxlService;
	
	@Autowired
	private InvitationsService invitationsService;

	/**
	 * 邀请分享
	 * 
	 * @param ticket
	 * @param clientType
	 * @return
	 */
	public ResponseDTO appShareSign(String ticket, Integer clientType,HttpServletRequest request) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		AppShareDTO appShareDTO = new AppShareDTO();
		appShareDTO.setTitle(DictionaryUtil.getString(DictionaryUtil.Invitation, 1));
		appShareDTO.setImageUrl(Constant.defaultHeadImgUrl);// "http://goods.021-sdeals.cn/logo.png"
		appShareDTO.setBrief(DictionaryUtil.getCode(DictionaryUtil.Invitation, 1));

		appShareDTO.setUrl(new StringBuilder().append(request.getScheme()).append("://").append(request.getServerName())
				.append(":").append(request.getServerPort()).append(request.getContextPath())
				.append("/api/system/share/invite/").append(customerDTO.getCustomerId()).toString());

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("appShare", appShareDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getInvitationList(String ticket, Integer clientType) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}

		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		String customerId = "";
		if (customerDTO != null) {
			customerId = customerDTO.getCustomerId();
		}

		responseDTO = invitationsService.getInvitationList(customerId);
		return responseDTO;
	}
	
	@Transactional(readOnly = true)
	public ResponseDTO getInvitationTop(String ticket, Integer clientType) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}

		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		String customerId = "";
		if (customerDTO != null) {
			customerId = customerDTO.getCustomerId();
		}

		responseDTO = invitationsService.getInvitationTop(customerId);
		return responseDTO;
	}

}
