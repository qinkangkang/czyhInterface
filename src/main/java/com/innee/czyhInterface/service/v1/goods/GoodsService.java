package com.innee.czyhInterface.service.v1.goods;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.solr.common.SolrDocument;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;

import com.fasterxml.jackson.databind.JavaType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.common.dict.RequestUrl;
import com.innee.czyhInterface.dao.ColumnBannerDAO;
import com.innee.czyhInterface.dao.CommentDAO;
import com.innee.czyhInterface.dao.EventDAO;
import com.innee.czyhInterface.dao.FavoriteDAO;
import com.innee.czyhInterface.dao.GoodsSkuDAO;
import com.innee.czyhInterface.dao.GoodsTypeClassValueDAO;
import com.innee.czyhInterface.dao.SeckillModuleDAO;
import com.innee.czyhInterface.dto.CustomerDTO;
import com.innee.czyhInterface.dto.EventDistanceDTO;
import com.innee.czyhInterface.dto.m.CartGoodsDTO;
import com.innee.czyhInterface.dto.m.PageDTO;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.dto.m.goods.GoodsDTO;
import com.innee.czyhInterface.dto.m.goods.GoodsDetailDTO;
import com.innee.czyhInterface.dto.m.goods.GoodsSkuDTO;
import com.innee.czyhInterface.dto.m.goods.GoodsSkuValueDTO;
import com.innee.czyhInterface.dto.m.goods.GoodsValueDTO;
import com.innee.czyhInterface.dto.m.goods.SpikeDTO;
import com.innee.czyhInterface.dto.m.goods.SpikeGoodsDTO;
import com.innee.czyhInterface.dto.m.system.SolrPageDTO;
import com.innee.czyhInterface.entity.TColumnBanner;
import com.innee.czyhInterface.entity.TComment;
import com.innee.czyhInterface.entity.TCustomer;
import com.innee.czyhInterface.entity.TEvent;
import com.innee.czyhInterface.entity.TFavorite;
import com.innee.czyhInterface.entity.TGoodsSku;
import com.innee.czyhInterface.entity.TGoodsTypeClassValue;
import com.innee.czyhInterface.entity.TOrderGoods;
import com.innee.czyhInterface.entity.TSeckillModule;
import com.innee.czyhInterface.entity.TSponsor;
import com.innee.czyhInterface.impl.couponImpl.CouponsService;
import com.innee.czyhInterface.service.v1.category.CategoryService;
import com.innee.czyhInterface.service.v1.system.RedisService;
import com.innee.czyhInterface.service.v2.CommonService;
import com.innee.czyhInterface.service.v2.FxlService;
import com.innee.czyhInterface.util.CommonPage;
import com.innee.czyhInterface.util.Constant;
import com.innee.czyhInterface.util.DictionaryUtil;
import com.innee.czyhInterface.util.HttpClientUtil;
import com.innee.czyhInterface.util.PropertiesUtil;
import com.innee.czyhInterface.util.redis.RedisMoudel;
import com.innee.czyhInterface.util.solr.SolrUtil;
import com.innee.czyhInterface.util.solr.model.SearchableGoodsInfo;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * 商品业务管理类
 * 
 * @author jinshengzhi
 */
@Component
@Transactional
public class GoodsService {

	private static final Logger logger = LoggerFactory.getLogger(GoodsService.class);

	private static JsonMapper mapper = JsonMapper.nonDefaultMapper();

	@Autowired
	private CommonService commonService;

	@Autowired
	private FxlService fxlService;

	@Autowired
	private EventDAO eventDAO;

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private FavoriteDAO favoriteDAO;

	@Autowired
	private RedisService redisService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ColumnBannerDAO columnBannerDAO;

	@Autowired
	private SeckillModuleDAO seckillModuleDAO;

	@Autowired
	private CommentDAO commentDAO;

	@Autowired
	private GoodsSkuDAO goodsSkuDAO;

	@Autowired
	private GoodsService goodsService;

	@Autowired
	private GoodsTypeClassValueDAO goodsTypeClassValueDAO;
	
	@Autowired
	private CouponsService couponsService;

	@Transactional(readOnly = true)
	public ResponseDTO getCategoryList() {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();

		/*
		 * Map<Object, Element> categoryMap =
		 * eventCategoryCache.getAll(eventCategoryCache.getKeysNoDuplicateCheck(
		 * ));
		 * 
		 * List<Map<String, Object>> list = Lists.newArrayList(); Map<String,
		 * Object> map = Maps.newHashMap(); map.put("id", 0); map.put("name",
		 * "全部"); list.add(map); for (Iterator it =
		 * categoryMap.entrySet().iterator(); it.hasNext();) { map =
		 * Maps.newHashMap(); Map.Entry<Object, Element> e = (Map.Entry<Object,
		 * Element>) it.next(); map.put("id", e.getKey()); Element ele =
		 * e.getValue(); map.put("name", ele.getObjectValue()); list.add(map); }
		 * returnData.put("categoryList", list);
		 */
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("商品分类加载成功！");
		return responseDTO;
	}

	/**
	 * 商品列表
	 * 
	 * @param valueMap
	 * @param page
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getGoodsList(Integer cityId, String key, Integer categoryId, Integer orderBy, Integer sellModel,
			String freePostage, String sessionId, String channelId, Integer sdealsModel, String ticket,
			Integer clientType, Integer pageSize, Integer offset) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (cityId == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("cityId参数不能为空，请检查cityId的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = null;
		if (StringUtils.isNotBlank(ticket)) {
			customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
			if (!customerDTO.isEnable()) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(customerDTO.getStatusCode());
				responseDTO.setMsg(customerDTO.getMsg());
				return responseDTO;
			}
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
		if (orderBy.intValue() == 4) {
			hql.append(
					"select t.id as id, t.ftitle as ftitle, s.fname as ffullName, t.ftypeA as ftypeA ,t.fbrief as fbrief, t.fimage1 as fimage1, t.fprice as fprice, t.fpriceMoney as fpriceMoney, t.fstatus as fstatus,t.foffSaleTime as foffSaleTime,t.fusePreferential as fusePreferential ")
					.append(",t.fspec as fspec,t.fsellModel as fsellModel,t.fsdealsModel as fsdealsModel, t.fspecModel as fSpecModel, t.fpromotionModel as fpromotionModel,t.flimitation as fLimitation,t.fstock as fstock,t.fsecondKill as fsecondKill, t.ftotal as ftotal,t.fstock as fstock,dt.fdistance as fdistance ")
					.append(",t.fsaleTotal as fsaleTotal from TEvent t left join t.TSponsor s left join TEventDistanceTemp as dt on s.id = dt.feventId and dt.fhsid = :sessionId ");
			hqlMap.put("sessionId", sessionId);
		} else {
			hql.append(
					"select t.id as id, t.ftitle as ftitle, t.TSponsor.fname as ffullName, t.ftypeA as ftypeA ,t.fbrief as fbrief, t.fimage1 as fimage1, t.fprice as fprice, t.fpriceMoney as fpriceMoney, t.fstatus as fstatus,t.foffSaleTime as foffSaleTime,t.fusePreferential as fusePreferential ")
					.append(",t.fsaleTotal as fsaleTotal,t.fspec as fspec,t.fsellModel as fsellModel,t.fsdealsModel as fsdealsModel, t.fspecModel as fSpecModel, t.fpromotionModel as fpromotionModel,t.flimitation as fLimitation,t.fstock as fstock,t.fsecondKill as fsecondKill,t.ftotal as ftotal,t.fstock as fstock from TEvent t ");
		}

		if (StringUtils.isNotBlank(channelId)) {
			hql.append(" inner join t.TAppChannels as ace");
		}
		hql.append(" where t.fstatus = 20 ");
		if (StringUtils.isNotBlank(channelId)) {
			hql.append(" and ace.TAppChannelSetting.id = :channelId");
			hqlMap.put("channelId", channelId);
		}
		if (StringUtils.isNotBlank(key)) {
			hql.append(" and (t.ftitle like :key )");
			hqlMap.put("key", "%" + key + "%");
		}
		if (sellModel != null) {
			hql.append(" and t.fsellModel = :sellModel");
			hqlMap.put("sellModel", sellModel);
		}
		if (categoryId.intValue() != 0) {
			hql.append(" and t.ftypeB = :categoryId");
			hqlMap.put("categoryId", categoryId);
		}
		if (sdealsModel.intValue() != 0) {
			hql.append(" and t.fsdealsModel = :sdealsModel");
			hqlMap.put("sdealsModel", sdealsModel);
		} else {
			hql.append(" and t.fsdealsModel = 0");
		}
		if (StringUtils.isNotBlank(freePostage)) {
			hql.append(" and t.fpriceMoney >= :freePostage");
			hqlMap.put("freePostage", new BigDecimal(freePostage));
			if (orderBy.intValue() != 0) {
				if (orderBy.intValue() == 1) {
					hql.append(" order by t.fpriceMoney,t.fsaleTotal desc");
				} else if (orderBy.intValue() == 2) {
					hql.append(" order by t.fpriceMoney asc");
				} else if (orderBy.intValue() == 3) {
					hql.append(" order by t.fpriceMoney desc");
				} else if (orderBy.intValue() == 4) {
					hql.append(" order by t.fpriceMoney,dt.fdistance desc");
				}
			} else {
				hql.append(" order by t.fpriceMoney,t.fupdateTime desc");
			}
		} else {
			if (orderBy.intValue() != 0) {
				if (orderBy.intValue() == 1) {
					hql.append(" order by t.fsaleTotal desc");
				} else if (orderBy.intValue() == 2) {
					hql.append(" order by t.fpriceMoney asc");
				} else if (orderBy.intValue() == 3) {
					hql.append(" order by t.fpriceMoney desc");
				} else if (orderBy.intValue() == 4) {
					hql.append(" order by dt.fdistance desc");
				}
			} else {
				hql.append(" order by t.fupdateTime desc");
			}
		}
		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();

		// 获取购物车信息
		JavaType jt = mapper.contructMapType(HashMap.class, String.class, Integer.class);
		Map<String, Integer> goodsMap = Maps.newHashMap();
		if (customerDTO != null) {
			try {
				goodsMap = mapper.fromJson(redisService.getValue(customerDTO.getCustomerId(), RedisMoudel.shoppingcar),
						jt);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
			if (goodsMap != null && goodsMap.containsKey(amap.get("id").toString())) {
				goodsDTO.setCount(goodsMap.get(amap.get("id").toString()));
			}
			goodsDTO.setGoodsTitle(amap.get("ftitle").toString());
			if (amap.get("ftypeA") != null && StringUtils.isNotBlank(amap.get("ftypeA").toString())) {
				type = categoryService.getCategoryA((Integer) amap.get("ftypeA"));
				goodsDTO.setType(type != null ? type : StringUtils.EMPTY);
			}
			if (amap.get("fusePreferential") != null
					&& StringUtils.isNotBlank(amap.get("fusePreferential").toString())) {
				if (((Integer) amap.get("fusePreferential")).intValue() == 1) {
					goodsDTO.setUseCoupon(false);
				}
			}
			if (amap.get("fsaleTotal") != null && StringUtils.isNotBlank(amap.get("fsaleTotal").toString())) {
				goodsDTO.setSaleTotal((Integer) amap.get("fsaleTotal"));
			}
			if (amap.get("ftotal") != null && StringUtils.isNotBlank(amap.get("ftotal").toString())) {
				goodsDTO.setPercentage(((Integer) amap.get("fstock")) * 100 / ((Integer) amap.get("ftotal")));
			}
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
					goodsDTO.setDesc(amap.get("fbrief").toString().substring(0, 15) + "...");
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
				goodsDTO.setLimitation((Integer) amap.get("fLimitation"));
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
			if (amap.get("fPromotionModel") != null && StringUtils.isNotBlank(amap.get("fPromotionModel").toString())) {
				goodsDTO.setPromotionModel(((Integer) amap.get("fPromotionModel")).intValue());
			}
			if (amap.get("fsdealsModel") != null && StringUtils.isNotBlank(amap.get("fsdealsModel").toString())) {
				goodsDTO.setSdealsModel(((Integer) amap.get("fsdealsModel")).intValue());
			}
			if (amap.get("fstock") != null && StringUtils.isNotBlank(amap.get("fstock").toString())) {
				if (((Integer) amap.get("fstock")).intValue() > 0) {
					goodsDTO.setIfInStock(true);
				}
			}
			ele = sessionIdEventDistanceCache.get(sessionId);
			if (ele != null) {
				Map<String, EventDistanceDTO> eventDistanceMap = (Map<String, EventDistanceDTO>) ele.getObjectValue();
				EventDistanceDTO eventDistanceDTO = eventDistanceMap.get(goodsDTO.getGoodsId());
				if (eventDistanceDTO != null) {
					goodsDTO.setDistance(eventDistanceDTO.getDistanceString());
				}
			} else if (amap.get("fdistance") != null && StringUtils.isNotBlank(amap.get("fdistance").toString())) {
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
			responseDTO.setMsg("没有检索到您查找的活动！");
		}
		return responseDTO;
	}

	/**
	 * 商品搜索引擎
	 * 
	 * @param valueMap
	 * @param page
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getGoodsSearch(String searchValue, Integer categoryId, Integer orderBy, Integer pageSize,
			Integer offset) {
		ResponseDTO responseDTO = new ResponseDTO();

		SolrUtil solrUtil = new SolrUtil();
		SolrPageDTO solrPageDTO = solrUtil.querySolr(searchValue, categoryId, orderBy, pageSize, offset);

		if (solrPageDTO.getSolrDocumentList() == null) {
			responseDTO.setMsg("没有检索到您查找的商品！");
			return responseDTO;
		}
		// 对活动信息进行细化加载
		List<GoodsDTO> resultList = Lists.newArrayList();
		GoodsDTO goodsDTO = null;
		Date date = null;
		for (SolrDocument amap : solrPageDTO.getSolrDocumentList()) {
			goodsDTO = new GoodsDTO();
			goodsDTO.setGoodsId(amap.get("id").toString());

			if(amap.get(SearchableGoodsInfo.NAME_FIELD)!=null){
				goodsDTO.setGoodsTitle(amap.get(SearchableGoodsInfo.NAME_FIELD).toString());
			}
			if (amap.get(SearchableGoodsInfo.IMAGE_FIELD) != null) {
				goodsDTO.setImageUrl(amap.get(SearchableGoodsInfo.IMAGE_FIELD).toString());
			}
			if (amap.get(SearchableGoodsInfo.TYPE_FIELD) != null) {
				goodsDTO.setType(amap.get(SearchableGoodsInfo.TYPE_FIELD).toString());
			}
			if (amap.get(SearchableGoodsInfo.CATEGORYID_FIELD) != null) {
				goodsDTO.setCategoryId(Integer.parseInt(amap.get(SearchableGoodsInfo.CATEGORYID_FIELD).toString()));
			}
			if (amap.get(SearchableGoodsInfo.SPEC_FIELD) != null) {
				goodsDTO.setSpec(amap.get(SearchableGoodsInfo.SPEC_FIELD).toString());
			}
			if (amap.get(SearchableGoodsInfo.ORIGINALPRICE_FIELD) != null) {
				goodsDTO.setOriginalPrice(amap.get(SearchableGoodsInfo.ORIGINALPRICE_FIELD).toString());
			}
			if (amap.get(SearchableGoodsInfo.PRESENTPRICE_FIELD) != null) {
				goodsDTO.setPresentPrice(amap.get(SearchableGoodsInfo.PRESENTPRICE_FIELD).toString());
			}
			if (amap.get(SearchableGoodsInfo.DESC_FIELD) != null) {
				goodsDTO.setDesc(amap.get(SearchableGoodsInfo.DESC_FIELD).toString());
			}
			if (amap.get(SearchableGoodsInfo.SPONSORNAME_FIELD) != null) {
				goodsDTO.setSponsorName(amap.get(SearchableGoodsInfo.SPONSORNAME_FIELD).toString());
			}
			if (amap.get(SearchableGoodsInfo.STOCKFLAG_FIELD) != null) {
				goodsDTO.setStockFlag(Integer.parseInt(amap.get(SearchableGoodsInfo.STOCKFLAG_FIELD).toString()));
			}
			if (amap.get(SearchableGoodsInfo.STATUS_FIELD) != null) {
				goodsDTO.setStatus(Integer.parseInt(amap.get(SearchableGoodsInfo.STATUS_FIELD).toString()));
			}
			if (amap.get(SearchableGoodsInfo.SELLMODEL_FIELD) != null) {
				goodsDTO.setSellModel(Integer.parseInt(amap.get(SearchableGoodsInfo.SELLMODEL_FIELD).toString()));
			}
			if (amap.get(SearchableGoodsInfo.SPECMODEL_FIELD) != null) {
				goodsDTO.setSpecModel(Integer.parseInt(amap.get(SearchableGoodsInfo.SPECMODEL_FIELD).toString()));
			}
			if (amap.get(SearchableGoodsInfo.PROMOTIONMODEL_FIELD) != null) {
				goodsDTO.setPromotionModel(
						Integer.parseInt(amap.get(SearchableGoodsInfo.PROMOTIONMODEL_FIELD).toString()));
			}
			if (amap.get(SearchableGoodsInfo.LIMITATION_FIELD) != null) {
				goodsDTO.setLimitation(Integer.parseInt(amap.get(SearchableGoodsInfo.LIMITATION_FIELD).toString()));
			}
			if (amap.get(SearchableGoodsInfo.PERCENTAGE_FIELD) != null) {
				goodsDTO.setPercentage(Integer.parseInt(amap.get(SearchableGoodsInfo.PERCENTAGE_FIELD).toString()));
			}
			if (amap.get(SearchableGoodsInfo.PERCENTAGE_FIELD) != null) {
				goodsDTO.setPercentage(Integer.parseInt(amap.get(SearchableGoodsInfo.PERCENTAGE_FIELD).toString()));
			}
			if (amap.get(SearchableGoodsInfo.SALETOTAL_FIELD) != null) {
				goodsDTO.setSaleTotal(Integer.parseInt(amap.get(SearchableGoodsInfo.SALETOTAL_FIELD).toString()));
			}
			if (amap.get(SearchableGoodsInfo.SDEALSMODEL_FIELD) != null) {
				goodsDTO.setSdealsModel(Integer.parseInt(amap.get(SearchableGoodsInfo.SDEALSMODEL_FIELD).toString()));
			}
			if (amap.get(SearchableGoodsInfo.USECOUPON_FIELD) != null) {
				goodsDTO.setUseCoupon(Boolean.valueOf(amap.get(SearchableGoodsInfo.USECOUPON_FIELD).toString()));
			}
			if (amap.get(SearchableGoodsInfo.IFINSTOCK_FIELD) != null) {
				goodsDTO.setIfInStock(Boolean.valueOf(amap.get(SearchableGoodsInfo.IFINSTOCK_FIELD).toString()));
			}
			if (amap.get(SearchableGoodsInfo.COUNT_FIELD) != null) {
				goodsDTO.setCount(Integer.parseInt(amap.get(SearchableGoodsInfo.COUNT_FIELD).toString()));
			}
			if (amap.get(SearchableGoodsInfo.STATUSSTRING_FIELD) != null) {
				goodsDTO.setStatusString(amap.get(SearchableGoodsInfo.STATUSSTRING_FIELD).toString());
			}
			if (amap.get(SearchableGoodsInfo.CREATETIME_FIELD) != null) {
				date = (Date) amap.get(SearchableGoodsInfo.CREATETIME_FIELD);
				goodsDTO.setCreateTime(DateFormatUtils.format(date, "yyyy-MM-dd HH:mm"));
			}
			if (amap.get(SearchableGoodsInfo.BARGAININGID_FIELD) != null) {
				goodsDTO.setBargainingId(amap.get(SearchableGoodsInfo.BARGAININGID_FIELD).toString());
			}

			resultList.add(goodsDTO);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("goodsSearchList", resultList);
		PageDTO pageDTO = new PageDTO(solrPageDTO.getTotalCount(), solrPageDTO.getPageSize(), solrPageDTO.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);
		if (solrPageDTO.getTotalCount() == 0L) {
			responseDTO.setMsg("没有检索到您查找的商品！");
		}

		return responseDTO;
	}

	public ResponseDTO getGoodsDetail(String ticket, String goodsId, Integer clientType) {
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
		GoodsDetailDTO goodsDetailDTO = new GoodsDetailDTO();
		Map<String, Object> returnData = Maps.newHashMap();

		goodsDetailDTO.setGoodsId(goodsId);
		goodsDetailDTO.setGoodsTitle(tEvent.getFtitle());
		goodsDetailDTO.setDesc(tEvent.getFbrief());
		Date killTime = null;
		if (tEvent.getFsdealsModel() != null && tEvent.getFsdealsModel().intValue() == 3) {
			TColumnBanner banner = columnBannerDAO.findColumnBanner(2, 1);
			killTime = banner.getFseckillTime();
			TSeckillModule tSeckillModule = seckillModuleDAO.findByGoodsId(tEvent.getId());
			if (killTime.compareTo(new Date()) > 0) {
				goodsDetailDTO.setStatus(tSeckillModule.getFgoodstatus());
			} else {
				goodsDetailDTO.setStatus(90);
			}
		} else if (tEvent.getFsdealsModel() != null && tEvent.getFsdealsModel().intValue() == 1) {
			TColumnBanner banner = columnBannerDAO.findColumnBanner(1, 1);
			killTime = banner.getFseckillTime();
			goodsDetailDTO.setStatus(tEvent.getFstatus());
		} else {
			killTime = new Date();
			goodsDetailDTO.setStatus(tEvent.getFstatus());
		}
		Integer second = Seconds.secondsBetween(new DateTime(new Date()), new DateTime(killTime)).getSeconds();
		goodsDetailDTO.setGoodsTime(second.toString());
		goodsDetailDTO.setSpec(tEvent.getFspec());
		goodsDetailDTO.setSellModel(tEvent.getFsellModel());
		goodsDetailDTO.setSpecModel(tEvent.getFspecModel());
		goodsDetailDTO.setLimitation(tEvent.getFlimitation());
		// 促销模式
		if (tEvent.getFpromotionModel() != null && tEvent.getFpromotionModel().intValue() != 0) {
			goodsDetailDTO.setPromotionModel(
					DictionaryUtil.getString(DictionaryUtil.PromotionModel, tEvent.getFpromotionModel()));
		}
		if (tEvent.getFusePreferential() != null && tEvent.getFusePreferential().intValue() == 1) {
			goodsDetailDTO.setPromotionModel(
					DictionaryUtil.getString(DictionaryUtil.PromotionModel, tEvent.getFpromotionModel()));
		}
		TSponsor tSponsor = tEvent.getTSponsor();
		goodsDetailDTO.setMerchantId(tSponsor.getId());
		goodsDetailDTO.setMerchantName(tSponsor.getFname());

		if (tEvent.getFsaleTotal() != null) {
			goodsDetailDTO.setSaleTotal(tEvent.getFsaleTotal());
		}
		goodsDetailDTO.setStock(tEvent.getFstock());
		goodsDetailDTO.setOriginalPrice(tEvent.getFprice().toString());
		goodsDetailDTO.setPresentPrice(tEvent.getFpriceMoney().toString());
		if (tEvent.getFusePreferential() != null && tEvent.getFusePreferential() == 1) {
			goodsDetailDTO.setUseCoupon(false);
		}
		goodsDetailDTO.setImageUrls(fxlService.getImageUrls(tEvent.getFimage2(), false));
		if (StringUtils.isNotBlank(tEvent.getFdetailHtmlUrl())) {
			goodsDetailDTO.setDetailHtmlUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
					.append(PropertiesUtil.getProperty("htmlRootPath")).append(tEvent.getFdetailHtmlUrl()).toString());
		}

		goodsDetailDTO.setStatusString(DictionaryUtil.getString(DictionaryUtil.EventStatus, tEvent.getFstatus()));

		// 商品规格图片
		if (tEvent.getFgoodsSpecImage() != null) {
			goodsDetailDTO.setGoodsSpecImage(tEvent.getFgoodsSpecImage());
		}
		// 获取商品属性
		if (tSponsor.getFpinkage() != null) {
			goodsDetailDTO.setPinkage("全场满" + tSponsor.getFpinkage().toString() + "包邮");
		}
		Map<Integer, String> goodsLabelMap = DictionaryUtil.getStatueMap(DictionaryUtil.GoodsLabel);
		List<String> goodsLabel = Lists.newArrayList();
		for (Iterator it = goodsLabelMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Integer, String> e = (Map.Entry<Integer, String>) it.next();
			goodsLabel.add(e.getValue().toString());
		}
		goodsLabel.add(tSponsor.getFpinkage().toString() + "元免基础运费");
		goodsDetailDTO.setGoodsLabel(goodsLabel);

		// 获取到活动类目缓存对象
		String ele = categoryService.getCategoryA(tEvent.getFtypeA());
		goodsDetailDTO.setType(ele != null ? ele : StringUtils.EMPTY);

		CustomerDTO customerDTO = null;
		// 判断商品是否收藏
		if (StringUtils.isNotBlank(ticket)) {
			customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
			if (customerDTO != null && customerDTO.isEnable()) {
				TFavorite tFavorite = favoriteDAO.getFavoriteEventByCustomerIdAndObejctId(customerDTO.getCustomerId(),
						goodsId);
				if (tFavorite != null) {
					goodsDetailDTO.setFavorite(true);
				} else {
					goodsDetailDTO.setFavorite(false);
				}

			} else {
				goodsDetailDTO.setFavorite(false);
			}
		} else {
			goodsDetailDTO.setFavorite(false);
		}

		if (tEvent.getFsdealsModel().intValue() != 0) {
			goodsDetailDTO.setIfSeckill(true);
		}

		// 获取购物车信息
		JavaType jt = mapper.contructMapType(HashMap.class, String.class, CartGoodsDTO.class);
		Map<String, CartGoodsDTO> goodsMap = Maps.newHashMap();
		if (customerDTO != null && customerDTO.isEnable()) {
			try {
				goodsMap = mapper.fromJson(redisService.getValue(customerDTO.getCustomerId(), RedisMoudel.shoppingcar),
						jt);
				if (goodsMap != null && goodsMap.containsKey(goodsId)) {
					goodsDetailDTO.setCount(((CartGoodsDTO) goodsMap.get(goodsId)).getCount());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		int goodComment = 0;

		// 获取商品评价信息
		List<TComment> comments = commentDAO.findComment(goodsId);
		if (comments != null && comments.size() > 0) {
			for (TComment comment : comments) {
				if (comment.getFscore() > 1) {
					goodComment++;
				}
			}
			goodsDetailDTO.setAllComment(comments.size());
			goodsDetailDTO.setGoodComment(goodComment);
			goodsDetailDTO.setBadComment(comments.size() - goodComment);
			goodsDetailDTO.setRating(goodComment * 100 / comments.size() + "%");
		}

		ResponseDTO dto = new ResponseDTO();
		
		String customerId = "";
		if(customerDTO!=null&&customerDTO.getCustomerId()!=null){
			customerId= customerDTO.getCustomerId();
		}
		dto = couponsService.getCouponByEvent(customerId, goodsId, 
				tEvent.getFtypeA().toString(), tSponsor.getId());

		JavaType couponJt = mapper.contructCollectionType(ArrayList.class, String.class);
		List<String> couponString = Lists.newArrayList();
		if (dto.isSuccess()) {
			couponString = mapper.fromJson(dto.getData().get("couponString").toString(), couponJt);
			returnData.put("couponList", dto.getData().get("couponList"));
		} else {
			returnData.put("couponList", new ArrayList());
		}

		// 商品规格处理模块
		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = Maps.newHashMap();

		hql.append(
				"select t.id as skuId,t.fgoodsId as fgoodsId,t.ftotal as ftotal,t.fstock as fstock,t.fprice as fprice,")
				.append(" t.fpriceMoney as fpriceMoney,t.fimage as fimage,t.flimitation as flimitation,t.flag as flag,c.id as cid,")
				.append(" c.fvalue as cfvalue,c.fextendClassId as cfextendClassId,c.fextendClassName as cfextendClassName,")
				.append(" f.id as fid,f.fvalue as tfvalue,f.fextendClassId as ffextendClassId,f.fextendClassName as ffextendClassName ")
				.append(" from TGoodsSku t left join TGoodsTypeClassValue c on t.fclassTypeValue1 = c.id left join TGoodsTypeClassValue f on t.fclassTypeValue2 = f.id");

		if (StringUtils.isNotBlank(goodsId)) {
			hql.append(" where t.fgoodsId = :goodsId and t.fstock > 0 order by t.flag desc");
			hqlMap.put("goodsId", goodsId);
		}

		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);

		// 循环商品规格信息
		GoodsSkuDTO goodsSkuDTO = null;
		GoodsValueDTO goodsValueDTO = new GoodsValueDTO();

		List<GoodsSkuDTO> goodsSkuDTOList = Lists.newArrayList();

		List<GoodsSkuValueDTO> goodsValue1List = Lists.newArrayList();
		List<GoodsSkuValueDTO> goodsValue2List = Lists.newArrayList();
		Map<String, Object> goodsValue1 = new HashMap<String, Object>();
		Map<String, Object> goodsValue2 = new HashMap<String, Object>();
		GoodsSkuValueDTO goodsSkuValueDTO1 = null;
		GoodsSkuValueDTO goodsSkuValueDTO2 = null;

		goodsDetailDTO.setCanChoose(true);
		/*if (list.size() > 1) {
		}*/

		Integer allStock = 0;
		boolean isFlag = true;
		Integer size = 0;
		for (Map<String, Object> amap : list) {
			goodsSkuDTO = new GoodsSkuDTO();
			size++;
			// 详情加入默认选中sku信息
			if (amap.get("flag").toString().equals("1")) {
				isFlag = false;
				if (amap.get("cfvalue") != null && StringUtils.isNotBlank(amap.get("cfvalue").toString())) {
					goodsDetailDTO.setGoodsValue1Name(amap.get("cfvalue").toString());
				}
				if (amap.get("cid") != null && StringUtils.isNotBlank(amap.get("cid").toString())) {
					goodsDetailDTO.setGoodsValue1Id(amap.get("cid").toString());
				}
				if (amap.get("tfvalue") != null && StringUtils.isNotBlank(amap.get("tfvalue").toString())) {
					goodsDetailDTO.setGoodsValue2Name(amap.get("tfvalue").toString());
				}
				if (amap.get("fid") != null && StringUtils.isNotBlank(amap.get("fid").toString())) {
					goodsDetailDTO.setGoodsValue2Id(amap.get("fid").toString());
				}
				if (amap.get("skuId") != null && StringUtils.isNotBlank(amap.get("skuId").toString())) {
					goodsDetailDTO.setGoodsSkuId(amap.get("skuId").toString());
				}
			}
			
			if(isFlag && size==list.size()){
				if (amap.get("cfvalue") != null && StringUtils.isNotBlank(amap.get("cfvalue").toString())) {
					goodsDetailDTO.setGoodsValue1Name(amap.get("cfvalue").toString());
				}
				if (amap.get("cid") != null && StringUtils.isNotBlank(amap.get("cid").toString())) {
					goodsDetailDTO.setGoodsValue1Id(amap.get("cid").toString());
				}
				if (amap.get("tfvalue") != null && StringUtils.isNotBlank(amap.get("tfvalue").toString())) {
					goodsDetailDTO.setGoodsValue2Name(amap.get("tfvalue").toString());
				}
				if (amap.get("fid") != null && StringUtils.isNotBlank(amap.get("fid").toString())) {
					goodsDetailDTO.setGoodsValue2Id(amap.get("fid").toString());
				}
				if (amap.get("skuId") != null && StringUtils.isNotBlank(amap.get("skuId").toString())) {
					goodsDetailDTO.setGoodsSkuId(amap.get("skuId").toString());
				}
			}
			// spu信息
			goodsSkuDTO.setGoodsId(amap.get("fgoodsId").toString());
			goodsSkuDTO.setImageUrl(fxlService.getImageUrl(amap.get("fimage").toString(), false));
			// sku信息
			if (amap.get("cfvalue") != null && StringUtils.isNotBlank(amap.get("cfvalue").toString())) {
				goodsSkuDTO.setGoodsValue1Name(amap.get("cfvalue").toString());
			}
			if (amap.get("cid") != null && StringUtils.isNotBlank(amap.get("cid").toString())) {
				goodsSkuDTO.setGoodsValue1Id(amap.get("cid").toString());
			}
			if (amap.get("tfvalue") != null && StringUtils.isNotBlank(amap.get("tfvalue").toString())) {
				goodsSkuDTO.setGoodsValue2Name(amap.get("tfvalue").toString());
			}
			if (amap.get("fid") != null && StringUtils.isNotBlank(amap.get("fid").toString())) {
				goodsSkuDTO.setGoodsValue2Id(amap.get("fid").toString());
			}
			if (amap.get("skuId") != null && StringUtils.isNotBlank(amap.get("skuId").toString())) {
				goodsSkuDTO.setSkuId(amap.get("skuId").toString());
			}
			if (amap.get("fstock") != null && StringUtils.isNotBlank(amap.get("fstock").toString())) {
				goodsSkuDTO.setStock((Integer) amap.get("fstock"));
				allStock = allStock + (Integer) amap.get("fstock");
			}
			if (amap.get("fpriceMoney") != null && StringUtils.isNotBlank(amap.get("fpriceMoney").toString())) {
				goodsSkuDTO.setPriceMoney((BigDecimal) amap.get("fpriceMoney"));
			}
			if (amap.get("flimitation") != null && StringUtils.isNotBlank(amap.get("flimitation").toString())) {
				goodsSkuDTO.setLimitation((Integer) amap.get("flimitation"));
			}

			// sku全部规格1信息息
			if (amap.get("cid") != null && StringUtils.isNotBlank(amap.get("cid").toString())) {
				if (!goodsValue1.containsKey(amap.get("cid").toString())) {
					goodsSkuValueDTO1 = new GoodsSkuValueDTO();
					goodsSkuValueDTO1.setSkuValueId(amap.get("cid").toString());
					goodsSkuValueDTO1.setSkuValueName(amap.get("cfvalue").toString());
					goodsValue1List.add(goodsSkuValueDTO1);
				}
				goodsValue1.put(amap.get("cid").toString(), amap.get("cfvalue").toString());
			}

			// sku全部规格2信息
			if (amap.get("fid") != null && StringUtils.isNotBlank(amap.get("fid").toString())) {
				if (!goodsValue2.containsKey(amap.get("fid").toString())) {
					goodsSkuValueDTO2 = new GoodsSkuValueDTO();
					goodsSkuValueDTO2.setSkuValueId(amap.get("fid").toString());
					goodsSkuValueDTO2.setSkuValueName(amap.get("tfvalue").toString());
					goodsValue2List.add(goodsSkuValueDTO2);
				}
				goodsValue2.put(amap.get("fid").toString(), amap.get("tfvalue").toString());
				goodsDetailDTO.setSingleSku(false);
			} else {
				goodsDetailDTO.setSingleSku(true);
			}
			if (amap.get("cfextendClassName") != null
					&& StringUtils.isNotBlank(amap.get("cfextendClassName").toString())) {
				goodsValueDTO.setGoodsValueName1(amap.get("cfextendClassName").toString());
			}
			if (amap.get("ffextendClassName") != null
					&& StringUtils.isNotBlank(amap.get("ffextendClassName").toString())) {
				goodsValueDTO.setGoodsValueName2(amap.get("ffextendClassName").toString());
			}

			goodsSkuDTOList.add(goodsSkuDTO);
		}
		
		if (allStock <= 0) {
			goodsDetailDTO.setIfInStock(false);
		}

		goodsValueDTO.setGoodsValue1(goodsValue1List);
		goodsValueDTO.setGoodsValue2(goodsValue2List);

		responseDTO.setData(returnData);
		goodsDetailDTO.setCouponString(couponString);
		returnData.put("goodsDetailDTO", goodsDetailDTO);
		returnData.put("goodsSkuList", goodsSkuDTOList);
		returnData.put("goodsValueDTO", goodsValueDTO);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("商品详情加载成功！");
		return responseDTO;
	}

	public ResponseDTO favoriteEvent(Integer clientType, String ticket, String eventId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(eventId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("eventId参数不能为空，请检查eventId的传递参数值！");
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
		TFavorite tFavorite = favoriteDAO.getFavoriteEventByCustomerIdAndObejctId(customerDTO.getCustomerId(), eventId);
		if (tFavorite == null) {
			flag = true;
			tFavorite = new TFavorite();
			tFavorite.setTCustomer(new TCustomer(customerDTO.getCustomerId()));
			tFavorite.setFobjectId(eventId);
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
			responseDTO.setMsg("活动收藏成功！");
		} else {
			responseDTO.setMsg("活动取消收藏成功！");
		}
		return responseDTO;
	}

	/**
	 * 商品标签列表
	 * 
	 * @param valueMap
	 * @param page
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getGoodsTagList(Integer cityId, String key, Integer goodstag, Integer sellModel, Integer orderBy,
			Integer pageSize, Integer offset, String sessionId, String ticket, Integer clientType) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (cityId == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("cityId参数不能为空，请检查cityId的传递参数值！");
			return responseDTO;
		}

		if (goodstag == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("goodstag参数不能为空，请检查goodstag的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = null;
		if (StringUtils.isNotBlank(ticket)) {
			customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
			if (!customerDTO.isEnable()) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(customerDTO.getStatusCode());
				responseDTO.setMsg(customerDTO.getMsg());
				return responseDTO;
			}
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
		if (orderBy.intValue() == 4) {
			hql.append(
					"select t.id as id, t.ftitle as ftitle, s.fname as ffullName, t.ftypeA as ftypeA ,t.fbrief as fbrief, t.fimage1 as fimage1, t.fprice as fprice, t.fpriceMoney as fpriceMoney, t.fstatus as fstatus,t.foffSaleTime as foffSaleTime ")
					.append(",t.fspec as fspec,t.fsellModel as fsellModel, t.fspecModel as fSpecModel, t.fpromotionModel as fpromotionModel,t.flimitation as fLimitation,t.fstock as fstock,t.fsecondKill as fsecondKill, dt.fdistance as fdistance from TEvent t left join t.TSponsor s left join TEventDistanceTemp as dt on s.id = dt.feventId and dt.fhsid = :sessionId and t.fgoodsTag = :goodstag and t.fstatus = 20");
			hqlMap.put("sessionId", sessionId);
			hqlMap.put("goodstag", goodstag);
		} else {
			hql.append(
					"select t.id as id, t.ftitle as ftitle, t.TSponsor.fname as ffullName, t.ftypeA as ftypeA ,t.fbrief as fbrief, t.fimage1 as fimage1, t.fprice as fprice, t.fpriceMoney as fpriceMoney, t.fstatus as fstatus,t.foffSaleTime as foffSaleTime ")
					.append(",t.fspec as fspec,t.fsellModel as fsellModel, t.fspecModel as fSpecModel, t.fpromotionModel as fpromotionModel,t.flimitation as fLimitation,t.fstock as fstock,t.fsecondKill as fsecondKill from TEvent t where t.fgoodsTag = :goodstag and t.fstatus = 20");
			hqlMap.put("goodstag", goodstag);
		}
		if (StringUtils.isNotBlank(key)) {
			hql.append(" and (t.ftitle like :key )");
			hqlMap.put("key", "%" + key + "%");
		}
		if (sellModel != null) {
			hql.append(" and t.fsellModel = :sellModel");
			hqlMap.put("sellModel", sellModel);
		}
		if (orderBy.intValue() != 0) {
			if (orderBy.intValue() == 1) {
				hql.append(" order by t.fsaleTotal desc");
			} else if (orderBy.intValue() == 2) {
				hql.append(" order by t.fdeal asc");
			} else if (orderBy.intValue() == 3) {
				hql.append(" order by t.fdeal desc");
			} else if (orderBy.intValue() == 4) {
				hql.append(" order by dt.fdistance desc");
			}
		} else {
			hql.append(" order by t.fupdateTime desc");
		}
		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();

		JavaType jt = mapper.contructMapType(HashMap.class, String.class, Integer.class);
		Map<String, Integer> goodsMap = Maps.newHashMap();
		if (customerDTO != null) {
			try {
				goodsMap = mapper.fromJson(redisService.getValue(customerDTO.getCustomerId(), RedisMoudel.shoppingcar),
						jt);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
			if (goodsMap != null && goodsMap.containsKey(amap.get("id").toString())) {
				goodsDTO.setCount(goodsMap.get(amap.get("id").toString()));
			}
			goodsDTO.setGoodsTitle(amap.get("ftitle").toString());
			if (amap.get("ftypeA") != null && StringUtils.isNotBlank(amap.get("ftypeA").toString())) {
				type = categoryService.getCategoryA((Integer) amap.get("ftypeA"));
				goodsDTO.setType(type != null ? type : StringUtils.EMPTY);
			}
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
			if (amap.get("fPromotionModel") != null && StringUtils.isNotBlank(amap.get("fPromotionModel").toString())) {
				goodsDTO.setPromotionModel(((Integer) amap.get("fPromotionModel")).intValue());
			}
			if (amap.get("fstock") != null && StringUtils.isNotBlank(amap.get("fstock").toString())) {
				if (((Integer) amap.get("fstock")).intValue() <= 0) {
					goodsDTO.setIfInStock(false);
				}
			}
			ele = sessionIdEventDistanceCache.get(sessionId);
			if (ele != null) {
				Map<String, EventDistanceDTO> eventDistanceMap = (Map<String, EventDistanceDTO>) ele.getObjectValue();
				EventDistanceDTO eventDistanceDTO = eventDistanceMap.get(goodsDTO.getGoodsId());
				if (eventDistanceDTO != null) {
					goodsDTO.setDistance(eventDistanceDTO.getDistanceString());
				}
			} else if (amap.get("fdistance") != null && StringUtils.isNotBlank(amap.get("fdistance").toString())) {
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
			responseDTO.setMsg("没有检索到您查找的活动！");
		}
		return responseDTO;
	}

	/**
	 * 商品列表
	 * 
	 * @param valueMap
	 * @param page
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getSpikeGoods(Integer cityId, String ticket, Integer clientType) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (cityId == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("cityId参数不能为空，请检查cityId的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = null;
		if (StringUtils.isNotBlank(ticket)) {
			customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
			if (!customerDTO.isEnable()) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(customerDTO.getStatusCode());
				responseDTO.setMsg(customerDTO.getMsg());
				return responseDTO;
			}
		}

		List<TColumnBanner> banners = columnBannerDAO.findByType(2);

		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = Maps.newHashMap();
		hql.append(
				"select t.id as id, t.ftitle as ftitle,t.fsubTitle as fsubTitle, t.fbrief as fbrief, t.fimage1 as fimage1, t.fprice as fprice,")
				.append("t.fpriceMoney as fpriceMoney,t.fspec as fspec,t.fstock as fstock,t.ftotal as ftotal,s.ftype as ftype,")
				.append("s.ftodaySeckillType as ftodaySeckillType,s.fgoodstatus as fgoodstatus,s.fgiftImageUrl as fgiftImageUrl")
				.append(" from TSeckillModule s inner join TEvent t on t.id = s.fgoodsId");
		hql.append(" where s.fgoodstatus = 20");
		hql.append(" order by s.fgoodsUpdateTime desc");
		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);

		SpikeDTO spikeDTO = null;
		List<SpikeDTO> spikeDTOs = Lists.newArrayList();
		SpikeGoodsDTO spikeGoodsDTO = null;
		List<SpikeGoodsDTO> spikeGoodsDTOs = null;
		for (TColumnBanner bannner : banners) {
			spikeGoodsDTOs = Lists.newArrayList();
			spikeDTO = new SpikeDTO();
			spikeDTO.setSeckillTypeTitle(DictionaryUtil.getString(DictionaryUtil.TodaySeckillType, bannner.getFtag()));
			if (bannner.getFtag().intValue() != 1) {
				spikeDTO.setSeckillTypeimageUrl(fxlService.getImageUrl(bannner.getFimageUrl(), false));
			}

			for (Map<String, Object> amap : list) {
				if (((Integer) amap.get("ftodaySeckillType")).intValue() == bannner.getFtag().intValue()) {
					spikeGoodsDTO = new SpikeGoodsDTO();
					if (amap.get("id") != null && StringUtils.isNotBlank(amap.get("id").toString())) {
						spikeGoodsDTO.setGoodsId(amap.get("id").toString());
					}
					if (amap.get("fbrief") != null && StringUtils.isNotBlank(amap.get("fbrief").toString())) {
						spikeGoodsDTO.setDesc(amap.get("fbrief").toString());
					}
					if (amap.get("fgoodstatus") != null && StringUtils.isNotBlank(amap.get("fgoodstatus").toString())) {
						spikeGoodsDTO.setStatus(Integer.parseInt(amap.get("fgoodstatus").toString()));
					}
					if (amap.get("ftitle") != null && StringUtils.isNotBlank(amap.get("ftitle").toString())) {
						spikeGoodsDTO.setGoodsTitle(amap.get("ftitle").toString());
					}
					if (amap.get("ftype") != null && StringUtils.isNotBlank(amap.get("ftype").toString())) {
						spikeGoodsDTO.setGiftModel((Integer) amap.get("ftype"));
					}
					if (amap.get("fimage1") != null && StringUtils.isNotBlank(amap.get("fimage1").toString())) {
						spikeGoodsDTO.setImageUrl(fxlService.getImageUrl(amap.get("fimage1").toString(), false));
					}
					if (amap.get("fprice") != null && StringUtils.isNotBlank(amap.get("fprice").toString())) {
						spikeGoodsDTO.setOriginalPrice(amap.get("fprice").toString());
					}
					if (amap.get("fpriceMoney") != null && StringUtils.isNotBlank(amap.get("fpriceMoney").toString())) {
						spikeGoodsDTO.setPresentPrice(amap.get("fpriceMoney").toString());
					}
					if (amap.get("fspec") != null && StringUtils.isNotBlank(amap.get("fspec").toString())) {
						spikeGoodsDTO.setSpec(amap.get("fspec").toString());
					}
					if (amap.get("fgiftImageUrl") != null
							&& StringUtils.isNotBlank(amap.get("fgiftImageUrl").toString())) {
						spikeGoodsDTO.setGiftImageUrl(amap.get("fgiftImageUrl").toString());
					}
					if (amap.get("fstock") != null && StringUtils.isNotBlank(amap.get("fstock").toString())) {
						if (((Integer) amap.get("fstock")).intValue() > 0) {
							spikeGoodsDTO.setIfInStock(true);
						}
					}
					spikeGoodsDTOs.add(spikeGoodsDTO);
				}
			}
			spikeDTO.setSpikeGoodsDTOList(spikeGoodsDTOs);
			spikeDTOs.add(spikeDTO);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("spikeDTOs", spikeDTOs);
		responseDTO.setData(returnData);
		responseDTO.setMsg("成功获取秒杀商品！");
		return responseDTO;
	}

	public String getGoodsSku(String goodsSkuId) {
		StringBuilder goodsSku = new StringBuilder();
		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = Maps.newHashMap();

		hql.append("select t.id as skuId,").append("c.fvalue as cfvalue,c.fextendClassName as cfextendClassName,")
				.append("f.fextendClassName as ffextendClassName,f.fvalue as tfvalue ")
				.append("from TGoodsSku t inner join TGoodsTypeClassValue c ")
				.append("on t.fclassTypeValue1 = c.id left join TGoodsTypeClassValue f on t.fclassTypeValue2 = f.id");
		if (StringUtils.isNotBlank(goodsSkuId)) {
			hql.append(" and t.id = :goodsSkuId");
			hqlMap.put("goodsSkuId", goodsSkuId);
		}

		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);
		for (Map<String, Object> amap : list) {
			if (amap.get("cfvalue") != null && StringUtils.isNotBlank(amap.get("cfvalue").toString())) {
				goodsSku.append(amap.get("fextendClassName").toString()).append(":");
			}
			if (amap.get("cfvalue") != null && StringUtils.isNotBlank(amap.get("cfvalue").toString())) {
				goodsSku.append(amap.get("cfvalue").toString()).append(" ");
			}
			if (amap.get("ffextendClassName") != null
					&& StringUtils.isNotBlank(amap.get("ffextendClassName").toString())) {
				goodsSku.append(amap.get("ffextendClassName").toString()).append(":");
			}
			if (amap.get("tfvalue") != null && StringUtils.isNotBlank(amap.get("tfvalue").toString())) {
				goodsSku.append(amap.get("tfvalue").toString());
			}
		}

		return goodsSku.toString();
	}

	public String getGoodsSku(Long fclassTypeValue1, Long fclassTypeValue2) {
		StringBuilder goodsSku = new StringBuilder();
		TGoodsTypeClassValue tGoodsTypeClassValue1 = null;
		tGoodsTypeClassValue1 = goodsTypeClassValueDAO.findOne(fclassTypeValue1);
		TGoodsTypeClassValue tGoodsTypeClassValue2 = null;
		tGoodsTypeClassValue2 = goodsTypeClassValueDAO.findOne(fclassTypeValue2);
		if (tGoodsTypeClassValue1 != null) {
			goodsSku.append(tGoodsTypeClassValue1.getFextendClassName()).append(":");
			goodsSku.append(tGoodsTypeClassValue1.getFvalue()).append(" ");
		}
		if (tGoodsTypeClassValue2 != null) {
			goodsSku.append(tGoodsTypeClassValue2.getFextendClassName()).append(":");
			goodsSku.append(tGoodsTypeClassValue2.getFvalue());
		}
		return goodsSku.toString();
	}

	public void backStock(List<TOrderGoods> tOrderGoodsList) {
		TGoodsSku tGoodsSku = null;
		for (TOrderGoods orderGoods : tOrderGoodsList) {
			eventDAO.backStock(orderGoods.getFcount(), orderGoods.getFeventId());
			tGoodsSku = goodsSkuDAO.findOne(orderGoods.getFgoodsSkuId());
			goodsSkuDAO.backStock(orderGoods.getFcount(), orderGoods.getFgoodsSkuId());
			tGoodsSku.setFstock(tGoodsSku.getFstock() + orderGoods.getFcount());
			redisService.putCache(RedisMoudel.goodsSku, orderGoods.getFgoodsSkuId(), mapper.toJson(tGoodsSku));
		}
	}

	public void subtractStock(List<TOrderGoods> tOrderGoodsList) {
		TGoodsSku tGoodsSku = null;
		for (TOrderGoods orderGoods : tOrderGoodsList) {
			eventDAO.subtractStock(orderGoods.getFcount(), orderGoods.getFeventId());
			tGoodsSku = goodsSkuDAO.findOne(orderGoods.getFgoodsSkuId());
			goodsSkuDAO.subtractStock(orderGoods.getFcount(), orderGoods.getFgoodsSkuId());
			tGoodsSku.setFstock(tGoodsSku.getFstock() - orderGoods.getFcount());
			redisService.putCache(RedisMoudel.goodsSku, orderGoods.getFgoodsSkuId(), mapper.toJson(tGoodsSku));
		}
	}
	
	
	public TGoodsSku getGoodsSkuCache(String goodsSkuId){
		String goodsSku = null;
		TGoodsSku tGoodsSku = null;
		try {
			goodsSku = redisService.getValue(goodsSkuId, RedisMoudel.goodsSku);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(StringUtils.isNotBlank(goodsSku)){
			tGoodsSku = mapper.fromJson(goodsSku, TGoodsSku.class);
		}else{
			tGoodsSku = goodsSkuDAO.findOne(goodsSkuId);
		}
		return tGoodsSku;
	}

}