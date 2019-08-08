package com.lambda.sleuth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import zipkin2.server.internal.EnableZipkinServer;

/**
 * 链路追踪服务器
 * @author WKX
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableZipkinServer
public class SleuthApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(SleuthApplication.class, args);
	}
}