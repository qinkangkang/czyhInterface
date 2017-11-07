package com.innee.czyhInterface.service.v2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.internal.QueryImpl;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.Exceptions;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.innee.czyhInterface.dao.CustomerBabyDAO;
import com.innee.czyhInterface.dao.CustomerEventBrowseDAO;
import com.innee.czyhInterface.dao.CustomerEventRecommendDAO;
import com.innee.czyhInterface.dao.CustomerFeedbackDAO;
import com.innee.czyhInterface.dao.CustomerInfoDAO;
import com.innee.czyhInterface.dao.CustomerLikesDislikesDAO;
import com.innee.czyhInterface.dto.CustomerDTO;
import com.innee.czyhInterface.dto.EventDistanceDTO;
import com.innee.czyhInterface.dto.EventRecommendDTO;
import com.innee.czyhInterface.dto.m.CustomerBabyDTO;
import com.innee.czyhInterface.dto.m.EventDTO;
import com.innee.czyhInterface.dto.m.PageDTO;
import com.innee.czyhInterface.dto.m.RecommendedRankingDTO;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.entity.TCustomerBaby;
import com.innee.czyhInterface.entity.TCustomerEventBrowse;
import com.innee.czyhInterface.entity.TCustomerEventRecommend;
import com.innee.czyhInterface.entity.TCustomerFeedback;
import com.innee.czyhInterface.entity.TCustomerInfo;
import com.innee.czyhInterface.entity.TCustomerLikesDislikes;
import com.innee.czyhInterface.exception.ServiceException;
import com.innee.czyhInterface.util.CommonPage;
import com.innee.czyhInterface.util.Constant;
import com.innee.czyhInterface.util.DictionaryUtil;
import com.innee.czyhInterface.util.PropertiesUtil;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.CustomerAddEventBrowseBean;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.CustomerLikesDislikesBean;
import com.innee.czyhInterface.util.personalized.Personalized2Comparator;
import com.innee.czyhInterface.util.personalized.PersonalizedComparator;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

@Component
@Transactional
public class PersonalizedService {

	private static final Logger logger = LoggerFactory.getLogger(PersonalizedService.class);

	// private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private CommonService commonService;

	@Autowired
	private FxlService fxlService;

	@Autowired
	private CustomerInfoDAO customerInfoDAO;

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private CustomerBabyDAO customerBabyDAO;

	@Autowired
	private CustomerEventRecommendDAO customerEventRecommendDAO;

	@Autowired
	private CustomerLikesDislikesDAO customerLikesDislikesDAO;

	@Autowired
	private CustomerEventBrowseDAO customerEventBrowseDAO;

	@PersistenceContext
	protected EntityManager em;

	@Autowired
	private CustomerFeedbackDAO customerFeedbackDAO;

	@Transactional(readOnly = true)
	public ResponseDTO getStatusRecommend(String ticket, Integer clientType) {
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

		String customerId = fxlService.getCustomerIdByTicket(ticket, clientType);

		TCustomerBaby customerBaby = customerBabyDAO.getByCustomerId(customerId);

		TCustomerInfo customerInfo = customerInfoDAO.getByCustomerId(customerId);
		// 如果宝贝信息不为空则加载列表
		if (customerBaby != null) {
			CustomerBabyDTO customerBabyDTO = new CustomerBabyDTO();
			if (StringUtils.isNotBlank(customerBaby.getFlikeType())) {
				ResponseDTO dto = this.getInterestsList(null);
				List<String> dtoList = (List<String>) dto.getData().get("interestsList");
				String[] likeType = customerBaby.getFlikeType().split(";");
				List<String> list = new ArrayList<String>(Arrays.asList(likeType));
				// list.retainAll(dtoList);
				List<String> listlike = ListUtils.intersection(list, dtoList);
				if (CollectionUtils.isNotEmpty(listlike)) {
					StringBuilder FlikeType = new StringBuilder();
					for (int i = 0; i < listlike.size(); i++) {
						FlikeType.append(listlike.get(i)).append(";");
					}
					customerBabyDTO.setFlikeType(FlikeType.toString().substring(0, FlikeType.length() - 1));
				} else {
					responseDTO.setSuccess(true);
					responseDTO.setStatusCode(0);
					Map<String, Object> returnData = Maps.newHashMap();
					returnData.put("isNullList", true);
					responseDTO.setMsg("当前用户已经填写过推荐信息,请加载推荐活动列表！");
					responseDTO.setData(returnData);
					return responseDTO;
				}
			} else {
				customerBabyDTO.setFlikeType(customerBaby.getFlikeType());
			}
			customerBabyDTO.setSex(customerBaby.getFsex());
			customerBabyDTO.setAge(customerBaby.getFage());

			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			responseDTO.setMsg("该用户存在宝宝信息！");
			Map<String, Object> returnData = Maps.newHashMap();
			returnData.put("customerBabyDTO", customerBabyDTO);
			returnData.put("isNullList", true);
			responseDTO.setData(returnData);
			return responseDTO;
		}

		if (customerInfo.getFtipNumber().intValue() == 0) {
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			Map<String, Object> returnData = Maps.newHashMap();
			returnData.put("isNullList", false);
			responseDTO.setMsg("当前用户第一次填写推荐信息,请跳转到活动信息填写界面！");
			responseDTO.setData(returnData);
			return responseDTO;
		} else {
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			Map<String, Object> returnData = Maps.newHashMap();
			returnData.put("isNullList", true);
			responseDTO.setMsg("当前用户已经填写过推荐信息,请加载推荐活动列表！");
			responseDTO.setData(returnData);
			return responseDTO;
		}
	}

	public ResponseDTO getUserRecommendList(String ticket, Integer clientType, Integer offset, Integer pageSize,
			String sessionId) {
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

		String customerId = fxlService.getCustomerIdByTicket(ticket, clientType);

		// 调用存储过程为用户添加信息
		try {
			StringBuilder sql = new StringBuilder();
			Query q = null;
			sql.append("{call proc_recommend_byCustomerID(?)}");
			q = em.createNativeQuery(sql.toString());
			q.setParameter(1, customerId);
			int ls = q.executeUpdate();

			logger.info("ls is " + ls);
			logger.info("customerId is " + customerId);
			logger.info("执行了 proc_recommend_byCustomerID 存储过程");
			if (ls == -1) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(202);
				responseDTO.setMsg("执行存储过程出错！");
				return responseDTO;
			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			throw new ServiceException("调用存储过程时发声错误,请检查存储过程！");
		}

		// 添加完成后不用用户再次填写推荐信息
		customerInfoDAO.updateTipNumber(customerId, 1);

		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}

		StringBuilder hql = new StringBuilder();

		hql.append(
				"select t.id as id, t.ftitle as ftitle, t.ftypeA as ftypeA ,t.feventTime as feventTime, t.fimage1 as fimage1, t.fprice as fprice, t.fdeal as fdeal, t.fsaleFlag as fsaleFlag, t.ftag as ftag, t.fstatus as fstatus, t.frecommend as frecommend, t.fscore as fscore,t.fsubTitle as fsubTitle from TEvent t left join TCustomerEventRecommend as r on t.id = r.feventId where r.fcustomerId=:fcustomerId and t.fstatus < 999");

		Map<String, Object> hqlMap = Maps.newHashMap();
		hqlMap.put("fcustomerId", customerId);
		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();
		List<EventDTO> recommendList = Lists.newArrayList();

		// 获取到活动类目缓存对象
		Cache eventCategoryCache = cacheManager.getCache(Constant.EventCategory);
		// 获取活动推荐数缓存对象
		Cache eventRecommendCache = cacheManager.getCache(Constant.EventRecommend);
		// 获取活动距离信息缓存对象
		Cache sessionIdEventDistanceCache = cacheManager.getCache(Constant.SessionIdEventDistance);
		Element ele = null;
		EventDTO eventDTO = null;
		int statusValue = 0;
		int stockFlag = 0;

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
					eventDTO.setGps(eventDistanceDTO.getGps());
					eventDTO.setGpsLng(eventDistanceDTO.getGpsLng());
					eventDTO.setGpsLat(eventDistanceDTO.getGpsLat());
				}
			}

			if (amap.get("fscore") != null && StringUtils.isNotBlank(amap.get("fscore").toString())) {
				eventDTO.setScore((BigDecimal) amap.get("fscore"));
			}
			if (amap.get("fstatus") != null && StringUtils.isNotBlank(amap.get("fstatus").toString())) {
				statusValue = ((Integer) amap.get("fstatus")).intValue();
				eventDTO.setStatus(statusValue);
				eventDTO.setStatusString(DictionaryUtil.getString(DictionaryUtil.EventStatus, statusValue));
			}

			recommendList.add(eventDTO);

		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		returnData.put("recommendList", recommendList);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public ResponseDTO addUserRecommend(String ticket, Integer sex, Integer age, Integer clientType, String interest) {
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

		String customerId = fxlService.getCustomerIdByTicket(ticket, clientType);

		TCustomerBaby baby = customerBabyDAO.getByCustomerId(customerId);

		if (baby != null) {
			baby.setFcustomerId(customerId);
			baby.setFsex(sex);
			baby.setFage(age);
			baby.setFlikeType(interest);
			baby.setFcreateTime(new Date());
			baby.setForder(0);
			customerBabyDAO.save(baby);
		} else {
			TCustomerBaby tCustomerBaby = new TCustomerBaby();
			tCustomerBaby.setFcustomerId(customerId);
			tCustomerBaby.setFsex(sex);
			tCustomerBaby.setFage(age);
			tCustomerBaby.setFlikeType(interest);
			tCustomerBaby.setFcreateTime(new Date());
			tCustomerBaby.setForder(0);
			customerBabyDAO.save(tCustomerBaby);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("用户个性推荐信息保存成功！");
		return responseDTO;
	}

	public ResponseDTO getInterestsList(Integer size) {
		ResponseDTO responseDTO = new ResponseDTO();

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

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("");
		returnData.put("interestsList", list);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * 删除不喜欢的活动
	 * 
	 * @param ticket
	 * @param eventId
	 * @param clientType
	 * @return
	 */
	public ResponseDTO deleteInterests(String ticket, String eventId, Integer clientType) {
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

		String customerId = fxlService.getCustomerIdByTicket(ticket, clientType);

		customerEventRecommendDAO.deleteByRecommendId(customerId, eventId);

		CustomerLikesDislikesBean cld = new CustomerLikesDislikesBean();
		cld.setCustomerId(customerId);
		cld.setEventId(eventId);
		cld.setType(2);
		addLikesDislikes(cld);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("个性推荐货品删除成功！");
		return responseDTO;
	}

	/**
	 * 添加用户喜好数据
	 * 
	 * @param ticket
	 * @param eventId
	 * @param clientType
	 * @return
	 */
	public void addLikesDislikes(CustomerLikesDislikesBean cld) {

		TCustomerLikesDislikes likesDislikes = new TCustomerLikesDislikes();

		likesDislikes.setFcustomerId(cld.getCustomerId());
		likesDislikes.setFeventId(cld.getEventId());
		likesDislikes.setFtype(cld.getType());// 1.喜欢 2.不喜欢
		likesDislikes.setFcreateTime(new Date());
		customerLikesDislikesDAO.save(likesDislikes);
	}

	/**
	 * 添加用户浏览记录
	 * 
	 * @param ticket
	 * @param eventId
	 * @param clientType
	 * @return
	 */
	public void addEventBrowse(CustomerAddEventBrowseBean caeb) {

		List<TCustomerEventBrowse> eventBrowseLists = customerEventBrowseDAO
				.findByCustomerIdAndEventId(caeb.getCustomerId(), caeb.getEventId());
		if (CollectionUtils.size(eventBrowseLists) > 1) {
			eventBrowseLists.remove(0);
			customerEventBrowseDAO.delete(eventBrowseLists);
		} else if (CollectionUtils.isEmpty(eventBrowseLists)) {
			Integer browses = customerEventBrowseDAO.getCountCustomerEventBrowse(caeb.getCustomerId());
			if (browses.intValue() == 20) {
				StringBuilder hql = new StringBuilder();
				hql.append(
						"select t.id as id from TCustomerEventBrowse t where t.fcustomerId=:fcustomerId order by t.fcreateTime asc");
				Map<String, Object> hqlMap = Maps.newHashMap();
				hqlMap.put("fcustomerId", caeb.getCustomerId());
				Query q = commonService.createQuery(hql.toString(), hqlMap);
				q.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				q.setFirstResult(0).setMaxResults(1);
				Map<String, String> oldId = (Map<String, String>) q.getSingleResult();
				customerEventBrowseDAO.setEventId(oldId.get("id"), caeb.getEventId(), new Date());

			} else {
				TCustomerEventBrowse tCustomerEventBrowse = new TCustomerEventBrowse();

				tCustomerEventBrowse.setFcustomerId(caeb.getCustomerId());
				tCustomerEventBrowse.setFeventId(caeb.getEventId());
				tCustomerEventBrowse.setFcreateTime(new Date());
				customerEventBrowseDAO.save(tCustomerEventBrowse);
			}
		}
	}

	/**
	 * 当用户选择tag的时候加载
	 * 
	 * @param customerId
	 */
	@Deprecated
	public void getPersonalizedTagList(String customerId) {

		int count = 0;
		TCustomerBaby customerBaby = customerBabyDAO.getByCustomerId(customerId);

		StringBuilder hql = new StringBuilder();

		hql.append(
				"select t.id as id, t.ftitle as ftitle,t.forderPrice as forderPrice, t.ftag as ftag,t.TSponsor.ftype as ftype,t.fageA as fageA,t.fageB as fageB from TEvent t where t.fstatus = 20");

		Map<String, Object> hqlMap = Maps.newHashMap();

		List<Map<String, Object>> eventList = commonService.find(hql.toString(), hqlMap);

		RecommendedRankingDTO recommendedRankingDTO = null;

		List<RecommendedRankingDTO> resultList = Lists.newArrayList();

		for (Map<String, Object> map : eventList) {

			// 判断宝宝信息是否符合当前一个货品的年龄段 +10
			if (customerBaby != null) {
				if (map.get("fageA") != null && StringUtils.isNotBlank(map.get("fageA").toString())
						&& map.get("fageB") != null && StringUtils.isNotBlank(map.get("fageB").toString())) {

					if (Integer.parseInt(map.get("fageA").toString()) <= customerBaby.getFage()
							&& Integer.parseInt(map.get("fageB").toString()) >= customerBaby.getFage()) {
						count = count + 10;
					}
				}

			}

			// 判断哪类商家
			if (map.get("ftype").toString().equals(3)) {
				count = count + 20;
			} else if (map.get("ftype").toString().equals(2)) {
				count = count + 10;
			}

			// 用户添加的喜欢标签
			// if (StringUtils.isNotBlank(customerBaby.getFlikeType())) {
			// int tagCount = 0;
			// String[] type = customerBaby.getFlikeType().split(";");
			// for (int i = 0; i < type.length; i++) {
			//
			// System.out.println(type[i]);
			// System.out.println(map.get("id").toString());
			// TEvent tEvent = eventDAO.findLikeTag(type[i],
			// map.get("id").toString());
			//
			// if (tEvent != null) {
			// tagCount++;
			// }
			//
			// System.out.println(tagCount + "循环了多杀次");
			// count = count + (tagCount * 10);
			// System.out.println(count + "标签分数");
			//
			// }
			// }

			// 近期浏览过的货品 -15
			List<TCustomerEventBrowse> eventBrowseLists = customerEventBrowseDAO.findByCustomerIdAndEventId(customerId,
					map.get("id").toString());

			if (CollectionUtils.isNotEmpty(eventBrowseLists)) {
				count = count - 15;
			}

			// 不感兴趣的货品 -40
			List<TCustomerLikesDislikes> customerLikesDislikesList = customerLikesDislikesDAO
					.findByCustomerIdAndEventId(customerId, map.get("id").toString(), 2);

			if (CollectionUtils.isNotEmpty(customerLikesDislikesList)) {
				count = count - 40;
			}

			// 近七天的销量 +10
			// SalesId sales = salesDAO.findByEventId(tEvent.getId());
			// if (sales != null) {
			// count = count + 10;
			// }

			// 购买过相同货品 +10

			recommendedRankingDTO = new RecommendedRankingDTO();
			recommendedRankingDTO.setEventId(map.get("id").toString());
			recommendedRankingDTO.setName(map.get("ftitle").toString());
			recommendedRankingDTO.setPrice(new BigDecimal(map.get("forderPrice").toString()));
			recommendedRankingDTO.setScore(count);
			recommendedRankingDTO.setCreateTime(new Date());
			resultList.add(recommendedRankingDTO);
		}

		if (resultList.size() <= 50) {
			Comparator<RecommendedRankingDTO> recommendedComparator = Ordering.from(new PersonalizedComparator())
					.compound(new Personalized2Comparator());
			Collections.sort(resultList, recommendedComparator);
			int i = 1;
			TCustomerEventRecommend customerEventRecommend = null;
			for (RecommendedRankingDTO recommended : resultList) {
				recommended.setRanking(i);
				i++;
				// 在这set进去值
				customerEventRecommend = new TCustomerEventRecommend();
				customerEventRecommend.setFcustomerId(customerId);
				customerEventRecommend.setFeventId(recommended.getEventId());
				customerEventRecommend.setForder(i);
				customerEventRecommend.setFcreateTime(new Date());
				customerEventRecommendDAO.save(customerEventRecommend);
			}
		} else {
			List<RecommendedRankingDTO> recommendedList = new ArrayList<RecommendedRankingDTO>();
			Comparator<RecommendedRankingDTO> recommendedComparator = Ordering.from(new PersonalizedComparator())
					.compound(new Personalized2Comparator());
			Collections.sort(resultList, recommendedComparator);

			recommendedList = resultList.subList(0, 50);
			int i = 1;
			TCustomerEventRecommend customerEventRecommend = null;
			for (RecommendedRankingDTO recommended : recommendedList) {
				recommended.setRanking(i);
				i++;
				// 在这set进去值
				customerEventRecommend = new TCustomerEventRecommend();
				customerEventRecommend.setFcustomerId(customerId);
				customerEventRecommend.setFeventId(recommended.getEventId());
				customerEventRecommend.setForder(i);
				customerEventRecommend.setFcreateTime(new Date());
				customerEventRecommendDAO.save(customerEventRecommend);
			}

		}

	}

	/**
	 * 当用户没选择tag的时候加载
	 * 
	 * @param customerId
	 */
	@Deprecated
	public void getPersonalizedNoTagList(String customerId) {

		int count = 0;
		TCustomerBaby customerBaby = customerBabyDAO.getByCustomerId(customerId);

		StringBuilder hql = new StringBuilder();

		hql.append(
				"select t.id as id, t.ftitle as ftitle,t.forderPrice as forderPrice, t.ftag as ftag,t.TSponsor.ftype as ftype,t.fageA as fageA,t.fageB as fageB from TEvent t where t.fstatus = 20");

		Map<String, Object> hqlMap = Maps.newHashMap();

		List<Map<String, Object>> eventList = commonService.find(hql.toString(), hqlMap);

		RecommendedRankingDTO recommendedRankingDTO = null;

		List<RecommendedRankingDTO> resultList = Lists.newArrayList();

		for (Map<String, Object> map : eventList) {

			// 判断宝宝信息是否符合当前一个货品的年龄段 +10
			if (customerBaby != null) {
				if (map.get("fageA") != null && StringUtils.isNotBlank(map.get("fageA").toString())
						&& map.get("fageB") != null && StringUtils.isNotBlank(map.get("fageB").toString())) {

					if (Integer.parseInt(map.get("fageA").toString()) <= customerBaby.getFage()
							&& Integer.parseInt(map.get("fageB").toString()) >= customerBaby.getFage()) {
						count = count + 10;
					}
				}

			}

			// 判断哪类商家
			if (map.get("ftype").toString().equals(3)) {
				count = count + 20;
			} else if (map.get("ftype").toString().equals(2)) {
				count = count + 10;
			}

			// 近期浏览过的货品 -15
			List<TCustomerEventBrowse> eventBrowseLists = customerEventBrowseDAO.findByCustomerIdAndEventId(customerId,
					map.get("id").toString());

			if (CollectionUtils.isNotEmpty(eventBrowseLists)) {
				count = count - 15;
			}

			// 不感兴趣的货品 -40
			List<TCustomerLikesDislikes> customerLikesDislikesList = customerLikesDislikesDAO
					.findByCustomerIdAndEventId(customerId, map.get("id").toString(), 2);

			if (CollectionUtils.isNotEmpty(customerLikesDislikesList)) {
				count = count - 40;
			}

			// // 近七天的销量 +10
			// SalesId sales = salesDAO.findByEventId(tEvent.getId());
			// if (sales != null) {
			// count = count + 10;
			// }

			// 购买过相同货品 +10

			recommendedRankingDTO = new RecommendedRankingDTO();
			recommendedRankingDTO.setEventId(map.get("id").toString());
			recommendedRankingDTO.setName(map.get("ftitle").toString());
			recommendedRankingDTO.setPrice(new BigDecimal(map.get("forderPrice").toString()));
			recommendedRankingDTO.setScore(count);
			recommendedRankingDTO.setCreateTime(new Date());
			resultList.add(recommendedRankingDTO);
		}

		if (resultList.size() <= 50) {
			Comparator<RecommendedRankingDTO> recommendedComparator = Ordering.from(new PersonalizedComparator())
					.compound(new Personalized2Comparator());
			Collections.sort(resultList, recommendedComparator);
			int i = 1;
			TCustomerEventRecommend customerEventRecommend = null;
			for (RecommendedRankingDTO recommended : resultList) {
				recommended.setRanking(i);
				i++;
				// 在这set进去值
				customerEventRecommend = new TCustomerEventRecommend();
				customerEventRecommend.setFcustomerId(customerId);
				customerEventRecommend.setFeventId(recommended.getEventId());
				customerEventRecommend.setForder(i);
				customerEventRecommend.setFcreateTime(new Date());
				customerEventRecommendDAO.save(customerEventRecommend);
			}
		} else {
			List<RecommendedRankingDTO> recommendedList = new ArrayList<RecommendedRankingDTO>();
			Comparator<RecommendedRankingDTO> recommendedComparator = Ordering.from(new PersonalizedComparator())
					.compound(new Personalized2Comparator());
			Collections.sort(resultList, recommendedComparator);

			recommendedList = resultList.subList(0, 50);
			int i = 1;
			TCustomerEventRecommend customerEventRecommend = null;
			for (RecommendedRankingDTO recommended : recommendedList) {
				recommended.setRanking(i);
				i++;
				// 在这set进去值
				customerEventRecommend = new TCustomerEventRecommend();
				customerEventRecommend.setFcustomerId(customerId);
				customerEventRecommend.setFeventId(recommended.getEventId());
				customerEventRecommend.setForder(i);
				customerEventRecommend.setFcreateTime(new Date());
				customerEventRecommendDAO.save(customerEventRecommend);
			}

		}

	}

	public ResponseDTO addUserFeedBack(String ticket, String message, Integer clientType) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		String customerId = fxlService.getCustomerIdByTicket(ticket, clientType);
		if (customerId != null) {
			TCustomerFeedback customerFeedback = new TCustomerFeedback();
			customerFeedback.setFcustomerId(customerId);
			customerFeedback.setFmessage(message);
			customerFeedback.setFtype(clientType);
			customerFeedback.setFcreateTime(new Date());
			customerFeedbackDAO.save(customerFeedback);
		} else {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(100);
			responseDTO.setMsg("ticket参数有误，这是个无效或者过期的ticket信息！");
			return responseDTO;
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("用户反馈推荐信息成功！");
		return responseDTO;
	}

}
// public static List<RecommendedRankingDTO> getRecommendedRankingList() {
//
// List<RecommendedRankingDTO> resultList = Lists.newArrayList();
//
// Comparator<RecommendedRankingDTO> recommendedComparator = Ordering.from(new
// PersonalizedComparator())
// .compound(new Personalized2Comparator());
// Collections.sort(resultList, recommendedComparator);
// int i = 1;
// for (RecommendedRankingDTO recommendedRankingDTO : resultList) {
// recommendedRankingDTO.setRanking(i);
// i++;
// }

// return resultList;
// }

// }