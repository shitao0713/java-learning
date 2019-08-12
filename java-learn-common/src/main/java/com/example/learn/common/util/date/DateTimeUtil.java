package com.example.learn.common.util.date;

import com.example.learn.common.constant.DateConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @description JDK8 LocalDate、LocalTime、LocalDateTime工具类(线程安全)
 *
 * @author xiaoTaoShi
 * @date 2018/11/29 11:37
 * @version 0.0.4
 * @copyright (C) 2013 WonHigh Information Technology Co.,Ltd 
 *  All Rights Reserved. 
 *
 * The software for the WonHigh technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 */

@Slf4j
public class DateTimeUtil {
	
	/**
	 * 私有构造方法
	 */
	private DateTimeUtil() {
	}
	
	/**
	 * LocalDateTime转换为Date
	 * @param localDateTime
	 */
	public static Date localDateTime2Date(LocalDateTime localDateTime) {
		if (localDateTime == null) {
			log.info("localDateTime参数为空!");
			return null;
		}
		ZoneId zoneId = ZoneId.systemDefault();
		//Combines this date-time with a time-zone to create a  ZonedDateTime.
		ZonedDateTime zdt = localDateTime.atZone(zoneId);
		return Date.from(zdt.toInstant());
	}
	
	/**
	 * 将LocalDate转换成Date
	 *
	 * @param localDate
	 * @return
	 */
	public static Date localDate2Date(LocalDate localDate) {
		if (localDate == null) {
			log.info("localDate参数为空!");
			return null;
		}
		ZoneId zoneId = ZoneId.systemDefault();
		//Combines this date-time with a time-zone to create a  ZonedDateTime.
		Instant instant = localDate.atStartOfDay().atZone(zoneId).toInstant();
		return Date.from(instant);
	}
	
	/**
	 * 将Date转换成LocalDateTime
	 *
	 * @param date
	 * @return
	 */
	public static LocalDateTime date2LocalDateTime(Date date) {
		if (date == null) {
			log.info("date参数为空!");
			return null;
		}
		//An instantaneous point on the time-line.(时间线上的一个瞬时点。)
		Instant instant = date.toInstant();
		
		//A time-zone ID, such as {@code Europe/Paris}.(时区)
		ZoneId zoneId = ZoneId.systemDefault();
		return instant.atZone(zoneId).toLocalDateTime();
	}
	
	/**
	 * 将Date转换成LocalDate
	 *
	 * @param date
	 * @return
	 */
	public static LocalDate date2LocalDate(Date date) {
		if (date == null) {
			log.info("date参数为空!");
			return null;
		}
		//An instantaneous point on the time-line.(时间线上的一个瞬时点。)
		Instant instant = date.toInstant();

		//A time-zone ID, such as {@code Europe/Paris}.(时区)
		ZoneId zoneId = ZoneId.systemDefault();
		return instant.atZone(zoneId).toLocalDate();
	}
	
	/**
	 * Date转换为LocalDateTime
	 * @param date
	 */
	public static String date2LocalDateTimeStr(Date date, String format) {
		if (date == null) {
			log.info("date参数为空!");
			return null;
		}
		//An instantaneous point on the time-line.(时间线上的一个瞬时点。)
		Instant instant = date.toInstant();
		
		//A time-zone ID, such as {@code Europe/Paris}.(时区)
		ZoneId zoneId = ZoneId.systemDefault();
		LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
		// 判断转换格式
		format = StringUtils.isEmpty(format) ? DateConstant.DATETIME_DEFAULT_FORMAT : format;
		
		//This class is immutable and thread-safe.@since 1.8
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
		//2018-03-27 14:52:57
		return dtf.format(localDateTime);
	}
	
	/**
	 * dateTimeStr TO LocalDateTime
	 *
	 * @param dateTimeStr
	 * @param format
	 * @return
	 */
	public static LocalDateTime dateStr2LocalDateTime(String dateTimeStr, String format) {
		if (StringUtils.isEmpty(dateTimeStr)) {
			log.info("dateTimeStr参数为空!");
			return null;
		}
		format = StringUtils.isEmpty(format) ? DateConstant.DATETIME_DEFAULT_FORMAT : format;
		
		//This class is immutable and thread-safe.@since 1.8
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
		return LocalDateTime.parse(dateTimeStr, dtf);
	}
	
	/**
	 * dateTimeStr TO LocalDate
	 *
	 * @param dateTimeStr
	 * @param format
	 * @return
	 */
	public static LocalDate dateStr2LocalDate(String dateTimeStr, String format) {
		return dateStr2LocalDateTime(dateTimeStr, format).toLocalDate();
	}
	
	/**
	 * localDateTime to dateString
	 *
	 * @param localDateTime
	 * @param format
	 * @return
	 */
	public static String localDateTime2DateString(LocalDateTime localDateTime, String format) {
		if (localDateTime == null) {
			log.info("localDateTime参数为空!");
			return null;
		}
		// 判断转换格式
		format = StringUtils.isEmpty(format) ? DateConstant.DATETIME_DEFAULT_FORMAT : format;
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
		return dtf.format(localDateTime);
	}
	
	/**
	 * localDateTime to dateString
	 *
	 * @param localDate
	 * @param format
	 * @return
	 */
	public static String localDate2String(LocalDate localDate, String format) {
		if (localDate == null) {
			log.info("localDate参数为空!");
			return null;
		}
		// 判断转换格式
		format = StringUtils.isEmpty(format) ? DateConstant.DATE_DEFAULT_FORMAT : format;
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
		return dtf.format(localDate);
	}
	
	/**
	 * localDateTime to dateString
	 *
	 * @param localTime
	 * @param format
	 * @return
	 */
	public static String localTime2TimeString(LocalTime localTime, String format) {
		if (localTime == null) {
			log.info("localDateTime参数为空!");
			return null;
		}
		// 判断转换格式
		format = StringUtils.isEmpty(format) ? DateConstant.TIME_DEFAULT_FORMAT : format;
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
		return dtf.format(localTime);
	}
	
	/**
	 * dateTime字符串  TO  Date
	 *
	 * @param dateTime
	 * @param format
	 * @return
	 */
	public static Date dateTimeStr2Date(String dateTime, String format) {
		if (dateTime == null) {
			log.info("dateTime参数为空!");
			return null;
		}
		LocalDateTime localDateTime = dateStr2LocalDateTime(dateTime, format);
		
		return localDateTime2Date(localDateTime);
	}
	
	/**
	 * localDateTime TO 毫秒
	 *
	 * @param localDateTime
	 * @return
	 */
	public static Long localDateTime2Millis(LocalDateTime localDateTime) {
		//Combines this date-time with a time-zone to create a  ZonedDateTime.
		return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
	
	/**
	 * 毫秒 TO localDateTime
	 *
	 * @param timestamp
	 * @return
	 */
	public static LocalDateTime milliSecond2LocalDateTime(Long timestamp) {
		return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
	
	/**
	 * 时间是否在范围之内
	 * @param startTime
	 * @param endTime
	 * @param time
	 * @return
	 */
	public static boolean isInRightRange(LocalDateTime startTime, LocalDateTime endTime, LocalDateTime time) {
		boolean flag = false;
		if (time.isAfter(startTime) && time.isBefore(endTime)) {
			flag = true;
		}
		
		return flag;
	}
	
	public static void main(String[] args) {
		String date = "2019-07-19 16:00:00";
		
		LocalDate date1 = DateTimeUtil.dateStr2LocalDate(date, null);
		System.out.println("date1:" + date1);
		
	}
	
}
