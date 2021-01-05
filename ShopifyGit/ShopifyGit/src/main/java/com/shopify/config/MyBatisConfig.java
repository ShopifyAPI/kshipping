package com.shopify.config;
 
import javax.sql.DataSource;
 
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.shopify.interceptor.CertificationInterceptor;
 
@SuppressWarnings("deprecation")
@Configuration
@MapperScan(basePackages = "com.shopify.mapper")
public class MyBatisConfig extends WebMvcConfigurerAdapter {
	
	@Value("${certificate.skip}")	
	private	boolean certificateSkip;
    
    @Bean
	public SqlSessionFactory sqlSessionFactory (DataSource dataSource) throws Exception {
    	SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    	sessionFactory.setDataSource(dataSource);
    	
        Resource[] res = new PathMatchingResourcePatternResolver().getResources("classpath:mappers/*Mapper.xml");
        sessionFactory.setMapperLocations(res);
        sessionFactory.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        sessionFactory.setVfs(SpringBootVFS.class);
        
        Resource myBatisConfig = new PathMatchingResourcePatternResolver().getResource("classpath:config/mybatis-config.xml");
        sessionFactory.setConfigLocation(myBatisConfig);

        
        return sessionFactory.getObject();
	}
    
    @Bean
	public SqlSessionTemplate sqlSession (SqlSessionFactory sqlSessionFactory) {
    	return new SqlSessionTemplate(sqlSessionFactory);
	}
    
    //interceptor 에서 로그인처리시 예외 페이지 설정
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	
    	CertificationInterceptor certificationInterceptor = new CertificationInterceptor();
    	certificationInterceptor.setCertificateSkip(this.certificateSkip);
    	
        registry.addInterceptor(certificationInterceptor)
                .addPathPatterns("/*")
                .addPathPatterns("/admin/**")
		        .addPathPatterns("/cs/*")
		        .addPathPatterns("/setting/*")
		        .addPathPatterns("/board/*")
                .excludePathPatterns("/login")
                .excludePathPatterns("/loginOri")
                .excludePathPatterns("/loginProc")
                .excludePathPatterns("/logout")
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/admin/loginProc")
                .excludePathPatterns("/admin/logout")
                .excludePathPatterns("/shopify**")
                ;
    }

}
