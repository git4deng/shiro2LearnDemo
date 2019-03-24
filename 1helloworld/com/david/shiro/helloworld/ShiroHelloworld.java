package com.david.shiro.helloworld;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ShiroHelloworld {

	private static final transient Logger log = LoggerFactory.getLogger(ShiroHelloworld.class);

	public static void main(String[] args) {

		
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
		
		SecurityManager securityManager =factory.getInstance();

		SecurityUtils.setSecurityManager(securityManager);
		//��ȡ��ǰ��subject������SecurityUtils.getSubject();
		Subject currentUser = SecurityUtils.getSubject();
		//����ʹ��session
		//��ȡsession������subject��getSession����
		Session session = currentUser.getSession();
		//
		session.setAttribute("someKey", "aValue");
		
		String value = (String) session.getAttribute("someKey");
		
		if (value.equals("aValue")) {
			log.info("====================================>Retrieved the correct value! [" + value + "]");
		}
		//���Ե�ǰ�û��Ƿ��Ѿ�����֤���Ƿ��Ѿ���½������subject.isAuthenticated����������֤
		if (!currentUser.isAuthenticated()) {
			//û�е�½�����û����������װΪUsernamePasswordToken����
			UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");
			//��ס��
			token.setRememberMe(true);
			try {
				//ִ�е�½��
				currentUser.login(token);
				log.info("====================================>��½�ɹ���");
			} catch (UnknownAccountException uae) {
				//���û��ָ���û���shiro�����׳�UnknownAccountException
				log.info("====================================>δ֪�û����� " + token.getPrincipal());
				return;
			} catch (IncorrectCredentialsException ice) {
				//�����˻����ڣ����벻ƥ�����׳�IncorrectCredentialsException�쳣
				log.info("====================================>���벻��ȷ��");
				return;
			} catch (LockedAccountException lae) {
				//�û��������쳣
				log.info("The account for username " + token.getPrincipal() + " is locked.  "
						+ "Please contact your administrator to unlock it.");
			}catch (AuthenticationException ae) {
				//AuthenticationException ������֤�쳣�ĸ���
			}
		}
		log.info("+++++++++++++++++++++++++++++++++++User [" + currentUser.getPrincipal() + "] logged in successfully.");

		//�����û��Ƿ���ĳһ����ɫ
		if (currentUser.hasRole("schwartz")) {
			log.info("++++++++++++++++���û���"+currentUser.getPrincipal()+"�н�ɫ��schwartz��");
		} else {
			log.info("++++++++++++++++���û���"+currentUser.getPrincipal()+"û�н�ɫ��schwartz��");
		}
		// �����û��Ƿ�߱�ĳһ����Ϊ������currentUser.isPermitted������һ�������ֲ��� * ��ͨ��������� ��lightsaber�����κ���Ϊ
		if (currentUser.isPermitted("lightsaber:weild")) {
			log.info("====================================>You may use a lightsaber ring.  Use it wisely.");
		} else {
			log.info("Sorry, lightsaber rings are for schwartz masters only.");
		}
		// �����û��Ƿ�߱�ĳһ����Ϊ������currentUser.isPermitted�������������ϸ���ȵ�Ȩ�޿���
		if (currentUser.isPermitted("winnebago:drive:eagle5")) {
			log.info("You are permitted to 'drive' the winnebago with license plate (id) 'eagle5'.  "
					+ "Here are the keys - have fun!");
		} else {
			log.info("Sorry, you aren't allowed to drive the 'eagle5' winnebago!");
		}
		//�ǳ�����
		currentUser.logout();
		System.exit(0);
	}

}
