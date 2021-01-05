package com.shopify.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.shopify.main.MainData;

/**
* DashBoard Mapper 
* @author : jwh
* @since  : 2020-02-17
* @desc   : DashBoard 정보 
*/
@Mapper
public interface MainMapper {
	/**
	 * DashBoard 통계 정보
	 */
	public MainData selectOrderTotal(MainData main);
	
	
	/* YR 추가*/
	/**
	 * K-Shipping 공지사항 조회
	 */
	public List<MainData> selectNotice(MainData mainData );
	
	/* 셀러 NOTICE 상세조회*/
	public MainData showNotice(MainData mainData);
	public List<MainData> showNoticeFile(MainData mainData);

    public int selectAllNoticeCount(MainData admin);
    public int NoticeFlagCount(MainData admin);
	public List<MainData> selectAllNotice(MainData admin);
/*	
	public int selectAllFaqCount(BoardData admin);
	public List<BoardData> selectAllFaq(BoardData admin);*/
	/* YR 추가*/
}
