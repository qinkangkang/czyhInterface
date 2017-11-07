package com.innee.czyhInterface.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springside.modules.utils.Exceptions;

public class DateUtil {

	private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

	private static final SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 获得所在第几季度
	 * 
	 * @return
	 */
	public static int getQuarter(Date date) {
		int quarter = 0;
		int year = DateUtils.toCalendar(date).get(Calendar.YEAR);
		Date qStart = DateUtil.getQuarterStartTime(date);
		try {
			if (DateUtils.truncatedCompareTo(qStart, DateUtils.parseDate(String.valueOf(year) + "-01", "yyyy-MM"),
					Calendar.MONTH) == 0) {
				quarter = 1;
			} else if (DateUtils.truncatedCompareTo(qStart,
					DateUtils.parseDate(String.valueOf(year) + "-04", "yyyy-MM"), Calendar.MONTH) == 0) {
				quarter = 2;
			} else if (DateUtils.truncatedCompareTo(qStart,
					DateUtils.parseDate(String.valueOf(year) + "-07", "yyyy-MM"), Calendar.MONTH) == 0) {
				quarter = 3;
			} else if (DateUtils.truncatedCompareTo(qStart,
					DateUtils.parseDate(String.valueOf(year) + "-10", "yyyy-MM"), Calendar.MONTH) == 0) {
				quarter = 4;
			}
		} catch (ParseException e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
		return quarter;
	}

	/**
	 * 获得本周的第一天，周一
	 * 
	 * @return
	 */
	public static Date getWeekDayStartTime(Date date) {
		Calendar c = DateUtils.toCalendar(date);
		try {
			int weekday = c.get(Calendar.DAY_OF_WEEK) - 2;
			c.add(Calendar.DATE, -weekday);
			c.setTime(longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00"));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
		return c.getTime();
	}

	/**
	 * 获得本周的最后一天，周日
	 * 
	 * @return
	 */
	public static Date getWeekDayEndTime(Date date) {
		Calendar c = DateUtils.toCalendar(date);
		try {
			int weekday = c.get(Calendar.DAY_OF_WEEK);
			c.add(Calendar.DATE, 8 - weekday);
			c.setTime(longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59"));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
		return c.getTime();
	}

	/**
	 * 获得本月的开始时间，即2012-01-01 00:00:00
	 * 
	 * @return
	 */
	public static Date getMonthStartTime(Date date) {
		Calendar c = DateUtils.toCalendar(date);
		Date now = null;
		try {
			c.set(Calendar.DATE, 1);
			now = shortSdf.parse(shortSdf.format(c.getTime()));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
		return now;
	}

	/**
	 * 当前月的结束时间，即2012-01-31 23:59:59
	 * 
	 * @return
	 */
	public static Date getMonthEndTime(Date date) {
		Calendar c = DateUtils.toCalendar(date);
		Date now = null;
		try {
			c.set(Calendar.DATE, 1);
			c.add(Calendar.MONTH, 1);
			c.add(Calendar.DATE, -1);
			now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
		return now;
	}

	/**
	 * 当前年的开始时间，即2012-01-01 00:00:00
	 * 
	 * @return
	 */
	public static Date getYearStartTime(Date date) {
		Calendar c = DateUtils.toCalendar(date);
		Date now = null;
		try {
			c.set(Calendar.MONTH, 0);
			c.set(Calendar.DATE, 1);
			now = shortSdf.parse(shortSdf.format(c.getTime()));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
		return now;
	}

	/**
	 * 当前年的结束时间，即2012-12-31 23:59:59
	 * 
	 * @return
	 */
	public static Date getYearEndTime(Date date) {
		Calendar c = DateUtils.toCalendar(date);
		Date now = null;
		try {
			c.set(Calendar.MONTH, 11);
			c.set(Calendar.DATE, 31);
			now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
		return now;
	}

	/**
	 * 当前季度的开始时间，即2012-01-1 00:00:00
	 * 
	 * @return
	 */
	public static Date getQuarterStartTime(Date date) {
		Calendar c = DateUtils.toCalendar(date);
		int currentMonth = c.get(Calendar.MONTH) + 1;
		Date now = null;
		try {
			if (currentMonth >= 1 && currentMonth <= 3)
				c.set(Calendar.MONTH, 0);
			else if (currentMonth >= 4 && currentMonth <= 6)
				c.set(Calendar.MONTH, 3);
			else if (currentMonth >= 7 && currentMonth <= 9)
				c.set(Calendar.MONTH, 4);
			else if (currentMonth >= 10 && currentMonth <= 12)
				c.set(Calendar.MONTH, 9);
			c.set(Calendar.DATE, 1);
			now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
		return now;
	}

	/**
	 * 当前季度的结束时间，即2012-03-31 23:59:59
	 * 
	 * @return
	 */
	public static Date getQuarterEndTime(Date date) {
		Calendar c = DateUtils.toCalendar(date);
		int currentMonth = c.get(Calendar.MONTH) + 1;
		Date now = null;
		try {
			if (currentMonth >= 1 && currentMonth <= 3) {
				c.set(Calendar.MONTH, 2);
				c.set(Calendar.DATE, 31);
			} else if (currentMonth >= 4 && currentMonth <= 6) {
				c.set(Calendar.MONTH, 5);
				c.set(Calendar.DATE, 30);
			} else if (currentMonth >= 7 && currentMonth <= 9) {
				c.set(Calendar.MONTH, 8);
				c.set(Calendar.DATE, 30);
			} else if (currentMonth >= 10 && currentMonth <= 12) {
				c.set(Calendar.MONTH, 11);
				c.set(Calendar.DATE, 31);
			}
			now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
		return now;
	}

	/**
	 * 获取前/后半年的开始时间
	 * 
	 * @return
	 */
	public static Date getHalfYearStartTime(Date date) {
		Calendar c = DateUtils.toCalendar(date);
		int currentMonth = c.get(Calendar.MONTH) + 1;
		Date now = null;
		try {
			if (currentMonth >= 1 && currentMonth <= 6) {
				c.set(Calendar.MONTH, 0);
			} else if (currentMonth >= 7 && currentMonth <= 12) {
				c.set(Calendar.MONTH, 6);
			}
			c.set(Calendar.DATE, 1);
			now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
		return now;

	}

	/**
	 * 获取前/后半年的结束时间
	 * 
	 * @return
	 */
	public static Date getHalfYearEndTime(Date date) {
		Calendar c = DateUtils.toCalendar(date);
		int currentMonth = c.get(Calendar.MONTH) + 1;
		Date now = null;
		try {
			if (currentMonth >= 1 && currentMonth <= 6) {
				c.set(Calendar.MONTH, 5);
				c.set(Calendar.DATE, 30);
			} else if (currentMonth >= 7 && currentMonth <= 12) {
				c.set(Calendar.MONTH, 11);
				c.set(Calendar.DATE, 31);
			}
			now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
		return now;
	}
	
	
	public static Date getDay(Date date) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	     
	    Date start = calendar.getTime();
		return start;
	}
	
	public static String formatDuring(long mss) {
		if(mss<=0){
			 return 0 + "天" + 0 + "小时";  
		}
	    long days = mss / (60 * 60 * 24);  
	    long hours = (mss % (60 * 60 * 24)) / (60 * 60);  
	    return days + "天" + hours + "小时";  
	}
}