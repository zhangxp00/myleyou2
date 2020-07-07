package com.leyou.item.service;

import com.leyou.item.pojo.SpecParam;
import java.util.List;

public interface SpecParamService {
    /**
     * 根据不同参数查询对应的   规格参数的详细参数信息
     * @param gid
     * @param cid
     * @param generic
     * @param searching
     * @return
     */
    public List<SpecParam> findByParam(Long gid, Long cid, Boolean generic, Boolean searching);
}
