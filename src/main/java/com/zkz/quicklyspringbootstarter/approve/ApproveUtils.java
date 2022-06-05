package com.zkz.quicklyspringbootstarter.approve;

import com.zkz.quicklyspringbootstarter.exception.BaseException;
import com.zkz.quicklyspringbootstarter.exception.MiddlewareException;
import com.zkz.quicklyspringbootstarter.security.Role;
import com.zkz.quicklyspringbootstarter.security.RoleUtils;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApproveUtils {
    /**
     * 默认的审核状态迁徙
     */
    private static final List<ApproveStatusTransition> defaultTransitionList = Arrays.asList(
            // 初始-提交-已提交
            new ApproveStatusTransition(ApproveStatus.INITIAL, ApproveAction.SUBMIT, ApproveStatus.SUBMITTED),
            // 已提交-放弃-初始
            new ApproveStatusTransition(ApproveStatus.SUBMITTED, ApproveAction.CANCEL, ApproveStatus.INITIAL),
            // 已提交-驳回-已驳回
            new ApproveStatusTransition(ApproveStatus.SUBMITTED, ApproveAction.REJECT, ApproveStatus.REJECTED),
            // 已提交-通过-已通过
            new ApproveStatusTransition(ApproveStatus.SUBMITTED, ApproveAction.PASS, ApproveStatus.PASSED),
            // 已驳回-提交-已提交
            new ApproveStatusTransition(ApproveStatus.REJECTED, ApproveAction.SUBMIT, ApproveStatus.SUBMITTED),
            // 已通过-驳回-已驳回
            new ApproveStatusTransition(ApproveStatus.PASSED, ApproveAction.REJECT, ApproveStatus.REJECTED)
    );
    /**
     * 允许的状态迁移列表
     */
    private static List<ApproveStatusTransition> transitionList = defaultTransitionList;

    /**
     * 自定义设置状态迁移列表
     *
     * @param transitionList 自定义的状态迁移列表
     */
    public static void setTransitionList(List<ApproveStatusTransition> transitionList) {
        if (transitionList == null || transitionList.isEmpty()) {
            throw new MiddlewareException("0601");
        }

        ApproveUtils.transitionList = transitionList;
    }

    /**
     * 最低的可实行审核的角色
     * 默认是管理员组长
     */
    private static String lowestRoleForApprove = Role.ADMIN_GROUP_LEADER;

    /**
     * 设置最低的可实行审核的角色
     *
     * @param lowestRoleForApprove 最低的可实行审核的角色. 不可为空
     */
    public static void setLowestRoleForApprove(@NotBlank String lowestRoleForApprove) {
        ApproveUtils.lowestRoleForApprove = lowestRoleForApprove;
    }

    /**
     * 验证审核状态和操作
     *
     * @param currentStatus 当前状态
     * @param action        操作
     * @return 目标状态
     * @throws BaseException 如果不合法,则将抛异常
     */
    public static ApproveStatus getTargetApproveStatus(ApproveStatus currentStatus, ApproveAction action) {
        // 如果审核操作"不是提交审核",且当前用户低于管理员组长时
        if (!action.equals(ApproveAction.SUBMIT) && RoleUtils.lt(lowestRoleForApprove)) {
            throw new BaseException("5005");
        }

        for (ApproveStatusTransition transition : transitionList) {
            if (transition.getForm() == currentStatus && transition.getAction() == action) {
                return transition.getTo();
            }
        }
        throw new BaseException("5004");
    }

}
