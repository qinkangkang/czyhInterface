package com.innee.czyhInterface.web.m.api.v1.goods;

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
import com.innee.czyhInterface.service.v1.goods.GoodsService;
import com.innee.czyhInterface.service.v2.CustomerService;

@RestController("M_API_V1_GoodsController")
@RequestMapping(value = "/m/api/goods", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class GoodsController {

	private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private GoodsService goodsService;

	@Autowired
	private CustomerService customerService;

	/**
	 * 获取商品类目信息
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @return
	 */
	@PostMapping(value = "/getCategoryList" )
	public String getCategoryList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = goodsService.getCategoryList();
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取商品分类时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 获取商品列表信息
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @return
	 */
	@PostMapping(value = "/getGoodsList" )
	public String searchEventList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "cityId", required = false) Integer cityId,
			@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "categoryId", required = false, defaultValue = "0") Integer categoryId,
			@RequestParam(value = "orderBy", required = false, defaultValue = "0") Integer orderBy,
			@RequestParam(value = "sellModel", required = false, defaultValue = "0") Integer sellModel,
			@RequestParam(value = "channelId", required = false) String channelId,
			@RequestParam(value = "sdealsModel", required = false, defaultValue = "0") Integer sdealsModel,
			@RequestParam(value = "freePostage", required = false) String freePostage,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = goodsService.getGoodsList(cityId, key, categoryId, orderBy, sellModel, freePostage,
					request.getSession(true).getId(), channelId, sdealsModel, ticket, clientType, pageSize, offset);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取商品列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/getGoodsSearch" )
	public String getGoodsSearch(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "searchValue", required = false) String searchValue,
			@RequestParam(value = "categoryId", required = false) Integer categoryId,
			@RequestParam(value = "orderBy", required = false, defaultValue = "0") Integer orderBy,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
			@RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = goodsService.getGoodsSearch(searchValue, categoryId, orderBy, pageSize, offset);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取商品列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 获取商品详情信息
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @return
	 */
	@PostMapping(value = "/getGoodsDetail" )
	public String getGoodsDetail(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "goodsId", required = false) String goodsId,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = goodsService.getGoodsDetail(ticket, goodsId, clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取商品详情时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/favoriteEvent" )
	public String favoriteEvent(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "eventId", required = false) String eventId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = customerService.favoriteEvent(clientType, ticket, eventId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("收藏活动时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 获取吃喝玩乐商品列表
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @return
	 */
	@PostMapping(value = "/getGoodsTagList" )
	public String getGoodsTagList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "cityId", required = false) Integer cityId,
			@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "goodstag", required = false) Integer goodstag,
			@RequestParam(value = "orderBy", required = false, defaultValue = "0") Integer orderBy,
			@RequestParam(value = "sellModel", required = false, defaultValue = "0") Integer sellModel,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "offset", required = false) Integer offset,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = goodsService.getGoodsTagList(cityId, key, goodstag, sellModel, orderBy, pageSize, offset,
					request.getSession(true).getId(), ticket, clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取商品列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 获取秒杀商品列表信息
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @return
	 */
	@PostMapping(value = "/getSpikeGoods" )
	public String getSpikeGoods(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "cityId", required = false) Integer cityId,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = goodsService.getSpikeGoods(cityId, ticket, clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取秒杀商品列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

}