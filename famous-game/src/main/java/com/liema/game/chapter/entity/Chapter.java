package com.liema.game.chapter.entity;

import com.liema.common.bean.ChapterBean;
import com.liema.common.db.pojo.GeneralBean;
import com.liema.common.global.Misc;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document(collection = "famous-game-chapter")
public class Chapter extends GeneralBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public Chapter(Long rid, Map<Integer, ChapterBean> chapters) {
        this.rid = rid;
        this.chapters = chapters;
    }

    private Long rid;

    private Map<Integer, ChapterBean> chapters;

}
