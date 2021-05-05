package com.djmanong.mall.constant;

/**
 * Redis缓存的key
 * @author DjManong
 */
public class SystemCacheConstant {

    /**系统菜单*/
    public static final String CATEGORY_MENU_CACHE_KEY = "sys_category";

    /**
     * 登录的用户(redis)key login:member:token={userObj}
     */
    public static final String LOGIN_MEMBER = "login:member:";

    public static final Long LOGIN_MEMBER_TIMEOUT = 30 * 60L;
}
