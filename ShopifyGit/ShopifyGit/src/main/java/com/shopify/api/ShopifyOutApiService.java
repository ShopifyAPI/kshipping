package com.shopify.api;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.shopify.mapper.SettingMapper;
import com.shopify.mapper.ShopifyOutApiMapper;
import com.shopify.shop.ShopData;

/**
 * api
 *
 */
@Service
@Transactional
public class ShopifyOutApiService {
	private Logger LOGGER = LoggerFactory.getLogger(ShopifyOutApiService.class);
	@Autowired private ShopifyOutApiMapper spApiMapper;
	@Autowired private SettingMapper settingMapper;
//	public List<ShopifyOutApiDataCarrier> selectCarrier(ShopifyOutApiDataCarrier spApi){
//		return spApiMapper.selectCarrier(spApi);
//	}
	
	public ShopifyOutApiDataCarrier selectCarrierCount(ShopifyOutApiDataCarrier spApi){
		return spApiMapper.selectCarrierCount(spApi);
	}
	
	public int insertCarrier(ShopifyOutApiDataCarrier spApi){
		return spApiMapper.insertCarrier(spApi);
	}
	
	public int updateCarrier(ShopifyOutApiDataCarrier spApi){
		return spApiMapper.updateCarrier(spApi);
	}
	
	public int deleteCarrier(ShopifyOutApiDataCarrier spApi){
		return spApiMapper.deleteCarrier(spApi);
	}
	
	public String apiCarrierServiceSelect(ShopifyOutApiDataCarrier aData, String domain) {
		
		if ( StringUtils.isBlank(domain)) {
			LOGGER.error("shop domain 이 없습니다.");
		}
		ShopData sd = new ShopData();
		int shopIdx = 0 ;
		try 
		{
			sd = spApiMapper.selectEmailFromDomain(domain);
			aData.setEmail(sd.getEmail());
			shopIdx=sd.getShopIdx();
		}
		catch (Exception e) {
			LOGGER.error("shop domain 이 없습니다.");
		}
		
		//ShopData shop = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		//if(shop != null) {
			String localLocale = "en";//shop.getLocale();
			aData.setLocalLocale(localLocale);
		//}
		LOGGER.debug("### ShopifyOutApiDataCarrier Start aData###"+aData);
    	ModelAndView mav = new ModelAndView("jsonView");
		//out param
    	JSONObject rates = new JSONObject();
		JSONArray ratesAry = new JSONArray();
		
		List<ShopifyOutApiDataCarrier> reqData = new ArrayList<ShopifyOutApiDataCarrier>();
		try {
			int gram = 0;
			int weightsix =0;
			int weightfive =0;
			if(aData != null && aData.getRate().getItems() != null) {
				int itemSize = aData.getRate().getItems().size();
				
				////// 부피중량 체크 Start //////
				List<ShopifyOutApiDataCarrierItems> weightitems = aData.getRate().getItems();
				if(shopIdx !=0) {
					for(ShopifyOutApiDataCarrierItems weightitem : weightitems) {
						weightitem.setShopIdx(sd.getShopIdx());
					}
				}
				
 				List<ShopifyOutApiDataCarrierItems> list = settingMapper.selectVolumeWeight(weightitems);
				int getWeightsix=0;
				int getWeightfive=0;
				int getGram=0;
				if(itemSize > 0) {
					for(int n = 0; n < itemSize; n++) {
						if(weightitems.get(n) != null) {
							getGram = weightitems.get(n).getGrams();
							if(list.size()>n){
								int i=0;
								while(i<list.size())
								{
									if(list.get(i).getProductCode().equals(weightitems.get(n).getProduct_id())) {										
										getWeightsix=list.get(i).getVolumeWeightSix();
										getWeightfive = list.get(i).getVolumeWeightFive();
										i++;
									}
									else
										i++;
								}
							}
							else {
								getWeightsix=0;
								getWeightfive=0;
							}
							int itemNum = weightitems.get(n).getQuantity();
							if(getGram<getWeightsix) {
								getWeightsix=getWeightsix*itemNum;
								getWeightfive=getWeightfive*itemNum;
								getGram=getGram*itemNum;
							}
							else{
								if(getGram<getWeightfive) {
									getWeightsix=getGram*itemNum;
									getWeightfive=getWeightfive*itemNum;
									getGram=getGram*itemNum;
								}
								else {
								getGram=getGram*itemNum;
								getWeightsix=getGram*itemNum;
								getWeightfive=getGram*itemNum;
								
								}}
	
							gram = gram+getGram;
							weightsix = weightsix+getWeightsix;
							weightfive = weightfive+getWeightfive;
							LOGGER.debug("### ShopifyOutApiDataCarrierItems ## Gram:"+gram+"## Weightdividesix: "+weightsix +"## Weightdividesix: "+weightfive );
						}
					}
				}
				////// 부피중량 체크 End //////											
				aData.setLocale(aData.getRate().getDestination().getCountry());
				aData.setWeight(gram);
				aData.setVolumeWeightSix(weightsix);
				aData.setVolumeWeightFive(weightfive);
				
			}
			
			reqData = spApiMapper.selectCarrier(aData);
			
			System.out.println("######apiCarrierService###reqData111==>"+reqData);
			
			if(reqData != null) {
				
				int ratesItemSize = reqData.size();
				//System.out.println("######apiCarrierServiceCreate###reqData==>"+reqData.toString());
				for(int n = 0; n < ratesItemSize; n++) {
					JSONObject rates_item = new JSONObject();
					/*System.out.println("######apiCarrierServiceCreate###n==>"+n);
					System.out.println("######apiCarrierServiceCreate###reqData.get(n)==>"+reqData.get(n));*/
					String desctiption = "Estimated delivery time: "+ reqData.get(n).getMinDeliveryDate() + " ~ " + reqData.get(n).getMaxDeliveryDate() + " days (Due to COVID19 there will be extra delays with customs processing)";
					rates_item.put("service_name", reqData.get(n).getCodeName());
					rates_item.put("service_code", reqData.get(n).getCode());
					rates_item.put("total_price", reqData.get(n).getPrice()+"00");		//쇼피파이에서는 센트단위로 인식 하는것 같음..
					System.out.println("######apiCarrierService###total_price111==>"+reqData.get(n).getPrice());
					rates_item.put("description", desctiption);
					rates_item.put("currency", "KRW");
					rates_item.put("min_delivery_date", reqData.get(n).getMinDeliveryDate());
					rates_item.put("max_delivery_date", reqData.get(n).getMaxDeliveryDate());
					ratesAry.put(n, rates_item);
				}
			}else {
				JSONObject rates_item = new JSONObject();
				rates_item.put("service_name", "");
				rates_item.put("service_code", "");
				rates_item.put("total_price", 0);
				rates_item.put("description", "");
				rates_item.put("currency", "");
				rates_item.put("min_delivery_date", "");
				rates_item.put("max_delivery_date", "");
				ratesAry.put(rates_item);
			}
			rates.put("rates", ratesAry);
			//System.out.println("######apiCarrierServiceCreate###rates==>"+rates);
			//String jsonString = rates.writeValueAsString(rates);
			
			//mav.addObject("rates", rates);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rates.toString();
	}
	
    
}
	