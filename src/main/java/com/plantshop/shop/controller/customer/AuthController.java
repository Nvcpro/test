package com.plantshop.shop.controller.customer;

import com.plantshop.shop.model.Account;
import com.plantshop.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
	@Autowired private UserService userService;
	private boolean isAuthenticated(){
		Authentication auth=SecurityContextHolder.getContext().getAuthentication();
		return auth!=null&&auth.isAuthenticated()&&!(auth instanceof AnonymousAuthenticationToken);
	}
	@GetMapping("/register")
	public String getRegister(@ModelAttribute("account") Account account){
		if(isAuthenticated()){
			return "redirect:/";
		}
		return "customer/register";
	}
	@PostMapping("/register")
	public String doRegister(@ModelAttribute("account") Account account){
		if(userService.getUserByEmail(account.getEmail())!=null){
			return "redirect:/register?error";
		}
		userService.createAccount(account);
		return "redirect:/login";
	}
}
