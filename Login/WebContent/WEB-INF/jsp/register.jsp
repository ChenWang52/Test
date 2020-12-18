<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>注册</title>

<script type="text/javascript" src="js/jquery1.10.2.min.js"></script>

<style type="text/css">
body {
	font: normal 14px/1.4 "Helvetica Neue", "HelveticaNeue", Helvetica,
		Arial, sans-serif;
}

div {
	display: block;
}

input, button {
	outline: none;
}

::-moz-focus-inner {
	border: 0px;
}

/*去除按钮点击时出现的虚线边框*/
.login_bg {
	position: fixed;
	top: 0;
	right: 0;
	bottom: 0;
	left: 0;
	background-size: cover;
}

.container {
	position: relative;
	top: 50%;
	margin-top: 60px;
	margin-left: 350px;
	width: 500px;
}

.signup_forms {
	width: 620px;
	height: 145px;
}

.signup_forms_container {
	width: 620px;
	overflow: hidden;
	background: #fff;
	border-radius: 3px;
}

.form_user, .form_password {
	width: 500px;
	height: 45px;
	margin: 0px;
	padding: 0px;
	border: 0px;
}

.form_password {
	border-top: 1px solid #e3e3e3;
}

.signup_forms input {
	padding: 11px 10px 11px 13px;
	width: 100%;
	margin: 0px;
	background: 0;
	font: 16px/1.4 "Helvetica Neue", "HelveticaNeue", Helvetica, Arial,
		sans-serif;
}

#signup_forms_submit {
	margin-top: 8px;
	width: 500px;
	height: 45px;
	background: #529ECC;
	border: 0px;
	border-radius: 3px;
	cursor: pointer;
}

#signup_forms_submit span {
	color: #fff;
	font: "Helvetica Neue", Arial, Helvetica, sans-serif;
	font-size: 16px;
}
</style>
<script type="text/javascript">
	//倒计时
	var countdown = 60;

	function settime(val) {
		if (countdown == 0) {
			val.removeAttribute("disabled");
			val.value = "获取验证码";
			countdown = 60;
			return false;
		} else {
			val.setAttribute("disabled", true);
			val.value = "重新发送(" + countdown + ")";
			countdown--;
		}
		setTimeout(function() {
			settime(val);
		}, 1000);
	}

	function getCode(val) {
		var valid_rule = /^(13[0-9]|14[5-9]|15[012356789]|166|17[0-8]|18[0-9]|19[8-9])[0-9]{8}$/;// 手机号码校验规则
		$("#phone_msg").html("");
		$("#code_msg").html("");
		$("#password_msg").html("");
		$("#repassword_msg").html("");
		$("#xy_msg").html("");
		var phone = $.trim($("#phone").val());
		if (!phone) {
			$("#phone_msg").html("手机号码不能为空");
			return;
		}
		if (!valid_rule.test($.trim($("#phone").val()))) {
			$("#phone_msg").html("请输入正确的手机号码");
			return;
		}
		$.ajax({
			url : 'sendVerifyCode', //换成自己的url
			type : 'POST',
			dataType : 'json',
			data : {
				'phone' : phone
			},
			success : function(res) {

				if (res.msg == 0) {
					settime(val)
					alert('验证码已发送到' + phone + '，请注意查收');

				} else {

					alert('获取验证码失败,请重新获取');
				}
			},
			error : function(e) {

			}
		});

	}

	$(function() {
		$("#signup_forms_submit")
				.click(
						function() {
							var valid_rule = /^(13[0-9]|14[5-9]|15[012356789]|166|17[0-8]|18[0-9]|19[8-9])[0-9]{8}$/;// 手机号码校验规则

							$("#phone_msg").html("");
							$("#code_msg").html("");
							$("#password_msg").html("");
							$("#repassword_msg").html("");
							$("#xy_msg").html("");
							if ($.trim($("#phone").val()) == "") {
								$("#phone_msg").html("手机号码不能为空");

							} else if (!valid_rule.test($.trim($("#phone")
									.val()))) {
								$("#phone_msg").html("请输入正确的手机号码");

							} else if ($.trim($("#code").val()) == "") {
								$("#code_msg").html("验证码不能为空");

							} else if ($.trim($("#password").val()) == "") {
								$("#password_msg").html("登录密码不能为空");

							} else if ($.trim($("#password").val()).length < 8
									|| $.trim($("#password").val()).length > 20) {
								$("#password_msg").html("密码在8~20个字符之间");

							} else if ($.trim($("#repassword").val()) == "") {
								$("#repassword_msg").html("确认密码不能为空");

							} else if ($.trim($("#password").val()) != $
									.trim($("#repassword").val())) {
								$("#password_msg").html("登录密码和确认密码不一致");
								$("#repassword_msg").html("登录密码和确认密码不一致");

							} else if (!$("#xy").is(":checked")) {
								$("#xy_msg").html("请勾选用户协议");
							} else {

								$("#sign_form").submit();
							}
						});
	});
</script>
</head>
<body>
	<div class="container">
		<div class="signup_forms" class="signup_forms">
			<div id="signup_forms_container" class="signup_forms_container">
				<form class="signup_form_form" id="sign_form" method="post"
					action="registerCon">
					<center>
						<table border="0" cellpadding="10">
							<tr>
								<td align="right"><font color="#7d7d7d"></td>
								<td colspan=2 height=10><h3 style="color: red">${error}</h3></td>
							</tr>
							<tr>
								<td align="right"><font color="#7d7d7d"><strong>手机号码:</td>
								<td colspan=2><input id="phone" name="phone"
									value="${phone}" style="height: 20px; width: 300px;"
									placeholder="" type="text" size="25" /><span id="phone_msg"
									style="color: #FF0000"></span></td>
							</tr>
							<tr>
								<td align="right"><font color="#7d7d7d"><strong>验证码:</strong></font></td>
								<td align="left" colspan=2><input type="text" name="code"
									id="code" value="${code}" style="height: 20px; width: 180px;"
									placeholder="请输入手机验证码" size="20" /> <input type="button"
									style="background-color: #f5f5f5; border: none; height: 48px; width: 120px; border-radius: 2px; color: #929aab;font-size='20px'"
									class="obtain generate_code" value="获取验证码"
									onclick="getCode(this);" style="width: 120px"><span
									id="code_msg" style="color: #FF0000"></span></td>
							</tr>
							<tr>
								<td align="right"><font color="#7d7d7d"><strong>登录密码:</td>
								<td colspan=2><input id="password" name="password"
									value="${password}" style="height: 20px; width: 300px;"
									placeholder="" type="password" size="25" /><span
									id="password_msg" style="color: #FF0000"></span></td>
							</tr>
							<tr>
								<td align="right"><font color="#7d7d7d"><strong>确认密码:</td>
								<td colspan=2><input id="repassword" name="repassword"
									value="${password}" style="height: 20px; width: 300px;"
									placeholder="" type="password" size="25" /><span
									id="repassword_msg" style="color: #FF0000"></span></td>
							</tr>
							<tr>
								<td align="right"><font color="#7d7d7d"></td>
								<td colspan=2><input id="xy" name="xy" value=""
									style="height: 13px; width: 20px;" placeholder=""
									type="checkbox" /><span><font color="#7d7d7d" size="2"><strong>已阅读同意<span
												style="color: #FF0000">《用户协议》</span></strong></font></span><span id="xy_msg"
									style="color: #FF0000"></span></td>
							</tr>
							<tr>
								<td colspan=3><input type="button" id="signup_forms_submit"
									value="立即注册"
									style="background-color: #ffcd3c; border: none; height: 50px; width: 420px; border-radius: 10px; color: #929aab; font-size: 24px; color: white;">

								</td>
							</tr>
						</table>
					</center>
				</form>
			</div>
		</div>
</body>
</html>