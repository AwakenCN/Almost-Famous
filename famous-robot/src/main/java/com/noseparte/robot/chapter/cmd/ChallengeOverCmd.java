package com.noseparte.robot.chapter.cmd;

import com.noseparte.robot.BaseCmd;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Noseparte
 * @date 2019/9/17 10:15
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class ChallengeOverCmd extends BaseCmd {

    public Integer chapterId;
    public Integer schoolId;
    public Integer state;

}
