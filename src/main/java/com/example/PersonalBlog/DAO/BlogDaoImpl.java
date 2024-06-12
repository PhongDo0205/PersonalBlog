package com.example.PersonalBlog.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import com.example.PersonalBlog.Model.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class BlogDaoImpl implements BlogDao {

	private static final Logger logger = LoggerFactory.getLogger(BlogDaoImpl.class);

	private final DataSource dataSource;

	@Autowired
	public BlogDaoImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void create(Blog blog) {
		String query = "INSERT INTO blogs (blogname, userid, title, content) VALUES (?, ?, ?, ?)";
		try (Connection con = dataSource.getConnection();
			 PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, blog.getBlogname());
			ps.setLong(2, blog.getUserid());
			ps.setString(3, blog.getTitle());
			ps.setString(4, blog.getContent());
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.error("Error creating blog", e);
		}
	}

	public void update(Blog blog) {
		String query = "UPDATE blogs SET title = ?, content = ? WHERE id = ?";
		try (Connection con = dataSource.getConnection();
			 PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, blog.getTitle());
			ps.setString(2, blog.getContent());
			ps.setLong(3, blog.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.error("Error updating blog", e);
		}
	}

	public void delete(Blog blog) {
		String query = "DELETE FROM blogs WHERE id = ?";
		try (Connection con = dataSource.getConnection();
			 PreparedStatement ps = con.prepareStatement(query)) {
			ps.setLong(1, blog.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.error("Error deleting blog", e);
		}
	}

	public String getBlogName(Long userid) {
		String query = "SELECT blogname FROM users WHERE id = ?";
		try (Connection con = dataSource.getConnection();
			 PreparedStatement ps = con.prepareStatement(query)) {
			ps.setLong(1, userid);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return rs.getString("blogname");
			}
		} catch (SQLException e) {
			logger.error("Error getting blog name", e);
		}
		return null;
	}

	public List<Blog> getAll(int pageNumber) {
		String query = "SELECT * FROM blogs ORDER BY id DESC LIMIT 6 OFFSET ?";
		List<Blog> blogList = new ArrayList<>();
		try (Connection con = dataSource.getConnection();
			 PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, pageNumber * 6);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Blog blog = new Blog();
					blog.setId(rs.getLong("id"));
					blog.setBlogname(rs.getString("blogname"));
					blog.setUserid(rs.getLong("userid"));
					blog.setTitle(rs.getString("title"));
					blog.setContent(rs.getString("content"));
					blogList.add(blog);
				}
			}
		} catch (SQLException e) {
			logger.error("Error getting all blogs", e);
		}
		return blogList;
	}

	public List<Blog> getAllUser(Long userid) {
		String query = "SELECT * FROM blogs WHERE userid = ?";
		List<Blog> blogList = new ArrayList<>();
		try (Connection con = dataSource.getConnection();
			 PreparedStatement ps = con.prepareStatement(query)) {
			ps.setLong(1, userid);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Blog blog = new Blog();
					blog.setId(rs.getLong("id"));
					blog.setBlogname(rs.getString("blogname"));
					blog.setUserid(rs.getLong("userid"));
					blog.setTitle(rs.getString("title"));
					blog.setContent(rs.getString("content"));
					blogList.add(blog);
				}
			}
		} catch (SQLException e) {
			logger.error("Error getting all user blogs", e);
		}
		return blogList;
	}

	public Blog getByID(Long id) {
		String query = "SELECT * FROM blogs WHERE id = ?";
		Blog blog = new Blog();
		try (Connection con = dataSource.getConnection();
			 PreparedStatement ps = con.prepareStatement(query)) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					blog.setId(rs.getLong("id"));
					blog.setBlogname(rs.getString("blogname"));
					blog.setUserid(rs.getLong("userid"));
					blog.setTitle(rs.getString("title"));
					blog.setContent(rs.getString("content"));
				}
			}
		} catch (SQLException e) {
			logger.error("Error getting blog by ID", e);
		}
		return blog;
	}
}
