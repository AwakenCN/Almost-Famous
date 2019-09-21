package com.noseparte.common.bean;

import lombok.Data;

@Data
public class ChapterBean implements Comparable<ChapterBean> {

    int chapterId;
    StateCode state;
    long beginTime;
    long endTime;

    @Override
    public int compareTo(ChapterBean o) {
        return chapterId < o.getChapterId() ? -1 : (chapterId == o.getChapterId()) ? 0 : 1;
    }


}
