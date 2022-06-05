package com.zkz.quicklyspringbootstarter.apiResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PageResponse extends SuccessResponse {
    private Integer pageNo;
    private Integer pageSize;
    private Long total;

    public PageResponse(Iterable<?> data, Integer pageNo, Integer pageSize, Long total) {
        super(data);
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.total = total;
    }
}
