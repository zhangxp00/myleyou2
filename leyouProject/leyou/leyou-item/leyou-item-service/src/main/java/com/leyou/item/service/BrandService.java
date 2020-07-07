package com.leyou.item.service;

import com.leyou.item.pojo.Brand;
import com.leyou.pojo.ResultPage;

import java.util.List;

public interface BrandService {

    /**
     * 根据条件分页查询品牌
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    public ResultPage<Brand> findPage(String key,Integer page,Integer rows,String sortBy,Boolean desc);

    /**
     * 添加商品品牌
     * @param brand
     * @param cids
     * @return
     */
    public void saveBrand(Brand brand, List<Long> cids);

    /**
     * 根据分类id查询对应的品牌信息
     * @param cid
     * @return
     */
    public List<Brand> findByCid(Long cid);

    /**
     * 根据品牌id查询品牌
     * @param bid
     * @return
     */
    Brand findByBid(Long bid);
}
