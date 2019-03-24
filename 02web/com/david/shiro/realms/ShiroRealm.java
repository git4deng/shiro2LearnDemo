package com.david.shiro.realms;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.AuthenticatingRealm;

public class ShiroRealm extends AuthenticatingRealm {

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("doGetAuthenticationInfo:"+token);
		//1. �� AuthenticationToken ת��Ϊ UsernamePasswordToken
		UsernamePasswordToken upToken=(UsernamePasswordToken) token;
		//2. �� UsernamePasswordToken ������ȡ username
		String username = upToken.getUsername();
		//3. �������ݿ�ķ���, �����ݿ��в�ѯ username ��Ӧ���û���¼
		System.out.println("�����ݿ��в�ѯ�û���Ϣ��"+username);
		//4. ���û�������, ������׳� UnknownAccountException �쳣
		if("unknown".equals(username))
			throw new UnknownAccountException("�û������ڣ�");
		
		//5. �����û���Ϣ�����, �����Ƿ���Ҫ�׳������� AuthenticationException �쳣. 
		if("monster".equals(username))
			throw new LockedAccountException("�û���������");
		//6. �����û������, ������ AuthenticationInfo ���󲢷���.ͨ��ʹ�õ�ʵ����Ϊ: SimpleAuthenticationInfo
		//������Ϣ�Ǵ����ݿ��л�ȡ��.
		//1). principal: ��֤��ʵ����Ϣ. ������ username, Ҳ���������ݱ��Ӧ���û���ʵ�������. 
		Object principal=username;
		//2). credentials: ����. 
		Object credentials="123456";
		//3). realmName: ��ǰ realm ����� name. ���ø���� getName() ��������
		String realmName = getName();
		SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(principal, credentials, realmName);
		return info;
	}

	

}
