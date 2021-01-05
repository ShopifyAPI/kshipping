package com.shopify.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.LocaleResolver;

import com.shopify.admin.board.AdminBoardData;
import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.mapper.AdminMapper;
import com.shopify.shop.ShopData;

/**
 * Admin 서비스
 *
 */

@Service
@Transactional
public class AdminService {
	
	
	private Logger LOGGER = LoggerFactory.getLogger(AdminService.class);
	
	@Autowired private AdminMapper adminMapper;
    @Autowired private MessageSource messageSource;
    @Autowired LocaleResolver localeResolver;
    @Autowired private UtilFn util;
	public int insertAdmin(AdminData admin){
		return adminMapper.insertAdmin(admin);
	}
	
	public int updateAdmin(AdminData admin){
		return adminMapper.updateAdmin(admin);
	}

	public int selectAdminCount(AdminData admin){
		return adminMapper.selectAdminCount(admin);
	}
	
	public int selectAdminPasswdCount(AdminData admin){
		return adminMapper.selectAdminPasswdCount(admin);
	}
	
	public AdminData selectAdmin(AdminData admin){
		return adminMapper.selectAdmin(admin);
	}
	
	public AdminData selectAdminPasswd(AdminData admin){
		return adminMapper.selectAdminPasswd(admin);
	}
	
	/**
	 * 운영관리 > 관리자 목록 조회
	 * @return
	 */
	public Map<String, Object> selectAdminList(HttpSession sess , AdminData adminData) {
		
		    AdminData ad = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);

		    adminData.setLocale(ad.getLocale());
		    String searchWord = adminData.getSearchWord();
		    
		    //검색조건 이름일 경우 암호화 처리
		    adminData.setSearchWordAese(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, searchWord));
		    
		    int dataCount = adminMapper.selectAllAdminCount(adminData);
			int currentPage = adminData.getCurrentPage(); //현제 페이지
			int pageSize = adminData.getPageSize(); // 페이지 당 데이터 수
			int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수
		    
			Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
			
			List<AdminData> adminList = new ArrayList<AdminData>(); //데이터 리스트
			
			List<AdminData> returnList = new ArrayList<AdminData>();//복호화처리한 데이터 리스트
			
			if (dataCount > 0) {
				
				paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
				adminData.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
				adminData.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
				adminList = adminMapper.selectAllAdmin(adminData);//데이터 리스트
				
				for(AdminData item : adminList){
					String adminName = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, item.getAdminName());
					String adminPhoneNumber = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, item.getAdminPhoneNumber());
					item.setAdminName(adminName);
					item.setAdminPhoneNumber(adminPhoneNumber);
					returnList.add(item);
				}
				
			}
			
			// 리턴 변수 선언
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list", returnList);
			map.put("paging", paging);
			
			return map;
	}
	
	/**
	 * 운영관리 > 관리자 상세조회
	 * @return
	 */
	public List<AdminData> adminShow(AdminData admin, HttpSession sess) {
		
		AdminData ad = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);

		return adminMapper.adminShow(admin);
	}
	
	/**
	 * 운영관리 > 관리자 삭제
	 * @return
	 */
	public int deleteAdminList(AdminData adminData , HttpSession sess){
		
		AdminData ad = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);

		int result = 0;
		
		String key = adminData.getCkBox();
		
		String[] array = key.split(",");

		ArrayList list = new ArrayList(Arrays.asList(array));
		
		Iterator rootItr = list.iterator();
		
		while (rootItr.hasNext()) {
    		
			String id = (String) rootItr.next();

    		adminMapper.deleteAdminList(id);
    		
    		result++;
    		
    	}
		
		return result;
		
	}
	/**
	 * 운영관리 > 관리자 수정
	 * @return
	 */
    public int updateAdminList(AdminData admin , HttpSession sess){

    	AdminData ad = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	
    	String adminPhoneNumber = util.getAESEncrypt(SpConstants.ENCRYPT_KEY, admin.getAdminPhoneNumber());
    	
    	//암호화 처리
    	admin.setAdminPhoneNumber(adminPhoneNumber);
   
    	String adminPasswd = admin.getAdminPasswd();
    	if(adminPasswd != null && !"".equals(adminPasswd)) {
    		admin.setAdminPasswd(util.getBCryptDecrypt(adminPasswd));
    	}
    	
    	int result = adminMapper.updateAdminList(admin);
    	
		return result;
	}
	
    /**
	 * 운영관리 > 관리자 상세조회
	 * @return
	 */
	public AdminData selectAdminShow(AdminData adminData, HttpSession sess) {

		AdminData ad = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		
		AdminData adminList = adminMapper.selectAdminShow(adminData);
		String adminName = "";
		String adminPhoneNumber = "";
		
		try {
			adminName = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, adminList.getAdminName());
		} catch (Exception e) {
			e.getStackTrace();
		}
		
		try {
			adminPhoneNumber = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, adminList.getAdminPhoneNumber());
		} catch (Exception e) {
			e.getStackTrace();
		}
		
		adminList.setAdminName(adminName);
		adminList.setAdminPhoneNumber(adminPhoneNumber);
		
		return adminMapper.selectAdminShow(adminData);
	}
	
	/**
	 * 관리자 로그인
	 * @return
	 */
	public AdminData selectAdminLogin(AdminData admin){
		
		return adminMapper.selectAdminLogin(admin);
	}
	
	/**
	 * 관리자 권한 설정
	 * @return
	 */
	public List<AdminScopeData> selectAdminScope(AdminScopeData admin){
		
		return adminMapper.selectAdminScope(admin);
	}
	
	/**
	 * 운영관리 > 관리자 등록
	 * @return
	 */
    public int insertAdminListPop(AdminData admin , HttpSession sess){
    	
    	AdminData ad = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	
    	
    	String adminPasswd = util.getBCryptDecrypt(admin.getAdminPasswd());
    	String adminName = util.getAESEncrypt(SpConstants.ENCRYPT_KEY, admin.getAdminName());
    	String adminPhoneNumber = util.getAESEncrypt(SpConstants.ENCRYPT_KEY, admin.getAdminPhoneNumber());
    	
    	//암호화 처리
    	admin.setAdminPasswd(adminPasswd);
    	admin.setAdminName(adminName);
    	admin.setAdminPhoneNumber(adminPhoneNumber);
    	
    	//id(email)중복확인
    	int chk = adminMapper.chkAdmin(admin);
    	
    	if(chk > 0) {
    		return -1;
    	}else {
    		return adminMapper.insertAdminListPop(admin);
    	}
		
	}
    
    /**
	 * 운영관리 > 관리자 등록
	 * @return
	 */
    public int editAdminPassword(AdminData admin, HttpSession sess){
    	
    	AdminData ad = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	
    	String passwd = util.getBCryptDecrypt(admin.getPasswd());
    	admin.setPasswd(passwd);
    	
    	return adminMapper.updatePassword(admin);
    }
}