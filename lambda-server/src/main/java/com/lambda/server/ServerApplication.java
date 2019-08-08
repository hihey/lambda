package com.lambda.server;

import javax.servlet.MultipartConfigElement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.unit.DataSize;
import org.springframework.web.client.RestTemplate;

/**
 * 服务端（数据持久层）
 * @author WKX
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableCaching
@EnableTransactionManagement
@Configuration
@EnableAsync
@MapperScan("com.lambda.server.mapper")
public class ServerApplication {
	
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> containerCustomizer() throws Exception {
	    return (ConfigurableServletWebServerFactory container) -> {
	        if (container instanceof TomcatServletWebServerFactory) {
	        	TomcatServletWebServerFactory tomcat = (TomcatServletWebServerFactory) container;
	            tomcat.addConnectorCustomizers(
	                (connector) -> {
	                    connector.setMaxPostSize(10000000);//10MB
	                }
	            );
	        }
	    };
	}
	
	/**
     * 文件上传配置
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.parse("204800KB"));//单个文件最大（KB,MB）
        factory.setMaxRequestSize(DataSize.parse("204800KB"));//设置总上传数据总大小
        return factory.createMultipartConfig();
    }
    
    /**
     * 配置线程池
     */
    @Bean(name="processExecutor")
    public TaskExecutor workExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix("Async-");
        threadPoolTaskExecutor.setCorePoolSize(5);
        threadPoolTaskExecutor.setMaxPoolSize(10);
        threadPoolTaskExecutor.setQueueCapacity(25);
        threadPoolTaskExecutor.afterPropertiesSet();
        return threadPoolTaskExecutor;
    }

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}
}