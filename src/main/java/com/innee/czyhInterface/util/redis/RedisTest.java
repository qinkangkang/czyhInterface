package com.innee.czyhInterface.util.redis;

import redis.clients.jedis.Jedis;

/**
 * redis工具类
 * ClassName: RedisUtil 
 * @Description: TODO
 * @author jsz
 * @date 2016-5-10
 */
public class RedisTest {

	static Jedis jedis = null;
	
	public static Jedis RedisDB(){
		
		jedis = new Jedis("192.168.1.190");//测试环境
		jedis.auth("root");
		
		return jedis;
	}
}
