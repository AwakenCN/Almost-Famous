package com.noseparte.robot.mission;

import com.noseparte.robot.BaseCmd;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Noseparte
 * @date 2019/9/17 10:53
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class FinishCmd extends BaseCmd {

    public Integer missionId;
    
}
