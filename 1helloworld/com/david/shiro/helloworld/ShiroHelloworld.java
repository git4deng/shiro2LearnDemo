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
		//获取当前的subject，调用SecurityUtils.getSubject();
		Subject currentUser = SecurityUtils.getSubject();
		//测试使用session
		//获取session：调用subject的getSession方法
		Session session = currentUser.getSession();
		//
		session.setAttribute("someKey", "aValue");
		
		String value = (String) session.getAttribute("someKey");
		
		if (value.equals("aValue")) {
			log.info("====================================>Retrieved the correct value! [" + value + "]");
		}
		//测试当前用户是否已经被认证即是否已经登陆，调用subject.isAuthenticated方法进行验证
		if (!currentUser.isAuthenticated()) {
			//没有登陆，把用户名和密码封装为UsernamePasswordToken对象。
			UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");
			//记住我
			token.setRememberMe(true);
			try {
				//执行登陆。
				currentUser.login(token);
				log.info("====================================>登陆成功！");
			} catch (UnknownAccountException uae) {
				//如果没有指定用户，shiro将会抛出UnknownAccountException
				log.info("====================================>未知用户名： " + token.getPrincipal());
				return;
			} catch (IncorrectCredentialsException ice) {
				//若果账户存在，密码不匹配则抛出IncorrectCredentialsException异常
				log.info("====================================>密码不正确！");
				return;
			} catch (LockedAccountException lae) {
				//用户被锁定异常
				log.info("The account for username " + token.getPrincipal() + " is locked.  "
						+ "Please contact your administrator to unlock it.");
			}catch (AuthenticationException ae) {
				//AuthenticationException 所有认证异常的父类
			}
		}
		log.info("+++++++++++++++++++++++++++++++++++User [" + currentUser.getPrincipal() + "] logged in successfully.");

		//测试用户是否有某一个角色
		if (currentUser.hasRole("schwartz")) {
			log.info("++++++++++++++++》用户："+currentUser.getPrincipal()+"有角色：schwartz！");
		} else {
			log.info("++++++++++++++++》用户："+currentUser.getPrincipal()+"没有角色：schwartz！");
		}
		// 测试用户是否具备某一个行为，调用currentUser.isPermitted方法，一下配置种采用 * 的通配符，所有 对lightsaber允许任何行为
		if (currentUser.isPermitted("lightsaber:weild")) {
			log.info("====================================>You may use a lightsaber ring.  Use it wisely.");
		} else {
			log.info("Sorry, lightsaber rings are for schwartz masters only.");
		}
		// 测试用户是否具备某一个行为，调用currentUser.isPermitted方法，比上面更细粒度的权限控制
		if (currentUser.isPermitted("winnebago:drive:eagle5")) {
			log.info("You are permitted to 'drive' the winnebago with license plate (id) 'eagle5'.  "
					+ "Here are the keys - have fun!");
		} else {
			log.info("Sorry, you aren't allowed to drive the 'eagle5' winnebago!");
		}
		//登出操作
		currentUser.logout();
		System.exit(0);
	}

}
