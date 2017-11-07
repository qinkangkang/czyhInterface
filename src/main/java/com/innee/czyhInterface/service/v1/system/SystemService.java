package com.innee.czyhInterface.service.v1.system;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;
import org.springside.modules.utils.Identities;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JavaType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.common.dict.ResponseConfigurationDict;
import com.innee.czyhInterface.dao.AppChannelSettingDAO;
import com.innee.czyhInterface.dao.AppFlashDAO;
import com.innee.czyhInterface.dao.AppNoticeDAO;
import com.innee.czyhInterface.dao.ArticleDAO;
import com.innee.czyhInterface.dao.ColumnBannerDAO;
import com.innee.czyhInterface.dao.DictionaryCityDao;
import com.innee.czyhInterface.dao.DictionaryDao;
import com.innee.czyhInterface.dao.DictionaryRegionDao;
import com.innee.czyhInterface.dao.EventBargainingDAO;
import com.innee.czyhInterface.dao.EventDAO;
import com.innee.czyhInterface.dao.FavoriteDAO;
import com.innee.czyhInterface.dao.GoodsSpaceValueDAO;
import com.innee.czyhInterface.dao.SponsorDAO;
import com.innee.czyhInterface.dao.SubjectDAO;
import com.innee.czyhInterface.dao.SubobjectTimeStampDAO;
import com.innee.czyhInterface.dao.SysCityDAO;
import com.innee.czyhInterface.dao.SysProvinceDAO;
import com.innee.czyhInterface.dao.SysRegionDAO;
import com.innee.czyhInterface.dao.TTagDAO;
import com.innee.czyhInterface.dto.ArticleRecommendDTO;
import com.innee.czyhInterface.dto.CustomerDTO;
import com.innee.czyhInterface.dto.EventDistanceDTO;
import com.innee.czyhInterface.dto.m.AppChannelSettingDTO;
import com.innee.czyhInterface.dto.m.AppChannelSliderDTO;
import com.innee.czyhInterface.dto.m.AppShareDTO;
import com.innee.czyhInterface.dto.m.AppVersionDTO;
import com.innee.czyhInterface.dto.m.ArticleDTO;
import com.innee.czyhInterface.dto.m.EventSimpleDTO;
import com.innee.czyhInterface.dto.m.MerchantSimpleDTO;
import com.innee.czyhInterface.dto.m.PageDTO;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.dto.m.goods.GoodsDTO;
import com.innee.czyhInterface.dto.m.goods.OtherDTO;
import com.innee.czyhInterface.dto.m.solr.GoodsSolrDTO;
import com.innee.czyhInterface.dto.m.sponsor.SponsorDTO;
import com.innee.czyhInterface.dto.m.system.ArticleSimpleDTO;
import com.innee.czyhInterface.dto.m.system.CityListDTO;
import com.innee.czyhInterface.dto.m.system.ColumnDTO;
import com.innee.czyhInterface.dto.m.system.DictionaryDTO;
import com.innee.czyhInterface.dto.m.system.NoticeDTO;
import com.innee.czyhInterface.dto.m.system.TagDTO;
import com.innee.czyhInterface.dto.m.system.address.CityDTO;
import com.innee.czyhInterface.dto.m.system.address.ProvinceDTO;
import com.innee.czyhInterface.dto.m.system.address.RegionDTO;
import com.innee.czyhInterface.dto.m.system.indexInfo.AppAdvertDTO;
import com.innee.czyhInterface.entity.TAppChannelSetting;
import com.innee.czyhInterface.entity.TAppFlash;
import com.innee.czyhInterface.entity.TAppNotice;
import com.innee.czyhInterface.entity.TArticle;
import com.innee.czyhInterface.entity.TColumnBanner;
import com.innee.czyhInterface.entity.TCustomer;
import com.innee.czyhInterface.entity.TDictionary;
import com.innee.czyhInterface.entity.TDictionaryCity;
import com.innee.czyhInterface.entity.TDictionaryRegion;
import com.innee.czyhInterface.entity.TEvent;
import com.innee.czyhInterface.entity.TEventBargaining;
import com.innee.czyhInterface.entity.TEventDistanceTemp;
import com.innee.czyhInterface.entity.TEventSession;
import com.innee.czyhInterface.entity.TFavorite;
import com.innee.czyhInterface.entity.TGoodsSpaceValue;
import com.innee.czyhInterface.entity.TSponsor;
import com.innee.czyhInterface.entity.TSubject;
import com.innee.czyhInterface.entity.TSubobjectTimeStamp;
import com.innee.czyhInterface.entity.TSysCity;
import com.innee.czyhInterface.entity.TSysProvince;
import com.innee.czyhInterface.entity.TSysRegion;
import com.innee.czyhInterface.entity.TTag;
import com.innee.czyhInterface.exception.ServiceException;
import com.innee.czyhInterface.service.v1.category.CategoryService;
import com.innee.czyhInterface.service.v2.CommonService;
import com.innee.czyhInterface.service.v2.FxlService;
import com.innee.czyhInterface.util.CommonPage;
import com.innee.czyhInterface.util.ConfigurationUtil;
import com.innee.czyhInterface.util.Constant;
import com.innee.czyhInterface.util.DictionaryUtil;
import com.innee.czyhInterface.util.PropertiesUtil;
import com.innee.czyhInterface.util.RandomColorUtil;
import com.innee.czyhInterface.util.asynchronoustasks.AsynchronousTasksManager;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.AppStartupBean;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.CustomerEventDistanceBean;
import com.innee.czyhInterface.util.bmap.BmapUtil;
import com.innee.czyhInterface.util.bufferedImage.DrawTableImg;
import com.innee.czyhInterface.util.solr.SolrUtil;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * 系统类接口
 * 
 * @author jinshenghzi
 *
 */
@Component
@Transactional
public class SystemService {

	private static final Logger logger = LoggerFactory.getLogger(SystemService.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private CommonService commonService;

	@Autowired
	private FxlService fxlService;

	@Autowired
	private AppChannelSettingDAO appChannelSettingDAO;

	@Autowired
	private SubjectDAO subjectDAO;

	@Autowired
	private TTagDAO tTagDAO;

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private EventDAO eventDAO;

	@Autowired
	private DictionaryCityDao dictionaryCityDao;

	@Autowired
	private DictionaryRegionDao dictionaryRegionDao;

	@Autowired
	private DictionaryDao dictionaryDao;

	@Autowired
	private SysProvinceDAO sysProvinceDAO;

	@Autowired
	private SysCityDAO sysCityDAO;

	@Autowired
	private SysRegionDAO sysRegionDAO;

	@Autowired
	private FavoriteDAO favoriteDAO;

	@Autowired
	private ColumnBannerDAO columnBannerDAO;

	@Autowired
	private AppFlashDAO appFlashDAO;

	@Autowired
	private ArticleDAO articleDAO;

	@Autowired
	private SponsorDAO sponsorDAO;

	@Autowired
	private AppNoticeDAO appNoticeDAO;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private SubobjectTimeStampDAO subobjectTimeStampDAO;

	@Autowired
	private GoodsSpaceValueDAO goodsSpaceValueDAO;

	@Autowired
	private RedisService redisService;

	@Autowired
	private EventBargainingDAO eventBargainingDAO;

	@Transactional(readOnly = true)
	public ResponseDTO getIndexPageInfo(Integer cityId, Integer clientType) {
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

		// app首页上部轮播图
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
				if (bmap.get("fentityTitle") != null && StringUtils.isNotBlank(bmap.get("fentityTitle").toString())) {
					appChannelSliderDTO.setEntityTitle(bmap.get("fentityTitle").toString());
				}
				if (appChannelSliderDTO.getUrlType() == 6) {
					// 如果轮播类型是优惠券链接或者外部链接
					if (bmap.get("fexternalUrl") != null
							&& StringUtils.isNotBlank(bmap.get("fexternalUrl").toString())) {
						appChannelSliderDTO.setLinkUrl(bmap.get("fexternalUrl").toString());
					}

				}
				appChannelSliderResultList.add(appChannelSliderDTO);

			}
		}

		// app首页下部轮播图
		TAppChannelSetting tAppChannelDownSetting = appChannelSettingDAO.getIndexDownSilderChannel(cityId);

		List<AppChannelSliderDTO> appDownChannelSliderResultList = Lists.newArrayList();
		// APP首页轮播图列表
		if (tAppChannelSetting != null) {
			// 加载轮播图信息
			StringBuilder hql = new StringBuilder();
			hql.append(
					"select t.fimage as fimage, t.furlType as furlType, t.fentityId as fentityId, t.fentityTitle as fentityTitle, t.fexternalUrl as fexternalUrl from TAppChannelSlider t where t.TAppChannelSetting.id = :channelId and t.fisVisible = 1 order by t.forder");
			Map<String, Object> hqlMap = new HashMap<String, Object>();
			hqlMap.put("channelId", tAppChannelDownSetting.getId());
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
					if (bmap.get("fexternalUrl") != null
							&& StringUtils.isNotBlank(bmap.get("fexternalUrl").toString())) {
						appChannelSliderDTO.setLinkUrl(bmap.get("fexternalUrl").toString());
					}
				}
				appDownChannelSliderResultList.add(appChannelSliderDTO);
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
				"select t.fimage as fimage, t.furlType as furlType, t.fentityId as fentityId, t.fentityTitle as fentityTitle, t.fexternalUrl as fexternalUrl, t.flocationNum as flocationNum,flagType as flagType from TAppIndexItem t where t.fcity = :cityId and t.fisVisible = 1");
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
		AppAdvertDTO appAdvertDTO = null;

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
				if (bmap.get("fexternalUrl") != null && StringUtils.isNotBlank(bmap.get("fexternalUrl").toString())) {
					appChannelSliderDTO.setLinkUrl(bmap.get("fexternalUrl").toString());
				}
			}

			if (bmap.get("flagType").toString().equals("1")) {
				appAdvertDTO = new AppAdvertDTO();
				appAdvertDTO.setUrlType((Integer) bmap.get("furlType"));
				if (bmap.get("flocationNum") != null && StringUtils.isNotBlank(bmap.get("flocationNum").toString())) {
					locationNum = (Integer) bmap.get("flocationNum");
				} else {
					locationNum = 0;
				}
				if (bmap.get("fentityId") != null && StringUtils.isNotBlank(bmap.get("fentityId").toString())) {
					appAdvertDTO.setEntityId(bmap.get("fentityId").toString());
				}
				appAdvertDTO.setImageUrl(fxlService.getImageUrl(bmap.get("fimage").toString(), false));

				if (appAdvertDTO.getUrlType() == 3) {
					// 如果轮播类型是专题链接
					TSubject tSubject = subjectDAO.getOne(bmap.get("fentityId").toString());
					appAdvertDTO.setLinkUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
							.append(PropertiesUtil.getProperty("htmlRootPath")).append(tSubject.getFdetailHtmlUrl())
							.toString());
				}
				if (appAdvertDTO.getUrlType() == 6) {
					// 如果轮播类型是优惠券链接或者外部链接
					if (bmap.get("fexternalUrl") != null
							&& StringUtils.isNotBlank(bmap.get("fexternalUrl").toString())) {
						appAdvertDTO.setLinkUrl(bmap.get("fexternalUrl").toString());
					}
				}
				appAdvertDTO.setFlagType((Integer) bmap.get("flagType"));
			}

			if (locationNum == 1) {
				indexAvertising1List.add(appChannelSliderDTO);
			} else if (locationNum == 2) {
				indexAvertising2List.add(appChannelSliderDTO);
			}
		}

		// 首页公告
		List<TAppNotice> appNotices = appNoticeDAO.findOrderBy();
		List<NoticeDTO> noticeList = Lists.newArrayList();
		NoticeDTO noticeDTO = null;
		for (TAppNotice tAppNotice : appNotices) {
			noticeDTO = new NoticeDTO();
			noticeDTO.setNoticeName(tAppNotice.getFnoticeName());
			noticeDTO.setNoticeTime(tAppNotice.getFnoticeTime().toString());
			noticeDTO.setNoticeId(tAppNotice.getFnoticeId());
			noticeDTO.setNoticeType(tAppNotice.getFnoticeType());
			noticeDTO.setNoticeTitle(tAppNotice.getFnoticeTitle());
			noticeDTO.setNoticeUrl(tAppNotice.getFnoticeUrl());
			noticeList.add(noticeDTO);
		}

		// 首页栏目文字图
		long classId2 = 88;
		List<ColumnDTO> columnList = Lists.newArrayList();
		ColumnDTO columnDTO = null;
		List<TDictionary> tDictionaryCoumn = dictionaryDao.findOrderByClassId(classId2);
		for (TDictionary tDictionaryCoumn2 : tDictionaryCoumn) {
			columnDTO = new ColumnDTO();
			columnDTO.setColumnUrl(tDictionaryCoumn2.getName());
			columnDTO.setColumnName(tDictionaryCoumn2.getCode());
			columnList.add(columnDTO);
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("indexSliderList", appChannelSliderResultList);// 顶部轮播图
		returnData.put("indexDownSliderList", appDownChannelSliderResultList);// 底部轮播图
		returnData.put("indexActivityList", indexActivityList);// 首页活动位栏目(8个板块)
		// returnData.put("indexFixedList", indexFixedList);//首页固定位栏目列表
		returnData.put("indexAvertising", indexAvertising1List);// 中部一个广告位
		returnData.put("indexAvertising2List", indexAvertising2List);// 三个版块
		returnData.put("indexEventListChannelId", ConfigurationUtil
				.getPropertiesValue(ResponseConfigurationDict.RESPONSE_PROPERTIES_OPTIMIZATIONCHANNELID));// 精选栏目id
		returnData.put("noticeList", noticeList);// app公告
		returnData.put("columnList", columnList);// 栏目文章图片
		returnData.put("placeholderWord", DictionaryUtil.getString(DictionaryUtil.placeholderWord, 1));// 搜索占位字
		returnData.put("indexAdvert", appAdvertDTO);

		TColumnBanner tSeckilltime = columnBannerDAO.findColumnBanner(1, 1);

		if (tSeckilltime.getFseckillTime() != null) {
			Integer second = Seconds
					.secondsBetween(new DateTime(new Date()), new DateTime(tSeckilltime.getFseckillTime()))
					.getSeconds();
			returnData.put("goodsTime", second.toString());// 倒计时
		} else {
			returnData.put("goodsTime", 0);// 倒计时
		}
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("首页信息时加载成功！");
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
		String[] localGps = BmapUtil.gaoDeToBaidu(Double.valueOf(gps.split(",")[0]), Double.valueOf(gps.split(",")[1]));
		Cache sessionIdEventDistanceCache = cacheManager.getCache(Constant.SessionIdEventDistance);
		Map<String, EventDistanceDTO> eventDistanceMap = Maps.newHashMap();
		Element element = new Element(sessionId, eventDistanceMap);
		sessionIdEventDistanceCache.put(element);

		try {

			StringBuilder hql = new StringBuilder();
			hql.append(
					"select s.id as id, s.fgps as fgps from TSponsor s where s.fstatus = 1 and s.fgps is not null and s.fgps != ''");
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

	@Transactional(readOnly = true)
	public ResponseDTO appShareGoods(String goodsId, HttpServletRequest request) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (StringUtils.isBlank(goodsId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("goodsId参数不能为空，请检查goodsId的传递参数值！");
			return responseDTO;
		}
		TEvent tEvent = eventDAO.findOne(goodsId);
		if (tEvent == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("goodsId参数信息有误，系统中没有商品ID为“" + goodsId + "”的活动！");
			return responseDTO;
		}

		AppShareDTO appShareDTO = new AppShareDTO();
		appShareDTO.setTitle(tEvent.getFtitle());

		if (StringUtils.isNotBlank(tEvent.getFimage1())) {
			appShareDTO.setImageUrl(fxlService.getImageUrl(tEvent.getFimage1(), true));
		} else {
			appShareDTO.setImageUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
					.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPicThumbnail.jpg").toString());
		}
		if (StringUtils.isNotBlank(tEvent.getFbrief())) {
			appShareDTO.setBrief(tEvent.getFbrief());
		} else {
			appShareDTO.setBrief(PropertiesUtil.getProperty("appShareDefaultInfo"));
		}
		// appShareDTO.setUrl(new
		// StringBuilder().append(request.getScheme()).append("://").append(request.getServerName())
		// .append(":").append(request.getServerPort()).append(request.getContextPath())
		// .append("/api/system/share/event/").append(goodsId).toString());
		StringBuilder sb = new StringBuilder();
		sb.append(PropertiesUtil.getProperty("H5Url")).append("/#/event-basic?id=").append(goodsId);
		appShareDTO.setUrl(sb.toString());

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("appShare", appShareDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("商品分享成功！");
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO cityCascaded(Integer cityId, HttpServletRequest request) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (cityId == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("cityId参数不能为空，请检查cityId的传递参数值！");
			return responseDTO;
		}
		CityListDTO cityListDTO = new CityListDTO();

		TDictionaryCity dictionaryCity = dictionaryCityDao.findOneCity(cityId);
		List<TDictionaryRegion> dictionaryRegionList = dictionaryRegionDao.findOneCityRegion(cityId);
		cityListDTO.setCityId(cityId);
		cityListDTO.setCityName(dictionaryCity.getName());
		cityListDTO.setRegionList(dictionaryRegionList);

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("regionList", cityListDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("城市级联加载成功！");
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO systemDictionary(Long dictionaryId, Integer dictionaryValue, HttpServletRequest request) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (dictionaryId == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("dictionaryId参数不能为空，请检查dictionaryId的传递参数值！");
			return responseDTO;
		}
		List<DictionaryDTO> dicList = null;
		DictionaryDTO dictionaryDTO = null;
		if (dictionaryValue == null) {
			dicList = Lists.newArrayList();

			List<TDictionary> tDictionary = dictionaryDao.findOrderByClassId(dictionaryId);
			for (TDictionary tDictionary2 : tDictionary) {
				dictionaryDTO = new DictionaryDTO();
				dictionaryDTO.setSystemModel("系统字典");
				dictionaryDTO.setSystemName(tDictionary2.getName());
				dictionaryDTO.setSystemImageUrl(tDictionary2.getCode());
				dictionaryDTO.setSystemId(tDictionary2.getValue());
				dicList.add(dictionaryDTO);
			}
		} else {
			dicList = Lists.newArrayList();

			TDictionary tDictionary = dictionaryDao.findNameAndValue(dictionaryValue, dictionaryId);

			dictionaryDTO = new DictionaryDTO();
			dictionaryDTO.setSystemModel("系统字典");
			dictionaryDTO.setSystemName(tDictionary.getName());
			dictionaryDTO.setSystemImageUrl(tDictionary.getCode());
			dictionaryDTO.setSystemId(tDictionary.getValue());
			dicList.add(dictionaryDTO);
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("systemDicList", dicList);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("系统字典信息加载成功！");
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getSysAddress(HttpServletRequest request) {
		ResponseDTO responseDTO = new ResponseDTO();

		List<TSysProvince> pList = sysProvinceDAO.getAllProvince();
		List<TSysCity> cList = sysCityDAO.getAllCity();
		List<TSysRegion> rList = sysRegionDAO.getAllRegion();

		List<RegionDTO> rAllList = null; // 全部的regDto
		List<CityDTO> cAllList = null; // 全部的cityDto
		List<ProvinceDTO> pAllList = null;

		RegionDTO regDTO = null;
		CityDTO cityDto = null;
		ProvinceDTO proDto = null;

		if (null != rList && rList.size() > 0) {
			rAllList = new ArrayList<RegionDTO>();
			for (TSysRegion tSysRegion : rList) {
				regDTO = new RegionDTO();
				regDTO.setRegionId(tSysRegion.getRegionId());
				regDTO.setRegionName(tSysRegion.getRegionName());
				rAllList.add(regDTO);
			}
		}

		if (null != cList && cList.size() > 0) {

			cAllList = new ArrayList<CityDTO>();
			for (TSysCity c : cList) {
				cityDto = new CityDTO();
				cityDto.setCityId(c.getCityId());
				cityDto.setCityName(c.getCityName());
				cityDto.setIsHot(c.getIsHot());

				// 筛选区
				List<RegionDTO> regList = new ArrayList<RegionDTO>();
				for (RegionDTO r : rAllList) {
					if (r.getRegionId() / 100 == c.getCityId() / 100
							|| (c.getCityId() % 10000 == 0 && r.getRegionId() / 10000 * 10000 == c.getCityId())) {
						regList.add(r);
					}
				}
				cityDto.setRegionList(regList);
				cAllList.add(cityDto);
			}
		}

		// 循环省份
		if (null != pList && pList.size() > 0) {
			pAllList = new ArrayList<ProvinceDTO>();
			// 循环省
			for (TSysProvince p : pList) {
				proDto = new ProvinceDTO();
				proDto.setProId(p.getProId());
				proDto.setProName(p.getProName());

				// 筛选市
				List<CityDTO> cityList = new ArrayList<CityDTO>();
				for (CityDTO c : cAllList) {
					if (c.getCityId() / 10000 == p.getProId() / 10000) {
						cityList.add(c);
					}
				}
				proDto.setCityList(cityList);
				pAllList.add(proDto);
			}
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("systemAddressList", pAllList);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("获取市区级联成功！");
		return responseDTO;
	}

	public ResponseDTO favoriteGoods(Integer clientType, String ticket, String goodsId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(goodsId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("goodsId参数不能为空，请检查goodsId的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}
		boolean flag = false;
		TFavorite tFavorite = favoriteDAO.getFavoriteEventByCustomerIdAndObejctId(customerDTO.getCustomerId(), goodsId);
		if (tFavorite == null) {
			flag = true;
			tFavorite = new TFavorite();
			tFavorite.setTCustomer(new TCustomer(customerDTO.getCustomerId()));
			tFavorite.setFobjectId(goodsId);
			tFavorite.setFtype(1);
			favoriteDAO.save(tFavorite);
		} else {
			flag = false;
			favoriteDAO.delete(tFavorite);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("favorite", flag);
		responseDTO.setData(returnData);
		if (flag) {
			responseDTO.setMsg("商品收藏成功！");
		} else {
			responseDTO.setMsg("商品取消收藏成功！");
		}
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getFavoriteGoodsList(Integer clientType, String ticket, Integer pageSize, Integer offset) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		List<EventSimpleDTO> list = Lists.newArrayList();
		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}
		StringBuilder hql = new StringBuilder();
		hql.append(
				"select t.id as id, t.fimage1 as fimage1, t.ftitle as ftitle, t.fsubTitle as fsubTitle,  t.fprice as fprice, t.fstatus as fstatus from TEvent t, TFavorite f where t.id = f.fobjectId and f.TCustomer.id = :customerId and f.ftype = 1 and t.fstatus < 999 order by t.fstatus");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("customerId", customerDTO.getCustomerId());

		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> favoriteList = page.getResult();

		EventSimpleDTO eventSimpleDTO = null;
		for (Map<String, Object> amap : favoriteList) {
			eventSimpleDTO = new EventSimpleDTO();
			if (amap.get("id") != null && StringUtils.isNotBlank(amap.get("id").toString())) {
				eventSimpleDTO.setGoodsId(amap.get("id").toString());
				TEvent tEvent = eventDAO.getOne(amap.get("id").toString());
				Set<TEventSession> set = tEvent.getTEventSessions();
				TEventSession tEventSession = null;
				if (set.size() > 0) {
					tEventSession = tEvent.getTEventSessions().iterator().next();
					eventSimpleDTO.setGoodsDate(DateFormatUtils.format(tEventSession.getFstartDate(), "yyyy-MM"));
					eventSimpleDTO.setGoodsDateValue(tEventSession.getFstartDate().getTime());
					eventSimpleDTO.setGoodsAddress(tEventSession.getFaddress());
				}
			}
			if (amap.get("ftitle") != null && StringUtils.isNotBlank(amap.get("ftitle").toString())) {
				eventSimpleDTO.setTitle(amap.get("ftitle").toString());
			}
			if (amap.get("fsubTitle") != null && StringUtils.isNotBlank(amap.get("fsubTitle").toString())) {
				eventSimpleDTO.setSubTitle(amap.get("fsubTitle").toString());
			}
			if (amap.get("fimage1") != null && StringUtils.isNotBlank(amap.get("fimage1").toString())) {
				eventSimpleDTO.setImageUrl(fxlService.getImageUrl(amap.get("fimage1").toString(), false));
			} else {
				eventSimpleDTO.setImageUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
						.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPic.jpg").toString());
			}
			if (amap.get("feventTime") != null && StringUtils.isNotBlank(amap.get("feventTime").toString())) {
				eventSimpleDTO.setGoodsTime(amap.get("feventTime").toString());
			}
			if (amap.get("fprice") != null && StringUtils.isNotBlank(amap.get("fprice").toString())) {
				eventSimpleDTO.setPrice(amap.get("fprice").toString());
			}
			if (amap.get("fdeal") != null && StringUtils.isNotBlank(amap.get("fdeal").toString())) {
				eventSimpleDTO.setDeal(amap.get("fdeal").toString());
			} else {
				eventSimpleDTO.setDeal("免费");
			}
			if (amap.get("fstatus") != null && StringUtils.isNotBlank(amap.get("fstatus").toString())) {
				eventSimpleDTO.setStatus((Integer) amap.get("fstatus"));
			}
			list.add(eventSimpleDTO);
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("favoriteEventList", list);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("用户已收藏商品列表加载成功！");
		return responseDTO;
	}

	public ResponseDTO favoriteMerchant(Integer clientType, String ticket, String merchantId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(merchantId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("merchantId参数不能为空，请检查merchantId的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}
		boolean flag = false;
		TFavorite tFavorite = favoriteDAO.getFavoriteMerchantByCustomerIdAndObejctId(customerDTO.getCustomerId(),
				merchantId);
		if (tFavorite == null) {
			flag = true;
			tFavorite = new TFavorite();
			tFavorite.setTCustomer(new TCustomer(customerDTO.getCustomerId()));
			tFavorite.setFobjectId(merchantId);
			tFavorite.setFtype(2);
			favoriteDAO.save(tFavorite);
		} else {
			flag = false;
			favoriteDAO.delete(tFavorite);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("favorite", flag);
		responseDTO.setData(returnData);
		if (flag) {
			responseDTO.setMsg("商家收藏成功！");
		} else {
			responseDTO.setMsg("商家取消收藏成功！");
		}
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getFavoriteMerchantList(Integer clientType, String ticket, Integer pageSize, Integer offset) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		List<MerchantSimpleDTO> list = Lists.newArrayList();

		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}

		StringBuilder hql = new StringBuilder();
		hql.append(
				"select t.id as id, t.fname as fname, t.fimage as fimage, t.faddress as faddress, t.fscore as fscore from TSponsor t, TFavorite f where t.id = f.fobjectId and f.TCustomer.id = :customerId and f.ftype = 2");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("customerId", customerDTO.getCustomerId());

		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> favoriteList = page.getResult();
		MerchantSimpleDTO merchantSimpleDTO = null;
		for (Map<String, Object> amap : favoriteList) {
			merchantSimpleDTO = new MerchantSimpleDTO();
			if (amap.get("id") != null && StringUtils.isNotBlank(amap.get("id").toString())) {
				merchantSimpleDTO.setMerchantId(amap.get("id").toString());
				merchantSimpleDTO.setEventCount(eventDAO.getEventCountBySponsorId(amap.get("id").toString()));
			}
			if (amap.get("fname") != null && StringUtils.isNotBlank(amap.get("fname").toString())) {
				merchantSimpleDTO.setMerchantName(amap.get("fname").toString());
			}
			if (amap.get("fimage") != null && StringUtils.isNotBlank(amap.get("fimage").toString())) {
				merchantSimpleDTO.setMerchantLogoUrl(fxlService.getImageUrl(amap.get("fimage").toString(), true));
			} else {
				merchantSimpleDTO.setMerchantLogoUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
						.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPicThumbnail.jpg")
						.toString());
			}
			if (amap.get("faddress") != null && StringUtils.isNotBlank(amap.get("faddress").toString())) {
				merchantSimpleDTO.setAddress(amap.get("faddress").toString());
			}
			if (amap.get("fscore") != null && StringUtils.isNotBlank(amap.get("fscore").toString())) {
				merchantSimpleDTO.setScore((BigDecimal) amap.get("fscore"));
			}

			list.add(merchantSimpleDTO);
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("favoriteMerchantList", list);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("获取用户已收藏商家列表成功！");
		return responseDTO;
	}

	public ResponseDTO favoriteArticle(Integer clientType, String ticket, String articleId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(articleId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("articleId参数不能为空，请检查articleId的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}
		boolean flag = false;
		TFavorite tFavorite = favoriteDAO.getFavoriteArticleIdByCustomerIdAndObejctId(customerDTO.getCustomerId(),
				articleId);
		if (tFavorite == null) {
			flag = true;
			tFavorite = new TFavorite();
			tFavorite.setTCustomer(new TCustomer(customerDTO.getCustomerId()));
			tFavorite.setFobjectId(articleId);
			tFavorite.setFtype(3);
			favoriteDAO.save(tFavorite);
		} else {
			flag = false;
			favoriteDAO.delete(tFavorite);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("favorite", flag);
		responseDTO.setData(returnData);
		if (flag) {
			responseDTO.setMsg("文章收藏成功！");
		} else {
			responseDTO.setMsg("文章取消收藏成功！");
		}
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getFavoriteArticletList(Integer clientType, String ticket, Integer pageSize, Integer offset) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		List<ArticleSimpleDTO> list = Lists.newArrayList();

		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}

		StringBuilder hql = new StringBuilder();
		hql.append(
				"select t.id as id, t.ftitle as ftitle, t.ftype as ftype, t.fimage as fimage, t.fbrief as fbrief, t.fdetail as fdetail, t.fdetailHtmlUrl as fdetailHtmlUrl,t.fstatus as fstatus,t.fartType as fartType,t.fartCity as fartCity from TArticle t, TFavorite f where t.id = f.fobjectId and f.TCustomer.id = :customerId and f.ftype = 3");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("customerId", customerDTO.getCustomerId());

		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> favoriteList = page.getResult();
		ArticleSimpleDTO articleSimpleDTO = null;
		for (Map<String, Object> amap : favoriteList) {
			articleSimpleDTO = new ArticleSimpleDTO();
			if (amap.get("id") != null && StringUtils.isNotBlank(amap.get("id").toString())) {
				articleSimpleDTO.setArticleId(amap.get("id").toString());
			}
			if (amap.get("ftitle") != null && StringUtils.isNotBlank(amap.get("ftitle").toString())) {
				articleSimpleDTO.setTitle(amap.get("ftitle").toString());
			}
			if (amap.get("fimage") != null && StringUtils.isNotBlank(amap.get("fimage").toString())) {
				articleSimpleDTO.setImageUrl(fxlService.getImageUrl(amap.get("fimage").toString(), true));
			}
			if (amap.get("fdetailHtmlUrl") != null && StringUtils.isNotBlank(amap.get("fdetailHtmlUrl").toString())) {
				articleSimpleDTO.setDetailHtmlUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
						.append(PropertiesUtil.getProperty("htmlRootPath"))
						.append(amap.get("fdetailHtmlUrl").toString()).toString());
			}
			if (amap.get("fbrief") != null && StringUtils.isNotBlank(amap.get("fbrief").toString())) {
				articleSimpleDTO.setBrief(amap.get("fbrief").toString());
			}
			if (StringUtils.isNotBlank(ticket)) {
				customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
				if (customerDTO != null && customerDTO.isEnable()) {
					TFavorite tFavorite = favoriteDAO.getFavoriteArticleIdByCustomerIdAndObejctId(
							customerDTO.getCustomerId(), amap.get("id").toString());
					if (tFavorite != null) {
						articleSimpleDTO.setFavorite(true);
					} else {
						articleSimpleDTO.setFavorite(false);
					}

				} else {
					articleSimpleDTO.setFavorite(false);
				}
			} else {
				articleSimpleDTO.setFavorite(false);
			}
			list.add(articleSimpleDTO);
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("favoriteArticleList", list);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("用户已收藏文章列表加载成功！");
		return responseDTO;
	}

	/**
	 * 按照 商品 文章 店铺 查询
	 * 
	 * @param valueMap
	 * @param page
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO searchList(Integer cityId, String key, Integer serchType, Integer orderBy, Integer pageSize,
			Integer offset, String sessionId) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (cityId == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
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

		StringBuilder hql = null;

		if (serchType.intValue() == 1) {
			// 搜索商品
			hql = new StringBuilder();
			Map<String, Object> hqlMap = Maps.newHashMap();
			if (orderBy.intValue() == 1) {
				hql.append(
						"select t.id as id, t.ftitle as ftitle, s.fname as ffullName, t.ftypeA as ftypeA ,t.fbrief as fbrief, t.fimage1 as fimage1, t.fprice as fprice, t.fpriceMoney as fpriceMoney, t.fstatus as fstatus,t.foffSaleTime as foffSaleTime ")
						.append(",t.fspec as fspec,t.fsellModel as fsellModel, t.fspecModel as fSpecModel, t.fpromotionModel as fpromotionModel,t.flimitation as fLimitation,t.fstock as fstock, dt.fdistance as fdistance from TEvent t left join t.TSponsor s left join TEventDistanceTemp as dt on s.id = dt.feventId");
			} else {
				hql.append(
						"select t.id as id, t.ftitle as ftitle, t.TSponsor.fname as ffullName, t.ftypeA as ftypeA ,t.fbrief as fbrief, t.fimage1 as fimage1, t.fprice as fprice, t.fpriceMoney as fpriceMoney, t.fstatus as fstatus,t.foffSaleTime as foffSaleTime")
						.append(",t.fspec as fspec,t.fsellModel as fsellModel, t.fspecModel as fSpecModel, t.fpromotionModel as fpromotionModel,t.flimitation as fLimitation,t.fstock as fstock from TEvent t ");
			}
			hql.append(" where t.fstatus = 20");
			hql.append(" and t.fsdealsModel = 0");

			if (StringUtils.isNotBlank(key)) {
				hql.append(" and (t.ftitle like :key )");
				hqlMap.put("key", "%" + key + "%");
			}
			if (orderBy.intValue() == 0) {
				hql.append(" order by t.fsaleTotal desc");
			} else if (orderBy.intValue() == 1) {
				hql.append(" order by dt.fdistance desc");
			} else {
				hql.append(" order by t.fupdateTime desc");
			}

			commonService.findPage(hql.toString(), page, hqlMap);
			List<Map<String, Object>> list = page.getResult();

			// 获取活动距离信息缓存对象
			Cache sessionIdEventDistanceCache = cacheManager.getCache(Constant.SessionIdEventDistance);
			Element ele = null;
			String type = null;

			// 对活动信息进行细化加载
			List<GoodsDTO> resultList = Lists.newArrayList();
			GoodsDTO goodsDTO = null;
			int statusValue = 0;
			BigDecimal distance = null;
			for (Map<String, Object> amap : list) {
				goodsDTO = new GoodsDTO();
				goodsDTO.setGoodsId(amap.get("id").toString());
				goodsDTO.setGoodsTitle(amap.get("ftitle").toString());
				if (amap.get("ftypeA") != null && StringUtils.isNotBlank(amap.get("ftypeA").toString())) {
					type = categoryService.getCategoryA((Integer) amap.get("ftypeA"));
					goodsDTO.setType(type != null ? type : StringUtils.EMPTY);
				}
				// if (amap.get("foffSaleTime") != null &&
				// StringUtils.isNotBlank(amap.get("foffSaleTime").toString()))
				// {
				// Integer second = Seconds
				// .secondsBetween(new DateTime(new Date()), new
				// DateTime(amap.get("foffSaleTime")))
				// .getSeconds();
				// goodsDTO.setGoodsTime(second.toString());
				// }
				if (amap.get("fimage1") != null && StringUtils.isNotBlank(amap.get("fimage1").toString())) {
					goodsDTO.setImageUrl(fxlService.getImageUrl(amap.get("fimage1").toString(), false));
				} else {
					goodsDTO.setImageUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
							.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPic.jpg").toString());
				}
				if (amap.get("fprice") != null && StringUtils.isNotBlank(amap.get("fprice").toString())) {
					goodsDTO.setOriginalPrice(amap.get("fprice").toString());
				}
				if (amap.get("fbrief") != null && StringUtils.isNotBlank(amap.get("fbrief").toString())) {
					if (amap.get("fbrief").toString().length() >= 15) {
						goodsDTO.setDesc(amap.get("fbrief").toString().substring(0, 15));
					} else {
						goodsDTO.setDesc(amap.get("fbrief").toString());
					}
				}
				if (amap.get("ffullName") != null && StringUtils.isNotBlank(amap.get("ffullName").toString())) {
					goodsDTO.setSponsorName(amap.get("ffullName").toString());
				}
				if (amap.get("fspec") != null && StringUtils.isNotBlank(amap.get("fspec").toString())) {
					goodsDTO.setSpec(amap.get("fspec").toString());
				}
				if (amap.get("fpriceMoney") != null && StringUtils.isNotBlank(amap.get("fpriceMoney").toString())) {
					goodsDTO.setPresentPrice(amap.get("fpriceMoney").toString());
				} else {
					goodsDTO.setPresentPrice("0");
				}
				if (amap.get("fstatus") != null && StringUtils.isNotBlank(amap.get("fstatus").toString())) {
					statusValue = ((Integer) amap.get("fstatus")).intValue();
					goodsDTO.setStatus(statusValue);
					goodsDTO.setStatusString(DictionaryUtil.getString(DictionaryUtil.EventStatus, statusValue));
				}
				if (amap.get("fLimitation") != null && StringUtils.isNotBlank(amap.get("fLimitation").toString())) {
					if (((Integer) amap.get("fLimitation")).intValue() > 0) {
						goodsDTO.setStockFlag(1);
					} else {
						goodsDTO.setStockFlag(0);
					}
				} else {
					goodsDTO.setStockFlag(0);
				}
				if (amap.get("fsellModel") != null && StringUtils.isNotBlank(amap.get("fsellModel").toString())) {
					goodsDTO.setSellModel(((Integer) amap.get("fsellModel")).intValue());
				}
				if (amap.get("fSpecModel") != null && StringUtils.isNotBlank(amap.get("fSpecModel").toString())) {
					goodsDTO.setSpecModel(((Integer) amap.get("fSpecModel")).intValue());
				}
				if (amap.get("fPromotionModel") != null
						&& StringUtils.isNotBlank(amap.get("fPromotionModel").toString())) {
					goodsDTO.setPromotionModel(((Integer) amap.get("fPromotionModel")).intValue());
				}
				if (amap.get("fstock") != null && StringUtils.isNotBlank(amap.get("fstock").toString())) {
					if (((Integer) amap.get("fstock")).intValue() <= 0) {
						goodsDTO.setIfInStock(false);
					}
				}

				if (amap.get("fdistance") != null && StringUtils.isNotBlank(amap.get("fdistance").toString())) {
					distance = new BigDecimal((Integer) amap.get("fdistance")).negate().divide(new BigDecimal(1000), 1,
							RoundingMode.HALF_UP);
					goodsDTO.setDistance(distance.toString() + "km");
				}
				resultList.add(goodsDTO);
			}

			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			Map<String, Object> returnData = Maps.newHashMap();
			returnData.put("eventList", resultList);
			PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
			returnData.put("page", pageDTO);
			responseDTO.setData(returnData);
			if (page.getTotalCount() == 0L) {
				responseDTO.setMsg("没有检索到您查找的商品！");
			}
		} else if (serchType.intValue() == 2) {
			// 搜索文章
			hql = new StringBuilder();
			Map<String, Object> hqlMap = new HashMap<String, Object>();
			hql.append(
					"select t.id as id, t.ftitle as ftitle, t.ftype as ftype, t.fimage as fimage, t.fbrief as fbrief, t.fdetailHtmlUrl as fdetailHtmlUrl, t.frecommend as frecommend, t.fcomment as fcomment, t.fcreateTime as fcreateTime from TArticle t where t.fcityId in (0 , :cityId) and t.fstatus = 20");
			hqlMap.put("cityId", cityId);

			if (StringUtils.isNotBlank(key)) {
				hql.append(" and (t.ftitle like :key )");
				hqlMap.put("key", "%" + key + "%");
			}
			hql.append(" order by t.forder desc");
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
				if (amap.get("fdetailHtmlUrl") != null
						&& StringUtils.isNotBlank(amap.get("fdetailHtmlUrl").toString())) {
					articleDTO.setDetailHtmlUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
							.append(PropertiesUtil.getProperty("htmlRootPath"))
							.append(amap.get("fdetailHtmlUrl").toString()).toString());
				}
				ele = articleRecommendCache.get(articleDTO.getArticleId());
				if (ele != null) {
					ArticleRecommendDTO articleRecommendDTO = (ArticleRecommendDTO) ele.getObjectValue();
					articleDTO.setRecommend(articleRecommendDTO.getRecommend());
				} else if (amap.get("frecommend") != null
						&& StringUtils.isNotBlank(amap.get("frecommend").toString())) {
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
			if (page.getTotalCount() == 0L) {
				responseDTO.setMsg("没有检索到您查找的文章！");
			}

		} else if (serchType.intValue() == 3) {
			// 搜索商户
			hql = new StringBuilder();
			List<SponsorDTO> sponsorList = Lists.newArrayList();

			Map<String, Object> hqlMap = Maps.newHashMap();
			if (orderBy.intValue() == 1) {
				hql.append(
						"select t.id as id ,t.fname as fname,t.fimage as fimage,t.fscore as fscore,t.fsponsorTag as fsponsorTag ")
						.append(",t.fperPrice as fperPrice,dt.fdistance as fdistance,t.ftermValidity as ftermValidity,t.fexceptionDate as fexceptionDate ")
						.append(",t.fuseDate as fuseDate,t.freminder as freminder,t.fregion as fregion")
						.append(" from TSponsor t left join TEventDistanceTemp as dt on t.id = dt.feventId and dt.fhsid = :sessionId where t.fstatus<999");
				hqlMap.put("sessionId", sessionId);
			} else {
				hql.append(
						"select t.id as id ,t.fname as fname,t.fimage as fimage,t.fscore as fscore,t.fsponsorTag as fsponsorTag ")
						.append(",t.fperPrice as fperPrice,t.ftermValidity as ftermValidity,t.fexceptionDate as fexceptionDate ")
						.append(",t.fuseDate as fuseDate,t.freminder as freminder,t.fregion as fregion")
						.append(" from TSponsor t where t.fstatus<999");
			}

			if (StringUtils.isNotBlank(key)) {
				hql.append(" and (t.fname like :key )");
				hqlMap.put("key", "%" + key + "%");
			}
			if (orderBy.intValue() == 1) {
				hql.append(" order by dt.fdistance");
			} else {
				hql.append(" order by t.fcreateTime");
			}
			commonService.findPage(hql.toString(), page, hqlMap);
			List<Map<String, Object>> list = page.getResult();

			SponsorDTO sponsorDTO = null;
			int statusValue = 0;
			for (Map<String, Object> amap : list) {
				sponsorDTO = new SponsorDTO();
				if (amap.get("id") != null && StringUtils.isNotBlank(amap.get("id").toString())) {
					sponsorDTO.setSponsorId(amap.get("id").toString());
				}
				if (amap.get("fsponsorTag") != null && StringUtils.isNotBlank(amap.get("fsponsorTag").toString())) {
					statusValue = (Integer.parseInt(amap.get("fsponsorTag").toString()));
					sponsorDTO.setTag(DictionaryUtil.getString(DictionaryUtil.SponsorTag, statusValue));
				}

				if (amap.get("fperPrice") != null && StringUtils.isNotBlank(amap.get("fperPrice").toString())) {
					sponsorDTO.setPerPrice(amap.get("fperPrice").toString());
				}
				if (amap.get("fscore") != null && StringUtils.isNotBlank(amap.get("fscore").toString())) {
					sponsorDTO.setScore(amap.get("fscore").toString());
				}

				if (amap.get("fimage") != null && StringUtils.isNotBlank(amap.get("fimage").toString())) {
					sponsorDTO.setSponsorImage(fxlService.getImageUrl(amap.get("fimage").toString(), false));
				}
				if (amap.get("fname") != null && StringUtils.isNotBlank(amap.get("fname").toString())) {
					sponsorDTO.setSponsorName(amap.get("fname").toString());
				}
				if (amap.get("fregion") != null && StringUtils.isNotBlank(amap.get("fregion").toString())) {
					sponsorDTO.setDistrict(amap.get("fregion").toString());
				}

				sponsorList.add(sponsorDTO);
			}

			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			Map<String, Object> returnData = Maps.newHashMap();
			returnData.put("sponsorList", sponsorList);
			PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
			returnData.put("page", pageDTO);
			responseDTO.setData(returnData);
			if (page.getTotalCount() == 0L) {
				responseDTO.setMsg("没有检索到您查找的店铺！");
			}
		}

		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO sysColumns(Integer tag, String channelId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (tag == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("tag参数不能为空，请检查tag的传递参数值！");
			return responseDTO;
		}
		OtherDTO otherDTO = null;
		if (StringUtils.isNotBlank(channelId)) {
			otherDTO = new OtherDTO();
			TColumnBanner tSeckilltime = columnBannerDAO.findChannelId(channelId);

			if (tSeckilltime.getFseckillTime() != null) {
				Integer second = Seconds
						.secondsBetween(new DateTime(new Date()), new DateTime(tSeckilltime.getFseckillTime()))
						.getSeconds();
				otherDTO.setGoodsTime(second.toString());
			}
			otherDTO.setImageUrl(fxlService.getImageUrl(tSeckilltime.getFimageUrl(), false));
			otherDTO.setChannelId(tSeckilltime.getFchannelId());
			otherDTO.setTagId(tag);

		} else {
			otherDTO = new OtherDTO();
			TColumnBanner tSeckilltime = columnBannerDAO.findColumnBanner(tag, 1);

			if (tSeckilltime.getFseckillTime() != null) {
				Integer second = Seconds
						.secondsBetween(new DateTime(new Date()), new DateTime(tSeckilltime.getFseckillTime()))
						.getSeconds();
				otherDTO.setGoodsTime(second.toString());
			}
			otherDTO.setImageUrl(fxlService.getImageUrl(tSeckilltime.getFimageUrl(), false));
			otherDTO.setChannelId(tSeckilltime.getFchannelId());
			otherDTO.setTagId(tag);
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("sysColumns", otherDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("加载栏目banner广告图成功！");

		return responseDTO;
	}

	public ResponseDTO deleteFavoriteGoods(Integer clientType, String ticket, String ids, Integer type) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(ids)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("ids参数不能为空，请检查ids的传递参数值！");
			return responseDTO;
		}
		if (type == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("type参数不能为空，请检查type的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}
		JavaType jt = mapper.contructCollectionType(ArrayList.class, String.class);
		List<String> goodslist = new ArrayList<>();
		goodslist = mapper.fromJson(ids, jt);
		List<TFavorite> tFavorite = favoriteDAO.findByIds(customerDTO.getCustomerId(), goodslist, type);
		if (tFavorite != null && tFavorite.size() > 0) {
			favoriteDAO.delete(tFavorite);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("favorite", true);
		responseDTO.setData(returnData);
		responseDTO.setMsg("批量取消收藏成功！");
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO tagType(Integer type, HttpServletRequest request) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (type == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("type参数不能为空，请检查type的传递参数值！");
			return responseDTO;
		}
		List<TagDTO> tagList = Lists.newArrayList();
		TagDTO tagDTO = null;
		List<TTag> TTags = tTagDAO.findByType(type);
		for (TTag tag : TTags) {
			tagDTO = new TagDTO();
			tagDTO.setTagId(tag.getFtag());
			tagDTO.setStagImageUrl(tag.getFimageUrl());
			tagDTO.setTagName(tag.getFdes());
			tagList.add(tagDTO);
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("tagType", tagList);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("惠说分类信息加载成功！");
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getAppVersion(Integer clientType, Integer type) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("clientType参数不能为空，请检查clientType的传递参数值！");
			return responseDTO;
		}
		Map<Integer, AppVersionDTO> avMap = Maps.newHashMap();
		if (type != null && type == 2) {
			avMap = Constant.getSponsorVersionMap();
		} else {
			avMap = Constant.getAppVersionMap();
		}
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
		responseDTO.setMsg("应用版本信息加载成功！");
		return responseDTO;
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
			returnData.put("imageUrl", fxlService.getImageUrl(tAppFlash.getFimage(), false));
			returnData.put("urlType", tAppFlash.getFurlType());
			if (tAppFlash.getFurlType().intValue() == 5) {
				returnData.put("urlType", tAppFlash.getFurlType());
				returnData.put("linkUrl", StringUtils.EMPTY);
				returnData.put("entityId", tAppFlash.getFentityId());
			} else if (tAppFlash.getFurlType().intValue() == 1 || tAppFlash.getFurlType().intValue() == 4) {
				returnData.put("linkUrl", tAppFlash.getFexternalUrl());
				returnData.put("entityId", StringUtils.EMPTY);
			} else {
				returnData.put("linkUrl", StringUtils.EMPTY);
				returnData.put("entityId", StringUtils.EMPTY);
			}
		} else {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(110);
			responseDTO.setData(returnData);
			responseDTO.setMsg("当前没有可用的启动广告位");
			return responseDTO;
		}
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO appShareMerchant(String merchantId, HttpServletRequest request) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (StringUtils.isBlank(merchantId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("goodsId参数不能为空，请检查goodsId的传递参数值！");
			return responseDTO;
		}
		// TArticle tArticle = articleDAO.findOne(spec);
		TSponsor tSponsor = sponsorDAO.findOne(merchantId);
		if (tSponsor == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("merchantId参数信息有误，系统中没有商品ID为“" + merchantId + "”的活动！");
			return responseDTO;
		}

		AppShareDTO appShareDTO = new AppShareDTO();
		appShareDTO.setTitle(tSponsor.getFname());

		if (StringUtils.isNotBlank(tSponsor.getFimage())) {
			appShareDTO.setImageUrl(fxlService.getImageUrl(tSponsor.getFimage(), true));
		} else {
			appShareDTO.setImageUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
					.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPicThumbnail.jpg").toString());
		}
		if (StringUtils.isNotBlank(tSponsor.getFbrief())) {
			appShareDTO.setBrief(tSponsor.getFbrief());
		} else {
			appShareDTO.setBrief(PropertiesUtil.getProperty("appShareDefaultInfo"));
		}
		// appShareDTO.setUrl(new
		// StringBuilder().append(request.getScheme()).append("://").append(request.getServerName())
		// .append(":").append(request.getServerPort()).append(request.getContextPath())
		// .append("/api/system/share/merchant/").append(merchantId).toString());

		StringBuilder sb = new StringBuilder();
		sb.append(PropertiesUtil.getProperty("H5Url")).append("/#/merchant?id=").append(merchantId);
		appShareDTO.setUrl(sb.toString());

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("appShare", appShareDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("商户分享成功！");
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO appShareArticle(String articleId, HttpServletRequest request) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (StringUtils.isBlank(articleId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("articleId参数不能为空，请检查articleId的传递参数值！");
			return responseDTO;
		}
		TArticle tArticle = articleDAO.findOne(articleId);
		if (tArticle == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("articleId参数信息有误，系统中没有文章ID为“" + articleId + "”的文章！");
			return responseDTO;
		}

		AppShareDTO appShareDTO = new AppShareDTO();
		appShareDTO.setTitle(tArticle.getFtitle());

		if (tArticle.getFimage() != null) {
			appShareDTO.setImageUrl(fxlService.getImageUrl(tArticle.getFimage().toString(), true));
		} else {
			appShareDTO.setImageUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
					.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPicThumbnail.jpg").toString());
		}
		if (StringUtils.isNotBlank(tArticle.getFbrief())) {
			appShareDTO.setBrief(tArticle.getFbrief());
		} else {
			appShareDTO.setBrief(PropertiesUtil.getProperty("appShareDefaultInfo"));
		}
		// appShareDTO.setUrl(new
		// StringBuilder().append(request.getScheme()).append("://").append(request.getServerName())
		// .append(":").append(request.getServerPort()).append(request.getContextPath())
		// .append("/api/system/share/artical/").append(articleId).toString());

		StringBuilder sb = new StringBuilder();
		sb.append(PropertiesUtil.getProperty("H5Url")).append("/#/article-detail?id=").append(articleId);
		appShareDTO.setUrl(sb.toString());

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("appShare", appShareDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("文章分享成功！");
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getTimestamp(Integer clientType) {
		ResponseDTO responseDTO = new ResponseDTO();

		Map<String, Object> returnData = Maps.newHashMap();
		List<TSubobjectTimeStamp> timeStamps = subobjectTimeStampDAO.findAll();
		List<Map<String, Object>> list = Lists.newArrayList();
		Map<String, Object> timeStampsMap = Maps.newHashMap();
		for (TSubobjectTimeStamp timeStamp : timeStamps) {
			timeStampsMap = new HashMap<String, Object>();
			timeStampsMap.put("type", timeStamp.getFsubObject());
			timeStampsMap.put("timeStamp", timeStamp.getFsubUpdateTime().getTime());
			list.add(timeStampsMap);
		}
		returnData.put("timeStampsMap", list);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setData(returnData);
		responseDTO.setMsg("时间戳加载成功！");
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getGoodsSpecValue(String goodsId) {

		ResponseDTO responseDTO = new ResponseDTO();

		Map<String, Object> returnData = Maps.newHashMap();

		List<TGoodsSpaceValue> goodsSpaceValueList = goodsSpaceValueDAO.findGoodsList(goodsId);

		StringBuilder httpUrl = null;
		List<String[]> list = Lists.newArrayList();
		String[][] bigarr = null;
		String[] arr = null;

		if (goodsSpaceValueList.size() > 0) {

			for (TGoodsSpaceValue tGoodsSpaceValue : goodsSpaceValueList) {
				String fspaceName = tGoodsSpaceValue.getFspaceName();
				String fvalueName = tGoodsSpaceValue.getFvalueName();
				arr = new String[2];
				arr[0] = fspaceName;
				arr[1] = fvalueName;
				list.add(arr);
			}
			bigarr = new String[goodsSpaceValueList.size()][];
			for (int i = 0; i < bigarr.length; i++) {
				bigarr[i] = list.get(i);
			}

			// 调用图片
			DrawTableImg drawTableImg = new DrawTableImg();

			// 分配图片上传路径
			String filePathVar = PropertiesUtil.getProperty("goodsSkuPath");
			StringBuilder relativePath = new StringBuilder(filePathVar)
					.append(DateFormatUtils.format(new Date(), "yyyy-MM-dd")).append("/");
			StringBuilder rootPath = new StringBuilder(Constant.RootPath)
					.append(PropertiesUtil.getProperty("imageRootPath")).append(relativePath);

			// 判断如果没有该目录则创建一个目录
			File destDir = new File(rootPath.toString());
			if (!destDir.exists()) {
				destDir.mkdirs();
			}
			String storeFileName;
			if (StringUtils.isBlank(goodsId)) {
				storeFileName = goodsId + ".png";
			} else {
				storeFileName = Identities.uuid2() + ".png";
			}

			relativePath.append(storeFileName);

			drawTableImg.myGraphicsGeneration(bigarr, rootPath.append(storeFileName).toString());

			httpUrl = new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
					.append(PropertiesUtil.getProperty("imageRootPath")).append(relativePath.toString());
		} else {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(100);
			responseDTO.setMsg("当前产品没有规格请添加规格");
			return responseDTO;
		}
		returnData.put("httpUrl", httpUrl);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setData(returnData);
		responseDTO.setMsg("商品规格表格图片信息加载成功！");
		return responseDTO;
	}

	public ResponseDTO solrAddDocument(String id, String name) {
		ResponseDTO responseDTO = new ResponseDTO();

		Map<String, Object> returnData = Maps.newHashMap();

		List<TEvent> goodsIdList = eventDAO.findAllList();

		List<GoodsSolrDTO> goodsDTOList = Lists.newArrayList();

		TEventBargaining tEventBargaining;

		try {
			GoodsSolrDTO goodsDTO = null;
			String type = null;
			for (TEvent tEvent : goodsIdList) {

				goodsDTO = new GoodsSolrDTO();
				tEventBargaining = eventBargainingDAO.getByEventId(tEvent.getId());

				goodsDTO.setGoodsId(tEvent.getId());
				goodsDTO.setImageUrl(fxlService.getImageUrl(tEvent.getFimage1(), false));
				type = categoryService.getCategoryA(tEvent.getFtypeA());
				goodsDTO.setType(type != null ? type : StringUtils.EMPTY);
				if (tEvent.getFtypeB() != null) {
					goodsDTO.setCategoryId(tEvent.getFtypeB());
				}
				goodsDTO.setSpec(tEvent.getFspec());
				goodsDTO.setGoodsTitle(tEvent.getFtitle());
				if (tEvent.getFprice() != null) {
					goodsDTO.setOriginalPrice(tEvent.getFprice().toString());
				}
				if (tEvent.getFpriceMoney() != null) {
					goodsDTO.setPresentPrice(tEvent.getFpriceMoney().toString());
				}
				goodsDTO.setDesc(tEvent.getFbrief());

				if (tEvent.getTSponsor() != null && tEvent.getTSponsor().getFname() != null) {
					goodsDTO.setSponsorName(tEvent.getTSponsor().getFname());
				}

				// if (tEvent.getFlimitation().intValue() > 0) {
				// goodsDTO.setStockFlag(1);
				// } else {
				// goodsDTO.setStockFlag(0);
				// }
				goodsDTO.setStatus(tEvent.getFstatus());
				goodsDTO.setStatusString(DictionaryUtil.getString(DictionaryUtil.EventStatus, tEvent.getFstatus()));

				if (tEvent.getFsellModel() != null) {
					goodsDTO.setSellModel(tEvent.getFsellModel());
				}
				if (tEvent.getFspecModel() != null) {
					goodsDTO.setSpecModel(tEvent.getFspecModel());
				}

				if (tEvent.getFpromotionModel() != null) {
					goodsDTO.setPromotionModel(tEvent.getFpromotionModel());
				}

				if (tEvent.getFlimitation() != null) {
					goodsDTO.setLimitation(tEvent.getFlimitation());
				}

				if (tEvent.getFsdealsModel() != null) {
					goodsDTO.setSdealsModel(tEvent.getFsdealsModel());
				}

				if (tEvent.getFstock() != null) {
					if (tEvent.getFstock().intValue() <= 0) {
						goodsDTO.setIfInStock(false);
					}
				}
				if (tEvent.getFstock() != null) {
					goodsDTO.setPercentage(tEvent.getFstock() * 100 / tEvent.getFtotal());
				}

				if (tEvent.getFsaleTotal() != null) {
					goodsDTO.setSaleTotal(tEvent.getFsaleTotal());
				}

				if (tEvent.getFusePreferential() != null && tEvent.getFusePreferential() == 1) {
					goodsDTO.setUseCoupon(false);
				}
				// goodsDTO.setCount(1);
				if (tEvent.getFonSaleTime() != null) {
					goodsDTO.setCreateTime(tEvent.getFonSaleTime());
				}

				if (tEventBargaining != null) {
					goodsDTO.setBargainingId(tEventBargaining.getId());
				}

				goodsDTOList.add(goodsDTO);
			}
			SolrUtil solrUtil = new SolrUtil();
			solrUtil.solrSave(goodsDTOList);

		} catch (

		Exception e) {
			e.printStackTrace();
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			responseDTO.setMsg("搜索引擎增加词失败！");
			responseDTO.setData(returnData);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("搜索引擎增加词成功！");
		returnData.put("goodsDTOList", goodsDTOList);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO solrdeleteDocument(String id, String name) {
		ResponseDTO responseDTO = new ResponseDTO();

		Map<String, Object> returnData = Maps.newHashMap();
		SolrUtil solrUtil = new SolrUtil();
		try {

			solrUtil.deleteSolr(id);
		} catch (Exception e) {
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			responseDTO.setMsg("搜索引擎增加词失败！");
			responseDTO.setData(returnData);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("搜索引擎增加词成功！");
		responseDTO.setData(returnData);
		return responseDTO;
	}

}