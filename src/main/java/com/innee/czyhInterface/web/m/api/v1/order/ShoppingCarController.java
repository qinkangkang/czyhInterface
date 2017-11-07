package com.innee.czyhInterface.web.m.api.v1.order;

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
import com.innee.czyhInterface.service.v1.order.ShoppingCarService;

@RestController("M_API_V1_ShoppingCar")
@RequestMapping(value = "/m/api/shoppingcar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ShoppingCarController {

	private static final Logger logger = LoggerFactory.getLogger(ShoppingCarController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private ShoppingCarService shoppingCarService;

	/**
	 * 显示购物车信息
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @return
	 */
	@RequestMapping(value = "/showShoppingcart", method = { RequestMethod.GET, RequestMethod.POST })
	public String shoppingcart(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = shoppingCarService.shoppingcart(ticket,clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("加载购物车信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	
	/**
	 * 修改购物车信息
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @return
	 */
	@RequestMapping(value = "/modifyGoods", method = { RequestMethod.GET, RequestMethod.POST })
	public String modifyGoods(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "goodsSkuId", required = false) String goodsSkuId,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "num", required = false, defaultValue = "1") Integer num,
			@RequestParam(value = "type", required = false,defaultValue = "0") Integer type,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = shoppingCarService.modifyGoods(ticket,goodsSkuId,status,type,num,clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("修改购物车信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	
	/**
	 * 清空购物车信息
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @return
	 */
	@RequestMapping(value = "/emptyShoppingcart", method = { RequestMethod.GET, RequestMethod.POST })
	public String emptyShoppingcart(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = shoppingCarService.emptyShoppingcart(ticket,clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("清空购物车信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	
	/**
	 * 清空购物车信息
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @return
	 */
	@RequestMapping(value = "/deleteShoppingcart", method = { RequestMethod.GET, RequestMethod.POST })
	public String deleteShoppingcart(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "goodsSkuId", required = false) String goodsSkuId,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = shoppingCarService.deleteShoppingcart(ticket,clientType,goodsSkuId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("清空购物车信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	

	/**
	 * 显示购物车数量
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @return
	 */
	@RequestMapping(value = "/showGoodsNum", method = { RequestMethod.GET, RequestMethod.POST })
	public String showGoodsNum(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = shoppingCarService.showGoodsNum(ticket,clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("显示购物车商品数量时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	/**
	 * 选择购物车信息
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @return
	 */
	@RequestMapping(value = "/checkShoppingcart", method = { RequestMethod.GET, RequestMethod.POST })
	public String checkShoppingcart(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "goodsSkuId", required = false) String goodsSkuId,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = shoppingCarService.checkShoppingcart(ticket,clientType,goodsSkuId,type);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("选中购物车信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}


}