package com.example.demo.smart.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.smart.dao.UserRepository;
import com.example.demo.smart.entities.User;
import com.example.demo.smart.helper.Message;


@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@RequestMapping("/")
	public String home(ModelMap model) {
		model.addAttribute("title", "Home - Smart Contact Manager.");
		return "home";
	}

	@RequestMapping("/about")
	public String about(ModelMap model) {
		model.addAttribute("title", "About - Smart Contact Manager.");
		return "about";
	}

	@RequestMapping("/signup")
	public String signup(ModelMap model) {
		model.addAttribute("title", "Register - Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}

	@RequestMapping(value = "/do_register", method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult result1,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, ModelMap model,
			HttpSession session) {
		try {
			if (!agreement) {
				System.out.println("You have not agreed the terms & condition");
				throw new Exception("You have not agreed the terms & condition");
			}

			if(result1.hasErrors()){
				System.out.println("Error "+result1.toString());
				model.addAttribute("user", user);
				return "signup";
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageurl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			System.out.println("agreement " + agreement);
			System.out.println("USER" + user);
			User result = this.userRepository.save(user);
			model.addAttribute("user", new User());
			// session.setAttribute("Successfully Registered !!", "alert-success");
			session.setAttribute("message", new Message("Successfully Registered !!", "alert-success"));
			return "signup";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong !!" + e.getMessage(), "alert-danger"));
			return "signup";

		}
	}
	@RequestMapping("/signin")
	public String login(ModelMap model) {
		model.addAttribute("title", "Login - Smart Contact Manager.");
		return "login";
	}

	@RequestMapping("/login-fail")
	public String error(ModelMap model){
		model.addAttribute("title", "Error - Smart Contact Manager");
		return "error";
	}
}
