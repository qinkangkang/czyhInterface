package com.innee.czyhInterface.service.v2;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.innee.czyhInterface.dao.ConfigurationDAO;
import com.innee.czyhInterface.entity.TConfiguration;
import com.innee.czyhInterface.util.Constant;

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
public class ConfigurationService {

	private static final Logger logger = LoggerFactory.getLogger(ConfigurationService.class);

	@Autowired
	private ConfigurationDAO configurationDAO;

	@Autowired
	private CacheManager cacheManager;

	/**
	 * 定时自动结算方法，根据商家ID，将一个时间区间内的核销单记录
	 * 
	 * @param merchantId
	 */
	public List<TConfiguration> findAll() {
		List<TConfiguration> list = configurationDAO.findAll();
		return list;
	}

	@Transactional(readOnly = true)
	public void initConfigurationMap() {
		Cache configurationCache = cacheManager.getCache(Constant.Configuration);
		configurationCache.removeAll();
		// 获取所有活动一级类目和二级类目
		List<TConfiguration> list = configurationDAO.findAll();

		Element element = null;
		for (TConfiguration tConfiguration : list) {
			element = new Element(tConfiguration.getFkey(), tConfiguration);
			configurationCache.put(element);
		}
	}

}