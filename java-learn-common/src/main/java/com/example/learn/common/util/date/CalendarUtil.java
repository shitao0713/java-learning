package com.example.learn.common.util.date;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;

/**
 * @description
 *
 * @author xiaoTaoShi
 * @date 2018/11/29 19:11
 * @version 1.0.0
 * @copyright (C) 2013 WonHigh Information Technology Co.,Ltd 
 *  All Rights Reserved. 
 *
 * The software for the WonHigh technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 */
public class CalendarUtil {
	/**
	 * 根据日期字符串判断当月第几周
	 * @param dateTime
	 * @return
	 * @throws Exception
	 */
	public static Integer getWeek(Date dateTime) {
		if (dateTime == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateTime);
		//第几周
		return calendar.get(Calendar.WEEK_OF_MONTH);
	}
	
	public static Integer getWeek(LocalDate date) {
		if (date == null) {
			return null;
		}
		
		// 自定义日期是当前月份第几周, date.get(ChronoField.ALIGNED_WEEK_OF_MONTH) 1~7 first week 8~16 second week ...
		return date.get(WeekFields.of(DayOfWeek.MONDAY, 1).weekOfMonth());
	}
	
	/**
	 * 根据日期字符串判断星期几,第几天，从周日开始
	 * @param dateTime
	 * @return
	 * @throws Exception
	 */
	public static Integer getDay(Date dateTime) {
		if (dateTime == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateTime);
		//第几天，从周日开始
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	
	public static Integer getDay(LocalDate date) {
		if (date == null) {
			return null;
		}
		/*int value = date.getDayOfWeek().getValue();*/
		//第几天，从周日开始
		return date.get(ChronoField.DAY_OF_WEEK);
	}
	
	public static boolean isMonday(Calendar calendar) {
		return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
	}
	
	public static boolean isMonday(LocalDate localDate) {
		return localDate.getDayOfWeek() == DayOfWeek.MONDAY;
	}
	
	public static boolean is1StDayOfMonth(LocalDate localDate) {
		return localDate.getDayOfMonth() == 1;
	}
	
	public static boolean is1StDayOfMonth(Calendar calendar) {
		return calendar.get(Calendar.DAY_OF_MONTH) == 1;
	}
	
}
