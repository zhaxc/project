package com.github;

import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.junit.Test;

import junit.framework.Assert;


public class PermissionTest extends BaseTest {

    @Test
    public void testIsPermitted() {
        login("classpath:shiro-permission.ini", "zhang", "123");
        //判断拥有权限：user:create
        Assert.assertTrue(getSubject().isPermitted("user:create"));
        //判断拥有权限：user:update and user:delete
        Assert.assertTrue(getSubject().isPermittedAll("user:update", "user:delete"));
        //判断没有权限：user:view
        Assert.assertFalse(getSubject().isPermitted("user:view"));
    }

    @Test(expected = UnauthorizedException.class)
    public void testCheckPermission() {
        login("classpath:shiro-permission.ini", "zhang", "123");
        //断言拥有权限：user:create
        getSubject().checkPermission("user:create");
        //断言拥有权限：user:delete and user:update
        getSubject().checkPermissions("user:delete", "user:update");
        //断言拥有权限：user:view 失败抛出异常
        getSubject().checkPermissions("user:view");
    }


    @Test
    public void testWildcardPermission1() {
        login("classpath:shiro-permission.ini", "li", "123");

        getSubject().checkPermissions("system:user:update", "system:user:delete");
        getSubject().checkPermissions("system:user:update,delete");
    }

    @Test
    public void testWildcardPermission2() {
        login("classpath:shiro-permission.ini", "li", "123");
        getSubject().checkPermissions("system:user:create,delete,update:view");

        getSubject().checkPermissions("system:user:*");
        getSubject().checkPermissions("system:user");
    }

    @Test
    public void testWildcardPermission3() {
        login("classpath:shiro-permission.ini", "li", "123");
        getSubject().checkPermissions("user:view");

        getSubject().checkPermissions("system:user:view");
    }

    @Test
    public void testWildcardPermission4() {
        login("classpath:shiro-permission.ini", "li", "123");
        getSubject().checkPermissions("user:view:1");

        getSubject().checkPermissions("user:delete,update:1");
        getSubject().checkPermissions("user:update:1", "user:delete:1");

        getSubject().checkPermissions("user:update:1", "user:delete:1", "user:view:1");

        getSubject().checkPermissions("user:auth:1", "user:auth:2");

    }

    @Test
    public void testWildcardPermission5() {
        login("classpath:shiro-permission.ini", "li", "123");
        getSubject().checkPermissions("menu:view:1");

        getSubject().checkPermissions("organization");
        getSubject().checkPermissions("organization:view");
        getSubject().checkPermissions("organization:view:1");

    }


    @Test
    public void testWildcardPermission6() {
        login("classpath:shiro-permission.ini", "li", "123");
        getSubject().checkPermission("menu:view:1");
        getSubject().checkPermission(new WildcardPermission("menu:view:1"));

    }



}
