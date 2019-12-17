package com.noseparte.robot.login;

import com.noseparte.robot.BaseCmd;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterCmd extends BaseCmd {
    public String account;
    public String password;
}
