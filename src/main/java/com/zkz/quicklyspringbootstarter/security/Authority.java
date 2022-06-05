package com.zkz.quicklyspringbootstarter.security;


public class Authority {
    /**
     * 权限判断EL表达式
     */
    private static final String PREFIX = "hasAuthority('";
    private static final String POSTFIX = "')";

    /**
     * 管理员--超级管理员
     */
    public static final String ADMIN_ROOT = PREFIX + Role.ADMIN_ROOT + POSTFIX;
    /**
     * 管理员-经理
     */
    public static final String ADMIN_MANAGER = PREFIX + Role.ADMIN_MANAGER + POSTFIX;
    /**
     * 管理员-组长
     */
    public static final String ADMIN_GROUP_LEADER = PREFIX + Role.ADMIN_GROUP_LEADER + POSTFIX;
    /**
     * 管理员-组员
     */
    public static final String ADMIN_GROUP_MEMBER = PREFIX + Role.ADMIN_GROUP_MEMBER + POSTFIX;
    /**
     * 运维
     */
    public static final String OPERATOR_GENERAL = PREFIX + Role.OPERATOR_GENERAL + POSTFIX;
    /**
     * 普通业主
     */
    public static final String PARTNER = PREFIX + Role.PARTNER + POSTFIX;
    /**
     * 一级用户
     */
    public static final String USER_LEVEL_ONE = PREFIX + Role.USER_LEVEL_ONE + POSTFIX;
    /**
     * 登录后的普通用户
     */
    public static final String USER_GENERAL = PREFIX + Role.USER_GENERAL + POSTFIX;
}
