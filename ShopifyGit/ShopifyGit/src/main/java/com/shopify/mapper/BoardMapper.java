package com.shopify.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.shopify.admin.board.AdminBoardData;
import com.shopify.board.BoardData;

/**
* Board Mapper 
* @author : kyh
* @since  : 2020-01-09
* @desc   : /board 정보 
*/
@Mapper
public interface BoardMapper {
	
	/* 셀러Q&A 조회*/
	public List<BoardData> selectQna(BoardData boardData );
	/* 셀러Q&A 상세조회*/
	public BoardData showQna(BoardData boardData );
	/* 셀러Q&A 등록*/
	public int insertQna(BoardData boardData);
	
	/* 셀러 FAQ 조회 */
	public List<BoardData> selectFaq(BoardData boardData);
	
    /* 게시물 삭제  */
    public void deleteBoard(Map<String, Object> params);
	
    public int selectAllQnaCount(BoardData admin);
	public List<BoardData> selectAllQna(BoardData admin);
	
	public int selectAllFaqCount(BoardData admin);
	public List<BoardData> selectAllFaq(BoardData admin);
    
	
	
	
	
	
}




