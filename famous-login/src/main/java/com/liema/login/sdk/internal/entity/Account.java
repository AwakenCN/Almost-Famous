package com.liema.login.sdk.internal.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Noseparte
 * @date 2019/8/12 18:09
 * @Description
 */
@Data
@Document(collection = "famous-login-account")
public class Account {

    private Long uid;

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 创建时间
     */
    private Long createTime;

    private String token;
}
