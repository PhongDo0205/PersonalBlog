package com.example.PersonalBlog.Service;

import com.example.PersonalBlog.Model.Comment;
import java.util.List;

public interface CommentService {
	List<Comment> getComments(Long blogid);
	void create(Comment comment);
}
