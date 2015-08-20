package tr.org.lkd.lyk2015.camp.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import tr.org.lkd.lyk2015.camp.controller.valid.ApplicationFormValidator;
import tr.org.lkd.lyk2015.camp.dto.ApplicationFormDto;
import tr.org.lkd.lyk2015.camp.model.Application;
import tr.org.lkd.lyk2015.camp.service.ApplicationService;
import tr.org.lkd.lyk2015.camp.service.CourseService;

@Controller
@RequestMapping("/basvuru")
public class ApplicationController {

	@Autowired
	private CourseService courseService;

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private ApplicationFormValidator applicationFormValidator;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String form(@ModelAttribute("form") ApplicationFormDto applicationFormDto, Model model) {

		model.addAttribute("courses", this.courseService.getAll());

		return "applicationForm";
	}

	@InitBinder
	protected void InitBinder(final WebDataBinder binder) {
		binder.addValidators(this.applicationFormValidator);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public String create(@ModelAttribute("form") @Valid ApplicationFormDto applicationFormDto,
			BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {

			model.addAttribute("courses", this.courseService.getAll());
			return "applicationForm";
		}
		this.applicationService.create(applicationFormDto);

		return "applicationSuccess"; // applicationFormSuccess

		// model.addAttribute("courses", this.courseService.getAll());
		// getAllActive

	}

	@RequestMapping(value = "/validate/{id}", method = RequestMethod.GET)
	public String form(@PathVariable("id") String id, Model model) {

		if (this.applicationService.validate(id)) {
			model.addAttribute("message", "başvurunuz doğrulanmıştır.");
			return "validated";
		} else {
			model.addAttribute("message", "böyle bi basvuru bulunmamaktadır.");
			return "validated";
		}
	}

	@RequestMapping(value = "/apps", method = RequestMethod.GET)
	public String list(Model model) {
		List<Application> apps = this.applicationService.getAll();

		model.addAttribute("applicationList", apps);
		return "applications";
	}

	// @RequestMapping(value = "/apps/selected/{id}", method =
	// RequestMethod.GET)
	// public String selectedApps(@ModelAttribute Application application, Model
	// model, @PathVariable("id") Long id) {
	//
	// return "applications";
	// }

}
