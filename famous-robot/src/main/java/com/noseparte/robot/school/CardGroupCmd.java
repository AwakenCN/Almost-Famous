package com.noseparte.robot.school;

import com.noseparte.robot.BaseCmd;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Noseparte
 * @date 2019/9/17 11:53
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class CardGroupCmd extends BaseCmd {

    public int schoolId;
    public int groupId;

}
