package com.innee.czyhInterface.service.v1.asynchronousTasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.innee.czyhInterface.exception.ServiceException;
import com.innee.czyhInterface.service.v1.order.OrderGoodsService;
import com.innee.czyhInterface.service.v2.AppService;
import com.innee.czyhInterface.service.v2.CustomerService;
import com.innee.czyhInterface.service.v2.PersonalizedService;
import com.innee.czyhInterface.service.v2.WxService;
import com.innee.czyhInterface.util.SpringContextHolder;
import com.innee.czyhInterface.util.asynchronoustasks.ITaskBean;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.AppStartupBean;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.BargainCountBean;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.CustomerAddEventBrowseBean;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.CustomerEventDistanceBean;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.CustomerLikesDislikesBean;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.CustomerLogBean;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.EventSoldOutBean;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.OrderSendSmsBean;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.OrderUpdateCustomerInfoBean;
import com.innee.czyhInterface.util.dingTalk.DingTalkUtil;

/**
 * 
 * @author zgzhou
 */
@Component
@Transactional
public class AsynchronousTasksService {

	private static Logger logger = LoggerFactory.getLogger(AsynchronousTasksService.class);

	/**
	 * 执行异步任务
	 */
	public void performTasks(ITaskBean taskBean) throws ServiceException {
		int taskType = taskBean.getTaskType();
		switch (taskType) {
		case 1: {
			// 异步写入用户日志
			try {
				AppService appService = SpringContextHolder.getBean(AppService.class);
				appService.insertCustomerLog((CustomerLogBean) taskBean);
				break;
			} catch (Exception e) {
				logger.error("异步写入用户日志出错", e);
				throw new ServiceException("异步写入用户日志出错", e);
			}

		}
		case 2: {
			// 异步写入订单附加统计信息
			try {
				OrderGoodsService orderService = SpringContextHolder.getBean(OrderGoodsService.class);
				orderService.updateCustomerInfo((OrderUpdateCustomerInfoBean) taskBean);
				break;
			} catch (Exception e) {
				logger.error("下单时异步写入订单统计到用户附加信息表出错", e);
				throw new ServiceException("下单时异步写入订单统计到用户附加信息表出错", e);
			}
		}
		case 3: {
			// 异步发送短信
			try {
				OrderGoodsService orderService = SpringContextHolder.getBean(OrderGoodsService.class);
				orderService.sendPayZeroSuccessSms((OrderSendSmsBean) taskBean);
				break;
			} catch (Exception e) {
				logger.error("下零元单时异步发送短信出错", e);
				throw new ServiceException("下零元单时异步发送短信出错", e);
			}
		}
		case 4: {
			// 异步发送短信
			try {
				/*OrderService orderService = SpringContextHolder.getBean(OrderService.class);
				orderService.sendSMS((OrderSendSmsBean) taskBean);*/
				break;
			} catch (Exception e) {
				logger.error("下非零元单时异步发送短信与钉钉消息出错", e);
				throw new ServiceException("下非零元单时异步发送短信与钉钉消息出错", e);
			}
		}
		case 5: {
			// 下单时异步修改用户标签
			try {
				/*OrderService orderService = SpringContextHolder.getBean(OrderService.class);
				orderService.updateCustomerTag((OrderUpdateCustomerTagBean) taskBean);*/
				break;
			} catch (Exception e) {
				logger.error("下单时异步修改用户标签出错", e);
				throw new ServiceException("下单时异步修改用户标签出错", e);
			}
		}
		case 6: {
			// 下单时异步修改用户gps
			try {
				/*OrderService orderService = SpringContextHolder.getBean(OrderService.class);
				orderService.updateOrderGpsByIp((OrderUpdateOrderGpsByIpBean) taskBean);*/
				break;
			} catch (Exception e) {
				logger.error("下单时异步修改用户gps出错", e);
				throw new ServiceException("下单时异步修改用户gps出错", e);
			}
		}

		case 7: {
			// 记录点赞用户的id
			try {
				//EventService eventService = SpringContextHolder.getBean(EventService.class);
				//eventService.addEventRecommend((CustomerAddEventRecommendBean) taskBean);
				break;
			} catch (Exception e) {
				logger.error("点赞时记录用户id", e);
				throw new ServiceException("点赞时异步添加用户id出错", e);
			}
		}
		case 8: {
			// 记录用户喜欢的活动type为 1
			try {
				PersonalizedService personalizedService = SpringContextHolder.getBean(PersonalizedService.class);
				personalizedService.addLikesDislikes((CustomerLikesDislikesBean) taskBean);
				break;
			} catch (Exception e) {
				logger.error("点赞时记录用户喜欢的活动id", e);
				throw new ServiceException("点赞时异步添加用户喜欢的活动出错", e);
			}
		}
		case 9: {
			// 添加用户浏览记录用
			try {
				PersonalizedService personalizedService = SpringContextHolder.getBean(PersonalizedService.class);
				personalizedService.addEventBrowse((CustomerAddEventBrowseBean) taskBean);
				break;
			} catch (Exception e) {
				logger.error("添加用户浏览记录", e);
				throw new ServiceException("添加用户浏览记录出错", e);
			}
		}

		case 10: {
			// 下单时若该活动规格售完则发送钉钉推送
			try {
				EventSoldOutBean eso = (EventSoldOutBean) taskBean;
				StringBuilder dingTalk = new StringBuilder();
				dingTalk.append("活动售罄提醒：活动名称【").append(eso.getEventTitle()).append("】，场次【")
						.append(eso.getSessionTitle()).append("】，规格【").append(eso.getSpecTitle())
						.append("】已经售罄，请及时处理。");
				DingTalkUtil.sendDingTalk(dingTalk.toString(), DingTalkUtil.getEventSpecNoneDingTalk());
				break;
			} catch (Exception e) {
				logger.error("下单时若该活动规格售完则发送钉钉推送出错", e);
				throw new ServiceException("下单时若该活动规格售完则发送钉钉推送出错", e);
			}
		}
		case 11: {
			// 收集用户启动位置信息
			try {
				CustomerService customerService = SpringContextHolder.getBean(CustomerService.class);
				customerService.appStartUp((AppStartupBean) taskBean);
				break;
			} catch (Exception e) {
				logger.error("添加用户位置信息", e);
				throw new ServiceException("添加用户位置信息出错", e);
			}
		}
		case 12: {
			// 异步写入订单附加统计信息
			try {
				/*OrderService orderService = SpringContextHolder.getBean(OrderService.class);
				orderService.backCustomerInfo((OrderUpdateCustomerInfoBean) taskBean);*/
				break;
			} catch (Exception e) {
				logger.error("下单时异步写入订单统计到用户附加信息表出错", e);
				throw new ServiceException("下单时异步写入订单统计到用户附加信息表出错", e);
			}
		}
		case 13: {
			// 异步写入客户与各个活动的距离信息到活动距离临时表中
			try {
				AppService appService = SpringContextHolder.getBean(AppService.class);
				appService.saveDistanceTemp((CustomerEventDistanceBean) taskBean);
				break;
			} catch (Exception e) {
				logger.error("异步写入客户与各个活动的距离信息到活动距离临时表出错", e);
				throw new ServiceException("异步写入客户与各个活动的距离信息到活动距离临时表出错", e);
			}
		}
		case 14: {
			// 异步砍一砍库存不足20时发送微信push
			try {
				WxService wxService = SpringContextHolder.getBean(WxService.class);
				wxService.pushremainCountMsg((BargainCountBean) taskBean);
				break;
			} catch (Exception e) {
				logger.error("异步砍一砍库存不足20时发送微信push出错", e);
				throw new ServiceException("异步砍一砍库存不足20时发送微信push出错", e);
			}
		}
		case 15: {
			// 异步砍一砍库存不足时发送钉钉
			try {
				BargainCountBean eso = (BargainCountBean) taskBean;
				StringBuilder dingTalk = new StringBuilder();
				dingTalk.append("砍一砍活动售罄提醒：活动名称【").append(eso.getEventTitle()).append("】").append("】已经售罄，请及时处理。");
				DingTalkUtil.sendDingTalk(dingTalk.toString(), DingTalkUtil.getEventSpecNoneDingTalk());
				break;
			} catch (Exception e) {
				logger.error("异步砍一砍库存不足时发送钉钉出错", e);
				throw new ServiceException("异步砍一砍库存不足时发送钉钉出错", e);
			}
		}
		default: {
			break;
		}
		}
	}

}