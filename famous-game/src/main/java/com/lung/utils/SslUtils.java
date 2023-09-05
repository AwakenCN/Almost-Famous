package com.lung.utils;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;

/**
 * @author noseparte
 * @version 1.0
 * @implSpec 创建ssl证书工具类
 * @since 2023/9/5 - 22:35
 */
public class SslUtils {

    private final static Logger logger = LoggerFactory.getLogger(SslUtils.class);
    private final static String KEYSTORE_PASSWORD = "noseparte"; // Keystore 密码

    /**
     * 1. 准备 Keystore：
     * @return keyStore
     * @throws Exception 抛出异常由业务端处理
     */
    private static KeyStore createKeystore() throws Exception {
        //
        String fileName = "/almost-famous.keystore"; // CA 证书文件路径
        File file = new File(CommonUtils.getClassPath() + fileName);
        String keystorePath = file.getPath();  // Keystore 文件路径
        logger.info("ssl 证书路径：{}", keystorePath);

        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (InputStream inputStream = Files.newInputStream(Paths.get(keystorePath))) {
            keyStore.load(inputStream, KEYSTORE_PASSWORD.toCharArray());
        }

        return keyStore;
    }

    /**
     * 2. 创建 KeyManagerFactory：
     * @return KeyManagerFactory
     * @throws Exception 抛出异常由业务端处理
     */
    private static KeyManagerFactory createKeyManagerFactory(KeyStore keyStore) throws Exception {
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, KEYSTORE_PASSWORD.toCharArray());
        return kmf;
    }

    /**
     * 3. 创建 TrustManagerFactory（用于服务器验证客户端证书）：
     * @return TrustManagerFactory
     * @throws Exception 抛出异常由业务端处理
     */
    private static TrustManagerFactory createTrustManagerFactory(KeyStore keyStore) throws Exception {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);
        return tmf;
    }

    /**
     * 服务端ssl证书
     * @return {@link io.netty.handler.ssl.SslContext}
     * @throws Exception 抛出异常由业务端处理
     */
    public static SslContext createServerSslContext() throws Exception {
        KeyStore keyStore = createKeystore();
        KeyManagerFactory kmf = createKeyManagerFactory(keyStore);
        TrustManagerFactory tmf = createTrustManagerFactory(keyStore);
        return SslContextBuilder.forServer(kmf).trustManager(tmf).build();
    }

    /**
     * 客户端ssl证书
     * @return {@link io.netty.handler.ssl.SslContext}
     * @throws Exception 抛出异常由业务端处理
     */
    public static SslContext createClientSslContext() throws Exception {
        KeyStore keyStore = createKeystore();
        TrustManagerFactory tmf = createTrustManagerFactory(keyStore);
        return SslContextBuilder.forClient().trustManager(tmf).build();
    }

}
