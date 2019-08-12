package com.example.learn.common.util.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @description: Redis工具类
 *
 * @author: xiaoTaoShi
 * @date: 2019/6/19 17:46
 * @version 1.0.0
 * @copyright (C) 2013 WonHigh Information Technology Co.,Ltd 
 *  All Rights Reserved. 
 *
 * The software for the WonHigh technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 */

@Slf4j
@Component
@ConditionalOnClass(RedisConfiguration.class)
public class RedisUtil {
	
	@Resource
	private RedisTemplate<Serializable, Object> redisTemplate;
	
	/**
	 * 指定缓存失效时间
	 *
	 * @param key 键
	 * @param time 时间(秒)
	 * @return
	 */
	public Boolean expire(String key, long time, TimeUnit timeUnit) {
		try {
			boolean condition = key != null && time > 0;
			return condition ? redisTemplate.expire(key, time, timeUnit == null ? TimeUnit.SECONDS : timeUnit) : false;
		} catch (Exception e) {
			log.error("redis操作异常! errorMsg:{}", e.getMessage(), e);
		}
		return false;
	}
	
	/**
	 * 根据key 获取过期时间
	 *
	 * @param key 键 不能为null
	 * @return 时间(秒) 返回0代表为永久有效
	 */
	public Long getExpire(String key) {
		return key == null ? -2 : redisTemplate.getExpire(key, TimeUnit.SECONDS);
	}
	
	/**
	 * 判断key是否存在
	 * @param key 键
	 * @return true 存在 false不存在
	 */
	public Boolean hasKey(String key) {
		try {
			Assert.notNull(key, "key must not be null");
			return redisTemplate.hasKey(key);
		} catch (Exception e) {
			log.error("redis操作异常! errorMsg:{}", e.getMessage(), e);
		}
		return false;
	}
	
	/**
	 * 读取缓存
	 *
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		try {
			return key == null ? null : redisTemplate.opsForValue().get(key);
		} catch (Exception e) {
			log.error("redis操作异常! errorMsg:{}", e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 写入缓存
	 */
	public boolean set(String key, Object value) {
		boolean result = false;
		try {
			Assert.notNull(key, "key must not be null");
			return set(key, value, 0);
		} catch (Exception e) {
			log.error("redis set error! errorMsg:{}", e.getMessage(), e);
		}
		return false;
	}
	
	/**
	 * 更新缓存
	 */
	public boolean getAndSet(String key, Object value) {
		boolean result = false;
		try {
			Assert.notNull(key, "key must not be null");
			redisTemplate.opsForValue().getAndSet(key, value);
			result = true;
		} catch (Exception e) {
			log.error("redis getAndSet error! errorMsg:{}", e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * 删除缓存
	 */
	public boolean delete(String key) {
		boolean result = false;
		try {
			redisTemplate.delete(key);
			result = true;
		} catch (Exception e) {
			log.error("redis delete error! errorMsg:{}", e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * 普通缓存放入并设置时间
	 *
	 * @param key 键
	 * @param value 值
	 * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
	 * @return true成功 false 失败
	 */
	public boolean set(String key, Object value, long time) {
		boolean result = false;
		try {
			ValueOperations<Serializable, Object> valueOperations = redisTemplate.opsForValue();
			if (time > 0) {
				valueOperations.set(key, value, time, TimeUnit.SECONDS);
			} else {
				valueOperations.set(key, value);
			}
			result = true;
		} catch (Exception e) {
			log.error("redis set error! errorMsg:{}", e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * 如果redis中已有该数据不保存返回false，不存该数据保存返回true
	 *
	 * @param key
	 * @param value
	 * @param time(秒)
	 * @return
	 */
	public Boolean setIfAbsent(String key, Object value, long time) {
		try {
			Assert.notNull(key, "key must not be null");
			
			ValueOperations<Serializable, Object> valueOperations = redisTemplate.opsForValue();
			if (time > 0) {
				return valueOperations.setIfAbsent(key, value, time, TimeUnit.SECONDS);
			}
			return valueOperations.setIfAbsent(key, value);
		} catch (Exception e) {
			log.error("redis setIfAbsent error! errorMsg:{}", e.getMessage(), e);
		}
		return false;
	}
	
}
