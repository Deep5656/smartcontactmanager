package com.example.demo.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.smart.dao.ContactRepository;
import com.example.demo.smart.dao.UserRepository;
import com.example.demo.smart.entities.Contact;
import com.example.demo.smart.entities.User;
import com.example.demo.smart.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    // method for aing comman data to the response.
    @ModelAttribute
    public void addCommanData(ModelMap model, Principal principal) {
        String userName = principal.getName();
        System.out.println("userName " + userName);
        // get the user using userName
        User user = userRepository.getUserByUserName(userName);
        System.out.println(user);
        model.addAttribute("user", user);
    }

    // dashboard home
    @RequestMapping("/index")
    public String index(ModelMap model, Principal principal) {
        model.addAttribute("title", "User Dashboard");

        return "normal/user_dashboard";
    }

    // open add-form handler
    @GetMapping("/add-contact")
    public String openAddContactForm(ModelMap model) {
        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());
        return "normal/add_contact_form";
    }

    // processing add-contact form
    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
            Principal principal,HttpSession session) {
        try {
            String name = principal.getName();
            User user = this.userRepository.getUserByUserName(name);


            // if(3>2){
            //     throw new Exception();
            // }

            // processing and uploading file...
            if(file.isEmpty()){
                //if file is empty then try our message
                System.out.println("File is empty");
            }else{
                //update the file to the folder and name to the contact
                contact.setImage(file.getOriginalFilename());
                File saveFile = new ClassPathResource("/static/image").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image is uploaded");
            }

            user.getContacts().add(contact);
            contact.setUser(user);

            this.userRepository.save(user);
            System.out.println("Data" + contact);

            System.out.println("Added to the database");
            // message success...
            session.setAttribute("message", new Message("Your contact is added successfully", "success"));


        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
            e.printStackTrace();
            // message alert...
            session.setAttribute("message", new Message("Something went wrong !!, try again", "danger"));


        }
        return "normal/add_contact_form";
    }

    // show contacts handler...
    @GetMapping("/show-contact")
    public String showContacts(ModelMap m,Principal principal){
        m.addAttribute("title", "Show Contact Page");
        // contact ki list ko bhejna hai...
        String userName = principal.getName();
        User user = this.userRepository.getUserByUserName(userName);
        List<Contact> contacts = this.contactRepository.findContactByUser(user.getId());

        m.addAttribute("contacts",contacts);
        return "normal/show_contacts";
    }
}
