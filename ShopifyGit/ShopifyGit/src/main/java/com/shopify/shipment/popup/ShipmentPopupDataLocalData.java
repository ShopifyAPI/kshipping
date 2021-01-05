package com.shopify.shipment.popup;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@JsonIgnoreProperties(ignoreUnknown = true)		//JSON 스트링에는 resultCode, resultMessage name이 존재하지만 ConstraintData 클래스에는 해당 멤버 변수가 존재하지 않아도 처리
@Data
public class ShipmentPopupDataLocalData {
        private String company;
        private String boxSize;
        private int price;
        private String[] mcode;
}