package com.leyou.item.api;

import com.leyou.item.mydo.SpuDo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.pojo.ResultPage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface GoodsApi {

    /**
     * 根据条件分页查询商品
     *
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("spu/page")
    public ResultPage<SpuDo> findPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows
    );


    /**
     * 根据spu的id查询对应的spuDetail信息
     * @param id
     * @return
     */
    @GetMapping("spu/detail/{id}")
    public SpuDetail findSpuDetailBySpuId(@PathVariable(value = "id") Long id);

    /**
     * 根据spu的Id查询sku
     * @param id
     * @return
     */
    @GetMapping("sku/list")
    public List<Sku> findSkuBySpuId(@RequestParam(value = "id") Long id);

    /**
     * 根据id查询spu
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public Spu findSpuById(@PathVariable(value = "id") Long id);

    /**
     * 根据skuId查询sku
     * @param skuId
     * @return
     */
    @GetMapping("sku/{skuId}")
    public Sku findSkuBySkuId(@PathVariable("skuId") Long skuId);

}
