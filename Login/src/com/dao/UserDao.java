package com.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.entity.User;

public interface UserDao {

	// ��� �û�
	int addUser(User user);

	// �����û�id ��ѯ �û���Ϣ
	User queryUserById(@Param("id") String id);

	// �����û��� �� ���� ��ѯ �û���Ϣ
	User queryUserByNameAndPwd(@Param("userName") String userName, @Param("userPwd") String userPwd);

	// �����û��� ��ѯ �û���Ϣ
	User queryUserByName(@Param("userName") String userName);

	// ɾ���û�
	int deleteUserById(@Param("id") String id);

	// �޸��û�
	int editUser(User user);

	List<User> queryUserByLike(@Param("name") String name, @Param("offset") int offset, @Param("rows") int rows);

}
