package com.zkz.quicklyspringbootstarter.approve;

/**
 * 审核状态
 */
public enum ApproveStatus {
    /**
     * 初始状态,未提交审核
     */
    INITIAL,

    /**
     * 已提交审核
     */
    SUBMITTED,

    /**
     * 被驳回
     */
    REJECTED,

    /**
     * 通过
     */
    PASSED
}
