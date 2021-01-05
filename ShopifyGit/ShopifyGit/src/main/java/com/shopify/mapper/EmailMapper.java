package com.shopify.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.shopify.common.EmailData;


/**
* EmailMapper
* @author : handoocho
* @since  : 2020-08-13
* @desc   : /이메일 
*/

@Mapper
public interface EmailMapper {
	public int insertEmail(EmailData email); // 보낼 이메일 입력 
	public List<EmailData> selectEmailList(); 
	public int updateEmailList(EmailData email);
}
