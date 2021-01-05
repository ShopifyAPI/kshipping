package com.shopify.order;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shopify.shipment.popup.ShipmentPopupData;

import lombok.Data;


@JsonIgnoreProperties(ignoreUnknown = true)		//JSON 스트링에는 resultCode, resultMessage name이 존재하지만 ConstraintData 클래스에는 해당 멤버 변수가 존재하지 않아도 처리
@Data
public class OrderDetailSkuData {
	private int orderIdx;
	private int shopIdx;
	private int skuIdx;
	private String masterCode;
	private String orderCode;
	private String regDate;
	private String goodsCode;
	private String goods;
	private String goodsType;
	private String goodsSku;
	private String unitCost;
	private String price;
	private String taxable;
	private String barcode;
	private String origin;
	private String hscode;
	private int selectBox;
	private String boxLength;
	private String boxWidth;
	private String boxHeight;
	private String boxWeight;
	private String boxUnit;
	private String weight;
	private String weightUnit;	
	private String itemQty;
	private String customerId;
	private String customerName;
	private String combineOrderIdx;
	private int quantity;
	private String priceCurrency;
	private String goodsItemId;
	private String orderStatusUrl;
	private String repreItemNm;
	private String repreItemNmRu;
	private String vendor;
	private String itemLink;
	private String brand;
	
	private String[] arrShopIdx;
	private String[] arrOrderCode;
	
//	private List<OrderCodeData> orderCodeData;
//	
//	@Data
//    public class OrderCodeData {
//        private String orderCode;
//        private int shipIdx;
//    }
}

