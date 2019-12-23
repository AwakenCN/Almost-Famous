package com.noseparte.robot.cardpackage;

import com.noseparte.robot.BaseCmd;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Noseparte
 * @date 2019/9/17 11:17
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CardLockCmd extends BaseCmd {

    public String cardIds;
    public Integer state;

}
