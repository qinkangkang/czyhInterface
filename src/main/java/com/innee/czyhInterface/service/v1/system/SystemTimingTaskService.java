package com.innee.czyhInterface.service.v1.system;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.common.dict.RequestUrl;
import com.innee.czyhInterface.dao.CalendarDAO;
import com.innee.czyhInterface.dao.CouponDAO;
import com.innee.czyhInterface.dao.CouponDeliveryDAO;
import com.innee.czyhInterface.dao.CustomerBargainingDAO;
import com.innee.czyhInterface.dao.CustomerInfoDAO;
import com.innee.czyhInterface.dao.DeliveryDAO;
import com.innee.czyhInterface.dao.EventBargainingDAO;
import com.innee.czyhInterface.dao.EventDAO;
import com.innee.czyhInterface.dao.OrderDAO;
import com.innee.czyhInterface.dao.OrderGoodsDAO;
import com.innee.czyhInterface.dao.TimingTaskDAO;
import com.innee.czyhInterface.dto.CalendarCountDTO;
import com.innee.czyhInterface.dto.EventRecommendDTO;
import com.innee.czyhInterface.entity.TCalendar;
import com.innee.czyhInterface.entity.TCouponDelivery;
import com.innee.czyhInterface.entity.TCustomerBargaining;
import com.innee.czyhInterface.entity.TCustomerInfo;
import com.innee.czyhInterface.entity.TEventBargaining;
import com.innee.czyhInterface.entity.TOrder;
import com.innee.czyhInterface.entity.TOrderGoods;
import com.innee.czyhInterface.entity.TTimingTask;
import com.innee.czyhInterface.impl.couponImpl.CouponsService;
import com.innee.czyhInterface.service.v1.goods.GoodsService;
import com.innee.czyhInterface.service.v1.push.PushService;
import com.innee.czyhInterface.service.v2.AppService;
import com.innee.czyhInterface.service.v2.FxlService;
import com.innee.czyhInterface.util.Constant;
import com.innee.czyhInterface.util.HttpClientUtil;
import com.innee.czyhInterface.util.PropertiesUtil;

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
public class SystemTimingTaskService {

	private static final Logger logger = LoggerFactory.getLogger(SystemTimingTaskService.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private FxlService fxlService;

	@Autowired
	private AppService appService;

	@Autowired
	private EventDAO eventDAO;

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
	private PushService pushService;

	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private OrderGoodsDAO orderGoodsDAO;
	
	@Autowired
	private CustomerInfoDAO customerInfoDAO;
	
	@Autowired
	private CouponDeliveryDAO couponDeliveryDAO;	
	
	@Autowired
	private CustomerBargainingDAO customerBargainingDAO;
	
	@Autowired
	private EventBargainingDAO eventBargainingDAO;
	
	@Autowired
	private CouponsService couponsService;

	public int updateEventStatus() {
		// 1、活动自动上架任务
		// 2、活动自动下架任务
		// 7、未支付订单过期作废
		// 10、优惠券领取有效期开始任务，将该优惠券的状态变更为可领取状态
		// 11、优惠券领取有效期截止任务，将该优惠券的状态变更为领取过期状态
		// 12、APP版本发布定时任务，到期后将去执行将新的APP版本信息更新到APP版本MAP中
		// 13、文章下架定时任务，到期后将执行清除文章推荐数缓存
		// 14、优惠券使用有效期开始任务，到期后更改用户优惠券未到有效期状态变更为可使用状态
		// 15、优惠券使用有效期截止任务，到期后将该优惠券的状态变更为使用过期状态，同时更改用户优惠券未使用状态变更为使用过期状态
		// 16、推送信息定时任务，触发友盟推送任务
		// 17、 发送订单评价提醒模板消息 (可变更为短信消息/推送消息)
		// 18、 发送订单未支付通知消息 (可变更为短信消息/推送消息)
		// 19、优惠券过期提醒
		// 20、自动核销订单 x
		// 21、自动确认收货 x
		List<TTimingTask> list = timingTaskDAO.findByTaskTimeLessThan(new Date().getTime());
		int taskType = 0;

		// 定义场次起止日期的最小值和最大值，为了去日历表中获取日历记录为条件
		CalendarCountDTO calendarCountDTO = new CalendarCountDTO();

		for (TTimingTask timingTask : list) {
			taskType = timingTask.getTaskType().intValue();
			switch (taskType) {
			case 1: {
				eventDAO.saveStatus(20, timingTask.getEntityId());
				this.eventAutoOnsale(timingTask.getEntityId());
				break;
			}
			case 2: {
				eventDAO.saveStatus(90, timingTask.getEntityId());
				this.removeEventRecommendCache(timingTask.getEntityId());
				// todo 商户端应该给商户app发送推送下架通知
				break;
			}
			case 7: {
				fxlService.orderStatusChange(3, "系统自动操作", timingTask.getEntityId(), null, 10, 109);
				int count = orderDAO.updateOrderStatus(109, timingTask.getEntityId());
				if (count > 0) {
					// 将订单占用的库存数量退回
					TOrder tOrder = orderDAO.findOne(timingTask.getEntityId());
					List<TOrderGoods> orderGoodslist = orderGoodsDAO.findByOrderId(timingTask.getEntityId());
					goodsService.backStock(orderGoodslist);
					couponsService.backCoupon(tOrder.getId(), tOrder.getFstatus());
					//如果是砍一砍库存
					if(tOrder!=null && tOrder.getFsource()==20){
						TCustomerBargaining tCustomerBargaining = customerBargainingDAO.getByOrderId(timingTask.getEntityId());
						TEventBargaining tEventBargaining = eventBargainingDAO.findOne(tCustomerBargaining.getFbargainingId());
						if (tCustomerBargaining.getFdefaultLevel() == 1) {
							tEventBargaining.setFremainingStock1(tEventBargaining.getFremainingStock1() + 1);
						} else if (tCustomerBargaining.getFdefaultLevel() == 2) {
							tEventBargaining.setFremainingStock2(tEventBargaining.getFremainingStock2() + 1);
						} else if (tCustomerBargaining.getFdefaultLevel() == 3) {
							tEventBargaining.setFremainingStock3(tEventBargaining.getFremainingStock3() + 1);
						}
						tEventBargaining = eventBargainingDAO.save(tEventBargaining);
						customerBargainingDAO.updateStatus(timingTask.getEntityId(),100);
					}
				}
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
				// todo 修改为短信评价/推送评价提醒
				// TOrder tOrder = orderDAO.getOne(timingTask.getEntityId());
				// TCustomer tCustomer =
				// customerDAO.findOne(tOrder.getTCustomer().getId());
				break;
			}
			case 18: {
				TOrder tOrder = orderDAO.getOne(timingTask.getEntityId());
				TCustomerInfo customerInfo = customerInfoDAO.getByCustomerId(tOrder.getTCustomer().getId());
				if(customerInfo!=null && customerInfo.getFregisterDeviceTokens() != null){
					pushService.toPaid(tOrder.getForderNum(), tOrder.getId(), 
							customerInfo.getFregisterDeviceTokens(), tOrder.getTCustomer().getId());
				}
				break;
			}
			case 19: {
				TCouponDelivery tCouponDelivery = couponDeliveryDAO.findOne(timingTask.getEntityId());
				if(tCouponDelivery!=null){
					TCustomerInfo customerInfo = customerInfoDAO.getByCustomerId(tCouponDelivery.getTCustomer().getId());
					if(customerInfo!=null && customerInfo.getFregisterDeviceTokens() != null){
						String money = "";
						if(tCouponDelivery.getTCouponInformation().getFamount()!=null){
							money = tCouponDelivery.getTCouponInformation().getFamount().toString()+"元";
						}else if(tCouponDelivery.getTCouponInformation().getFdiscount()!=null){
							money = tCouponDelivery.getTCouponInformation().getFdiscount().toString()+"折";
						}
						pushService.couponOverdue(money, customerInfo.getFregisterDeviceTokens(),
								tCouponDelivery.getTCustomer().getId(), tCouponDelivery.getTCustomer().getFname());
					}
				}
				break;
			}
			case 21: {
				TOrder tOrder = orderDAO.getOne(timingTask.getEntityId());
				if(tOrder!=null&&tOrder.getFstatus().intValue()==30){
					fxlService.orderStatusChange(4, "系统自动操作", timingTask.getEntityId(), null, 30, 70);
					orderDAO.updateOrderStatus(70, timingTask.getEntityId());
				}
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
		pushService.pushTask(pushId);
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