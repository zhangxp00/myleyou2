package com.leyou.item.service;

import com.leyou.item.pojo.Category;

import java.util.List;

public interface CategoryService {
    /**
     * 根据节点id查询同一级别的节点
     * @param pid
     * @return
     */
    public List<Category> findByPid(Long pid);

    /**
     * 根据多个is查询多个分类信息
     * @param ids
     * @return
     */
    public List<Category> findByIds(List<Long> ids);

    /**
     * 根据多个is查询多个分类信息的名字
     * @param ids
     * @return
     */
    public List<String> findNameByIds(List<Long> ids);
}
