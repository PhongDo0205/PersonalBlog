package com.example.PersonalBlog.DAO;

import com.example.PersonalBlog.Model.Comment;
import java.util.List;

public interface CommentDao {
	List<Comment> getComments(Long blogid);
	void create(Comment comment);
}
