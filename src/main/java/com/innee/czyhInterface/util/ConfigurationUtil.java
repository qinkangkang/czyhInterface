package com.innee.czyhInterface.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.innee.czyhInterface.common.dict.ResponseConfigurationDict;
import com.innee.czyhInterface.entity.TConfiguration;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * @name PropertiesUtil
 * @title 操作属性文件工具类
 * @desc
 * @author
 * @version
 */
public class ConfigurationUtil {

	public static List<TConfiguration> ConfigurationList = null;

	public static String getPropertiesValue(String key){
		CacheManager cacheManager = SpringContextHolder.getBean(CacheManager.class);
		Cache configurationCache = cacheManager.getCache(Constant.Configuration);
		Element element = configurationCache.get(key);
		TConfiguration configuration = (TConfiguration) element.getObjectValue();
		if(configuration != null){
			return configuration.getFvalue();	
		}else{
			return "";
		}
	}
	
	public static String getExpressPhone(Integer key){
		String expressPhone = ConfigurationUtil
				.getPropertiesValue(ResponseConfigurationDict.EXPRESS_PHONE);
		String phone = "";
		if(StringUtils.isNotBlank(expressPhone)){
			return expressPhone.split(",")[key-1];
		}else{
			return phone;
		}
	}
	
}