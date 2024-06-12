package com.example.PersonalBlog.Service;

import com.example.PersonalBlog.Model.User;
import java.util.List;

public interface UserService {
	void create(User user);
	List<User> getAll();
	void delete(User user);
	void suspendOrEnable(String username, int enabled);
	User getUserByUsername(String username);
	List<String> getRolesByUsername(String username);
    String getBlogName(String name);

	Long getUserIdByUsername(String name);

	String getUsernameByUserId(Long userid);
}
