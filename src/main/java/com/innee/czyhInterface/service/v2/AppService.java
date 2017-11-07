package com.innee.czyhInterface.service.v2;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.query.internal.QueryImpl;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.common.dict.ResponseConfigurationDict;
import com.innee.czyhInterface.dao.AppChannelSettingDAO;
import com.innee.czyhInterface.dao.AppFlashDAO;
import com.innee.czyhInterface.dao.AppVersionDAO;
import com.innee.czyhInterface.dao.ArticleDAO;
import com.innee.czyhInterface.dao.CustomerLogDAO;
import com.innee.czyhInterface.dao.EventDistanceTempDAO;
import com.innee.czyhInterface.dao.SubjectDAO;
import com.innee.czyhInterface.dto.AndroidUrlDTO;
import com.innee.czyhInterface.dto.ArticleRecommendDTO;
import com.innee.czyhInterface.dto.EventDistanceDTO;
import com.innee.czyhInterface.dto.m.AppChannelSettingDTO;
import com.innee.czyhInterface.dto.m.AppChannelSliderDTO;
import com.innee.czyhInterface.dto.m.AppVersionDTO;
import com.innee.czyhInterface.dto.m.ArticleDTO;
import com.innee.czyhInterface.dto.m.ChannelDTO;
import com.innee.czyhInterface.dto.m.ChannelSliderDTO;
import com.innee.czyhInterface.dto.m.PageDTO;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.entity.TAppChannelSetting;
import com.innee.czyhInterface.entity.TAppFlash;
import com.innee.czyhInterface.entity.TAppVersion;
import com.innee.czyhInterface.entity.TArticle;
import com.innee.czyhInterface.entity.TCustomerLog;
import com.innee.czyhInterface.entity.TEventDistanceTemp;
import com.innee.czyhInterface.entity.TSubject;
import com.innee.czyhInterface.exception.ServiceException;
import com.innee.czyhInterface.util.CommonPage;
import com.innee.czyhInterface.util.ConfigurationUtil;
import com.innee.czyhInterface.util.Constant;
import com.innee.czyhInterface.util.DictionaryUtil;
import com.innee.czyhInterface.util.PropertiesUtil;
import com.innee.czyhInterface.util.RandomColorUtil;
import com.innee.czyhInterface.util.asynchronoustasks.AsynchronousTasksManager;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.AppStartupBean;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.CustomerEventDistanceBean;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.CustomerLogBean;
import com.innee.czyhInterface.util.bmap.BmapUtil;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * czyhInterface业务管理类.
 * 
 * @author zgzhou
 */
@Component
@Transactional
public class AppService {

	private static final Logger logger = LoggerFactory.getLogger(AppService.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@PersistenceContext
	protected EntityManager em;

	@Autowired
	private CommonService commonService;

	@Autowired
	private FxlService fxlService;

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private SubjectDAO subjectDAO;

	@Autowired
	private AppChannelSettingDAO appChannelSettingDAO;

	@Autowired
	private AppFlashDAO appFlashDAO;

	@Autowired
	private CustomerLogDAO customerLogDAO;

	@Autowired
	private EventDistanceTempDAO eventDistanceTempDAO;

	@Autowired
	private AppVersionDAO appVersionDAO;

	@Autowired
	private ArticleDAO articleDAO;

	@Autowired
	private CustomerService customerService;

	public void insertCustomerLog(CustomerLogBean customerLogBean) {
		try {
			TCustomerLog tCustomerLog = new TCustomerLog();
			BeanMapper.copy(customerLogBean, tCustomerLog);
			customerLogDAO.save(tCustomerLog);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			throw new ServiceException();
		}
	}

	@Transactional(readOnly = true)
	public ResponseDTO getAppFlash(Integer cityId) {
		ResponseDTO responseDTO = new ResponseDTO();

		Map<Integer, String> cityMap = DictionaryUtil.getStatueMap(DictionaryUtil.City);
		// 找不到城市ID就默认为北京
		if (!cityMap.containsKey(cityId)) {
			cityId = 1;
		}

		Map<String, Object> returnData = Maps.newHashMap();
		TAppFlash tAppFlash = appFlashDAO.findByFcityAndFisVisible(cityId, 1);
		if (tAppFlash != null) {
			returnData.put("urlType", tAppFlash.getFurlType());
			returnData.put("entityId", tAppFlash.getFentityId());
			returnData.put("linkUrl", tAppFlash.getFexternalUrl());
			returnData.put("imageUrl", fxlService.getImageUrl(tAppFlash.getFimage(), false));
		} else {
			returnData.put("urlType", StringUtils.EMPTY);
			returnData.put("entityId", StringUtils.EMPTY);
			returnData.put("linkUrl", StringUtils.EMPTY);
			returnData.put("imageUrl", StringUtils.EMPTY);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getChannelByCityId(Integer cityId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (cityId == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("cityId参数不能为空，请检查cityId的传递参数值！");
			return responseDTO;
		}

		StringBuilder hql = new StringBuilder();
		hql.append(
				"select t.id as id, t.ftitle as ftitle, t.fsubTitle as fsubTitle, t.fpromotion as fpromotion from TAppChannelSetting t where t.fisVisible = 1 and t.fcity = :fcity and t.fcode = :fcode order by t.forder asc");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("fcity", cityId);
		hqlMap.put("fcode", "index_web_" + cityId);
		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);

		// 对活动信息进行细化加载
		List<ChannelDTO> resultList = Lists.newArrayList();
		ChannelDTO channelDTO = null;
		for (Map<String, Object> amap : list) {
			channelDTO = new ChannelDTO();
			channelDTO.setChannelId(amap.get("id").toString());

			// 加载栏目轮播图信息
			hql.delete(0, hql.length());
			hql.append(
					"select t.fimage as fimage, t.furlType as furlType, t.fentityId as fentityId, t.fentityTitle as fentityTitle, t.fexternalUrl as fexternalUrl from TAppChannelSlider t where t.TAppChannelSetting.id = :channelId and t.fisVisible = 1 order by t.forder");
			hqlMap.clear();
			hqlMap.put("channelId", amap.get("id"));
			List<Map<String, Object>> list2 = commonService.find(hql.toString(), hqlMap);
			if (CollectionUtils.isNotEmpty(list2)) {
				channelDTO.setSlider(true);
				List<ChannelSliderDTO> sliderList = Lists.newArrayList();
				ChannelSliderDTO channelSliderDTO = null;
				for (Map<String, Object> bmap : list2) {
					channelSliderDTO = new ChannelSliderDTO();
					channelSliderDTO.setUrlType((Integer) bmap.get("furlType"));
					if (bmap.get("fentityId") != null && StringUtils.isNotBlank(bmap.get("fentityId").toString())) {
						channelSliderDTO.setEntityId(bmap.get("fentityId").toString());
					}
					channelSliderDTO.setSliderImageUrl(fxlService.getImageUrl(bmap.get("fimage").toString(), false));
					if (channelSliderDTO.getUrlType() == 3) {
						// 如果轮播类型是专题链接
						TSubject tSubject = subjectDAO.getOne(bmap.get("fentityId").toString());
						channelSliderDTO.setLinkUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
								.append(PropertiesUtil.getProperty("htmlRootPath")).append(tSubject.getFdetailHtmlUrl())
								.toString());
					}
					if (channelSliderDTO.getUrlType() == 4 || channelSliderDTO.getUrlType() == 5) {
						// 如果轮播类型是优惠券链接或者外部链接
						channelSliderDTO.setLinkUrl(bmap.get("fexternalUrl").toString());
					}
					sliderList.add(channelSliderDTO);
				}
				channelDTO.setSliderList(sliderList);
			} else {
				channelDTO.setSlider(false);
			}
			if (amap.get("ftitle") != null && StringUtils.isNotBlank(amap.get("ftitle").toString())) {
				channelDTO.setTitle(amap.get("ftitle").toString());
			}
			if (amap.get("fsubTitle") != null && StringUtils.isNotBlank(amap.get("fsubTitle").toString())) {
				channelDTO.setSubTitle(amap.get("fsubTitle").toString());
			}
			if (amap.get("fpromotion") != null && StringUtils.isNotBlank(amap.get("fpromotion").toString())) {
				channelDTO.setPromotion(amap.get("fpromotion").toString());
			}
			resultList.add(channelDTO);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("channelList", resultList);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getChannelSliderList(String channelId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(channelId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("channelId参数不能为空，请检查channelId的传递参数值！");
			return responseDTO;
		}

		Map<String, Object> returnData = Maps.newHashMap();
		TAppChannelSetting tAppChannelSetting = appChannelSettingDAO.getOne(channelId);
		returnData.put("channelName", tAppChannelSetting.getFtitle());
		returnData.put("channelCode", tAppChannelSetting.getFcode());
		returnData.put("channelSubName", tAppChannelSetting.getFsubTitle());
		returnData.put("channelPromotion", tAppChannelSetting.getFpromotion());

		// 加载栏目轮播图信息
		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hql.append(
				"select t.fimage as fimage, t.furlType as furlType, t.fentityId as fentityId, t.fentityTitle as fentityTitle, t.fexternalUrl as fexternalUrl from TAppChannelSlider t where t.TAppChannelSetting.id = :channelId and t.fisVisible = 1 order by t.forder");
		hqlMap.put("channelId", channelId);
		List<Map<String, Object>> list2 = commonService.find(hql.toString(), hqlMap);
		if (CollectionUtils.isNotEmpty(list2)) {
			returnData.put("slider", true);
			List<ChannelSliderDTO> sliderList = Lists.newArrayList();
			ChannelSliderDTO channelSliderDTO = null;
			for (Map<String, Object> bmap : list2) {
				channelSliderDTO = new ChannelSliderDTO();
				channelSliderDTO.setUrlType((Integer) bmap.get("furlType"));
				if (bmap.get("fentityId") != null && StringUtils.isNotBlank(bmap.get("fentityId").toString())) {
					channelSliderDTO.setEntityId(bmap.get("fentityId").toString());
				}
				channelSliderDTO.setSliderImageUrl(fxlService.getImageUrl(bmap.get("fimage").toString(), false));
				if (channelSliderDTO.getUrlType() == 3) {
					// 如果轮播类型是专题链接
					TSubject tSubject = subjectDAO.getOne(bmap.get("fentityId").toString());
					channelSliderDTO.setLinkUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
							.append(PropertiesUtil.getProperty("htmlRootPath")).append(tSubject.getFdetailHtmlUrl())
							.toString());
				}
				if (channelSliderDTO.getUrlType() == 4 || channelSliderDTO.getUrlType() == 5) {
					// 如果轮播类型是优惠券链接或者外部链接
					channelSliderDTO.setLinkUrl(bmap.get("fexternalUrl").toString());
				}
				sliderList.add(channelSliderDTO);
			}
			returnData.put("channelSliderList", sliderList);
		} else {
			returnData.put("slider", false);
		}
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * 获取首页栏目和轮播 service
	 * 
	 * @param cityId
	 *            城市ID
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getIndexPageInfo(Integer cityId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (cityId == null || cityId.intValue() <= 0) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("cityId参数不能为空，请检查cityId的传递参数值！");
			return responseDTO;
		}
		Map<Integer, String> cityMap = DictionaryUtil.getStatueMap(DictionaryUtil.City);

		// 找不到城市ID就默认为北京
		if (!cityMap.containsKey(cityId)) {
			cityId = 1;
		}

		TAppChannelSetting tAppChannelSetting = appChannelSettingDAO.getIndexSilderChannel(cityId);

		List<AppChannelSliderDTO> appChannelSliderResultList = Lists.newArrayList();
		// APP首页轮播图列表
		if (tAppChannelSetting != null) {
			// 加载轮播图信息
			StringBuilder hql = new StringBuilder();
			hql.append(
					"select t.fimage as fimage, t.furlType as furlType, t.fentityId as fentityId, t.fentityTitle as fentityTitle, t.fexternalUrl as fexternalUrl from TAppChannelSlider t where t.TAppChannelSetting.id = :channelId and t.fisVisible = 1 order by t.forder");
			Map<String, Object> hqlMap = new HashMap<String, Object>();
			hqlMap.put("channelId", tAppChannelSetting.getId());
			List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);

			AppChannelSliderDTO appChannelSliderDTO = null;
			for (Map<String, Object> bmap : list) {
				appChannelSliderDTO = new AppChannelSliderDTO();
				appChannelSliderDTO.setUrlType((Integer) bmap.get("furlType"));
				if (bmap.get("fentityId") != null && StringUtils.isNotBlank(bmap.get("fentityId").toString())) {
					appChannelSliderDTO.setEntityId(bmap.get("fentityId").toString());
				}
				appChannelSliderDTO.setImageUrl(fxlService.getImageUrl(bmap.get("fimage").toString(), false));

				if (appChannelSliderDTO.getUrlType() == 3) {
					// 如果轮播类型是专题链接
					TSubject tSubject = subjectDAO.getOne(bmap.get("fentityId").toString());
					appChannelSliderDTO.setLinkUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
							.append(PropertiesUtil.getProperty("htmlRootPath")).append(tSubject.getFdetailHtmlUrl())
							.toString());
				}
				if (appChannelSliderDTO.getUrlType() == 4 || appChannelSliderDTO.getUrlType() == 5) {
					// 如果轮播类型是优惠券链接或者外部链接
					appChannelSliderDTO.setLinkUrl(bmap.get("fexternalUrl").toString());
				}
				appChannelSliderResultList.add(appChannelSliderDTO);
			}
		}
		// 获取到本城市id下的所有栏目列表，准备根据不同的栏目类型来组成不同的栏目列表
		List<TAppChannelSetting> tAppChannelSettingList = appChannelSettingDAO.findChannelListByCityId(cityId);

		// 定义活动位栏目列表(activity)
		List<AppChannelSettingDTO> indexActivitycList = Lists.newArrayList();
		// 定义固定位栏目列表(fixed)
		List<AppChannelSettingDTO> indexFixedList = Lists.newArrayList();
		// 定义TAB位栏目列表(fixed)
		List<AppChannelSettingDTO> indexTabList = Lists.newArrayList();

		AppChannelSettingDTO appChannelSettingDTO = null;
		for (TAppChannelSetting tAppChannel : tAppChannelSettingList) {
			appChannelSettingDTO = new AppChannelSettingDTO();
			appChannelSettingDTO.setChannelId(tAppChannel.getId());
			// 如果栏目类型是活动位栏目，则进入活动栏目列表
			if (tAppChannel.getFtype().intValue() == 1) {
				if (StringUtils.isNotBlank(tAppChannel.getFicon())) {
					appChannelSettingDTO.setImageUrl(fxlService.getImageUrl(tAppChannel.getFicon(), false));
				}
				appChannelSettingDTO.setPromotion(tAppChannel.getFpromotion());
				appChannelSettingDTO.setTitle(tAppChannel.getFtitle());
				indexActivitycList.add(appChannelSettingDTO);
			} else if (tAppChannel.getFtype().intValue() == 2) {
				// 如果栏目类型是固定位栏目，则进入固定位栏目列表
				if (StringUtils.isNotBlank(tAppChannel.getFicon())) {
					appChannelSettingDTO.setImageUrl(fxlService.getImageUrl(tAppChannel.getFicon(), false));
				}
				appChannelSettingDTO.setPromotion(tAppChannel.getFpromotion());
				appChannelSettingDTO.setTitle(tAppChannel.getFtitle());
				indexFixedList.add(appChannelSettingDTO);
			} else if (tAppChannel.getFtype().intValue() == 3) {
				// 如果栏目类型是TAB位栏目，则进入TAB位栏目列表
				appChannelSettingDTO.setTitle(tAppChannel.getFtitle());
				indexTabList.add(appChannelSettingDTO);
			}
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("indexSliderList", appChannelSliderResultList);
		returnData.put("indexChannelActivityList", indexActivitycList);
		returnData.put("indexChannelFixedList", indexFixedList);
		returnData.put("indexChannelTabList", indexTabList);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * WEB端新版首页信息方法
	 * 
	 * @param cityId
	 *            城市ID
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getNewIndexPageInfo(Integer cityId, Integer clientType) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (cityId == null || cityId.intValue() <= 0) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("cityId参数不能为空，请检查cityId的传递参数值！");
			return responseDTO;
		}
		Map<Integer, String> cityMap = DictionaryUtil.getStatueMap(DictionaryUtil.City);

		// 找不到城市ID就默认为北京
		if (!cityMap.containsKey(cityId)) {
			cityId = 1;
		}

		TAppChannelSetting tAppChannelSetting = appChannelSettingDAO.getIndexSilderChannel(cityId);

		List<AppChannelSliderDTO> appChannelSliderResultList = Lists.newArrayList();
		// APP首页轮播图列表
		if (tAppChannelSetting != null) {
			// 加载轮播图信息
			StringBuilder hql = new StringBuilder();
			hql.append(
					"select t.fimage as fimage, t.furlType as furlType, t.fentityId as fentityId, t.fentityTitle as fentityTitle, t.fexternalUrl as fexternalUrl from TAppChannelSlider t where t.TAppChannelSetting.id = :channelId and t.fisVisible = 1 order by t.forder");
			Map<String, Object> hqlMap = new HashMap<String, Object>();
			hqlMap.put("channelId", tAppChannelSetting.getId());
			List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);

			AppChannelSliderDTO appChannelSliderDTO = null;
			for (Map<String, Object> bmap : list) {
				appChannelSliderDTO = new AppChannelSliderDTO();
				appChannelSliderDTO.setUrlType((Integer) bmap.get("furlType"));
				if (bmap.get("fentityId") != null && StringUtils.isNotBlank(bmap.get("fentityId").toString())) {
					appChannelSliderDTO.setEntityId(bmap.get("fentityId").toString());
				}
				appChannelSliderDTO.setImageUrl(fxlService.getImageUrl(bmap.get("fimage").toString(), false));

				if (appChannelSliderDTO.getUrlType() == 3) {
					// 如果轮播类型是专题链接
					TSubject tSubject = subjectDAO.getOne(bmap.get("fentityId").toString());
					appChannelSliderDTO.setLinkUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
							.append(PropertiesUtil.getProperty("htmlRootPath")).append(tSubject.getFdetailHtmlUrl())
							.toString());
				}
				if (appChannelSliderDTO.getUrlType() == 4 || appChannelSliderDTO.getUrlType() == 5) {
					// 如果轮播类型是优惠券链接或者外部链接
					appChannelSliderDTO.setLinkUrl(bmap.get("fexternalUrl").toString());
				}
				appChannelSliderResultList.add(appChannelSliderDTO);
			}
		}
		// 获取到本城市id下的所有栏目列表，准备根据不同的栏目类型来组成不同的栏目列表
		List<TAppChannelSetting> tAppChannelSettingList = null;
		if (clientType.intValue() == 1) {
			tAppChannelSettingList = appChannelSettingDAO.findChannelListByCityIdAndWebClient(cityId);
		} else if (clientType.intValue() == 2) {
			tAppChannelSettingList = appChannelSettingDAO.findChannelListByCityIdAndIOSClient(cityId);
		} else if (clientType.intValue() == 3) {
			tAppChannelSettingList = appChannelSettingDAO.findChannelListByCityIdAndAndroidClient(cityId);
		}

		// 定义活动位栏目列表(activity)
		List<AppChannelSettingDTO> indexActivityList = Lists.newArrayList();
		// 定义固定位栏目列表(fixed)
		List<AppChannelSettingDTO> indexFixedList = Lists.newArrayList();

		AppChannelSettingDTO appChannelSettingDTO = null;
		for (TAppChannelSetting tAppChannel : tAppChannelSettingList) {
			appChannelSettingDTO = new AppChannelSettingDTO();
			appChannelSettingDTO.setChannelId(tAppChannel.getId());
			// 如果当前客户端是web端的话，判断栏目的web端类型字段来决定栏目的位置
			if (clientType.intValue() == 1) {
				if (StringUtils.isNotBlank(tAppChannel.getFicon())) {
					appChannelSettingDTO.setImageUrl(fxlService.getImageUrl(tAppChannel.getFicon(), false));
				}
				appChannelSettingDTO.setPromotion(tAppChannel.getFpromotion());
				appChannelSettingDTO.setTitle(tAppChannel.getFtitle());
				// 如果栏目类型是活动位栏目，则进入活动栏目列表
				if (tAppChannel.getFwebType() != null) {
					if (tAppChannel.getFwebType().intValue() == 1) {
						indexActivityList.add(appChannelSettingDTO);
					} else if (tAppChannel.getFwebType().intValue() == 2) {
						// 如果栏目类型是固定位栏目，则进入固定位栏目列表
						indexFixedList.add(appChannelSettingDTO);
					}
				}
			} else {
				// 如果当前客户端是APP端的话，判断栏目的app端类型字段来决定栏目的位置
				if (StringUtils.isNotBlank(tAppChannel.getFicon())) {
					appChannelSettingDTO.setImageUrl(fxlService.getImageUrl(tAppChannel.getFicon(), false));
				}
				appChannelSettingDTO.setPromotion(tAppChannel.getFpromotion());
				appChannelSettingDTO.setTitle(tAppChannel.getFtitle());

				// 如果栏目类型是活动位栏目，则进入活动栏目列表
				if (tAppChannel.getFtype() != null) {
					if (tAppChannel.getFtype().intValue() == 1) {
						indexActivityList.add(appChannelSettingDTO);
					} else if (tAppChannel.getFtype().intValue() == 2) {
						// 如果栏目类型是固定位栏目，则进入固定位栏目列表
						indexFixedList.add(appChannelSettingDTO);
					}
				}
			}
		}

		// 定义首页广告位列表
		List<AppChannelSliderDTO> indexAvertising1List = Lists.newArrayList();
		List<AppChannelSliderDTO> indexAvertising2List = Lists.newArrayList();

		// 加载轮播图信息
		StringBuilder hql = new StringBuilder();
		hql.append(
				"select t.fimage as fimage, t.furlType as furlType, t.fentityId as fentityId, t.fentityTitle as fentityTitle, t.fexternalUrl as fexternalUrl, t.flocationNum as flocationNum from TAppIndexItem t where t.fcity = :cityId and t.fisVisible = 1");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("cityId", cityId);
		if (clientType.intValue() == 1) {
			hql.append(" and t.ffrontType in (1,2)");
		} else if (clientType.intValue() == 2) {
			hql.append(" and t.ffrontType in (1,3,5)");
		} else if (clientType.intValue() == 3) {
			hql.append(" and t.ffrontType in (1,4,5)");
		}
		hql.append(" order by t.forder asc");

		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);

		AppChannelSliderDTO appChannelSliderDTO = null;
		int locationNum = 0;
		for (Map<String, Object> bmap : list) {
			appChannelSliderDTO = new AppChannelSliderDTO();
			appChannelSliderDTO.setUrlType((Integer) bmap.get("furlType"));
			if (bmap.get("flocationNum") != null && StringUtils.isNotBlank(bmap.get("flocationNum").toString())) {
				locationNum = (Integer) bmap.get("flocationNum");
			} else {
				locationNum = 0;
			}
			if (bmap.get("fentityId") != null && StringUtils.isNotBlank(bmap.get("fentityId").toString())) {
				appChannelSliderDTO.setEntityId(bmap.get("fentityId").toString());
			}
			appChannelSliderDTO.setImageUrl(fxlService.getImageUrl(bmap.get("fimage").toString(), false));

			if (appChannelSliderDTO.getUrlType() == 3) {
				// 如果轮播类型是专题链接
				TSubject tSubject = subjectDAO.getOne(bmap.get("fentityId").toString());
				appChannelSliderDTO.setLinkUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
						.append(PropertiesUtil.getProperty("htmlRootPath")).append(tSubject.getFdetailHtmlUrl())
						.toString());
			}
			if (appChannelSliderDTO.getUrlType() == 4 || appChannelSliderDTO.getUrlType() == 5) {
				// 如果轮播类型是优惠券链接或者外部链接
				appChannelSliderDTO.setLinkUrl(bmap.get("fexternalUrl").toString());
			}
			if (locationNum == 1) {
				indexAvertising1List.add(appChannelSliderDTO);
			} else if (locationNum == 2) {
				indexAvertising2List.add(appChannelSliderDTO);
			}
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("indexSliderList", appChannelSliderResultList);
		returnData.put("indexActivityList", indexActivityList);
		returnData.put("indexFixedList", indexFixedList);
		returnData.put("indexAvertising1List", indexAvertising1List);
		returnData.put("indexAvertising2List", indexAvertising2List);
		returnData.put("indexEventListChannelId", ConfigurationUtil
				.getPropertiesValue(ResponseConfigurationDict.RESPONSE_PROPERTIES_OPTIMIZATIONCHANNELID));
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * 设置当前客户端坐标并且保存该坐标与所有活动之间的距离的方法
	 * 
	 * @param sessionId
	 * @param gps
	 * @return
	 */
	public ResponseDTO setLocation(String sessionId, String gps, String ip, String ticket, Integer clientType) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(sessionId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("sessionId参数不能为空，请检查sessionId的传递参数值！");
			return responseDTO;
		}

		boolean gpsIsError = false;
		if (StringUtils.isBlank(gps) || !gps.contains(",") || gps.contains("E")) {
			gpsIsError = true;
		}
		String[] gpsa = StringUtils.split(gps, ',');
		if (gpsa.length != 2) {
			gpsIsError = true;
		} else if (gpsa.length == 2) {
			if (!NumberUtils.isCreatable(gpsa[0]) || !NumberUtils.isCreatable(gpsa[1])) {
				gpsIsError = true;
			}
		} else {
			gpsIsError = true;
		}

		// 如果传入的gps格式信息错误，将使用百度地图IP地址定位方式
		if (gpsIsError) {
			gps = BmapUtil.getGpsByIp(ip);
		}
		if (StringUtils.isBlank(gps)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("gps参数有误，请检查gps的传递参数值！");
			return responseDTO;
		}
		String[] localGps = gps.split(",");
		Cache sessionIdEventDistanceCache = cacheManager.getCache(Constant.SessionIdEventDistance);
		Map<String, EventDistanceDTO> eventDistanceMap = Maps.newHashMap();
		Element element = new Element(sessionId, eventDistanceMap);
		sessionIdEventDistanceCache.put(element);

		try {

			StringBuilder hql = new StringBuilder();
			hql.append(
					"select t.id as id, s.fgps as fgps from TEvent t inner join t.TEventSessions s where t.fstatus = 20 and s.fstatus < 999 and s.fgps is not null and s.fgps != ''");
			// and (s.fdeadline is null or s.fdeadline > now())
			List<Map<String, Object>> list = commonService.find(hql.toString());

			EventDistanceDTO eventDistanceDTO = null;
			BigDecimal distance = null;
			String eventId = null;
			String[] targetGps = null;
			for (Map<String, Object> amap : list) {
				eventId = amap.get("id").toString();
				targetGps = amap.get("fgps").toString().split(",");

				distance = BigDecimal.valueOf(
						BmapUtil.GetShortDistance(Double.parseDouble(localGps[0]), Double.parseDouble(localGps[1]),
								Double.parseDouble(targetGps[0]), Double.parseDouble(targetGps[1])));
				distance = distance.setScale(0, RoundingMode.HALF_UP);
				// 如果活动已经保存到活动距离MAP中的
				if (eventDistanceMap.containsKey(eventId)) {
					eventDistanceDTO = eventDistanceMap.get(eventId);
					// 如果活动的场次距离比新计算出的场次距离远，则保留距离近的场次
					if (eventDistanceDTO.getDistance() > distance.intValue()) {

						eventDistanceDTO = new EventDistanceDTO();
						eventDistanceDTO.setEventId(eventId);
						eventDistanceDTO.setDistance(distance.setScale(0, RoundingMode.HALF_UP).intValue());
						if (distance.compareTo(new BigDecimal(1000)) < 0) {
							eventDistanceDTO.setDistanceString(distance + "m");
						} else if (distance.compareTo(new BigDecimal(1000000)) > 0) {
							eventDistanceDTO.setDistanceString(">1000km");
						} else {
							distance = distance.divide(new BigDecimal(1000), 1, RoundingMode.HALF_UP);
							eventDistanceDTO.setDistanceString(distance.toString() + "km");
						}
						eventDistanceDTO.setGps(amap.get("fgps").toString());
						eventDistanceDTO.setGpsLng(targetGps[0]);
						eventDistanceDTO.setGpsLat(targetGps[1]);
						eventDistanceMap.put(eventId, eventDistanceDTO);
					}
				} else {
					eventDistanceDTO = new EventDistanceDTO();
					eventDistanceDTO.setEventId(eventId);
					eventDistanceDTO.setDistance(distance.setScale(0, RoundingMode.HALF_UP).intValue());
					if (distance.compareTo(new BigDecimal(1000)) < 0) {
						eventDistanceDTO.setDistanceString(distance + "m");
					} else if (distance.compareTo(new BigDecimal(1000000)) > 0) {
						eventDistanceDTO.setDistanceString(">1000km");
					} else {
						distance = distance.divide(new BigDecimal(1000), 1, RoundingMode.HALF_UP);
						eventDistanceDTO.setDistanceString(distance.toString() + "km");
					}
					eventDistanceDTO.setGps(amap.get("fgps").toString());
					eventDistanceDTO.setGpsLng(targetGps[0]);
					eventDistanceDTO.setGpsLat(targetGps[1]);
					eventDistanceMap.put(eventId, eventDistanceDTO);
				}
			}

			long createTime = new Date().getTime();
			List<TEventDistanceTemp> distanceTempList = Lists.newArrayList();
			TEventDistanceTemp tEventDistanceTemp = null;

			for (Iterator it = eventDistanceMap.entrySet().iterator(); it.hasNext();) {
				Map.Entry e = (Map.Entry) it.next();
				eventDistanceDTO = eventDistanceMap.get(e.getKey());

				tEventDistanceTemp = new TEventDistanceTemp();
				tEventDistanceTemp.setFcreateTime(createTime);
				tEventDistanceTemp.setFhsid(sessionId);
				tEventDistanceTemp.setFdistance(0 - eventDistanceDTO.getDistance());
				tEventDistanceTemp.setFeventId(eventDistanceDTO.getEventId());
				distanceTempList.add(tEventDistanceTemp);
			}
			// 异步保存活动距离临时表数据
			CustomerEventDistanceBean ced = new CustomerEventDistanceBean();
			ced.setSessionId(sessionId);
			ced.setEventDistanceTempList(distanceTempList);
			ced.setTaskType(13);
			AsynchronousTasksManager.put(ced);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			throw new ServiceException(e.getMessage());
		}

		if (StringUtils.isNotBlank(ticket)) {
			// 进行异步线程处理，保存客户终端的启动信息
			AppStartupBean asu = new AppStartupBean();
			String customerId = fxlService.getCustomerIdByTicket(ticket, clientType);
			asu.setCustomerId(customerId);
			asu.setGps(gps);
			asu.setClientType(clientType);
			asu.setTaskType(11);
			AsynchronousTasksManager.put(asu);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("设定用户位置成功！");
		return responseDTO;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveDistanceTemp(CustomerEventDistanceBean ced) {
		// 先将该SessionId的活动距离临时表的记录清除
		eventDistanceTempDAO.deleteDistanceTempByHsid(ced.getSessionId());

		List<TEventDistanceTemp> list = ced.getEventDistanceTempList();
		eventDistanceTempDAO.save(list);
	}

	/**
	 * 活动距离临时表。5分钟执行一次，将当前时间2小时之前的临时记录清除
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int deleteDistanceTempByOverdue() {
		long overdue = DateUtils.addHours(new Date(), -2).getTime();
		return eventDistanceTempDAO.deleteDistanceTempByOverdue(overdue);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public void initAppVersionMap() {
		Map<Integer, AppVersionDTO> avMap = Constant.getAppVersionMap();
		
		Map<Integer, AppVersionDTO> sponsorVersionMap = Constant.getSponsorVersionMap();

		StringBuilder hql = new StringBuilder();
		hql.append(
				"select t.freleaseDate as freleaseDate,t.fappType as fappType,t.fpathway as fpathway, t.fversionNum as fversionNum, t.fversionValue as fversionValue, t.fforceUpgradeValue as fforceUpgradeValue, t.fdescription as fdescription, t.fappUrl as fappUrl from TAppVersion t where t.freleaseDate < now() order by t.fversionValue desc");
		Map<String, Object> hqlMap = Maps.newHashMap();

		Query q = commonService.createQuery(hql.toString(), hqlMap);
		q.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> list2 = q.getResultList();

		AppVersionDTO appVersionDTO = null;
		for(Map<String, Object> bmap:list2){
			if (MapUtils.isNotEmpty(bmap)) {
				appVersionDTO = new AppVersionDTO();
				if(bmap.get("fappType") != null && StringUtils.isNotBlank(bmap.get("fappType").toString())){
					appVersionDTO.setAppType((Integer)bmap.get("fappType"));
				}
				if (bmap.get("fversionNum") != null && StringUtils.isNotBlank(bmap.get("fversionNum").toString())) {
					appVersionDTO.setVersionNum(bmap.get("fversionNum").toString());
				}
				if (bmap.get("fversionValue") != null && StringUtils.isNotBlank(bmap.get("fversionValue").toString())) {
					appVersionDTO.setVersionValue((Long) bmap.get("fversionValue"));
				}
				if (bmap.get("fforceUpgradeValue") != null
						&& StringUtils.isNotBlank(bmap.get("fforceUpgradeValue").toString())) {
					if(((Long) bmap.get("fforceUpgradeValue")).equals(new Long(0))){
						appVersionDTO.setIfUpdate(true);
					}
				}
				if (bmap.get("freleaseDate") != null && StringUtils.isNotBlank(bmap.get("freleaseDate").toString())) {
					Date date = (Date) bmap.get("freleaseDate");
					appVersionDTO.setReleaseDate(DateFormatUtils.format(date, "yyyy-MM-dd HH:mm"));
				}
				if (bmap.get("fdescription") != null && StringUtils.isNotBlank(bmap.get("fdescription").toString())) {
					appVersionDTO.setDescription(bmap.get("fdescription").toString());
				}
				if (bmap.get("fappUrl") != null && StringUtils.isNotBlank(bmap.get("fappUrl").toString())) {
					AndroidUrlDTO androidUrlDTO = mapper.fromJson(bmap.get("fappUrl").toString(), AndroidUrlDTO.class);
					appVersionDTO.setAppUrls(androidUrlDTO);
				}
				if((Integer)bmap.get("fpathway") == 1){
					avMap.put((Integer)bmap.get("fappType"), appVersionDTO);
				}else if((Integer)bmap.get("fpathway") == 2){
					sponsorVersionMap.put((Integer)bmap.get("fappType"), appVersionDTO);
				}
			}
		}
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public void resetAppVersionMap(String id) {
		Map<Integer, AppVersionDTO> avMap = Constant.getAppVersionMap();

		TAppVersion tAppVersion = appVersionDAO.findOne(id);
		if (tAppVersion != null) {
			AppVersionDTO appVersionDTO = new AppVersionDTO();
			appVersionDTO.setAppType(tAppVersion.getFappType());
			appVersionDTO.setVersionNum(tAppVersion.getFversionNum());
			appVersionDTO.setVersionValue(tAppVersion.getFversionValue());
			appVersionDTO.setReleaseDate(DateFormatUtils.format(tAppVersion.getFreleaseDate(), "yyyy-MM-dd HH:mm"));
			if(tAppVersion.getFforceUpgradeValue().equals(new Long(0))){
				appVersionDTO.setIfUpdate(true);
			}
			appVersionDTO.setDescription(tAppVersion.getFdescription());
			if (StringUtils.isNotBlank(tAppVersion.getFappUrl())) {
				AndroidUrlDTO androidUrlDTO = mapper.fromJson(tAppVersion.getFappUrl(), AndroidUrlDTO.class);
				appVersionDTO.setAppUrls(androidUrlDTO);
			}
			avMap.put(tAppVersion.getFappType(), appVersionDTO);
		}
	}

	@Transactional(readOnly = true)
	public ResponseDTO getAppVersion(Integer clientType) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("clientType参数不能为空，请检查clientType的传递参数值！");
			return responseDTO;
		}
		Map<Integer, AppVersionDTO> avMap = Constant.getAppVersionMap();
		AppVersionDTO appVersionDTO = avMap.get(clientType);

		Map<String, Object> returnData = Maps.newHashMap();
		if (appVersionDTO == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("服务器端没有本移动端的版本信息！");
			return responseDTO;
		}
		returnData.put("appVersion", appVersionDTO);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setData(returnData);
		responseDTO.setMsg("获取应用版本信息成功！");
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getArticleList(Integer cityId, Integer pageSize, Integer offset) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (cityId == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("cityId参数不能为空，请检查cityId的传递参数值！");
			return responseDTO;
		}
		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}

		// 加载栏目轮播图信息
		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hql.append(
				"select t.id as id, t.ftitle as ftitle, t.ftype as ftype, t.fimage as fimage, t.fbrief as fbrief, t.fdetailHtmlUrl as fdetailHtmlUrl, t.frecommend as frecommend, t.fcomment as fcomment, t.fcreateTime as fcreateTime from TArticle t where t.fcityId in (0 , :cityId) and t.fstatus = 20 order by t.forder desc");
		hqlMap.put("cityId", cityId);
		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();

		Cache articleRecommendCache = cacheManager.getCache(Constant.ArticleRecommend);
		Element ele = null;

		Date now = new Date();
		Date date = null;
		List<ArticleDTO> articleList = Lists.newArrayList();
		ArticleDTO articleDTO = null;
		for (Map<String, Object> amap : list) {
			articleDTO = new ArticleDTO();
			articleDTO.setArticleId(amap.get("id").toString());
			if (amap.get("ftitle") != null && StringUtils.isNotBlank(amap.get("ftitle").toString())) {
				articleDTO.setTitle(amap.get("ftitle").toString());
			}
			if (amap.get("ftype") != null && StringUtils.isNotBlank(amap.get("ftype").toString())) {
				articleDTO.setType(amap.get("ftype").toString());
				// 添加tag标签背景色数据
				articleDTO.setTypeColor(RandomColorUtil.getRandowColor());
			}
			if (amap.get("fimage") != null && StringUtils.isNotBlank(amap.get("fimage").toString())) {
				articleDTO.setImageUrl(fxlService.getImageUrl(amap.get("fimage").toString(), false));
			} else {
				articleDTO.setImageUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
						.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPic.jpg").toString());
			}
			if (amap.get("fbrief") != null && StringUtils.isNotBlank(amap.get("fbrief").toString())) {
				articleDTO.setBrief(amap.get("fbrief").toString());
			}
			if (amap.get("fdetailHtmlUrl") != null && StringUtils.isNotBlank(amap.get("fdetailHtmlUrl").toString())) {
				articleDTO.setDetailHtmlUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
						.append(PropertiesUtil.getProperty("htmlRootPath"))
						.append(amap.get("fdetailHtmlUrl").toString()).toString());
			}
			ele = articleRecommendCache.get(articleDTO.getArticleId());
			if (ele != null) {
				ArticleRecommendDTO articleRecommendDTO = (ArticleRecommendDTO) ele.getObjectValue();
				articleDTO.setRecommend(articleRecommendDTO.getRecommend());
			} else if (amap.get("frecommend") != null && StringUtils.isNotBlank(amap.get("frecommend").toString())) {
				articleDTO.setRecommend((Long) amap.get("frecommend"));
			}
			if (amap.get("fcomment") != null && StringUtils.isNotBlank(amap.get("fcomment").toString())) {
				articleDTO.setComment((Long) amap.get("fcomment"));
			}
			if (amap.get("fcreateTime") != null && StringUtils.isNotBlank(amap.get("fcreateTime").toString())) {
				date = (Date) amap.get("fcreateTime");
				if (DateUtils.truncatedEquals(date, now, Calendar.YEAR)) {
					articleDTO.setCreateTime(DateFormatUtils.format(date, "MM-dd HH:mm"));
				} else {
					articleDTO.setCreateTime(DateFormatUtils.format(date, "yyyy-MM-dd HH:mm"));
				}
			}
			articleList.add(articleDTO);
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("articleList", articleList);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getArticle(String articleId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(articleId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("articleId参数不能为空，请检查articleId的传递参数值！");
			return responseDTO;
		}

		Cache articleRecommendCache = cacheManager.getCache(Constant.ArticleRecommend);
		Element ele = null;

		ArticleDTO articleDTO = new ArticleDTO();
		TArticle tArticle = articleDAO.getOne(articleId);
		articleDTO.setArticleId(articleId);
		articleDTO.setTitle(tArticle.getFtitle());
		articleDTO.setType(tArticle.getFtype());
		// 添加tag标签背景色数据
		articleDTO.setTypeColor(RandomColorUtil.getRandowColor());
		if (tArticle.getFimage() != null) {
			articleDTO.setImageUrl(fxlService.getImageUrl(tArticle.getFimage().toString(), false));
		} else {
			articleDTO.setImageUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
					.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPic.jpg").toString());
		}

		articleDTO.setBrief(tArticle.getFbrief());
		articleDTO.setDetailHtmlUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
				.append(PropertiesUtil.getProperty("htmlRootPath")).append(tArticle.getFdetailHtmlUrl()).toString());
		ele = articleRecommendCache.get(articleDTO.getArticleId());
		if (ele != null) {
			ArticleRecommendDTO articleRecommendDTO = (ArticleRecommendDTO) ele.getObjectValue();
			articleDTO.setRecommend(articleRecommendDTO.getRecommend());
		} else {
			articleDTO.setRecommend(tArticle.getFrecommend());
		}
		articleDTO.setComment(tArticle.getFcomment());
		if (tArticle.getFcreateTime() != null) {
			Date date = tArticle.getFcreateTime();
			if (DateUtils.truncatedEquals(tArticle.getFcreateTime(), new Date(), Calendar.YEAR)) {
				articleDTO.setCreateTime(DateFormatUtils.format(date, "MM-dd HH:mm"));
			} else {
				articleDTO.setCreateTime(DateFormatUtils.format(date, "yyyy-MM-dd HH:mm"));
			}
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("article", articleDTO);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * 推荐文章方法
	 * 
	 * @param articleId
	 *            文章ID
	 * @return 响应用户的json数据
	 */
	@Transactional(readOnly = true)
	public ResponseDTO clickArticleRecommend(String articleId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(articleId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("articleId参数不能为空，请检查articleId的传递参数值！");
			return responseDTO;
		}

		Map<String, Object> returnData = Maps.newHashMap();
		Cache articleRecommendCache = cacheManager.getCache(Constant.ArticleRecommend);
		Element ele = articleRecommendCache.get(articleId);
		if (ele == null) {
			Long recommend = articleDAO.findArticleRecommend(articleId);
			ArticleRecommendDTO articleRecommendDTO = new ArticleRecommendDTO();
			articleRecommendDTO.setArticleId(articleId);
			articleRecommendDTO.setRecommend(recommend == null ? 1 : recommend + 1);
			ele = new Element(articleId, articleRecommendDTO);
			articleRecommendCache.put(ele);
			returnData.put("total", articleRecommendDTO.getRecommend());
		} else {
			ArticleRecommendDTO articleRecommendDTO = (ArticleRecommendDTO) ele.getObjectValue();
			articleRecommendDTO.addOne();
			returnData.put("total", articleRecommendDTO.getRecommend());
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setData(returnData);
		responseDTO.setMsg("文章推荐点赞成功！");
		return responseDTO;
	}

	/**
	 * 将某个文章的推荐数缓存移除
	 * 
	 * @param articleId
	 */
	public void removeArticleRecommendCache(String articleId) {
		Cache articleRecommendCache = cacheManager.getCache(Constant.ArticleRecommend);
		Element ele = articleRecommendCache.get(articleId);
		if (ele == null) {
			return;
		}

		ArticleRecommendDTO articleRecommendDTO = (ArticleRecommendDTO) ele.getObjectValue();
		if (articleRecommendDTO.isChange()) {
			articleDAO.updateArticleRecommend(articleId, articleRecommendDTO.getRecommend());
		}
		articleRecommendCache.remove(articleId);
	}

	/**
	 * 将文章推荐数缓存同步到数据库中
	 * 
	 * @return
	 */
	public int savingArticleRecommendCache() {
		Cache articleRecommendCache = cacheManager.getCache(Constant.ArticleRecommend);
		Map<Object, Element> recommendMap = articleRecommendCache
				.getAll(articleRecommendCache.getKeysNoDuplicateCheck());
		int i = 0;
		for (Iterator it = recommendMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Object, Element> e = (Map.Entry<Object, Element>) it.next();
			Element ele = e.getValue();
			ArticleRecommendDTO articleRecommendDTO = (ArticleRecommendDTO) ele.getObjectValue();
			if (articleRecommendDTO.isChange()) {
				articleDAO.updateArticleRecommend(articleRecommendDTO.getArticleId(),
						articleRecommendDTO.getRecommend());
				articleRecommendDTO.setChange(false);
				i++;
			}
		}
		return i;
	}

}