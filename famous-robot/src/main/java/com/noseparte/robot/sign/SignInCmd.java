package com.noseparte.robot.sign;

import com.noseparte.robot.BaseCmd;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Noseparte
 * @date 2019/9/17 11:00
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class SignInCmd extends BaseCmd {

    public Integer day;

}
