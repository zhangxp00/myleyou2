package com.leyou.item.controller;

import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import com.leyou.pojo.ResultPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandServiceImpl;

    /**
     * 品牌分页查询
     *
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<ResultPage<Brand>> findPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", required = false) Boolean desc
    ) {
        ResultPage<Brand> resultPage = brandServiceImpl.findPage(key, page, rows, sortBy, desc);
        if (CollectionUtils.isEmpty(resultPage.getItems())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultPage);
    }

    /**
     * 添加商品品牌
     *
     * @param brand
     * @param cids
     * @return
     */
    @PostMapping
    private ResponseEntity<Void> saveBrand(Brand brand, @RequestParam(value = "cids") List<Long> cids) {
        brandServiceImpl.saveBrand(brand, cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> findByCid(@PathVariable(value = "cid") Long cid) {
        List<Brand> brands = brandServiceImpl.findByCid(cid);
        if (CollectionUtils.isEmpty(brands)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brands);
    }

    /**
     * 根据品牌id查询品牌
     * @param bid
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<Brand> findByBid(@PathVariable(value = "id") Long bid) {
        Brand brand = brandServiceImpl.findByBid(bid);
        if (brand == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brand);
    }
}
