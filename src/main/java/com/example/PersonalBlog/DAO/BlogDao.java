package com.example.PersonalBlog.DAO;

import com.example.PersonalBlog.Model.Blog;
import java.util.List;

public interface BlogDao {
	void create(Blog blog);
	void update(Blog blog);
	void delete(Blog blog);
	String getBlogName(Long userid);
	List<Blog> getAll(int pageNumber);
	List<Blog> getAllUser(Long userid);
	Blog getByID(Long id);
}
