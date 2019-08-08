package com.lambda.core.utils;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
import org.apache.commons.lang.StringUtils;
 
/**
 * 字符串替换（工具类）
 * @author WKX
 */
public class StringTemplateUtils {
    
    public static final String DEF_REGEX="\\{(.+?)\\}";
     
    public static String render(String template, Map<String, String> data) {
        return render(template,data,DEF_REGEX);
    }
     
    public static String render(String template, Map<String, String> data,String regex) {
        if(StringUtils.isBlank(template)){
            return "";
        }
        if(StringUtils.isBlank(regex)){
            return template;
        }
        if(data == null || data.size() == 0){
            return template;
        }
        try {
            StringBuffer sb = new StringBuffer();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(template);
            while (matcher.find()) {
                String name = matcher.group(1);//键名
                String value = data.get(name);//键值
                if (value == null) {value = "";}
                matcher.appendReplacement(sb, value);
            }
            matcher.appendTail(sb);
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return template;
    }
    
    /**
     * 数组转字符串
     * @param arr 数组
     */
    public static String arraytoString(Object[] arr) {
    	StringBuffer sb = new StringBuffer();
    	if(arr!=null && arr.length>0){
    		for(int i = 0; i < arr.length; i++){
        		if(sb.length()>0)sb.append(",");
        		sb.append(arr[i]);
        	}
    	}
    	String s = sb.toString();
    	return s;
    }
    
    /**
     * 隐藏部分字符串
     * @author WKX
     * @param src 原字符串
     */
    public static String hideString(Object src) {
    	if(src==null)return "";
    	String src_ = src.toString();
    	int length = src_.length();
    	int len = length/3;
    	int yu = length%3;
    	String start = src_.substring(0,len);
    	String end = src_.substring(len*2+yu/2,length);
    	return start + "***" + end;
    }

    /**
     * 测试
     */
    public static void main(String args[]) throws ParseException {
        String template="您提现{borrowAmount}元至尾号{tailNo}的请求失败，您可以重新提交提款申请。";
        Map<String, String> data = new HashMap<String, String>();
        data.put("borrowAmount", "1000.00");
        data.put("tailNo", "1234");
        System.out.println(render(template,data));
        
        System.err.println(hideString("13818994981"));
    }
}