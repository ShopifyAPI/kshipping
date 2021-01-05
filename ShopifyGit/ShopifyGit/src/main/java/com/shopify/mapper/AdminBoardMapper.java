package com.shopify.mapper;

import java.util.List;

import com.shopify.admin.board.AdminBoardData;

public interface AdminBoardMapper {
			
	
	/* 고객센터 Q&A 조회  */
	public List<AdminBoardData> selectQna(AdminBoardData boardData);
	/* 고객센터 Q&A 상세 조회  */
	public AdminBoardData selectAdmQnaAnswer(AdminBoardData boardData);
	/* 고객센터 Q&A 답글 작성 */
	public int updateBoardAnswer(AdminBoardData boardData);
	/* 고객센터 FAQ 조회 */
	public List<AdminBoardData> selectFaq(AdminBoardData boardData);
	/* 고객센터 FAQ 상세 조회 */
	public AdminBoardData selectFaqShow(AdminBoardData boardData);
	/* 고객센터 FAQ 등록 */
	public int insertBoard(AdminBoardData boardData);
	/* 고객센터 FAQ 수정 */
    public int updateFaq(AdminBoardData boardData);
    /* 고객센터 게시물 삭제 */
    public void deleteBoard(String idx);
    
    public int selectAllQnaCount(AdminBoardData boardData);
	public List<AdminBoardData> selectAllQna(AdminBoardData admin);
	
	public int selectAllFaqCount(AdminBoardData boardData);
	public List<AdminBoardData> selectAllFaq(AdminBoardData admin);
	
	
	
	
}
