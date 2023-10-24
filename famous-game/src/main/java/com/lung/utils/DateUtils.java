package com.lung.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 14-4-3.
 */
public class DateUtils {

    public static final long ONEDAYTIME=24*3600*1000L;
    
    public static final long SECONDMILLS = 1000;
	public static final long MINUTEMILLS = 1000 * 60;
	public static final long HOURMILLS = 1000 * 60 * 60;
	public static final long DAYMILLS = HOURMILLS * 24;

	public static final long WEEKMILLS = DAYMILLS * 7;

	public static final String PATTERN_DATE_FORMAT = "yyyy-MM-dd HH:mm";
	public static final String PATTERN_DATE_SHORT_TERM_FORMAT = "yyyy-MM-dd-HH-mm"; //短线
	public static final String PATTERN_DATE_SLASH_FORMAT = "yyyy/MM/dd HH:mm:ss"; //斜杠


    
    
    
    /**
     * 14-12-4 下午7:24
     * @param time
     * @return
     */
    public static String fromTimeToStr(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return sdf.format(calendar.getTime());
    }

    /**
     * 2014-12-04 19:24:29
     * @param time
     * @return
     */
    public static String fromTimeToStandardStr(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return sdf.format(calendar.getTime());
    }

    /**
     * @param time
     * @return HH:mm:ss
     */
    public static String fromTimeToStandardStr2(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return sdf.format(calendar.getTime());
    }

    public static String fromTimeToFromatStr(long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return sdf.format(calendar.getTime());
    }

    /**
     * 以周一未基准点
     * 算当前天数
     * @return
     */
    public static int getCurrentDay(long timeStamp) {
        long startPoint = DateUtils.getWeekNumTime(timeStamp, 1);
        return (int) (startPoint/DAYMILLS + (timeStamp - startPoint)/DAYMILLS + 1);
    }

    /**
     * MM-DD-HH-mm，表示在MM月DD天HH时mm分
     * @param timeStr
     * @return
     */
    public static long parseTime(String timeStr) {
        String[] timeArr = StringUtils.split(timeStr, "-");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Integer.parseInt(timeArr[0]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(timeArr[1]));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArr[2]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArr[3]));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * D-HH-MM，表示在每周第D天HH时MM分
     * @param timeStr
     * @return
     */
    public static long parseWeekTime(String timeStr) {
        String[] timeArr = StringUtils.split(timeStr, "-");
        Calendar calendar = Calendar.getInstance();
        int week = Integer.parseInt(timeArr[0]);
        int currWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.set(Calendar.DAY_OF_WEEK, week);
        if(currWeek > week) {
            calendar.add(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArr[1]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArr[2]));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取某个时间戳的所在周的周几： 周1 ~ 周日：1 ~ 7
     * @param millisTime
     * @param dayOfWeekNum
     * @return
     */
    public static long getWeekNumTime(Long millisTime, int dayOfWeekNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTimeInMillis(millisTime);
        calendar.set(Calendar.DAY_OF_WEEK, (dayOfWeekNum % 7) + 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取某个时间戳的下一个周几： 周1 ~ 周日：1 ~ 7
     * @param millisTime
     * @param dayOfWeekNum
     * @return
     */
    public static long getNextWeekNumTime(Long millisTime, int dayOfWeekNum) {
        dayOfWeekNum = (dayOfWeekNum % 7) + 1;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisTime);
        int currWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeekNum);
        if(currWeek >= dayOfWeekNum) {
            calendar.add(Calendar.WEEK_OF_MONTH, 1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
    
    public static int getCurrentWeek() {
    	return (int)((getWeekNumTime(System.currentTimeMillis(), 1)) / WEEKMILLS);
	}
   
    /**
     * 获取某个时间戳的上一个周几： 周1 ~ 周日：1 ~ 7
     * @param millisTime
     * @param dayOfWeekNum
     * @return 
     */
    public static long getLastWeekNumTime(Long millisTime, int dayOfWeekNum) {
        dayOfWeekNum = (dayOfWeekNum % 7) + 1;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisTime);
        int currWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeekNum);
        if(currWeek <= dayOfWeekNum) {
            calendar.add(Calendar.WEEK_OF_MONTH, -1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
    
    
    public static long unixStringToTime(String val){
    	try {
			return  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(val).getTime();
		} catch (ParseException e) {
		}
    	return 0;
    }
    
    public static String unixDate2String(long time){
        return new SimpleDateFormat("yyyy-MM-dd").format(time);
    }

    public static String unixTime2String(long time){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
    }

    public static long parseTimeInLiuWenStyle(String timeStr){
        return parseFullTimeStr(timeStr, "yyyy-MM-dd-HH-mm");
    }

    public static long parseFullTimeStr(String fullTimeStr, String format) {
        SimpleDateFormat sdf  =   new SimpleDateFormat(format);
        try {
            Date date = sdf.parse(fullTimeStr);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long parseFullTimeStr(String fullTimeStr) {
        return parseFullTimeStr(fullTimeStr, "yyyy-MM-dd HH:mm:ss");
    }

	/**
	 * 获取下一天的零点
	 */
	public static long getNewDay() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return  cal.getTimeInMillis();
	}

	/**
	 * 获取某时间下一天的零点
	 */
	public static long getNewDay(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return  cal.getTimeInMillis();
	}


	/**
	 * 获取当天零点
	 */
	public static long getToday() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return  cal.getTimeInMillis();
	}

    /**
     * HH-MM，取今天的HH时MM分
     * @param timeStr
     * @return
     */
    public static long todayTime(String timeStr) {
        String[] timeArr = StringUtils.split(timeStr, "-");
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArr[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArr[1]));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取xx天以后的0点
     * @param day
     * @return
     */
    public static long getZeroClockDaysAfter(long day) {
        return getToday() + day * DAYMILLS;
    }

    /**
     * 获取xx天以后的时间
     * @param time
     * @param day
     * @return
     */
    public static long getClockDaysAfter(long time, long day) {
        return time + day * DAYMILLS;
    }

    /**
     * 获取xx天以前的0点
     * @param day
     * @return
     */
    public static long getZeroClockDaysBefore(long day) {
        return getToday() - day * 86400 * 1000;
    }

    /*
    * 获取某天零点
    * */
    public static long getZeroClock(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return  cal.getTimeInMillis();
    }

    public static long getZeroClock(long dateInMillis){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dateInMillis);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return  cal.getTimeInMillis();
    }

	/**
	 * 周日 ~ 周六 : 1 ~ 7
	 */
	public static int getDayOfWeek() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		return cal.get(Calendar.DAY_OF_WEEK);
	}

    /**
     * 周日 ~ 周六 : 1 ~ 7
     */
    public static int getDayOfWeek(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 周一 ~ 周日 ： 1 ~ 7
     * @param time
     * @return
     */
    public static int getSpecialDayOfWeek(long time){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int number = cal.get(Calendar.DAY_OF_WEEK);
        return (number + 5) % 7 + 1;
    }


    /*
    * 计算两个时间戳间隔几天
    * */
    public static int getDayInterval(long time1, long time2){
		long zeroTime1 = getZeroClock(time1);
		long zeroTime2 = getZeroClock(time2);
        long interval = Math.abs(zeroTime2 - zeroTime1);
        int dayInterval = (int)(interval/86400000);
        return dayInterval;
    }

    /**
     * 计算从time1开始，time2位于第几周(从1开始)
     * @param time1
     * @param time2
     * @return
     */
    public static int getWeekInterval(long time1, long time2){
        long zeroTime1 = getZeroClock(time1);
        long zeroTime2 = getZeroClock(time2);
        long interval = Math.abs(zeroTime2 - zeroTime1);
        return (int)(interval * 1.0d / WEEKMILLS) + 1;
    }

    /**
     * 取得当前日期所在周的第一天
     *
     * @return
     */
    public static Date getFirstDayOfWeek() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK,
                calendar.getFirstDayOfWeek()); // Sunday
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 取得当前日期所在周的最后一天  //实际获取的是周六24点
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        if(date == null) {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK,
                calendar.getFirstDayOfWeek() + 6); // Saturday
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 取得当前日期所在周的最后一天24点
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfWeekNew(Date date) {
        if(date == null) {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK,
                calendar.getFirstDayOfWeek() + 6); // Saturday
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static long getWeekFirstDay() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
    
    /***
     * 获取指定时间对应的毫秒数
     * @param time "HH:mm:ss" 
     * @return
     */
    public static long getTimeMillis(String time) {  
	    try {  
	        DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");  
	        DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");  
	        Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);  
	        return curDate.getTime();  
	    } catch (ParseException e) {  
	        e.printStackTrace();  
	    }  
	    return 0;  
	}

    public static int getMinute(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        return calendar.get(Calendar.MINUTE);
    }
    
    public static int getHourOfDay(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }


    /**
     * 月份(从0开始)
     * @param timeStamp
     * @return
     */
    public static int getMonthOfDay(long timeStamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        return  calendar.get(Calendar.MONTH);
    }

    /**
     * 本月第几天(从1开始)
     * @param timeStamp
     * @return
     */
    public static int getDayOfMonth(long timeStamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        return  calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 本月第几天的时间
     * @param dayOfMonth
     * @return
     */
	public static long getDayOfMonthTime(int dayOfMonth) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}
    
	/**
	 * 获得本周周几的时间 
	 * @param dayOfWeek 周一~周日 1~7
	 * @return
	 */
	public static long getDayOfWeekTime(int dayOfWeek) {
		dayOfWeek=dayOfWeek+1;
		if(dayOfWeek==8) {
			dayOfWeek=1;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

    /**
     * 获取本周周几的时间
     * @param dayOfWeek 周日 ~ 周六  1~7
     * @return
     */
	public static long getDayOfWeekTime2(int dayOfWeek){
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

	/**
	 * 获得指定时间所在周, 周几的时间
	 * @param dayOfWeek 周一~周日 1~7
	 * @return
	 */
	public static long getTimestampOfWeekTime(long timestamp, int dayOfWeek) {
		dayOfWeek=dayOfWeek+1;
		if(dayOfWeek>=8) {
			dayOfWeek=1;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

	
    /**
     * 当月第一天0点的时间戳
     * @param timeStamp
     * @return
     */
    public static long getMonthFirstDay(long timeStamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 当月最后一天24点的时间戳
     * @param timeStamp
     * @return
     */
    public static long getMonthLastDay(long timeStamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static int getYearOfDay(long timeStamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        return  calendar.get(Calendar.YEAR);
    }

    public static int getDayOfYear(long timeStamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        return  calendar.get(Calendar.DAY_OF_YEAR);
    }
    
    public static long getHourOfDayStartTime(long timeStamp){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeStamp);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return  cal.getTimeInMillis();
    }
    
	public static long getCurrentUTCTime() {
		// 1、取得本地时间：
		Calendar cal = Calendar.getInstance();
		// 2、取得时间偏移量：
		int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
		// 3、取得夏令时差：
		int dstOffset = cal.get(Calendar.DST_OFFSET);
		// 4、从本地时间里扣除这些差量，即可以取得UTC时间：
		cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		return cal.getTimeInMillis();
	}
	
	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		 long now=DateUtils.getCurrentUTCTime();
		 calendar.setTimeInMillis(now);
		 int day=calendar.get(Calendar.DAY_OF_WEEK)-1;
		 System.out.println(day);
		}

    public static String getMinuteStr(long sec) {
        StringBuilder stringBuilder = new StringBuilder();
        if(sec>59){
            timeBuildString(sec, stringBuilder);
        }else {
            stringBuilder.append("00:");
            long secValue = sec%60;
            if(secValue<10){
                stringBuilder.append("0");
            }
            stringBuilder.append(secValue);
        }
        return stringBuilder.toString();
    }

    private static void timeBuildString(long sec, StringBuilder stringBuilder) {
        long secValue = sec%60;
        long minValue = sec/60;
        if(minValue<10){
            stringBuilder.append("0");
        }
        stringBuilder.append(minValue).append(":");
        if(secValue<10){
            stringBuilder.append("0");
        }
        stringBuilder.append(secValue);
    }

    /**
     * 获取精确到秒的时间戳
     * @return
     */
    public static int getSecondTimestamp(Date date){
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime());
        int length = timestamp.length();
        if (length > 3) {
            return Integer.valueOf(timestamp.substring(0,length-3));
        } else {
            return 0;
        }
    }

}
