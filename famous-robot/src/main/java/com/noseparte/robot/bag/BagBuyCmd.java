package com.noseparte.robot.bag;

import com.noseparte.robot.BaseCmd;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Noseparte
 * @date 2019/9/17 12:31
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BagBuyCmd extends BaseCmd {

    /**
     *
     */
    private static final long serialVersionUID = 3851000205948158841L;

    public String packages;

}

