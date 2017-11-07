package com.innee.czyhInterface.web.m.api.v1.coupon;

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
import com.innee.czyhInterface.impl.couponImpl.CouponsService;
import com.innee.czyhInterface.service.v1.coupon.CouponService;
import com.innee.czyhInterface.util.IpUtil;

@RestController("M_API_CouponController")
@RequestMapping(value = "/m/api/coupon", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
	@PostMapping(value = "/couponExplain")
	public String couponExplain(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
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
	@PostMapping(value = "/findCouponInfo")
	public String findCouponInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "couponId", required = false) String couponId) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
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
	@PostMapping(value = "/appShareCoupon")
	public String appShareCoupon(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "couponId", required = false) String couponId) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
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
	@PostMapping(value = "/receiveCouponByOrder")
	public String receiveCouponByOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "fromOrderId", required = false) String fromOrderId,
			@RequestParam(value = "deliveryId", required = false) String deliveryId) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
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
	@PostMapping(value = "/shareCouponByOrder")
	public String shareCouponByOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "fromOrderId", required = false) String fromOrderId) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
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

	/******************************************* 零到壹 ***********************************************/

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
	@PostMapping(value = "/getChannelCouponList")
	public String getChannelCouponList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");

		ResponseDTO responseDTO = null;
		try {
			responseDTO = couponService.getChannelCouponList(ticket, clientType, pageSize, offset);
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
	@PostMapping(value = "/receiveCouponByChannel")
	public String receiveCouponByChannel(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = true) Integer clientType,
			@RequestParam(value = "couponId", required = false) String couponId,
			@RequestParam(value = "sign", required = true) String sign) {

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");

		ResponseDTO responseDTO = null;
		try {
			responseDTO = couponService.receiveCouponByChannel(ticket, clientType, couponId, sign,
					IpUtil.getIpAddr(request));
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
	 * 获取用户的优惠券
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param ticket
	 *            票
	 * @param status
	 *            优惠卷的状态
	 * @return
	 */
	@PostMapping(value = "/getUserCouponByStatus")
	public String getUserCouponByStatus(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset) {

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
		ResponseDTO responseDTO = null;
		try {
			responseDTO = couponService.getUserCouponByStatus(clientType, ticket, status, pageSize, offset);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取优惠券时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 获取活动可用的优惠卷列表
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param ticket
	 *            票
	 * @param eventId
	 *            活动ID
	 * @return
	 */
	@PostMapping(value = "/getAvailableCoupon")
	public String getAvailableCoupon(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "goodsSkuList", required = false) String goodsSkuList) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");

		ResponseDTO responseDTO = null;
		try {
			responseDTO = couponService.getAvailableCoupon(clientType, ticket, goodsSkuList);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取可用优惠券时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 获取活动可用的优惠卷数量
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param ticket
	 *            票
	 * @param eventId
	 *            活动ID
	 * @return
	 */
	@PostMapping(value = "/getAvailableNum")
	public String getAvailableNum(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "goodsSkuList", required = false) String goodsSkuList) {

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");

		ResponseDTO responseDTO = null;
		try {
			responseDTO = couponService.getAvailableNum(clientType, ticket, goodsSkuList);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取可用优惠券数量时出错！");
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
	@PostMapping(value = "/receiveCouponByPhone")
	public String receiveCouponByPhone(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "deliveryId", required = false) String deliveryId,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");

		ResponseDTO responseDTO = null;
		try {
			responseDTO = couponService.receiveCouponByPhone(deliveryId, ticket, clientType);
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

	@PostMapping(value = "/unCheckCoupon")
	public String getDiscountAmount(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");

		ResponseDTO responseDTO = null;
		try {
			responseDTO = couponService.unCheckCoupon(clientType, ticket);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("取消时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

}