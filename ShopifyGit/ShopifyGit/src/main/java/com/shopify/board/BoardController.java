package com.shopify.board;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shopify.common.SpConstants;
import com.shopify.mapper.BoardMapper;
import com.shopify.shop.ShopData;

@Controller
@SpringBootApplication

/**
 * 게시판 컨트롤러
 *
 */
public class BoardController {
	
	@Autowired
	private BoardMapper boardMapper;
	@Autowired
	private BoardService boardService;
	
	private Logger LOGGER = LoggerFactory.getLogger(BoardController.class);
	
	/**
	 * 셀러 Q&A 조회
	 * @return
	 */
	@GetMapping("/board/selectQna")
	public String selectQna(Model model, HttpSession sess,BoardData boardData
			, @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage
			, @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType
			, @RequestParam(value = "searchWord", required = false, defaultValue = "") String searchWord
			, @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize
		) {
		
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		
		boardData.setCurrentPage(currentPage);
		boardData.setSearchType(searchType);
		boardData.setSearchWord(searchWord);
		boardData.setPageSize(pageSize);
		
		Map<String, Object> map = boardService.selectQna(boardData, sess);
		
		model.addAttribute("search", boardData);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		
		return "board/qnaList";
	}
	
	/**
	 * Q&A 상세 조회(셀러 답변확인)
	 * @return
	 */
	@GetMapping("/board/showQna")
	public String showQna(Model model, BoardData boardData, HttpSession sess) throws Exception {
		
	    BoardData data  = boardService.showQna(boardData,sess);
		
		model.addAttribute("boardData", data);
		
		return "board/qnaShow";
	}
	
	/**
	 * Q&A 등록
	 * @return
	 */
	@PostMapping("/board/insertQna")
	public ModelAndView insertQna(@RequestBody BoardData boardData, HttpSession sess) throws Exception {
		    	
		ModelAndView mv = new ModelAndView("jsonView");
		
		int result = boardService.insertQna(boardData,sess);
		
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
	}
		
	/**
	 * 셀러 FAQ 조회
	 * @return
	 */
	@GetMapping("/board/selectFaq")
	public String selectFaq(Model model,HttpSession sess,  BoardData boardData
			, @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage
			, @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType
			, @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize
		) {
		
		boardData.setCurrentPage(currentPage);
		boardData.setSearchType(searchType);
		boardData.setPageSize(pageSize);
		
		Map<String, Object> map = boardService.selectFaq(boardData, sess);
		
		model.addAttribute("search", boardData);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		
		return "board/faqList";
		
	}
	
	/**
	 * 셀러 FAQ 조회
	 * @return
	 */
	@GetMapping("/board/faq")
	public String selectOpenFaq(Model model,HttpSession sess,  BoardData boardData
			, @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage
			, @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType
			, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize
		) {
		
		boardData.setCurrentPage(currentPage);
		boardData.setSearchType(searchType);
		boardData.setPageSize(pageSize);
		
		Map<String, Object> map = boardService.selectFaq(boardData, sess);
		
		model.addAttribute("search", boardData);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		
		return "board/faqOpenList";
		
	}

	/**
	 * 게시물 삭제
	 * @return
	 * 여러개의 row를 삭제처리 하기 위해 Map으로 처리하였음
	 */
	@PostMapping("/board/deleteBoard")
	public ModelAndView deleteBoard(Model model, @RequestBody Map<String, Object> params,HttpSession sess) throws Exception {
		    	
		ModelAndView mv = new ModelAndView("jsonView");
		
		int result = boardService.deleteBoard(params,sess);
		
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
	}
	
	/**
	 * 화면 이동(셀러 Q&A 신규등록)
	 * @return
	 */
	@GetMapping("/board/newQna")
	public String newQna(HttpServletRequest req, HttpSession sess) throws Exception {
		
		return "board/qnaNew";

	}
	
	@GetMapping("/board/newTestBoard")
	public String newTestBoard(HttpServletRequest req, HttpSession sess) throws Exception {
		
		
		
		return "board/newTestBoard";

	}
	
}
