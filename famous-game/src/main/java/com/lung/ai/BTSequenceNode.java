package com.lung.ai;

import java.util.List;

public class BTSequenceNode extends BTNode {
    private List<BTNode> children;

    public BTSequenceNode(List<BTNode> children) {
        this.children = children;
    }

    public BTNodeStatus update() {
        for (BTNode child : children) {
            BTNodeStatus status = child.update();
            if (status == BTNodeStatus.FAILURE) {
                return BTNodeStatus.FAILURE;
            }
        }
        return BTNodeStatus.SUCCESS;
    }
}
