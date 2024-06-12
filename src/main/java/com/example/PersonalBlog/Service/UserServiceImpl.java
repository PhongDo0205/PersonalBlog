package com.example.PersonalBlog.Service;

import java.util.List;
import com.example.PersonalBlog.DAO.UserDao;
import com.example.PersonalBlog.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    public void create(User user) {
        userDao.createUser(user);
    }

    public List<User> getAll() {
        return userDao.getAll();
    }

    public void delete(User user) {
        userDao.delete(user);
    }

    public void suspendOrEnable(String username, int enabled) {
        userDao.suspendOrEnable(username, enabled);
    }

    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    public List<String> getRolesByUsername(String username) {
        return userDao.getRolesByUsername(username);
    }

    @Override
    public String getBlogName(String name) {
        return userDao.getBlogName(name);
    }

    @Override
    public Long getUserIdByUsername(String name) {
        return userDao.getUserIdByUsername(name);
    }

    @Override
    public String getUsernameByUserId(Long userId) {
        return userDao.getUsernameByUserId(userId);
    }

}
