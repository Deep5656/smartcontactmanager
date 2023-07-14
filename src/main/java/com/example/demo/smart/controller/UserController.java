package com.example.demo.smart.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.smart.dao.UserRepository;
import com.example.demo.smart.entities.User;

@Controller
@RequestMapping("/user")
public class UserController {
    
	@Autowired
	private UserRepository userRepository;
	
    @RequestMapping("/index")
    public String index(ModelMap model,Principal principal){
        String userName = principal.getName();
        System.out.println("userName "+userName);
        //get the user using userName
        User user = userRepository.getUserByUserName(userName); 
        System.out.println(user);
        model.addAttribute("user", user);
        return "normal/user_dashboard";
    }
}
