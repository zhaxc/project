package com.github;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;

/**
 * 
 * @author zhaxc
 * @date 2016年12月19日 上午9:28:25
 * @version 1.0
 */
public class BaseTest {
	
	@After
	public void tearDown(){
		//退出时解除绑定到线程 否则对下次测试造成
		ThreadContext.unbindSubject();
	}
	
	protected void login(String configFile,String username,String password){
		//1.获取SecurityManager工厂，此处使用ini配置文件初始化SecurityManager
		Factory<SecurityManager> factory = new IniSecurityManagerFactory(configFile);
		
		//获得securityManager实例并绑定给SecurityUtils
		SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
		
		//得到主体 subject 创建用户名/密码身份验证token
		Subject subject =  getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username,password);
		
		//登陆
		subject.login(token);
		
	}
	
	public Subject getSubject() {
		return SecurityUtils.getSubject();
	}
	
}
