package tr.org.lkd.lyk2015.camp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import dto.ApplicationFormDto;
import tr.org.lkd.lyk2015.camp.service.CourseService;

@Controller
@RequestMapping("/basvuru")
public class ApplicationController {
	
	@Autowired
	CourseService courseService;

	@RequestMapping(value="", method = RequestMethod.GET)
	public String form(@ModelAttribute("form") ApplicationFormDto applicationFormDto, Model model) {
		
		model.addAttribute("courseList", courseService.getAll());
		
		return "applicationForm";
	}

}
