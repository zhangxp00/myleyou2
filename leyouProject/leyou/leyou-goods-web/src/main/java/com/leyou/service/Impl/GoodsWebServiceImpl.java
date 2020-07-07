package com.leyou.service.Impl;

import com.leyou.feign.*;
import com.leyou.item.pojo.*;
import com.leyou.service.GoodsWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class GoodsWebServiceImpl implements GoodsWebService{

    @Autowired
    private CategoryFeign categoryFeign;

    @Autowired
    private BrandFeign brandFeign;

    @Autowired
    private SpecParamFeign specParamFeign;

    @Autowired
    private GoodsFeign goodsFeign;

    @Autowired
    private GroupFeign groupFeign;

    @Override
    public Map<String, Object> goodWebParam(Long id) {
        //获取spu的信息
        Spu spu = goodsFeign.findSpuById(id);
        //获取分类的信息
        List<Long> cids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
        List<String> categorys = categoryFeign.findNameByIds(cids);
        List<Map<String,Object>> categoryList=new ArrayList<>();
        for (int i = 0; i < categorys.size(); i++) {
            Map<String,Object> categoryMap=new HashMap<>();
            categoryMap.put("id",cids.get(i));
            categoryMap.put("name",categorys.get(i));
            categoryList.add(categoryMap);
        }
        //获取品牌的信息
        Brand brand = brandFeign.findByBid(spu.getBrandId());
        //获取skus
        List<Sku> skus = goodsFeign.findSkuBySpuId(spu.getId());
        //获取spuDetail
        SpuDetail spuDetail = goodsFeign.findSpuDetailBySpuId(spu.getId());
        //获取组以及组元素
        List<SpecGroup> groupAndParam = groupFeign.findGroupAndParamById(spu.getCid3());
        //获取特殊的规格参数名称以及id
        List<SpecParam> specParams = specParamFeign.findByGid(null, spu.getCid3(), false, null);
        Map<String, String> specParamMap = new HashMap<>();
        specParams.forEach(specParam -> {
            specParamMap.put(specParam.getId().toString(),specParam.getName());
        });
        //获取该分类的所有通用规格参数
        List<SpecParam> genericParams = specParamFeign.findByGid(null, spu.getCid3(), true, null);
        //组装数据源
        Map<String,Object> goodsWebMap=new HashMap<>();
        goodsWebMap.put("spu",spu);
        goodsWebMap.put("categoryList",categoryList);
        goodsWebMap.put("brand",brand);
        goodsWebMap.put("skus",skus);
        goodsWebMap.put("spuDetail",spuDetail);
        goodsWebMap.put("groupAndParam",groupAndParam);
        goodsWebMap.put("specParamMap",specParamMap);
        goodsWebMap.put("genericParams",genericParams);
        return goodsWebMap;
    }
}
