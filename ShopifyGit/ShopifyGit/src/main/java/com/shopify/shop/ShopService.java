package com.shopify.shop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopify.common.SpConstants;
import com.shopify.mapper.ShopMapper;
 
@Service
@Transactional
public class ShopService {
 
    @Autowired private ShopMapper shopMapper;
    
    public int insertShop(ShopData shop){
		return shopMapper.insertShop(shop);
	}
    
    public int updateShop(ShopData shop){
        return shopMapper.updateShop(shop);
    }
    
    public int selectOneShopCount(ShopData shop){
		return shopMapper.selectOneShopCount(shop);
	}
    
    public ShopData selectOneShop(ShopData order){
		return shopMapper.selectOneShop(order);
	}
    
    public List<ShopData> selectShopOrderDomain(ShopData order){
		return shopMapper.selectShopOrderDomain(order);
	}
    
    public List<ShopData> selectAllShop(ShopData order){
		return shopMapper.selectAllShop(order);
	}
    
    // paging
    public Map<String, Object> shopList(int currentPage){
		ShopData shop = new ShopData();
		// 페이지에 보여줄 행의 개수 ROW_PER_PAGE = 10으로 고정
		final int ROW_PER_PAGE = 10; 
		
		// 페이지에 보여줄 첫번째 페이지 번호는 1로 초기화
		int startPageNum = 1;
		
		// 처음 보여줄 마지막 페이지 번호는 10
		int lastPageNum = ROW_PER_PAGE;
		
		// 현재 페이지가 ROW_PER_PAGE/2 보다 클 경우
		if(currentPage > (ROW_PER_PAGE/2)) {
			// 보여지는 페이지 첫번째 페이지 번호는 현재페이지 - ((마지막 페이지 번호/2) -1 )
			// ex 현재 페이지가 6이라면 첫번째 페이지번호는 2
			startPageNum = currentPage - ((lastPageNum/2)-1);
			// 보여지는 마지막 페이지 번호는 현재 페이지 번호 + 현재 페이지 번호 - 1 
			lastPageNum += (startPageNum-1);
		}
		
		// Map Data Type 객체 참조 변수 map 선언
		// HashMap() 생성자 메서드로 새로운 객체를 생성, 생성된 객체의 주소값을 객체 참조 변수에 할당
		Map<String, Object> map = new HashMap<String, Object>();
		// 한 페이지에 보여지는 첫번째 행은 (현재페이지 - 1) * 10
		int startRow = (currentPage - 1)*ROW_PER_PAGE;
		// 값을 map에 던져줌
		shop.setStartRow(startRow);
		shop.setRowPerPage(ROW_PER_PAGE);
		shop.setEncryptKey(SpConstants.ENCRYPT_KEY);
		
		// DB 행의 총 개수를 구하는 getBoardAllCount() 메서드를 호출하여 double Date Type의 boardCount 변수에 대입
		double shopCount = shopMapper.selectAllShopCount();
		
		// 마지막 페이지번호를 구하기 위해 총 개수 / 페이지당 보여지는 행의 개수 -> 올림 처리 -> lastPage 변수에 대입
		int lastPage = (int)(Math.ceil(shopCount/ROW_PER_PAGE));
		
		// 현재 페이지가 (마지막 페이지-4) 보다 같거나 클 경우
		if(currentPage >= (lastPage-4)) {
			// 마지막 페이지 번호는 lastPage
			lastPageNum = lastPage;
		}
		
		// 구성한 값들을 Map Date Type의 resultMap 객체 참조 변수에 던져주고 return
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", shopMapper.selectAllShop(shop));
		resultMap.put("currentPage", currentPage);
		resultMap.put("lastPage", lastPage);
		resultMap.put("startPageNum", startPageNum);
		resultMap.put("lastPageNum", lastPageNum);
		return resultMap;
	}
   
    
}
