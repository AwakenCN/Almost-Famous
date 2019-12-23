package com.noseparte.robot.school;

import com.noseparte.robot.BaseCmd;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Auther: Noseparte
 * @Date: 2019/10/22 11:14
 * @Description: 
 * 
 *          <p>更新卡组</p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateCardGroupCmd extends BaseCmd {

    public Integer schoolId;
    public String cardGroup;

}
