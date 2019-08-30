package com.liema.game.chapter.entity;

import com.liema.common.bean.ChapterBean;
import com.liema.common.global.Misc;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author liang
 * @since 2019-06-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
public class Chapter implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long rid;

    public Chapter(Long rid, String json) {
        this.rid = rid;
        this.chapters = Misc.parseToMap(json, Integer.class, ChapterBean.class);
    }

    private Map<Integer, ChapterBean> chapters;

}
