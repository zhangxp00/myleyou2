package com.leyou.search.pojo;

import java.util.Map;

public class SearchParam {
    private String key;
    private Integer pageNum;

    private Map<String, String> searchfilter;

    private static final Integer DEFAULT_ROWS = 20;
    private static final Integer DEFAULT_PAGE = 1;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPageNum() {
        if (pageNum == null) {
            return DEFAULT_PAGE;
        }
        //Math.max返回两个数中最大的数
        return Math.max(DEFAULT_PAGE, pageNum);
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getSize() {
        return DEFAULT_ROWS;
    }

    public Map<String, String> getSearchfilter() {
        return searchfilter;
    }

    public void setSearchfilter(Map<String, String> searchfilter) {
        this.searchfilter = searchfilter;
    }
}
