package com.noseparte.common.resources;

/**
 *
 */
public class CardGroupConf implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

/********** attribute ***********/
    private Integer id;

    private String cards;

/********** constructors ***********/
    public CardGroupConf() {

    }

    public CardGroupConf(Integer id, String cards) {
        this.id = id;
        this.cards = cards;
    }

/********** get/set ***********/
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCards() {
        return cards;
    }

    public void setCards(String cards) {
        this.cards = cards;
    }

}
