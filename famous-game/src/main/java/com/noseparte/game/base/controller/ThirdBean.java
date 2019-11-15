package com.noseparte.game.base.controller;/**
 * Created by Enzo Cotter on 2019/11/15.
 */

import lombok.Data;

/**
 * @Auther: Noseparte
 * @Date: 2019/11/15 11:23
 * @Description: <p></p>
 */
@Data
public class ThirdBean {

    private String token;
    private String sig;
    private String cses;
    private long pushTick;
    private long expire;
    private long expireTick = pushTick + expire;

}
