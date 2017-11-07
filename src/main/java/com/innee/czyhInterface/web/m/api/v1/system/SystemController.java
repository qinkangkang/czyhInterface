package com.innee.czyhInterface.web.m.api.v1.system;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.service.v1.system.SystemService;
import com.innee.czyhInterface.util.Constant;
import com.innee.czyhInterface.util.DictionaryUtil;
import com.innee.czyhInterface.util.IpUtil;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

@RestController("M_API_V1_SystemController")
@RequestMapping(value = "/m/api/system", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SystemController {

	private static final Logger logger = LoggerFactory.getLogger(SystemController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private SystemService systemService;

	/**
	 * 获取首页轮播图
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param cityId
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/getIndexPageInfo")
	public String getNewIndexPageInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "cityId", required = false) Integer cityId,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods","POST");
		response.setHeader("Access-Control-Allow-Headers","x-requested-with,content-type");
		ResponseDTO responseDTO = null;
		try {
			System.out.println(IpUtil.getIpAddr(request));
			responseDTO = systemService.getIndexPageInfo(cityId, clientType);
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

	@PostMapping(value = "/setLocation")
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
				responseDTO = systemService.setLocation(sessionId, gps, IpUtil.getIpAddr(request), ticket, clientType);
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

	@PostMapping(value = "/appShareGoods")
	public String appShareGoods(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "goodsId", required = false) String goodsId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = systemService.appShareGoods(goodsId, request);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取活动分享时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/cityCascaded")
	public String cityCascaded(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "cityId", required = false) Integer cityId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = systemService.cityCascaded(cityId, request);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取城市级联失败！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/systemDictionary")
	public String systemDictionary(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "dictionaryId", required = false) Long dictionaryId,
			@RequestParam(value = "dictionaryValue", required = false) Integer dictionaryValue) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = systemService.systemDictionary(dictionaryId, dictionaryValue, request);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取系统字典信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/getSysAddress")
	public String getSysAddress(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = systemService.getSysAddress(request);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取市区级联时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/getHotWordList")
	public String getHotWordList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "type", required = false, defaultValue = "1") Integer type,
			@RequestParam(value = "size", required = false) Integer size) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			Map<String, Object> returnData = Maps.newHashMap();

			List<String> list = Lists.newArrayList();
			Map<Integer, String> map = Maps.newHashMap();
			if(type==1){
				map = DictionaryUtil.getStatueMap(DictionaryUtil.HotSearchWord);
			}else if(type==2){
				map = DictionaryUtil.getStatueMap(DictionaryUtil.SponsorSearch);
			}
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

	@PostMapping(value = "/favoriteGoods")
	public String favoriteGoods(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "goodsId", required = false) String goodsId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = systemService.favoriteGoods(clientType, ticket, goodsId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("收藏商品时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/getFavoriteGoodsList")
	public String getFavoriteGoodsList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = systemService.getFavoriteGoodsList(clientType, ticket, pageSize, offset);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取用户已收藏商品列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/favoriteMerchant")
	public String favoriteMerchant(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "merchantId", required = false) String merchantId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = systemService.favoriteMerchant(clientType, ticket, merchantId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("收藏商家时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/getFavoriteMerchantList")
	public String getFavoriteMerchantList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = systemService.getFavoriteMerchantList(clientType, ticket, pageSize, offset);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取用户已收藏商家列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/favoriteArticle")
	public String favoriteArticle(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "articleId", required = false) String articleId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = systemService.favoriteArticle(clientType, ticket, articleId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("收藏文章时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/getFavoriteArticletList")
	public String getFavoriteArticletList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = systemService.getFavoriteArticletList(clientType, ticket, pageSize, offset);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取用户已收藏文章列表时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 全局搜索商品
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @param cityId
	 * @param key
	 * @param serchType
	 * @param orderBy
	 * @param pageSize
	 * @param offset
	 * @return
	 */
	@PostMapping(value = "/searchList")
	public String searchList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "cityId", required = false) Integer cityId,
			@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "serchType", required = false) Integer serchType,
			@RequestParam(value = "orderBy", required = false) Integer orderBy,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "offset", required = false) Integer offset) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = systemService.searchList(cityId, key, serchType, orderBy, pageSize, offset,
					request.getSession(true).getId());
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取搜索结果出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/sysColumns")
	public String sysColumns(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "tag", required = false) Integer tag,
			@RequestParam(value = "channelId", required = false) String channelId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = systemService.sysColumns(tag, channelId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取加载栏目banner广告图出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/deleteFavoriteGoods")
	public String deleteFavoriteGoods(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "ids", required = false) String ids) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = systemService.deleteFavoriteGoods(clientType, ticket, ids, type);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("批量取消收藏时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/tagType")
	public String tagType(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "type", required = false) Integer type) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = systemService.tagType(type, request);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取惠说分类信息时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/getAppVersion")
	public String getAppVersion(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "type", required = false, defaultValue = "1") Integer type,
			@RequestParam(value = "clientType", required = false) Integer clientType) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = systemService.getAppVersion(clientType, type);
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

	@PostMapping(value = "/getStartImage")
	public String getStartImage(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "cityId", required = false, defaultValue = "1") Integer cityId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = systemService.getAppFlash(cityId);
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

	@PostMapping(value = "/appShareMerchant")
	public String appShareMerchant(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "merchantId", required = false) String merchantId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = systemService.appShareMerchant(merchantId, request);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取商户分享时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/appShareArticle")
	public String appShareArticle(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "articleId", required = false) String articleId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = systemService.appShareArticle(articleId, request);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取文章分享时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/getTimestamp")
	public String getTimestamp(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false) Integer clientType) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = systemService.getTimestamp(clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取时间戳信息出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/getGoodsSpecValue")
	public String getGoodsSpecValue(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "goodsId", required = false) String goodsId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = systemService.getGoodsSpecValue(goodsId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取商品规格表格图片时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	@PostMapping(value = "/solrAddDocument")
	public String solrAddDocument(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "name", required = false) String name) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = systemService.solrAddDocument(id, name);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("搜索引擎增加词失败！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	@PostMapping(value = "/solrdeleteDocument")
	public String solrdeleteDocument(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "name", required = false) String name) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = systemService.solrdeleteDocument(id, name);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("搜索引擎刪除词失败！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

}