package com.zkz.quicklyspringbootstarter.security;

import com.zkz.quicklyspringbootstarter.exception.BaseException;
import com.zkz.quicklyspringbootstarter.jwt.Token;
import com.zkz.quicklyspringbootstarter.jwt.TokenUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 角色工具类
 * <p>
 * 角色 0000 0000 0000 0000
 * 从左到右分为4组，左1管理员,左2预留,左3为政府或合作方，左4普通个人
 * 角色具有层级关系,上级角色拥有下级角色的权限,比如"普通管理员"可以访问"普通个人"的接口
 * <p>
 * 目前每一位的使用:
 * 1 超级管理员
 * 2 高级管理员
 * 3 普通管理员-项目组长
 * 4 普通管理员-项目组员
 * 11 政府组织
 * 12 普通合作方
 * 16 普通个人
 */
public class RoleUtils {
    /**
     * 为了代码更加清晰
     * 已经准备废弃此类中的角色和权限相关常量
     * 替代者是{@link Role}和{@link Authority}
     */
    //权限判断EL表达式
    @Deprecated
    private static final String HAS_AUTHORITY_PREFIX = "hasAuthority('";
    @Deprecated
    private static final String HAS_AUTHORITY_POSTFIX = "')";

    //管理员-经理 !!!已废弃,详见上面注释!!!
    @Deprecated
    public static final String ADMIN_MANAGER = "0100000000000000";
    //管理员-组长 !!!已废弃,详见上面注释!!!
    @Deprecated
    public static final String ADMIN_GROUP_LEADER = "0010000000000000";
    //管理员-组员 !!!已废弃,详见上面注释!!!
    @Deprecated
    public static final String ADMIN_GROUP_MEMBER = "0001000000000000";
    //普通业主 !!!已废弃,详见上面注释!!!
    @Deprecated
    public static final String PARTNER = "0000000000010000";
    //登录后的普通用户 !!!已废弃,详见上面注释!!!
    @Deprecated
    public static final String USER_GENERAL = "0000000000000001";


    //管理员-经理 !!!已废弃,详见上面注释!!!
    @Deprecated
    public static final String AUTHORITY_ADMIN_MANAGER = HAS_AUTHORITY_PREFIX + ADMIN_MANAGER + HAS_AUTHORITY_POSTFIX;
    //管理员-组长 !!!已废弃,详见上面注释!!!
    @Deprecated
    public static final String AUTHORITY_ADMIN_GROUP_LEADER = HAS_AUTHORITY_PREFIX + ADMIN_GROUP_LEADER + HAS_AUTHORITY_POSTFIX;
    //管理员-组员 !!!已废弃,详见上面注释!!!
    @Deprecated
    public static final String AUTHORITY_ADMIN_GROUP_MEMBER = HAS_AUTHORITY_PREFIX + ADMIN_GROUP_MEMBER + HAS_AUTHORITY_POSTFIX;
    //普通业主 !!!已废弃,详见上面注释!!!
    @Deprecated
    public static final String AUTHORITY_PARTNER = HAS_AUTHORITY_PREFIX + PARTNER + HAS_AUTHORITY_POSTFIX;
    //登录后的普通用户 !!!已废弃,详见上面注释!!!
    @Deprecated
    public static final String AUTHORITY_USER_GENERAL = HAS_AUTHORITY_PREFIX + USER_GENERAL + HAS_AUTHORITY_POSTFIX;

    //角色总长度
    private static final int TOTAL_SIZE = 16;
    //每段角色长度
    private static final int GROUP_SIZE = 4;
    //管理员组开始坐标
    private static final int ADMIN_INDEX = 0;
    //业主组开始坐标
    private static final int PARTNER_INDEX = 8;


    /**
     * 判断是否是管理员
     *
     * @return 属于任何阶级的管理员都会返回true, 否则false
     */
    public static boolean isAdmin() {
        String role = getRoleCode();
        return role.substring(ADMIN_INDEX, ADMIN_INDEX + GROUP_SIZE).contains("1");
    }

    /**
     * 判断是否是业主
     *
     * @return 属于任何阶级的管理员都会返回true, 否则false
     */
    public static boolean isPartner() {
        String role = getRoleCode();
        return role.substring(PARTNER_INDEX, PARTNER_INDEX + GROUP_SIZE).contains("1");
    }

    /**
     * 判断是否是登录用户
     *
     * @return 任何用户登录后返回true, 未登陆的返回false
     */
    public static boolean isLogin() {
        return !TokenUtils.getToken().isVisitorToken();
    }

    /**
     * 获取当前请求用户的角色
     */
    private static String getRoleCode() {
        Token token = TokenUtils.getToken();
        if (token == null) {
            return "";
        }
        String roleCode = token.getRoleCode();
        if (checkRoleFormat(roleCode)) {
            return roleCode;
        } else {
            return "";
        }
    }

    /**
     * 检查角色字符正确性
     */
    public static boolean checkRoleFormat(String role) {
        if (StringUtils.isBlank(role)) {
            return false;
        }
        return role.matches("^[01]{" + TOTAL_SIZE + "}$");
    }

    /**
     * 判断第一个角色和第二个角色的级别高低
     *
     * @param firstRole  第一个角色
     * @param secondRole 第二个角色
     * @return 第一个角色大于第二个角色返回1，等于返回0，小于返回-1
     */
    public static int compare(String firstRole, String secondRole) {
        if (!checkRoleFormat(firstRole) || !checkRoleFormat(secondRole)) {
            throw new BaseException("0500");
        }
        // 注意这里的取反操作: 权限高 ==> indexOf("1")小 ==> Integer.compare 返回-1 ==> 取反
        return -Integer.compare(firstRole.indexOf("1"), secondRole.indexOf("1"));
    }

    /**
     * 比较权限相等
     *
     * @param firstRole  第一个角色
     * @param secondRole 第二个角色
     * @return boolean
     */
    public static boolean equals(String firstRole, String secondRole) {
        return compare(firstRole, secondRole) == 0;
    }

    /**
     * 比较权限相等
     *
     * @param targetRole 要比较的角色
     * @return boolean
     */
    public static boolean equals(String targetRole) {
        return compare(getRoleCode(), targetRole) == 0;
    }

    /**
     * 第一个权限是否小于第二个
     *
     * @param firstRole  第一个角色
     * @param secondRole 第二个角色
     * @return boolean
     */
    public static boolean lt(String firstRole, String secondRole) {
        return compare(firstRole, secondRole) == -1;
    }

    /**
     * 第一个权限是否小于第二个
     *
     * @param targetRole 要比较的角色
     * @return boolean
     */
    public static boolean lt(String targetRole) {
        return compare(getRoleCode(), targetRole) == -1;
    }

    /**
     * 第一个权限是否小于等于第二个
     *
     * @param firstRole  第一个角色
     * @param secondRole 第二个角色
     * @return boolean
     */
    public static boolean lot(String firstRole, String secondRole) {
        return compare(firstRole, secondRole) <= 0;
    }

    /**
     * 第一个权限是否小于等于第二个
     *
     * @param targetRole 要比较的角色
     * @return boolean
     */
    public static boolean lot(String targetRole) {
        return compare(getRoleCode(), targetRole) <= 0;
    }

    /**
     * 第一个权限是否大于第二个
     *
     * @param firstRole  第一个角色
     * @param secondRole 第二个角色
     * @return boolean
     */
    public static boolean gt(String firstRole, String secondRole) {
        return compare(firstRole, secondRole) == 1;
    }

    /**
     * 第一个权限是否大于第二个
     *
     * @param targetRole 要比较的角色
     * @return boolean
     */
    public static boolean gt(String targetRole) {
        return compare(getRoleCode(), targetRole) == 1;
    }

    /**
     * 第一个权限是否大于等于第二个
     *
     * @param firstRole  第一个角色
     * @param secondRole 第二个角色
     * @return boolean
     */
    public static boolean got(String firstRole, String secondRole) {
        return compare(firstRole, secondRole) >= 0;
    }

    /**
     * 第一个权限是否大于等于第二个
     *
     * @param targetRole 要比较的角色
     * @return boolean
     */
    public static boolean got(String targetRole) {
        return compare(getRoleCode(), targetRole) >= 0;
    }
}
