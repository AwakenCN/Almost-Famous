package com.noseparte.common.resources;

/**
 *
 */
public class SchoolInitConf implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

/********** attribute ***********/
    private Integer id;

    private Integer occupation;

    private Integer isLock;

    private Integer defaultWeapon;

    private String cardDeck;

    private String deckName;

    private String checkCard;

    private Integer strength;

    private Integer agility;

    private Integer intelligence;

/********** constructors ***********/
    public SchoolInitConf() {

    }

    public SchoolInitConf(Integer id, Integer occupation, Integer isLock, Integer defaultWeapon, String cardDeck, String deckName, String checkCard, Integer strength, Integer agility, Integer intelligence) {
        this.id = id;
        this.occupation = occupation;
        this.isLock = isLock;
        this.defaultWeapon = defaultWeapon;
        this.cardDeck = cardDeck;
        this.deckName = deckName;
        this.checkCard = checkCard;
        this.strength = strength;
        this.agility = agility;
        this.intelligence = intelligence;
    }

/********** get/set ***********/
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOccupation() {
        return occupation;
    }

    public void setOccupation(Integer occupation) {
        this.occupation = occupation;
    }

    public Integer getIsLock() {
        return isLock;
    }

    public void setIsLock(Integer isLock) {
        this.isLock = isLock;
    }

    public Integer getDefaultWeapon() {
        return defaultWeapon;
    }

    public void setDefaultWeapon(Integer defaultWeapon) {
        this.defaultWeapon = defaultWeapon;
    }

    public String getCardDeck() {
        return cardDeck;
    }

    public void setCardDeck(String cardDeck) {
        this.cardDeck = cardDeck;
    }

    public String getDeckName() {
        return deckName;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public String getCheckCard() {
        return checkCard;
    }

    public void setCheckCard(String checkCard) {
        this.checkCard = checkCard;
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public Integer getAgility() {
        return agility;
    }

    public void setAgility(Integer agility) {
        this.agility = agility;
    }

    public Integer getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(Integer intelligence) {
        this.intelligence = intelligence;
    }

}
