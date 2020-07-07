package com.leyou.item.service;

import com.leyou.item.mydo.SpuDo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.pojo.ResultPage;

import java.util.List;

public interface GoodsService {
    /**
     * 根据条件分页查询商品
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    public ResultPage<SpuDo> findPage(String key, Boolean saleable, Integer page, Integer rows);

    /**
     * 添加商品信息
     * @param spuDo
     */
    void saveGoods(SpuDo spuDo);

    /**
     * 根据spu的id查询对应的spuDetail信息
     * @param id
     * @return
     */
    SpuDetail findSpuDetailBySpuId(Long id);

    /**
     * 根据spu的Id查询sku
     * @param id
     * @return
     */
    List<Sku> findSkuBySpuId(Long id);

    /**
     * 更新商品信息
     * @param spuDo
     */
    void updateGoods(SpuDo spuDo);

    /**
     * 根据id查询spu
     * @param id
     * @return
     */
    Spu findSpuById(Long id);

    /**
     * 根据skuId查询sku
     * @param skuId
     * @return
     */
    Sku findSkuBySkuId(Long skuId);
}
