package com.zkz.quicklyspringbootstarter.approve;

/**
 * 审核动作
 */
public enum ApproveAction {
    /**
     * 提交
     */
    SUBMIT,

    /**
     * 放弃,撤销
     */
    CANCEL,

    /**
     * 通过
     */
    PASS,

    /**
     * 驳回,拒绝
     */
    REJECT
}
