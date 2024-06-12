package com.example.PersonalBlog.Controller;

import com.example.PersonalBlog.Model.User;
import com.example.PersonalBlog.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);


    @Autowired
    private HttpServletRequest request;

//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    public String processLogin(Model,
//                               @Valid @ModelAttribute("user") User user,
//                               BindingResult bindingResult) {
//        System.out.println("login post");
//        if (bindingResult.hasErrors()) {
//            return "login";
//        }
//
//        String username = user.getUsername();
//        String password = user.getPassword();
//        User existingUser = userService.getUserByUsername(username);
//
//        if (existingUser != null) {
//            if (bCryptPasswordEncoder.matches(password, existingUser.getPassword())) {
//                System.out.println("login success!");
//
//                HttpSession session = request.getSession(true);
//                session.setAttribute("username", username);
//                session.setAttribute("role", userService.getRolesByUsername(username));
//                return "redirect:/";
//            } else {
//                model.addAttribute("error", "Incorrect password.");
//                return "login";
//            }
//        } else {
//            model.addAttribute("error", "Username not found.");
//            return "login";
//        }
//    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        System.out.println("login get");
        model.addAttribute("user", new User());
        return "login";
    }


    @RequestMapping(value = "/login/success", method = RequestMethod.GET)
    public String loginSuccess() {
        return "redirect:/";
    }

    @RequestMapping(value = "/login/failed", method = RequestMethod.GET)
    public String loginFailed(Model model) {
        model.addAttribute("error", "true");
        return "error";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logout() {
        return "redirect:/";
    }
}

