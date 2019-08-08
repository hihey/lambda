package com.lambda.server;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.lambda.server.filter.CrossFilter;

/**
 * 配置拦截器
 * @author WKX
 * addPathPatterns 用于添加拦截规则
 * excludePathPatterns 用户排除拦截
 */
@Configuration
public class WebAppConfig implements WebMvcConfigurer {

/* *********拦截器********************************************************************************************* */
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry)
	 */
	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
	}
	
	@Bean
	public Filter getCrossFilter() {//必须以Bean的形式创建
		return new CrossFilter();
	}
	
	@Bean
    public FilterRegistrationBean<Filter> indexFilterRegistration() {
		FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<Filter>();
		registration.setFilter(getCrossFilter());
		registration.setOrder(1);
        registration.addUrlPatterns("/*");
        return registration;
    }
}