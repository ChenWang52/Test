package com.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.config.Config;
import com.entity.User;
import com.service.UserService;
import com.zhenzi.sms.ZhenziSmsClient;

import net.sf.json.JSONObject;

@Controller
@SessionAttributes("user")
public class UserController {
	@Autowired
	private HttpServletRequest request;// 自动注入request
	@Autowired
	private HttpServletResponse response;// 自动注入response

	@Autowired
	private UserService userService;

	// 一进去见到的页面
	@RequestMapping(value = "/")
	public String getIndex() {
		return "login";

	}

	// 登录页面
	@RequestMapping(value = "/login")
	public String login() {
		return "login";

	}

	// 注册页面
	@RequestMapping(value = "/register")
	public String register() {
		return "register";

	}

	// 登录成功页面
	@RequestMapping(value = "/success")
	public String success() {
		return "success";

	}

	// 登录校验
	@RequestMapping(value = "/loginCon", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String login(String userName, String userPwd, HttpSession session) {
		// 根据用户名 和 密码 查询 用户信息
		User user = userService.queryUserByNameAndPwd(userName, userPwd);
		// 判断 该用户是否存在
		if (user == null) {
			return "{\"msg\":\"-1\"}";
		}

		// 校验通过,把登录用户信息设置到session中
		session.setAttribute("user", user);
		// 跳转到 主界面
		return "{\"msg\":\"0\"}";

	}

	// 通过榛子云短信平台发送验证码
	@RequestMapping(value = "/sendVerifyCode", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String sendVerifyCode(Model model, String phone, HttpSession session) {
		try {
			// 生成6位验证码
			String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
			System.out.print("验证码: " + verifyCode);
			// 发送短信
			ZhenziSmsClient client = new ZhenziSmsClient(Config.apiUrl, Config.appId, Config.appSecret);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("number", phone);
			params.put("templateId", Config.templateId);
			String[] templateParams = { verifyCode, "5分钟内有效" };
			params.put("templateParams", templateParams);
			String result = client.send(params);
			JSONObject json = JSONObject.fromObject(result);
			System.out.println(json.getString("code"));
			if (!"0".equals(json.getString("code"))) {// 发送短信失败
				return "{\"msg\":\"-1\"}";
			}
			session.setAttribute("zhenzisms_mobile", phone);
			session.setAttribute("zhenzisms_code", verifyCode);
			session.setAttribute("zhenzisms_createTime", System.currentTimeMillis());
			session.setAttribute("zhenzisms_expire", 5 * 60);
			return "{\"msg\":\"0\"}";// 发送短信成功
		} catch (Exception e) {

		}
		return "{\"msg\":\"-1\"}";// 发送短信失败
	}

	// 注册
	@RequestMapping(value = "/registerCon", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
	public String register(Model model, String phone, String password, String code, HttpSession session) {
		String sessionMobile = blank(session.getAttribute("zhenzisms_mobile"));
		String sessionCode = blank(session.getAttribute("zhenzisms_code"));
		String createTime = blank(session.getAttribute("zhenzisms_createTime"));
		String expire = blank(session.getAttribute("zhenzisms_expire"));

		model.addAttribute("phone", phone);
		model.addAttribute("code", code);
		model.addAttribute("password", password);
		if (sessionMobile.equals("")) {
			// msg:-2 未生成验证码
			model.addAttribute("error", "该手机号码未获取验证码");
			return "register";

		}
		if (!sessionMobile.equals(phone)) {
			// msg:-3 手机号错误
			model.addAttribute("error", "手机号错误");
			return "register";

		}
		if (!sessionCode.equals(code)) {
			// msg:-4 验证码错误
			model.addAttribute("error", "验证码错误");
			return "register";

		}
		if ((System.currentTimeMillis() - Long.parseLong(createTime)) > 1000 * Integer.parseInt(expire)) {
			// msg:-5 验证码已过期
			model.addAttribute("error", "验证码已过期");
			return "register";

		}
		// 根据用户名 查询 该用户名是否已经注册
		User user1 = userService.queryUserByName(phone);
		if (user1 != null) {
			// msg:1 该手机号已经被注册
			model.addAttribute("error", "该手机号已经被注册");
			return "register";
		}
		session.setAttribute("zhenzisms_mobile", "");
		session.setAttribute("zhenzisms_code", "");
		session.setAttribute("zhenzisms_createTime", System.currentTimeMillis());
		session.setAttribute("zhenzisms_expire", 0);
		session.setAttribute("zhenzisms_phone", phone);

		// 构造 User 对象
		User user = new User();
		user.setUsername(phone);
		user.setPassword(password);

		// 插入新用户
		int num = userService.addUser(user);
		if (num > 0) {

			// msg:0 用户注册成功
			model.addAttribute("error", "用户注册成功");
			return "login";
		}
		// msg:-1 用户注册失败
		model.addAttribute("error", "用户注册失败");
		return "register";

	}

	private String blank(Object s) {
		if (s == null)
			return "";
		return s.toString();
	}
}
