package com.shopify.payinfo;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.shopify.mapper.PayinfoMapper;

/**
* 결제 Service
* @desc   : /결제 정보 
*/

@Service
@Transactional
public class PayinfoService {
	
	@Autowired 
	private PayinfoMapper payinfoMapper;

	/**
     * 결재저장
     * @return
     */
	public int insertPayinfo(PayinfoData payinfoData) {
		int cnt = 0;
		cnt = payinfoMapper.insertPayinfo(payinfoData);
        
        return cnt;
    }
	
	
}
