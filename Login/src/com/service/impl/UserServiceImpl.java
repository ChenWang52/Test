package com.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.UserDao;
import com.entity.User;
import com.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;

	public int addUser(User user) {

		return userDao.addUser(user);
	}

	public User queryUserById(String id) {

		return userDao.queryUserById(id);
	}

	public User queryUserByNameAndPwd(String userName, String userPwd) {

		return userDao.queryUserByNameAndPwd(userName, userPwd);
	}

	public User queryUserByName(String userName) {

		return userDao.queryUserByName(userName);
	}

	public int deleteUserById(String id) {

		return userDao.deleteUserById(id);
	}

	public int editUser(User user) {

		return userDao.editUser(user);
	}

	public List<User> queryUserByLike(String name, int offset, int rows) {

		return userDao.queryUserByLike(name, offset, rows);
	}

}
