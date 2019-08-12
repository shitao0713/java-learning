package com.example.learn.common.util.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @description:
 *
 * @author: xiaoTaoShi
 * @date: 2019/6/24 17:59
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
public class JsonUtil {
	
	/**
	 * 私有构造函数
	 */
	private JsonUtil() {
	}
	
	/**
	 * 将对象转换成Json字符全, 大对象转换时fastJson性能更好
	 * SerializerFeature: 是json转换特征变量.
	 * WriteDateUseDateFormat是日期格式, 配置后默认yyyy-MM-dd HH:mm:ss,如果不配置该参数默认转换成毫秒值;
	 * WriteMapNullValue配置后不会丢弃null值的变量, 不配置该参数fastJson默认会舍弃NULL的对象
	 *
	 * @param obj
	 * @return
	 * @throws JSONException
	 */
	public static String toJsonString(Object obj) throws JSONException {
		String json = null;
		try {
			if (obj == null) {
				throw new IllegalArgumentException("toJsonString转换参数为空");
			}
			//日期格式,格式为yyyy-MM-dd HH:mm:ss
			SerializerFeature dateFormat = SerializerFeature.WriteDateUseDateFormat;
			//保留NULL值的空对象
			SerializerFeature nullValue = SerializerFeature.WriteMapNullValue;
			return JSON.toJSONString(obj, dateFormat, nullValue);
		} catch (Exception e) {
			log.error("JSON转成异常! errorMsg:{}", e.getMessage(), e);
			throw new JSONException("JSON转成异常!", e);
		}
	}
	
	/**
	 * 自定义日期格式转换
	 *
	 * @param obj
	 * @param dateFormat
	 * @return
	 * @throws JSONException
	 */
	public static String toJsonStringWithDateFormat(Object obj, String dateFormat) throws JSONException {
		try {
			if (obj == null) {
				throw new IllegalArgumentException("toJsonStringWithDateFormat转换参数为空");
			}
			
			if (StringUtils.isBlank(dateFormat)) {
				dateFormat = "yyyy-MM-dd HH:mm:ss";
			}
			//保留NULL值的空对象
			SerializerFeature nullValue = SerializerFeature.WriteMapNullValue;
			return JSON.toJSONStringWithDateFormat(obj, dateFormat, nullValue);
		} catch (Exception e) {
			log.error("JSON转成异常! errorMsg:{}", e.getMessage(), e);
			throw new JSONException("JSON转成异常!", e);
		}
	}
	
	/**
	 * json字符串-简单对象与JavaBean_obj之间的转换
	 *
	 * @param string
	 * @param type
	 * @param <T>
	 * @return
	 * @throws JSONException
	 */
	public static <T> T parseObject(String string, TypeReference<T> type) throws JSONException {
		try {
			if (StringUtils.isBlank(string)) {
				throw new IllegalArgumentException("parseObject转换参数为空");
			}
			return JSON.parseObject(string, type);
		} catch (Exception e) {
			log.error("JSON转成异常! errorMsg:{}", e.getMessage(), e);
			throw new JSONException("JSON转成异常!", e);
		}
	}
	
}
