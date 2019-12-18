package com.noseparte.robot.enitty;

import com.noseparte.common.bean.ChapterBean;
import com.noseparte.common.db.pojo.GeneralBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Map;

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
