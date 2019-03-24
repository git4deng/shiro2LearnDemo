package com.david.shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/shiro")
public class LoginHandler {
	//private static final transient Logger log = LoggerFactory.getLogger(LoginHandler.class);
	@RequestMapping("/login")
	public String Login(@RequestParam("username")String username,@RequestParam("password" )String password){
		
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		//判断用户是否被认证
		if (!currentUser.isAuthenticated()) {
			//没有登陆，把用户名和密码封装为UsernamePasswordToken对象。
			UsernamePasswordToken token = new UsernamePasswordToken(username,password);
			//记住我
			token.setRememberMe(true);
			try {
				//执行登陆。
				currentUser.login(token);
			//log.info("====================================>登陆成功！");
			}catch (AuthenticationException ae) {
				System.out.println("登陆失败："+ae.getMessage());
			}
		}
		return "redirect:/views/list.jsp";
	}
}
