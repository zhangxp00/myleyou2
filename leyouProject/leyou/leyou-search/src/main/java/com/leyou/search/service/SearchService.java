package com.leyou.search.service;

import com.leyou.search.pojo.SearchParam;
import com.leyou.search.pojo.SearchResult;

public interface SearchService {

    /**
     * 根据搜索条件查询结果集
     * @param searchParam
     * @return
     */
    public SearchResult searchPage(SearchParam searchParam);

    /**
     * 添加search或者更新
     * @param id
     */
    void saveOrUpdate(Long id);
}
