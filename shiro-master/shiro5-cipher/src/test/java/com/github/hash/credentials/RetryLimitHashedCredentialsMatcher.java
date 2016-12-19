package com.github.hash.credentials;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher{

	private Ehcache passwordRetryCache;
	
	private final Integer total = 5;//重试总次数
	/**
	 * no args constructor
	 */
	public RetryLimitHashedCredentialsMatcher() {
		CacheManager cacheManager = CacheManager.newInstance(CacheManager.class.getClassLoader().getResource("ehcache.xml"));
		passwordRetryCache = cacheManager.getCache("passwordRetryCache");
	}

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		String username = (String) token.getPrincipal();
		Element element = passwordRetryCache.get(username);
		if (element == null) {
			element = new Element(username, new AtomicInteger(0));
			passwordRetryCache.put(element);
		}
		AtomicInteger retryCount = (AtomicInteger) element.getObjectValue();
		if (retryCount.incrementAndGet() > total) {
			throw new ExcessiveAttemptsException("密码重试5次,请1小时后重试");
		}
		boolean match = super.doCredentialsMatch(token, info);
	
		if (match) {//匹配通过
			passwordRetryCache.remove(username);
		}else {//匹配不通过
			Integer remain = total - retryCount.get();//剩余次数
			throw new ExcessiveAttemptsException("第"+retryCount+"次校验,剩余"+remain+"次机会");
		}
		
		return match;
	}
	
}
