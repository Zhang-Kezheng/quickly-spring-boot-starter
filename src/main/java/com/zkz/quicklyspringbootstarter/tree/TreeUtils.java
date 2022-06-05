package com.zkz.quicklyspringbootstarter.tree;

import com.zkz.quicklyspringbootstarter.exception.MiddlewareException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TreeUtils {

    /**
     * 将列表转化为tree
     * 此方法自动寻找层级最高的节点
     *
     * @param nodeList 节点池
     * @return 最外层是列表
     */
    public static <T extends TreeNode> List<T> transformToTree(List<T> nodeList) {
        if (nodeList.isEmpty()) {
            return new ArrayList<>();
        }

        //找到最高的层级
        Integer minDepth = nodeList.parallelStream()
                .map(TreeNode::getDepth)
                .min(Integer::compareTo)
                .orElseThrow(() -> new MiddlewareException("0441"));

        //调用另一个方法进行转换
        return nodeList.stream()
                .filter(treeNode -> treeNode.getDepth().equals(minDepth))
                .map(treeNode -> transformToTree(treeNode, nodeList))
                .collect(Collectors.toList());
    }

    /**
     * 将列表转化为tree
     *
     * @param currentNode 当前节点
     * @param nodeList    节点池
     * @return 整理好的当前节点
     */
    private static <T extends TreeNode> T transformToTree(T currentNode, List<T> nodeList) {
        List<TreeNode> children = nodeList.stream()
                .filter(treeNode -> {
                    if (treeNode.getParentId() != null) {
                        return treeNode.getParentId().equals(currentNode.getId());
                    } else if (treeNode.getParent() != null && treeNode.getParent().getId() != null) {
                        return treeNode.getParent().getId().equals(currentNode.getId());
                    } else {
                        return false;
                    }
                })
                .map(struct -> transformToTree(struct, nodeList))
                .collect(Collectors.toList());
        if (!children.isEmpty()) {
            currentNode.setChildren(children);
        }
        return currentNode;
    }
}
