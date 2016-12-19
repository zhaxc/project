package com.github;

import junit.framework.Assert;
import org.apache.shiro.authz.UnauthorizedException;
import org.junit.Test;

import java.util.Arrays;

/**
 *
 * @author zhaxc
 * @date 2016年12月19日 上午9:37:02
 * @version 1.0
 */
public class RoleTest extends BaseTest {

    @Test
    public void testHasRole() {
        login("classpath:shiro-role.ini", "zhang", "123");
        //判断拥有角色：role1
        Assert.assertTrue(getSubject().hasRole("role1"));
        //判断拥有角色：role1 and role2
        Assert.assertTrue(getSubject().hasAllRoles(Arrays.asList("role1", "role2")));
        //判断拥有角色：role1 and role2 and !role3
        boolean[] result = getSubject().hasRoles(Arrays.asList("role1", "role2", "role3"));
        Assert.assertEquals(true, result[0]);
        Assert.assertEquals(true, result[1]);
        Assert.assertEquals(false, result[2]);
    }

    @Test(expected = UnauthorizedException.class)
    public void testCheckRole() {
        login("classpath:shiro-role.ini", "zhang", "123");
        //断言拥有角色：role1
        getSubject().checkRole("role1");
        //断言拥有角色：role1 and role3 失败抛出异常
        getSubject().checkRoles("role1", "role3");
    }

}
