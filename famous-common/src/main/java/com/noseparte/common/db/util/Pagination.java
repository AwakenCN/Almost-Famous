package com.noseparte.common.db.util;

import java.util.Arrays;
import java.util.List;

/**
 * 分页数据类
 *
 * @author hk
 */
public class Pagination<T> {

    // 导航页码数
    private int navigatePages = 5;
    // 所有导航页号
    private int[] navigatepageNums;

    public int[] getNavigatepageNums() {
        return navigatepageNums;
    }

    public void setNavigatepageNums(int[] navigatepageNums) {
        this.navigatepageNums = navigatepageNums;
    }

    /**
     * 一页数据默认10条
     */
    private int pageSize = 10;
    /**
     * 当前页码
     */
    private int pageNo = 1;

    /**
     * 上一页
     */
    private int upPage;

    /**
     * 下一页
     */
    private int nextPage;
    /**
     * 一共有多少条数据
     */
    private long totalCount;

    /**
     * 一共有多少页
     */
    private int totalPage;
    /**
     * 数据集合
     */
    private List<T> datas;

    /**
     * 分页的url
     */
    private String pageUrl;

    private boolean hasPreviousPage;

    private boolean hasNextPage;

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    /**
     * 判定页面边界
     */
    private void judgePageBoudary() {
        hasPreviousPage = pageNo > 1;
        hasNextPage = pageNo < totalPage;
    }

    /**
     * 计算导航页
     */
    private void calcNavigatepageNums() {
        // 当总页数小于或等于导航页码数时
        if (totalPage <= navigatePages) {
            navigatepageNums = new int[totalPage];
            for (int i = 0; i < totalPage; i++) {
                navigatepageNums[i] = i + 1;
            }
        } else { // 当总页数大于导航页码数时
            navigatepageNums = new int[navigatePages];
            int startNum = pageNo - navigatePages / 2;
            int endNum = pageNo + navigatePages / 2;

            if (startNum < 1) {
                startNum = 1;
                // (最前navigatePages页
                for (int i = 0; i < navigatePages; i++) {
                    navigatepageNums[i] = startNum++;
                }
            } else if (endNum > totalPage) {
                endNum = totalPage;
                // 最后navigatePages页
                for (int i = navigatePages - 1; i >= 0; i--) {
                    navigatepageNums[i] = endNum--;
                }
            } else {
                // 所有中间页
                for (int i = 0; i < navigatePages; i++) {
                    navigatepageNums[i] = startNum++;
                }
            }
        }
    }

    /**
     * 获取第一条记录位置
     *
     * @return
     */
    public int getFirstResult() {
        return (this.getPageNo() - 1) * this.getPageSize();
    }

    /**
     * 获取最后记录位置
     *
     * @return
     */
    public int getLastResult() {
        return this.getPageNo() * this.getPageSize();
    }

    /**
     * 计算一共多少页
     */
    public void setTotalPage() {
        this.totalPage = (int) ((this.totalCount % this.pageSize > 0) ? (this.totalCount
                / this.pageSize + 1)
                : this.totalCount / this.pageSize);
    }

    /**
     * 设置 上一页
     */
    public void setUpPage() {
        this.upPage = (this.pageNo > 1) ? this.pageNo - 1 : this.pageNo;
    }

    /**
     * 设置下一页
     */
    public void setNextPage() {
        this.nextPage = (this.pageNo == this.totalPage) ? this.pageNo
                : this.pageNo + 1;
    }

    public int getNextPage() {
        return nextPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getUpPage() {
        return upPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount2) {
        this.totalCount = totalCount2;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public Pagination(int pageNo, int pageSize, long totalCount2) {
        this.setPageNo(pageNo);
        this.setPageSize(pageSize);
        this.setTotalCount(totalCount2);
        this.init();
    }

    public Pagination() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return "Pagination [navigatePages=" + navigatePages
                + ", navigatepageNums=" + Arrays.toString(navigatepageNums)
                + ", pageSize=" + pageSize + ", pageNo=" + pageNo + ", upPage="
                + upPage + ", nextPage=" + nextPage + ", totalCount="
                + totalCount + ", totalPage=" + totalPage + ", datas=" + datas
                + ", pageUrl=" + pageUrl + ", hasPreviousPage="
                + hasPreviousPage + ", hasNextPage=" + hasNextPage + "]";
    }

    /**
     * 初始化计算分页
     */
    private void init() {
        this.setTotalPage();// 设置一共页数
        this.setUpPage();// 设置上一页
        this.setNextPage();// 设置下一页
        this.judgePageBoudary();// 是否有上一页和下一页
        this.calcNavigatepageNums();// 计算页数
    }
}
