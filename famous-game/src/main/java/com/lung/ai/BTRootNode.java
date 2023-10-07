package com.lung.ai;

public class BTRootNode extends BTNode {
    private BTNode child;

    public BTRootNode(BTNode child) {
        this.child = child;
    }

    public BTNodeStatus update() {
        return child.update();
    }
}
