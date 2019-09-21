package com.noseparte.unique.service;

import com.noseparte.common.rpc.protocol.RPCDateService;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.thrift.TException;

import java.util.Date;

/**
 * @author Noseparte
 * @date 2019/8/8 10:15
 * @Description
 */
public class RPCDataServiceImpl implements RPCDateService.Iface {

    @Override
    public String getDate(String userName) throws TException {
        String now = DateFormatUtils.format(new Date(), "北京时间： YYYY年-MM月-DD日 E hh时:mm分:ss秒");
        return "Hello: " + userName + "=====================" + now;
    }

}
