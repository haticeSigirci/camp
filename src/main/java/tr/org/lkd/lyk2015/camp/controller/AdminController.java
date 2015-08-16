package tr.org.lkd.lyk2015.camp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


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
	

}
