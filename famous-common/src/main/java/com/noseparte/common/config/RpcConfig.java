package com.noseparte.common.config;

import com.noseparte.common.rpc.RpcClient;
import com.noseparte.common.rpc.ThriftClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "rpc")
public class RpcConfig {
    private String host;
    private int port;
    private String rpcType;

    @Bean(initMethod = "init")
    public RpcClient rpcThriftClient() {
        RpcClient rpcClient;
        if (null != rpcType && "Thrift".equals(rpcType)) {
            rpcClient = new ThriftClient();
            rpcClient.setHost(host);
            rpcClient.setPort(port);
        } else {
            rpcClient = new ThriftClient();
            rpcClient.setHost(host);
            rpcClient.setPort(port);
        }
        return rpcClient;
    }
}
