package com.shopify.common.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopify.common.SpConstants;
import com.shopify.shop.ShopData;

@RestController
public class I18nController {
	
	@Value("classpath:messages")
	private Resource resourceFile;
	
	private Map<String, String> messageMap = new HashMap<>();
	
//	private Logger LOGGER = LoggerFactory.getLogger(I18nController.class);
	
	@PostConstruct
	 private void postConstruct() throws IOException {
		
		File[] files = resourceFile.getFile().listFiles();
		Matcher m = Pattern.compile("message_(..)\\.properties").matcher("");
		String lang = null;
		
		for( File f : files ) {
			String name = f.getName();
			if ( m.reset(name).find() ) {
				lang = m.group(1);
			} else {
				lang = "en";
			}
			messageMap.put(name, lang);
		}
		
//		for( Entry<String, String> e : messageMap.entrySet() ) {
//			LOGGER.debug(e.getKey() + " = " + e.getValue());
//		}
    }
	
	@RequestMapping("/messages/{propertiesName}")
	public void getProperties(@PathVariable String propertiesName, HttpServletRequest req, HttpServletResponse response, HttpSession sess) throws IOException {
//		LocaleConfig localeConfig = new LocaleConfig();
//		String lang = localeConfig.localeResolver().resolveLocale(req).toString();
//		//String[] res = propertiesName.split("_");
//		
//		if("ko_KR".equals(lang) || "ko".equals(lang)) {
//			lang = "ko";
//		}else {
//			lang = "en";
//		}
		
		String lang = messageMap.containsKey(propertiesName) ? messageMap.get(propertiesName) : "en";
		System.out.println("I18nController > #########"+lang);
		
		//lang에 따른 properties가져오기
		Resource resource = new ClassPathResource("messages/" + propertiesName );
		//System.out.println("#########"+resource);
		
		String readLines = "";
		try( InputStream inputStream = resource.getInputStream() ) {
			readLines = IOUtils.toString(inputStream, "UTF-8");
//		readLines = new String(readLines.getBytes("ISO-8859-1"), "UTF-8");
			readLines = readLines.replaceAll("\\=", "\\:");
			readLines = readLines.replaceAll("(\r\n|\r|\n|\n\r)", ",");
			
			String unescapeJava = StringEscapeUtils.unescapeJava(readLines);
			
			OutputStream outputStream = response.getOutputStream();
			IOUtils.write(unescapeJava, outputStream, "UTF-8");
		}
		
		ShopData sd = new ShopData(); 
		
		//if(sd != null) {
		    sd.setLocale(lang);
		    sess.setAttribute(SpConstants.LOCALE_SESSION_KEY, sd);
		    System.out.println("######### setAttribute[LOCALE_SESSION_KEY] : " + resource);
		    System.out.println("######### setAttribute[lang] : " + lang);
		//}
		
		//properties 다국어 front에서 처리하기위함
//		sess.setAttribute(SpConstants.MSG_SESSION_KEY, readLines);
		//System.out.println("#########"+readLines);
		
		//System.out.println("#########"+localeConfig.localeResolver().resolveLocale(req));
	}
}
