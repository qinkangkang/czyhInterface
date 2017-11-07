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
import com.innee.czyhInterface.service.v2.CouponService;

@RestController("M_API_V2_CouponController")
@RequestMapping(value = "/m/api/v2/coupon", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CouponController {

	private static final Logger logger = LoggerFactory.getLogger(CouponController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private CouponService couponService;

	/**
	 * 优惠券使用说明
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/couponExplain", method = { RequestMethod.GET, RequestMethod.POST })
	public String couponExplain(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = couponService.couponExplain();
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取优惠券使用说明！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 查询优惠券信息
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/findCouponInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public String findCouponInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "couponId", required = false) String couponId) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = couponService.findCouponInfo(couponId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取优惠券信息出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 查询优惠券信息
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/appShareCoupon", method = { RequestMethod.GET, RequestMethod.POST })
	public String appShareCoupon(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "couponId", required = false) String couponId) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = couponService.appShareCoupon(couponId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取优惠券信息出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 用户领取优惠券
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 *            用户票
	 * @return
	 */
	@RequestMapping(value = "/receiveCouponByPhone", method = { RequestMethod.GET, RequestMethod.POST })
	public String receiveCouponByPhone(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "deliveryId", required = false) String deliveryId,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "type", required = false, defaultValue = "1") Integer type) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = couponService.receiveCouponByPhone(deliveryId, ticket, type);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("领取优惠券出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 用户领取优惠券
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 *            用户票
	 * @return
	 */
	@RequestMapping(value = "/receiveCouponByOrder", method = { RequestMethod.GET, RequestMethod.POST })
	public String receiveCouponByOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "fromOrderId", required = false) String fromOrderId,
			@RequestParam(value = "deliveryId", required = false) String deliveryId) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = couponService.receiveCouponByOrder(ticket, fromOrderId, deliveryId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("领取优惠券出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 获取用户订单可分享券额度及分享地址
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 *            用户票
	 * @return
	 */
	@RequestMapping(value = "/shareCouponByOrder", method = { RequestMethod.GET, RequestMethod.POST })
	public String shareCouponByOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "fromOrderId", required = false) String fromOrderId) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = couponService.shareCouponByOrder(ticket, fromOrderId, request);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("领取优惠券出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 获取优惠券频道券列表
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param ticket
	 *            票
	 * @param orderId
	 *            订单号
	 * @return
	 */
	@RequestMapping(value = "/getChannelCouponList", method = { RequestMethod.GET, RequestMethod.POST })
	public String getChannelCouponList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = couponService.getChannelCouponList(clientType, ticket, pageSize, offset);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取优惠券频道券列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 用户从优惠券频道页领取优惠券
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 *            用户票
	 * @return
	 */
	@RequestMapping(value = "/receiveCouponByChannel", method = { RequestMethod.GET, RequestMethod.POST })
	public String receiveCouponByChannel(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "couponId", required = false) String couponId) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = couponService.receiveCouponByChannel(clientType, ticket, couponId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("领取优惠券出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

}