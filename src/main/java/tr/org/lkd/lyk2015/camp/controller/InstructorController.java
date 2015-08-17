package tr.org.lkd.lyk2015.camp.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import tr.org.lkd.lyk2015.camp.service.CourseService;
import tr.org.lkd.lyk2015.camp.service.InstructorService;
import tr.org.lkd.lyk2015.camp.model.Admin;
import tr.org.lkd.lyk2015.camp.model.Instructor;


@Controller
@RequestMapping("/instructor")
public class InstructorController {
	
	@Autowired
	private InstructorService instructorService;
	
	@RequestMapping("")
	public String list(Model model) {
		List<Instructor> instructors = instructorService.getAll();

		model.addAttribute("instructorList", instructors);
		
		return "instructorList";
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String createForm(@ModelAttribute Instructor instructor) {

		return "instructorForm";
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public String create(@ModelAttribute Instructor instructor) {

		instructorService.create(instructor);

		return "redirect:/instructor"; // ?? recursive
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET )
	public String edit(@ModelAttribute Instructor instructor, Model model, @PathVariable(value="id") Long id){

		
		Instructor instructorNew = instructorService.getById(id);

		model.addAttribute("instructor", instructorNew);

		return "instructorEdit";
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String update(@PathVariable(value = "id") Long id, @ModelAttribute Instructor instructor, Model model) {
	
		instructorService.update(instructor);

		return "redirect:/instructor";  // aynı formu iki kere gondermesin 
	}
	
}
