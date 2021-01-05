package com.shopify;

import java.util.Locale;

import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.shopify.config.FileUploadConfig;

@PropertySource("classpath:properties/config.properties")
@PropertySource("classpath:properties/shopifyApi.properties")
@PropertySource("classpath:properties/lotteLogis.properties")
@PropertySource("classpath:properties/pantos.properties")
@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({
    FileUploadConfig.class
})

public class ShopifyApplication extends SpringBootServletInitializer{

	@Value("${spring.mvc.locale}")
	Locale locale = null;

	@Value("${spring.messages.basename}")
	String messagesBasename = null;
	@Value("${spring.messages.encoding}")
	String messagesEncoding = null;
	@Value("${spring.messages.cache-seconds}")
	int messagesCacheSeconds;
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		System.out.println("============== configure started =================");
		return builder.sources(ShopifyApplication.class);
	}

	public static void main(String[] args) {
		System.out.println("============== main started =================");
	    java.security.Security.setProperty("networkaddress.cache.ttl" , "60");
		SpringApplication.run(ShopifyApplication.class, args);
		System.out.println("============== server started =================");
	}

//	@Bean
//	public HttpSessionListener httpSessionListener(){
//	    return new SessionListener();
//	}
}
