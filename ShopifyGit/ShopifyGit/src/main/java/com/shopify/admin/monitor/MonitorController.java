package com.shopify.admin.monitor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@RestController
public class MonitorController{
    private Logger LOGGER = LoggerFactory.getLogger(MonitorController.class);
    
	@Autowired
	private MonitorService monitorService;
    
    @GetMapping(value="/admin/admin/lastExceptionLine", produces=MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView lastExceptionLine(Model model, 
    									@RequestParam(value = "dateLine", required = false) String dateLine,
    									@RequestParam(value = "id", required = false) String id) {
    	
    	ModelAndView mav = new ModelAndView("jsonView");

    	List<ExceptionLine> list = monitorService.lastExceptionLine(dateLine, id);
        
        model.addAttribute("list", list);
        return mav;
    }
    
    
}