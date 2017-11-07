package com.innee.czyhInterface.service.v1.category;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JavaType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.dao.EventCategoryDAO;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.dto.m.category.CategoryDTO;
import com.innee.czyhInterface.dto.m.category.SecondaryCategoryDTO;
import com.innee.czyhInterface.entity.TEventCategory;
import com.innee.czyhInterface.service.v1.system.RedisService;
import com.innee.czyhInterface.service.v2.FxlService;
import com.innee.czyhInterface.util.redis.RedisMoudel;

/**
 * 商品分类
 * 
 * @author jinsey
 *
 */
@Component
@Transactional
public class CategoryService {

	private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	private final static int levelNoe = 1;

	private final static int leveTwo = 2;

	@Autowired
	private RedisService redisService;

	@Autowired
	private EventCategoryDAO eventCategoryDAO;
	
	@Autowired
	private FxlService fxlService;

	/**
	 * 获取商品一级分类
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getPrimaryCategory() {
		ResponseDTO responseDTO = new ResponseDTO();
		List<CategoryDTO> categoryList = Lists.newArrayList();
		try {
			CategoryDTO categoryDTO = new CategoryDTO();
			String category = redisService.getValue("primaryCategory", RedisMoudel.category);
			if (StringUtils.isNotBlank(category)) {
				// 不等于空的情况下去redis中查询出缓存分类
				JavaType jt = mapper.contructCollectionType(List.class, CategoryDTO.class);
				categoryList = mapper.fromJson(category, jt);
			} else {
				// 如果为空的情况下添加缓存到redis
				List<TEventCategory> getCategoryList = eventCategoryDAO.findByLevel(levelNoe);
				for (TEventCategory tEventCategory : getCategoryList) {
					categoryDTO = new CategoryDTO();
					categoryDTO.setCategoryId(tEventCategory.getValue());
					categoryDTO.setCategoryAName(tEventCategory.getName());
					categoryDTO.setImageA(fxlService.getImageUrl(tEventCategory.getImageA(), false));
					categoryDTO.setImageB(fxlService.getImageUrl(tEventCategory.getImageB(), false));
					// categoryDTO.setUpdateTime("");
					categoryList.add(categoryDTO);
				}
				redisService.putCache(RedisMoudel.category, "primaryCategory", mapper.toJson(categoryList));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("categoryList", categoryList);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("商品一级分类加载成功！");
		return responseDTO;
	}

	/**
	 * 获取商品二级分类
	 * 
	 * @param categoryId
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getSecondaryCategory(Integer categoryId) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (categoryId == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("categoryId参数不能为空，请检查categoryId的传递参数值！");
			return responseDTO;
		}
		List<SecondaryCategoryDTO> secondaryCategoryList = Lists.newArrayList();
		List<SecondaryCategoryDTO> secondaryCategoryList2 = Lists.newArrayList();
		try {
			SecondaryCategoryDTO secondaryCategoryDTO = null;
			String secondaryCategory = redisService.getValue("secondaryCategory", RedisMoudel.category);
			if (StringUtils.isNotBlank(secondaryCategory)) {
				// 不等于空的情况下去redis中查询出缓存分类
				JavaType jt = mapper.contructCollectionType(List.class, SecondaryCategoryDTO.class);
				secondaryCategoryList = mapper.fromJson(secondaryCategory, jt);
				for (SecondaryCategoryDTO tEventCategory2 : secondaryCategoryList) {
					if (tEventCategory2.getParentId() == categoryId.intValue()) {
						secondaryCategoryDTO = new SecondaryCategoryDTO();
						secondaryCategoryDTO.setSecondaryCategoryId(tEventCategory2.getSecondaryCategoryId());
						secondaryCategoryDTO.setImage(tEventCategory2.getImage());
						secondaryCategoryDTO.setParentId(tEventCategory2.getParentId());
						secondaryCategoryDTO.setCategoryName(tEventCategory2.getCategoryName());
						secondaryCategoryDTO.setUpdateTime(tEventCategory2.getUpdateTime());
						secondaryCategoryList2.add(secondaryCategoryDTO);
					}
				}
			} else {
				// 如果为空的情况下添加缓存到redis
				List<TEventCategory> getCategoryList = eventCategoryDAO.findByLevel(leveTwo);
				for (TEventCategory tEventCategory : getCategoryList) {
					secondaryCategoryDTO = new SecondaryCategoryDTO();
					secondaryCategoryDTO.setSecondaryCategoryId(tEventCategory.getValue());
					secondaryCategoryDTO.setImage(fxlService.getImageUrl(tEventCategory.getImageA(), false));
					secondaryCategoryDTO.setParentId(tEventCategory.getParentId().intValue());
					secondaryCategoryDTO.setCategoryName(tEventCategory.getName());
					// secondaryCategoryDTO.setUpdateTime("");
					secondaryCategoryList.add(secondaryCategoryDTO);
					if (tEventCategory.getParentId() == categoryId.intValue()) {
						secondaryCategoryList2.add(secondaryCategoryDTO);
					}
				}
				redisService.putCache(RedisMoudel.category, "secondaryCategory", mapper.toJson(secondaryCategoryList));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("secondaryCategoryList", secondaryCategoryList2);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("商品二级分类加载成功！");
		return responseDTO;
	}
	
	
	/**
	 * 获取商品一级分类名称
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public String getCategoryA(int typeA) {
		List<CategoryDTO> categoryList = Lists.newArrayList();
		try {
			CategoryDTO categoryDTO = new CategoryDTO();
			String category = redisService.getValue("primaryCategory", RedisMoudel.category);
			if (StringUtils.isNotBlank(category)) {
				// 不等于空的情况下去redis中查询出缓存分类
				JavaType jt = mapper.contructCollectionType(List.class, CategoryDTO.class);
				categoryList = mapper.fromJson(category, jt);
			} else {
				// 如果为空的情况下添加缓存到redis
				List<TEventCategory> getCategoryList = eventCategoryDAO.findByLevel(levelNoe);
				for (TEventCategory tEventCategory : getCategoryList) {
					categoryDTO = new CategoryDTO();
					categoryDTO.setCategoryId(tEventCategory.getValue());
					categoryDTO.setCategoryAName(tEventCategory.getName());
					categoryDTO.setImageA(fxlService.getImageUrl(tEventCategory.getImageA(), false));
					categoryDTO.setImageB(fxlService.getImageUrl(tEventCategory.getImageB(), false));
					// categoryDTO.setUpdateTime("");
					categoryList.add(categoryDTO);
				}
				redisService.putCache(RedisMoudel.category, "primaryCategory", mapper.toJson(categoryList));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		for (CategoryDTO categoryDTO : categoryList) {
			if(categoryDTO.getCategoryId()==typeA){
				return categoryDTO.getCategoryAName();
			}
		}
		return null;
	}
	
	/**
	 * 获取商品二级分类
	 * 
	 * @param categoryId
	 * @return
	 */
	@Transactional(readOnly = true)
	public String getCategoryB(int typeB) {
		List<SecondaryCategoryDTO> secondaryCategoryList = Lists.newArrayList();
		try {
			SecondaryCategoryDTO secondaryCategoryDTO = new SecondaryCategoryDTO();
			String secondaryCategory = redisService.getValue("secondaryCategory", RedisMoudel.category);
			if (StringUtils.isNotBlank(secondaryCategory)) {
				// 不等于空的情况下去redis中查询出缓存分类
				JavaType jt = mapper.contructCollectionType(List.class, SecondaryCategoryDTO.class);
				secondaryCategoryList = mapper.fromJson(secondaryCategory, jt);
				for (SecondaryCategoryDTO tEventCategory2 : secondaryCategoryList) {
					if (tEventCategory2.getParentId() == typeB) {
						return tEventCategory2.getCategoryName();
					}
				}
			} else {
				// 如果为空的情况下添加缓存到redis
				List<TEventCategory> getCategoryList = eventCategoryDAO.findByLevel(leveTwo);
				for (TEventCategory tEventCategory : getCategoryList) {
					secondaryCategoryDTO = new SecondaryCategoryDTO();
					secondaryCategoryDTO.setSecondaryCategoryId(tEventCategory.getValue());
					secondaryCategoryDTO.setImage(fxlService.getImageUrl(tEventCategory.getImageA(), false));
					secondaryCategoryDTO.setParentId(tEventCategory.getParentId().intValue());
					secondaryCategoryDTO.setCategoryName(tEventCategory.getName());
					// secondaryCategoryDTO.setUpdateTime("");
					secondaryCategoryList.add(secondaryCategoryDTO);
				}
				redisService.putCache(RedisMoudel.category, "secondaryCategory", mapper.toJson(secondaryCategoryList));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		for(SecondaryCategoryDTO dto:secondaryCategoryList){
			if(dto.getSecondaryCategoryId()==typeB){
				return dto.getCategoryName();
			}
		}
		return null;
	}
	

}