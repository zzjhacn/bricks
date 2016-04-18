package com.bricks.core.redis;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;

import com.bricks.lang.log.LogAble;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author bricks <long1795@gmail.com>
 */
public class RedisService implements LogAble {

	@Value("${bricks.core.redis.addr}")
	private String addr;

	@Value("${bricks.core.redis.port}")
	private int port = 6379;

	@Value("${bricks.core.redis.auth}")
	private static String auth;

	// 可用连接实例的最大数目，默认值为8；
	// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	@Value("${bricks.core.redis.maxActive}")
	private int maxActive = 1024;

	// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
	@Value("${bricks.core.redis.maxIdle}")
	private int maxIdle = 200;

	// 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	@Value("${bricks.core.redis.maxWait}")
	private int maxWait = 10000;

	@Value("${bricks.core.redis.timeout}")
	private int timeout = 10000;

	// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	@Value("${bricks.core.redis.testOnBorrow}")
	private boolean testOnBorrow = true;

	private static JedisPool jedisPool = null;

	/**
	 * 初始化Redis连接池
	 */
	@PostConstruct
	public void init() {
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(maxActive);
			config.setMaxWaitMillis(maxWait);
			config.setMaxIdle(maxIdle);
			config.setTestOnBorrow(testOnBorrow);
			jedisPool = new JedisPool(config, addr, port, timeout, auth);
		} catch (Exception e) {
			err(e);
		}
	}

	/**
	 * 获取Jedis实例
	 * 
	 * @return
	 */
	public Jedis getJedis() {
		try {
			if (jedisPool != null) {
				Jedis resource = jedisPool.getResource();
				return resource;
			} else {
				return null;
			}
		} catch (Exception e) {
			err(e);
			return null;
		}
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public static void setAuth(String auth) {
		RedisService.auth = auth;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public static void setJedisPool(JedisPool jedisPool) {
		RedisService.jedisPool = jedisPool;
	}

}