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
		//�ж��û��Ƿ���֤
		if (!currentUser.isAuthenticated()) {
			//û�е�½�����û����������װΪUsernamePasswordToken����
			UsernamePasswordToken token = new UsernamePasswordToken(username,password);
			//��ס��
			token.setRememberMe(true);
			try {
				//ִ�е�½��
				currentUser.login(token);
			//log.info("====================================>��½�ɹ���");
			}catch (AuthenticationException ae) {
				System.out.println("��½ʧ�ܣ�"+ae.getMessage());
			}
		}
		return "redirect:/views/list.jsp";
	}
}
