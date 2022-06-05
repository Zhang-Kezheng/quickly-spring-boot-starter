package com.zkz.quicklyspringbootstarter.tree;

import lombok.Data;

import java.util.List;

@Data
public abstract class TreeNode {
    private Long id;
    private Long parentId;
    private TreeNode parent;
    private Integer depth;
    private String path;
    private Boolean isLeaf;

    // 用于工具类组装时,存放子节点列表
    private List<TreeNode> children;
}
