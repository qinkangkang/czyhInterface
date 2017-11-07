package com.innee.czyhInterface.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.innee.czyhInterface.service.v1.system.SystemTimingTaskService;
import com.innee.czyhInterface.service.v2.AppService;
import com.innee.czyhInterface.service.v2.CustomerService;
import com.innee.czyhInterface.service.v2.EventService;

/**
 * 被Spring的Quartz MethodInvokingJobDetailFactoryBean定时执行的普通Spring Bean.
 */
public class FlxQuartzJob {

	private static Logger logger = LoggerFactory.getLogger(FlxQuartzJob.class);

	@Autowired
	private EventService eventService;

	@Autowired
	private SystemTimingTaskService systemTimingTaskService;

	@Autowired
	private AppService appService;

	@Autowired
	private CustomerService customerService;

	// @Autowired
	// private CarnivalService carnivalService;

	public void executeTimingTask() {
	    StringBuilder info = new StringBuilder();
		int i = systemTimingTaskService.updateEventStatus();
		logger.warn(info.append("执行了").append(i).append("个定时任务！").toString());
		/*i = eventService.savingEventRecommendCache();
		info.delete(0, info.length());
		logger.warn(info.append("同步了").append(i).append("个活动推荐数缓存到数据库！").toString());
		i = appService.savingArticleRecommendCache();
		info.delete(0, info.length());
		logger.warn(info.append("同步了").append(i).append("个文章推荐数缓存到数据库！").toString());
		i = carnivalService.savingCarnivalUserCache();
		info.delete(0, info.length());
		logger.warn(info.append("同步了").append(i).append("个嘉年华用户积分缓存到数据库！").toString());*/
		i = appService.deleteDistanceTempByOverdue();
		info.delete(0, info.length());
		logger.warn(info.append("清理了").append(i).append("个活动距离临时记录！").toString());
	}

	public void resetSerial() {
		NumberUtil.setSerial(0L);
		NumberUtil.setOtherSerial(0L);
		logger.warn("重置订单流水号成功成功！");
		// eventService.updateCalendarDay();
		// logger.warn("更新活动场次日历表成功！");
		// carnivalService.updateCarnival();
		// logger.warn("更新嘉年华用户的排名以及积分");
		StringBuilder info = new StringBuilder();
		int i = customerService.deleteCustomertTicketByOverdue();
		logger.warn(info.append("清理了").append(i).append("条一个月未登录的用户TICKET表记录！").toString());
	}
}
