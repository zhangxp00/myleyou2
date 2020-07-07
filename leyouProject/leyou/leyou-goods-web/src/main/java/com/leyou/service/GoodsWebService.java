package com.leyou.service;

import java.util.Map;

public interface GoodsWebService {

    /**
     * 根据spuId组装详情页需要的数据模型
     * @param id
     * @return
     */
    public Map<String,Object> goodWebParam(Long id);
}
