package com.lambda.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class MyProperties implements ApplicationListener<ContextRefreshedEvent> {
	
	@Value("${my.base.path}")
	private String basePath;
	
	/**
	 * 项目路径（BASE）
	 */
	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	@Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {}
}