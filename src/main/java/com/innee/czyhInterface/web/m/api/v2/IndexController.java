package com.innee.czyhInterface.web.m.api.v2;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.service.v2.AppService;
import com.innee.czyhInterface.util.Constant;
import com.innee.czyhInterface.util.DictionaryUtil;
import com.innee.czyhInterface.util.IpUtil;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

@RestController("M_API_V2_IndexController")
@RequestMapping(value = "/m/api/v2/index", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class IndexController {

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private AppService appService;

	@RequestMapping(value = "/getStartImage", method = { RequestMethod.GET, RequestMethod.POST })
	public String getStartImage(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "cityId", required = false, defaultValue = "1") Integer cityId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = appService.getAppFlash(cityId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取本应用启动首页图片时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getAllCity", method = { RequestMethod.GET, RequestMethod.POST })
	public String getAllCity(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);

			Map<String, Object> returnData = Maps.newHashMap();
			Map<Integer, String> map = DictionaryUtil.getStatueMap(DictionaryUtil.City);
			List<Map<String, Object>> list = Lists.newArrayList();
			for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
				Map.Entry e = (Map.Entry) it.next();
				Map<String, Object> mapa = Maps.newHashMap();
				mapa.put("cityId", e.getKey());
				mapa.put("cityName", e.getValue());

				list.add(mapa);
			}
			returnData.put("cityList", list);
			responseDTO.setData(returnData);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取城市列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getCityId", method = { RequestMethod.GET, RequestMethod.POST })
	public String getCityId(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "cityName") String cityName) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {

			Map<String, Object> returnData = Maps.newHashMap();
			boolean flag = false;
			Integer cityId = null;
			String cityNameValue = null;
			Map<Integer, String> map = DictionaryUtil.getStatueMap(DictionaryUtil.City);
			for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
				Map.Entry e = (Map.Entry) it.next();
				if (cityName.contains(e.getValue().toString())) {
					flag = true;
					cityId = (Integer) e.getKey();
					cityNameValue = e.getValue().toString();
					break;
				}
			}
			if (flag) {
				responseDTO.setSuccess(true);
				responseDTO.setStatusCode(0);
				returnData.put("cityId", cityId);
				returnData.put("cityName", cityNameValue);
				responseDTO.setData(returnData);
			} else {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(101);
				responseDTO.setMsg("您输入的城市名没有对应的城市ID！");
			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取城市ID信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getAllChannel", method = { RequestMethod.GET, RequestMethod.POST })
	public String getAllChannel(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "cityId") Integer cityId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = appService.getChannelByCityId(cityId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取栏目列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getChannelSliderList", method = { RequestMethod.GET, RequestMethod.POST })
	public String getChannelSliderList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "channelId", required = false) String channelId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = appService.getChannelSliderList(channelId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取栏目轮播图时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getTagList", method = { RequestMethod.GET, RequestMethod.POST })
	public String getTagList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "size", required = false) Integer size) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			Map<String, Object> returnData = Maps.newHashMap();

			List<String> list = Lists.newArrayList();
			Map<Integer, String> map = DictionaryUtil.getStatueMap(DictionaryUtil.EventTag);
			for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
				Map.Entry<Integer, String> e = (Map.Entry<Integer, String>) it.next();
				list.add(e.getValue().toString());
			}
			if (size != null) {
				while (size < list.size()) {
					list.remove(list.size() - 1);
				}
			}
			returnData.put("tagList", list);
			responseDTO.setData(returnData);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取活动标签列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getHotWordList", method = { RequestMethod.GET, RequestMethod.POST })
	public String getHotWordList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "size", required = false) Integer size) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			Map<String, Object> returnData = Maps.newHashMap();

			List<String> list = Lists.newArrayList();
			Map<Integer, String> map = DictionaryUtil.getStatueMap(DictionaryUtil.HotSearchWord);
			for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
				Map.Entry<Integer, String> e = (Map.Entry<Integer, String>) it.next();
				list.add(e.getValue().toString());
			}
			if (size != null) {
				while (size < list.size()) {
					list.remove(list.size() - 1);
				}
			}
			returnData.put("hotList", list);
			responseDTO.setData(returnData);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取热搜词列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 获取首页的轮播和栏目
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param cityId
	 *            城市ID
	 * @return
	 */
	@RequestMapping(value = "/getIndexPageInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public String getIndexPageInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "cityId", required = false) Integer cityId) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = appService.getIndexPageInfo(cityId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取首页信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 前端新版首页信息接口
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param cityId
	 * @return
	 */
	@RequestMapping(value = "/getNewIndexPageInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public String getNewIndexPageInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "cityId", required = false) Integer cityId,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = appService.getNewIndexPageInfo(cityId, clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取首页信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/setLocation", method = { RequestMethod.GET, RequestMethod.POST })
	public String setLocation(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "gps", required = false) String gps,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType) {

		String sessionId = request.getSession(true).getId();
		Cache sessionIdEventDistanceCache = cacheManager.getCache(Constant.SessionIdEventDistance);
		Element element = sessionIdEventDistanceCache.get(sessionId);
		ResponseDTO responseDTO = null;
		try {
			if (element == null) {
				responseDTO = appService.setLocation(sessionId, gps, IpUtil.getIpAddr(request), ticket, clientType);
			} else {
				responseDTO = new ResponseDTO();
				responseDTO.setSuccess(true);
				responseDTO.setStatusCode(0);
				responseDTO.setMsg("用户设定位置成功！");
			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			// 出错了需要清除在活动距离缓存中的缓存
			sessionIdEventDistanceCache.remove(sessionId);
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("设定用户位置出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getAppVersion", method = { RequestMethod.GET, RequestMethod.POST })
	public String getAppVersion(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false) Integer clientType) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = appService.getAppVersion(clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取应用版本信息出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getArticleList", method = { RequestMethod.GET, RequestMethod.POST })
	public String getArticleList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "cityId", required = false, defaultValue = "1") Integer cityId,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = appService.getArticleList(cityId, pageSize, offset);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取文章列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/getArticle", method = { RequestMethod.GET, RequestMethod.POST })
	public String getArticle(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "articleId", required = false) String articleId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = appService.getArticle(articleId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取文章详细信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@RequestMapping(value = "/clickArticleRecommend", method = { RequestMethod.GET, RequestMethod.POST })
	public String clickArticleRecommend(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "articleId", required = false) String articleId) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = appService.clickArticleRecommend(articleId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("推荐这篇文章时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

}