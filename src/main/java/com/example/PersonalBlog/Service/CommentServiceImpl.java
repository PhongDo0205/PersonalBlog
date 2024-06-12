package com.example.PersonalBlog.Service;

import java.util.List;
import com.example.PersonalBlog.DAO.CommentDao;
import com.example.PersonalBlog.Model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentDao commentDao;

	public List<Comment> getComments(Long blogid) {
		return commentDao.getComments(blogid);
	}

	@Transactional
	public void create(Comment comment) {
		commentDao.create(comment);
	}
}
