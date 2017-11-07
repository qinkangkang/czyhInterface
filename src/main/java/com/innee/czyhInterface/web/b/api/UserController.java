package com.innee.czyhInterface.web.b.api;

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
import org.springframework.web.multipart.MultipartFile;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.service.v1.SponsorUserService;

@RestController("B_API_UserController")
@RequestMapping(value = "/b/api/sponsorUser", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private SponsorUserService sponsorUserService;

	@RequestMapping(value = "/sponsorLogin", method = { RequestMethod.GET, RequestMethod.POST })
	public String sponsorLogin(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "sponsorPhone", required = false) String sponsorPhone,
			@RequestParam(value = "checkCode", required = false) String checkCode) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = sponsorUserService.sponsorLogin(sponsorPhone, checkCode, request);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("商户登录时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getSponsorDetail", method = { RequestMethod.GET, RequestMethod.POST })
	public String getSponsorDetail(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = sponsorUserService.getSponsorDetail(ticket);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取商家详细信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getBankInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public String getBankInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = sponsorUserService.getBankInfo(ticket);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取商家银行信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/saveBankInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public String saveBankInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "bankPhone", required = false) String bankPhone,
			@RequestParam(value = "checkCode", required = false) String checkCode,
			@RequestParam(value = "sponsorId", required = false) String sponsorId,
			@RequestParam(value = "bankId", required = false) Integer bankId,
			@RequestParam(value = "bankName", required = false) String bankName,
			@RequestParam(value = "bankAccount", required = false) String bankAccount) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = sponsorUserService.saveBankInfo(ticket, bankPhone, checkCode, sponsorId, bankId, bankName,
					bankAccount);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("绑定银行卡信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getBankList", method = { RequestMethod.GET, RequestMethod.POST })
	public String getBankList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = sponsorUserService.getBankList();
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取银行列表信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getBalanceInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public String getBalanceInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = sponsorUserService.getBalanceInfo(ticket);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取商家余额信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/withdrawal", method = { RequestMethod.GET, RequestMethod.POST })
	public String withdrawal(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "amount", required = false) String amount,
			@RequestParam(value = "remark", required = false) String remark,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "clientDevice", required = false) String clientDevice,
			@RequestParam(value = "clientGps", required = false) String clientGps) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = sponsorUserService.withdrawal(ticket, amount, remark, clientType, clientDevice, clientGps);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("商家申请提现时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	@RequestMapping(value = "/myBalance", method = { RequestMethod.GET, RequestMethod.POST })
	public String myBalance(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = sponsorUserService.myBalance(ticket, clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取商家余额时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	@RequestMapping(value = "/getWithdrawalList", method = { RequestMethod.GET, RequestMethod.POST })
	public String getWithdrawalList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = sponsorUserService.getWithdrawalList(ticket, status, pageSize, offset);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取商家提现记录列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/logout", method = { RequestMethod.GET, RequestMethod.POST })
	public String logout(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = sponsorUserService.logoutM(ticket);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("用户登录时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/resetPassword", method = { RequestMethod.GET, RequestMethod.POST })
	public String resetPassword(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "checkCode", required = false) String checkCode,
			@RequestParam(value = "password", required = false) String password) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = sponsorUserService.resetPasswordByM(phone, checkCode, password);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("重置用户密码时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/uploadLogo", method = { RequestMethod.GET, RequestMethod.POST })
	public String uploadLogo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "file", required = false) String file) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		ResponseDTO responseDTO = null;
		try {
			responseDTO = sponsorUserService.uploadLogo(clientType, ticket, file, null, true);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("上传用户头像图片时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/appUploadLogo", method = { RequestMethod.GET, RequestMethod.POST })
	public String appUploadLogo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "file", required = false) MultipartFile file) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = sponsorUserService.uploadLogo(clientType, ticket, null, file, false);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("上传用户头像图片时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getUserInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public String getUserInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = sponsorUserService.getUserInfo(ticket);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取商家银行信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/sendCheckCode", method = { RequestMethod.GET, RequestMethod.POST })
	public String sendCheckCode(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "phone", required = false) String phone) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = sponsorUserService.sendCheckCode(phone);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("发送短信校验码时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	@RequestMapping(value = "/getAppVersion", method = { RequestMethod.GET, RequestMethod.POST })
	public String getAppVersion(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "type", required = false, defaultValue = "1") Integer type,
			@RequestParam(value = "clientType", required = false) Integer clientType) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = sponsorUserService.getAppVersion(clientType,type);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取应用版本信息出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

}