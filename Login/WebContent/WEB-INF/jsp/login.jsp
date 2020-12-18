<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="css/common_form.css" type="text/css"></link>
<link rel="stylesheet" href="js/layui/css/layui.css" type="text/css"></link>

<script src="js/jquery1.10.2.min.js"></script>
<script type="text/javascript" src="js/layui/layui.js"></script>
</head>
<body>
	<header>
	<div class="header-line"></div>
	</header>
	<div class="content">
		<img class="content-logo" src="img/login.png" alt="logo">
		<h1 class="content-title">登录</h1>
		<div class="content-form">
			<form method="post" name="loginForm" action="">
				<div id="change_margin_1">
					<input class="user" type="text" name="userName" id="userName"
						placeholder="请输入用户名" onblur="oBlur_1()" onfocus="oFocus_1()">
				</div>
				<!-- input的value为空时弹出提醒 -->
				<p id="remind_1"></p>
				<div id="change_margin_2">
					<input class="password" type="password" name="userPwd" id="userPwd"
						placeholder="请输入密码" onblur="oBlur_2()" onfocus="oFocus_2()">
				</div>
				<!-- input的value为空时弹出提醒 -->
				<p id="remind_2"></p>
				<div id="change_margin_3">
					<input class="content-form-signup" type="button" onclick="login();"
						value="登录"> <input class="content-form-signup"
						type="button" id="register" value="注册">
				</div>
			</form>
		</div>
	</div>

	<script type="text/javascript">
		$("#register").click(function() {
			window.location.href = "register";
		});
		// 用户框失去焦点后验证value值
		function oBlur_1() {
			var userName = $('#userName').val();

			if (!$.trim(userName)) { //用户框value值为空
				document.getElementById("remind_1").innerHTML = "请输入用户名！";
				document.getElementById("change_margin_1").style.marginBottom = 1 + "px";
			} else { //用户框value值不为空
				document.getElementById("remind_1").innerHTML = "";
				document.getElementById("change_margin_1").style.marginBottom = 19 + "px";
			}
		}

		// 密码框失去焦点后验证value值
		function oBlur_2() {
			var userPwd = $('#userPwd').val();

			if (!$.trim(userPwd)) { //密码框value值为空
				document.getElementById("remind_2").innerHTML = "请输入密码！";
				document.getElementById("change_margin_2").style.marginBottom = 1 + "px";
				document.getElementById("change_margin_3").style.marginTop = 2 + "px";
			} else { //密码框value值不为空
				document.getElementById("remind_2").innerHTML = "";
				document.getElementById("change_margin_2").style.marginBottom = 19 + "px";
				document.getElementById("change_margin_3").style.marginTop = 19 + "px";
			}
		}

		// 用户框获得焦点的隐藏提醒
		function oFocus_1() {
			document.getElementById("remind_1").innerHTML = "";
			document.getElementById("change_margin_1").style.marginBottom = 19 + "px";
		}

		// 密码框获得焦点的隐藏提醒
		function oFocus_2() {
			document.getElementById("remind_2").innerHTML = "";
			document.getElementById("change_margin_2").style.marginBottom = 19 + "px";
			document.getElementById("change_margin_3").style.marginTop = 19 + "px";
		}

		//登录验证
		function login() {
			var userName = $('#userName').val();
			var userPwd = $('#userPwd').val();
			if (!$.trim(userName)) { //用户框value值为空

				layer.msg("用户名不能为空", {
					icon : 2,
					time : 1000
				}, function() {

				});
				return;
			}
			if (!$.trim(userPwd)) { //密码框value值为空

				layer.msg("密码不能为空", {
					icon : 2,
					time : 1000
				}, function() {

				});
				return;
			}
			$.ajax({
				url : 'loginCon', //换成自己的url
				type : 'POST',
				dataType : 'json',
				data : {
					'userName' : userName,
					'userPwd' : userPwd
				},
				success : function(res) {
					if (res.msg == 0) {
						window.location.href = "success";
					} else {
						layer.msg("登录失败,用户名 或 密码 错误", {
							icon : 2,
							time : 2000
						}, function() {

						});
					}
				},
				error : function(e) {
					layer.msg("登录失败,用户名 或 密码 错误", {
						icon : 2,
						time : 2000
					}, function() {

					});
				}
			});
		}
	</script>
	<script>
		layui.use("layer", function() {
			layui.use([ 'layer', 'laydate', 'form' ], function() {
			});
		});
	</script>
</body>
</html>
