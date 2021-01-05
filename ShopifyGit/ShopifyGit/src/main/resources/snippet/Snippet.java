package snippet;

public class Snippet {
	#개발용
	shopify.AppId=8ddc97ed345de0e6f565f19d1fad27a6
	shopify.AppPw=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
	#shopify.AppId=485014c679f909c871e973ab93c9765b
	#shopify.AppPw=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
	#shopigate@shopify.com용
	#shopify.AppId = dd2eba13c56eb7f4fadbdc42477451da
	#shopify.AppPw = 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
	shopify.App.callbackUrl=https://www.qakship.co.kr/shopifyOutApi/apiCarrierServiceSelect
	shopify.App.carrierName=KShippingQA
	shopify.redirectUri=https://www.qakship.co.kr/shopifyGenToken
	shopify.applicationCharge.returnUrl=https://www.qakship.co.kr/shipment/confirm/
	
	# 기존 
	#spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
	#spring.datasource.url=jdbc:mysql://1.231.29.25:3306/shopify?useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC
	# 신규 log4j 사용 
	spring.datasource.driver-class-name=net.sf.log4jdbc.sql.jdbcapi.DriverSpy
	spring.datasource.url=jdbc:log4jdbc:mysql://192.168.0.237:3306/qa_shopify?useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC
	spring.datasource.username=qa_shopify
	spring.datasource.password=qa_solugate
	
	
	server.ssl.key-store=classpath:keystore.p12
	server.ssl.key-store-type=PKCS12
	server.ssl.key-store-password=shopify
	server.ssl.key-alias=tomcat
	
	## 프로젝트내의 폴더에 업로드 파일을 설정하고 싶으면 아래 줄처럼 
	#file.upload-dir=./uploads 
	## 본인 PC의 특정 dir에 업로드 하고 싶으면 아래줄처럼 설정한다. 
	#file.upload-dir=D:/Users/yun/upload
	file.upload-dir=/data/upload_qa
	
	# Mail SMTP
	mail.smtp.host=wsmtp.ecounterp.com
	mail.smtp.port=587
	mail.smtp.user=shopigate@solugate.com
	mail.smtp.pass=tyvlrpdlxm!23
	mail.smtp.mail=shopigate@solugate.com
	
	# 롯데 로지스 택배 거래처 코드(테스트용) 
	lottte.logis.jobcustcd=983985 
	
	# 우체국 연동 API인증 키(테스트용) 
	post.api.regkey=bf1703e42ec9c7fad1582172325332
	
	shopify.App.name=qakship
	
	logging.config=classpath:logback-qa.xml
	
	
	#lotte I/F testMode
	lotte.api.test=Y
	
	# debug rest message
	solugate.rest.debug=true
	
	# qa skip agree
	
	certificate.skip=true
	
}

