package com.noseparte.robot.role;

import com.noseparte.robot.BaseCmd;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetRoleAttrCmd extends BaseCmd {
    public int attrId;
}
