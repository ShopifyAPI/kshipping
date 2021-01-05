package com.shopify.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Value("${spring.profiles.active}") private String activeProfile;
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		// 허용되어야 할 경로들
		web.ignoring().antMatchers(
				"/resources/**"
				,"/shopifyShopAuth"
				,"/shopifyOAuth"
				,"/shopifyInstall"
				,"/shopifyGenToken"
				,"/shopifyGetOrder"
				,"/shopify**"
				,"/shopifyOutApi/**"
				,"/uploadFile"
				,"/admin/board/insertNotice"
				,"/admin/board/updateNotice"
				,"/admin/uploadPriceExcelFile"
				,"/admin/uploadVolumeWeightExcelFile"
				,"/board/faq"
				,"/api/pantos/tracking"
				,"/api/lotte/homeTracking"
				,"/order/popup/popupAddressChange"
				);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf()
        		.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        .and()
        	.logout()
        		.logoutSuccessUrl("/login")
        		.logoutUrl("/logout")
        		//.logoutSuccessHandler(customLogoutSuccessHandler)
        		.invalidateHttpSession(true)
        		//.deleteCookies(JSESSIONID)
        .and()
        	.formLogin()
            	.loginPage("/login").permitAll()
            	.loginPage("/admin/login").permitAll()
            	
		.and()
			.authorizeRequests()
				.antMatchers("/board/faq").permitAll()
				.antMatchers("/board**").permitAll()
				.antMatchers("/order**").permitAll()
				.antMatchers("/api/**").permitAll()
				.antMatchers("/login/**").permitAll()
				.antMatchers("/admin/**").permitAll()
				.antMatchers("/faqUpdate").permitAll()
				.antMatchers("/shopify**").permitAll()
				.antMatchers("/shopifyOutApi/**").permitAll()
				.antMatchers("/", "/resources/static/**").permitAll()
		.and()
				.headers().frameOptions().disable()
		;
		
		String[] profs  = { "pc", "dev", "qa" }; 
		for( String name : profs ) {
			if ( activeProfile.equals(name) ) {
				http.headers().httpStrictTransportSecurity().disable();
			}
		}
	}
	
	@Bean
    public HttpFirewall allowSemicolonHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowSemicolon(true);
        return firewall;
    }
}

