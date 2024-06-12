package com.example.PersonalBlog.Service;

import java.util.List;
import com.example.PersonalBlog.DAO.BlogDao;
import com.example.PersonalBlog.Model.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BlogServiceImpl implements BlogService {

	@Autowired
	private BlogDao blogDao;

	@Transactional
	public void create(Blog blog) {
		blogDao.create(blog);
	}

	public List<Blog> getAll(int pageNumber) {
		return blogDao.getAll(pageNumber);
	}

	public List<Blog> getAllUser(Long userid) {
		return blogDao.getAllUser(userid);
	}

	@Transactional
	public void update(Blog blog) {
		blogDao.update(blog);
	}

	public String getBlogName(Long userid) {
		return blogDao.getBlogName(userid);
	}

	public Blog getByID(Long id) {
		return blogDao.getByID(id);
	}

	@Transactional
	public void delete(Blog blog) {
		blogDao.delete(blog);
	}
}
