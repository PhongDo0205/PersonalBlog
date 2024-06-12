package com.example.PersonalBlog.Controller;

import com.example.PersonalBlog.Model.User;
import com.example.PersonalBlog.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdminController {

	@Autowired
	UserService userService;

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String admin(Model model) {
		List<User> userList = userService.getAll();
		model.addAttribute("users", userList);
		return "admin";
	}

	@RequestMapping(value = "/admin/user/suspend/{username}", method = RequestMethod.GET)
	public String suspendUser(Model model, @PathVariable("username") String username) {
		userService.suspendOrEnable(username, 0);

		List<User> userList = userService.getAll();
		model.addAttribute("users", userList);
		return "admin";
	}

	@RequestMapping(value = "/admin/user/enable/{username}", method = RequestMethod.GET)
	public String enableUser(Model model, @PathVariable("username") String username) {
		userService.suspendOrEnable(username, 1);

		List<User> userList = userService.getAll();
		model.addAttribute("users", userList);
		return "admin";
	}
}
