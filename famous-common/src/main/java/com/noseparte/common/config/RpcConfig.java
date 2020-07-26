package com.noseparte.common.config;

import com.noseparte.common.rpc.GrpcClient;
import com.noseparte.common.rpc.RpcClient;
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

    @Bean
    public RpcClient rpcClient() {
        return new GrpcClient(host, port);
    }
}

