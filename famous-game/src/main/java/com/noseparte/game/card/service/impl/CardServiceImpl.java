package com.noseparte.game.card.service.impl;

import com.noseparte.common.bean.AttrCode;
import com.noseparte.common.bean.CardBean;
import com.noseparte.common.bean.EventCode;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.ConfigManager;
import com.noseparte.common.global.Misc;
import com.noseparte.common.resources.CardConf;
import com.noseparte.common.resources.GlobalVariableConf;
import com.noseparte.game.card.entity.Card;
import com.noseparte.game.card.mongo.CardDao;
import com.noseparte.game.card.service.CardService;
import com.noseparte.game.role.entity.Role;
import com.noseparte.game.role.service.RoleService;
import com.noseparte.game.school.service.SchoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 卡包 服务实现类
 * </p>
 *
 * @author liang
 * @since 2019-03-18
 */
@Slf4j
@Service
public class CardServiceImpl implements CardService {

    @Resource
    RoleService roleService;
    @Autowired
    private SchoolService schoolService;
    @Resource
    private CardDao cardDao;

    @Override
    public void initCard(Long rid) {
        Card Card = cardDao.getCardById(rid);
        if (null != Card) {
            return;
        }
        Card = new Card();
        Card.setRid(rid);
        Card.setCards(new HashMap<>());
        cardDao.addCard(Card);
        GlobalVariableConf globalVariable = ConfigManager.globalVariableConfMap.get(1023);
        if (globalVariable != null) {
            String[] cards = Misc.split(globalVariable.getValue(), "\\;");
            for (String card : cards) {
                String[] cardBeans = Misc.split(card, "\\,");
                this.addCard(Card, Integer.parseInt(cardBeans[0]), Integer.parseInt(cardBeans[1]));
            }
            cardDao.updateCard(Card);
        }
    }

    public boolean addCard(Card Card, Integer cardId, Integer num) {
        if (null == Card || num <= 0) {
            return false;
        }
        Map<Integer, CardBean> cards = Card.getCards();
        if (cards.containsKey(cardId)) {
            CardBean cardBean = cards.get(cardId);
            cardBean.setNum(cardBean.getNum() + num);
        } else {
            CardBean cardBean = new CardBean();
            cardBean.setCardId(cardId);
            cardBean.setNum(num);
            cards.put(cardId, cardBean);
        }
        Card.setCards(cards);
        return true;
    }

    public boolean deleteCard(Card Card, Integer cardId, Integer num) {
        if (null == Card || num <= 0) {
            return false;
        }
        Map<Integer, CardBean> cards = Card.getCards();
        if (cards.containsKey(cardId)) {
            CardBean cardBean = cards.get(cardId);
            int currNum = cardBean.getNum();
            currNum -= num;
            if (currNum < 0) {
                return false;
            }
            if (currNum > 0) {
                cardBean.setNum(currNum);
            } else if (currNum == 0) {
                cards.remove(cardId);
            }
            return true;
        }
        return false;
    }

    @Override
    public ErrorCode buy(Long rid, List<CardBean> cards) {
        if (null == cards || cards.size() == 0) {
            return ErrorCode.CLIENT_PARAMS_ERROR;
        }

        Card Card = cardDao.getCardById(rid);
        Role role = roleService.selectByRoleId(rid);
        int sum = 0;

        for (CardBean card : cards) {
            int cardId = card.getCardId();
            int num = card.getNum();
            boolean isOk = addCard(Card, cardId, num);
            if (!isOk) {
                return ErrorCode.SERVER_ERROR;
            }
            CardConf cardConf = ConfigManager.cardConfMap.get(cardId);
            int value = cardConf.getBuy() * num;
            int result = roleService.cost(role, AttrCode.SILVER, -value, EventCode.CARD_BUY);
            sum += num;
            if (result <= 0) {
                return ErrorCode.SERVER_ERROR;
            }
        }
        Integer buyCnt = Misc.increase(Card.getBuyCnt(), sum);
        Card.setBuyCnt(buyCnt);
        roleService.updateByRoleId(role);
        cardDao.updateCard(Card);

        //验证卡组
        ErrorCode errorCode = schoolService.verifyCardGroup(rid);
        if (errorCode != ErrorCode.SERVER_SUCCESS) {
            return errorCode;
        }

        return ErrorCode.SERVER_SUCCESS;
    }

    @Override
    public ErrorCode sale(Long rid, List<CardBean> cards) {
        if (null == cards || cards.size() == 0) {
            return ErrorCode.SERVER_ERROR;
        }

        Card Card = cardDao.getCardById(rid);
        Role role = roleService.selectByRoleId(rid);
        for (CardBean card : cards) {
            int cardId = card.getCardId();
            int num = card.getNum();
            boolean isOk = deleteCard(Card, card.getCardId(), card.getNum());
            if (!isOk) {
                return ErrorCode.SERVER_ERROR;
            }
            CardConf cardConf = ConfigManager.cardConfMap.get(cardId);
            int value = cardConf.getSale() * num;
            int result = roleService.add(role, AttrCode.SILVER, value, EventCode.CARD_SALE);
            if (result <= 0) {
                return ErrorCode.SERVER_ERROR;
            }
        }
        roleService.updateByRoleId(role);
        cardDao.updateCard(Card);

        //验证卡组
        ErrorCode errorCode = schoolService.verifyCardGroup(rid);
        if (errorCode != ErrorCode.SERVER_SUCCESS) {
            return errorCode;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("rid", rid);
        data.put("attrId", AttrCode.SILVER.value());
        data.put("attrVal", role.getSilver());
        data.put("cards", cards);

        return ErrorCode.SERVER_SUCCESS;
    }

    @Override
    public Card getCardById(Long rid) {
        return cardDao.getCardById(rid);
    }

    @Override
    public boolean lock(Long rid, List<Integer> cardIds, Integer state) {
        if (null == cardIds || cardIds.size() == 0) {
            return false;
        }
        Card Card = cardDao.getCardById(rid);
        for (Integer cardId : cardIds) {
            CardBean cardBean = Card.getCards().get(cardId);
            if (null == cardBean) {
                log.error("未找到锁定的卡牌cardId = {}", cardId);
                return false;
            }
            cardBean.setClock(state);
        }
        cardDao.updateCard(Card);
        return true;
    }

    @Override
    public void updateCard(Card Card) {
        cardDao.updateCard(Card);
    }

    @Override
    public ErrorCode storeCardPackage(Card card, List<CardBean> cards) {
        for (CardBean bean : cards) {
            addCard(card, bean.getCardId(), bean.getNum());
        }
        cardDao.updateCard(card);
        return ErrorCode.SERVER_SUCCESS;
    }


}
