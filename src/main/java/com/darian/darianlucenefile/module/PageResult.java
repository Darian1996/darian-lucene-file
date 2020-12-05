
package com.darian.darianlucenefile.module;

import java.util.List;

public class PageResult<T> extends PublicResult {

    private List<T> lists;

    /**
     * 当前页数
     */
    private int pageNum;

    /**
     * 每页的条数
     */
    private int pageSize;

    /**
     * 总条数
     */
    private int total;

    /**
     * 总页数
     */
    private int pages;

    public List<T> getLists() {
        return lists;
    }

    public void setLists(List<T> lists) {
        this.lists = lists;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}