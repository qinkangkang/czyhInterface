package com.innee.czyhInterface.web.m.api.v1.category;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.service.v1.category.CategoryService;

@RestController("M_API_V1_CategoryController")
@RequestMapping(value = "/m/api/category", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CategoryController {

	private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private CategoryService categoryService;

	/**
	 * 商品一级分类
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @return
	 */
	@PostMapping(value = "/getPrimaryCategory")
	public String getPrimaryCategory(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
		ResponseDTO responseDTO = null;
		try {
			responseDTO = categoryService.getPrimaryCategory();
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取商品一级分类时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

	/**
	 * 商品二级分类
	 * 
	 * @param request
	 * @param response
	 * @param callback
	 * @return
	 */
	@PostMapping(value = "/getSecondaryCategory")
	public String getSecondaryCategory(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "categoryId", required = false) Integer categoryId) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
		ResponseDTO responseDTO = null;
		try {
			responseDTO = categoryService.getSecondaryCategory(categoryId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("获取商品二级分类时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

}