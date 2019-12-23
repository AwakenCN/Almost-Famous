package com.noseparte.robot.bag;

import com.noseparte.robot.BaseCmd;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Noseparte
 * @date 2019/9/17 12:44
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BagSelectCmd extends BaseCmd {

    public String packages;

}
