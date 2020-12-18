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
	private HttpServletRequest request;// �Զ�ע��request
	@Autowired
	private HttpServletResponse response;// �Զ�ע��response

	@Autowired
	private UserService userService;

	// һ��ȥ������ҳ��
	@RequestMapping(value = "/")
	public String getIndex() {
		return "login";

	}

	// ��¼ҳ��
	@RequestMapping(value = "/login")
	public String login() {
		return "login";

	}

	// ע��ҳ��
	@RequestMapping(value = "/register")
	public String register() {
		return "register";

	}

	// ��¼�ɹ�ҳ��
	@RequestMapping(value = "/success")
	public String success() {
		return "success";

	}

	// ��¼У��
	@RequestMapping(value = "/loginCon", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String login(String userName, String userPwd, HttpSession session) {
		// �����û��� �� ���� ��ѯ �û���Ϣ
		User user = userService.queryUserByNameAndPwd(userName, userPwd);
		// �ж� ���û��Ƿ����
		if (user == null) {
			return "{\"msg\":\"-1\"}";
		}

		// У��ͨ��,�ѵ�¼�û���Ϣ���õ�session��
		session.setAttribute("user", user);
		// ��ת�� ������
		return "{\"msg\":\"0\"}";

	}

	// ͨ������ƶ���ƽ̨������֤��
	@RequestMapping(value = "/sendVerifyCode", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String sendVerifyCode(Model model, String phone, HttpSession session) {
		try {
			// ����6λ��֤��
			String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
			System.out.print("��֤��: " + verifyCode);
			// ���Ͷ���
			ZhenziSmsClient client = new ZhenziSmsClient(Config.apiUrl, Config.appId, Config.appSecret);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("number", phone);
			params.put("templateId", Config.templateId);
			String[] templateParams = { verifyCode, "5��������Ч" };
			params.put("templateParams", templateParams);
			String result = client.send(params);
			JSONObject json = JSONObject.fromObject(result);
			System.out.println(json.getString("code"));
			if (!"0".equals(json.getString("code"))) {// ���Ͷ���ʧ��
				return "{\"msg\":\"-1\"}";
			}
			session.setAttribute("zhenzisms_mobile", phone);
			session.setAttribute("zhenzisms_code", verifyCode);
			session.setAttribute("zhenzisms_createTime", System.currentTimeMillis());
			session.setAttribute("zhenzisms_expire", 5 * 60);
			return "{\"msg\":\"0\"}";// ���Ͷ��ųɹ�
		} catch (Exception e) {

		}
		return "{\"msg\":\"-1\"}";// ���Ͷ���ʧ��
	}

	// ע��
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
			// msg:-2 δ������֤��
			model.addAttribute("error", "���ֻ�����δ��ȡ��֤��");
			return "register";

		}
		if (!sessionMobile.equals(phone)) {
			// msg:-3 �ֻ��Ŵ���
			model.addAttribute("error", "�ֻ��Ŵ���");
			return "register";

		}
		if (!sessionCode.equals(code)) {
			// msg:-4 ��֤�����
			model.addAttribute("error", "��֤�����");
			return "register";

		}
		if ((System.currentTimeMillis() - Long.parseLong(createTime)) > 1000 * Integer.parseInt(expire)) {
			// msg:-5 ��֤���ѹ���
			model.addAttribute("error", "��֤���ѹ���");
			return "register";

		}
		// �����û��� ��ѯ ���û����Ƿ��Ѿ�ע��
		User user1 = userService.queryUserByName(phone);
		if (user1 != null) {
			// msg:1 ���ֻ����Ѿ���ע��
			model.addAttribute("error", "���ֻ����Ѿ���ע��");
			return "register";
		}
		session.setAttribute("zhenzisms_mobile", "");
		session.setAttribute("zhenzisms_code", "");
		session.setAttribute("zhenzisms_createTime", System.currentTimeMillis());
		session.setAttribute("zhenzisms_expire", 0);
		session.setAttribute("zhenzisms_phone", phone);

		// ���� User ����
		User user = new User();
		user.setUsername(phone);
		user.setPassword(password);

		// �������û�
		int num = userService.addUser(user);
		if (num > 0) {

			// msg:0 �û�ע��ɹ�
			model.addAttribute("error", "�û�ע��ɹ�");
			return "login";
		}
		// msg:-1 �û�ע��ʧ��
		model.addAttribute("error", "�û�ע��ʧ��");
		return "register";

	}

	private String blank(Object s) {
		if (s == null)
			return "";
		return s.toString();
	}
}
