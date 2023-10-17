package com.lung.game.entry;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author noseparte
 * @implSpec 英雄类
 * @since 2023/9/25 - 15:16
 * @version 1.0
 */
@Getter
@Setter
@ToString
public class HeroEntry {

    // 英雄ID
    private int id;
    // 玩家ID
    private String uid;
    // 名字
    private String name;
    // 类型
    private String type;
    // 等级
    private String lv;
    // 星级
    private String star;
    // 经验
    private int exp;
    // 血量
    private int hp;





    @Getter
    public enum HeroType {
        POWER("力量型", 1),
        AGILE("敏捷型", 2),
        INTELLECTUAL("智力型", 3),
        ;

        HeroType(String typeName, int type) {
            this.typeName = typeName;
            this.type = type;
        }

        final String typeName;
        final int type;

        public static String getTypeName(int type) {
            for (HeroType heroType : values()) {
                if (type == heroType.getType()) {
                    return heroType.getTypeName();
                }
            }
            return "";
        }

    }



}
