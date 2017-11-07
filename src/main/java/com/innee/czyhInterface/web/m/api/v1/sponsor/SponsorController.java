package com.innee.czyhInterface.web.m.api.v1.sponsor;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.service.v1.sponsor.SponsorService;

@RestController("M_API_V1_SponsorController")
@RequestMapping(value = "/m/api/sponsor", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SponsorController {

	private static final Logger logger = LoggerFactory.getLogger(SponsorController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private SponsorService sponsorService;

	/**
	 * 获取商户列表的方法
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/getSponsorList")
	public String getMerchantList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "goodsTag", required = false) Integer goodsTag,
			@RequestParam(value = "orderBy", required = false, defaultValue = "0") Integer orderBy,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = sponsorService.getSponsorList(goodsTag, orderBy, pageSize, offset,
					request.getSession(true).getId());
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取商户列表列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}

	}

	/**
	 * 获取客户商品的方法
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/getGoodsBySponsor")
	public String getGoodsBySponsor(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "sponsorId", required = false) String sponsorId,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = sponsorService.getGoodsBySponsor(sponsorId, pageSize, offset);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取商户商品列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}

	}

	/**
	 * 获取商户详情
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/getSponsorDetail")
	public String getSponsorDetail(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "sponsorId", required = false) String sponsorId,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = sponsorService.getSponsorDetail(sponsorId, ticket, clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取商户详情时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}

	}
	
	/**
	 * 自提区的商户的商品列表
	 * @param request
	 * @param response
	 * @param callback
	 * @param sponsorId
	 * @param pageSize
	 * @param offset
	 * @return
	 */
	
	@ResponseBody
	@PostMapping(value = "/getGoodsListBySponsor")
	public String getGoodsListBySponsor(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
		//	@RequestParam(value = "sellModel", required = false, defaultValue = "1") Integer sellModel,
			@RequestParam(value = "orderBy", required = false, defaultValue = "0") Integer orderBy,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset,
			@RequestParam(value = "searchKey", required = false) String searchKey){
			
		ResponseDTO responseDTO = null;
		Integer sellModel=1;//售卖模式:0:自营;1:自提
		try {
			responseDTO = sponsorService.getSponsorAndGoodsList(orderBy, pageSize, offset,sellModel,searchKey,
					request.getSession(true).getId());
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取商户列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}

	}
	

}