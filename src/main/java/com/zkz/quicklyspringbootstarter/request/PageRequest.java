package com.zkz.quicklyspringbootstarter.request;


import com.zkz.quicklyspringbootstarter.utils.StringUtils;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Data
public class PageRequest {
    // 不分页时的分页大小. 此值仅由后端使用
    public static final short NO_PAGE_SIZE = Short.MAX_VALUE;
    // 前端能指定的 最大分页大小
    public static final short MAX_PAGE_SIZE = 1000;
    // 最大的模糊查询长度
    public static final byte MAX_KEYWORD_LENGTH = 100;


    @Range(min = 1)
    private Integer pageNo = 1;
    @Range(min = 1, max = MAX_PAGE_SIZE)
    private Integer pageSize = 20;

    @Length(max = 100)
    private String orderBy;

    @Length(max = MAX_KEYWORD_LENGTH)
    private String keyword;


    /**
     * 获取关键字,两端都会添加'%'
     *
     * @return 如果原来是null或空字符串, 则返回null;否则对其两端添加'%'并返回
     */
    public String getKeywordWithPercent() {
        return StringUtils.isBlank(keyword) ? null : '%' + keyword.trim() + '%';
    }
}
