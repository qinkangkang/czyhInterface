package com.innee.czyhInterface.service.v1.sponsor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.internal.QueryImpl;
import org.hibernate.transform.Transformers;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.dao.FavoriteDAO;
import com.innee.czyhInterface.dto.CustomerDTO;
import com.innee.czyhInterface.dto.EventDistanceDTO;
import com.innee.czyhInterface.dto.m.PageDTO;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.dto.m.goods.SponsorGoodsDTO;
import com.innee.czyhInterface.dto.m.sponsor.SponsorDTO;
import com.innee.czyhInterface.dto.m.sponsor.SponsorDetailDTO;
import com.innee.czyhInterface.entity.TEvent;
import com.innee.czyhInterface.entity.TFavorite;
import com.innee.czyhInterface.entity.TSponsor;
import com.innee.czyhInterface.service.v2.CommonService;
import com.innee.czyhInterface.service.v2.FxlService;
import com.innee.czyhInterface.util.CommonPage;
import com.innee.czyhInterface.util.Constant;
import com.innee.czyhInterface.util.DictionaryUtil;
import com.innee.czyhInterface.util.PropertiesUtil;
import com.innee.czyhInterface.util.bmap.BmapUtil;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * 商户管理
 * 
 * @author jinshengzhi
 */
@Component
@Transactional
public class SponsorService {

	private static final Logger logger = LoggerFactory.getLogger(SponsorService.class);

	@Autowired
	private CommonService commonService;

	@Autowired
	private FxlService fxlService;

	@Autowired
	private FavoriteDAO favoriteDAO;

	@Autowired
	private CacheManager cacheManager;

	@Transactional(readOnly = true)
	public ResponseDTO getSponsorList(Integer goodsTag, Integer orderBy, Integer pageSize, Integer offset,
			String sessionId) {

		ResponseDTO responseDTO = new ResponseDTO();

		if (goodsTag == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("tag参数不能为空，请检查tag的传递参数值！");
			return responseDTO;
		}

		if (orderBy == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("orderBy参数不能为空，请检查orderBy的传递参数值！");
			return responseDTO;
		}

		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}

		List<SponsorDTO> sponsorList = Lists.newArrayList();

		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = Maps.newHashMap();
		if (orderBy.intValue() == 1) {
			hql.append(
					"select t.id as id ,t.fname as fname,t.fimage as fimage,t.fscore as fscore,t.fsponsorTag as fsponsorTag,t.fregion as fregion ")
					.append(",t.fperPrice as fperPrice,dt.fdistance as fdistance,t.ftermValidity as ftermValidity,t.fexceptionDate as fexceptionDate ")
					.append(",t.fuseDate as fuseDate,t.freminder as freminder,t.ftype as ftype")
					.append(" from TSponsor t left join TEventDistanceTemp as dt on t.id = dt.feventId and dt.fhsid = :sessionId where t.fstatus<999");
			hqlMap.put("sessionId", sessionId);
		} else {
			hql.append(
					"select t.id as id ,t.fname as fname,t.fimage as fimage,t.fscore as fscore,t.fsponsorTag as fsponsorTag,t.fregion as fregion ")
					.append(",t.fperPrice as fperPrice,t.ftermValidity as ftermValidity,t.fexceptionDate as fexceptionDate ")
					.append(",t.fuseDate as fuseDate,t.freminder as freminder,t.ftype as ftype")
					.append(" from TSponsor t where t.fstatus<999");
		}
		if (goodsTag.intValue() != 0) {
			hql.append(" and t.fsponsorTag = :goodsTag");
			hqlMap.put("goodsTag", goodsTag);
		}
		if (orderBy.intValue() == 1) {
			hql.append(" order by dt.fdistance");
		} else {
			hql.append(" order by t.fcreateTime");
		}

		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();

		// 获取活动距离信息缓存对象
		Cache sessionIdEventDistanceCache = cacheManager.getCache(Constant.SessionIdEventDistance);
		Element ele = null;
		SponsorDTO sponsorDTO = null;
		BigDecimal distance = null;
		int statusValue = 0;
		for (Map<String, Object> amap : list) {
			sponsorDTO = new SponsorDTO();
			if (amap.get("id") != null && StringUtils.isNotBlank(amap.get("id").toString())) {
				sponsorDTO.setSponsorId(amap.get("id").toString());
			}
			if (amap.get("ftype") != null && StringUtils.isNotBlank(amap.get("ftype").toString())) {
				statusValue = (Integer.parseInt(amap.get("ftype").toString()));
				sponsorDTO.setTag(DictionaryUtil.getString(DictionaryUtil.SponsorTag, statusValue));
			}

			if (amap.get("fregion") != null && StringUtils.isNotBlank(amap.get("fregion").toString())) {
				sponsorDTO.setDistrict(amap.get("fregion").toString());
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

			ele = sessionIdEventDistanceCache.get(sessionId);
			if (ele != null) {
				Map<String, EventDistanceDTO> eventDistanceMap = (Map<String, EventDistanceDTO>) ele.getObjectValue();
				EventDistanceDTO eventDistanceDTO = eventDistanceMap.get(amap.get("id").toString());
				if (eventDistanceDTO != null) {
					sponsorDTO.setDistance(eventDistanceDTO.getDistanceString());
				}
			} else if (amap.get("fdistance") != null && StringUtils.isNotBlank(amap.get("fdistance").toString())) {
				distance = new BigDecimal((Integer) amap.get("fdistance")).negate().divide(new BigDecimal(1000), 1,
						RoundingMode.HALF_UP);
				sponsorDTO.setDistance(distance.toString() + "km");
			}
			sponsorList.add(sponsorDTO);
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("sponsorList", sponsorList);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("商家列表加载成功！");
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getGoodsBySponsor(String sponsorId, Integer pageSize, Integer offset) {

		ResponseDTO responseDTO = new ResponseDTO();

		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}

		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = Maps.newHashMap();

		hql.append(
				"select t.id as id, t.ftitle as ftitle, t.TSponsor.fname as ffullName, t.ftypeA as ftypeA ,t.fbrief as fbrief,t.fimage1 as fimage1, t.fprice as fprice, t.fpriceMoney as fpriceMoney, t.fstatus as fstatus,t.foffSaleTime as foffSaleTime ")
				.append(",t.fspec as fspec,t.fsellModel as fsellModel,t.fprice as fprice, t.fpriceMoney as fpriceMoney,t.fspecModel as fSpecModel, t.fpromotionModel as fpromotionModel,t.flimitation as fLimitation,t.fstock as fstock from TEvent t ");
		hql.append(" where t.fstatus = 20");
		hql.append(" and t.TSponsor.id = :sponsorId");
		hqlMap.put("sponsorId", sponsorId);
		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();

		List<SponsorGoodsDTO> resultList = Lists.newArrayList();
		SponsorGoodsDTO goodsDTO = null;
		for (Map<String, Object> amap : list) {
			goodsDTO = new SponsorGoodsDTO();
			goodsDTO.setGoodsId(amap.get("id").toString());
			goodsDTO.setGoodsTitle(amap.get("ftitle").toString());

			if (amap.get("foffSaleTime") != null && StringUtils.isNotBlank(amap.get("foffSaleTime").toString())) {
				Integer second = Seconds
						.secondsBetween(new DateTime(new Date()), new DateTime(amap.get("foffSaleTime"))).getSeconds();
				goodsDTO.setGoodsTime(second.toString());
			}
			if (amap.get("fimage1") != null && StringUtils.isNotBlank(amap.get("fimage1").toString())) {
				goodsDTO.setImageUrl(fxlService.getImageUrl(amap.get("fimage1").toString(), false));
			} else {
				goodsDTO.setImageUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
						.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPic.jpg").toString());
			}
			if (amap.get("fbrief") != null && StringUtils.isNotBlank(amap.get("fbrief").toString())) {
				if (amap.get("fbrief").toString().length() >= 15) {
					goodsDTO.setDesc(amap.get("fbrief").toString().substring(0, 15));
				} else {
					goodsDTO.setDesc(amap.get("fbrief").toString());
				}
			}
			if (amap.get("fspec") != null && StringUtils.isNotBlank(amap.get("fspec").toString())) {
				goodsDTO.setSpec(amap.get("fspec").toString());
			}
			if (amap.get("fprice") != null && StringUtils.isNotBlank(amap.get("fprice").toString())) {
				goodsDTO.setOriginalPrice(amap.get("fprice").toString());
			} else {
				goodsDTO.setOriginalPrice("0");
			}
			if (amap.get("fpriceMoney") != null && StringUtils.isNotBlank(amap.get("fpriceMoney").toString())) {
				goodsDTO.setPresentPrice(amap.get("fpriceMoney").toString());
			} else {
				goodsDTO.setPresentPrice("0");
			}
			resultList.add(goodsDTO);
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("SponsorGoodsList", resultList);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("商户商品列表加载成功！");
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getSponsorDetail(String sponsorId, String ticket, Integer clientType) {

		ResponseDTO responseDTO = new ResponseDTO();

		if (StringUtils.isBlank(sponsorId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("sponsorId参数不能为空，请检查sponsorId的传递参数值！");
			return responseDTO;
		}

		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = Maps.newHashMap();

		hql.append(
				"select t.id as id ,t.fname as fname,t.foffSaleTime as foffSaleTime,t.fimage as fimage,t.fimages as fimages, t.fscore as fscore,t.fsponsorTag as fsponsorTag ")
				.append(",t.fperPrice as fperPrice,t.ftermValidity as ftermValidity,t.fexceptionDate as fexceptionDate,t.fregion as fregion,t.fgps as fgps ")
				.append(",t.fuseDate as fuseDate, t.ftype as ftype,t.freminder as freminder,t.faddress as faddress,t.fphone as fphone,t.fbrief as fbrief")
				.append(" from TSponsor t where t.fstatus<999");

		if (StringUtils.isNotBlank(sponsorId)) {
			hql.append(" and t.id = :sponsorId");
			hqlMap.put("sponsorId", sponsorId);
		}

		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);

		SponsorDetailDTO sponsorDetailDTO = null;
		int statusValue = 0;
		sponsorDetailDTO = new SponsorDetailDTO();
		Map<String, Object> amap = list.get(0);
		if (amap.get("id") != null && StringUtils.isNotBlank(amap.get("id").toString())) {
			sponsorDetailDTO.setSponsorId(amap.get("id").toString());
		}
		if (amap.get("ftype") != null && StringUtils.isNotBlank(amap.get("ftype").toString())) {
			statusValue = (Integer.parseInt(amap.get("ftype").toString()));
			sponsorDetailDTO.setTag(DictionaryUtil.getString(DictionaryUtil.SponsorTag, statusValue));
		}

		if (amap.get("fperPrice") != null && StringUtils.isNotBlank(amap.get("fperPrice").toString())) {
			sponsorDetailDTO.setPerPrice(amap.get("fperPrice").toString());
		}
		if (amap.get("fscore") != null && StringUtils.isNotBlank(amap.get("fscore").toString())) {
			sponsorDetailDTO.setScore(amap.get("fscore").toString());
		}

		if (amap.get("fregion") != null && StringUtils.isNotBlank(amap.get("fregion").toString())) {
			sponsorDetailDTO.setRegion(amap.get("fregion").toString());
		}
		if (amap.get("fimage") != null && StringUtils.isNotBlank(amap.get("fimage").toString())) {
			sponsorDetailDTO.setSponsorIcon(fxlService.getImageUrl(amap.get("fimage").toString(), true));
		}
		if (amap.get("fimages") != null && StringUtils.isNotBlank(amap.get("fimages").toString())) {
			sponsorDetailDTO.setSponsorImage(fxlService.getImageUrls(amap.get("fimages").toString(), false));
		}

		if (amap.get("fname") != null && StringUtils.isNotBlank(amap.get("fname").toString())) {
			sponsorDetailDTO.setSponsorName(amap.get("fname").toString());
		}

		if (amap.get("ftermValidity") != null && StringUtils.isNotBlank(amap.get("ftermValidity").toString())) {
			sponsorDetailDTO.setValidityDate(amap.get("ftermValidity").toString());
		}

		if (amap.get("fgps") != null && StringUtils.isNotBlank(amap.get("fgps").toString())) {
			sponsorDetailDTO.setGps(BmapUtil.baiduToGaoDe(amap.get("fgps").toString()));
		}

		if (amap.get("fexceptionDate") != null && StringUtils.isNotBlank(amap.get("fexceptionDate").toString())) {
			sponsorDetailDTO.setExceptDate(amap.get("fexceptionDate").toString());
		}

		if (amap.get("fuseDate") != null && StringUtils.isNotBlank(amap.get("fuseDate").toString())) {
			sponsorDetailDTO.setUseDate(amap.get("fuseDate").toString());
		}

		if (amap.get("foffSaleTime") != null && StringUtils.isNotBlank(amap.get("foffSaleTime").toString())) {
			Integer second = Seconds
					.secondsBetween(new DateTime(new Date()), new DateTime((Date) amap.get("foffSaleTime")))
					.getSeconds();
			if (second.intValue() >= 0) {
				sponsorDetailDTO.setSecondKill(second.toString());
			}
		}

		if (amap.get("freminder") != null && StringUtils.isNotBlank(amap.get("freminder").toString())) {
			sponsorDetailDTO.setWarmPrompt(amap.get("freminder").toString());
		}

		if (amap.get("faddress") != null && StringUtils.isNotBlank(amap.get("faddress").toString())) {
			sponsorDetailDTO.setAddress(amap.get("faddress").toString());
		}

		if (amap.get("fphone") != null && StringUtils.isNotBlank(amap.get("fphone").toString())) {
			sponsorDetailDTO.setPhone(amap.get("fphone").toString());
		}

		if (amap.get("fbrief") != null && StringUtils.isNotBlank(amap.get("fbrief").toString())) {
			sponsorDetailDTO.setSponsorBrief(amap.get("fbrief").toString());
		}

		CustomerDTO customerDTO = null;

		if (StringUtils.isNotBlank(ticket)) {
			customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
			if (customerDTO != null && customerDTO.isEnable()) {
				TFavorite tFavorite = favoriteDAO.getFavoriteMerchantByCustomerIdAndObejctId(
						customerDTO.getCustomerId(), amap.get("id").toString());
				if (tFavorite != null) {
					sponsorDetailDTO.setFavorite(true);
				} else {
					sponsorDetailDTO.setFavorite(false);
				}
			} else {
				sponsorDetailDTO.setFavorite(false);
			}
		} else {
			sponsorDetailDTO.setFavorite(false);
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("sponsorDetail", sponsorDetailDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("商户详情加载成功！");
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getGoodsListBySponsor(String sponsorId) {
		ResponseDTO responseDTO = new ResponseDTO();

		CommonPage page = new CommonPage();
		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = Maps.newHashMap();

		hql.append(
				"select t.id as id, t.ftitle as ftitle, t.TSponsor.fname as ffullName, t.ftypeA as ftypeA ,t.fbrief as fbrief,t.fimage1 as fimage1, t.fprice as fprice, t.fpriceMoney as fpriceMoney, t.fstatus as fstatus,t.foffSaleTime as foffSaleTime ")
				.append(",t.fspec as fspec,t.fsellModel as fsellModel,t.fprice as fprice, t.fpriceMoney as fpriceMoney,t.fspecModel as fSpecModel, t.fpromotionModel as fpromotionModel,t.flimitation as fLimitation,t.fstock as fstock from TEvent t ");
		hql.append(" where t.fstatus = 20");
		hql.append(" and t.TSponsor.id = :sponsorId");

		hqlMap.put("sponsorId", sponsorId);

		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();

		List<SponsorGoodsDTO> resultList = Lists.newArrayList();
		SponsorGoodsDTO goodsDTO = null;
		for (Map<String, Object> amap : list) {
			goodsDTO = new SponsorGoodsDTO();
			goodsDTO.setGoodsId(amap.get("id").toString());
			goodsDTO.setGoodsTitle(amap.get("ftitle").toString());

			if (amap.get("foffSaleTime") != null && StringUtils.isNotBlank(amap.get("foffSaleTime").toString())) {
				Integer second = Seconds
						.secondsBetween(new DateTime(new Date()), new DateTime(amap.get("foffSaleTime"))).getSeconds();
				goodsDTO.setGoodsTime(second.toString());
			}
			if (amap.get("fimage1") != null && StringUtils.isNotBlank(amap.get("fimage1").toString())) {
				goodsDTO.setImageUrl(fxlService.getImageUrl(amap.get("fimage1").toString(), false));
			} else {
				goodsDTO.setImageUrl(new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
						.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPic.jpg").toString());
			}
			if (amap.get("fbrief") != null && StringUtils.isNotBlank(amap.get("fbrief").toString())) {
				if (amap.get("fbrief").toString().length() >= 15) {
					goodsDTO.setDesc(amap.get("fbrief").toString().substring(0, 15));
				} else {
					goodsDTO.setDesc(amap.get("fbrief").toString());
				}
			}
			if (amap.get("fspec") != null && StringUtils.isNotBlank(amap.get("fspec").toString())) {
				goodsDTO.setSpec(amap.get("fspec").toString());
			}
			if (amap.get("fprice") != null && StringUtils.isNotBlank(amap.get("fprice").toString())) {
				goodsDTO.setOriginalPrice(amap.get("fprice").toString());
			} else {
				goodsDTO.setOriginalPrice("0");
			}
			if (amap.get("fpriceMoney") != null && StringUtils.isNotBlank(amap.get("fpriceMoney").toString())) {
				goodsDTO.setPresentPrice(amap.get("fpriceMoney").toString());
			} else {
				goodsDTO.setPresentPrice("0");
			}
			resultList.add(goodsDTO);
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("SponsorGoodsList", resultList);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("商户商品列表加载成功！");
		return responseDTO;

	}

	/**
	 * 获得自提区的商户列表(商户的前3个商品)
	 * 
	 * @param goodsTag
	 * @param orderBy
	 * @param pageSize
	 * @param offset
	 * @param sellModel
	 * @param searchKey
	 * @param id
	 * @return
	 */

	@Transactional(readOnly = true)
	public ResponseDTO getSponsorAndGoodsList(Integer orderBy, Integer pageSize, Integer offset, Integer sellModel,
			String searchKey, String sessionId) {

		ResponseDTO responseDTO = new ResponseDTO();

		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}

		List<SponsorDTO> sponsorList = Lists.newArrayList();

		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = Maps.newHashMap();

		// 拼接查找条件 0:综合; 1:价格从低到高；2：价格从高到低； 3： 评分高到低

		hql.append(
				"select t.id as id ,t.fname as fname,t.fimage as fimage,t.fscore as fscore,t.fsponsorTag as fsponsorTag,t.fregion as fregion ")
				.append(",t.fperPrice as fperPrice,dt.fdistance as fdistance,t.ftermValidity as ftermValidity,t.fexceptionDate as fexceptionDate ")
				.append(",t.fuseDate as fuseDate,t.freminder as freminder,t.ftype as ftype")
				.append(" from TSponsor t left join TEventDistanceTemp as dt on t.id = dt.feventId and dt.fhsid = :sessionId where t.fstatus<999 ");
		if (StringUtils.isNotBlank(searchKey) && StringUtils.isNotBlank(searchKey.trim())) {
			// hql.append(" and t.fsponsorModel=0");
			hql.append(" and  (t.fname like :searchKey  ");
			hql.append(" or t.ffullName like :searchKey ");
			hql.append(" or t.fbrief like :searchKey) ");
			hqlMap.put("searchKey", "%" + searchKey.trim() + "%");
		} else {
			hql.append(" and t.fsponsorModel=1");
		}

		hqlMap.put("sessionId", sessionId);

		if (orderBy.intValue() == 1) {
			hql.append(" order by t.fperPrice asc");
		} else if (orderBy.intValue() == 2) {
			hql.append(" order by t.fperPrice desc");
		} else if (orderBy.intValue() == 3) {
			hql.append(" order by t.fscore desc");
		} else if (orderBy.intValue() == 4) {
			hql.append(" order by t.fscore desc");
		} else {
			hql.append(" order by t.fcreateTime");
		}

		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();// 获得所有的商户
		List<SponsorGoodsDTO> goodsList = Lists.newArrayList();
		Cache sessionIdEventDistanceCache = cacheManager.getCache(Constant.SessionIdEventDistance);
		Element ele = null;
		BigDecimal distance = null;
		SponsorDTO sponsorDTO = null;
		int statusValue = 0;
		for (Map<String, Object> amap : list) {
			sponsorDTO = new SponsorDTO();
			if (amap.get("id") != null && StringUtils.isNotBlank(amap.get("id").toString())) {
				sponsorDTO.setSponsorId(amap.get("id").toString());
				// 依据商户查找对应的商品前3个
				goodsList = getGoodsBySponsor(amap.get("id").toString());
				sponsorDTO.setGoodsList(goodsList);
			}
			if (amap.get("ftype") != null && StringUtils.isNotBlank(amap.get("ftype").toString())) {
				statusValue = (Integer.parseInt(amap.get("ftype").toString()));
				sponsorDTO.setTag(DictionaryUtil.getString(DictionaryUtil.SponsorTag, statusValue));
			}

			if (amap.get("fregion") != null && StringUtils.isNotBlank(amap.get("fregion").toString())) {
				sponsorDTO.setDistrict(amap.get("fregion").toString());
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

			ele = sessionIdEventDistanceCache.get(sessionId);
			if (ele != null) {
				Map<String, EventDistanceDTO> eventDistanceMap = (Map<String, EventDistanceDTO>) ele.getObjectValue();
				EventDistanceDTO eventDistanceDTO = eventDistanceMap.get(amap.get("id").toString());
				if (eventDistanceDTO != null) {
					sponsorDTO.setDistance(eventDistanceDTO.getDistanceString());
				}
			} else if (amap.get("fdistance") != null && StringUtils.isNotBlank(amap.get("fdistance").toString())) {
				distance = new BigDecimal((Integer) amap.get("fdistance")).negate().divide(new BigDecimal(1000), 1,
						RoundingMode.HALF_UP);
				sponsorDTO.setDistance(distance.toString() + "km");
			}

			sponsorList.add(sponsorDTO);
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("sponsorList", sponsorList);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("商家列表加载成功！");
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public List<SponsorGoodsDTO> getGoodsBySponsor(String sponsorId) {

		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = Maps.newHashMap();

		hql.append(
				"select t.id as id, t.ftitle as ftitle, t.fsaleTotal as fsaleTotal, t.fprice as fprice, t.fpriceMoney as fpriceMoney from TEvent t ");
		hql.append(" where t.fstatus = 20 ");
		hql.append(" and t.TSponsor.id = :sponsorId ");
		hql.append(" order by t.fcreateTime desc ");
		// hql.append(" limit 0,3");
		hqlMap.put("sponsorId", sponsorId);
		Query q = commonService.createQuery(hql.toString(), hqlMap);
		q.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		q.setFirstResult(0).setMaxResults(3);
		List<Map<String, Object>> list = q.getResultList();

		List<SponsorGoodsDTO> resultList = Lists.newArrayList();
		SponsorGoodsDTO goodsDTO = null;
		for (Map<String, Object> amap : list) {
			goodsDTO = new SponsorGoodsDTO();
			goodsDTO.setGoodsId(amap.get("id").toString());
			goodsDTO.setGoodsTitle(amap.get("ftitle").toString());

			if (amap.get("ftitle") != null && StringUtils.isNotBlank(amap.get("ftitle").toString())) {
				goodsDTO.setGoodsTitle(amap.get("ftitle").toString());

			}
			if (amap.get("fprice") != null && StringUtils.isNotBlank(amap.get("fprice").toString())) {
				goodsDTO.setOriginalPrice(amap.get("fprice").toString());
			} else {
				goodsDTO.setOriginalPrice("0");
			}
			if (amap.get("fpriceMoney") != null && StringUtils.isNotBlank(amap.get("fpriceMoney").toString())) {
				goodsDTO.setPresentPrice(amap.get("fpriceMoney").toString());
			} else {
				goodsDTO.setPresentPrice("0");
			}

			if (amap.get("fsaleTotal") != null && StringUtils.isNotBlank(amap.get("fsaleTotal").toString())) {
				goodsDTO.setSaleTotal(Integer.valueOf(amap.get("fsaleTotal").toString()));
			} else {
				goodsDTO.setPresentPrice("0");
			}
			resultList.add(goodsDTO);
		}

		return resultList;
	}

}