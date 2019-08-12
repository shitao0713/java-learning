package com.example.learn.common.util.common;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 *
 * @author wang.w
 * @date 2014-6-16 上午10:29:32
 * @version 0.9.1
 * @copyright yougou.com
 */
public class CommonUtil {

	/**
	 * 数据长度
	 */
	private final static Pattern NUM_LENGTH_PATTERN = Pattern.compile("^\\d{6,20}$");
	/**
	 * 邮箱
	 */
	private final static Pattern EMAIL_PATTERN = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

	public static String create32RandomNumber() {
		UUID uuid = UUID.randomUUID();
		String r = uuid.toString().replace("-", "");
		return r;
	}

	/**
	 * 检查email地址是否正确
	 * @param email
	 * @return
	 */
	public static boolean checkEmailAddr(String email) {
		Matcher m = EMAIL_PATTERN.matcher(email);
		return m.find();
	}

	/**
	 * 检查电话号码
	 * @param mobile
	 * @return
	 *
	 */
	public static boolean checkMobilePhone(String mobile) {
		Matcher matcher = NUM_LENGTH_PATTERN.matcher(mobile);
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * 校验list值
	 * @param list
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean checkList(List list) {
		return list != null && !list.isEmpty();
	}

	/**
	 * @param regex
	 * 正则表达式字符串
	 * @param str
	 * 要匹配的字符串
	 * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
	 */
	public static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	/**
	 * 字符串转int,如果为空,使用一个默认值
	 * @param valueStr 需要转换的字符串
	 * @param defaultValue 默认值
	 * @return 转化后的int值
	 */
	public static int getIntValue(String valueStr, int defaultValue) {
		return (StringUtils.isNotEmpty(valueStr) && StringUtils.isNumeric(valueStr)) ? Integer.parseInt(valueStr)
				: defaultValue;
	}

	public static int getIntValue(String valueStr, int defaultValue, boolean existMinus) {
		char ch = '-';
		if (existMinus && valueStr.indexOf(ch) == 0) {
			String tempStr = valueStr.replace("-", "");
			return (StringUtils.isNotEmpty(tempStr) && StringUtils.isNumeric(tempStr)) ? Integer.parseInt(valueStr)
					: defaultValue;
		} else {
			return getIntValue(valueStr, defaultValue);
		}

	}

	/**
	 * Object转成int(
	 *
	 * StringUtils.isNumeric("-1")认为不是数字
	 *
	 * 解决办法:  str.indexOf('-') == 0, 表明是负数
	 * )
	 *
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static int getIntValue(Object value, int defaultValue) {
		String str = getStrValue(value, "");
		boolean isNUM = StringUtils.isNotEmpty(str) && (StringUtils.isNumeric(str) || str.indexOf('-') == 0);
		return isNUM ? Integer.parseInt(str) : defaultValue;
	}

	/**
	 * 取非空值
	 *
	 * @param attrJson
	 * @param fieldName
	 * @param defaultValue
	 * @return
	 */
	public static int getIntValue(Map<String, Object> attrJson, String fieldName, int defaultValue) {
		if (attrJson == null) {
			return defaultValue;
		}
		return getIntValue(attrJson.get(fieldName), defaultValue);
	}

	/**
	 * 如果前置字符串为空 则使用默认字符串
	 * @param valueStr 前置字符串
	 * @param defaultValue 默认支付攒
	 * @return 字符串
	 */
	public static String getStrValue(String valueStr, String defaultValue) {
		return StringUtils.isEmpty(valueStr) ? defaultValue : valueStr;
	}

	public static String getStrValue(Object valueStr, String defaultValue) {
		return valueStr == null ? defaultValue : valueStr.toString();
	}

	public static String getStrValue(Map<String, Object> attrJson, String fieldName, String defaultValue) {
		if (attrJson == null) {
			return defaultValue;
		}
		return getStrValue(attrJson.get(fieldName), defaultValue);
	}

	/**
	 * a 是否是 b 的前缀
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean isPrefix(String a, String b) {
		if (a == null || b == null) {
			return false;
		}
		int len = a.length() - b.length();
		if (len > 0) {
			return false;
		}
		len = a.length();
		if (a.substring(0, len).equals(b.substring(0, len))) {
			return true;
		}
		return false;
	}
	
	public static String cutOut(String cutStr, int maxLength) {
		return cutStr.substring(0, cutStr.length() > maxLength ? maxLength : cutStr.length());
	}

}
