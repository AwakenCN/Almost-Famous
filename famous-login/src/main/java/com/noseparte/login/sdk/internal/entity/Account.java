package com.noseparte.login.sdk.internal.entity;

import com.noseparte.common.db.pojo.GeneralBean;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Noseparte
 * @date 2019/8/12 18:09
 * @Description
 */
@Data
@Document(collection = "famous-login-account")
public class Account extends GeneralBean {

    private Long uid;

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    private String token;
}
