package com.shopify.admin.notice;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.shopify.admin.AdminData;
import com.shopify.common.SpConstants;
import com.shopify.common.file.FileUploadDownloadService;
import com.shopify.common.util.UtilFn;
import com.shopify.mapper.AdminNoticeMapper;

/**
 * Admin Notice 서비스
 *
 */

@Service
@Transactional
public class AdminNoticeService {
	private Logger LOGGER = LoggerFactory.getLogger(AdminNoticeService.class);
	@Autowired private AdminNoticeMapper adminNoticeMapper;
    @Autowired private UtilFn util;
    @Autowired private FileUploadDownloadService fileService;

    /**
	 * 관리자 NOTICE 조회
	 * @return
	 */
    public Map<String, Object> selectNotice(AdminNoticeData noticeData, HttpSession sess){
    	AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	noticeData.setDivision("D010003");
    	noticeData.setLocale(adminData.getLocale());
		int dataCount = adminNoticeMapper.selectAllNoticeCount(noticeData);
		int currentPage = noticeData.getCurrentPage(); //현제 페이지
		int pageSize = noticeData.getPageSize(); // 페이지 당 데이터 수
		int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수
		Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
		List<AdminNoticeData> returnList = new ArrayList<AdminNoticeData>();//복호화처리한 데이터 리스트
		// 데이터가 0보다 크면 페이징 및 데이터 받아 오기
		if (dataCount > 0) {
			paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
			noticeData.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
			noticeData.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
			returnList = adminNoticeMapper.selectAllNotice(noticeData);//데이터 리스트
		}
		// 리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", returnList);
		map.put("paging", paging);
		return map;
    }
    
    /**
	 * NOTICE 등록
	 * @return
	 */
    public int insertNotice(AdminNoticeData noticeData, List<MultipartFile> fileList, HttpSession sess) {
    	// AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	// String eMail = adminData.getEmail();
    	AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY) ;
    	String eMail = sd.getEmail() ;
		noticeData.setReg_admin(eMail) ;
		int result = 0 ;
		// tb_notice에 글을 저장하고 idx 값을 받아옵니다.
		if ((result = adminNoticeMapper.insertNotice(noticeData)) <= 0) {
			LOGGER.error("adminNoticeMapper.insertNotice() - fail to insert") ;
		} else if (noticeData.getIdx() <= 0) {
			LOGGER.error("adminNoticeMapper.insertNotice() - fail to get inserted idx") ;
		} else {
			noticeData.setNoti_idx(noticeData.getIdx()) ;
			// 업로드된 파일 목록이 존재한느 경우 각 파일을 서버에 저장하고 해당 정보를 기록합니다.
			if (fileList != null && fileList.size() > 0) {
				for (MultipartFile file : fileList) {
	 				String realName = fileService.storeFile(file, noticeData.getIdx()) ; // 저장된 파일명
	 				String fileName = StringUtils.cleanPath(file.getOriginalFilename()) ; // 원본 파일명
					String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadAttachedFile/").path(realName).toUriString() ;
					noticeData.setRealName(realName) ;
					noticeData.setFileName(fileName) ;
					noticeData.setFileDownloadUri(fileDownloadUri) ;
					noticeData.setFileType(file.getContentType()) ;
					noticeData.setSize(file.getSize()) ;
					adminNoticeMapper.insertNoticeFile(noticeData) ;
				}
			}
		}
		return result ;
    }
    
    /**
	 * NOTICE 수정
	 * @return
	 */
	public int updateNotice(AdminNoticeData noticeData, List<MultipartFile> fileList, HttpSession sess) {
    	// AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	// String eMail = adminData.getEmail();
    	AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY) ;
    	String eMail = sd.getEmail() ;
		noticeData.setReg_admin(eMail) ;
		int result = 0 ;
		// tb_notice에 글을 저장하고 idx 값을 받아옵니다.
		if ((result = adminNoticeMapper.updateNotice(noticeData)) <= 0) {
			LOGGER.error("adminNoticeMapper.insertNotice() - fail to insert") ;
		} else if (noticeData.getIdx() <= 0) {
			LOGGER.error("adminNoticeMapper.insertNotice() - fail to get inserted idx") ;
		} else {
			noticeData.setNoti_idx(noticeData.getIdx()) ;
			// 업로드된 파일 목록이 존재한느 경우 각 파일을 서버에 저장하고 해당 정보를 기록합니다.
			if (fileList != null && fileList.size() > 0) {
				for (MultipartFile file : fileList) {
	 				String realName = fileService.storeFile(file, noticeData.getIdx()) ; // 저장된 파일명
	 				String fileName = StringUtils.cleanPath(file.getOriginalFilename()) ; // 원본 파일명
					String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadAttachedFile/").path(realName).toUriString() ;
					noticeData.setRealName(realName) ;
					noticeData.setFileName(fileName) ;
					noticeData.setFileDownloadUri(fileDownloadUri) ;
					noticeData.setFileType(file.getContentType()) ;
					noticeData.setSize(file.getSize()) ;
					adminNoticeMapper.insertNoticeFile(noticeData) ;
				}
			}
		}
		return result ;
	}
	
	public int deleteFile(String idx, HttpSession sess) {
		int result = 0 ;
		result = adminNoticeMapper.deleteFile(idx) ;
		return result ;
	}
	

	/**
	 * 관리자 NOTICE 상세조회
	 * @return
	 */
    public AdminNoticeData selectNoticeShow(AdminNoticeData noticeData, HttpSession sess){
    	AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	noticeData.setLocale(adminData.getLocale());
    	AdminNoticeData data = adminNoticeMapper.selectNoticeShow(noticeData);
        return data;
    }
		
	/**
	 * 관리자 NOTICE 상세조회
	 * @return
	 */
    public List<AdminNoticeData> selectNoticeShowFile(AdminNoticeData noticeData, HttpSession sess){
    	AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	noticeData.setLocale(adminData.getLocale());
    	List<AdminNoticeData> data = adminNoticeMapper.selectNoticeShowFile(noticeData);
        return data;
    }
		
	/**
	 * 관리자 게시물 삭제(Q&A , FAQ)
	 * @return
	 * 여러개의 row를 삭제처리 하기 위해 Map으로 처리하였음
	 */
	public int deleteNotice(AdminNoticeData noticeData, HttpSession sess) {
		// AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		int result = 0;
		String key = noticeData.getCkBox();
		String[] array = key.split(",");
		ArrayList list = new ArrayList(Arrays.asList(array));
		Iterator rootItr = list.iterator();
		while (rootItr.hasNext()) {
    		String idx = (String) rootItr.next();
    		adminNoticeMapper.deleteNotice(idx);
    		result ++;
    	}
		return result;
	}
}