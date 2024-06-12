package com.example.PersonalBlog.Controller;

import com.example.PersonalBlog.Model.Blog;
import com.example.PersonalBlog.Model.Comment;
import com.example.PersonalBlog.Service.BlogService;
import com.example.PersonalBlog.Service.CommentService;
import com.example.PersonalBlog.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/blog/create", method = RequestMethod.POST)
    public String blogCreatePost(@ModelAttribute("blog") Blog blog, Principal principal, RedirectAttributes redirectAttributes) {
        String blogName = userService.getBlogName(principal.getName());
        Long userId = userService.getUserIdByUsername(principal.getName());
        blog.setBlogname(blogName);
        blog.setUserid(userId);
        blogService.create(blog);

        redirectAttributes.addFlashAttribute("blogname", blog.getBlogname());
        redirectAttributes.addFlashAttribute("blogtitle", blog.getTitle());
        redirectAttributes.addFlashAttribute("blogcontent", blog.getContent());
        return "redirect:/blog/success";
    }

    @RequestMapping(value = "/blog/create", method = RequestMethod.GET)
    public String blogCreateGet(Model model, Principal principal) {
        String username = principal.getName();
        String blogname = userService.getBlogName(username);
        model.addAttribute("blogname", blogname);
        model.addAttribute("username", username);
        model.addAttribute("blog", new Blog());
        return "blogpost";
    }

    @RequestMapping(value = "/blog/success", method = RequestMethod.GET)
    public String successPost(Model model,
                              Principal principal,
                              @ModelAttribute("blogtitle") String blogtitle,
                              @ModelAttribute("blogcontent") String blogcontent,
                              @ModelAttribute("blogname") String blogname) {
        model.addAttribute("blogtitle", blogtitle);
        model.addAttribute("blogcontent", blogcontent);
        model.addAttribute("blogname", blogname);
        model.addAttribute("role", getUserRole(principal));
        return "posted";
    }

    @RequestMapping(value = "/blog/{blogname}/{id}", method = RequestMethod.GET)
    public String showBlogDetails(@PathVariable("blogname") String blogname, @PathVariable("id") Long id,
                                  Model model, Principal principal) {
        Blog blog = blogService.getByID(id);
        if (blog == null) {
            return "redirect:/error";
        }
        List<Comment> comments = commentService.getComments(id);
        Map<Long, String> usernameMap = new HashMap<>();
        for (Comment comment : comments) {
            Long userId = comment.getUserid();
            String username = userService.getUsernameByUserId(userId);
            usernameMap.put(userId, username);
        }

        if (principal != null) {
            String username = principal.getName();
            String blogUsername = userService.getUsernameByUserId(blog.getUserid());
            model.addAttribute("edit", username.equals(blogUsername));
            model.addAttribute("username", username);
            model.addAttribute("blogUsername", blogUsername);
            model.addAttribute("blogID", blog);
            model.addAttribute("comments", comments);
            model.addAttribute("usernameMap", usernameMap);
            model.addAttribute("editedBlog", blog);
            model.addAttribute("newcomment", new Comment());
            model.addAttribute("role", getUserRole(principal));
        } else {
            return "redirect:/error";
        }
        return "post";
    }

    @RequestMapping(value = "/blog/{blogname}/{id}/edit", method = RequestMethod.POST)
    public String editBlog(@ModelAttribute("editedBlog") Blog editedBlog, @PathVariable("id") Long id,
                           @PathVariable("blogname") String name) {
        Blog blogToUpdate = blogService.getByID(id);

        if (blogToUpdate != null) {
            String blogToUpdateUsername = userService.getUsernameByUserId(blogToUpdate.getUserid());
            if (blogToUpdateUsername.equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
                blogToUpdate.setTitle(editedBlog.getTitle());
                blogToUpdate.setContent(editedBlog.getContent());
                blogService.update(blogToUpdate);
            }
        } else {
            return "redirect:/error";
        }

        return "redirect:/blog/" + name + "/" + id;
    }

    @RequestMapping(value = "/blog/{blogname}/{id}/comment", method = RequestMethod.POST)
    public String addComment(@ModelAttribute("newcomment") Comment comment, Principal principal,
                             @PathVariable("id") Long id, @PathVariable("blogname") String name) {
        if (principal == null) {
            return "redirect:/login";
        }
        Long userId = userService.getUserIdByUsername(principal.getName());
        String username = userService.getUsernameByUserId(userId);
        if (!username.equals(principal.getName())) {
            return "redirect:/error";
        }

        comment.setBlogid(id);
        comment.setUserid(userId);
        comment.setDatetime(new Date());
        commentService.create(comment);

        return "redirect:/blog/" + name + "/" + id;
    }

    private boolean hasRole(Principal principal, String role) {
        if (principal instanceof Authentication) {
            return ((Authentication) principal).getAuthorities().stream()
                    .anyMatch(grantedAuthority -> role.equals(grantedAuthority.getAuthority()));
        }
        return false;
    }

    private String getUserRole(Principal principal) {
        if (principal == null) {
            return "ROLE_ANONYMOUS";
        }
        return ((Authentication) principal).getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));
    }
}

