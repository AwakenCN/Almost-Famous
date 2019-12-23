package com.noseparte.robot.school;

import com.noseparte.robot.BaseCmd;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Noseparte
 * @date 2019/9/17 11:52
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class DeleteCardGroupCmd extends BaseCmd {

    public int schoolId;
    public int groupId;

}
