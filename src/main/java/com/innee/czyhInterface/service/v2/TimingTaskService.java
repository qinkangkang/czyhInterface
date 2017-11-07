package com.innee.czyhInterface.service.v2;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.Exceptions;

import com.innee.czyhInterface.dao.CalendarDAO;
import com.innee.czyhInterface.dao.CouponDAO;
import com.innee.czyhInterface.dao.CustomerBargainingDAO;
import com.innee.czyhInterface.dao.CustomerDAO;
import com.innee.czyhInterface.dao.DeliveryDAO;
import com.innee.czyhInterface.dao.EventBargainingDAO;
import com.innee.czyhInterface.dao.EventDAO;
import com.innee.czyhInterface.dao.EventSessionDAO;
import com.innee.czyhInterface.dao.EventSpecDAO;
import com.innee.czyhInterface.dao.OrderDAO;
import com.innee.czyhInterface.dao.TimingTaskDAO;
import com.innee.czyhInterface.dto.CalendarCountDTO;
import com.innee.czyhInterface.dto.EventRecommendDTO;
import com.innee.czyhInterface.entity.TCalendar;
import com.innee.czyhInterface.entity.TCustomer;
import com.innee.czyhInterface.entity.TCustomerBargaining;
import com.innee.czyhInterface.entity.TEvent;
import com.innee.czyhInterface.entity.TEventBargaining;
import com.innee.czyhInterface.entity.TEventSession;
import com.innee.czyhInterface.entity.TOrder;
import com.innee.czyhInterface.entity.TTimingTask;
import com.innee.czyhInterface.util.Constant;
import com.innee.czyhInterface.util.dingTalk.DingTalkUtil;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * 定时器任务管理类
 * 
 * @author jinshengzhi
 */
@Component
@Transactional
public class TimingTaskService {

	private static final Logger logger = LoggerFactory.getLogger(TimingTaskService.class);

	// private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private FxlService fxlService;

	@Autowired
	private AppService appService;

	@Autowired
	private EventDAO eventDAO;

	@Autowired
	private EventSpecDAO eventSpecDAO;

	@Autowired
	private EventSessionDAO eventSessionDAO;

	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private TimingTaskDAO timingTaskDAO;

	@Autowired
	private CalendarDAO calendarDAO;

	@Autowired
	private CouponDAO couponDAO;

	@Autowired
	private CacheManager cacheManager;


	@Autowired
	private DeliveryDAO deliveryDAO;

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private WxService wxService;

	@Autowired
	private CustomerBargainingDAO customerBargainingDAO;

	@Autowired
	private EventBargainingDAO eventBargainingDAO;

	/**
	 * 计算某个活动的场次日期数量，并且将统计日期数放到上下架计数Cache中
	 * 
	 * @param eventId
	 *            要计算场次日期数的活动ID
	 * @param onSaleOrOffSale
	 *            上下架标志，true表示上架；false表示下架
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void computeEventToCache(String eventId, CalendarCountDTO calendarCountDTO, boolean onSaleOrOffSale) {
		// 定义上下架活动的场次日期记录变量，活动上下架，场次信息增减提交到日历表中
		Date startDay, endDay = null;

		int temp = 0;
		Date now = new Date();
		int today = Integer.valueOf(DateFormatUtils.format(now, "yyyyMMdd")).intValue();
		Cache onOffSaleCache = cacheManager.getCache(Constant.OnOffSale);

		int[] sessionDateArray = ArrayUtils.EMPTY_INT_ARRAY;
		// 手动即刻下架，异步更新场次日历表减去活动的场次
		List<TEventSession> sessionList = eventSessionDAO.findByEventId(eventId);
		for (TEventSession tEventSession : sessionList) {
			if (tEventSession.getFstartDate() == null || tEventSession.getFendDate() == null) {
				// 如果场次的起止日期有一个为空，则不参与场次运算
				break;
			} else if (DateUtils.truncatedCompareTo(tEventSession.getFstartDate(), now, Calendar.DAY_OF_MONTH) < 0
					&& DateUtils.truncatedCompareTo(tEventSession.getFendDate(), now, Calendar.DAY_OF_MONTH) < 0) {
				// 如果场次的起止日期都小于当前日期，则不参与场次运算
				break;
			}
			// 获取场次起日期INT值
			if (DateUtils.truncatedCompareTo(tEventSession.getFstartDate(), now, Calendar.DAY_OF_MONTH) < 0) {
				startDay = now;
				temp = today;
			} else {
				startDay = tEventSession.getFstartDate();
				temp = Integer.valueOf(DateFormatUtils.format(startDay, "yyyyMMdd"));
			}
			if (temp < calendarCountDTO.getMin()) {
				calendarCountDTO.setMin(temp);
			}
			// 获取场次止日期INT值
			if (DateUtils.truncatedCompareTo(tEventSession.getFendDate(), now, Calendar.DAY_OF_MONTH) < 0) {
				endDay = now;
				temp = today;
			} else {
				endDay = tEventSession.getFendDate();
				temp = Integer.valueOf(DateFormatUtils.format(endDay, "yyyyMMdd"));
			}
			if (temp > calendarCountDTO.getMax()) {
				calendarCountDTO.setMax(temp);
			}

			// 如果起日期小于等于止日期则继续循环
			while (DateUtils.truncatedCompareTo(startDay, endDay, Calendar.DAY_OF_MONTH) <= 0) {
				temp = Integer.valueOf(DateFormatUtils.format(startDay, "yyyyMMdd"));
				if (!ArrayUtils.contains(sessionDateArray, temp)) {
					sessionDateArray = ArrayUtils.add(sessionDateArray, temp);
				}
				startDay = DateUtils.addDays(startDay, 1);
			}
		}

		int sessionDate = 0;
		for (int i = 0; i < sessionDateArray.length; i++) {
			temp = sessionDateArray[i];
			Element element = onOffSaleCache.get(temp);
			if (element == null) {
				if (onSaleOrOffSale) {
					element = new Element(temp, 1);
				} else {
					element = new Element(temp, -1);
				}
				onOffSaleCache.put(element);
			} else {
				sessionDate = (int) element.getObjectValue();
				// onOffSaleCache.removeElement(element);
				if (onSaleOrOffSale) {
					onOffSaleCache.put(new Element(temp, sessionDate + 1));
				} else {
					onOffSaleCache.put(new Element(temp, sessionDate - 1));
				}
			}
		}
	}

	public int updateEventStatus() {
		// 1、活动自动上架任务 x
		// 2、活动自动下架任务  (去掉钉钉任务推送)
		// 3、场次报名截止任务 x
		// 4、场次退款截止任务 x
		// 5、场次开始任务         x
		// 6、场次结束任务         x
		// 7、未支付订单过期作废
		// 8、活动手动上架任务，这时活动已经是上架状态，但是活动场次信息在场次日历表中的数据还没有增加，需要添加本任务来异步处理 x
		// 9、活动手动下架任务，这时活动已经是下架状态，但是活动场次信息在场次日历表中的数据还没有减去，需要添加本任务来异步处理 x
		// 10、优惠券领取有效期开始任务，将该优惠券的状态变更为可领取状态
		// 11、优惠券领取有效期截止任务，将该优惠券的状态变更为领取过期状态
		// 12、APP版本发布定时任务，到期后将去执行将新的APP版本信息更新到APP版本MAP中 
		// 13、文章下架定时任务，到期后将执行清除文章推荐数缓存
		// 14、优惠券使用有效期开始任务，到期后更改用户优惠券未到有效期状态变更为可使用状态
		// 15、优惠券使用有效期截止任务，到期后将该优惠券的状态变更为使用过期状态，同时更改用户优惠券未使用状态变更为使用过期状态
		// 16、推送信息定时任务，触发友盟推送任务
		// 17、 发送订单评价提醒模板消息      (可变更为短信消息/推送消息)
		// 18、 发送订单未支付通知模板消息  (可变更为短信消息/推送消息)
		// 20、自动核销订单 x
		List<TTimingTask> list = timingTaskDAO.findByTaskTimeLessThan(new Date().getTime());
		int taskType = 0;

		// 定义场次起止日期的最小值和最大值，为了去日历表中获取日历记录为条件
		CalendarCountDTO calendarCountDTO = new CalendarCountDTO();

		for (TTimingTask timingTask : list) {
			taskType = timingTask.getTaskType().intValue();
			switch (taskType) {
			case 1: {
				eventDAO.saveStatus(20, timingTask.getEntityId());
				computeEventToCache(timingTask.getEntityId(), calendarCountDTO, true);
				this.eventAutoOnsale(timingTask.getEntityId());
				break;
			}
			case 2: {
				eventDAO.saveStatus(90, timingTask.getEntityId());
				computeEventToCache(timingTask.getEntityId(), calendarCountDTO, false);
				this.removeEventRecommendCache(timingTask.getEntityId());
				// 同步发送钉钉通知
				try {
					if (StringUtils.isNotBlank(DingTalkUtil.getEventOffSaleDingTalk())) {
						TEvent tEvent = eventDAO.getOne(timingTask.getEntityId());
						String msg = new StringBuilder().append("活动下架提醒：活动Id[").append(tEvent.getId()).append("]")
								.append("活动名称[").append(tEvent.getFtitle()).append("]").append("在")
								.append(DateFormatUtils.format(new Date(), "yyyy年MM月dd日HH时mm分")).append("自动下架。")
								.toString();
						DingTalkUtil.sendDingTalk(msg, DingTalkUtil.getEventOffSaleDingTalk());
					}
				} catch (Exception e) {
					logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
					logger.error("活动下架时调用钉钉通知时，钉钉通知接口出错。");
				}
				break;
			}
			case 3: {
				eventSessionDAO.saveStatus(30, timingTask.getEntityId());
				break;
			}
			// case 4: {
			// break;
			// }
			// case 5: {
			// eventSessionDAO.saveStatus(40, timingTask.getEntityId());
			// break;
			// }
			// case 6: {
			// eventSessionDAO.saveStatus(50, timingTask.getEntityId());
			// break;
			// }
			case 7: {
				fxlService.orderStatusChange(3, "系统自动操作", timingTask.getEntityId(), null, 10, 109);
				int count = orderDAO.updateOrderStatus(109, timingTask.getEntityId());
				if (count > 0) {
					TOrder tOrder = orderDAO.getOne(timingTask.getEntityId());
					eventSpecDAO.addStock(tOrder.getFcount(), tOrder.getTEventSpec().getId());

					//eventDAO.addStock(tOrder.getFcount(), tOrder.getTEvent().getId());
					fxlService.subCustomerBuy(tOrder.getTEvent().getId(), tOrder.getTCustomer().getId(),
							tOrder.getFcount());
					// 如果是砍一砍活动，则修改用户砍一砍状态
					if (tOrder.getFsource() != null && tOrder.getFsource().intValue() == 20) {
						customerBargainingDAO.updateStatus(tOrder.getId(), 999);
						TCustomerBargaining bargaining = customerBargainingDAO.getByOrderId(tOrder.getId());
						TEventBargaining tEventBargaining = eventBargainingDAO.findOne(bargaining.getFbargainingId());
						if (bargaining.getFdefaultLevel().intValue() == 1) {
							tEventBargaining.setFremainingStock1(tEventBargaining.getFremainingStock1() + 1);
						} else if (bargaining.getFdefaultLevel().intValue() == 2) {
							tEventBargaining.setFremainingStock2(tEventBargaining.getFremainingStock2() + 1);
						} else if (bargaining.getFdefaultLevel().intValue() == 3) {
							tEventBargaining.setFremainingStock3(tEventBargaining.getFremainingStock3() + 1);
						}
						eventBargainingDAO.save(tEventBargaining);
					}
				}
				break;
			}
			case 8: {
				computeEventToCache(timingTask.getEntityId(), calendarCountDTO, true);
				break;
			}
			case 9: {
				computeEventToCache(timingTask.getEntityId(), calendarCountDTO, false);
				this.removeEventRecommendCache(timingTask.getEntityId());
				break;
			}
			case 10: {
				couponDeliveryStart(timingTask.getEntityId());
				break;
			}
			case 11: {
				couponDeliveryEnd(timingTask.getEntityId());
				break;
			}
			case 12: {
				appService.resetAppVersionMap(timingTask.getEntityId());
				break;
			}
			case 13: {
				appService.removeArticleRecommendCache(timingTask.getEntityId());
				break;
			}
			case 14: {
				couponUseStart(timingTask.getEntityId());
				break;
			}
			case 15: {
				couponUseEnd(timingTask.getEntityId());
				break;
			}
			case 16: {
				pushMessage(timingTask.getEntityId());
				break;
			}
			case 17: {
				TOrder tOrder = orderDAO.getOne(timingTask.getEntityId());
				TCustomer tCustomer = customerDAO.findOne(tOrder.getTCustomer().getId());
				// if (tCustomer.getFweixinId() != null) {
				// if(tOrder.getTEvent().getFcommentRewardType() == null ||
				// tOrder.getTEvent().getFcommentRewardType().intValue()==10){
				// String orderPushWeiXin = ConfigurationUtil
				// .getPropertiesValue(ResponseConfigurationDict.RESPONSE_PROPERTIES_ORDEREVALUATION);
				// if (StringUtils.isNotBlank(orderPushWeiXin) &&
				// orderPushWeiXin.equals("1")
				// && StringUtils.isNotBlank(tCustomer.getFweixinId())) {
				// wxService.pushOrderEvaluationMsg(tOrder,
				// tCustomer.getFweixinId());
				// }
				// }else
				// if(tOrder.getTEvent().getFcommentRewardType().intValue()==20){
				// String orderPushWeiXin = ConfigurationUtil
				// .getPropertiesValue(ResponseConfigurationDict.RESPONSE_PROPERTIES_ORDEREVALUATIONBONUS);
				// if (StringUtils.isNotBlank(orderPushWeiXin) &&
				// orderPushWeiXin.equals("1")
				// && StringUtils.isNotBlank(tCustomer.getFweixinId())) {
				// wxService.pushOrderEvaluationBonusMsg(tOrder,
				// tCustomer.getFweixinId());
				// }
				// }else
				// if(tOrder.getTEvent().getFcommentRewardType().intValue()==30){
				// String orderPushWeiXin = ConfigurationUtil
				// .getPropertiesValue(ResponseConfigurationDict.RESPONSE_PROPERTIES_ORDERCASHBACK);
				// if (StringUtils.isNotBlank(orderPushWeiXin) &&
				// orderPushWeiXin.equals("1")
				// && StringUtils.isNotBlank(tCustomer.getFweixinId())) {
				// wxService.pushOrderCashBackMsg(tOrder,
				// tCustomer.getFweixinId());
				// }
				// }
				// }
				break;
			}
			case 18: {
				TOrder tOrder = orderDAO.getOne(timingTask.getEntityId());
				TCustomer customer = customerDAO.findOne(tOrder.getTCustomer().getId());
				if (customer.getFweixinId() != null && tOrder.getFstatus() < 20) {
					wxService.pushUnpaidOrderMsg(tOrder, customer.getFweixinId());
				}
				break;
			}
			case 19: {
				TEventBargaining tEventBargaining = eventBargainingDAO.getOne(timingTask.getEntityId());
				if (tEventBargaining != null) {
					wxService.pushTimeOutMsg(tEventBargaining);
				}
				break;
			}
			case 20: {
				/*TEventSession tEventSession = eventSessionDAO.findOne(timingTask.getEntityId());
				if (tEventSession != null) {
					orderService.autoVerification(tEventSession);
				}*/
				break;
			}
			default: {
				break;
			}
			}
		}

		Cache onOffSaleCache = cacheManager.getCache(Constant.OnOffSale);
		// 上下架缓存中有数据才执行上下架场次日历更新
		if (onOffSaleCache.getSize() > 0) {
			eventOnOffSaleToCalendar(calendarCountDTO);
			// 清空上下架场次缓存
			onOffSaleCache.removeAll();
		}
		timingTaskDAO.delete(list);

		return list.size();
	}

	/**
	 * 将上下架场次数Cache中的计数保存到场次日历中去
	 * 
	 * @param min
	 *            场次日历起日期
	 * @param max
	 *            场次日历止日期
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void eventOnOffSaleToCalendar(CalendarCountDTO calendarCountDTO) {
		Cache onOffSaleCache = cacheManager.getCache(Constant.OnOffSale);

		List<TCalendar> calendarList = calendarDAO.findByCalendarStartAndEnd(1, calendarCountDTO.getMin(),
				calendarCountDTO.getMax());
		int thisDay = 0;
		for (TCalendar tCalendar : calendarList) {
			thisDay = tCalendar.getEventDate().intValue();
			Element element = onOffSaleCache.get(thisDay);
			if (element != null) {
				calendarDAO.eventOnOffSaleToCalendar(tCalendar.getId(),
						((int) element.getObjectValue()) + tCalendar.getEventNum());
			}
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void couponDeliveryStart(String delivareyId) {
		deliveryDAO.updateStatus(delivareyId, 40);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void couponDeliveryEnd(String delivareyId) {
		deliveryDAO.updateStatus(delivareyId, 100);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void couponUseStart(String couponId) {
		// couponDeliveryDAO.updateStatusByCouponId2(couponId, 80, 10);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void couponUseEnd(String couponId) {
		couponDAO.updateStatus(couponId, 110);
		// couponDeliveryDAO.updateStatusByCouponId(couponId, 20, 90);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void pushMessage(String pushId) {
//		pushService.pushTask(pushId);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void eventAutoOnsale(String eventId) {
		//eventDAO.updateStockFlagBySpec(eventId);
	}

	/**
	 * 将某个活动的推荐数缓存移除
	 * 
	 * @param eventId
	 */
	public void removeEventRecommendCache(String eventId) {
		Cache eventRecommendCache = cacheManager.getCache(Constant.EventRecommend);
		Element ele = eventRecommendCache.get(eventId);
		if (ele == null) {
			return;
		}

		EventRecommendDTO eventRecommendDTO = (EventRecommendDTO) ele.getObjectValue();
		if (eventRecommendDTO.isChange()) {
			eventDAO.updateEventRecommend(eventId, eventRecommendDTO.getRecommend());
		}
		eventRecommendCache.remove(eventId);
	}
}