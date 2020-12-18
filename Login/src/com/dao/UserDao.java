package com.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.entity.User;

public interface UserDao {

	// 添加 用户
	int addUser(User user);

	// 根据用户id 查询 用户信息
	User queryUserById(@Param("id") String id);

	// 根据用户名 和 密码 查询 用户信息
	User queryUserByNameAndPwd(@Param("userName") String userName, @Param("userPwd") String userPwd);

	// 根据用户名 查询 用户信息
	User queryUserByName(@Param("userName") String userName);

	// 删除用户
	int deleteUserById(@Param("id") String id);

	// 修改用户
	int editUser(User user);

	List<User> queryUserByLike(@Param("name") String name, @Param("offset") int offset, @Param("rows") int rows);

}
