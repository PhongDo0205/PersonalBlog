package com.example.PersonalBlog.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import com.example.PersonalBlog.Model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class CommentDaoImpl implements CommentDao {

	private static final Logger logger = LoggerFactory.getLogger(CommentDaoImpl.class);

	private final DataSource dataSource;

	@Autowired
	public CommentDaoImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public List<Comment> getComments(Long blogid) {
		String query = "SELECT * FROM comments WHERE blogid = ?";
		List<Comment> list = new ArrayList<>();
		try (Connection con = dataSource.getConnection();
			 PreparedStatement ps = con.prepareStatement(query)) {
			ps.setLong(1, blogid);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Comment comment = new Comment();
					comment.setId(rs.getLong("id"));
					comment.setBlogid(rs.getLong("blogid"));
					comment.setUserid(rs.getLong("userid"));
					comment.setComment(rs.getString("comment"));
					comment.setDatetime(rs.getTimestamp("datetime"));
					list.add(comment);
				}
			}
		} catch (SQLException e) {
			logger.error("Error getting comments", e);
		}
		return list;
	}

	public void create(Comment comment) {
		String query = "INSERT INTO comments (blogid, userid, comment) VALUES (?, ?, ?)";
		try (Connection con = dataSource.getConnection();
			 PreparedStatement ps = con.prepareStatement(query)) {
			ps.setLong(1, comment.getBlogid());
			ps.setLong(2, comment.getUserid());
			ps.setString(3, comment.getComment());
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.error("Error creating comment", e);
		}
	}
}
