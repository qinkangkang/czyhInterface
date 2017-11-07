package com.innee.czyhInterface.util.pingPay.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.pingplusplus.exception.APIConnectionException;
import com.pingplusplus.exception.APIException;
import com.pingplusplus.exception.AuthenticationException;
import com.pingplusplus.exception.ChannelException;
import com.pingplusplus.exception.InvalidRequestException;
import com.pingplusplus.exception.RateLimitException;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.EventCollection;

/**
 * ping++支付回调接口
 * 
 * @author jinshenghzi
 *
 */
public class EventService {

	private static Logger logger = LoggerFactory.getLogger(EventService.class);

	/**
	 * 根据 ID 查询 Evnet
	 *
	 * 传递 Event 的 Id 查询 Event。 参考文档：https://www.pingxx.com/api#api-event-inquiry
	 * 
	 * @param id
	 */
	public void retrieve(String id) {
		try {
			Event event = Event.retrieve(id);
			System.out.println(event);

			// 解析 webhooks 可以采用如下方法
			// Object obj = Webhooks.getObject(event.toString());
			// if (obj instanceof Charge) {
			// System.out.println("webhooks 发送了 Charge");
			// } else if (obj instanceof Refund) {
			// System.out.println("webhooks 发送了 Refund");
			// } else if (obj instanceof Summary) {
			// System.out.println("webhooks 发送了 Summary");
			// }
		} catch (AuthenticationException e) {
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			e.printStackTrace();
		} catch (APIConnectionException e) {
			e.printStackTrace();
		} catch (APIException e) {
			e.printStackTrace();
		} catch (ChannelException e) {
			e.printStackTrace();
		} catch (RateLimitException e) {
			e.printStackTrace();
		}

	}

	public Map<String, String> parsePayedCallback(JsonObject eventJson) {
		if (eventJson == null) {
			throw new RuntimeException("解析订单回调数据错误");
		}
		JsonObject data = (JsonObject) eventJson.get("data");
		if (data == null) {
			throw new RuntimeException("解析订单回调数据data错误");
		}
		JsonObject object = (JsonObject) data.get("object");
		if (object == null) {
			throw new RuntimeException("解析订单回调数据object错误");
		}
		String orderNo = object.get("order_no").getAsString();
		Map<String, String> payedMap = new HashMap<>();
		payedMap.put("orderNo", orderNo);
		return payedMap;
	}
}
