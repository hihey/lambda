package com.lambda.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 终端（客户端/页面访问端）
 * @author WKX
 */
@EnableHystrix
@EnableDiscoveryClient
@SpringBootApplication
@Configuration
public class GatewayApplication {

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Value("${my.gateway.url}")
    private String uri;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
	        .route(r -> r.path("/api/**").filters(f -> f.stripPrefix(1).addRequestHeader("Hello", "World")).uri(uri))
	        .route(r -> r.path("/old/**").filters(f -> f.hystrix(config -> config.setName("fallback").setFallbackUri("forward:/fallback"))).uri(uri)).build();
    }
	
	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}
}