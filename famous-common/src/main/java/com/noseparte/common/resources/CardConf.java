package com.noseparte.common.resources;

/**
 *
 */
public class CardConf implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

/********** attribute ***********/
    private Integer id;

    private String name;

    private Integer vocation;

    private Integer type;

    private Integer cost;

    private Integer quality;

    private Integer level;

    private Integer sale;

    private Integer buy;

    private Integer attack;

    private Integer range;

    private Integer rangeType;

    private Integer durability;

    private String magicRange;

    private Integer damage;

    private Integer damageType;

    private String module;

    private String addCards;

    private String subsidiaryCards;

    private Integer lvUpExp;

    private Integer disenchantExp;

    private Integer lvUpID;

    private Integer upgradeID;

    private String draw;

    private String play;

    private String playCondition;

    private Integer death;

    private String discard;

    private String deathRattle;

    private String init;

    private String keyWord;

/********** constructors ***********/
    public CardConf() {

    }

    public CardConf(Integer id, String name, Integer vocation, Integer type, Integer cost, Integer quality, Integer level, Integer sale, Integer buy, Integer attack, Integer range, Integer rangeType, Integer durability, String magicRange, Integer damage, Integer damageType, String module, String addCards, String subsidiaryCards, Integer lvUpExp, Integer disenchantExp, Integer lvUpID, Integer upgradeID, String draw, String play, String playCondition, Integer death, String discard, String deathRattle, String init, String keyWord) {
        this.id = id;
        this.name = name;
        this.vocation = vocation;
        this.type = type;
        this.cost = cost;
        this.quality = quality;
        this.level = level;
        this.sale = sale;
        this.buy = buy;
        this.attack = attack;
        this.range = range;
        this.rangeType = rangeType;
        this.durability = durability;
        this.magicRange = magicRange;
        this.damage = damage;
        this.damageType = damageType;
        this.module = module;
        this.addCards = addCards;
        this.subsidiaryCards = subsidiaryCards;
        this.lvUpExp = lvUpExp;
        this.disenchantExp = disenchantExp;
        this.lvUpID = lvUpID;
        this.upgradeID = upgradeID;
        this.draw = draw;
        this.play = play;
        this.playCondition = playCondition;
        this.death = death;
        this.discard = discard;
        this.deathRattle = deathRattle;
        this.init = init;
        this.keyWord = keyWord;
    }

/********** get/set ***********/
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVocation() {
        return vocation;
    }

    public void setVocation(Integer vocation) {
        this.vocation = vocation;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getSale() {
        return sale;
    }

    public void setSale(Integer sale) {
        this.sale = sale;
    }

    public Integer getBuy() {
        return buy;
    }

    public void setBuy(Integer buy) {
        this.buy = buy;
    }

    public Integer getAttack() {
        return attack;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    public Integer getRange() {
        return range;
    }

    public void setRange(Integer range) {
        this.range = range;
    }

    public Integer getRangeType() {
        return rangeType;
    }

    public void setRangeType(Integer rangeType) {
        this.rangeType = rangeType;
    }

    public Integer getDurability() {
        return durability;
    }

    public void setDurability(Integer durability) {
        this.durability = durability;
    }

    public String getMagicRange() {
        return magicRange;
    }

    public void setMagicRange(String magicRange) {
        this.magicRange = magicRange;
    }

    public Integer getDamage() {
        return damage;
    }

    public void setDamage(Integer damage) {
        this.damage = damage;
    }

    public Integer getDamageType() {
        return damageType;
    }

    public void setDamageType(Integer damageType) {
        this.damageType = damageType;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getAddCards() {
        return addCards;
    }

    public void setAddCards(String addCards) {
        this.addCards = addCards;
    }

    public String getSubsidiaryCards() {
        return subsidiaryCards;
    }

    public void setSubsidiaryCards(String subsidiaryCards) {
        this.subsidiaryCards = subsidiaryCards;
    }

    public Integer getLvUpExp() {
        return lvUpExp;
    }

    public void setLvUpExp(Integer lvUpExp) {
        this.lvUpExp = lvUpExp;
    }

    public Integer getDisenchantExp() {
        return disenchantExp;
    }

    public void setDisenchantExp(Integer disenchantExp) {
        this.disenchantExp = disenchantExp;
    }

    public Integer getLvUpID() {
        return lvUpID;
    }

    public void setLvUpID(Integer lvUpID) {
        this.lvUpID = lvUpID;
    }

    public Integer getUpgradeID() {
        return upgradeID;
    }

    public void setUpgradeID(Integer upgradeID) {
        this.upgradeID = upgradeID;
    }

    public String getDraw() {
        return draw;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }

    public String getPlay() {
        return play;
    }

    public void setPlay(String play) {
        this.play = play;
    }

    public String getPlayCondition() {
        return playCondition;
    }

    public void setPlayCondition(String playCondition) {
        this.playCondition = playCondition;
    }

    public Integer getDeath() {
        return death;
    }

    public void setDeath(Integer death) {
        this.death = death;
    }

    public String getDiscard() {
        return discard;
    }

    public void setDiscard(String discard) {
        this.discard = discard;
    }

    public String getDeathRattle() {
        return deathRattle;
    }

    public void setDeathRattle(String deathRattle) {
        this.deathRattle = deathRattle;
    }

    public String getInit() {
        return init;
    }

    public void setInit(String init) {
        this.init = init;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

}
