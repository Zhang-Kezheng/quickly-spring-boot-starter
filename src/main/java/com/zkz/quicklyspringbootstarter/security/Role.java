package com.zkz.quicklyspringbootstarter.security;

public class Role {
    /**
     * 管理员-超级管理员
     */
    public static final String ADMIN_ROOT = "1000000000000000";

    /**
     * 管理员-经理
     */
    public static final String ADMIN_MANAGER = "0100000000000000";

    /**
     * 管理员-组长
     */
    public static final String ADMIN_GROUP_LEADER = "0010000000000000";

    /**
     * 管理员-组员
     */
    public static final String ADMIN_GROUP_MEMBER = "0001000000000000";

    /**
     * 运维
     */
    public static final String OPERATOR_GENERAL = "0000000100000000";

    /**
     * 普通业主
     */
    public static final String PARTNER = "0000000000010000";

    /**
     * 一级用户
     */
    public static final String USER_LEVEL_ONE = "0000000000001000";

    /**
     * 登录后的普通用户
     */
    public static final String USER_GENERAL = "0000000000000001";

    /**
     * 游客的token
     */
    public static final String VISITOR = "0000000000000000";
}
