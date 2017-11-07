package com.innee.czyhInterface.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;
import org.springside.modules.web.Servlets;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.service.v1.system.RedisService;
import com.innee.czyhInterface.service.v2.AppService;
import com.innee.czyhInterface.service.v2.BonusService;
import com.innee.czyhInterface.service.v2.ConfigurationService;
import com.innee.czyhInterface.service.v2.FxlService;
import com.innee.czyhInterface.util.Constant;
import com.innee.czyhInterface.util.NumberUtil;
import com.innee.czyhInterface.util.PropertiesUtil;
import com.innee.czyhInterface.util.dingTalk.DingTalkUtil;

import net.sf.ehcache.CacheManager;

/**
 * 平台内部通讯处理类
 * 
 * @author jinshengzhi
 * 
 */
@Controller
@RequestMapping(value = "/api/system", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class FxlController {

	private static Logger logger = LoggerFactory.getLogger(FxlController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private AppService appService;

	@Autowired
	private RedisService redisService;

	@Autowired
	private FxlService fxlService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private BonusService bonusService;

	@Autowired
	private CacheManager cacheManager;
	
	/**
	 * 刷新数据字典缓存的接口
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/refreshCache", method = { RequestMethod.GET, RequestMethod.POST })
	public String refreshCache(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = true) String ticket) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = new ResponseDTO();
			if (!ticket.equals("oGFYuX493SON3uHu")) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(100);
				responseDTO.setMsg("零到壹，查找优惠！");
				return mapper.toJson(responseDTO);
			}
			fxlService.initDictionary();
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			responseDTO.setMsg("数据字典缓存刷新成功！");
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("刷新数据字典缓存时出错！");
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 刷新活动类目缓存的接口
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/refreshEventCategory", method = { RequestMethod.GET, RequestMethod.POST })
	public String refreshEventCategory(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = true) String ticket) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = new ResponseDTO();
			if (!ticket.equals("oGFYuX493SON3uHu")) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(100);
				responseDTO.setMsg("零到壹，查找优惠！");
				return mapper.toJson(responseDTO);
			}
			fxlService.initEventCategory();
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			responseDTO.setMsg("活动类目缓存刷新成功！");
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("刷新活动类目缓存时出错！");
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 刷新查询到得配置文件数据
	 **/
	@ResponseBody
	@RequestMapping(value = "/refreshConfiguration", method = { RequestMethod.GET, RequestMethod.POST })
	public String refreshConfiguration(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = true) String ticket) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = new ResponseDTO();
			if (!ticket.equals("oGFYuX493SON3uHu")) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(100);
				responseDTO.setMsg("零到壹，查找优惠！");
				return mapper.toJson(responseDTO);
			}
			configurationService.initConfigurationMap();
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			responseDTO.setMsg("配置表缓存刷新时成功！");
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("配置表缓存刷新时出错！");
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 设置支付成功同步发送钉钉通知
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 * @param paySuccessSyncingDingTalk
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/setPaySuccessSyncingDingTalk", method = { RequestMethod.GET, RequestMethod.POST })
	public String setPaySuccessSyncingDingTalk(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = true) String ticket,
			@RequestParam(value = "paySuccessSyncingDingTalk", required = false) String paySuccessSyncingDingTalk) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = new ResponseDTO();
			if (!ticket.equals("oGFYuX493SON3uHu")) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(100);
				responseDTO.setMsg("零到壹，查找优惠！");
				return mapper.toJson(responseDTO);
			}
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			Map<String, Object> returnData = Maps.newHashMap();

			if (StringUtils.isNotBlank(paySuccessSyncingDingTalk)) {
				responseDTO.setMsg("更新paySuccessSyncingDingTalk信息成功");
				DingTalkUtil.setPaySuccessSyncingDingTalk(paySuccessSyncingDingTalk);
				returnData.put("当前paySuccessSyncingDingTalk的值：", paySuccessSyncingDingTalk);
			} else {
				responseDTO.setMsg("欢迎使用更新paySuccessSyncingDingTalk变量信息接口");
				returnData.put("当前paySuccessSyncingDingTalk的值：", DingTalkUtil.getPaySuccessSyncingDingTalk());
			}
			responseDTO.setData(returnData);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("设置支付同步钉钉时出错！");
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 设置用户退款同步发送钉钉通知
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 * @param refundSyncingDingTalk
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/setRefundSyncingDingTalk", method = { RequestMethod.GET, RequestMethod.POST })
	public String setRefundSyncingDingTalk(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = true) String ticket,
			@RequestParam(value = "refundSyncingDingTalk", required = false) String refundSyncingDingTalk) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = new ResponseDTO();
			if (!ticket.equals("oGFYuX493SON3uHu")) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(100);
				responseDTO.setMsg("零到壹，查找优惠！");
				return mapper.toJson(responseDTO);
			}
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			Map<String, Object> returnData = Maps.newHashMap();

			if (StringUtils.isNotBlank(refundSyncingDingTalk)) {
				responseDTO.setMsg("更新refundSyncingDingTalk信息成功");
				DingTalkUtil.setRefundSyncingDingTalk(refundSyncingDingTalk);
				returnData.put("当前refundSyncingDingTalk的值：", refundSyncingDingTalk);
			} else {
				responseDTO.setMsg("欢迎使用更新refundSyncingDingTalk变量信息接口");
				returnData.put("当前refundSyncingDingTalk的值：", DingTalkUtil.getRefundSyncingDingTalk());
			}
			responseDTO.setData(returnData);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("设置退款同步钉钉时出错！");
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 设置用户咨询同步发送钉钉通知
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 * @param consultSyncingDingTalk
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/setConsultSyncingDingTalk", method = { RequestMethod.GET, RequestMethod.POST })
	public String setConsultSyncingDingTalk(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = true) String ticket,
			@RequestParam(value = "consultSyncingDingTalk", required = false) String consultSyncingDingTalk) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = new ResponseDTO();
			if (!ticket.equals("oGFYuX493SON3uHu")) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(100);
				responseDTO.setMsg("零到壹，查找优惠！");
				return mapper.toJson(responseDTO);
			}
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			Map<String, Object> returnData = Maps.newHashMap();

			if (StringUtils.isNotBlank(consultSyncingDingTalk)) {
				responseDTO.setMsg("更新consultSyncingDingTalk信息成功");
				DingTalkUtil.setConsultSyncingDingTalk(consultSyncingDingTalk);
				returnData.put("当前consultSyncingDingTalk的值：", consultSyncingDingTalk);
			} else {
				responseDTO.setMsg("欢迎使用更新consultSyncingDingTalk变量信息接口");
				returnData.put("当前consultSyncingDingTalk的值：", DingTalkUtil.getConsultSyncingDingTalk());
			}
			responseDTO.setData(returnData);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("设置咨询同步钉钉时出错！");
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 设置用户评论同步发送钉钉通知
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 * @param commentSyncingDingTalk
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/setCommentSyncingDingTalk", method = { RequestMethod.GET, RequestMethod.POST })
	public String setCommentSyncingDingTalk(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = true) String ticket,
			@RequestParam(value = "commentSyncingDingTalk", required = false) String commentSyncingDingTalk) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = new ResponseDTO();
			if (!ticket.equals("oGFYuX493SON3uHu")) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(100);
				responseDTO.setMsg("零到壹，查找优惠！");
				return mapper.toJson(responseDTO);
			}
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			Map<String, Object> returnData = Maps.newHashMap();

			if (StringUtils.isNotBlank(commentSyncingDingTalk)) {
				responseDTO.setMsg("更新commentSyncingDingTalk信息成功");
				DingTalkUtil.setCommentSyncingDingTalk(commentSyncingDingTalk);
				returnData.put("当前commentSyncingDingTalk的值：", commentSyncingDingTalk);
			} else {
				responseDTO.setMsg("欢迎使用更新commentSyncingDingTalk变量信息接口");
				returnData.put("当前commentSyncingDingTalk的值：", DingTalkUtil.getCommentSyncingDingTalk());
			}
			responseDTO.setData(returnData);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("设置评论同步钉钉时出错！");
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 设置活动售罄同步发送钉钉通知
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 * @param consultSyncingDingTalk
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/setEventSoldOutDingTalk", method = { RequestMethod.GET, RequestMethod.POST })
	public String setEventSoldOutDingTalk(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = true) String ticket,
			@RequestParam(value = "eventSoldOutDingTalk", required = false) String eventSoldOutDingTalk) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = new ResponseDTO();
			if (!ticket.equals("oGFYuX493SON3uHu")) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(100);
				responseDTO.setMsg("零到壹，查找优惠！");
				return mapper.toJson(responseDTO);
			}
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			Map<String, Object> returnData = Maps.newHashMap();

			if (StringUtils.isNotBlank(eventSoldOutDingTalk)) {
				responseDTO.setMsg("更新eventSoldOutDingTalk信息成功");
				DingTalkUtil.setEventSoldOutDingTalk(eventSoldOutDingTalk);
				returnData.put("当前eventSoldOutDingTalk的值：", eventSoldOutDingTalk);
			} else {
				responseDTO.setMsg("欢迎使用更新eventSoldOutDingTalk变量信息接口");
				returnData.put("当前eventSoldOutDingTalk的值：", DingTalkUtil.getEventSoldOutDingTalk());
			}
			responseDTO.setData(returnData);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("设置活动售罄钉钉时出错！");
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 设置活动下架同步发送钉钉通知
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 * @param consultSyncingDingTalk
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/setEventOffSaleDingTalk", method = { RequestMethod.GET, RequestMethod.POST })
	public String setEventOffSaleDingTalk(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = true) String ticket,
			@RequestParam(value = "eventOffSaleDingTalk", required = false) String eventOffSaleDingTalk) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = new ResponseDTO();
			if (!ticket.equals("oGFYuX493SON3uHu")) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(100);
				responseDTO.setMsg("零到壹，查找优惠！");
				return mapper.toJson(responseDTO);
			}
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			Map<String, Object> returnData = Maps.newHashMap();

			if (StringUtils.isNotBlank(eventOffSaleDingTalk)) {
				responseDTO.setMsg("更新eventOffSaleDingTalk信息成功");
				DingTalkUtil.setEventOffSaleDingTalk(eventOffSaleDingTalk);
				returnData.put("当前eventOffSaleDingTalk的值：", eventOffSaleDingTalk);
			} else {
				responseDTO.setMsg("欢迎使用更新eventOffSaleDingTalk变量信息接口");
				returnData.put("当前eventOffSaleDingTalk的值：", DingTalkUtil.getConsultSyncingDingTalk());
			}
			responseDTO.setData(returnData);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("设置活动下架钉钉时出错！");
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 同步活动推荐数缓存到数据库的接口
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/savingEventRecommendCache", method = { RequestMethod.GET, RequestMethod.POST })
	public String savingEventRecommendCache(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = true) String ticket) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = new ResponseDTO();
			if (!ticket.equals("oGFYuX493SON3uHu")) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(100);
				responseDTO.setMsg("零到壹，查找优惠！");
				return mapper.toJson(responseDTO);
			}

			//eventService.savingEventRecommendCache();
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			responseDTO.setMsg("同步活动推荐数缓存到数据库成功！");
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("同步活动推荐数缓存到数据库出错！");
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 同步文章推荐数缓存到数据库的接口
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/savingArticleRecommendCache", method = { RequestMethod.GET, RequestMethod.POST })
	public String savingArticleRecommendCache(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = true) String ticket) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = new ResponseDTO();
			if (!ticket.equals("oGFYuX493SON3uHu")) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(100);
				responseDTO.setMsg("零到壹，查找优惠！");
				return mapper.toJson(responseDTO);
			}

			appService.savingArticleRecommendCache();
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			responseDTO.setMsg("同步文章推荐数缓存到数据库成功！");
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("同步文章推荐数缓存到数据库出错！");
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 分享活动跳转到指定前端活动的方法
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/share/event/{eventId}", method = RequestMethod.GET)
	public String shareEvent(HttpServletRequest request, Model model, @PathVariable String eventId) {
		return new StringBuilder().append("redirect:").append(Constant.getH5EventUrl()).append(eventId).toString();
	}


	/**
	 * 设置短信发送开关
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateSmsSwitch", method = { RequestMethod.GET, RequestMethod.POST })
	public String updateSmsSwitch(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = true) String ticket) {

		// 将request对象中的请求URL中的参数都放在Map中
		Map<String, Object> map = Servlets.getParametersStartingWith(request, null);

		ResponseDTO responseDTO = null;
		try {
			if (!ticket.equals("oGFYuX493SON3uHu")) {
				responseDTO = new ResponseDTO();
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(100);
				responseDTO.setMsg("零到壹，查找优惠！");
				return mapper.toJson(responseDTO);
			}
			responseDTO = fxlService.updateSmsSwitch(map);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("设置短信发送开关时出错！");
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 设置未支付超时分钟数的接口
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 * @param unPayFailureMinute
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateUnPayFailureMinute", method = { RequestMethod.GET, RequestMethod.POST })
	public String updateUnPayFailureMinute(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = true) String ticket,
			@RequestParam(required = false) Integer unPayFailureMinute) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = new ResponseDTO();
			if (!ticket.equals("oGFYuX493SON3uHu")) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(100);
				responseDTO.setMsg("零到壹，查找优惠！");
				return mapper.toJson(responseDTO);
			}
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			Map<String, Object> returnData = Maps.newHashMap();

			if (unPayFailureMinute != null) {
				responseDTO.setMsg("更新unPayFailureMinute信息成功");
				Constant.setUnPayFailureMinute(unPayFailureMinute);
				returnData.put("当前unPayFailureMinute的值：", unPayFailureMinute);
			} else {
				responseDTO.setMsg("欢迎使用更新unPayFailureMinute变量信息接口");
				returnData.put("当前unPayFailureMinute的值：", Constant.getUnPayFailureMinute());
			}
			responseDTO.setData(returnData);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("更新unPayFailureMinute信息时出错！");
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 设置日志过滤列表的接口
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/setupLogFilter", method = { RequestMethod.GET, RequestMethod.POST })
	public String setupLogFilter(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = true) String ticket,
			@RequestParam(value = "onOff", required = false) Integer onOff,
			@RequestParam(value = "filter", required = false) String filter) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			if (!ticket.equals("oGFYuX493SON3uHu")) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(100);
				responseDTO.setMsg("零到壹，查找优惠！");
				return mapper.toJson(responseDTO);
			}
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			Map<String, Object> returnData = Maps.newHashMap();

			if (onOff != null) {
				if (onOff.intValue() == 1) {
					responseDTO.setMsg("增加日志过滤列表值成功");
					if (!Constant.cLogFilterList.contains(filter)) {
						Constant.cLogFilterList.add(filter);
					}
				} else {
					responseDTO.setMsg("删除日志过滤列表值成功");
					Constant.cLogFilterList.remove(filter);
				}
			} else {
				responseDTO.setMsg("欢迎使用设置日志过滤列表接口");

			}
			returnData.put("当前日志过滤列表的值：", Constant.cLogFilterList);
			responseDTO.setData(returnData);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("设置日志过滤列表时出错！");
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 设置活动限购器的接口
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/setupEventQuota", method = { RequestMethod.GET, RequestMethod.POST })
	public String setupEventQuota(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = true) String ticket,
			@RequestParam(value = "onOff", required = false) Integer onOff,
			@RequestParam(value = "eventId", required = false) String eventId,
			@RequestParam(value = "quotaCount", required = false, defaultValue = "1") Integer quotaCount,
			@RequestParam(value = "quotaType", required = false, defaultValue = "0") Integer quotaType) {

		ResponseDTO responseDTO = null;
		try {
			if (!ticket.equals("oGFYuX493SON3uHu")) {
				responseDTO = new ResponseDTO();
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(100);
				responseDTO.setMsg("零到壹，查找优惠！");
				return mapper.toJson(responseDTO);
			}
			responseDTO = fxlService.setupEventQuota(onOff, eventId, quotaCount, quotaType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("更新零到壹设置限购规则器时出错！");
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 清除客户TicketToId缓存的接口
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/clearTicketToIdCache", method = { RequestMethod.GET, RequestMethod.POST })
	public String clearTicketCache(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = true) String ticket) {

		ResponseDTO responseDTO = null;
		try {
			if (!ticket.equals("oGFYuX493SON3uHu")) {
				responseDTO = new ResponseDTO();
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(100);
				responseDTO.setMsg("零到壹，查找优惠！");
				return mapper.toJson(responseDTO);
			}
			
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			responseDTO.setMsg("清除客户TicketToId缓存成功！");
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("清除客户TicketToId缓存时出错！");
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 清除客户TicketToId缓存的接口
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getOrderNum", method = { RequestMethod.GET, RequestMethod.POST })
	public String getOrderNum(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = true) String ticket,
			@RequestParam(value = "cityId", required = true) Integer cityId) {

		ResponseDTO responseDTO = null;
		try {
			if (!ticket.equals("oGFYuX493SON3uHu")) {
				responseDTO = new ResponseDTO();
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(100);
				responseDTO.setMsg("零到壹，查找优惠！");
				return mapper.toJson(responseDTO);
			}
			String orderNum = NumberUtil.getOrderNum(cityId);
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			responseDTO.setMsg(orderNum);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("清除客户TicketToId缓存时出错！");
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 系统报错后默认响应接口
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/defaultExceptionNotify", method = { RequestMethod.GET, RequestMethod.POST })
	public String updateUnPayFailureMinute(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback) {

		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setSuccess(false);
		responseDTO.setStatusCode(200);
		responseDTO.setMsg("接口出现异常，请检查调用路径！");

		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 系统报错后默认响应接口
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @return
	 */
	// @ResponseBody
	// @RequestMapping(value = "/sendSms", method = { RequestMethod.GET,
	// RequestMethod.POST })
	// public String sendSms(HttpServletRequest request, HttpServletResponse
	// response) {
	//
	// ResponseDTO responseDTO = new ResponseDTO();
	// responseDTO.setSuccess(false);
	// responseDTO.setStatusCode(0);
	//
	// int i = czyhInterfaceService.sendSms();
	// responseDTO.setMsg("成功发送了" + i + "条短信！");
	// return mapper.toJson(responseDTO);
	// }

	@ResponseBody
	@RequestMapping(value = "/ok2", method = { RequestMethod.GET, RequestMethod.POST })
	public String ok2(HttpServletRequest request, HttpServletResponse response) {

		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setSuccess(false);
		responseDTO.setStatusCode(0);

		fxlService.ok();
		responseDTO.setMsg("OK");
		return mapper.toJson(responseDTO);
	}

	/**
	 * 订单分享红包跳转到指定前端活动的方法
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/share/couponByOrder/{type}/{orderId}/{deliveryId}", method = RequestMethod.GET)
	public String sharecouponByOrder(HttpServletRequest request, Model model, @PathVariable Integer type,
			@PathVariable String orderId, @PathVariable String deliveryId) {
		StringBuilder shareUrl = new StringBuilder();
		String url = PropertiesUtil.getProperty("shareCouponUrl");
		shareUrl.append(url).append("?type=").append(type).append("&fromOrderId=").append(orderId)
				.append("&deliveryId=").append(deliveryId);
		return new StringBuilder().append("redirect:").append(shareUrl).toString();
	}

	/**
	 * 订单分享红包跳转到指定前端活动的方法
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/callInterface", method = RequestMethod.GET)
	public String callInterface(HttpServletRequest request, Model model) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = fxlService.callInterface();
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("查询数据库出错");
		}
		return mapper.toJson(responseDTO);

	}

	@ResponseBody
	@RequestMapping(value = "/runData", method = { RequestMethod.GET, RequestMethod.POST })
	public String setupEventQuota(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = true) String ticket) {
		ResponseDTO responseDTO = null;
		try {
			if (!ticket.equals("oGFYuX493SON3uHu")) {
				responseDTO = new ResponseDTO();
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(100);
				responseDTO.setMsg("零到壹，查找优惠！");
				return mapper.toJson(responseDTO);
			}
			responseDTO = bonusService.runData();
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("runData出错");
		}
		return mapper.toJson(responseDTO);

	}

	@RequestMapping(value = "/share/shareBargain/{eventBargainingId}/{customerBargainingId}", method = RequestMethod.GET)
	public String shareBargain(HttpServletRequest request, Model model, @PathVariable String customerBargainingId,
			@PathVariable String eventBargainingId) {
		StringBuilder shareUrl = new StringBuilder();
		shareUrl.append(Constant.bargaininglUrl).append("goodsBargainingId=").append(eventBargainingId)
				.append("&customerBargainingId=").append(customerBargainingId);
		return new StringBuilder().append("redirect:").append(shareUrl).toString();
	}

	// @ResponseBody
	// @RequestMapping(value = "/sendWxMsg", method = { RequestMethod.GET,
	// RequestMethod.POST })
	// public String sendWxMsg(HttpServletRequest request, HttpServletResponse
	// response,
	// @RequestParam(value = "id", required = true) String id,
	// @RequestParam(value = "remainCount", required = true) Integer
	// remainCount) {
	// ResponseDTO responseDTO = null;
	// try {
	// responseDTO = eventBargainingService.sendWxMsg(remainCount, id);
	// } catch (Exception e) {
	// logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
	// responseDTO = new ResponseDTO();
	// responseDTO.setSuccess(false);
	// responseDTO.setStatusCode(200);
	// responseDTO.setMsg("sendWxMsg出错");
	// }
	// return mapper.toJson(responseDTO);
	// }
	
	/**
	 * 分享活动跳转到指定前端活动的方法
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/share/merchant/{merchantId}", method = RequestMethod.GET)
	public String shareMerchant(HttpServletRequest request, Model model, @PathVariable String merchantId) {
		return new StringBuilder().append("redirect:").append(Constant.getH5MerchantUrl()).append(merchantId)
				.toString();
	}

	/**
	 * 分享活动跳转到指定前端文章的方法
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/share/artical/{articalId}", method = RequestMethod.GET)
	public String artical(HttpServletRequest request, Model model, @PathVariable String articalId) {
		return new StringBuilder().append("redirect:").append(Constant.getH5ArticleUrl()).append(articalId).toString();
	}
	
}