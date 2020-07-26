package com.noseparte.robot.gm;/**
 * Created by Enzo Cotter on 2019/12/27.
 */

import com.noseparte.robot.BaseCmd;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Auther: Noseparte
 * @Date: 2019/12/27 14:42
 * @Description: <p></p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GameMasterCmd extends BaseCmd {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1197985735278514686L;
	public Integer type;
    public String gameParams;

}
