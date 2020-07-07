package com.leyou.item.service;

import com.leyou.item.pojo.SpecGroup;

import java.util.List;

public interface SpecGroupService {
    /**
     * 根据商品的分类id查询对应的  规格参数组信息
     * @param cid
     * @return
     */
    public List<SpecGroup> findByCid(Long cid);

    /**
     * 根据cid查询对应的组以及组内的信息
     * @param cid
     * @return
     */
    List<SpecGroup> findGroupAndParamById(Long cid);
}
