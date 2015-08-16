package tr.org.lkd.lyk2015.camp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import tr.org.lkd.lyk2015.camp.model.Admin;
import tr.org.lkd.lyk2015.camp.model.Instructor;



@Controller
@RequestMapping("/admin")
public class AdminController {
	
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String createForm(@ModelAttribute Admin admin) {
		

		return "AdminForm";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(@ModelAttribute Admin admin) {
//
//		if(bindingResult.hasErrors()) {
//			
//			return "create";
//			
//		}
//		
		
//		todoService.create(todo);

		return "redirect:/todo"; // ?? recursive
	}
	

}
