package com.example.PersonalBlog.Service;

import com.example.PersonalBlog.Model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final DataSource dataSource;

    public UserDetailsServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String userQuery = "SELECT * FROM users WHERE username = ?";
        String roleQuery = "SELECT * FROM authorities WHERE userid = ?";
        User user = null;
        List<String> roles = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement userPs = con.prepareStatement(userQuery)) {
            userPs.setString(1, username);
            try (ResultSet rs = userPs.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getLong("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setEnabled(rs.getBoolean("enabled"));
                }
            }
        } catch (SQLException e) {
            logger.error("Error getting user by username", e);
            throw new UsernameNotFoundException("User not found with username: " + username, e);
        }

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        try (Connection con = dataSource.getConnection();
             PreparedStatement rolePs = con.prepareStatement(roleQuery)) {
            rolePs.setLong(1, user.getId());
            try (ResultSet rs = rolePs.executeQuery()) {
                while (rs.next()) {
                    roles.add(rs.getString("authority"));
                }
            }
        } catch (SQLException e) {
            logger.error("Error getting roles for user by username", e);
        }
        System.out.println(roles);
        return UserDetailsImpl.build(user, roles);
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        String query = "SELECT * FROM users u JOIN authorities a ON u.id = a.userid WHERE u.username = ? ";
//
//        User user = null;
//        List<String> roles = new ArrayList<>();
//
//        try (Connection con = dataSource.getConnection();
//             PreparedStatement ps = con.prepareStatement(query)) {
//            ps.setString(1, username);
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    if (user == null) {
//                        user = new User();
//                        user.setId(rs.getLong("id"));
//                        user.setUsername(rs.getString("username"));
//                        user.setPassword(rs.getString("password"));
//                        user.setEnabled(rs.getBoolean("enabled"));
//                        roles.add(rs.getString("authority"));
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            logger.error("Error getting user by username", e);
//            throw new UsernameNotFoundException("User not found with username: " + username, e);
//        }
//
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found with username: " + username);
//        }
//
//        return UserDetailsImpl.build(user, roles);
//    }

}
