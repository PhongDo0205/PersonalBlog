package com.example.PersonalBlog.Service;

import com.example.PersonalBlog.Model.Blog;
import java.util.List;

public interface BlogService {
	void create(Blog blog);
	void update(Blog blog);
	void delete(Blog blog);
	String getBlogName(Long userid);
	List<Blog> getAllUser(Long userid);
	List<Blog> getAll(int pageNumber);
	Blog getByID(Long id);
}
