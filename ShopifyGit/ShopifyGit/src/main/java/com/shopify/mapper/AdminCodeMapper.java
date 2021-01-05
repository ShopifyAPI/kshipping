package com.shopify.mapper;

import java.util.List;
import java.util.Map;

import com.shopify.admin.code.AdminCodeData;
import com.shopify.admin.code.popup.AdminCodePopupData;

public interface AdminCodeMapper {
			
	
	/**
	 * 관리자 > 코드 관리
	 */
	public List<AdminCodeData> selectAdminCode (AdminCodeData codeData);
	public int selectAdminCodeCount(AdminCodeData codeData);

	public int chkAdminCodeGroup(AdminCodeData codeData);
	public int insertAdminCodeGroup(AdminCodeData codeData);
	
	
	public int chkAdminCodeId(AdminCodeData codeData);
	public int insertAdminCodeId(AdminCodeData codeData);
	
	
	public int updateAdminCode(AdminCodeData codeData);
	public void deleteAdminCode(String codeId);
	
	public AdminCodePopupData selectAdminCodeShow (AdminCodePopupData codePopupData);
	
	public List<Map<String,Object>> selectAdminCodeGroup();
	public String selectCodeRef(AdminCodeData codeData);
	
	
	
}
