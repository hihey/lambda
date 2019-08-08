package com.lambda.core.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * 时间工具类
 */
public class DateUtil {
	
	/**
	 * 含毫秒（不会丢失精度）
	 * yyyy-MM-dd HH:mm:ss SSSZ
	 */
	public static final String DATE_TIME_SSSZ = "yyyy-MM-dd HH:mm:ss SSSZ";
	
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * yyyy-MM-dd
	 */
	public static final String DATE = "yyyy-MM-dd";
	
	/**
	 * HH:mm:ss
	 */
	public static final String TIME = "HH:mm:ss";
	
	/**
	 * 将日期按照格式返回 字符串
	 * @param date 日期
	 * @param formart 日期格式
	 * @return String 格式化之后的日期字符串
	 */
	public static String formart(Date date, String format) {
		if(date==null)return null;
		if(StringUtils.isBlank(format))format = DATE_TIME;
		SimpleDateFormat simple = new SimpleDateFormat(format);
		return simple.format(date);
	}
	
	/**
	 * 将字符串按照格式返回
	 * @param date 日期字符串
	 * @param format 日期格式
	 * @return Date 格式化之后的日期对象
	 */
	public static Date parser(String date, String format) throws ParseException {
		if(StringUtils.isBlank(date))return null;
		if(StringUtils.isBlank(format))format = DATE_TIME;
		
		SimpleDateFormat simple = new SimpleDateFormat(format);
		return simple.parse(date);
	}
	
	/**
	 * 增加日期
	 * @param date
	 * @param amount 正数往后，负数往前推
	 * @return
	 */
	public static Date addDays(Date date, int amount) {
		Calendar cal = Calendar.getInstance();  
        cal.setTime(date);  
        cal.add(Calendar.DATE, amount);
        return cal.getTime();
	}
	
	/**
	 * 时间退后或提前多少秒（正 往后，负往前）
	 * @author MH
	 * @param date
	 * @param second
	 * @return
	 */
	public static Date addMinusSecond(Date date,int second) {    
	    Calendar calendar = Calendar.getInstance();    
	    calendar.setTime(date);    
	    calendar.add(Calendar.SECOND, second);    
	    return calendar.getTime();    
	}
	
	/**
	 * 时间退后或提前多少分钟（正 往后，负往前）
	 * @param date 时间
	 * @param minute 分钟
	 */
	public static Date addMinusMinute(Date date,int minute) {    
	    Calendar calendar = Calendar.getInstance();    
	    calendar.setTime(date);    
	    calendar.add(Calendar.MINUTE, minute);    
	    return calendar.getTime();    
	}
	
	/**
	 * 当前时间加几天
	 * @param amount
	 */
	public static Date addDays(int amount) {
		return addDays(new Date(), amount);
	}
	
	/**  
     * 计算两个日期之间相差的天数  
     * @param smdate 较小的时间 
     * @param bdate  较大的时间 
     * @return 相差天数
     * @throws ParseException  
     */    
    public static int daysBetween(Date smdate,Date bdate) throws ParseException{    
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);
        return Integer.parseInt(String.valueOf(between_days));
    } 
    
    /**  
     * 计算两个日期之间相差的分钟数
     * @param smdate 较小的时间 
     * @param bdate  较大的时间 
     * @throws ParseException  
     */    
    public static int minusBetween(Date smdate,Date bdate) throws ParseException{
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 60);
            
        return Integer.parseInt(String.valueOf(between_days));           
    } 
    
    /**
     * 计算两个日期之间相差的月数  
     * @param smdate 较小的时间 
     * @param bdate  较大的时间 
     * @return 相差月数 
     * @throws ParseException
     */
    public static int monthsBetween(Date smdate,Date bdate) throws ParseException{
    	int result = 0;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(smdate);
		c2.setTime(bdate);
		int month2 = c2.get(Calendar.MONTH);
		int month1 = c1.get(Calendar.MONTH);
		int months = 0;
		int years = 0;
		int year1 = c1.get(Calendar.YEAR);
		int year2 = c2.get(Calendar.YEAR);
		if (month2 - month1 < 0) {
			months = 12 - month1 + month2;
			years = year2 - year1 - 1;
		} else {
			months = month2 - month1;
			years = year2 - year1;
		}
		result = 12 * years + months;
        return result;
	}
    
    /**
	 * 获取上月第一天
	 * @author WKX
	 * @since 2016年1月21日 下午4:24:01
	 */
	public static Date getPreviousMonthFirst() {
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(5, 1);
		lastDate.add(2, -1);
		return lastDate.getTime();
	}
	
	/**
	 * 获取上月最后一天
	 */
	public static Date getPreviousMonthEnd() {
		Calendar lastDate = Calendar.getInstance();
		lastDate.add(2, -1);
		lastDate.set(5, 1);
		lastDate.roll(5, -1);
		return lastDate.getTime();
	}
	
	/**
	 * 日期加減几个月
	 * @param date
	 * @param addMonths 几个月
	 */
	public static Date addMonth(Date date, int addMonths){
		Calendar cal = Calendar.getInstance();  
		cal.setTime(date);
		cal.add(Calendar.MONTH,addMonths);
		date = cal.getTime();
		return date;
	}
	
	/**
	 * 日期加减几年
	 * @param date 日期
	 * @param year 添加的年数
	 */
	public static Date addYear(Date date, int year){
		Calendar cal = Calendar.getInstance();  
		cal.setTime(date);
		cal.add(Calendar.YEAR,year);
		date = cal.getTime();
		return date;
	}
	
	/**
	 * 长整型转时间（字符串）
	 * @param dateFormat 格式
	 * @param millSec 时间long型
	 */
	public static String transferLongToString(String dateFormat, Long millSec) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date date = new Date(millSec);
		return sdf.format(date);
	}
	
	/**
	 * 长整型转时间（Date）
	 * @param millSec 时间long型
	 */
	public static Date transferLong(Long millSec) {
		Date date = new Date(millSec);
		return date;
	}
	
    /**
     * 获取 相差的 秒数
     * @param smdate 小的时间
     * @param bdate 大的时间
     * @throws ParseException
     */
    public static int calSeconds(Date smdate,Date bdate) throws ParseException{
    	long time1 = smdate.getTime();               
        long time2 = bdate.getTime();         
        long between_days=(time2-time1)/(1000);
        return Integer.parseInt(String.valueOf(between_days));
    }
    
    /**
     * 校验字符串日期格式是否正确（yyyyMMdd）
     * @author WKX
     * @param date 字符串日期
     */
    public static boolean isValidDate(String date) {
		return isValidDate(date, "yyyyMMdd");
	}
    
    /**
     * 校验字符串日期格式是否是指定格式
     * @author WKX
     * @param date 字符串日期
     * @param format 日期格式
     */
    public static boolean isValidDate(String date,String format) {
		boolean convertSuccess = true;
		SimpleDateFormat format_ = new SimpleDateFormat(format);
		try {
			format_.setLenient(false);
			format_.parse(date);
		} catch (ParseException e) {
			convertSuccess = false;
		}
		return convertSuccess;
	}
    
    /**
     * 获取区间内的时间（每一天）
     * @param dBegin 开始日期
     * @param dEnd 截止日期
     * @param format 格式
     */
    public static List<String> findDates(Date dBegin, Date dEnd,String format) {
    	if(StringUtils.isBlank(format))format = DATE;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		List<String> lDate = new ArrayList<String>();
		Calendar calBegin = Calendar.getInstance();
		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(dEnd);
		// 测试此日期是否在指定日期之后
		while (dEnd.after(calBegin.getTime())) {
			lDate.add(sdf.format(calBegin.getTime()));
			
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
		}
		return lDate;
	}
    
    /**
     * 获取区间内的时间（每一天）
     * @param dBegin 开始日期
     * @param dEnd 截止日期
     */
    public static List<Date> findDates(Date dBegin, Date dEnd) {
		List<Date> lDate = new ArrayList<Date>();
		Calendar calBegin = Calendar.getInstance();
		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(dEnd);
		// 测试此日期是否在指定日期之后
		while (dEnd.after(calBegin.getTime())) {
			lDate.add(calBegin.getTime());
			
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
		}
		return lDate;
	}
    
    /**
     * 任意格式转时间
     * @param date 字符串时间
     */
    public static Date parseStringToDate(String date) throws ParseException{
    	if(StringUtils.isBlank(date))return null;
    	date = addTo(date);//补充不完整的日期
    	
        Date result = null;
        String parse = date;
        parse = parse.replaceFirst("^[0-9]{4}([^0-9])", "yyyy$1");
        parse = parse.replaceFirst("^[0-9]{2}([^0-9])", "yy$1");
        parse = parse.replaceFirst("([^0-9 ^\\s ^:])[0-9]{1,2}([^0-9])", "$1MM$2");
        parse = parse.replaceFirst("([^0-9 ^:])[0-9]{1,2}( ?)", "$1dd$2");
        if(parse.equals(date)){//防止日期是连续的无特殊符号间隔
        	if(date.length()>=8){//满足日期长度
        		parse = parse.replaceFirst("^[0-9]{4}([0-9])", "yyyy$1");
        	}
            parse = parse.replaceFirst("^[0-9]{2}([0-9])", "yy$1");
            parse = parse.replaceFirst("([0-9]||[^0-9])[0-9]{1,2}([0-9])", "$1MM$2");
            parse = parse.replaceFirst("([0-9]||[^0-9])[0-9]{1,2}( ?)", "$1dd$2");
        }
        parse = parse.replaceFirst("( )[0-9]{1,2}([^0-9])", "$1HH$2");
        if(date.length()-date.replaceAll(":", "").length()==1){//验证是否有秒
        	parse = parse.replaceFirst("([^0-9])[0-9]{1,2}([^0-9]?)", "$1mm$2");
        }else{
        	parse = parse.replaceFirst("([^0-9])[0-9]{1,2}([^0-9])", "$1mm$2");
        }
        parse = parse.replaceFirst("([^0-9])[0-9]{1,2}([^0-9]?)", "$1ss$2");
        
        DateFormat format = new SimpleDateFormat(parse);
        result = format.parse(date);
        return result;
    }
    
    /**
     * 不足日期（缺少年或日）
     * @param str 原始日期
     */
    private static String addTo(String str){
    	List<String> list = Arrays.asList(new String[]{"/","-","."});
    	for(String k:list){
    		if(str.indexOf(k)!=-1 && str.split(k).length==2){
        		String temp[] = str.split(k);
        		if(Integer.valueOf(temp[0])>12){
        			Calendar now = Calendar.getInstance();
        			str += k + now.get(Calendar.DAY_OF_MONTH);
        		}else{
        			Calendar now = Calendar.getInstance();
        			str = now.get(Calendar.YEAR) + k + str;
        		}
        	}
    	}
    	return str;
    }
}