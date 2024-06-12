package com.example.PersonalBlog.Model;

import java.util.Date;

public class Comment {
	private Long id;
	private Long blogid;
	private Long userid;
	private String comment;
	private Date datetime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBlogid() {
		return blogid;
	}

	public void setBlogid(Long blogid) {
		this.blogid = blogid;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
}
