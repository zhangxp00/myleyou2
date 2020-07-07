package com.leyou.item.controller;

import com.leyou.item.mydo.SpuDo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.GoodsService;
import com.leyou.pojo.ResultPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GoodsController {

    @Autowired
    private GoodsService goodsServiceImpl;

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
    public ResponseEntity<ResultPage<SpuDo>> findPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows
    ) {
        ResultPage<SpuDo> resultPage = goodsServiceImpl.findPage(key, saleable, page, rows);
        if (resultPage == null || CollectionUtils.isEmpty(resultPage.getItems())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultPage);
    }

    /**
     * 添加商品信息
     *
     * @param spuDo
     */
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuDo spuDo) {
        goodsServiceImpl.saveGoods(spuDo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据spu的id查询对应的spuDetail信息
     *
     * @param id
     * @return
     */
    @GetMapping("spu/detail/{id}")
    public ResponseEntity<SpuDetail> findSpuDetailBySpuId(@PathVariable(value = "id") Long id) {
        SpuDetail spuDetail = goodsServiceImpl.findSpuDetailBySpuId(id);
        if (spuDetail == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spuDetail);
    }

    /**
     * 根据spu的Id查询sku
     *
     * @param id
     * @return
     */
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> findSkuBySpuId(@RequestParam(value = "id") Long id) {
        List<Sku> skus = goodsServiceImpl.findSkuBySpuId(id);
        if (CollectionUtils.isEmpty(skus)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(skus);
    }

    /**
     * 更新商品信息
     *
     * @param spuDo
     */
    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuDo spuDo) {
        goodsServiceImpl.updateGoods(spuDo);
        //响应204状态码
        return ResponseEntity.noContent().build();
    }

    /**
     * 根据id查询spu
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<Spu> findSpuById(@PathVariable(value = "id") Long id) {
        Spu spu = goodsServiceImpl.findSpuById(id);
        if (spu == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spu);
    }

    /**
     * 根据skuId查询sku
     * @param skuId
     * @return
     */
    @GetMapping("sku/{skuId}")
    public ResponseEntity<Sku> findSkuBySkuId(@PathVariable("skuId") Long skuId) {
        Sku sku = goodsServiceImpl.findSkuBySkuId(skuId);
        if (sku == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(sku);
    }

}
