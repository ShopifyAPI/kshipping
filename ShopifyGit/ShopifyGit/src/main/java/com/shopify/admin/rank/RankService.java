package com.shopify.admin.rank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopify.admin.AdminData;
import com.shopify.common.SpConstants;
import com.shopify.mapper.RankMapper;
import com.shopify.shop.ShopData;
@Service
@Transactional

/**
 * 할인율 서비스
 *
 */

public class RankService {
 
    @Autowired private RankMapper rankMapper;
    
    private Logger LOGGER = LoggerFactory.getLogger(RankService.class);
     
    /**
	 * rank > 할인율 조회
	 * @return
	 */
    public List<RankData> listRank(RankData rankData, HttpSession sess){
    	
//    	AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	return rankMapper.listRank(rankData);
    }
    
    /**
	 * rank > 할인율 상세조회
	 * @return
	 */
    public RankData showRank(RankData rankData, HttpSession sess){
        
//    	AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	return rankMapper.showRank(rankData);
    }
    
    /**
	 * rank > 할인율 수정
	 * @return
	 */
    public int updateRank(RankData rankData, HttpSession sess){
        
//    	AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	
		return rankMapper.updateRank(rankData);
    			
    }
    
    /**
	 * rank > 할인율 등록
	 * @return
	 */
    public int insertRank(Locale locale,RankData rankData ,HttpSession sess) {
		
//    	AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	//할인율 중복체크
		int chk = rankMapper.chkRank(rankData);
		
		if(chk > 0) {
			return -1;
		}else {
			return rankMapper.insertRank(rankData);
		}

    }
	
    /**
	 * rank > 할인율 삭제
	 * @return
	 */
	public int deleteRank(RankData rankData ,HttpSession sess) {
		
//		AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		int result = 0;
		
		String key = rankData.getCkBox();
		
		String[] array = key.split(",");

		ArrayList list = new ArrayList(Arrays.asList(array));
		
		Iterator rootItr = list.iterator();
		
		while (rootItr.hasNext()) {
    		String rankId = (String) rootItr.next();

    		rankMapper.deleteRank(rankId);
    		
    		result ++;
    		
    	}
		
		return result;
		
	}

}
