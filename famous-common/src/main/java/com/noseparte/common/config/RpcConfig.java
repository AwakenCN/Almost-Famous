//package com.noseparte.common.config;
//
//import lombok.Data;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//@Data
//@Component
//@ConfigurationProperties(prefix = "rpc")
//public class RpcConfig {
//    private String host;
//    private int port;
//
//    @Bean
//    public RpcClient rpcClient() {
//        RpcClient rpcClient = new GrpcClient(host, port);
//        return rpcClient;
//    }
//}
