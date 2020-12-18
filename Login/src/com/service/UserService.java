package com.service;

import java.util.List;

import com.entity.User;

public interface UserService {

	// 添加 用户
	int addUser(User user);

	// 根据用户id 查询 用户信息
	User queryUserById(String id);

	// 根据用户名 和 密码 查询 用户信息
	User queryUserByNameAndPwd(String userName, String userPwd);

	// 根据用户名 查询 用户信息
	User queryUserByName(String userName);

	// 删除用户
	int deleteUserById(String id);

	// 修改用户
	int editUser(User user);

	List<User> queryUserByLike(String name, int offset, int rows);

}
