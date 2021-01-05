package com.shopify.mapper;

import java.util.List;

import com.shopify.admin.notice.AdminNoticeData;


public interface AdminNoticeMapper {
			
	/* NOTICE 조회 */
	public List<AdminNoticeData> selectNotice(AdminNoticeData noticeData);
	/* NOTICE 상세 조회 */
	public AdminNoticeData selectNoticeShow(AdminNoticeData noticeData);
	public List<AdminNoticeData> selectNoticeShowFile(AdminNoticeData noticeData);
	/* NOTICE 등록 */
	public int insertNotice(AdminNoticeData noticeData);
	public int insertNoticeFile(AdminNoticeData noticeData);
	/* NOTICE 수정 */
    public int updateNotice(AdminNoticeData noticeData);
    /* NOTICE 게시물 삭제 */
    public void deleteNotice(String idx);
	public int selectAllNoticeCount(AdminNoticeData noticeData);
	public List<AdminNoticeData> selectAllNotice(AdminNoticeData admin);
	
	public String selectAttachedFileName(String stored_name) ;
	public int deleteFile(String idx) ;

}