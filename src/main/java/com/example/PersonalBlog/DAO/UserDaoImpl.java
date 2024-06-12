package com.example.PersonalBlog.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import com.example.PersonalBlog.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class UserDaoImpl implements UserDao {

	private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

	private final DataSource dataSource;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public UserDaoImpl(DataSource dataSource, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.dataSource = dataSource;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	public void createUser(User user) {
		String password = bCryptPasswordEncoder.encode(user.getPassword());
		String createQuery = "INSERT INTO users (username, password, email, blogname) VALUES (?, ?, ?, ?)";
		String roleQuery = "INSERT INTO authorities (userid, authority) VALUES (?, ?)";

		try (Connection con = dataSource.getConnection();
			 PreparedStatement psCreate = con.prepareStatement(createQuery, Statement.RETURN_GENERATED_KEYS);
			 PreparedStatement psRole = con.prepareStatement(roleQuery)) {

			psCreate.setString(1, user.getUsername());
			psCreate.setString(2, password);
			psCreate.setString(3, user.getEmail());
			psCreate.setString(4, user.getBlogname());
			psCreate.executeUpdate();

			try (ResultSet generatedKeys = psCreate.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					long userId = generatedKeys.getLong(1);

					psRole.setLong(1, userId);
					psRole.setString(2, "ROLE_USER");
					psRole.executeUpdate();
				} else {
					throw new SQLException("Creating user failed, no ID obtained.");
				}
			}
		} catch (SQLException e) {
			logger.error("Error creating user", e);
		}
	}


	public List<User> getAll() {
		String query = "SELECT * FROM users";
		List<User> userList = new ArrayList<>();
		try (Connection con = dataSource.getConnection();
			 PreparedStatement ps = con.prepareStatement(query);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getLong("id"));
				user.setUsername(rs.getString("username"));
				user.setEmail(rs.getString("email"));
				user.setBlogname(rs.getString("blogname"));
				userList.add(user);
			}
		} catch (SQLException e) {
			logger.error("Error getting all users", e);
		}
		return userList;
	}

	public void delete(User user) {
		String query = "DELETE FROM users WHERE username = ?";
		try (Connection con = dataSource.getConnection();
			 PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, user.getUsername());
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.error("Error deleting user", e);
		}
	}

	public void suspendOrEnable(String username, int enabled) {
		String query = "UPDATE users SET enabled = ? WHERE username = ?";
		try (Connection con = dataSource.getConnection();
			 PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, enabled);
			ps.setString(2, username);
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.error("Error suspending or enabling user", e);
		}
	}

	public User getUserByUsername(String username) {
		String query = "SELECT * FROM users WHERE username = ?";
		User user = null;
		try (Connection con = dataSource.getConnection();
			 PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, username);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					user = new User();
					user.setId(rs.getLong("id"));
					user.setUsername(rs.getString("username"));
					user.setPassword(rs.getString("password"));
					user.setEmail(rs.getString("email"));
					user.setBlogname(rs.getString("blogname"));
				}
			}
		} catch (SQLException e) {
			logger.error("Error getting user by username", e);
		}
		return user;
	}

	public List<String> getRolesByUsername(String username) {
		String query = "SELECT authority FROM authorities WHERE userid = (SELECT id FROM users WHERE username = ?)";
		List<String> roles = new ArrayList<>();
		try (Connection con = dataSource.getConnection();
			 PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, username);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					roles.add(rs.getString("authority"));
				}
			}
		} catch (SQLException e) {
			logger.error("Error getting roles by username", e);
		}
		return roles;
	}

	@Override
	public String getBlogName(String name) {
		String query = "SELECT blogname FROM users WHERE username = ?";
		try (Connection con = dataSource.getConnection();
			 PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, name);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getString("blogname");
				}
			}
		} catch (SQLException e) {
			logger.error("Error getting blogname by username", e);
		}
		return null;
	}


	@Override
	public Long getUserIdByUsername(String name) {
		String query = "SELECT id FROM users WHERE username = ?";
		try (Connection con = dataSource.getConnection();
			 PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, name);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getLong("id");
				}
			}
		} catch (SQLException e) {
			logger.error("Error getting user ID by username", e);
		}
		return null;
	}

	@Override
	public String getUsernameByUserId(Long userId) {
		String query = "SELECT username FROM users WHERE id = ?";
		try (Connection con = dataSource.getConnection();
			 PreparedStatement ps = con.prepareStatement(query)) {
			ps.setLong(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getString("username");
				}
			}
		} catch (SQLException e) {
			logger.error("Error getting username by user ID", e);
		}
		return null;
	}
}
