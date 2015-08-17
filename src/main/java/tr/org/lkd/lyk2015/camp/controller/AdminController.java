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
import tr.org.lkd.lyk2015.camp.service.AdminService;



@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@RequestMapping("")
	public String list(Model model) { 
		List<Admin> admins = adminService.getAll();

		model.addAttribute("adminList", admins);
		
		return "adminList";
	}
	
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String createForm(@ModelAttribute Admin admin) {
		
		return "adminForm";
	}
	
	////
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public String create(@ModelAttribute Admin admin) {	
		
		adminService.create(admin);

		return "redirect:/admin"; // ?? recursive
	}
	
	//
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET )
	public String edit(@ModelAttribute Admin admin, Model model, @PathVariable(value="id") Long id){

		
		Admin adminNew = adminService.getById(id);

		model.addAttribute("admin", adminNew);

		return "adminEdit";
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String update(@PathVariable(value = "id") Long id, @ModelAttribute Admin admin, Model model) {
	
		adminService.update(admin);

		return "redirect:/admin";  // aynı formu iki kere gondermesin 
	}

}
