
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.DiscountTestService;
import com.shopify.api.ShopifyApiService;
import com.shopify.api.aci.AciDeliveryData;
import com.shopify.api.aci.AciDeliveryService;
import com.shopify.api.aci.AciDeliveryService2;
import com.shopify.api.aci.AesService;
import com.shopify.api.aci.AesService2;
import com.shopify.common.RestService;
import com.shopify.config.FileUploadConfig;
import com.shopify.shipment.popup.ShipmentPopupData;
import com.shopify.shop.ShopData;


@PropertySource("classpath:properties/config.properties")
@PropertySource("classpath:properties/shopifyApi.properties")
@PropertySource("classpath:properties/lotteLogis.properties")
@PropertySource("classpath:properties/pantos.properties")
@PropertySource("classpath:properties/aci.properties")
@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({
    FileUploadConfig.class
})

@ComponentScan(basePackages = "com.shopify")
public class BeanRunner {
	
//	 public static void main(String[] args) throws Exception {
//		 
//	        ApplicationContext context = SpringApplication.run(BeanRunner.class, args);
	        
//	        testSetSendApiData(context);
//	        setOrderDeliveryAciXXXXXXXXX(context);
	        
//	        System.exit(0);
//
//	 }
	 
	 
	 private static void aci(ApplicationContext context) {
		 AesService2 aes2 = context.getBean(AesService2.class);
		 
			String key = "abcdefghijklmnopqrstuvwxyz123456";
		 
		 String plainText  = "imcore.net";
		 System.out.println(plainText);
		 
		 String encode = aes2.encode(plainText);
		 System.out.println(encode);
		 
		 String decode = aes2.decode(encode);
		 System.out.println(decode);
		
	}

	 
	 private static void testSetSendApiData(ApplicationContext context) {
		 
		 AciDeliveryService2 app = context.getBean(AciDeliveryService2.class);
		 
		 List<ShipmentPopupData> list = new ArrayList<>();
		 
		 ShipmentPopupData data = new ShipmentPopupData();
		 data.setMasterCode("201102132651IJ9L");
		 list.add(data);
		 
		 data = new ShipmentPopupData();
		 data.setMasterCode("201030131558C5KH");
		 list.add(data);
		 
		 data = new ShipmentPopupData();
		 data.setMasterCode("201102130701I4TZ");
		 list.add(data);
		 
		 
		 app.testSetSendApiData(list);	
		
	}
	 private static void setOrderDeliveryAciXXXXXXXXX(ApplicationContext context) {
		 
		 AciDeliveryService2 app = context.getBean(AciDeliveryService2.class);
		 
		 List<ShipmentPopupData> list = new ArrayList<>();
		 
		 ShipmentPopupData data = new ShipmentPopupData();
		 data.setMasterCode("201102132651IJ9L");
		 list.add(data);
		 
		 data = new ShipmentPopupData();
		 data.setMasterCode("201030131558C5KH");
		 list.add(data);
		 
		 data = new ShipmentPopupData();
		 data.setMasterCode("201102130701I4TZ");
		 list.add(data);
		 
		 
		 app.setOrderDeliveryAciXXXXXXXXX(list);	
		 
	 }
	
}
