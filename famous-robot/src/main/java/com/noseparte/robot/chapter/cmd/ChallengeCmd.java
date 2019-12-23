package com.noseparte.robot.chapter.cmd;

import com.noseparte.robot.BaseCmd;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Noseparte
 * @date 2019/9/17 9:49
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChallengeCmd extends BaseCmd {

    public Integer chapterId;
    public Integer schoolId;

}
