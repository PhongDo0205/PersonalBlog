package com.example.PersonalBlog.Controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import com.example.PersonalBlog.Model.Blog;
import com.example.PersonalBlog.Service.BlogService;
import com.example.PersonalBlog.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class HomeController {

	@Autowired
	BlogService blogService;

	@Autowired
	UserService userService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model, @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, Principal principal, Authentication authentication) {
		if (principal != null) {
			String username = principal.getName();
			model.addAttribute("username", username);
			String blogname = userService.getBlogName(username);
			model.addAttribute("blogname", blogname);
			String role = "";
			for (GrantedAuthority authority : authentication.getAuthorities()) {
				role = authority.getAuthority();
			}
			model.addAttribute("role", role);
		} else {
			model.addAttribute("username", null);
			model.addAttribute("blogname", null);
			model.addAttribute("role", null);
		}

		int temp = pageNumber * 6;
		List<Blog> list = blogService.getAll(temp);

		if (list.isEmpty()) {
			model.addAttribute("error", "Không có bài viết nào để hiển thị.");
			return "index";
		}

		int mid = Math.min(3, list.size());
		List<Blog> temp1 = list.subList(0, mid);
		List<Blog> temp2 = list.subList(mid, list.size());

		if (list.size() < 6) {
			model.addAttribute("nomorepages", true);
		}

		model.addAttribute("pageNumberNext", pageNumber + 1);
		model.addAttribute("pageNumberPrevious", pageNumber - 1);
		model.addAttribute("temp1", temp1);
		model.addAttribute("temp2", temp2);
		return "index";
	}

	@RequestMapping(value = "/checkSession", method = RequestMethod.GET)
	public String checkSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		if (session != null) {
			System.out.println("Session đã được tạo.");
		} else {
			System.out.println("Session chưa được tạo.");
		}
		return "redirect:/";
	}

}
