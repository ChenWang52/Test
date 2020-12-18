package com.service;

import java.util.List;

import com.entity.User;

public interface UserService {

	// ��� �û�
	int addUser(User user);

	// �����û�id ��ѯ �û���Ϣ
	User queryUserById(String id);

	// �����û��� �� ���� ��ѯ �û���Ϣ
	User queryUserByNameAndPwd(String userName, String userPwd);

	// �����û��� ��ѯ �û���Ϣ
	User queryUserByName(String userName);

	// ɾ���û�
	int deleteUserById(String id);

	// �޸��û�
	int editUser(User user);

	List<User> queryUserByLike(String name, int offset, int rows);

}
