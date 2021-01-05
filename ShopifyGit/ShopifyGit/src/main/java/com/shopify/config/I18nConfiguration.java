package com.shopify.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import java.util.Locale;
/** * 로케일 처리 순서 
* 1. 웹 요청의 로케일을 확인
* 1.1) 로케일 리졸버의 방식에 따라 세션 또는 쿠키에서 로케일 확인
* 1.2) 세션 또는 쿠키에서 로케일 확인이 안될 때(ex.첫 요청등)는 브라우저가 보내는 로케일 확인
* 1.2.1) 단, Resolver의 DefaultLocale이 설정되어 있을 경우 해당 값으로 덮어쓰기
* 2. 로케일에 맞는 메세지 파일의 값을 호출
* 3. 로케일에 맞는 메세지 파일이 없는 경우
* 3.1) FallBackToSystemLocale이 true(기본값)인 경우
* 3.1.1) 시스템 로케일에 맞는 메세지 파일의 값을 호출
* 3.1.2) 못찾았을 경우 기본 메세지 파일(messages.properties)의 값을 호출
* 3.2) FallBackToSystemLocale이 false인 경우
* 3.2.1) 기본 메세지 파일(messages.properties)의 값을 호출
* 4. 로케일에 맞는 메세지 파일이 없거나 값을 못찾았을 경우 예외 발생
*/

@Configuration
public class I18nConfiguration extends WebMvcConfigurerAdapter {

	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("/messages");		// in classpath
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setFallbackToSystemLocale(false);
		return messageSource;
	}

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		/* CookieLocaleResolver localeResolver = new CookieLocaleResolver();
		localeResolver.setCookieName("my-locale-cookie");
		localeResolver.setCookieMaxAge(3600);
		// in second (-1: deleted when client shuts down) */
		localeResolver.setDefaultLocale(Locale.KOREAN);
		return localeResolver;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
		interceptor.setParamName("lang");		// default: "locale"
		return interceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}
}
