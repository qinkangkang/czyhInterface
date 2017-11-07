package com.innee.czyhInterface.service.v1.findSaid;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.dao.ArticleDAO;
import com.innee.czyhInterface.dao.FavoriteDAO;
import com.innee.czyhInterface.dto.ArticleRecommendDTO;
import com.innee.czyhInterface.dto.CustomerDTO;
import com.innee.czyhInterface.dto.m.ArticleDTO;
import com.innee.czyhInterface.dto.m.PageDTO;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.entity.TArticle;
import com.innee.czyhInterface.entity.TFavorite;
import com.innee.czyhInterface.service.v2.AppService;
import com.innee.czyhInterface.service.v2.CommonService;
import com.innee.czyhInterface.service.v2.FxlService;
import com.innee.czyhInterface.util.CommonPage;
import com.innee.czyhInterface.util.Constant;
import com.innee.czyhInterface.util.DictionaryUtil;
import com.innee.czyhInterface.util.PropertiesUtil;
import com.innee.czyhInterface.util.RandomColorUtil;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

@Component
@Transactional
public class FindSaidService {

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
	private ArticleDAO articleDAO;

	@Autowired
	private FavoriteDAO favoriteDAO;

	@Transactional(readOnly = true)
	public ResponseDTO getArticleList(Integer cityId, Integer articleType, String ticket, Integer clientType,
			Integer pageSize, Integer offset) {
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
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hql.append(
				"select t.id as id, t.ftitle as ftitle, t.ftype as ftype, t.fimage as fimage, t.fbrief as fbrief, t.fdetailHtmlUrl as fdetailHtmlUrl, t.frecommend as frecommend, t.fcomment as fcomment, t.fcreateTime as fcreateTime,t.fartCity as fartCity from TArticle t where t.fcityId in (0 , :cityId) and t.fstatus = 20 and t.ftype = 1");
		hqlMap.put("cityId", cityId);
		if(articleType!=null){
			hql.append(" and t.fartType = :articleType ");
			hqlMap.put("articleType", articleType);
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
			if (amap.get("fartCity") != null && StringUtils.isNotBlank(amap.get("fartCity").toString())) {
				articleDTO.setAddress(amap.get("fartCity").toString());
			}
			// 返回文章是否收藏
			CustomerDTO customerDTO = null;
			if (StringUtils.isNotBlank(ticket)) {
				customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
				if (customerDTO != null && customerDTO.isEnable()) {
					TFavorite tFavorite = favoriteDAO.getFavoriteArticleIdByCustomerIdAndObejctId(
							customerDTO.getCustomerId(), amap.get("id").toString());
					if (tFavorite != null) {
						articleDTO.setFavorite(true);
					} else {
						articleDTO.setFavorite(false);
					}
				} else {
					articleDTO.setFavorite(false);
				}
			} else {
				articleDTO.setFavorite(false);
			}
			articleDTO.setFavoriteCount(favoriteDAO.getFavoriteCount(amap.get("id").toString()).intValue());
			articleList.add(articleDTO);
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("articleList", articleList);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("惠说列表加载成功！");
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getArticle(String articleId,String ticket,Integer clientType) {
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
		
		// 返回文章是否收藏
		CustomerDTO customerDTO = null;
		if (StringUtils.isNotBlank(ticket)) {
			customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
			if (customerDTO != null && customerDTO.isEnable()) {
				TFavorite tFavorite = favoriteDAO.getFavoriteArticleIdByCustomerIdAndObejctId(
						customerDTO.getCustomerId(), articleId);
				if (tFavorite != null) {
					articleDTO.setFavorite(true);
				} else {
					articleDTO.setFavorite(false);
				}
			} else {
				articleDTO.setFavorite(false);
			}
		} else {
			articleDTO.setFavorite(false);
		}
		articleDTO.setFavoriteCount(favoriteDAO.getFavoriteCount(articleId).intValue());
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("article", articleDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("文章详细信息加载成功！");
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

		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
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