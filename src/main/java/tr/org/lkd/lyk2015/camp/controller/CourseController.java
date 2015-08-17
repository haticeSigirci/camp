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

import tr.org.lkd.lyk2015.camp.model.Admin;
import tr.org.lkd.lyk2015.camp.model.Course;
import tr.org.lkd.lyk2015.camp.service.CourseService;

@Controller
@RequestMapping("/course")
public class CourseController {

	@Autowired
	private CourseService courseService;
	
	@RequestMapping("")
	public String list(Model model) { 
		List<Course> courses = courseService.getAll();

		model.addAttribute("courseList", courses);
		
		return "courseList";
	}
	
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String createForm(@ModelAttribute Course course) {
		
		return "courseForm";
	}

	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public String create(@ModelAttribute Course course) {	
		
		courseService.create(course);

		return "redirect:/course"; // ?? recursive
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String edit(@ModelAttribute Course course, Model model, @PathVariable(value="id") Long id) {

		
		Course courseNew = courseService.getById(course.getId());

		model.addAttribute("course", courseNew);

		return "courseEdit";
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String update(@PathVariable(value = "id") Long id,@ModelAttribute Course course, Model model) {
	
		courseService.update(course);

//		model.addAttribute("message", "success");

		return "redirect:/course";
	}
	
}
