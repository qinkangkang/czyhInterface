package com.innee.czyhInterface.service.v2;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.query.internal.QueryImpl;
import org.hibernate.transform.Transformers;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.Exceptions;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.dao.AppChannelSettingDAO;
import com.innee.czyhInterface.dao.CalendarDAO;
import com.innee.czyhInterface.dao.CommentDAO;
import com.innee.czyhInterface.dao.ConsultDAO;
import com.innee.czyhInterface.dao.CustomerRecommendDAO;
import com.innee.czyhInterface.dao.EventBargainingDAO;
import com.innee.czyhInterface.dao.EventCategoryDAO;
import com.innee.czyhInterface.dao.EventDAO;
import com.innee.czyhInterface.dao.EventRelationDAO;
import com.innee.czyhInterface.dao.EventSessionDAO;
import com.innee.czyhInterface.dao.EventSpecDAO;
import com.innee.czyhInterface.dao.FavoriteDAO;
import com.innee.czyhInterface.dao.SubjectDAO;
import com.innee.czyhInterface.dto.ArticleRecommendDTO;
import com.innee.czyhInterface.dto.CommentRecommendDTO;
import com.innee.czyhInterface.dto.CustomerDTO;
import com.innee.czyhInterface.dto.EventDistanceDTO;
import com.innee.czyhInterface.dto.EventRecommendDTO;
import com.innee.czyhInterface.dto.m.AppChannelSliderDTO;
import com.innee.czyhInterface.dto.m.CalendarDTO;
import com.innee.czyhInterface.dto.m.CouponDTO;
import com.innee.czyhInterface.dto.m.EventByChannelDTO;
import com.innee.czyhInterface.dto.m.EventDTO;
import com.innee.czyhInterface.dto.m.EventDetailDTO;
import com.innee.czyhInterface.dto.m.MerchantByChannelDTO;
import com.innee.czyhInterface.dto.m.PageDTO;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.entity.TAppChannelSetting;
import com.innee.czyhInterface.entity.TCalendar;
import com.innee.czyhInterface.entity.TCustomerRecommend;
import com.innee.czyhInterface.entity.TEvent;
import com.innee.czyhInterface.entity.TEventBargaining;
import com.innee.czyhInterface.entity.TEventCategory;
import com.innee.czyhInterface.entity.TEventRelation;
import com.innee.czyhInterface.entity.TFavorite;
import com.innee.czyhInterface.entity.TSponsor;
import com.innee.czyhInterface.entity.TSubject;
import com.innee.czyhInterface.util.CommonPage;
import com.innee.czyhInterface.util.Constant;
import com.innee.czyhInterface.util.DateUtil;
import com.innee.czyhInterface.util.DictionaryUtil;
import com.innee.czyhInterface.util.PropertiesUtil;
import com.innee.czyhInterface.util.asynchronoustasks.AsynchronousTasksManager;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.CustomerAddEventBrowseBean;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.CustomerAddEventRecommendBean;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.CustomerLikesDislikesBean;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Interface业务管理类
 * 
 * @author jinshengzhi
 */
@Component
@Transactional
public class EventService {

	private static final Logger logger = LoggerFactory.getLogger(EventService.class);

	// private static JsonMapper mapper = JsonMapper.nonDefaultMapper();

	@Autowired
	private CommonService commonService;

	@Autowired
	private FxlService fxlService;

	@Autowired
	private EventDAO eventDAO;

	@Autowired
	private EventSpecDAO eventSpecDAO;

	@Autowired
	private EventSessionDAO eventSessionDAO;

	@Autowired
	private FavoriteDAO favoriteDAO;

	@Autowired
	private CalendarDAO calendarDAO;

	@Autowired
	private AppChannelSettingDAO appChannelSettingDAO;

	@Autowired
	private CommentDAO commentDAO;

	@Autowired
	private SubjectDAO subjectDAO;

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private CouponService couponService;

	@Autowired
	private CustomerRecommendDAO customerRecommendDAO;

	@Autowired
	private EventBargainingDAO eventBargainingDAO;

	@Autowired
	private EventCategoryDAO eventCategoryDAO;

	@Autowired
	private ConsultDAO consultDAO;

	@Autowired
	private EventRelationDAO eventRelationDAO;

	/**
	 * 根据栏目ID返回该栏目下的活动过滤条件
	 * 
	 * @param valueMap
	 * @param page
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getEventFilterByChannelId(String channelId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(channelId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("channelId参数不能为空，请检查channelId的传递参数值！");
			return responseDTO;
		}

		// 获取到活动类目缓存对象
		Cache eventCategoryCache = cacheManager.getCache(Constant.EventCategory);
		Element ele = null;

		// 将检索结果的活动类目再次整理出一个类目list返回给前端
		List<Map<String, Object>> categoryList = Lists.newArrayList();
		Map<String, Object> map = Maps.newHashMap();
		map.put("id", 0);
		map.put("name", "全部");
		categoryList.add(map);
		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = Maps.newHashMap();
		hql.append(
				"select distinct(t.ftypeA) as ftypeA from TEvent t inner join t.TAppChannels as ace where t.fstatus = 20 and ace.TAppChannelSetting.id = :channelId");
		hqlMap.clear();
		hqlMap.put("channelId", channelId);

		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);
		for (Map<String, Object> amap : list) {
			if (amap.get("ftypeA") != null && StringUtils.isNotBlank(amap.get("ftypeA").toString())) {
				ele = eventCategoryCache.get(amap.get("ftypeA"));
				if (ele != null) {
					map = Maps.newHashMap();
					map.put("id", ele.getObjectKey());
					map.put("name", ele.getObjectValue());
					categoryList.add(map);
				}
			}
		}

		// 将检索结果的活动类目再次整理出一个活动适合年龄list返回给前端
		List<Map<String, Object>> ageList = Lists.newArrayList();
		map = Maps.newHashMap();
		map.put("id", 0);
		map.put("name", "不限");
		ageList.add(map);
		Map<Integer, String> ageMap = DictionaryUtil.getStatueMap(DictionaryUtil.EventAge);
		for (Iterator it = ageMap.entrySet().iterator(); it.hasNext();) {
			map = Maps.newHashMap();
			Map.Entry<Integer, String> e = (Map.Entry<Integer, String>) it.next();
			map.put("id", e.getKey());
			map.put("name", e.getValue());
			ageList.add(map);
		}

		// 将检索结果的活动类目再次整理出一个活动时长list返回给前端
		// List<Map<String, Object>> durationList = Lists.newArrayList();
		// Map<Integer, String> durationMap =
		// DictionaryUtil.getStatueMap(DictionaryUtil.EventDuration);
		// hql.delete(0, hql.length());
		// hql.append(
		// "select distinct(t.fduration) as fduration from TEvent t inner join
		// t.TAppChannels as ace where t.fstatus = 20 and
		// ace.TAppChannelSetting.id = :channelId");
		// hqlMap.clear();
		// hqlMap.put("channelId", channelId);
		//
		// list = commonService.find(hql.toString(), hqlMap);
		// int duration = 0;
		// for (Map<String, Object> amap : list) {
		// if (amap.get("fduration") != null &&
		// StringUtils.isNotBlank(amap.get("fduration").toString())) {
		// map = Maps.newHashMap();
		// duration = (Integer) amap.get("fduration");
		// map.put("id", duration);
		// map.put("name", durationMap.get(duration));
		// durationList.add(map);
		// }
		// }
		//
		// List<Map<String, Object>> siteList = Lists.newArrayList();
		// Map<Integer, String> amap =
		// DictionaryUtil.getStatueMap(DictionaryUtil.SiteType);
		// for (Iterator it = amap.entrySet().iterator(); it.hasNext();) {
		// map = Maps.newHashMap();
		// Map.Entry<Integer, String> e = (Map.Entry<Integer, String>)
		// it.next();
		// map.put("id", e.getKey());
		// map.put("name", e.getValue());
		// siteList.add(map);
		// }

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("categoryList", categoryList);
		returnData.put("ageList", ageList);
		returnData.put("durationList", StringUtils.EMPTY);
		returnData.put("siteList", StringUtils.EMPTY);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * 根据栏目ID返回该栏目下的活动过滤条件
	 * 
	 * @param valueMap
	 * @param page
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getEventFilterByChannelIdV2(String channelId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(channelId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("channelId参数不能为空，请检查channelId的传递参数值！");
			return responseDTO;
		}

		// 获取到活动类目缓存对象
		Cache eventCategoryCache = cacheManager.getCache(Constant.EventCategory);
		Element ele = null;

		// 将检索结果的活动类目再次整理出一个类目list返回给前端
		List<Map<String, Object>> categoryList = Lists.newArrayList();
		Map<String, Object> map = Maps.newHashMap();
		map.put("id", 0);
		map.put("name", "全部");
		map.put("imageUrl", new StringBuilder().append(PropertiesUtil.getProperty("fileServerUrl"))
				.append("/czyhInterface/image/event/category/all.png").toString());
		categoryList.add(map);
		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = Maps.newHashMap();
		hql.append(
				"select distinct(t.ftypeA) as ftypeA from TEvent t inner join t.TAppChannels as ace where t.fstatus = 20 and ace.TAppChannelSetting.id = :channelId");
		hqlMap.clear();
		hqlMap.put("channelId", channelId);

		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);
		TEventCategory tEventCategory = null;
		for (Map<String, Object> amap : list) {
			if (amap.get("ftypeA") != null && StringUtils.isNotBlank(amap.get("ftypeA").toString())) {
				ele = eventCategoryCache.get(amap.get("ftypeA"));
				if (ele != null) {
					map = Maps.newHashMap();
					map.put("id", ele.getObjectKey());
					map.put("name", ele.getObjectValue());
					tEventCategory = eventCategoryDAO.getByLevelAndValue(1, (Integer) amap.get("ftypeA"));
					if (StringUtils.isNotBlank(tEventCategory.getImageA())) {
						map.put("imageUrl", new StringBuilder().append(PropertiesUtil.getProperty("fileServerUrl"))
								.append(tEventCategory.getImageA()).toString());
					}
					categoryList.add(map);
				}
			}
		}

		// 将检索结果的活动类目再次整理出一个活动适合年龄list返回给前端
		List<Map<String, Object>> ageList = Lists.newArrayList();
		map = Maps.newHashMap();
		map.put("id", 0);
		map.put("name", "不限");
		ageList.add(map);
		Map<Integer, String> ageMap = DictionaryUtil.getStatueMap(DictionaryUtil.EventAge);
		for (Iterator it = ageMap.entrySet().iterator(); it.hasNext();) {
			map = Maps.newHashMap();
			Map.Entry<Integer, String> e = (Map.Entry<Integer, String>) it.next();
			map.put("id", e.getKey());
			map.put("name", e.getValue());
			ageList.add(map);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("categoryList", categoryList);
		returnData.put("ageList", ageList);
		returnData.put("durationList", StringUtils.EMPTY);
		returnData.put("siteList", StringUtils.EMPTY);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * 根据栏目ID返回该栏目下的活动列表
	 * 
	 * @param valueMap
	 * @param page
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getEventListByChannelId(String channelId, Integer categoryId, Integer age, Integer siteType,
			Integer duration, Integer orderBy, Integer pageSize, Integer offset, String sessionId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(channelId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("channelId参数不能为空，请检查channelId的传递参数值！");
			return responseDTO;
		}
		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}
		// 获取栏目信息，获取到该栏目默认排序规则
		TAppChannelSetting tAppChannelSetting = appChannelSettingDAO.getOne(channelId);
		int orderType = tAppChannelSetting.getFdefaultOrderType() != null
				? tAppChannelSetting.getFdefaultOrderType().intValue() : 1;

		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = Maps.newHashMap();
		hqlMap.put("channelId", channelId);
		if (orderBy.intValue() == 1 || orderType == 3) {
			hql.append(
					"select t.id as id, t.ftitle as ftitle, t.fsubTitle as fsubTitle, t.ftypeA as ftypeA ,t.feventTime as feventTime, t.fimage1 as fimage1, t.fprice as fprice, t.fdeal as fdeal, t.fsaleFlag as fsaleFlag, t.ftag as ftag, t.fstatus as fstatus, t.fscore as fscore, t.frecommend as frecommend, dt.fdistance as fdistance from TEvent t inner join t.TAppChannels as ace left join TEventDistanceTemp as dt on t.id = dt.feventId and dt.fhsid = :sessionId where t.fstatus = 20 and ace.TAppChannelSetting.id = :channelId");
			hqlMap.put("sessionId", sessionId);
		} else {
			hql.append(
					"select t.id as id, t.ftitle as ftitle, t.fsubTitle as fsubTitle, t.ftypeA as ftypeA ,t.feventTime as feventTime, t.fimage1 as fimage1, t.fprice as fprice, t.fdeal as fdeal, t.fsaleFlag as fsaleFlag, t.ftag as ftag, t.fstatus as fstatus, t.fscore as fscore, t.frecommend as frecommend from TEvent t inner join t.TAppChannels as ace where t.fstatus = 20 and ace.TAppChannelSetting.id = :channelId");
		}

		if (categoryId.intValue() != 0) {
			hql.append(" and t.ftypeA = :categoryId");
			hqlMap.put("categoryId", categoryId);
		}
		if (age.intValue() != 0) {
			// hql.append(" and t.ftypeA = :categoryId");
			// hqlMap.put("categoryId", categoryId);
		}
		if (siteType.intValue() != 0) {
			hql.append(" and t.fsiteType = :siteType");
			hqlMap.put("siteType", siteType);
		}
		if (duration.intValue() != 0) {
			hql.append(" and t.fduration = :duration");
			hqlMap.put("duration", duration);
		}
		if (orderBy.intValue() != 0) {
			if (orderBy.intValue() == 1) {
				hql.append(" order by dt.fdistance desc");
			} else if (orderBy.intValue() == 2) {
				hql.append(" order by t.frecommend desc");
			} else if (orderBy.intValue() == 3) {
				hql.append(" order by t.fupdateTime desc");
			} else if (orderBy.intValue() == 4) {
				hql.append(" order by t.forderPrice asc");
			} else if (orderBy.intValue() == 5) {
				hql.append(" order by t.forderPrice desc");
			}
		} else {
			// 如果前端没有传递排序规则信息，则使用该栏目默认排序规则
			if (orderType == 1) {
				hql.append(" order by ace.forder desc, ace.fpublishTime desc");
			} else if (orderType == 2) {
				hql.append(" order by t.fupdateTime desc");
			} else if (orderType == 3) {
				hql.append(" order by dt.fdistance desc");
			} else if (orderType == 4) {
				// TODO 目前还没有根据销量排序的好方法，先用推荐数排序代替
				hql.append(" order by t.frecommend desc");
			} else if (orderType == 5) {
				hql.append(" order by t.frecommend desc");
			}
		}
		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();

		// 获取到活动类目缓存对象
		Cache eventCategoryCache = cacheManager.getCache(Constant.EventCategory);
		// 获取活动推荐数缓存对象
		Cache eventRecommendCache = cacheManager.getCache(Constant.EventRecommend);
		// 获取活动距离信息缓存对象
		Cache sessionIdEventDistanceCache = cacheManager.getCache(Constant.SessionIdEventDistance);
		Element ele = null;

		// 对活动信息进行细化加载
		List<EventDTO> resultList = Lists.newArrayList();
		EventDTO eventDTO = null;
		int statusValue = 0;
		int stockFlag = 0;
		BigDecimal distance = null;
		for (Map<String, Object> amap : list) {
			eventDTO = new EventDTO();
			eventDTO.setEventId(amap.get("id").toString());
			eventDTO.setEventTitle(amap.get("ftitle").toString());
			if (amap.get("fsubTitle") != null && StringUtils.isNotBlank(amap.get("fsubTitle").toString())) {
				eventDTO.setEventSubTitle(amap.get("fsubTitle").toString());
			}
			if (amap.get("ftypeA") != null && StringUtils.isNotBlank(amap.get("ftypeA").toString())) {
				ele = eventCategoryCache.get((Integer) amap.get("ftypeA"));
				eventDTO.setType(ele != null ? ele.getObjectValue().toString() : StringUtils.EMPTY);
			}
			if (amap.get("feventTime") != null && StringUtils.isNotBlank(amap.get("feventTime").toString())) {
				eventDTO.setEventTime(amap.get("feventTime").toString());
			}
			if (amap.get("fimage1") != null && StringUtils.isNotBlank(amap.get("fimage1").toString())) {
				eventDTO.setImageUrl(fxlService.getImageUrl(amap.get("fimage1").toString(), false));
			} else {
				eventDTO.setImageUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
						.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPic.jpg").toString());
			}
			if (amap.get("fprice") != null && StringUtils.isNotBlank(amap.get("fprice").toString())) {
				eventDTO.setOriginalPrice(amap.get("fprice").toString());
			}
			if (amap.get("fdeal") != null && StringUtils.isNotBlank(amap.get("fdeal").toString())) {
				eventDTO.setPresentPrice(amap.get("fdeal").toString());
			} else {
				eventDTO.setPresentPrice("免费");
			}
			if (amap.get("fsaleFlag") != null && StringUtils.isNotBlank(amap.get("fsaleFlag").toString())) {
				stockFlag = ((Integer) amap.get("fsaleFlag")).intValue();
				if (stockFlag > 0) {
					eventDTO.setStockFlag(1);
				} else {
					eventDTO.setStockFlag(0);
				}
			} else {
				eventDTO.setStockFlag(0);
			}
			ele = eventRecommendCache.get(eventDTO.getEventId());
			if (ele != null) {
				EventRecommendDTO eventRecommendDTO = (EventRecommendDTO) ele.getObjectValue();
				eventDTO.setRecommend(eventRecommendDTO.getRecommend());
			} else if (amap.get("frecommend") != null && StringUtils.isNotBlank(amap.get("frecommend").toString())) {
				eventDTO.setRecommend((Long) amap.get("frecommend"));
			}
			ele = sessionIdEventDistanceCache.get(sessionId);
			if (ele != null) {
				Map<String, EventDistanceDTO> eventDistanceMap = (Map<String, EventDistanceDTO>) ele.getObjectValue();
				EventDistanceDTO eventDistanceDTO = eventDistanceMap.get(eventDTO.getEventId());
				if (eventDistanceDTO != null) {
					eventDTO.setDistance(eventDistanceDTO.getDistanceString());
				}
			} else if (amap.get("fdistance") != null && StringUtils.isNotBlank(amap.get("fdistance").toString())) {
				distance = new BigDecimal((Integer) amap.get("fdistance")).negate().divide(new BigDecimal(1000), 1,
						RoundingMode.HALF_UP);
				eventDTO.setDistance(distance.toString() + "km");
			}
			if (amap.get("fscore") != null && StringUtils.isNotBlank(amap.get("fscore").toString())) {
				eventDTO.setScore((BigDecimal) amap.get("fscore"));
			}
			if (amap.get("fstatus") != null && StringUtils.isNotBlank(amap.get("fstatus").toString())) {
				statusValue = ((Integer) amap.get("fstatus")).intValue();
				eventDTO.setStatus(statusValue);
				eventDTO.setStatusString(DictionaryUtil.getString(DictionaryUtil.EventStatus, statusValue));
			}

			// TODO 活动列表目前没有地址信息和gps坐标信息，所以先注释掉
			// hql.delete(0, hql.length());
			// hql.append(
			// "select t.faddress as faddress, t.fgps as fgps from TEventSession
			// t where t.TEvent.id = :eventId and t.faddress is not null and
			// t.faddress != '' and t.fstatus < 999");
			// hqlMap.clear();
			// hqlMap.put("eventId", amap.get("id"));
			// Query q = commonService.createQuery(hql.toString(), hqlMap);
			// q.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			// q.setFirstResult(0).setMaxResults(1);
			// List<Map<String, Object>> list2 = q.getResultList();
			// Map<String, Object> bmap = CollectionUtils.isNotEmpty(list2) ?
			// list2.get(0) : null;
			// if (MapUtils.isNotEmpty(bmap)) {
			// if (bmap.get("faddress") != null &&
			// StringUtils.isNotBlank(bmap.get("faddress").toString())) {
			// eventDTO.setAddress(bmap.get("faddress").toString());
			// }
			// if (bmap.get("fgps") != null &&
			// StringUtils.isNotBlank(bmap.get("fgps").toString())) {
			// String gps = bmap.get("fgps").toString();
			// eventDTO.setGps(gps);
			// if (StringUtils.isNotBlank(gps)) {
			// String[] gpss = gps.split(",");
			// if (gpss.length > 1) {
			// eventDTO.setGpsLng(gpss[0]);
			// eventDTO.setGpsLat(gpss[1]);
			// }
			// }
			// }
			// }
			// eventDTO.setCommentNum(commentDAO.getCommentCount(eventDTO.getEventId(),
			// 2));
			// eventDTO.setSubscribeNum(orderDAO.getOrderCountByEventId(eventDTO.getEventId()));
			resultList.add(eventDTO);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("eventList", resultList);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getCalendar(Integer cityId, String month) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (cityId == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("cityId参数不能为空，请检查cityId的传递参数值！");
			return responseDTO;
		}

		Map<String, Object> returnData = Maps.newHashMap();

		Date now = new Date();
		try {
			if (StringUtils.isNotBlank(month)) {
				Date monthDate = DateUtils.parseDate(month, "yyyy-MM");
				if (!DateUtils.truncatedEquals(now, monthDate, Calendar.MONTH)) {
					now = DateUtils.parseDate(month, "yyyy-MM");
				}
			}
		} catch (ParseException e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("组装活动场次日历时出错！");
			return responseDTO;
		}
		// 月底外加一周时间作为最后一天
		Date lastDay = DateUtils.addWeeks(DateUtil.getMonthEndTime(now), 1);

		Date thisDay = null;
		DateTime thisDayTime = null;
		String[] weekStr = ArrayUtils.toArray("周一", "周二", "周三", "周四", "周五", "周六", "周日");

		int min = Integer.valueOf(DateFormatUtils.format(now, "yyyyMMdd")).intValue();
		int max = Integer.valueOf(DateFormatUtils.format(lastDay, "yyyyMMdd")).intValue();

		List<CalendarDTO> list = Lists.newArrayList();

		List<TCalendar> calendarList = calendarDAO.findByCalendarStartAndEndAndHaveNum(cityId, min, max);
		CalendarDTO calendarDTO = null;

		try {
			for (TCalendar tCalendar : calendarList) {
				calendarDTO = new CalendarDTO();
				thisDay = DateUtils.parseDate(tCalendar.getEventDate().toString(), "yyyyMMdd");
				thisDayTime = new DateTime(thisDay);
				calendarDTO.setWeek(weekStr[thisDayTime.getDayOfWeek() - 1]);
				if (tCalendar.getEventNum().intValue() > 0) {
					calendarDTO.setSessionNum(tCalendar.getEventNum() + "场");
				}
				calendarDTO.setViewFlag(0);
				calendarDTO.setDay(DateFormatUtils.format(thisDay, "yyyy-MM-dd"));
				list.add(calendarDTO);
			}
		} catch (ParseException e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("组装活动场次日历时出错！");
			return responseDTO;
		}

		int maxMonth = StringUtils.isNotBlank(PropertiesUtil.getProperty("maxMonthForCalendar"))
				? Integer.valueOf(PropertiesUtil.getProperty("maxMonthForCalendar")).intValue() : 0;

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		returnData.put("eventNumList", list);
		returnData.put("maxMonth", maxMonth);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * @param cityId
	 * @return
	 */
	/**
	 * @param cityId
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getCalendarAll(Integer cityId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (cityId == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("cityId参数不能为空，请检查cityId的传递参数值！");
			return responseDTO;
		}

		Map<String, Object> returnData = Maps.newHashMap();

		Date now = new Date();

		int maxMonth = StringUtils.isNotBlank(PropertiesUtil.getProperty("maxMonthForCalendar"))
				? Integer.valueOf(PropertiesUtil.getProperty("maxMonthForCalendar")).intValue() : 0;
		// 当前月份之后三个月的活动日历
		Date lastDay = DateUtils.addWeeks(DateUtil.getMonthEndTime(DateUtils.addMonths(now, maxMonth)), 1);

		Date thisDay = null;
		DateTime thisDayTime = null;
		String[] weekStr = ArrayUtils.toArray("周一", "周二", "周三", "周四", "周五", "周六", "周日");

		int min = Integer.valueOf(DateFormatUtils.format(now, "yyyyMMdd")).intValue();
		int max = Integer.valueOf(DateFormatUtils.format(lastDay, "yyyyMMdd")).intValue();

		List<CalendarDTO> list = Lists.newArrayList();

		List<TCalendar> calendarList = calendarDAO.findByCalendarStartAndEndAndHaveNum(cityId, min, max);
		CalendarDTO calendarDTO = null;

		try {
			for (TCalendar tCalendar : calendarList) {
				calendarDTO = new CalendarDTO();
				thisDay = DateUtils.parseDate(tCalendar.getEventDate().toString(), "yyyyMMdd");
				thisDayTime = new DateTime(thisDay);
				calendarDTO.setWeek(weekStr[thisDayTime.getDayOfWeek() - 1]);
				if (tCalendar.getEventNum().intValue() > 0) {
					calendarDTO.setSessionNum(tCalendar.getEventNum() + "场");
				}
				calendarDTO.setViewFlag(0);
				calendarDTO.setDay(DateFormatUtils.format(thisDay, "yyyy-MM-dd"));
				list.add(calendarDTO);
			}
		} catch (ParseException e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("组装活动场次日历时出错！");
			return responseDTO;
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		returnData.put("eventNumList", list);
		returnData.put("maxMonth", maxMonth);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * 根据栏目ID返回该栏目下的活动过滤条件
	 * 
	 * @param valueMap
	 * @param page
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getEventFilterBySearch(Integer cityId, String key) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (cityId == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("cityId参数不能为空，请检查cityId的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(key)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("key参数不能为空，请检查key的传递参数值！");
			return responseDTO;
		}

		// 获取到活动类目缓存对象
		Cache eventCategoryCache = cacheManager.getCache(Constant.EventCategory);
		Element ele = null;

		// 将检索结果的活动类目再次整理出一个类目list返回给前端
		List<Map<String, Object>> categoryList = Lists.newArrayList();
		Map<String, Object> map = Maps.newHashMap();
		map.put("id", 0);
		map.put("name", "全部");
		categoryList.add(map);
		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = Maps.newHashMap();
		hql.append("select distinct(t.ftypeA) as ftypeA from TEvent t where t.fstatus = 20 and t.fcity = :cityId");
		hqlMap.put("cityId", cityId);
		if (StringUtils.isNotBlank(key)) {
			hql.append(" and (t.ftitle like :key or t.ftag like :key or t.fbrief like :key)");
			hqlMap.put("key", "%" + key + "%");
		}
		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);
		for (Map<String, Object> amap : list) {
			if (amap.get("ftypeA") != null && StringUtils.isNotBlank(amap.get("ftypeA").toString())) {
				ele = eventCategoryCache.get(amap.get("ftypeA"));
				if (ele != null) {
					map = Maps.newHashMap();
					map.put("id", ele.getObjectKey());
					map.put("name", ele.getObjectValue());
					categoryList.add(map);
				}
			}
		}

		// 将检索结果的活动类目再次整理出一个活动适合年龄list返回给前端
		List<Map<String, Object>> ageList = Lists.newArrayList();
		map = Maps.newHashMap();
		map.put("id", 0);
		map.put("name", "不限");
		ageList.add(map);
		Map<Integer, String> ageMap = DictionaryUtil.getStatueMap(DictionaryUtil.EventAge);
		for (Iterator it = ageMap.entrySet().iterator(); it.hasNext();) {
			map = Maps.newHashMap();
			Map.Entry<Integer, String> e = (Map.Entry<Integer, String>) it.next();
			map.put("id", e.getKey());
			map.put("name", e.getValue());
			ageList.add(map);
		}

		// 将检索结果的活动类目再次整理出一个活动时长list返回给前端
		// List<Map<String, Object>> durationList = Lists.newArrayList();
		//
		// Map<Integer, String> durationMap =
		// DictionaryUtil.getStatueMap(DictionaryUtil.EventDuration);
		// hql.delete(0, hql.length());
		// hql.append(
		// "select distinct(t.fduration) as fduration from TEvent t where
		// t.fstatus = 20 and t.fcity = :cityId");
		// hqlMap.put("cityId", cityId);
		// if (StringUtils.isNotBlank(key)) {
		// hql.append(" and (t.ftitle like :key or t.ftag like :key or t.fbrief
		// like :key)");
		// hqlMap.put("key", "%" + key + "%");
		// }
		// list = commonService.find(hql.toString(), hqlMap);
		// int duration = 0;
		// for (Map<String, Object> amap : list) {
		// if (amap.get("fduration") != null &&
		// StringUtils.isNotBlank(amap.get("fduration").toString())) {
		// map = Maps.newHashMap();
		// duration = (Integer) amap.get("fduration");
		// map.put("id", duration);
		// map.put("name", durationMap.get(duration));
		// durationList.add(map);
		// }
		// }
		//
		// List<Map<String, Object>> siteList = Lists.newArrayList();
		// Map<Integer, String> amap =
		// DictionaryUtil.getStatueMap(DictionaryUtil.SiteType);
		// for (Iterator it = amap.entrySet().iterator(); it.hasNext();) {
		// map = Maps.newHashMap();
		// Map.Entry<Integer, String> e = (Map.Entry<Integer, String>)
		// it.next();
		// map.put("id", e.getKey());
		// map.put("name", e.getValue());
		// siteList.add(map);
		// }

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("categoryList", categoryList);
		returnData.put("ageList", ageList);
		returnData.put("durationList", StringUtils.EMPTY);
		returnData.put("siteList", StringUtils.EMPTY);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * 根据栏目ID返回该栏目下的活动过滤条件
	 * 
	 * @param valueMap
	 * @param page
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getEventFilterBySearchV2(Integer cityId, String key) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (cityId == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("cityId参数不能为空，请检查cityId的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(key)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("key参数不能为空，请检查key的传递参数值！");
			return responseDTO;
		}

		// 获取到活动类目缓存对象
		Cache eventCategoryCache = cacheManager.getCache(Constant.EventCategory);
		Element ele = null;

		// 将检索结果的活动类目再次整理出一个类目list返回给前端
		List<Map<String, Object>> categoryList = Lists.newArrayList();
		Map<String, Object> map = Maps.newHashMap();
		map.put("id", 0);
		map.put("name", "全部");
		map.put("imageUrl", new StringBuilder().append(PropertiesUtil.getProperty("fileServerUrl"))
				.append("/czyhInterface/image/event/category/all.png").toString());
		categoryList.add(map);
		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = Maps.newHashMap();
		hql.append("select distinct(t.ftypeA) as ftypeA from TEvent t where t.fstatus = 20 and t.fcity = :cityId");
		hqlMap.put("cityId", cityId);
		if (StringUtils.isNotBlank(key)) {
			hql.append(" and (t.ftitle like :key or t.ftag like :key or t.fbrief like :key)");
			hqlMap.put("key", "%" + key + "%");
		}
		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);
		TEventCategory tEventCategory = null;
		for (Map<String, Object> amap : list) {
			if (amap.get("ftypeA") != null && StringUtils.isNotBlank(amap.get("ftypeA").toString())) {
				ele = eventCategoryCache.get(amap.get("ftypeA"));
				if (ele != null) {
					map = Maps.newHashMap();
					map.put("id", ele.getObjectKey());
					map.put("name", ele.getObjectValue());
					tEventCategory = eventCategoryDAO.getByLevelAndValue(1, (Integer) amap.get("ftypeA"));
					if (StringUtils.isNotBlank(tEventCategory.getImageA())) {
						map.put("imageUrl", new StringBuilder().append(PropertiesUtil.getProperty("fileServerUrl"))
								.append(tEventCategory.getImageA()).toString());
					}
					categoryList.add(map);
				}
			}
		}

		// 将检索结果的活动类目再次整理出一个活动适合年龄list返回给前端
		List<Map<String, Object>> ageList = Lists.newArrayList();
		map = Maps.newHashMap();
		map.put("id", 0);
		map.put("name", "不限");
		ageList.add(map);
		Map<Integer, String> ageMap = DictionaryUtil.getStatueMap(DictionaryUtil.EventAge);
		for (Iterator it = ageMap.entrySet().iterator(); it.hasNext();) {
			map = Maps.newHashMap();
			Map.Entry<Integer, String> e = (Map.Entry<Integer, String>) it.next();
			map.put("id", e.getKey());
			map.put("name", e.getValue());
			ageList.add(map);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("categoryList", categoryList);
		returnData.put("ageList", ageList);
		returnData.put("durationList", StringUtils.EMPTY);
		returnData.put("siteList", StringUtils.EMPTY);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * @param valueMap
	 * @param page
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO searchEventList(Integer cityId, String key, Integer categoryId, Integer age, Integer siteType,
			Integer duration, Integer orderBy, Integer pageSize, Integer offset, String sessionId) {
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

		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = Maps.newHashMap();
		if (orderBy.intValue() == 1) {
			hql.append(
					"select t.id as id, t.ftitle as ftitle, t.fsubTitle as fsubTitle, t.ftypeA as ftypeA ,t.feventTime as feventTime, t.fimage1 as fimage1, t.fprice as fprice, t.fdeal as fdeal, t.fsaleFlag as fsaleFlag, t.ftag as ftag, t.fstatus as fstatus, t.fscore as fscore, t.frecommend as frecommend, dt.fdistance as fdistance from TEvent t left join TEventDistanceTemp as dt on t.id = dt.feventId and dt.fhsid = :sessionId where t.fstatus = 20");
			hqlMap.put("sessionId", sessionId);
		} else {
			hql.append(
					"select t.id as id, t.ftitle as ftitle, t.fsubTitle as fsubTitle, t.ftypeA as ftypeA ,t.feventTime as feventTime, t.fimage1 as fimage1, t.fprice as fprice, t.fdeal as fdeal, t.fsaleFlag as fsaleFlag, t.ftag as ftag, t.fstatus as fstatus, t.fscore as fscore, t.frecommend as frecommend from TEvent t where t.fstatus = 20");
		}

		if (StringUtils.isNotBlank(key)) {
			hql.append(" and (t.ftitle like :key or t.ftag like :key or t.fbrief like :key)");
			hqlMap.put("key", "%" + key + "%");
		}
		if (categoryId.intValue() != 0) {
			hql.append(" and t.ftypeA = :categoryId");
			hqlMap.put("categoryId", categoryId);
		}
		if (age.intValue() != 0) {
			// hql.append(" and t.ftypeA = :categoryId");
			// hqlMap.put("categoryId", categoryId);
		}
		if (siteType.intValue() != 0) {
			hql.append(" and t.fsiteType = :siteType");
			hqlMap.put("siteType", siteType);
		}
		if (duration.intValue() != 0) {
			hql.append(" and t.fduration = :duration");
			hqlMap.put("duration", duration);
		}
		if (orderBy.intValue() != 0) {
			if (orderBy.intValue() == 1) {
				hql.append(" order by dt.fdistance desc");
			} else if (orderBy.intValue() == 2) {
				hql.append(" order by t.frecommend desc");
			} else if (orderBy.intValue() == 3) {
				hql.append(" order by t.fupdateTime desc");
			} else if (orderBy.intValue() == 4) {
				hql.append(" order by t.forderPrice asc");
			} else if (orderBy.intValue() == 5) {
				hql.append(" order by t.forderPrice desc");
			}
		} else {
			hql.append(" order by t.fupdateTime desc");
		}
		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();

		// 获取到活动类目缓存对象
		Cache eventCategoryCache = cacheManager.getCache(Constant.EventCategory);
		// 获取活动推荐数缓存对象
		Cache eventRecommendCache = cacheManager.getCache(Constant.EventRecommend);
		// 获取活动距离信息缓存对象
		Cache sessionIdEventDistanceCache = cacheManager.getCache(Constant.SessionIdEventDistance);
		Element ele = null;

		// 对活动信息进行细化加载
		List<EventDTO> resultList = Lists.newArrayList();
		EventDTO eventDTO = null;
		int statusValue = 0;
		int stockFlag = 0;
		BigDecimal distance = null;
		for (Map<String, Object> amap : list) {
			eventDTO = new EventDTO();
			eventDTO.setEventId(amap.get("id").toString());
			eventDTO.setEventTitle(amap.get("ftitle").toString());
			if (amap.get("fsubTitle") != null && StringUtils.isNotBlank(amap.get("fsubTitle").toString())) {
				eventDTO.setEventSubTitle(amap.get("fsubTitle").toString());
			}
			if (amap.get("ftypeA") != null && StringUtils.isNotBlank(amap.get("ftypeA").toString())) {
				ele = eventCategoryCache.get((Integer) amap.get("ftypeA"));
				eventDTO.setType(ele != null ? ele.getObjectValue().toString() : StringUtils.EMPTY);
			}
			if (amap.get("feventTime") != null && StringUtils.isNotBlank(amap.get("feventTime").toString())) {
				eventDTO.setEventTime(amap.get("feventTime").toString());
			}
			if (amap.get("fimage1") != null && StringUtils.isNotBlank(amap.get("fimage1").toString())) {
				eventDTO.setImageUrl(fxlService.getImageUrl(amap.get("fimage1").toString(), false));
			} else {
				eventDTO.setImageUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
						.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPic.jpg").toString());
			}
			if (amap.get("fprice") != null && StringUtils.isNotBlank(amap.get("fprice").toString())) {
				eventDTO.setOriginalPrice(amap.get("fprice").toString());
			}
			if (amap.get("fdeal") != null && StringUtils.isNotBlank(amap.get("fdeal").toString())) {
				eventDTO.setPresentPrice(amap.get("fdeal").toString());
			} else {
				eventDTO.setPresentPrice("免费");
			}
			if (amap.get("fsaleFlag") != null && StringUtils.isNotBlank(amap.get("fsaleFlag").toString())) {
				stockFlag = ((Integer) amap.get("fsaleFlag")).intValue();
				if (stockFlag > 0) {
					eventDTO.setStockFlag(1);
				} else {
					eventDTO.setStockFlag(0);
				}
			} else {
				eventDTO.setStockFlag(0);
			}
			if (amap.get("fscore") != null && StringUtils.isNotBlank(amap.get("fscore").toString())) {
				eventDTO.setScore((BigDecimal) amap.get("fscore"));
			}
			ele = eventRecommendCache.get(eventDTO.getEventId());
			if (ele != null) {
				EventRecommendDTO eventRecommendDTO = (EventRecommendDTO) ele.getObjectValue();
				eventDTO.setRecommend(eventRecommendDTO.getRecommend());
			} else if (amap.get("frecommend") != null && StringUtils.isNotBlank(amap.get("frecommend").toString())) {
				eventDTO.setRecommend((Long) amap.get("frecommend"));
			}
			ele = sessionIdEventDistanceCache.get(sessionId);
			if (ele != null) {
				Map<String, EventDistanceDTO> eventDistanceMap = (Map<String, EventDistanceDTO>) ele.getObjectValue();
				EventDistanceDTO eventDistanceDTO = eventDistanceMap.get(eventDTO.getEventId());
				if (eventDistanceDTO != null) {
					eventDTO.setDistance(eventDistanceDTO.getDistanceString());
				}
			} else if (amap.get("fdistance") != null && StringUtils.isNotBlank(amap.get("fdistance").toString())) {
				distance = new BigDecimal((Integer) amap.get("fdistance")).negate().divide(new BigDecimal(1000), 1,
						RoundingMode.HALF_UP);
				eventDTO.setDistance(distance.toString() + "km");
			}
			if (amap.get("fstatus") != null && StringUtils.isNotBlank(amap.get("fstatus").toString())) {
				statusValue = ((Integer) amap.get("fstatus")).intValue();
				eventDTO.setStatus(statusValue);
				eventDTO.setStatusString(DictionaryUtil.getString(DictionaryUtil.EventStatus, statusValue));
			}

			// TODO 活动列表目前没有地址信息和gps坐标信息，所以先注释掉
			// hql.delete(0, hql.length());
			// hql.append(
			// "select t.faddress as faddress, t.fgps as fgps from TEventSession
			// t where t.TEvent.id = :eventId and t.faddress is not null and
			// t.faddress != '' and t.fstatus < 999");
			// hqlMap.clear();
			// hqlMap.put("eventId", amap.get("id"));
			// Query q = commonService.createQuery(hql.toString(), hqlMap);
			// q.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			// q.setFirstResult(0).setMaxResults(1);
			// List<Map<String, Object>> list2 = q.getResultList();
			// Map<String, Object> bmap = CollectionUtils.isNotEmpty(list2) ?
			// list2.get(0) : null;
			// if (MapUtils.isNotEmpty(bmap)) {
			// if (bmap.get("faddress") != null &&
			// StringUtils.isNotBlank(bmap.get("faddress").toString())) {
			// eventDTO.setAddress(bmap.get("faddress").toString());
			// }
			// if (bmap.get("fgps") != null &&
			// StringUtils.isNotBlank(bmap.get("fgps").toString())) {
			// String gps = bmap.get("fgps").toString();
			// eventDTO.setGps(gps);
			// if (StringUtils.isNotBlank(gps)) {
			// String[] gpss = gps.split(",");
			// if (gpss.length > 1) {
			// eventDTO.setGpsLng(gpss[0]);
			// eventDTO.setGpsLat(gpss[1]);
			// }
			// }
			// }
			// }

			// eventDTO.setCommentNum(commentDAO.getCommentCount(eventDTO.getEventId(),
			// 2));
			// eventDTO.setSubscribeNum(orderDAO.getOrderCountByEventId(eventDTO.getEventId()));
			resultList.add(eventDTO);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("eventList", resultList);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);
		if (page.getTotalCount() == 0L) {
			responseDTO.setMsg("没有检索到您查找的活动！");
		}
		return responseDTO;
	}

	/**
	 * @param valueMap
	 * @param page
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<EventDTO> getEventListByEventDate(Integer categoryId, Integer age, Integer siteType, Integer duration,
			Integer orderBy, Map<String, Object> valueMap, CommonPage page, String sessionId) {
		return null;
	}

	/**
	 * 获取单品页面详细信息
	 * @param valueMap
	 * @param page
	 * @return
	 */
	public ResponseDTO getEventListByEventType(Integer cityId, Integer eventType, Integer categoryId, Integer age,
			Integer siteType, Integer duration, Integer orderBy, Integer pageSize, Integer offset, String sessionId) {
		return null;
	}

	public ResponseDTO getEventDetail(Integer clientType, String ticket, String eventId, String sessionId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(eventId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("eventId参数不能为空，请检查eventId的传递参数值！");
			return responseDTO;
		}
		
		TEvent tEvent = eventDAO.findOne(eventId);
		if (tEvent == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("eventId参数信息有误，系统中没有活动ID为“" + eventId + "”的活动！");
			return responseDTO;
		}
		EventDetailDTO eventDetailDTO = new EventDetailDTO();
		Map<String, Object> returnData = Maps.newHashMap();
		
		StringBuilder hql = new StringBuilder();
		hql.append(
				"select t.faddress as faddress, t.fgps as fgps from TEventSession t where t.TEvent.id = :eventId and t.faddress is not null and t.faddress != '' and t.fstatus < 999");
		Map<String, Object> hqlMap = Maps.newHashMap();
		hqlMap.put("eventId", eventId);
		Query q = commonService.createQuery(hql.toString(), hqlMap);
		q.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		q.setFirstResult(0).setMaxResults(1);
		List<Map<String, Object>> list2 = q.getResultList();
		Map<String, Object> bmap = CollectionUtils.isNotEmpty(list2) ? list2.get(0) : null;
		if (MapUtils.isNotEmpty(bmap)) {
			if (bmap.get("faddress") != null && StringUtils.isNotBlank(bmap.get("faddress").toString())) {
				eventDetailDTO.setAddress(bmap.get("faddress").toString());
			}
			if (bmap.get("fgps") != null && StringUtils.isNotBlank(bmap.get("fgps").toString())) {
				String gps = bmap.get("fgps").toString();
				eventDetailDTO.setGps(gps);
				if (StringUtils.isNotBlank(gps)) {
					String[] gpss = gps.split(",");
					if (gpss.length > 1) {
						eventDetailDTO.setGpsLng(gpss[0]);
						eventDetailDTO.setGpsLat(gpss[1]);
					}
				}
			}
		}

		/*eventDetailDTO.setEventId(eventId);
		eventDetailDTO.setEventTime(tEvent.getFeventTime());
		eventDetailDTO.setEventFocus(tEvent.getFfocus());
		if (StringUtils.isNotBlank(tEvent.getFage())) {
			eventDetailDTO.setAge(tEvent.getFage());
		} else {
			StringBuilder age = new StringBuilder().append("适合");
			if (tEvent.getFageA() != null && tEvent.getFageB() != null) {
				eventDetailDTO.setAge(
						age.append(tEvent.getFageA()).append("岁至").append(tEvent.getFageB()).append("岁的宝宝").toString());
			} else if (tEvent.getFageA() != null && tEvent.getFageB() == null) {
				eventDetailDTO.setAge(age.append(tEvent.getFageA()).append("岁以上的宝宝").toString());
			} else if (tEvent.getFageA() == null && tEvent.getFageB() != null) {
				eventDetailDTO.setAge(age.append(tEvent.getFageB()).append("岁以下的宝宝").toString());
			} else if (tEvent.getFageA() == null && tEvent.getFageB() == null) {
				eventDetailDTO.setAge("年龄不限");
			}
		}

		eventDetailDTO.setEventTitle(tEvent.getFtitle());
		eventDetailDTO.setEventSubTitle(tEvent.getFsubTitle());
		eventDetailDTO.setEventSubTitleImg(
				DictionaryUtil.getString(DictionaryUtil.EventSubtitleImg, tEvent.getFsubTitleImg()));
		eventDetailDTO.setScore(tEvent.getFscore().setScale(1, RoundingMode.HALF_UP).toString());
		// 获取活动推荐数缓存对象
		Cache eventRecommendCache = cacheManager.getCache(Constant.EventRecommend);
		Element ele = eventRecommendCache.get(eventId);
		if (ele != null) {
			EventRecommendDTO eventRecommendDTO = (EventRecommendDTO) ele.getObjectValue();
			eventDetailDTO.setRecommend(
					new StringBuilder().append("已有").append(eventRecommendDTO.getRecommend()).append("人推荐").toString());
		} else {
			eventDetailDTO.setRecommend(new StringBuilder().append("已有")
					.append(tEvent.getFrecommend() == null ? 0 : tEvent.getFrecommend()).append("人推荐").toString());
		}
		if (tEvent.getFduration() != null && tEvent.getFsiteType() != null) {
			eventDetailDTO
					.setSiteAndDuration(DictionaryUtil.getString(DictionaryUtil.EventDuration, tEvent.getFduration())
							+ " / " + DictionaryUtil.getString(DictionaryUtil.SiteType, tEvent.getFsiteType()));
		} else {
			eventDetailDTO
					.setSiteAndDuration(DictionaryUtil.getString(DictionaryUtil.EventDuration, tEvent.getFduration())
							+ DictionaryUtil.getString(DictionaryUtil.SiteType, tEvent.getFsiteType()));
		}
		if (StringUtils.isNotBlank(tEvent.getFimage2())) {
			eventDetailDTO.setImageUrls(fxlService.getImageUrls(tEvent.getFimage2(), false));
		} else {
			String imageUrl = fxlService.getImageUrl(tEvent.getFimage1(), false);
			eventDetailDTO.setImageUrls(ArrayUtils.toArray(imageUrl));
		}*/

		// 获取活动距离信息缓存对象
		Cache sessionIdEventDistanceCache = cacheManager.getCache(Constant.SessionIdEventDistance);
		Element element = sessionIdEventDistanceCache.get(sessionId);
		if (element != null) {
			Map<String, EventDistanceDTO> eventDistanceMap = (Map<String, EventDistanceDTO>) element.getObjectValue();
			EventDistanceDTO eventDistanceDTO = eventDistanceMap.get(eventId);
			if (eventDistanceDTO != null) {
				eventDetailDTO.setDistance(eventDistanceDTO.getDistanceString());
			}
		}

		TSponsor tSponsor = tEvent.getTSponsor();
		eventDetailDTO.setMerchantBrief(tSponsor.getFbrief());
		eventDetailDTO.setMerchantId(tSponsor.getId());
//		eventDetailDTO.setMerchantPhone(Constant.fxlServiceTel);
		if (StringUtils.isNotBlank(tSponsor.getFimage())) {
			eventDetailDTO.setMerchantLogoUrl(fxlService.getImageUrl(tSponsor.getFimage(), true));
		} else {
			eventDetailDTO.setMerchantLogoUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
					.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPicThumbnail.jpg").toString());
		}
		if (StringUtils.isNotBlank(tEvent.getFdetailHtmlUrl())) {
			eventDetailDTO.setDetailHtmlUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
					.append(PropertiesUtil.getProperty("htmlRootPath")).append(tEvent.getFdetailHtmlUrl()).toString());
		}
		eventDetailDTO.setMerchantName(tSponsor.getFname());
		eventDetailDTO.setOriginalPrice(tEvent.getFprice().toString());
	//	eventDetailDTO.setPresentPrice(tEvent.getFdeal());
		eventDetailDTO.setStatus(tEvent.getFstatus());
		eventDetailDTO.setStatusString(DictionaryUtil.getString(DictionaryUtil.EventStatus, tEvent.getFstatus()));
		eventDetailDTO.setCommentNum(commentDAO.getCommentCount(tEvent.getId(), 2));

		// eventDetailDTO.setSubscribeNum(String.valueOf(orderDAO.getOrderCountByEventId(tEvent.getId())));

		// 获取到活动类目缓存对象
		Cache eventCategoryCache = cacheManager.getCache(Constant.EventCategory);
		//ele = eventCategoryCache.get(tEvent.getFtypeA());
		//eventDetailDTO.setType(ele != null ? ele.getObjectValue().toString() : StringUtils.EMPTY);
		eventDetailDTO.setMerchantScore(tSponsor.getFscore().setScale(1, RoundingMode.HALF_UP).toString());
		eventDetailDTO.setMerchantUserScore(commentDAO.getMerchantCommentCount(tSponsor.getId(), 2));
		eventDetailDTO.setMerchantEventCount(eventDAO.getEventCountBySponsorId(tSponsor.getId()));

		boolean isHadTicket = true;
		CustomerDTO customerDTO = null;

		if (StringUtils.isNotBlank(ticket)) {
			customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
			if (customerDTO != null && customerDTO.isEnable()) {
				TFavorite tFavorite = favoriteDAO.getFavoriteEventByCustomerIdAndObejctId(customerDTO.getCustomerId(),
						eventId);
				if (tFavorite != null) {
					eventDetailDTO.setFavorite(true);
				} else {
					eventDetailDTO.setFavorite(false);
				}

				// 异步处理线程记录客户浏览了该活动记录
				CustomerAddEventBrowseBean caeb = new CustomerAddEventBrowseBean();
				caeb.setCustomerId(customerDTO.getCustomerId());
				caeb.setEventId(eventId);
				caeb.setTaskType(9);
				AsynchronousTasksManager.put(caeb);

			} else {
				eventDetailDTO.setFavorite(false);
				// 如果获取的用户DTO不是有效状态的，则将控制标示设置为false
				isHadTicket = false;
			}
		} else {
			eventDetailDTO.setFavorite(false);
			isHadTicket = false;
		}

		// 校验该活动是否有可领取优惠券活动
		List<CouponDTO> couponList = couponService.getCouponByEvent(customerDTO, tEvent, isHadTicket);

		// 返回活动类型
		TEventBargaining tEventBargaining = eventBargainingDAO.getByEventId(tEvent.getId());
		eventDetailDTO.setSaleType(tEvent.getFsalesType());
		if (tEventBargaining == null || tEventBargaining.getFstatus() != 20) {
			eventDetailDTO.setBargainingStatus(0);
		} else {
			eventDetailDTO.setBargainingStatus(1);
		}

		// 返回活动的咨询与评论总数
		eventDetailDTO.setCommentNum(commentDAO.findByEvent(eventId));
		eventDetailDTO.setConsultNum(consultDAO.findByEvent(eventId));

		// 返回活动推荐关联
		StringBuilder rql = new StringBuilder();
		Map<String, Object> rqlMap = Maps.newHashMap();
		rql.append(
				"select t.id as id, t.ftitle as ftitle, t.ftypeA as ftypeA ,t.feventTime as feventTime,t.fimage1 as fimage1, t.fprice as fprice, t.fdeal as fdeal, t.fsaleFlag as fsaleFlag,t.ftag as ftag, t.fstatus as fstatus, t.frecommend as frecommend, t.fscore as fscore,t.fsubTitle as fsubTitle,a.TAppChannelSetting.id as fchannelId from TEventRelation r")
				.append(" inner join TEvent t on r.fbyEventId = t.id")
				.append(" inner join TAppChannelEvent a on t.id = a.TEvent.id")
				.append(" where r.feventId= :eventId and t.fstatus < 999")
				.append(" GROUP BY t.id order by r.forder asc");
		rqlMap.put("eventId", eventId);
		List<Map<String, Object>> list = commonService.find(rql.toString(), rqlMap);

		try {
			if (null == list || list.size() == 0) {
				rql.delete(0, rql.length());
				rql.append(
						"select t.id as id, t.ftitle as ftitle, t.ftypeA as ftypeA ,t.feventTime as feventTime,t.fimage1 as fimage1, t.fprice as fprice, t.fdeal as fdeal, t.fsaleFlag as fsaleFlag,t.ftag as ftag, t.fstatus as fstatus, t.frecommend as frecommend, t.fscore as fscore,t.fsubTitle as fsubTitle,t.forderPrice as forderPrice, (select COUNT(r.id) from TOrder r where TO_DAYS(NOW()) - TO_DAYS(r.fcreateTime) <= 30 and r.fstatus in (20, 60, 70) and r.TEvent.id = t.id) as orderCount")
						.append(" from TEvent t where t.ftypeA = :ftypeA and t.fstatus=20 and t.id!= :eventId")
						.append(" order by orderCount desc, t.forderPrice asc,t.frecommend desc");

				rqlMap.clear();
				rqlMap.put("ftypeA", tEvent.getFtypeA());
				rqlMap.put("eventId", eventId);
				Query q2 = commonService.createQuery(rql.toString(), rqlMap);
				q2.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				q2.setFirstResult(1).setMaxResults(5);
				List<Map<String, Object>> list3 = q2.getResultList();
				TEventRelation db = null;
				int i = 1;
				for (Map<String, Object> amap2 : list3) {
					db = new TEventRelation();
					db.setFeventId(eventId);
					db.setFbyEventId(amap2.get("id").toString());
					db.setForder(i++);
					db.setFcreateTime(new Date());
					eventRelationDAO.save(db);
				}
				
				// 获取到活动类目缓存对象
				Cache eventCategoryCacher = cacheManager.getCache(Constant.EventCategory);
				// 获取活动推荐数缓存对象
				Cache eventRecommendCacher = cacheManager.getCache(Constant.EventRecommend);
				// 获取活动距离信息缓存对象
				Cache sessionIdEventDistanceCacher = cacheManager.getCache(Constant.SessionIdEventDistance);
				Element elemt = null;

				List<EventDTO> resultList = Lists.newArrayList();
				EventDTO eventDTO = null;
				int statusValue = 0;
				int stockFlag = 0;
				BigDecimal distance = null;
				for (Map<String, Object> amap : list3) {
					eventDTO = new EventDTO();
					eventDTO.setEventId(amap.get("id").toString());
					eventDTO.setEventTitle(amap.get("ftitle").toString());
					if (amap.get("fsubTitle") != null && StringUtils.isNotBlank(amap.get("fsubTitle").toString())) {
						eventDTO.setEventSubTitle(amap.get("fsubTitle").toString());
					}
					if (amap.get("ftypeA") != null && StringUtils.isNotBlank(amap.get("ftypeA").toString())) {
						elemt = eventCategoryCacher.get((Integer) amap.get("ftypeA"));
						eventDTO.setType(elemt != null ? elemt.getObjectValue().toString() : StringUtils.EMPTY);
					}
					if (amap.get("feventTime") != null && StringUtils.isNotBlank(amap.get("feventTime").toString())) {
						eventDTO.setEventTime(amap.get("feventTime").toString());
					}
					if (amap.get("fimage1") != null && StringUtils.isNotBlank(amap.get("fimage1").toString())) {
						eventDTO.setImageUrl(fxlService.getImageUrl(amap.get("fimage1").toString(), false));
					} else {
						eventDTO.setImageUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
								.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPic.jpg")
								.toString());
					}
					if (amap.get("fprice") != null && StringUtils.isNotBlank(amap.get("fprice").toString())) {
						eventDTO.setOriginalPrice(amap.get("fprice").toString());
					}
					if (amap.get("fdeal") != null && StringUtils.isNotBlank(amap.get("fdeal").toString())) {
						eventDTO.setPresentPrice(amap.get("fdeal").toString());
					} else {
						eventDTO.setPresentPrice("免费");
					}
					if (amap.get("fsaleFlag") != null && StringUtils.isNotBlank(amap.get("fsaleFlag").toString())) {
						stockFlag = ((Integer) amap.get("fsaleFlag")).intValue();
						if (stockFlag > 0) {
							eventDTO.setStockFlag(1);
						} else {
							eventDTO.setStockFlag(0);
						}
					} else {
						eventDTO.setStockFlag(0);
					}
					elemt = eventRecommendCacher.get(eventDTO.getEventId());
					if (elemt != null) {
						EventRecommendDTO eventRecommendDTO = (EventRecommendDTO) elemt.getObjectValue();
						eventDTO.setRecommend(eventRecommendDTO.getRecommend());
					} else if (amap.get("frecommend") != null
							&& StringUtils.isNotBlank(amap.get("frecommend").toString())) {
						eventDTO.setRecommend((Long) amap.get("frecommend"));
					}
					elemt = sessionIdEventDistanceCacher.get(sessionId);
					if (elemt != null) {
						Map<String, EventDistanceDTO> eventDistanceMap = (Map<String, EventDistanceDTO>) elemt
								.getObjectValue();
						EventDistanceDTO eventDistanceDTO = eventDistanceMap.get(eventDTO.getEventId());
						if (eventDistanceDTO != null) {
							eventDTO.setDistance(eventDistanceDTO.getDistanceString());
						}
					} else if (amap.get("fdistance") != null
							&& StringUtils.isNotBlank(amap.get("fdistance").toString())) {
						distance = new BigDecimal((Integer) amap.get("fdistance")).negate().divide(new BigDecimal(1000),
								1, RoundingMode.HALF_UP);
						eventDTO.setDistance(distance.toString() + "km");
					}
					if (amap.get("fscore") != null && StringUtils.isNotBlank(amap.get("fscore").toString())) {
						eventDTO.setScore((BigDecimal) amap.get("fscore"));
					}
					if (amap.get("fstatus") != null && StringUtils.isNotBlank(amap.get("fstatus").toString())) {
						statusValue = ((Integer) amap.get("fstatus")).intValue();
						eventDTO.setStatus(statusValue);
						eventDTO.setStatusString(DictionaryUtil.getString(DictionaryUtil.EventStatus, statusValue));
					}
					if (amap.get("fchannelId") != null && StringUtils.isNotBlank(amap.get("fchannelId").toString())) {
						eventDTO.setChannelId(amap.get("fchannelId").toString());
					}
					resultList.add(eventDTO);
					returnData.put("relationList", resultList);
				}
				
			}else {
				// 获取到活动类目缓存对象
				Cache eventCategoryCacher = cacheManager.getCache(Constant.EventCategory);
				// 获取活动推荐数缓存对象
				Cache eventRecommendCacher = cacheManager.getCache(Constant.EventRecommend);
				// 获取活动距离信息缓存对象
				Cache sessionIdEventDistanceCacher = cacheManager.getCache(Constant.SessionIdEventDistance);
				Element elemt = null;

				List<EventDTO> resultList = Lists.newArrayList();
				EventDTO eventDTO = null;
				int statusValue = 0;
				int stockFlag = 0;
				BigDecimal distance = null;
				for (Map<String, Object> amap : list) {
					eventDTO = new EventDTO();
					eventDTO.setEventId(amap.get("id").toString());
					eventDTO.setEventTitle(amap.get("ftitle").toString());
					if (amap.get("fsubTitle") != null && StringUtils.isNotBlank(amap.get("fsubTitle").toString())) {
						eventDTO.setEventSubTitle(amap.get("fsubTitle").toString());
					}
					if (amap.get("ftypeA") != null && StringUtils.isNotBlank(amap.get("ftypeA").toString())) {
						elemt = eventCategoryCacher.get((Integer) amap.get("ftypeA"));
						eventDTO.setType(elemt != null ? elemt.getObjectValue().toString() : StringUtils.EMPTY);
					}
					if (amap.get("feventTime") != null && StringUtils.isNotBlank(amap.get("feventTime").toString())) {
						eventDTO.setEventTime(amap.get("feventTime").toString());
					}
					if (amap.get("fimage1") != null && StringUtils.isNotBlank(amap.get("fimage1").toString())) {
						eventDTO.setImageUrl(fxlService.getImageUrl(amap.get("fimage1").toString(), false));
					} else {
						eventDTO.setImageUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
								.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPic.jpg").toString());
					}
					if (amap.get("fprice") != null && StringUtils.isNotBlank(amap.get("fprice").toString())) {
						eventDTO.setOriginalPrice(amap.get("fprice").toString());
					}
					if (amap.get("fdeal") != null && StringUtils.isNotBlank(amap.get("fdeal").toString())) {
						eventDTO.setPresentPrice(amap.get("fdeal").toString());
					} else {
						eventDTO.setPresentPrice("免费");
					}
					if (amap.get("fsaleFlag") != null && StringUtils.isNotBlank(amap.get("fsaleFlag").toString())) {
						stockFlag = ((Integer) amap.get("fsaleFlag")).intValue();
						if (stockFlag > 0) {
							eventDTO.setStockFlag(1);
						} else {
							eventDTO.setStockFlag(0);
						}
					} else {
						eventDTO.setStockFlag(0);
					}
					elemt = eventRecommendCacher.get(eventDTO.getEventId());
					if (elemt != null) {
						EventRecommendDTO eventRecommendDTO = (EventRecommendDTO) elemt.getObjectValue();
						eventDTO.setRecommend(eventRecommendDTO.getRecommend());
					} else if (amap.get("frecommend") != null && StringUtils.isNotBlank(amap.get("frecommend").toString())) {
						eventDTO.setRecommend((Long) amap.get("frecommend"));
					}
					elemt = sessionIdEventDistanceCacher.get(sessionId);
					if (elemt != null) {
						Map<String, EventDistanceDTO> eventDistanceMap = (Map<String, EventDistanceDTO>) elemt.getObjectValue();
						EventDistanceDTO eventDistanceDTO = eventDistanceMap.get(eventDTO.getEventId());
						if (eventDistanceDTO != null) {
							eventDTO.setDistance(eventDistanceDTO.getDistanceString());
						}
					} else if (amap.get("fdistance") != null && StringUtils.isNotBlank(amap.get("fdistance").toString())) {
						distance = new BigDecimal((Integer) amap.get("fdistance")).negate().divide(new BigDecimal(1000), 1,
								RoundingMode.HALF_UP);
						eventDTO.setDistance(distance.toString() + "km");
					}
					if (amap.get("fscore") != null && StringUtils.isNotBlank(amap.get("fscore").toString())) {
						eventDTO.setScore((BigDecimal) amap.get("fscore"));
					}
					if (amap.get("fstatus") != null && StringUtils.isNotBlank(amap.get("fstatus").toString())) {
						statusValue = ((Integer) amap.get("fstatus")).intValue();
						eventDTO.setStatus(statusValue);
						eventDTO.setStatusString(DictionaryUtil.getString(DictionaryUtil.EventStatus, statusValue));
					}
					if (amap.get("fchannelId") != null && StringUtils.isNotBlank(amap.get("fchannelId").toString())) {
						eventDTO.setChannelId(amap.get("fchannelId").toString());
					}
					resultList.add(eventDTO);
					returnData.put("relationList", resultList);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		returnData.put("eventDetail", eventDetailDTO);
		returnData.put("couponList", couponList);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public int updateCalendarDay() {
		Date now = new Date();
		Date yesterdayDate = DateUtils.addDays(now, -1);
		int yesterday = Integer.valueOf(DateFormatUtils.format(yesterdayDate, "yyyyMMdd")).intValue();
		// TODO 先创建从今天到未来15个月，即15个月
		int future = Integer.valueOf(DateFormatUtils.format(DateUtils.addMonths(yesterdayDate, 15), "yyyyMMdd"))
				.intValue();
		return calendarDAO.updateCalendarDay(yesterday, future);
	}

	public void initCalendarDay() {
		Date now = new Date();
		// TODO 先创建从今天到未来15个月，即15个月
		Date twoYear = DateUtils.addMonths(now, 15);

		int[] dayArray = ArrayUtils.EMPTY_INT_ARRAY;
		int temp = 0;

		// 如果起日期小于等于止日期则继续循环
		while (DateUtils.truncatedCompareTo(now, twoYear, Calendar.DAY_OF_MONTH) < 0) {
			temp = Integer.valueOf(DateFormatUtils.format(now, "yyyyMMdd")).intValue();
			dayArray = ArrayUtils.add(dayArray, temp);
			now = DateUtils.addDays(now, 1);
		}

		List<TCalendar> calendarList = calendarDAO.findAll();
		for (TCalendar tCalendar : calendarList) {
			temp = tCalendar.getEventDate().intValue();
			if (ArrayUtils.contains(dayArray, temp)) {
				dayArray = ArrayUtils.removeElement(dayArray, temp);
			}
		}

		Date now2 = new Date();
		List<TCalendar> newCalendarList = Lists.newArrayList();
		TCalendar newCalendar = null;
		for (int i = 0; i < dayArray.length; i++) {
			newCalendar = new TCalendar();
			newCalendar.setCityId(1);
			newCalendar.setEventNum(0);
			newCalendar.setEventDate(dayArray[i]);
			newCalendar.setUpdateTime(now2);
			newCalendarList.add(newCalendar);
		}
		if (newCalendarList.size() > 0) {
			calendarDAO.save(newCalendarList);
		}
	}

	@Transactional(readOnly = true)
	public void initEventStockCache() {
		Cache eventStockCache = cacheManager.getCache(Constant.EventStock);

		StringBuilder hql = new StringBuilder();
		hql.append("select t.id as eventId from TEvent t where t.fstatus = 20");
		List<Map<String, Object>> list = commonService.find(hql.toString());
		String eventId = null;
		Integer stock = null;
		for (Map<String, Object> amap : list) {
			eventId = amap.get("eventId").toString();
			stock = eventSpecDAO.getSumStock(eventId);
			Element element = new Element(eventId, stock);
			eventStockCache.put(element);
		}
	}

	@Transactional(readOnly = true)
	public void updateEventStockCache(String eventId) {
		Cache eventStockCache = cacheManager.getCache(Constant.EventStock);

		StringBuilder hql = new StringBuilder();
		hql.append("select t.id as eventId from TEvent t where t.id = :eventId");
		Map<String, Object> hqlMap = Maps.newHashMap();
		hqlMap.put("eventId", eventId);
		List<Map<String, Object>> list = commonService.find(hql.toString());

		Integer stock = null;
		for (Map<String, Object> amap : list) {
			eventId = amap.get("eventId").toString();
			stock = eventSpecDAO.getSumStock(eventId);
			Element element = new Element(eventId, stock);
			eventStockCache.put(element);
		}
	}

	@Transactional(readOnly = true)
	public void initRecommendCache() {
		// 读取活动推荐数保存在缓存中
		StringBuilder hql = new StringBuilder();
		hql.append("select t.id as id, t.frecommend as frecommend from TEvent t where t.fstatus = 20");
		// Map<String, Object> hqlMap = Maps.newHashMap();
		// hqlMap.put("quotaList", Constant.defaultHeadImgUrl)
		Cache eventRecommendCache = cacheManager.getCache(Constant.EventRecommend);
		eventRecommendCache.removeAll(true);
		Element ele = null;

		List<Map<String, Object>> list = commonService.find(hql.toString());
		EventRecommendDTO eventRecommendDTO = null;
		String id = null;
		for (Map<String, Object> amap : list) {
			eventRecommendDTO = new EventRecommendDTO();
			id = amap.get("id").toString();
			eventRecommendDTO.setEventId(id);
			if (amap.get("frecommend") != null && StringUtils.isNotBlank(amap.get("frecommend").toString())) {
				eventRecommendDTO.setRecommend((Long) amap.get("frecommend"));
			}
			ele = new Element(id, eventRecommendDTO);
			eventRecommendCache.put(ele);
		}
		// 读取文章推荐数保存在缓存中
		hql.delete(0, hql.length());
		hql.append("select t.id as id, t.frecommend as frecommend from TArticle t where t.fstatus = 20");
		Cache articleRecommendCache = cacheManager.getCache(Constant.ArticleRecommend);
		articleRecommendCache.removeAll(true);

		list = commonService.find(hql.toString());
		ArticleRecommendDTO articleRecommendDTO = null;
		for (Map<String, Object> amap : list) {
			articleRecommendDTO = new ArticleRecommendDTO();
			id = amap.get("id").toString();
			articleRecommendDTO.setArticleId(id);
			if (amap.get("frecommend") != null && StringUtils.isNotBlank(amap.get("frecommend").toString())) {
				articleRecommendDTO.setRecommend((Long) amap.get("frecommend"));
			}
			ele = new Element(id, articleRecommendDTO);
			articleRecommendCache.put(ele);
		}
		// 读取评论推荐数保存在缓存中
		hql.delete(0, hql.length());
		hql.append("select t.id as id, t.frecommend as frecommend from TComment t where t.fstatus = 20");
		Cache commentRecommendCache = cacheManager.getCache(Constant.CommentRecommend);
		commentRecommendCache.removeAll(true);

		list = commonService.find(hql.toString());
		CommentRecommendDTO commentRecommendDTO = null;
		for (Map<String, Object> amap : list) {
			commentRecommendDTO = new CommentRecommendDTO();
			id = amap.get("id").toString();
			commentRecommendDTO.setCommentId(id);
			if (amap.get("frecommend") != null && StringUtils.isNotBlank(amap.get("frecommend").toString())) {
				commentRecommendDTO.setRecommend((Long) amap.get("frecommend"));
			}
			ele = new Element(id, commentRecommendDTO);
			commentRecommendCache.put(ele);
		}
	}

	/**
	 * 将活动推荐数缓存同步到数据库中
	 * 
	 * @return
	 */
	public int savingEventRecommendCache() {
		Cache eventRecommendCache = cacheManager.getCache(Constant.EventRecommend);
		Map<Object, Element> recommendMap = eventRecommendCache.getAll(eventRecommendCache.getKeysNoDuplicateCheck());
		int i = 0;
		for (Iterator it = recommendMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Object, Element> e = (Map.Entry<Object, Element>) it.next();
			Element ele = e.getValue();
			EventRecommendDTO eventRecommendDTO = (EventRecommendDTO) ele.getObjectValue();
			if (eventRecommendDTO.isChange()) {
				eventDAO.updateEventRecommend(eventRecommendDTO.getEventId(), eventRecommendDTO.getRecommend());
				eventRecommendDTO.setChange(false);
				i++;
			}
		}
		return i;
	}

	/**
	 * 推荐活动的service方法
	 * 
	 * @param eventId
	 *            活动ID
	 * @return 响应用户的json数据
	 */
	public ResponseDTO clickEventRecommend(String ticket, String eventId, Integer clientType) {
		ResponseDTO responseDTO = new ResponseDTO();

		// if (StringUtils.isBlank(ticket)) {
		// responseDTO.setSuccess(false);
		// responseDTO.setStatusCode(201);
		// responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
		// return responseDTO;
		// }

		if (StringUtils.isBlank(eventId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("eventId参数不能为空，请检查eventId的传递参数值！");
			return responseDTO;
		}

		Map<String, Object> returnData = Maps.newHashMap();
		Cache eventRecommendCache = cacheManager.getCache(Constant.EventRecommend);
		Element ele = eventRecommendCache.get(eventId);
		if (ele == null) {
			Long recommend = eventDAO.findEventRecommend(eventId);
			EventRecommendDTO eventRecommendDTO = new EventRecommendDTO();
			eventRecommendDTO.setEventId(eventId);
			eventRecommendDTO.setRecommend(recommend == null ? 1 : recommend + 1);

			ele = new Element(eventId, eventRecommendDTO);
			eventRecommendCache.put(ele);

			returnData.put("total", eventRecommendDTO.getRecommend());
			returnData.put("totalInfo",
					new StringBuilder().append("已有").append(eventRecommendDTO.getRecommend()).append("人推荐").toString());
		} else {
			EventRecommendDTO eventRecommendDTO = (EventRecommendDTO) ele.getObjectValue();
			eventRecommendDTO.addOne();
			returnData.put("total", eventRecommendDTO.getRecommend());
			returnData.put("totalInfo",
					new StringBuilder().append("已有").append(eventRecommendDTO.getRecommend()).append("人推荐").toString());
		}

		// 判断ticket是否真正存在的如果不存在则返回false
		if (StringUtils.isNotBlank(ticket)) {
			CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
			if (!customerDTO.isEnable()) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(customerDTO.getStatusCode());
				responseDTO.setMsg(customerDTO.getMsg());
				return responseDTO;
			}

			// 异步线程处理添加点赞人的id
			CustomerAddEventRecommendBean car = new CustomerAddEventRecommendBean();
			car.setCustomerId(customerDTO.getCustomerId());
			car.setEventId(eventId);
			car.setTaskType(7);
			AsynchronousTasksManager.put(car);

			// 异步处理线程记录用户喜欢的活动type为 1
			CustomerLikesDislikesBean customerLikesDislikesBean = new CustomerLikesDislikesBean();
			customerLikesDislikesBean.setCustomerId(customerDTO.getCustomerId());
			customerLikesDislikesBean.setEventId(eventId);
			customerLikesDislikesBean.setType(1);
			customerLikesDislikesBean.setTaskType(8);
			AsynchronousTasksManager.put(customerLikesDislikesBean);

		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setData(returnData);
		responseDTO.setMsg("活动推荐点赞成功！");
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getCategoryList() {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();

		Cache eventCategoryCache = cacheManager.getCache(Constant.EventCategory);
		Map<Object, Element> categoryMap = eventCategoryCache.getAll(eventCategoryCache.getKeysNoDuplicateCheck());

		List<Map<String, Object>> list = Lists.newArrayList();
		Map<String, Object> map = Maps.newHashMap();
		map.put("id", 0);
		map.put("name", "全部");
		list.add(map);
		for (Iterator it = categoryMap.entrySet().iterator(); it.hasNext();) {
			map = Maps.newHashMap();
			Map.Entry<Object, Element> e = (Map.Entry<Object, Element>) it.next();
			map.put("id", e.getKey());
			Element ele = e.getValue();
			map.put("name", ele.getObjectValue());
			list.add(map);
		}
		returnData.put("categoryList", list);

		list = Lists.newArrayList();
		Map<Integer, String> amap = DictionaryUtil.getStatueMap(DictionaryUtil.EventDuration);
		for (Iterator it = amap.entrySet().iterator(); it.hasNext();) {
			map = Maps.newHashMap();
			Map.Entry<Integer, String> e = (Map.Entry<Integer, String>) it.next();
			map.put("id", e.getKey());
			map.put("name", e.getValue());
			list.add(map);
		}
		returnData.put("durationList", list);

		list = Lists.newArrayList();
		amap = DictionaryUtil.getStatueMap(DictionaryUtil.SiteType);
		for (Iterator it = amap.entrySet().iterator(); it.hasNext();) {
			map = Maps.newHashMap();
			Map.Entry<Integer, String> e = (Map.Entry<Integer, String>) it.next();
			map.put("id", e.getKey());
			map.put("name", e.getValue());
			list.add(map);
		}
		returnData.put("siteList", list);

		// 将检索结果的活动类目再次整理出一个活动适合年龄list返回给前端
		List<Map<String, Object>> ageList = Lists.newArrayList();
		Map<Integer, String> ageMap = DictionaryUtil.getStatueMap(DictionaryUtil.EventAge);
		for (Iterator it = ageMap.entrySet().iterator(); it.hasNext();) {
			map = Maps.newHashMap();
			Map.Entry<Integer, String> e = (Map.Entry<Integer, String>) it.next();
			map.put("id", e.getKey());
			map.put("name", e.getValue());
			ageList.add(map);
		}
		returnData.put("ageList", ageList);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * 获取活动优惠券
	 * 
	 * @param eventId
	 *            活动ID
	 * @param ticket
	 *            票
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getCouponByEvent(Integer clientType, String ticket, String eventId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(eventId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("eventId参数不能为空，请检查eventId的传递参数值！");
			return responseDTO;
		}
		boolean isHadTicket = true;
		if (StringUtils.isBlank(ticket)) {
			isHadTicket = false;
		}

		CustomerDTO customerDTO = null;
		if (isHadTicket) {
			customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
			// 如果获取的用户DTO不是有效状态的，则将控制标示设置为false
			if (!customerDTO.isEnable()) {
				isHadTicket = false;
			}
		}
		// TODO 这个以后要改现在只能获取活动下的优惠券
		StringBuilder hql = new StringBuilder();
		hql.append(
				"SELECT t.id AS couponId,t.fuseStartTime AS useStartTime,t.fuseEndTime AS useEndTime,t.fuseRange AS useRange,t.flimitation AS limitation, ")
				.append(" t.famount AS amount,t.fcount AS count,t.fsendCount AS sendCount FROM TCoupon t INNER JOIN t.TCouponObjects f ")
				.append("WHERE f.fobjectId = :objectID AND t.fdeliverType = :deliverType AND f.fuseType = :fuseType AND t.fstatus = :status ");
		Map<String, Object> hqlMap = Maps.newHashMap();
		hqlMap.put("objectID", eventId);
		hqlMap.put("deliverType", 110);
		hqlMap.put("fuseType", 40);
		hqlMap.put("status", 40);

		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);
		List<CouponDTO> dataList = Lists.newArrayList();
		CouponDTO couponDTO = null;
		Date date = null;
		for (Map<String, Object> amap : list) {
			couponDTO = new CouponDTO();
			if (amap.get("couponId") != null && StringUtils.isNotBlank(amap.get("couponId").toString())) {
				couponDTO.setCouponId(amap.get("couponId").toString());
				if (isHadTicket) {
					// couponDTO.setReceive(
					// fxlService.isReceiveCoupon(amap.get("couponId").toString(),
					// customerDTO.getCustomerId()));
				}
			}
			if (amap.get("useStartTime") != null && StringUtils.isNotBlank(amap.get("useStartTime").toString())) {
				date = (Date) amap.get("useStartTime");
				couponDTO.setUseStartTime(DateFormatUtils.format(date, "yyyy-MM-dd"));
			}
			if (amap.get("useEndTime") != null && StringUtils.isNotBlank(amap.get("useEndTime").toString())) {
				date = (Date) amap.get("useEndTime");
				couponDTO.setUseEndTime(DateFormatUtils.format(date, "yyyy-MM-dd"));
			}
			if (amap.get("limitation") != null && StringUtils.isNotBlank(amap.get("limitation").toString())) {
				couponDTO.setLimitation((BigDecimal) amap.get("limitation"));
				couponDTO.setLimitationInfo("满" + amap.get("limitation") + "元可使用");
			} else {
				couponDTO.setLimitationInfo("直减" + amap.get("amount") + "元");
			}
			if (amap.get("amount") != null && StringUtils.isNotBlank(amap.get("amount").toString())) {
				couponDTO.setAmount((BigDecimal) amap.get("amount"));
			}
			if (amap.get("useRange") != null && StringUtils.isNotBlank(amap.get("useRange").toString())) {
				couponDTO.setUseRange(amap.get("useRange").toString());
				couponDTO.setLimitationClient(amap.get("useRange").toString());
			}
			if (amap.get("sendCount") != null && StringUtils.isNotBlank(amap.get("sendCount").toString())
					&& amap.get("count") != null && StringUtils.isNotBlank(amap.get("count").toString())) {
				if ((Integer) amap.get("count") > 0 && (Integer) amap.get("count") <= (Integer) amap.get("sendCount")) {
					couponDTO.setReceiveFinish(true);
				}
			}
			dataList.add(couponDTO);
		}
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("couponList", dataList);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setData(returnData);
		responseDTO.setMsg("");
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getSilderListBySpecialTopic(String name, Integer city, HttpServletRequest request) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();
		if (StringUtils.isBlank(name)) {
			returnData.put("list", "");
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			responseDTO.setData(returnData);
			responseDTO.setMsg("");
			return responseDTO;
		}
		Map<Integer, String> cityMap = DictionaryUtil.getStatueMap(DictionaryUtil.City);

		// TODO 找不到城市默认为北京这里后面的城市id需要写活
		if (!cityMap.containsKey(city)) {
			city = 1;
		}

		StringBuilder code = new StringBuilder();
		code.append(name).append("_index_").append(city);
		StringBuilder hql = new StringBuilder();
		hql.append(
				"SELECT f.fimage AS fimage,f.fentityTitle AS fentityTitle,f.fentityId AS fentityId,f.fsliderText AS fsliderText, ")
				.append(" f.fsliderType AS fsliderType,f.furlType AS furlType,f.fexternalUrl AS fexternalUrl FROM TAppChannelSetting t INNER JOIN t.TAppChannelSliders f ")
				.append("WHERE t.fcode = :code AND t.fcity = :fcity AND t.fisVisible = :fisVisible AND f.fisVisible = :fisVisible AND t.fisSlider = :fisSlider");
		Map<String, Object> hqlMap = Maps.newHashMap();
		hqlMap.put("code", code.toString());
		hqlMap.put("fcity", city);
		hqlMap.put("fisVisible", 1);
		hqlMap.put("fisSlider", 1);
		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);
		List<AppChannelSliderDTO> silderList = Lists.newArrayList();
		List<AppChannelSliderDTO> fontSilderList = Lists.newArrayList();
		AppChannelSliderDTO appChannelSliderDTO = null;
		for (Map<String, Object> amap : list) {
			appChannelSliderDTO = new AppChannelSliderDTO();
			if (amap.get("furlType") != null && StringUtils.isNotBlank(amap.get("furlType").toString())
					&& amap.get("fentityId") != null && StringUtils.isNotBlank(amap.get("fentityId").toString())) {
				if ((Integer) amap.get("furlType") == 1) {
					appChannelSliderDTO.setLinkUrl(new StringBuilder().append(request.getScheme()).append("://")
							.append(request.getServerName()).append(":").append(request.getServerPort())
							.append(request.getContextPath()).append("/api/system/share/event/")
							.append(amap.get("fentityId")).toString());
				} else if ((Integer) amap.get("furlType") == 2) {
					appChannelSliderDTO.setLinkUrl(new StringBuilder().append(request.getScheme()).append("://")
							.append(request.getServerName()).append(":").append(request.getServerPort())
							.append(request.getContextPath()).append("/api/system/share/merchant/")
							.append(amap.get("fentityId")).toString());
				} else if ((Integer) amap.get("furlType") == 3) {
					// 专题链接
					TSubject tSubject = subjectDAO.getOne(amap.get("fentityId").toString());
					appChannelSliderDTO.setLinkUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
							.append(PropertiesUtil.getProperty("htmlRootPath")).append(tSubject.getFdetailHtmlUrl())
							.toString());
				} else if ((Integer) amap.get("furlType") == 5) {
					if (amap.get("fexternalUrl") != null
							&& StringUtils.isNotBlank(amap.get("fexternalUrl").toString())) {
						appChannelSliderDTO.setLinkUrl(amap.get("fexternalUrl").toString());
					}
				}
				appChannelSliderDTO.setUrlType((Integer) amap.get("furlType"));
				appChannelSliderDTO.setEntityId(amap.get("fentityId").toString());
			}

			if (amap.get("fsliderType") != null && StringUtils.isNotBlank(amap.get("fsliderType").toString())) {
				if ((Integer) amap.get("fsliderType") == 1) {// 轮播内容是图片
					if (amap.get("fimage") != null && StringUtils.isNotBlank(amap.get("fimage").toString())) {
						appChannelSliderDTO.setImageUrl(fxlService.getImageUrl(amap.get("fimage").toString(), false));
					}
					if (amap.get("fentityTitle") != null
							&& StringUtils.isNotBlank(amap.get("fentityTitle").toString())) {
						appChannelSliderDTO.setEntityTitle(amap.get("fentityTitle").toString());
					}
					silderList.add(appChannelSliderDTO);
				} else if ((Integer) amap.get("fsliderType") == 2) {// 轮播内容是文字
					if (amap.get("fsliderText") != null && StringUtils.isNotBlank(amap.get("fsliderText").toString())) {
						appChannelSliderDTO.setEntityTitle(amap.get("fsliderText").toString());
					}
					fontSilderList.add(appChannelSliderDTO);
				}
			}
		}

		returnData.put("silderList", silderList);
		returnData.put("fontSilderList", fontSilderList);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setData(returnData);
		responseDTO.setMsg("");
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getDataListBySpecialTopic(String name, Integer city, HttpServletRequest request,
			Integer pageSize, Integer offset) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();
		if (StringUtils.isBlank(name)) {
			returnData.put("list", "");
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			responseDTO.setData(returnData);
			responseDTO.setMsg("");
			return responseDTO;
		}
		Map<Integer, String> cityMap = DictionaryUtil.getStatueMap(DictionaryUtil.City);

		// TODO 找不到城市默认为北京这里后面的城市id需要写活
		if (!cityMap.containsKey(city)) {
			city = 1;
		}

		StringBuilder code = new StringBuilder();
		code.append(name).append("_index_").append(city);
		int fIsVisible = 1;
		TAppChannelSetting appChannelSetting = appChannelSettingDAO.getAppChannelSetting(code.toString(), city,
				fIsVisible);
		if (appChannelSetting == null) {
			returnData.put("list", "");
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			responseDTO.setData(returnData);
			responseDTO.setMsg("");
			return responseDTO;
		}
		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}

		StringBuilder hql = new StringBuilder();
		hql.append(
				"SELECT DISTINCT(t.id) as id,t.faddress AS faddress,t.fscore AS fscore,t.fname AS fname,t.fimage AS fimage ")
				.append(" FROM TSponsor t LEFT JOIN t.TEvents f  LEFT JOIN f.TAppChannels c WHERE c.TAppChannelSetting.id = :id ")
				.append(" AND t.fstatus = :fstatus AND f.fstatus = :eventStatus ORDER BY c.forder DESC ");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("id", appChannelSetting.getId());
		hqlMap.put("fstatus", 1);
		hqlMap.put("eventStatus", 20);

		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();
		List<MerchantByChannelDTO> dataList = Lists.newArrayList();
		MerchantByChannelDTO merchantByChannelDTO = null;
		EventByChannelDTO eventByChannelDTO = null;
		for (Map<String, Object> amap : list) {
			merchantByChannelDTO = new MerchantByChannelDTO();

			if (amap.get("id") != null && StringUtils.isNotBlank(amap.get("id").toString())) {
				merchantByChannelDTO.setMerchantId(amap.get("id").toString());
				merchantByChannelDTO.setLinkUrl(
						new StringBuilder().append(request.getScheme()).append("://").append(request.getServerName())
								.append(":").append(request.getServerPort()).append(request.getContextPath())
								.append("/api/system/share/merchant/").append(amap.get("id")).toString());
			}
			if (amap.get("faddress") != null && StringUtils.isNotBlank(amap.get("faddress").toString())) {
				merchantByChannelDTO.setAddress(amap.get("faddress").toString());
			}
			if (amap.get("fscore") != null && StringUtils.isNotBlank(amap.get("fscore").toString())) {
				merchantByChannelDTO.setScore((BigDecimal) amap.get("fscore"));
			}
			if (amap.get("fname") != null && StringUtils.isNotBlank(amap.get("fname").toString())) {
				merchantByChannelDTO.setMerchantName(amap.get("fname").toString());
			}
			if (amap.get("fimage") != null && StringUtils.isNotBlank(amap.get("fimage").toString())) {
				merchantByChannelDTO.setMerchantLogoUrl(fxlService.getImageUrl(amap.get("fimage").toString(), true));
			} else {
				merchantByChannelDTO.setMerchantLogoUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
						.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPicThumbnail.jpg")
						.toString());
			}

			StringBuilder ehql = new StringBuilder();
			ehql.append(
					"SELECT t.ftitle AS ftitle,t.feventTime AS feventTime,t.fsubTitle AS fsubTitle,t.fimage1 AS fimage1, ")
					.append(" t.fdeal AS fdeal,t.id AS id,t.fsaleFlag AS fsaleFlag FROM TEvent t LEFT JOIN t.TAppChannels f LEFT JOIN ")
					.append(" t.TSponsor s WHERE s.id = :sid AND f.TAppChannelSetting.id = :cid AND t.fstatus = :status ");
			Map<String, Object> ehqlMap = Maps.newHashMap();
			ehqlMap.put("sid", amap.get("id").toString());
			ehqlMap.put("cid", appChannelSetting.getId());
			ehqlMap.put("status", 20);
			List<Map<String, Object>> elist = commonService.find(ehql.toString(), ehqlMap);

			List<EventByChannelDTO> eventByChannelDTOList = Lists.newArrayList();
			for (Map<String, Object> bmap : elist) {
				eventByChannelDTO = new EventByChannelDTO();
				if (bmap.get("ftitle") != null && StringUtils.isNotBlank(bmap.get("ftitle").toString())) {
					eventByChannelDTO.setTitle(bmap.get("ftitle").toString());
				}
				if (bmap.get("feventTime") != null && StringUtils.isNotBlank(bmap.get("feventTime").toString())) {
					eventByChannelDTO.setEventTime(bmap.get("feventTime").toString());
				}
				if (bmap.get("fsubTitle") != null && StringUtils.isNotBlank(bmap.get("fsubTitle").toString())) {
					eventByChannelDTO.setSubTitle(bmap.get("fsubTitle").toString());
				}
				if (bmap.get("fimage1") != null && StringUtils.isNotBlank(bmap.get("fimage1").toString())) {
					eventByChannelDTO.setImageUrl(fxlService.getImageUrl(bmap.get("fimage1").toString(), false));
				}
				if (bmap.get("fdeal") != null && StringUtils.isNotBlank(bmap.get("fdeal").toString())) {
					eventByChannelDTO.setDeal(bmap.get("fdeal").toString());
				}
				if (bmap.get("id") != null && StringUtils.isNotBlank(bmap.get("id").toString())) {
					eventByChannelDTO.setEventId(bmap.get("id").toString());
					eventByChannelDTO.setLinkUrl(new StringBuilder().append(request.getScheme()).append("://")
							.append(request.getServerName()).append(":").append(request.getServerPort())
							.append(request.getContextPath()).append("/api/system/share/event/").append(bmap.get("id"))
							.toString());
				}

				if (bmap.get("fsaleFlag") != null && StringUtils.isNotBlank(bmap.get("fsaleFlag").toString())) {
					int i = ((Integer) bmap.get("fsaleFlag")).intValue();
					if (i <= 0) {
						eventByChannelDTO.setSaleFlag(false);
					}
				}

				eventByChannelDTOList.add(eventByChannelDTO);
			}
			merchantByChannelDTO.setEventList(eventByChannelDTOList);
			dataList.add(merchantByChannelDTO);
		}
		returnData.put("channelDataList", dataList);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);

		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);
		responseDTO.setMsg("");
		return responseDTO;
	}

	/**
	 * 记录用户点赞的id
	 * 
	 * @param customerId
	 * @param eventId
	 * @return
	 */
	public void addEventRecommend(CustomerAddEventRecommendBean cce) {

		TCustomerRecommend recommend = customerRecommendDAO.getByCustomerIdAndEventId(cce.getCustomerId(),
				cce.getEventId());

		TCustomerRecommend tcustomerRecommend = null;
		if (recommend == null) {
			tcustomerRecommend = new TCustomerRecommend();
			tcustomerRecommend.setFcustomerId(cce.getCustomerId());
			tcustomerRecommend.setFeventId(cce.getEventId());
			tcustomerRecommend.setFcreateTime(new Date());
			customerRecommendDAO.save(tcustomerRecommend);
		}
	}
}