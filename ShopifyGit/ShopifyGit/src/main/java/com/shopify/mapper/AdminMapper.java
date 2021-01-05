package com.shopify.mapper;

import java.util.List;

import com.shopify.admin.AdminData;
import com.shopify.admin.AdminScopeData;

public interface AdminMapper {
	public int insertAdmin(AdminData admin);
	public int updateAdmin(AdminData admin);
	public int selectAdminCount (AdminData admin);
	public int selectAdminPasswdCount (AdminData admin);
	public AdminData selectAdmin (AdminData admin);
	public AdminData selectAdminPasswd (AdminData admin);
	
	/* 관리자 관리 목록조회 , 수정, 삭제 , 등록  */
	public List<AdminData> selectAdminList (AdminData admin);
	public List<AdminData> adminShow (AdminData admin);
	
	public int updateAdminList(AdminData admin);
	public int chkAdmin(AdminData admin);
	public int insertAdminListPop(AdminData admin);
	
	
	public int updatePassword(AdminData admin);
	
	
	public int deleteAdminList(String id);
	public AdminData selectAdminShow (AdminData adminData);
	
	public int selectAllAdminCount(AdminData admin);
	public List<AdminData> selectAllAdmin(AdminData admin);
	
	public AdminData selectAdminLogin(AdminData admin);
	public List<AdminScopeData> selectAdminScope(AdminScopeData scope);
}
