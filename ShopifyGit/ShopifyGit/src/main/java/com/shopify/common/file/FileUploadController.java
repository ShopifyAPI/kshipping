package com.shopify.common.file;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.shopify.admin.statis.AdminStatisData;
import com.shopify.admin.statis.AdminStatisService;
import com.shopify.common.util.UtilFn;
import com.shopify.mapper.AdminNoticeMapper;
 
@RestController
@PropertySource("classpath:application.properties")
public class FileUploadController {
	private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
	
	@Value("${file.upload-dir}")		String uploadDir;
	
	@Autowired private AdminNoticeMapper adminNoticeMapper;
	@Autowired private FileUploadDownloadService service;
	@Autowired private ExcelService excelService;
	@Autowired private AdminStatisService adminStatisService;
	@Autowired private UtilFn util; 
	
	// 부피무게 등록 
	@PostMapping("/admin/uploadVolumeWeightExcelFile")
	public ModelAndView uploadVolumeWeightExcelFile(Model model, @RequestParam("file") MultipartFile file) {
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		ModelAndView mv = new ModelAndView("jsonView");
		int result = 0;
		// fileName = getOriginalFilename
		String fileName = service.storeFile(file);
		

		String filePath = uploadDir + "/" +fileName;
//        String filePath = "C:\\zWorkSpace\\workspace\\fileUpload\\shopify\\" + fileName;
//        String ststempPath = (String)properties.get("file.upload-dir");

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
								.path("/downloadFile/")
								.path(fileName)
								.toUriString();
		List<Map<String, String>> excelContent = excelService.volumeWeightUpload(fileName, filePath);  // **********************************************************************************
		 
		AdminStatisData statis = new AdminStatisData();
		statis.setSearchPaymentCode("ND"); // 국내 픽업 + 해외배송 
		String searchDateStart     = statis.getSearchDateStart();
		 String searchDateEnd       = statis.getSearchDateEnd();
		if (true) {
				// 검색기간 정보가 비어있을 경우, 지난 일주일의 데이터를 출력한다.
				int nPrevDays = 30 ;
				String sDateFormat = "YYYY-MM-dd" ;
				// 종료일자 : 오늘
				if (searchDateEnd == null || "".equals(searchDateEnd)) {
					searchDateEnd = util.getToday(sDateFormat) ;
					statis.setSearchDateEnd(searchDateEnd) ;
				}
				// 시작일자 : 30일전
				if (searchDateStart == null || "".equals(searchDateStart)) {
					searchDateStart = util.getPrevDate(nPrevDays, sDateFormat) ; 
					statis.setSearchDateStart(searchDateStart) ;
				}
		}    
		Map<String, Object> map = adminStatisService.selectStatisSales(statis, null, false);
		List<AdminStatisData> list = (List)map.get("list");
		for(AdminStatisData data : list)
		{
			logger.debug(">>>>>masterCode:" + data.getMasterCode());
			logger.debug(">>>>>weight:" +data.getWeight());
			
 
		}
		model.addAttribute("mode", statis.getSearchPaymentCode());
		model.addAttribute("search", map.get("search"));
		model.addAttribute("sum", map.get("sum"));
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		
		mv.setViewName("/admin/statis/salesNew");
		
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
		
	}
	// 요금 매핑 등록 
	@PostMapping("/admin/uploadPriceExcelFile")
	public ModelAndView uploadPriceExcelFile(Model model, @RequestParam("file") MultipartFile file,
			@RequestParam("shipCompany") String shipCompany,
			@RequestParam("deliveryService") String deliveryService,
			@RequestParam("startDate") String startDate,
			@RequestParam("gbn") String gbn) {
		
		ModelAndView mv = new ModelAndView("jsonView");
//		String resource = "resources/application-test.properties";
//		Properties properties = new Properties();
		
//		try {
//			Reader reader = Resources.getResourceAsReader(resource);
//	        properties.load(reader);
//	        
//		}catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
		        
		/*
		 * 파일업로드 현재 2분정도 걸림 (1500row)
		 * insert를 Iterator에서 LIST자체를 쿼리로 넘기는 방식으로 처리하고
		 * 테이블 인덱스 추가하여 시간 단축시켜야함
		 * 운영반영하기 전에 INSERT LOGGER 삭제 해야함
		 * */
		int result = 0;
		// fileName = getOriginalFilename
		String fileName = service.storeFile(file);
		

		String filePath = uploadDir + "/" +fileName;
//        String filePath = "C:\\zWorkSpace\\workspace\\fileUpload\\shopify\\" + fileName;
//        String ststempPath = (String)properties.get("file.upload-dir");

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
								.path("/downloadFile/")
								.path(fileName)
								.toUriString();
		if(gbn.equals("fees")){
			result = excelService.feesPriceExcelUpload(fileName,filePath,shipCompany,deliveryService,startDate);
		}else {
			result = excelService.salePriceExcelUpload(fileName,filePath,shipCompany,deliveryService,startDate);
		}
		
		mv.setViewName("admin/price/adminPriceMappingList");
		
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
		
	}
	
	
	@PostMapping("/uploadFile")
	public FileUploadData uploadFile(@RequestParam("file") MultipartFile file) {
		
		logger.debug("파일업로드"+file);
		
		String fileName = service.storeFile(file);
		
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
								.path("/downloadFile/")
								.path(fileName)
								.toUriString();
		
		return new FileUploadData(fileName, fileDownloadUri, file.getContentType(), file.getSize());
	}
	
	@PostMapping("/uploadMultipleFiles")
	public List<FileUploadData> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files){
		return Arrays.asList(files)
				.stream()
				.map(file -> uploadFile(file))
				.collect(Collectors.toList());
	}
	
	@GetMapping("/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request){
		 // Load file as Resource
		Resource resource = service.loadFileAsResource(fileName);
 
		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.info("Could not determine file type.");
		}
 
		// Fallback to the default content type if type could not be determined
		if(contentType == null) {
			contentType = "application/octet-stream";
		}
 
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
	
	/**
	 * 공지사항의 첨부파일을 원본이름으로 다운로드 받습니다.
	 * @param fileName
	 * @param request
	 * @return
	 */
	@GetMapping("/downloadAttachedFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadAttachedFile(@PathVariable String fileName, HttpServletRequest request){
		Resource resource = service.loadFileAsResource(fileName);
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.info("Could not determine file type.");
		}
		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		String downloadName = adminNoticeMapper.selectAttachedFileName(fileName) ;
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloadName + "\"")
				.body(resource);
	}
}