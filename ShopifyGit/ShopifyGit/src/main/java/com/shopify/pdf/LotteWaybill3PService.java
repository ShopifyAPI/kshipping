package com.shopify.pdf;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.shopify.api.lotte.delivery.LotteAddressResultData;
import com.shopify.api.lotte.delivery.LotteDeliveryData;
import com.shopify.common.RestService;
import com.shopify.common.util.UtilFunc;

@Service
@Transactional
public class LotteWaybill3PService implements ResourceLoaderAware
{
	private static final int FONT_MAX = 25;
	private static final int FONT_BIG = 14;
	private static final int FONT_MEDIUM = 12;
	private static final int FONT_SMALL = 9;
	
	private static final int PART_GAP = 279;
	
	private static final int GDS_SIZE = 4;
	private static final int GDS_LENGTH = 20;
	
	private ResourceLoader resourceLoader;
	
	@Autowired private RestService restService;
	
	
	public ByteArrayInputStream generate(String[] codeAry, 
										Map<String, LotteAddressResultData> addressMap,
										Map<String, LotteDeliveryData> deliveryMap) {

		
		 ByteArrayOutputStream out = new ByteArrayOutputStream();

		try ( PDDocument document = new PDDocument(); ) { 
			
			PDPage page = null;
			
			for( int i = 0; i < codeAry.length; i++ ) {
				String masterCode = codeAry[i];
				LotteAddressResultData address = addressMap.get(masterCode);
				if ( address == null ) {
					address = new LotteAddressResultData();
					UtilFunc.initializeBean(address);
				}
				LotteDeliveryData delivery = deliveryMap.get(masterCode);
				if ( delivery == null ) {
					delivery = new LotteDeliveryData();
					UtilFunc.initializeBean(delivery);
				}
				
				int mod = i % 3;
				if ( mod == 0 ) {
					page = new PDPage(PDRectangle.A4);
					document.addPage(page);
				}
				
				PDPageContentStream contentStream = new PDPageContentStream(document, page, AppendMode.APPEND, true, true);
				printLotteWaybill(document, page, contentStream, mod, address, delivery);
				contentStream.close();
			}
			
			document.save(out);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new ByteArrayInputStream(out.toByteArray());
	}
   
	/**
	 * @param addressList
	 * @param orderList
	 * @param pdfFile
	 * @return
	 */
	public boolean saveDocument(List<LotteAddressResultData> addressList, List<LotteDeliveryData> orderList, String pdfFile) {
		
		boolean result = false;
		
		try ( PDDocument document = new PDDocument(); ) {
			
			PDPage page = null;
			for( int i = 0; i < addressList.size(); i++ ) {
				
				LotteAddressResultData address = addressList.get(i);
				LotteDeliveryData order = orderList.get(i);
				
				int mod = i % 3;
				if ( mod == 0 ) {
					page = new PDPage(PDRectangle.A4);
					document.addPage(page);
				}
				
				PDPageContentStream contentStream = new PDPageContentStream(document, page, AppendMode.APPEND, true, true);
				printLotteWaybill(document, page, contentStream, mod, address, order);
				contentStream.close();
			}
			
			document.save(pdfFile);
			
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;		
	}
	

	/**
	 * @param template
	 * @param page
	 * @param contentStream
	 * @param mod
	 * @param address
	 * @param order
	 */
	private void printLotteWaybill(PDDocument template, PDPage page, PDPageContentStream contentStream, int mod, LotteAddressResultData address, LotteDeliveryData order) {
		
		int gap = PART_GAP * mod;
		
		try ( InputStream bold = getFontStream("bold");
				InputStream extraBold = getFontStream("extraBold");
				){
			PDType0Font extraFont = PDType0Font.load(template, extraBold);
			PDType0Font boldFont = PDType0Font.load(template, bold);
				
			contentStream.beginText();
			
			printLeft(mod, address, order, contentStream, extraFont, boldFont);
			Waybill.printBarcode39(template, page, address.getFiltCd(), 170, 780-gap, 86, 30);
			
			printRight(order, contentStream, extraFont);
			
			Waybill.printBarcode25(template, page, order.getInvNo(),  30, 570-gap, 110,35);
			Waybill.printBarcode25(template, page, order.getInvNo(),  420, 605-gap, 150, 50);
			
			contentStream.endText();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
	
	/**
	 * @param font
	 * @return
	 */
	public InputStream getFontStream(String font) {
		String name = null;
		
		switch ( font ) {
		case "gothic" : name = "NanumGothic.ttf"; break;
		case "bold" : name = "NanumGothicBold.ttf"; break;
		case "extraBold" : name = "NanumGothicExtraBold.ttf"; break;
		case "light" : name = "NanumGothicLight.ttf"; break;
		default : name = "NanumGothic.ttf"; break;
		}
		
		InputStream inputStream = null;
		try {
			inputStream =  getResource("classpath:templates/" + name).getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		 return inputStream;
	}


	

	/**
	 * @param order
	 * @param contentStream
	 * @param extraFont
	 */
	private void printRight(LotteDeliveryData order, PDPageContentStream contentStream, PDType0Font extraFont) {
		
		try {
			
			// -------------------------------------------------------------------------------------------
			// BLUE 1
			// -------------------------------------------------------------------------------------------
			// 보낸 분
			contentStream.setFont(extraFont, FONT_SMALL);
			contentStream.newLineAtOffset(145, 205);
			contentStream.showText(order.getSnperNm());
			

			// -------------------------------------------------------------------------------------------
			// RED
			// -------------------------------------------------------------------------------------------
			// 받는 고객 - 이름
			contentStream.setFont(extraFont, FONT_MEDIUM);
			contentStream.newLineAtOffset(15, -24);
			contentStream.showText(order.getAcperNm());
			
			// 받는 분 (blue) - 전화번호, 휴대전화번호
//			contentStream.setFont(extraFont, FONT_MEDIUM);
//			contentStream.newLineAtOffset(0, -12);
//			contentStream.showText(order.getAcperTel() + "   " + order.getAcperCpno());
			
			// 받는 분 (blue) - 주소
			contentStream.setFont(extraFont, FONT_MEDIUM);
			contentStream.newLineAtOffset(-50, -16);
			int dy = Waybill.wrapText(order.getAcperAdr(), contentStream, extraFont, FONT_MEDIUM, 318, 2);
			
			// ---------------------------------------------------
			// 보내는 분(blue) - 이름
			int next = Waybill.computeNext(28, dy);
			String sender = order.getSnperNm() + " / " + order.getSnperTel();
			contentStream.setFont(extraFont, FONT_SMALL);
			contentStream.newLineAtOffset(46, next);
			contentStream.showText(sender);
			
			// 보내는 분 (blue) - 주소
			contentStream.setFont(extraFont, FONT_SMALL);
			contentStream.newLineAtOffset(-46, -14);
			dy = Waybill.wrapText(order.getSnperAdr(), contentStream, extraFont, FONT_SMALL, 318, 2);
			
			
			// 주문번호
			next = Waybill.computeNext(32, dy);
			contentStream.newLineAtOffset(0, next);
			contentStream.showText( order.getOrderName() + ", " + order.getMasterCode() );
			
			// 날짜
			LocalDate lt = LocalDate.now(); 
			String date =  lt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
			contentStream.newLineAtOffset(238, 0);
			contentStream.showText( date.substring(2,4) );
			
			contentStream.newLineAtOffset(19, 0);
			contentStream.showText( date.substring(5,7) );
			
			contentStream.newLineAtOffset(21, 0);
			contentStream.showText( date.substring(8,10) );
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


	/**
	 * @param mod
	 * @param address
	 * @param order
	 * @param contentStream
	 * @param extraFont
	 * @param boldFont
	 */
	private void printLeft(int mod, LotteAddressResultData address, LotteDeliveryData order, PDPageContentStream contentStream, PDType0Font extraFont, PDType0Font boldFont) {
		
		int gap = PART_GAP * mod;
		
		try {
			// 터미널 번호
			contentStream.setFont(extraFont, FONT_MAX);
			contentStream.newLineAtOffset(65, 800 - gap);
			contentStream.showText(address.getTmlCd() + "-" + address.getTmlNm());
			
			// 도착지 주소
			contentStream.setFont(extraFont, FONT_BIG);
			contentStream.newLineAtOffset(-10, -20);
			contentStream.showText(address.getCityGunGu() + " " + address.getDong());
			
			
			// 운송장 번호
			contentStream.setFont(extraFont, FONT_MEDIUM);
			contentStream.newLineAtOffset(20, -32);
			contentStream.showText(order.getFormatedInvNo());
						
			// 받는 분 - 이름
//			String tel = StringUtils.isBlank(order.getAcperTel()) ?  order.getAcperCpno() :  order.getAcperTel();			
			contentStream.setFont(extraFont, FONT_BIG);
			contentStream.newLineAtOffset(-20, -27);
			contentStream.showText( order.getAcperNm());
			// 받는 분 - 전화번호 
			contentStream.setFont(extraFont, FONT_SMALL);
			contentStream.newLineAtOffset(-30, -13);
			contentStream.showText( order.getAcperTel() + "    " + order.getAcperCpno());
			// 받는 분 - 주소
			contentStream.newLineAtOffset(0, -15);
			int dy = Waybill.wrapText(order.getAcperAdr(), contentStream, extraFont, FONT_BIG, 230, 3);
//			System.out.println("dy1 = " + dy);
			
			
			List<String> list = new ArrayList<>();
			List<String> gdsNames = restService.readValue(order.getGdsNames(), new TypeReference<List<String>>() {});
			if ( gdsNames != null ) {
				for( int idx = 0; idx < gdsNames.size() && idx < GDS_SIZE; idx++ ) {
					String name = gdsNames.get(idx);
					if ( name.length() > GDS_LENGTH ) {
						name = name.substring(0, GDS_LENGTH);
					}
					list.add(name);
				}
				if ( gdsNames.size() > list.size() ) {
					int over = gdsNames.size() - list.size();
					String supple = list.get(GDS_SIZE - 1) + " 외(" + over + ")";
					list.set(GDS_SIZE - 1, supple);
				}
			}
			
			
			int next = Waybill.computeNext(55, dy);
			dy = Waybill.blockText(list, contentStream, extraFont, FONT_SMALL, 0, next); 
//			System.out.println("dy2 = " + dy);
			
			// 배달영업소
			next = Waybill.computeNext(57, dy);
			contentStream.setFont(extraFont, FONT_BIG);
			contentStream.newLineAtOffset(130, next);
			contentStream.showText(address.getBrnshpNm());
						
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public Resource getResource(String location){
		return resourceLoader.getResource(location);
	}



}