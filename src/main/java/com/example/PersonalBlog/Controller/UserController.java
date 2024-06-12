package com.example.PersonalBlog.Controller;

import com.example.PersonalBlog.Model.User;
import com.example.PersonalBlog.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public String createAccount(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "createUser";
        } else {
            userService.create(user);
            return "redirect:/user/create/success?username=" + user.getUsername();
        }
    }

    @RequestMapping(value = "/user/create", method = RequestMethod.GET)
    public String createAccount(Model model) {
        model.addAttribute("user", new User());
        return "createUser";
    }

    @RequestMapping(value = "/user/create/success", method = RequestMethod.GET)
    public String accountSuccess(Model model, @RequestParam("username") String username) {
        model.addAttribute("username", username);
        return "accountSuccess";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile(Model model, Principal principal, Authentication authentication) {
        if (principal != null) {
            String username = principal.getName();
            User user = userService.getUserByUsername(username);
            if (user != null) {
                model.addAttribute("username", username);
                model.addAttribute("user", user);
                String role = "";
                for (GrantedAuthority authority : authentication.getAuthorities()) {
                    role = authority.getAuthority();
                }
                model.addAttribute("role", role);
            }
        }
        return "profile";
    }
}
