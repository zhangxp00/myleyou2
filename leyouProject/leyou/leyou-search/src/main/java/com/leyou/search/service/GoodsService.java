package com.leyou.search.service;

import com.leyou.item.pojo.Spu;
import com.leyou.search.pojo.Goods;

public interface GoodsService {
    /**
     *根据一个spu得到一个Goods对象
     * @param spu
     * @return
     */
    public Goods getGoodsBySpu(Spu spu);
}
