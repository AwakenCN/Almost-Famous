package com.noseparte.common.global;


import lombok.extern.slf4j.Slf4j;
import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * @author Noseparte
 * @date 2019/8/8 18:06
 * @Description
 */
@Slf4j
public class ExitHandler implements SignalHandler {

    public void registerSignal (String signalName){
        Signal signal = new Signal(signalName);
        Signal.handle(signal, this);
    }


    @Override
    public void handle(Signal signal) {

        if ("TERM".equals(signal.getName())) {
            //
            log.error("TERM");
        } else if (signal.getName().equals("INT") || signal.getName().equals("HUP")) {
            //
            log.error("INT || HUP");
        } else {
            //
            log.error("ExitHandler");
        }

    }
}
