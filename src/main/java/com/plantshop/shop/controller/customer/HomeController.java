package com.plantshop.shop.controller.customer;

import com.plantshop.shop.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	@Autowired private PlantService plantService;
	@GetMapping("/")
	public String getHome(Model model){
		model.addAttribute("plants",plantService.getTopSellingPlant());
		return "customer/home";
	}
}
