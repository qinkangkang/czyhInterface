package com.innee.czyhInterface.web.m;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;
import org.springside.modules.mapper.JsonMapper;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.innee.czyhInterface.service.v1.system.RedisService;
import com.innee.czyhInterface.util.Constant;
import com.innee.czyhInterface.util.IpUtil;
import com.innee.czyhInterface.util.asynchronoustasks.AsynchronousTasksManager;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.CustomerLogBean;

public class MApiInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(MApiInterceptor.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private RedisService redisService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		// System.out.println("@@@@@@@@@@@@@@@ " +
		// request.getSession().getId());
		HandlerMethod hm = (HandlerMethod) handler;
		// System.out.println("---------------------------------------------------------------------------");
		// System.out.println("接口时间：" + DateFormatUtils.format(new Date(),
		// "yyyy-MM-dd HH:mm:ss"));
		// System.out.println("接口调用URI：" + request.getRequestURI());
		// System.out.println("接口调用：" + hm.getMethod().getName());
		// System.out.println("接口调用参数值：");
		// Map<String, Object> map = WebUtils.getParametersStartingWith(request,
		// null);
		// for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
		// Map.Entry e = (Map.Entry) it.next();
		// System.out.println(e.getKey() + " == " + e.getValue());
		// }
		if (Constant.cLogFilterList.contains(hm.getMethod().getName())) {
			Map<String, Object> map = WebUtils.getParametersStartingWith(request, null);

			CustomerLogBean customerLogBean = new CustomerLogBean();

			if (map.containsKey("ticket") && StringUtils.isNotBlank(map.get("ticket").toString())) {
				String ticketElement = null;
				try {
					ticketElement = redisService.getValue(map.get("ticket").toString(), "TicketToId");
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (ticketElement != null) {
					customerLogBean.setCustomerId(ticketElement.toString());
				}
			}
			customerLogBean.setUrl(request.getRequestURI());
			String param = mapper.toJson(map);
			if (param.length() > 1024) {
				param = StringUtils.substring(param, 0, 1024);
			}
			customerLogBean.setParam(param);
			customerLogBean.setCustomerType(1);
			customerLogBean.setCreateTime(new Date());
			if (map.containsKey("clientType") && StringUtils.isNotBlank(map.get("clientType").toString())) {
				customerLogBean.setClientType(Integer.valueOf(map.get("clientType").toString()));
			} else {
				customerLogBean.setClientType(1);
			}
			customerLogBean.setClientIp(IpUtil.getIpAddr(request));
			customerLogBean.setTaskType(1);
			AsynchronousTasksManager.put(customerLogBean);
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}