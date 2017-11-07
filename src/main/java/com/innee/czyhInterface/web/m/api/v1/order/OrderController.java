package com.innee.czyhInterface.web.m.api.v1.order;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.cuter44.wxpay.resps.Notify;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.entity.TOrder;
import com.innee.czyhInterface.service.v1.order.OrderGoodsService;
import com.innee.czyhInterface.service.v1.order.ShoppingCarService;
import com.innee.czyhInterface.util.IpUtil;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Webhooks;

@RestController("OrderControllerV1")
@RequestMapping(value = "/m/api/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OrderController {

	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private OrderGoodsService orderGoodsService;

	@Autowired
	private ShoppingCarService shoppingCarService;

	/**
	 * 订单列表
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param clientType
	 * @param ticket
	 * @param status
	 * @param pageSize
	 * @param offset
	 * @return
	 */
	@PostMapping(value = "/getOrderList")
	public String getOrderList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "source", required = false) Integer source,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = orderGoodsService.getOrderList(clientType, ticket, status,source, pageSize, offset);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取用户订单列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 查询订单详情
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param clientType
	 * @param ticket
	 * @param orderId
	 * @return
	 */
	@PostMapping(value = "/viewOrder")
	public String viewOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "orderId", required = false) String orderId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = orderGoodsService.viewOrder(clientType, ticket, orderId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取订单列表信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/wxPayConfirm")
	public String wxPayConfirm(HttpServletRequest request, HttpServletResponse response) {

		String confirm = null;
		try {
			InputStream reqBody = request.getInputStream();
			Notify notify = new Notify(reqBody);
			confirm = orderGoodsService.wxPayConfirm(notify);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			return "<xml><return_code>FAIL</return_code><return_msg>NO_HANDLER_REPORTED</return_msg></xml>";
		}
		return confirm;
	}

	/*
	 * 取消订单
	 */
	@PostMapping(value = "/cancelOrder")
	public String cancelPayOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "orderId", required = false) String orderId,
			@RequestParam(value = "refundReason", required = false) String refundReason) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = orderGoodsService.cancelPayOrder(clientType, ticket, orderId, refundReason);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("取消订单时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	/*
	 * 订单退款
	 */
	@PostMapping(value = "/refundOrder")
	public String refundOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "orderId", required = false) String orderId,
			@RequestParam(value = "refundTotal", required = false) String refundTotal,
			@RequestParam(value = "goodsStatus", required = false) String goodsStatus,
			@RequestParam(value = "refundDesc", required = false) String refundDesc,
			@RequestParam(value = "refundType", required = false) Integer refundType,
			@RequestParam(value = "refundReason", required = false) Integer refundReason) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = orderGoodsService.refundOrder(clientType, ticket, orderId, refundTotal,goodsStatus
					,refundDesc,refundReason,refundType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("订单退款时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	/*
	 * 获取订单退款信息
	 */
	@PostMapping(value = "/cancelOrderInfo")
	public String cancelOrderInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "orderId", required = false) String orderId,
			@RequestParam(value = "type", required = false) Integer type) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = orderGoodsService.cancelOrderInfo(clientType, ticket, orderId, type);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取取消订单信息出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	/*
	 * 获取订单退款详情
	 */
	@PostMapping(value = "/refundOrderDetail")
	public String refundOrderDetail(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "orderId", required = false) String orderId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = orderGoodsService.refundOrderDetail(clientType, ticket, orderId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取订单退款详情信息出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	@PostMapping(value = "/saveRefundExpress")
	public String saveRefundExpress(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "orderId", required = false) String orderId,
			@RequestParam(value = "expressNum", required = false) String expressNum,
			@RequestParam(value = "companyId", required = false) Integer companyId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = orderGoodsService.saveRefundExpress(clientType, ticket, orderId,expressNum,companyId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("保存退款物流出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 再次生成支付订单信息
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param clientType
	 * @param ticket
	 * @param orderId
	 * @return
	 */
	@PostMapping(value = "/againToFillOrder")
	public String againToFillOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "orderId", required = false) String orderId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = orderGoodsService.againToFillOrder(clientType, ticket, orderId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取订单信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 获取订单物流信息
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param clientType
	 * @param ticket
	 * @param orderId
	 * @return
	 */
	@PostMapping(value = "/orderTracking")
	public String orderTracking(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "orderId", required = false) String orderId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = orderGoodsService.orderTracking(clientType, ticket, orderId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取订单物流信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 获取用户订单带出行，待支付，待评价总数
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param ticket
	 *            票
	 * @return
	 */
	@PostMapping(value = "/getOrderNumByStatus")
	public String getOrderNumByStatus(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "sellModel", required = false, defaultValue = "0") Integer sellModel,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = orderGoodsService.getOrderNumByStatus(clientType, ticket, sellModel);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取订单状态总数时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/orderfindInfo")
	public String orderfindInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "goodsId", required = false) String goodsId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = orderGoodsService.orderfindInfo(clientType, ticket, goodsId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("查询h5订单信息失败！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/goSettlement")
	public String goSettlement(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = shoppingCarService.goSettlement(ticket, clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("订单结算时出错！");
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
	@PostMapping(value = "/buyNow")
	public String buyNow(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "goodsSkuId", required = false) String goodsSkuId,
			@RequestParam(value = "num", required = false) Integer num,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "sign", required = false) String sign) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = shoppingCarService.buyNow(ticket, goodsSkuId, num, clientType,sign,IpUtil.getIpAddr(request));
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

	@PostMapping(value = "/toPayOrderApp")
	public String toPayOrderApp(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "goodsSkuList", required = false) String goodsSkuList,
			@RequestParam(value = "remark", required = false) String remark,
			@RequestParam(value = "addressId", required = false) String addressId,
			@RequestParam(value = "payType", required = false) Integer payType,
			@RequestParam(value = "payClientType", required = false, defaultValue = "1") Integer payClientType,
			@RequestParam(value = "sellModel", required = false) Integer sellModel,
			@RequestParam(value = "fchannel", required = false, defaultValue = "1") Integer fchannel,
			@RequestParam(value = "gps", required = false) String gps,
			@RequestParam(value = "couponDeliveryId", required = false) String couponDeliveryId,
			@RequestParam(value = "postageCouponId", required = false) String postageCouponId,
			@RequestParam(value = "deviceId", required = false) String deviceId,
			@RequestParam(value = "sign", required = false) String sign) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = orderGoodsService.toPayOrderApp(clientType, ticket, goodsSkuList, addressId, payType, payClientType,
					IpUtil.getIpAddr(request), fchannel, gps, deviceId, sellModel, couponDeliveryId,postageCouponId,sign,IpUtil.getIpAddr(request));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取支付信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/appPay")
	public String appPay(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "orderId", required = false) String orderId,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "payType", required = false) Integer payType,
			@RequestParam(value = "payClientType", required = false, defaultValue = "1") Integer payClientType,
			@RequestParam(value = "sign", required = false) String sign) {
		Charge charge = null;

		charge = orderGoodsService.appPay(clientType, ticket, payType, payClientType, orderId, IpUtil.getIpAddr(request),sign);

		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(charge);
		} else {
			return mapper.toJsonP(callback, charge);
		}
	}

	@PostMapping(value = "/payedCallback")
	public String payedCallback(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String eventStr) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = orderGoodsService.verifyPayCallback(request, eventStr);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取支付信息时出错！");
		}
		return mapper.toJson(responseDTO);
	}

	@PostMapping(value = "/deleteOrder")
	public String deleteOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "orderId", required = false) String orderId,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = orderGoodsService.deleteOrder(ticket, clientType, orderId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("删除订单时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 获取订单提货码
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param ticket
	 *            票
	 * @return
	 */
	@PostMapping(value = "/getVerificationCode")
	public String getVerificationCode(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "orderId", required = false) String orderId,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = orderGoodsService.getVerificationCode(clientType, ticket, orderId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取订单提货码出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 用户确认收货
	 * @param request
	 * @param response
	 * @param callback
	 * @param ticket
	 * @return
	 */
	@PostMapping(value = "/confirmGoods")
	public String confirmGoods(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "orderId", required = false) String orderId,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = orderGoodsService.confirmGoods(clientType, ticket, orderId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("用户确认收货时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/againToPayOrder")
	public String againToPayOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "orderId", required = false) String orderId,
			@RequestParam(value = "payType", required = false) Integer payType,
			@RequestParam(value = "payClientType", required = false, defaultValue = "1") Integer payClientType) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = orderGoodsService.againToPayOrder(clientType, ticket, orderId, payClientType, payType,
					IpUtil.getIpAddr(request));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取支付信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/getDiscountAmount")
	public String getDiscountAmount(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "couponDeliveryId", required = false) String couponDeliveryId,
			@RequestParam(value = "postageCouponId", required = false) String postageCouponId,
			@RequestParam(value = "freight", required = false) String freight,
			@RequestParam(value = "goodsSkuList", required = false) String goodsSkuList,
			@RequestParam(value = "orderTotal", required = false) String orderTotal) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = orderGoodsService.getDiscountAmount(clientType, ticket, orderTotal, couponDeliveryId,postageCouponId,
					freight,goodsSkuList);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("计算优惠金额时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	@PostMapping(value = "/refundCallback")
	public String refundCallback(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String eventStr) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = orderGoodsService.refundCallback(request, eventStr);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("退款时出错！");
		}
		return mapper.toJson(responseDTO);
	}
	
	
	/*评价订单列表*/
	@PostMapping(value = "/commentOrder")
	public String commentOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = orderGoodsService.commentGoodsList(clientType, ticket, status,pageSize,offset);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取订单列表信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

}