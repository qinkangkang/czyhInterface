package com.innee.czyhInterface.service.v1.system;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;

import com.innee.czyhInterface.util.redis.ExpireTime;

/**
 * redis工具类
 * 
 * @author jinshengzhi
 *
 */
@Component
@Transactional
public class RedisService {

	private static final Logger logger = LoggerFactory.getLogger(RedisService.class);

	private static JsonMapper mapper = JsonMapper.nonDefaultMapper();

	private StringRedisSerializer serializer = new StringRedisSerializer();

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	private final static String prefix = "czyh";

	/**
	 * 添加缓存
	 * 
	 * @param moudle
	 * @param key
	 * @param value
	 * @return
	 */
	public Boolean putCache(final String moudle, final String key, final String value) {
		if (StringUtils.isBlank(key) || StringUtils.isBlank(moudle) || StringUtils.isBlank(value)) {
			logger.error("参数为空");
			return false;
		}
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.hSet(serializer.serialize(moudle), serializer.serialize(key),
						serializer.serialize(value));
			}
		});
	}

	/**
	 * 查询redis 缓存key
	 * 
	 * @param key
	 * @param moudle
	 * @return
	 * @throws Exception
	 */
	public String getValue(String key, String moudle) throws Exception {
		String value = null;
		if (StringUtils.isBlank(key) || StringUtils.isBlank(moudle)) {
			logger.error("参数为空");
			return null;
		}
		Object result = redisTemplate.opsForHash().get(moudle, key);
		if (result == null) {
		} else {
			value = result.toString();
		}
		return value;
	}

	/**
	 * 删除 key
	 * 
	 * @param moudle
	 * @param key
	 */
	public void removeCache(final String moudle, final String key) {
		if (StringUtils.isBlank(key) || StringUtils.isBlank(moudle)) {
			logger.error("参数为空");
			return;
		}
		redisTemplate.opsForHash().delete(moudle, key);
	}

	/**
	 * 修改key
	 * 
	 * @param moudle
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public boolean updateCache(final String moudle, final String key, final String value) throws Exception {
		return putCache(moudle, key, value);
	}

	/**
	 * 设置key失效时间
	 * 
	 * @param key
	 * @param value
	 * @param time
	 *            失效时间(单位秒)
	 */
	public void setJson(String key, ExpireTime time) {
		if (time.getTime() > 0) {
			redisTemplate.expire(key, time.getTime(), TimeUnit.SECONDS);
		}

	}

	/**
	 * 递减操作
	 * 
	 * @param key
	 * @param by
	 * @return
	 */
	public double decr(String key, double by) {
		return redisTemplate.opsForValue().increment(key, -by);
	}

	/**
	 * 递增操作
	 * 
	 * @param key
	 * @param by
	 * @return
	 */
	public double incr(String key, double by) {
		return redisTemplate.opsForValue().increment(key, by);
	}

	/**
	 * 模糊查询keys
	 * 
	 * @param pattern
	 * @return
	 */
	public Set<String> keys(String pattern) {
		return redisTemplate.keys(pattern);
	}

	
	private String getHKey(String moudle) {
		// redisTemplate.opsForList()
		return prefix + ":" + moudle;
	}
}