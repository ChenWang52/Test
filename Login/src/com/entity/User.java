package com.entity;

import lombok.Data;

@Data
public class User {
	int id;// id
	String username;// 用户名(手机号)
	String password;// 密码

	int total;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\", \"username\":\"" + username + "\", \"password\":\"" + password
				+ "\", \"total\":\"" + total + "\"}";
	}

}
