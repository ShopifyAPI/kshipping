package com.shopify.admin.notice;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 관리자 컨트롤러
 *
 */

@Controller
@SpringBootApplication
@PropertySource("classpath:application.properties")
public class AdminNoticeController {
	private Logger LOGGER = LoggerFactory.getLogger(AdminNoticeController.class);

	@Autowired
	private AdminNoticeService adminNoticeService;
    @Autowired
    LocaleResolver localeResolver;

	@Value("${file.upload-dir}") String uploadDir;

	/**
	 * 관리자 NOTICE 조회
	 * @return
	 */
	@GetMapping("/admin/board/selectNotice")
	public String selectNotice(Model model, HttpSession sess, AdminNoticeData noticeData
			, @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage
			, @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType
			, @RequestParam(value = "searchWord", required = false, defaultValue = "") String searchWord
			, @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize
		) {
		noticeData.setCurrentPage(currentPage);
		noticeData.setSearchType(searchType);
		noticeData.setSearchWord(searchWord);
		noticeData.setPageSize(pageSize);
		Map<String, Object> map = adminNoticeService.selectNotice(noticeData, sess); 
		model.addAttribute("search", noticeData);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		return "admin/notice/admNoticeList";
	}
	
	/**
	 * NOTICE 상세조회
	 * @return
	 */
	@GetMapping("/admin/board/admEditNotice")
	public String selectNoticeShow(Model model, AdminNoticeData noticeData, HttpSession sess) throws Exception {
		AdminNoticeData adminNoticeData = adminNoticeService.selectNoticeShow(noticeData,sess);
		List<AdminNoticeData> adminNoticeFileData = adminNoticeService.selectNoticeShowFile(noticeData,sess);
		model.addAttribute("adminNoticeData", adminNoticeData);
		model.addAttribute("adminNoticeFileData", adminNoticeFileData);
		return "admin/notice/admNotice";
	}
	
	/**
	 * 화면 이동 (관리자 NOTICE 등록 화면)
	 * @return
	 */
	@GetMapping("/admin/board/admNewNotice")
	public ModelAndView admNewNotice(HttpServletRequest req, HttpSession sess) throws Exception {
	    ModelAndView mav = new ModelAndView("admin/notice/admNotice");
		return mav;
	}

	/**
	 * 관리자 NOTICE 등록
	 * @return
	 */
	@PostMapping("/admin/board/insertNotice")
	public ModelAndView insertNotice(Model model, @ModelAttribute AdminNoticeData noticeData, @RequestParam(value="file", required=false) List<MultipartFile> fileList, HttpSession sess) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");
		int result = 0;
		result = adminNoticeService.insertNotice(noticeData, fileList, sess);
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		return mv;
	}

	/**
	 * 관리자 NOTICE 수정
	 * @return
	 */
	@PostMapping("/admin/board/updateNotice")
	public ModelAndView updateNotice(Model model, @ModelAttribute AdminNoticeData noticeData, @RequestParam(value="file", required=false) List<MultipartFile> fileList, HttpSession sess) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");
		int result = adminNoticeService.updateNotice(noticeData, fileList, sess);
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		return mv;		
	}
	
	/**
	 * 관리자 NOTICE 첨부파일을 삭제합니다.
	 * @param idx
	 * @param request
	 * @return
	 */
	@PostMapping("/admin/board/deletefile")
	public ModelAndView deleteFile(Model model, @RequestBody Map<String, Object> params, HttpServletRequest request, HttpSession sess) {
		ModelAndView mv = new ModelAndView("jsonView");
		int result = 0 ;
		if (params != null && params.get("idx") != null) {
			String idx = String.valueOf(params.get("idx")) ;
			result = adminNoticeService.deleteFile(idx, sess) ;
		} 
		if (result > 0) {
			mv.addObject("result", result);
			mv.addObject("errCode", true);
			mv.addObject("errMsg", "성공");
		} else {
			mv.addObject("result", result);
			mv.addObject("errCode", true);
			mv.addObject("errMsg", "실패");
		}
		return mv;		
	}	
	
	/**
	 * 게시물 삭제
	 * @return
	 * 여러개의 row를 삭제처리 하기 위해 Map으로 처리하였음
	 */
	@PostMapping("/admin/board/deleteNotice")
	public ModelAndView deleteNotice(Model model, @RequestBody AdminNoticeData noticeData,HttpSession sess) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");
		int result = adminNoticeService.deleteNotice(noticeData,sess);
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		return mv;
	}
}