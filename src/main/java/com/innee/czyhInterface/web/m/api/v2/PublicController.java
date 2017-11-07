package com.innee.czyhInterface.web.m.api.v2;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
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
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.service.v2.PublicService;

@RestController("M_API_V2_PublicController")
@RequestMapping(value = "/m/api/v2/publicImage", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PublicController {

	private static final Logger logger = LoggerFactory.getLogger(PublicController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private PublicService publicService;

	@RequestMapping(value = "/getToken", method = { RequestMethod.GET, RequestMethod.POST })
	public String getToken(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = publicService.getToken();
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取上传图片Token时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * app公共图片上传
	 * 
	 * @param request
	 * @param response
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/appUpload", method = { RequestMethod.POST })
	public String appUpload(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "file", required = false) MultipartFile file) {
		ResponseDTO responseDTO = null;
		try {
			// MultipartFile 转File类型
			CommonsMultipartFile cf = (CommonsMultipartFile) file;
			DiskFileItem fi = (DiskFileItem) cf.getFileItem();
			File f = fi.getStoreLocation();
			responseDTO = publicService.appUpload(f);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("上传图片时出错！");
		}
		return mapper.toJson(responseDTO);

	}

	/**
	 * web公共图片上传file版
	 * 
	 * @param request
	 * @param response
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/webUpload", method = { RequestMethod.POST })
	public String webUpload(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "callback", required = false) String callback) {

		response.setHeader("Access-Control-Allow-Origin", "*");
		ResponseDTO responseDTO = null;
		try {
			// MultipartFile 转File类型
			CommonsMultipartFile cf = (CommonsMultipartFile) file;
			DiskFileItem fi = (DiskFileItem) cf.getFileItem();
			File f = fi.getStoreLocation();
			responseDTO = publicService.webUpload(f);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("上传图片时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}

	}

	@RequestMapping(value = "/addAppError", method = { RequestMethod.GET, RequestMethod.POST })
	public String addAppError(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "clientInfo", required = false) String clientInfo,
			@RequestParam(value = "errorMessage", required = false) String errorMessage,
			@RequestParam(value = "errorText", required = false) String errorText,
			@RequestParam(value = "system", required = false) String system,
			@RequestParam(value = "user", required = false) String user,
			@RequestParam(value = "view", required = false) String view,
			@RequestParam(value = "data", required = false) String data,
			@RequestParam(value = "callback", required = false) String callback) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = publicService.addAppError(clientType, clientInfo, errorMessage,errorText,system,user,view,data);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("上报终端错误信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

}