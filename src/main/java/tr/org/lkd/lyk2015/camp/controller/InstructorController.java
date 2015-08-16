package tr.org.lkd.lyk2015.camp.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import tr.org.lkd.lyk2015.camp.model.Instructor;

@Controller
@RequestMapping("/instructor")
public class InstructorController {

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String createForm(@ModelAttribute Instructor instructor) {

		return "InstructorForm";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(@ModelAttribute Instructor instructor) {
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
