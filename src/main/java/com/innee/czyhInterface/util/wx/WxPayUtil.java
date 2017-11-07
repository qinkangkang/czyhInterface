package com.innee.czyhInterface.util.wx;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springside.modules.utils.Exceptions;

import com.github.cuter44.wxpay.WxAppPayFactory;
import com.github.cuter44.wxpay.WxpayFactory;
import com.github.cuter44.wxpay.constants.TradeType;
import com.github.cuter44.wxpay.reqs.GetBrandWCPayRequest;
import com.github.cuter44.wxpay.reqs.JSAPIUnifiedOrder;
import com.github.cuter44.wxpay.reqs.UnifiedOrder;
import com.github.cuter44.wxpay.resps.UnifiedOrderResponse;
import com.innee.czyhInterface.exception.ServiceException;

public class WxPayUtil {

	private static final Logger logger = LoggerFactory.getLogger(WxPayUtil.class);

	/**
	 * 调用微信支付的工具类方法
	 * 
	 * @param orderNum
	 *            订单编号
	 * @param orderDesc
	 *            订单描述，一般是活动的标题
	 * @param total
	 *            支付总金额
	 * @param openid
	 *            支付人的微信openid
	 * @param timeStart
	 *            预支付有效期起
	 * @param timeExpire
	 *            预支付有效期止
	 * @param ip
	 *            支付端的IP地址
	 * @param nonceStr
	 *            随机码
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public static WxPayResult wxPay(String orderNum, String orderDesc, int total, String openid, Date timeStart,
			Date timeExpire, String ip, String nonceStr) throws UnsupportedEncodingException, IOException {
		WxPayResult wxPayResult = new WxPayResult();
		WxpayFactory factory = WxpayFactory.getDefaultInstance();
		UnifiedOrderResponse unifiedOrderResponse = null;

		// 如果订单标题超过120个字符，则进行截取。微信要求是body字段小于等于128
		if (StringUtils.isBlank(orderDesc)) {
			orderDesc = new StringBuilder().append("零到壹(北京)-").toString();
		}else if (orderDesc.length() >= 120) {
			orderDesc = new StringBuilder().append("零到壹(北京)-").append(StringUtils.substring(orderDesc, 0, 120)).toString();
		} else {
			orderDesc = new StringBuilder().append("零到壹(北京)-").append(orderDesc).toString();
		}

		try {
			JSAPIUnifiedOrder order = (JSAPIUnifiedOrder) factory.instantiate(JSAPIUnifiedOrder.class);

			order.setTimeExpire(timeExpire).setTimeStart(timeStart).setBody(orderDesc).setTotalFee(total)
					.setOpenid(openid).setOutTradeNo(orderNum).setSpbillCreateIp(ip).setNonceStr(nonceStr).build()
					.sign();

			unifiedOrderResponse = order.execute();

			wxPayResult.setSuccess(unifiedOrderResponse.isResultCodeSuccess());
			wxPayResult.setResponse(unifiedOrderResponse.getString());
		} catch (UnsupportedOperationException e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			throw new ServiceException("连接微信支付接口提交支付信息时出错！出错信息：" + e.getMessage());
		} catch (IllegalStateException e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			throw new ServiceException("连接微信支付接口提交支付信息时出错！出错信息：" + e.getMessage());
		}

		try {
			GetBrandWCPayRequest getBrandWCPayRequest = ((GetBrandWCPayRequest) factory
					.instantiate(GetBrandWCPayRequest.class, unifiedOrderResponse.getProperties())
					.setNonceStr(unifiedOrderResponse.getProperty(JSAPIUnifiedOrder.KEY_NONCE_STR))).build().sign();

			wxPayResult.setAppId(getBrandWCPayRequest.getProperty(GetBrandWCPayRequest.KEY_APPID));
			wxPayResult.setPrepayId(getBrandWCPayRequest.getProperty(GetBrandWCPayRequest.KEY_PREPAY_ID));
			wxPayResult.setPaySign(getBrandWCPayRequest.getProperty(GetBrandWCPayRequest.KEY_PAY_SIGN));
			wxPayResult.setPayPackage(getBrandWCPayRequest.getProperty(GetBrandWCPayRequest.KEY_PACKAGE));
			wxPayResult.setTimestamp(getBrandWCPayRequest.getProperty(GetBrandWCPayRequest.KEY_TIME_STAMP));
			wxPayResult.setSignType(getBrandWCPayRequest.getProperty(GetBrandWCPayRequest.KEY_SIGN_TYPE));
			wxPayResult.setNonceStrVal(getBrandWCPayRequest.getProperty(GetBrandWCPayRequest.KEY_NONCE_STR));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			throw new ServiceException("加密前端微信支付验证时出错！出错信息：" + e.getMessage());
		}

		return wxPayResult;
	}

	/**
	 * APP端微信支付调用方法
	 * 
	 * @param orderNum
	 * @param orderDesc
	 * @param total
	 * @param timeStart
	 * @param timeExpire
	 * @param ip
	 * @param nonceStr
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public static WxPayResult wxAppPay(String orderNum, String orderDesc, int total, Date timeStart, Date timeExpire,
			String ip, String nonceStr) throws UnsupportedEncodingException, IOException {
		WxPayResult wxPayResult = new WxPayResult();

		logger.info(new StringBuilder().append("orderNum:").append(orderNum).append("  orderDesc:").append(orderDesc)
				.append("  total:").append(total).append("  timeStart:").append(timeStart).append("  timeExpire:")
				.append(timeExpire).append("  ip:").append(ip).append("  nonceStr:").append(nonceStr).toString());

		WxAppPayFactory factory = WxAppPayFactory.getDefaultInstance();
		UnifiedOrderResponse unifiedOrderResponse = null;

		// 如果订单标题超过120个字符，则进行截取。微信要求是body字段小于等于128
		if (StringUtils.isBlank(orderDesc)) {
			orderDesc = new StringBuilder().append("零到壹(北京)").toString();
		}else if (orderDesc.length() >= 120) {
			orderDesc = new StringBuilder().append("零到壹(北京)").append(StringUtils.substring(orderDesc, 0, 120)).toString();
		} else {
			orderDesc = new StringBuilder().append("零到壹(北京)").append(orderDesc).toString();
		}
		try {
			UnifiedOrder order = factory.newUnifiedOrder(factory.getConf());

			order.setTradeType(TradeType.APP).setTimeExpire(timeExpire).setTimeStart(timeStart).setBody(orderDesc)
					.setTotalFee(total).setOutTradeNo(orderNum).setSpbillCreateIp(ip).setNonceStr(nonceStr).build()
					.sign();
			unifiedOrderResponse = order.execute();

			wxPayResult.setSuccess(unifiedOrderResponse.isResultCodeSuccess());
			wxPayResult.setResponse(unifiedOrderResponse.getReturnMsg().getReturnMsg());
			wxPayResult.setAppId(unifiedOrderResponse.getProperty(UnifiedOrder.KEY_APPID));
			wxPayResult.setPartnerId(unifiedOrderResponse.getProperty("mch_id"));
			wxPayResult.setTimestamp(unifiedOrderResponse.getProperty(GetBrandWCPayRequest.KEY_TIME_STAMP));
			wxPayResult.setPrepayId(unifiedOrderResponse.getProperty(GetBrandWCPayRequest.KEY_PREPAY_ID));
			wxPayResult.setNonceStrVal(unifiedOrderResponse.getProperty(GetBrandWCPayRequest.KEY_NONCE_STR));
		} catch (UnsupportedOperationException e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			throw new ServiceException("连接微信支付接口提交支付信息时出错！出错信息：" + e.getMessage());
		} catch (IllegalStateException e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			throw new ServiceException("连接微信支付接口提交支付信息时出错！出错信息：" + e.getMessage());
		}
		return wxPayResult;
	}

}