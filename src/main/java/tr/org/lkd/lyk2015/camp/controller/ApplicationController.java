package tr.org.lkd.lyk2015.camp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
import tr.org.lkd.lyk2015.camp.model.Student;
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
	public String form(Model model, Authentication authentication) {

		Student student = null;
		if (authentication != null && authentication.getPrincipal() instanceof Student) {

			student = (Student) authentication.getPrincipal();

		}

		if (student != null) {

			// user is updating his/her form
			ApplicationFormDto formDto = this.applicationService.createApplicationDto(student);
			model.addAttribute("form", formDto);
			model.addAttribute("update", true);

		} else {

			// new user creating a new application
			model.addAttribute("form", new ApplicationFormDto());
		}

		model.addAttribute("courses", this.courseService.getAllActive());

		return "applicationForm";
	}

	@InitBinder
	protected void InitBinder(final WebDataBinder binder) {
		binder.addValidators(this.applicationFormValidator);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public String create(@ModelAttribute("form") @Valid ApplicationFormDto applicationFormDto,
			BindingResult bindingResult, Model model, Authentication authentication) {

		if (bindingResult.hasErrors()) {

			model.addAttribute("courses", this.courseService.getAllActive());
			return "applicationForm";

		} else {

			if (applicationFormDto.getApplication().getId() == null) {
				this.applicationService.create(applicationFormDto);
				model.addAttribute("message", "Başvurunuz başarıyla alındı, epostanızı kontrol ediniz.");

			} else {

				Student student = null;
				if (authentication != null && authentication.getPrincipal() instanceof Student) {
					student = (Student) authentication.getPrincipal();
				} else {
					return "error";
				}

				this.applicationService.isUserAuthorizedForForm(student, applicationFormDto.getApplication());
				this.applicationService.update(applicationFormDto);

				model.addAttribute("message", "Başvurunuz başarıyla gucellendi.");

			}
			return "applicationSuccess";

		}

	}

	@RequestMapping(value = "/validate/{id}", method = RequestMethod.GET)
	public String form(@PathVariable("id") String id, Model model) {

		if (this.applicationService.validate(id)) {
			model.addAttribute("message", "Başvurunuz doğrulanmıştır.");
			return "validated";
		} else {
			model.addAttribute("message", "Böyle bi basvuru bulunmamaktadır.");
			return "validated";
		}
	}

	@RequestMapping(value = "/applications", method = RequestMethod.GET)
	public String list(Model model) {

		model.addAttribute("applicationList", this.applicationService.getAll());
		return "applications";
	}

}
