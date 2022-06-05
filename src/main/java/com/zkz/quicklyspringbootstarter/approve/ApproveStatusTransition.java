package com.zkz.quicklyspringbootstarter.approve;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class ApproveStatusTransition {
    private ApproveStatus form;
    private ApproveAction action;
    private ApproveStatus to;
}
