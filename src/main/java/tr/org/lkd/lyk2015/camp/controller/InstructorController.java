package tr.org.lkd.lyk2015.camp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import tr.org.lkd.lyk2015.camp.model.Instructor;
import tr.org.lkd.lyk2015.camp.service.CourseService;
import tr.org.lkd.lyk2015.camp.service.InstructorService;


@Controller
@RequestMapping("/instructor")
public class InstructorController {
	
	@Autowired
	private InstructorService instructorService;
	
	@Autowired
	private CourseService courseService;
	
	@RequestMapping("")
	public String list(Model model) {
		List<Instructor> instructors = instructorService.getAll();

		model.addAttribute("instructorList", instructors);
		
		//
		
		
		return "instructorList";
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String createForm(@ModelAttribute Instructor instructor, Model model) {

		model.addAttribute("courses", courseService.getAll());
		
//		System.out.println(courseService.getAll());
		
		return "instructorForm";
	}
	
	//create post
	@RequestMapping(value = "", method = RequestMethod.POST)
	public String create(@ModelAttribute Instructor instructor, @RequestParam("courseIds") List<Long> ids, Model model) {

		//ids
		
		instructorService.create(instructor, ids);

		return "redirect:/instructor"; // ?? recursive
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET )
	public String edit(@ModelAttribute Instructor instructor, Model model, @PathVariable(value="id") Long id){

		
		model.addAttribute("courses", courseService.getAll());
		model.addAttribute("instructor", instructorService.getInstructorWithCourses(id));

		return "instructorEdit";
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String update(@PathVariable(value = "id") Long id, @ModelAttribute Instructor instructor, Model model) {
	
		instructorService.update(instructor);

		return "redirect:/instructor";  // aynı formu iki kere gondermesin 
	}
	
}
